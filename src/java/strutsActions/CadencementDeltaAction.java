/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package strutsActions;

import dataBaseTracRequests.DataBaseTracGenericRequests;
import dto.EnvironnementDTO;
import entitiesMysql.Cadencement;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import static strutsActions.CadencementDeltaAction.getCadencementGlobal;
import static strutsActions.CadencementDeltaAction.sortByComparator;
import static strutsActions.CadencementDeltaAction.transfertFtpFichierCadencement;
import strutsForms.CadencementDeltaForm;
import t24Scripts.T24Scripts;
import tools.Configuration;
import tools.DataBaseTools;
import tools.FtpTools;
import tools.Tools;

/**
 *
 * @author 04494
 */
public class CadencementDeltaAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, InterruptedException {
        try {
            String connectedUser = Tools.getConnectedLogin();
            servlets.AfficherMessageEtatAvancement.executionEnCours.put(connectedUser, true);
            CadencementDeltaForm inF = (CadencementDeltaForm) form;
            String packName = inF.getNomPack();
            servlets.AfficherMessageEtatAvancement.setLogmessage("Génération du cadencement Delta T24 à partir de l'export " + packName, connectedUser);

            //************test existance dossier de l'export sous /work 
            T24Scripts t24Scripts = new T24Scripts();
            EnvironnementDTO environnementSource = Configuration.environnementDTOMapUserHasEnvironnement.get(connectedUser).get("VERSIONNING");

            boolean packExists = false;
            String dossierDeBaseDuPack = "/work";
            packExists = t24Scripts.testExistanceDossier(environnementSource, packName, dossierDeBaseDuPack);
            if (packExists == false) {
                return Tools.redirectionPageErreurs("Dossier inéxistant", "Le dossier <b>" + dossierDeBaseDuPack + "/" + packName + "</b> n'existe pas sur l'environnement " + environnementSource.getNom(), mapping, request, response, connectedUser);
            }
            //fin test existance dossier de l'export sous /work

            //************extraire la liste des types T24 à partir de l'export
            Tools tools = new Tools();
            FtpTools ftpTools = new FtpTools();
            long millisecondsTime = System.currentTimeMillis();
            String pathDuPack = dossierDeBaseDuPack + "/" + packName;

            String resultatListerFichiers = t24Scripts.executerCommandeListEnvironnement(environnementSource, pathDuPack, "ls > ../OBJETSDELTAOV_" + millisecondsTime + ".txt");
            ftpTools.downloadFile(environnementSource, dossierDeBaseDuPack, "OBJETSDELTAOV_" + millisecondsTime + ".txt");

            String[] fileTab = tools.convertFileContentToTab(Configuration.parametresList.get("espaceLocal") + "/OBJETSDELTAOV_" + millisecondsTime + ".txt");
            System.out.println("__________________________");

            List<String> typesT24Deltalist = new LinkedList<>();
            for (int i = 0; i < fileTab.length; i++) {
                if ((fileTab[i].trim().length() > 0)) {
                    typesT24Deltalist.add(fileTab[i].trim());
                }
            }
            //fin extraire la liste des types T24 à partir de l'export  

            //********générer le cadencement du Delta à partir du cadencement global
            Map<String, Cadencement> cadencementGlobalMap = getCadencementGlobal();
            Map<String, Integer> cadencementDeltaMap = new LinkedHashMap<>();
            Integer orderMax = cadencementGlobalMap.size() + 1;
            for (String typeDelta : typesT24Deltalist) {
                if (cadencementGlobalMap.containsKey(typeDelta)) {
                    cadencementDeltaMap.put(typeDelta.concat("," + cadencementGlobalMap.get(typeDelta).getIteration()), cadencementGlobalMap.get(typeDelta).getOrdre());
                } else {
                    cadencementDeltaMap.put(typeDelta.concat(",1"), orderMax);
                }
            }

            Map<String, Integer> sortedCadencementDeltaMap = sortByComparator(cadencementDeltaMap);

            //affichage
            for (String type : sortedCadencementDeltaMap.keySet()) {
                System.out.println(type);
            }
            //fin générer le cadencement du Delta à partir du cadencement global

            //transférer fichier sous
            transfertFtpFichierCadencement(sortedCadencementDeltaMap, connectedUser);

            //envoyer le résultat en session à la page jsp
            request.getSession().setAttribute("sortedCadencementDeltaMap", sortedCadencementDeltaMap);
            request.getSession().setAttribute("packName", packName);


        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return mapping.findForward("ResultatCadencement");
    }

    public static void transfertFtpFichierCadencement(Map<String, Integer> sortedCadencementDeltaMap, String connectedUser) {
        List<String> commandeList = new ArrayList<String>();
        for (String type : sortedCadencementDeltaMap.keySet()) {
            if (type.trim().length() > 0) {
                commandeList.add(type);
            }
        }
        File file = null;
        Tools tools = new Tools();
        try {
            file = tools.createFile(Configuration.parametresList.get("espaceLocal") + System.getProperty("file.separator") + "CADENCEMENT_DELTA");
            tools.writeInFile(file, commandeList);
            FtpTools ftpTools = new FtpTools();
            EnvironnementDTO envVersionning = Configuration.environnementDTOMapUserHasEnvironnement.get(connectedUser).get("VERSIONNING");
            ftpTools.uploadFileToServerDirectory(connectedUser, file, "/DEPT24/DEPLOIEMENT_DELTA_AUTO", envVersionning, false, false);
        } catch (IOException exep) {
            exep.printStackTrace();
            Tools.traiterException(Tools.getStackTrace(exep));
        }
    }

    public static Map<String, Integer> sortByComparator(Map<String, Integer> unsortMap) {
        // Convert Map to List
        List<Map.Entry<String, Integer>> list = new LinkedList<>(unsortMap.entrySet());
        // Sort list with comparator, to compare the Map values
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1,
                    Map.Entry<String, Integer> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });
        // Convert sorted map back to a Map
        Map<String, Integer> sortedMap = new LinkedHashMap<>();
        for (Iterator<Map.Entry<String, Integer>> it = list.iterator(); it.hasNext();) {
            Map.Entry<String, Integer> entry = it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    public static Map<String, Cadencement> getCadencementGlobal() {
        String pu = "mysqlDataBasePU";
        String namedQuery = "Cadencement.findAll";
        List<Cadencement> cadencementGlobalList = new DataBaseTracGenericRequests<Cadencement>().getList_TYPE_OfnamedQuery(pu, namedQuery, null);
        Map<String, Cadencement> cadencementGlobalMap = new HashMap<>();
        try {
            for (Cadencement objet : cadencementGlobalList) {
                cadencementGlobalMap.put(objet.getType(), objet);
            }
        } catch (Exception ex) {
            Tools.traiterException(Tools.getStackTrace(ex));
        }
        return cadencementGlobalMap;
    }
}
