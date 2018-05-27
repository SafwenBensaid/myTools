/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package strutsActions;

import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import java.util.*;
import strutsForms.FormerUnPackForm;
import t24Scripts.PM;
import tools.Configuration;
import tools.FolderZiper;
import tools.FtpTools;
import tools.Tools;
import dto.VerificationDeltaChampsDTO;
import dto.EnvironnementDTO;
import t24Scripts.T24Scripts;

/**
 *
 * @author 04486
 */
public class FormerUnPackAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, InterruptedException {

        String connectedUser = Tools.getConnectedLogin();
        try {
            servlets.AfficherMessageEtatAvancement.executionEnCours.put(connectedUser, true);
            String fileName;
            String packName;
            String envName;
            String objectList;
            PM pm;
            int objectNumber;
            EnvironnementDTO environnement;

            Configuration.initialisation();

            FormerUnPackForm inF = (FormerUnPackForm) form;
            Tools tools = new Tools();

            packName = inF.getNomPack();
            fileName = packName.replace("TAF-", "") + ".txt";
            envName = inF.getEnvironnementSource();

            environnement = Configuration.environnementDTOMapUserHasEnvironnement.get(connectedUser).get(envName);


            //test d'authentification
            T24Scripts t24Scripts = new T24Scripts();
            Map<String, String> errorMap = new HashMap<String, String>();
            String resultatTestAuthentification;
            FtpTools ftpTools = new FtpTools();
            objectList = inF.getTextAreaObjectList();

            if (!envName.equals("VERSIONNING")) {
                resultatTestAuthentification = t24Scripts.testLoginPasswordSystemeEtBrowser(environnement, Tools.getConnectedLogin()).trim();
                if (!resultatTestAuthentification.equals("TEST CONNEXION OK")) {
                    return Tools.redirectionPageErreurs("Erreur d'authentification", resultatTestAuthentification, mapping, request, response, connectedUser);
                }
                //fin test d'authentification

                //Vérification inéxistance Objets
                pm = new PM(environnement, fileName, "PACK.TAF", connectedUser);
                objectNumber = pm.preparerPackMan(objectList);
                String resultatVerifObjets = pm.PmFormerPack();
                System.err.println("**********************************\n" + resultatVerifObjets + "\n**********************************");

                //Vérification inéxistance Objets
                if (resultatVerifObjets.length() > 0) {
                    String nomErreur = "Objets inexistants";
                    if (resultatVerifObjets.indexOf("Sorry, but your session is no longer active") > 0 || resultatVerifObjets.indexOf("SECURITY.VIOLATION") > 0) {
                        nomErreur = "Erreur d'authentification";
                    }
                    return Tools.redirectionPageErreurs(nomErreur, resultatVerifObjets, mapping, request, response, connectedUser);
                }
                //Fin Vérification inéxistance Objets
                //télécharger le pack destination
                ftpTools.downloadFolder(environnement, "PACK.TAF", packName, packName, false, true);
            } else {
                //créer le pack sous /work, le remplir a partir du dépôt et enfin télécharger le pack
                String nomProjetBI = inF.getSelectedDepot();
                String cheminDepot = "";
                if (nomProjetBI.equals("C.RELEASE")) {
                    cheminDepot = Configuration.parametresList.get("cheminDepotRelese");
                } else if (nomProjetBI.equals("C.PROJET")) {
                    cheminDepot = Configuration.parametresList.get("cheminDepotProjet");
                } else if (nomProjetBI.equals("C.UPGRADE")) {
                    cheminDepot = Configuration.parametresList.get("cheminDepotUpgrade");
                } else if (nomProjetBI.equals("TRUNK")) {
                    cheminDepot = Configuration.parametresList.get("cheminDepotTrunk");
                } else {
                    cheminDepot = Configuration.parametresList.get("cheminDepotDeltaProjets") + "/" + nomProjetBI + "/TAF-FULLPACK";
                }
                ftpTools.createFolder(packName, "/work", environnement);
                List<String> objectPathList = tools.convertObjectListToObjetctPathList(objectList);
                List<String> commandList = new ArrayList<String>();
                for (String pathObj : objectPathList) {
                    commandList.add("cp " + cheminDepot + "/" + pathObj + " /work/" + packName);
                }

                File file = null;
                try {
                    file = tools.createFile(Configuration.parametresList.get("espaceLocal") + "/commandsFileFormerPack");
                    tools.writeInFile(file, commandList);
                } catch (Exception exep) {
                    exep.printStackTrace();
                    Tools.traiterException(Tools.getStackTrace(exep));
                }
                ftpTools.uploadFileToServerDirectory(connectedUser, file, "/work", environnement, false, false);
                String[] commandArray = new String[2];
                commandArray[0] = "chmod 777 commandsFileFormerPack";
                commandArray[1] = "./commandsFileFormerPack";
                String resultat = t24Scripts.executerCommandeListEnvironnement(environnement, "/work", commandArray);


                //Vérification inéxistance Objets
                String listeObjetsInexistants = tools.getInexistantsObjetsIntoDepot(resultat, nomProjetBI);
                if (listeObjetsInexistants.length() > 0) {
                    return Tools.redirectionPageErreurs("Objets inexistants", listeObjetsInexistants, mapping, request, response, connectedUser);
                }
                //Fin Vérification inéxistance Objets


                ftpTools.downloadFolder(environnement, "/work", packName, packName, true, true);
            }







            //Zipper le dossier daté
            String packNameAbsolutePath = Configuration.parametresList.get("espaceLocal") + "/" + packName;
            Tools.showConsolLog(packNameAbsolutePath);
            new FolderZiper().zipFolder(packNameAbsolutePath, packNameAbsolutePath + ".zip");

            VerificationDeltaChampsDTO champs = new VerificationDeltaChampsDTO();
            champs.setEnvironnementSourceName(envName);
            champs.setNameCompressedFolder(packName + ".zip");
            champs.setPackName(packName);
            champs.setUrlCompressedFolder(packNameAbsolutePath + ".zip");


            request.getSession().setAttribute("VerificationDeltaChamps", champs);

        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        servlets.AfficherMessageEtatAvancement.executionEnCours.put(connectedUser, false);
        servlets.AfficherMessageEtatAvancement.setLogmessage("", Tools.getConnectedLogin());
        return mapping.findForward("ResultatCreerUnPack");
    }
}
