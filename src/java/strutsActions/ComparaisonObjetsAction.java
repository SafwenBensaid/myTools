/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package strutsActions;

import dto.EnvironnementDTO;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import strutsForms.ComparaisonObjetsForm;
import t24Scripts.PM;
import tools.Configuration;
import tools.FolderZiper;
import tools.FtpTools;
import tools.Tools;
import dto.VerificationDeltaChampsDTO;
import java.util.List;
import t24Scripts.T24Scripts;
import t24Scripts.T24Tools;

/**
 *
 * @author 04486
 */
public class ComparaisonObjetsAction extends Action {

    private String date;
    private String dossierDownload;
    private String datedFolder;
    private String triExportParTypeString;
    private boolean triExportParType;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, InterruptedException {

        String connectedUser = Tools.getConnectedLogin();
        try {
            servlets.AfficherMessageEtatAvancement.executionEnCours.put(connectedUser, true);
            String fileName;
            String packName;
            String envName1;
            String envName2;
            String objectList;
            PM pm;
            int objectNumber;
            EnvironnementDTO environnement1;
            EnvironnementDTO environnement2;

            Configuration.initialisation();

            ComparaisonObjetsForm inF = (ComparaisonObjetsForm) form;
            Tools tools = new Tools();

            packName = inF.getNomPack();
            fileName = packName.replace("TAF-", "");
            fileName += ".txt";


            envName1 = inF.getEnvironnementSourceName1();
            envName2 = inF.getEnvironnementSourceName2();
            triExportParTypeString = inF.getTri();
            if (triExportParTypeString.equals("OUI")) {
                triExportParType = true;
            } else {
                triExportParType = false;
            }
            dossierDownload = Configuration.parametresList.get("espaceLocal");
            environnement1 = Configuration.environnementDTOMapUserHasEnvironnement.get(connectedUser).get(envName1);
            environnement2 = Configuration.environnementDTOMapUserHasEnvironnement.get(connectedUser).get(envName2);


            //test d'authentification
            T24Scripts t24Scripts = new T24Scripts();
            Map<String, String> errorMap = new HashMap<String, String>();
            String resultatTestAuthentification;
            if (!envName1.equals("VERSIONNING")) {
                resultatTestAuthentification = t24Scripts.testLoginPasswordSystemeEtBrowser(environnement1, Tools.getConnectedLogin()).trim();
                if (!resultatTestAuthentification.equals("TEST CONNEXION OK")) {
                    return Tools.redirectionPageErreurs("Erreur d'authentification", resultatTestAuthentification, mapping, request, response, connectedUser);
                }
            }
            if (!envName2.equals("VERSIONNING")) {
                resultatTestAuthentification = t24Scripts.testLoginPasswordSystemeEtBrowser(environnement2, Tools.getConnectedLogin()).trim();
                if (!resultatTestAuthentification.equals("TEST CONNEXION OK")) {
                    return Tools.redirectionPageErreurs("Erreur d'authentification", resultatTestAuthentification, mapping, request, response, connectedUser);
                }
            }
            //fin test d'authentification


            objectList = inF.getTextAreaObjectList();
            objectList = tools.traiterString(objectList);
            //Calculer le nombre d'objets à packager

            //create dir if not exists        
            tools.createDirectoryIfNotExists(Configuration.parametresList.get("espaceLocal"));
            //create file and write objects in it
            File file = tools.createFile(Configuration.parametresList.get("espaceLocal") + "/" + fileName);
            objectList = objectList.replace(" ", "");
            objectList = objectList.replace("\t", "");
            tools.writeInFile(file, objectList);
            //
            FtpTools ftpTools = new FtpTools();
            T24Tools t24Tools = new T24Tools();
            String listeErreursObjets = "";
            String resultatVerifObjets1 = "";
            String nomProjetBI = inF.getSelectedDepot();
            String nomProjetBI2 = inF.getSelectedDepot2();
            if (!envName1.equals("VERSIONNING")) {
                //PackMan
                ftpTools.uploadFileToSavedList(file, "./&SAVEDLISTS&", environnement1);

                pm = new PM(environnement1, fileName, "PACK.TAF", connectedUser);
                resultatVerifObjets1 = pm.PmFormerPack();

                System.err.println("**********************************\n" + resultatVerifObjets1 + "\n**********************************");

                //télécharger le pack destination
                ftpTools.downloadFolder(environnement1, "PACK.TAF", packName, packName, true, true);
            } else {
                //créer le pack sous /work, le remplir a partir du dépôt et enfin télécharger le pack

                String cheminDepot = "";
                if (nomProjetBI.equals("C.RELEASE")) {
                    cheminDepot = Configuration.parametresList.get("cheminDepotRelese");
                } else if (nomProjetBI.equals("C.PROJET")) {
                    cheminDepot = Configuration.parametresList.get("cheminDepotProjet");
                } else if (nomProjetBI.equals("TRUNK")) {
                    cheminDepot = Configuration.parametresList.get("cheminDepotTrunk");
                } else if (nomProjetBI.equals("C.UPGRADE")) {
                    cheminDepot = Configuration.parametresList.get("cheminDepotUpgrade");
                } else {
                    cheminDepot = Configuration.parametresList.get("cheminDepotDeltaProjets") + "/" + nomProjetBI + "/TAF-FULLPACK";
                }
                ftpTools.createFolder(packName, "/work", environnement1);
                List<String> objectPathList = tools.convertObjectListToObjetctPathList(objectList);
                List<String> commandList = new ArrayList<String>();
                for (String pathObj : objectPathList) {
                    commandList.add("cp " + cheminDepot + "/" + pathObj + " /work/" + packName);
                }

                String[] commandesArray = commandList.toArray(new String[commandList.size()]);

                t24Scripts.executerCommandeListEnvironnement(environnement1, cheminDepot, commandesArray);
                ftpTools.downloadFolder(environnement1, "/work", packName, packName, true, true);
            }
            String resultatVerifObjets2 = "";
            if (!envName2.equals("VERSIONNING")) {
                //PackMan
                ftpTools.uploadFileToSavedList(file, "./&SAVEDLISTS&", environnement2);

                pm = new PM(environnement2, fileName, "PACK.TAF", connectedUser);
                resultatVerifObjets2 = pm.PmFormerPack();

                System.err.println("**********************************\n" + resultatVerifObjets2 + "\n**********************************");
            } else {
                //créer le pack sous /work, le remplir a partir du dépôt et enfin télécharger le pack

                String cheminDepot = "";
                if (nomProjetBI2.equals("C.RELEASE")) {
                    cheminDepot = Configuration.parametresList.get("cheminDepotRelese");
                } else if (nomProjetBI2.equals("C.PROJET")) {
                    cheminDepot = Configuration.parametresList.get("cheminDepotProjet");
                } else if (nomProjetBI2.equals("TRUNK")) {
                    cheminDepot = Configuration.parametresList.get("cheminDepotTrunk");
                } else if (nomProjetBI.equals("C.UPGRADE")) {
                    cheminDepot = Configuration.parametresList.get("cheminDepotUpgrade");
                } else {
                    cheminDepot = Configuration.parametresList.get("cheminDepotDeltaProjets") + "/" + nomProjetBI2 + "/TAF-FULLPACK";
                }

                new Tools().viderLeDossierSiRempliSinonLeCreer(environnement2, packName, "/work");

                List<String> objectPathList = tools.convertObjectListToObjetctPathList(objectList);
                List<String> commandList = new ArrayList<String>();
                for (String pathObj : objectPathList) {
                    commandList.add("cp " + cheminDepot + "/" + pathObj + " /work/" + packName);
                }

                String[] commandesArray = commandList.toArray(new String[commandList.size()]);

                t24Scripts.executerCommandeListEnvironnement(environnement2, cheminDepot, commandesArray);
            }

            StringBuilder resultatVerifObjetsGlobal = new StringBuilder();
            //Vérification inéxistance Objets
            if (resultatVerifObjets1.length() > 0 || resultatVerifObjets2.length() > 0) {

                if (resultatVerifObjets1.length() > 0) {
                    resultatVerifObjetsGlobal.append(resultatVerifObjets1);
                }
                if (resultatVerifObjets1.length() > 0 && resultatVerifObjets2.length() > 0) {
                    resultatVerifObjetsGlobal.append("<br/><br/><br/>");
                }
                if (resultatVerifObjets2.length() > 0) {
                    resultatVerifObjetsGlobal.append(resultatVerifObjets2);
                }
                String nomErreur = "Objets inexistants";
                if (resultatVerifObjetsGlobal.indexOf("Sorry, but your session is no longer active") > 0 || resultatVerifObjetsGlobal.indexOf("SECURITY.VIOLATION") > 0) {
                    nomErreur = "Erreur d'authentification";
                }
                return Tools.redirectionPageErreurs(nomErreur, resultatVerifObjetsGlobal.toString(), mapping, request, response, connectedUser);
            }
            //Fin Vérification inéxistance Objets


            //si l'option tri est activée, trier les objets par type
            if (triExportParType == true) {
                tools.triObjetsParType(packName, dossierDownload);
            }
            //Deplacer les deux exports dans un dossier daté
            try {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM-dd_HH-mm-ss");
                date = sdf.format(new Date());
            } catch (Exception ex1) {
                ex1.printStackTrace();
                date = "" + System.currentTimeMillis();
            }
            String suffixe1;
            String suffixe2;
            if (!envName1.equals("VERSIONNING")) {
                suffixe1 = envName1;
            } else {
                suffixe1 = nomProjetBI;
            }
            if (!envName2.equals("VERSIONNING")) {
                suffixe2 = envName2;
            } else {
                suffixe2 = nomProjetBI2;
            }


            datedFolder = dossierDownload + "/" + date + "_" + suffixe1 + "_" + suffixe2;
            tools.createDirectoryIfNotExists(datedFolder);

            String dossierSourceAvantDeplacement = dossierDownload + "/" + packName;
            String dossierSourceApresDeplacement = datedFolder + "/" + packName + "_" + suffixe1;
            tools.deplacerDossier(dossierSourceAvantDeplacement, dossierSourceApresDeplacement);

            //télécharger le pack destination
            if (!envName2.equals("VERSIONNING")) {
                ftpTools.downloadFolder(environnement2, "PACK.TAF", packName, packName, true, true);
            } else {
                ftpTools.downloadFolder(environnement2, "/work", packName, packName, true, true);
            }
            //si l'option tri est activée, trier les objets par type
            if (triExportParType == true) {
                tools.triObjetsParType(packName, dossierDownload);
            }
            //Deplacer les deux exports dans un dossier daté
            dossierSourceAvantDeplacement = dossierDownload + "/" + packName;
            dossierSourceApresDeplacement = datedFolder + "/" + packName + "_" + suffixe2;
            tools.deplacerDossier(dossierSourceAvantDeplacement, dossierSourceApresDeplacement);

            //Zipper le dossier daté

            new FolderZiper().zipFolder(datedFolder, datedFolder + ".zip");

            VerificationDeltaChampsDTO champs = new VerificationDeltaChampsDTO();

            if (!envName1.equals("VERSIONNING")) {
                champs.setEnvironnementSourceName(envName1);
            } else {
                champs.setEnvironnementSourceName(envName1 + " (Branche: " + nomProjetBI + ")");
            }
            if (!envName2.equals("VERSIONNING")) {
                champs.setEnvironnementDestinationName(envName2);
            } else {
                champs.setEnvironnementDestinationName(envName2 + " (Branche: " + nomProjetBI2 + ")");
            }

            champs.setNameCompressedFolder(packName + ".zip");
            champs.setPackName(packName);
            champs.setUrlCompressedFolder(datedFolder + ".zip");


            request.getSession().setAttribute("VerificationDeltaChamps", champs);

        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        servlets.AfficherMessageEtatAvancement.executionEnCours.put(connectedUser, false);
        servlets.AfficherMessageEtatAvancement.setLogmessage("", Tools.getConnectedLogin());
        return mapping.findForward("ResultatComparaisonObjets");
    }
}
