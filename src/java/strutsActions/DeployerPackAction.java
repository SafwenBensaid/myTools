/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package strutsActions;

import dto.EnvironnementDTO;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import tools.Configuration;
import dto.DetailsLivraisonDTO;
import dto.TripleDTO;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import t24Scripts.T24Scripts;
import tools.DeploiementParalleleTools;
import tools.Tools;

/**
 *
 * @author 04486
 */
public class DeployerPackAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, InterruptedException {

        String connectedUser = null;
        try {
            connectedUser = Tools.getConnectedLogin();
            servlets.AfficherMessageEtatAvancement.executionEnCours.put(connectedUser, true);
            String nomEnvTestDep = null;
            EnvironnementDTO envirTestDep = null;
            String resultatDeploiement;
            Configuration.initialisation();
            DetailsLivraisonDTO detailsLivraison = Configuration.usersDetailsLivraisonsMap.get(connectedUser);

            if (detailsLivraison.getCircuit().equals("RELEASE")) {
                nomEnvTestDep = "ASS";
            } else if (detailsLivraison.getCircuit().equals("PROJET")) {
                nomEnvTestDep = "ASS2";
            } else if (detailsLivraison.getCircuit().equals("HOTFIX")) {
                nomEnvTestDep = "ASS";
            } else if (detailsLivraison.getCircuit().equals("UPGRADE")) {
                nomEnvTestDep = "ASSU";
            }
            envirTestDep = Configuration.environnementDTOMapUserHasEnvironnement.get(connectedUser).get(nomEnvTestDep);
            int nbrIterationDeploiement = detailsLivraison.getNbrIterationDeploiement();
            servlets.AfficherMessageEtatAvancement.deploiementEnCours.get(connectedUser).put(envirTestDep.getNom(), true);
            //pack simple
            String[] packsNamesList = new String[]{"TAF-" + detailsLivraison.getPackName()};
            String[] mnemonicList = new String[]{detailsLivraison.getMnemonicCompany()};
            String[] nbrIterList = new String[]{String.valueOf(nbrIterationDeploiement)};
            List<TripleDTO> packName_companyMnemonic_nbrIter_liste = DeploiementParalleleTools.genererDtoList(packsNamesList, mnemonicList, nbrIterList);
            //pack BNK
            String[] packsNamesListBNK = new String[]{"TAF-" + detailsLivraison.getPackName() + ".BNK"};
            String[] mnemonicListBNK = new String[]{"BNK"};
            List<TripleDTO> packName_companyMnemonic_nbrIter_listeBNK = DeploiementParalleleTools.genererDtoList(packsNamesListBNK, mnemonicListBNK, nbrIterList);
            //pack TN1
            String[] packsNamesListTN1 = new String[]{"TAF-" + detailsLivraison.getPackName() + ".TN1"};
            String[] mnemonicListTN1 = new String[]{"TN1"};
            List<TripleDTO> packName_companyMnemonic_nbrIter_listeTN1 = DeploiementParalleleTools.genererDtoList(packsNamesListTN1, mnemonicListTN1, nbrIterList);

            /*
             if (detailsLivraison.isCusExists() == false) {
             resultatDeploiement = new T24Scripts().deployerPack(connectedUser, envirTestDep, scriptDeploiement, detailsLivraison.getPackName(), detailsLivraison.getMnemonicCompany(), nbrIterationDeploiement);
             } else {
             resultatDeploiement = "<b>PACK: " + detailsLivraison.getPackName() + ".BNK</b><br>";
             resultatDeploiement += new T24Scripts().deployerPack(connectedUser, envirTestDep, scriptDeploiement, detailsLivraison.getPackName() + ".BNK", "BNK", nbrIterationDeploiement);
             resultatDeploiement += "%_%";
             resultatDeploiement += "<b>PACK: " + detailsLivraison.getPackName() + ".TN1</b><br>";
             resultatDeploiement += new T24Scripts().deployerPack(connectedUser, envirTestDep, scriptDeploiement, detailsLivraison.getPackName() + ".TN1", detailsLivraison.getMnemonicCompany(), nbrIterationDeploiement);
             }
             */

            Map<String, String[]> resultMap = null;
            if (detailsLivraison.isCusExists() == false) {
                resultMap = new DeploiementParalleleTools().deploiementParalleleMultiPack(connectedUser, nomEnvTestDep, "PACK.TAF", "", packsNamesList, new String[]{nomEnvTestDep}, packName_companyMnemonic_nbrIter_liste, false, true);
                resultatDeploiement = analyseResultatDeploiement(resultMap);
            } else {
                resultMap = new DeploiementParalleleTools().deploiementParalleleMultiPack(connectedUser, nomEnvTestDep, "PACK.TAF", "", packsNamesListBNK, new String[]{nomEnvTestDep}, packName_companyMnemonic_nbrIter_listeBNK, false, true);
                resultatDeploiement = analyseResultatDeploiement(resultMap);
                resultMap = new DeploiementParalleleTools().deploiementParalleleMultiPack(connectedUser, nomEnvTestDep, "PACK.TAF", "", packsNamesListTN1, new String[]{nomEnvTestDep}, packName_companyMnemonic_nbrIter_listeTN1, false, true);
                resultatDeploiement += analyseResultatDeploiement(resultMap);
            }

            detailsLivraison.setResultatDeploiement(resultatDeploiement);
            Configuration.usersDetailsLivraisonsMap.put(connectedUser, detailsLivraison);
            servlets.AfficherMessageEtatAvancement.executionEnCours.put(connectedUser, false);
            servlets.AfficherMessageEtatAvancement.deploiementEnCours.get(connectedUser).put(envirTestDep.getNom(), false);
            servlets.AfficherMessageEtatAvancement.setLogmessage("", connectedUser);

            Configuration.resetDeploiementMap(connectedUser);

            response.setContentType("text/text;charset=utf-8");
            response.setHeader("cache-control", "no-cache");
            PrintWriter out = response.getWriter();
            out.println("PACK_DEPLOYED");
            out.flush();
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException("connectedUser:" + connectedUser + "|||\n" + servlets.AfficherMessageEtatAvancement.deploiementEnCours.keySet().toString() + "|||\n" + tools.Tools.getStackTrace(exep));
        }
        return mapping.findForward(null);
    }

    private String analyseResultatDeploiement(Map<String, String[]> resultMap) {
        StringBuilder resultatSb = new StringBuilder();
        if (resultMap.containsKey("PROBLEME") && resultMap.get("PROBLEME")[0].length() > 0 && resultMap.get("PROBLEME")[1].length() > 0) {
            //problème d'authentification, prob d'existance de pack ou ...
            String[] resTab = resultMap.get("PROBLEME");
            resultatSb.append("PROBLEME:").append(resTab[0]).append(resTab[1]);
        } else {
            resultatSb.append(resultMap.get("RESULTAT_HTML_COMPLET")[0]);
        }
        return resultatSb.toString();
    }
}
