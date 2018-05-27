/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package strutsActions;

import dto.VerificationDeltaChampsDTO;
import dto.EnvironnementDTO;
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
import strutsForms.VerificationDeltaDownloadPackForm;
import t24Scripts.PM;
import t24Scripts.T24Scripts;
import tools.Configuration;
import tools.FolderZiper;
import tools.FtpTools;
import tools.Tools;

/**
 *
 * @author 04486
 */
public class ComparaisonPacksAction extends Action {

    //String pathDuPackEnvSource1;
    String pathDuPackEnvSource;
    String environnementSourceNom;
    String packName;
    String pathDuDossierSource;
    String environnementDestinationNom;
    String espaceLocal;
    String triExportParTypeString;
    boolean triExportParType;
    String resultatListerFichiers;
    String datedFolder;
    String date;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, InterruptedException {

        String connectedUser = Tools.getConnectedLogin();
        servlets.AfficherMessageEtatAvancement.executionEnCours.put(connectedUser, true);
        request.getSession().removeAttribute("VerificationDeltaChamps");
        List<String> listOfFiles = new ArrayList<String>();
        String pathDuPackEnvSource1 = null;
        try {
            Configuration.initialisation();
            VerificationDeltaDownloadPackForm inF = (VerificationDeltaDownloadPackForm) form;

            environnementSourceNom = inF.getEnvironnementSourceName();
            packName = inF.getDossierSource();
            pathDuDossierSource = inF.getDossierSourcePath();
            while (pathDuDossierSource.startsWith("/")) {
                pathDuDossierSource = pathDuDossierSource.replaceFirst("/", "");
            }
            environnementDestinationNom = inF.getEnvironnementDestinationName();
            espaceLocal = Configuration.parametresList.get("espaceLocal");
            triExportParTypeString = inF.getTri();
            String fileName = packName.replace("TAF-", "");
            fileName += ".txt";



            if (environnementSourceNom.equals("VERSIONNING")) {
                pathDuPackEnvSource = "/" + pathDuDossierSource;

            } else {
                pathDuPackEnvSource = "PACK.TAF";
            }
            pathDuPackEnvSource1 = pathDuPackEnvSource + "/";


            if (triExportParTypeString.equals("OUI")) {
                triExportParType = true;
            } else {
                triExportParType = true;
            }

            ////////////


            EnvironnementDTO environnementSource = Configuration.environnementDTOMapUserHasEnvironnement.get(connectedUser).get(environnementSourceNom);
            EnvironnementDTO environnementDestination = Configuration.environnementDTOMapUserHasEnvironnement.get(connectedUser).get(environnementDestinationNom);

            //test d'authentification
            T24Scripts t24Scripts = new T24Scripts();
            Map<String, String> errorMap = new HashMap<String, String>();
            String resultatTestAuthentification;
            if (!environnementSourceNom.equals("VERSIONNING")) {
                resultatTestAuthentification = t24Scripts.testLoginPasswordSystemeEtBrowser(environnementSource, Tools.getConnectedLogin()).trim();
                if (!resultatTestAuthentification.equals("TEST CONNEXION OK")) {
                    return Tools.redirectionPageErreurs("Erreur d'authentification", resultatTestAuthentification, mapping, request, response, connectedUser);
                }
            }
            if (!environnementDestinationNom.equals("VERSIONNING")) {
                resultatTestAuthentification = t24Scripts.testLoginPasswordSystemeEtBrowser(environnementDestination, Tools.getConnectedLogin()).trim();
                if (!resultatTestAuthentification.equals("TEST CONNEXION OK")) {
                    return Tools.redirectionPageErreurs("Erreur d'authentification", resultatTestAuthentification, mapping, request, response, connectedUser);
                }
            }
            //fin test d'authentification
            //test existance dossier Environnement 1            
            boolean packExists = false;
            String dossierDeBaseDuPack = "";
            if (environnementSourceNom.equals("VERSIONNING")) {
                dossierDeBaseDuPack = pathDuDossierSource;
            } else {
                dossierDeBaseDuPack = "PACK.TAF";
            }
            packExists = t24Scripts.testExistanceDossier(environnementSource, packName, dossierDeBaseDuPack);
            if (packExists == false) {
                tools.Tools.traiterException("Le dossier <b>" + dossierDeBaseDuPack + "/" + packName + "</b> n'existe pas sur l'environnement " + environnementSource.getNom());
                return Tools.redirectionPageErreurs("Dossier inéxistant", "Le dossier <b>" + dossierDeBaseDuPack + "/" + packName + "</b> n'existe pas sur l'environnement " + environnementSource.getNom(), mapping, request, response, connectedUser);
            }
            //fin test existance dossier Environnement 1

            Tools tools = new Tools();
            FtpTools ftpTools = new FtpTools();

            listOfFiles = tools.listDirectoryFiles(environnementSource, pathDuPackEnvSource, packName);

            Tools.showConsolLog("_____________list Size_____________" + listOfFiles.size());

            //create dir if not exists        
            tools.createDirectoryIfNotExists(espaceLocal);
            //create file on localDirectory and write objects in it  



            File file = tools.createFile(espaceLocal + "/" + fileName);
            tools.writeInFile(file, tools.traiterListString(listOfFiles));
            //DeplacerFichierDansSavedList

            ftpTools.uploadFileToSavedList(file, "./&SAVEDLISTS&", environnementDestination);
            Tools.showConsolLog(environnementDestination.getNom());


            //Vérification inéxistance Objets
            PM packman = new PM(environnementDestination, fileName, "PACK.TAF", connectedUser);
            String resultatVerifObjets = packman.PmFormerPack();

            System.err.println("**********************************\n" + resultatVerifObjets + "\n**********************************");

            //Vérification inéxistance Objets
            if (resultatVerifObjets.length() > 0) {
                String nomErreur = "Objets inexistants";
                if (resultatVerifObjets.indexOf("Sorry, but your session is no longer active") > 0 || resultatVerifObjets.indexOf("SECURITY.VIOLATION") > 0) {
                    nomErreur = "Erreur d'authentification";
                }
                return Tools.redirectionPageErreurs(resultatVerifObjets, resultatVerifObjets, mapping, request, response, connectedUser);
            }
            //Fin Vérification inéxistance Objets



            //télécharger le pack destination
            ftpTools.downloadFolder(environnementDestination, "PACK.TAF", packName, packName, true, true);
            tools.deplacerDossier(espaceLocal + "/" + packName, espaceLocal + "/" + packName + "." + environnementDestinationNom);
            //si l'option tri est activée, trier les objets par type
            if (triExportParType == true) {
                tools.triObjetsParType(packName + "." + environnementDestinationNom, espaceLocal);
            }



            //télécharger le pack source

            ftpTools.downloadFolder(environnementSource, pathDuPackEnvSource1, packName, packName, true, true);
            tools.deplacerDossier(espaceLocal + "/" + packName, espaceLocal + "/" + packName + "." + environnementSourceNom);


            //si l'option tri est activée, trier les objets par type
            if (triExportParType == true) {
                tools.triObjetsParType(packName + "." + environnementSourceNom, espaceLocal);
            } else {
                tools.mergeObjetsParType(packName + "." + environnementSourceNom, espaceLocal);
            }



            //Deplacer les deux exports dans un dossier daté
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("YYYY-MM-dd_HH-mm-ss");
            date = sdf.format(new Date());
            datedFolder = espaceLocal + "/" + date + "_" + environnementSourceNom + "_" + environnementDestinationNom;
            tools.createDirectoryIfNotExists(datedFolder);
            String dossierSourceAvantDeplacement = espaceLocal + "/" + packName + "." + environnementSourceNom;
            String dossierDestinationAvantDeplacement = espaceLocal + "/" + packName + "." + environnementDestinationNom;
            String dossierSourceApresDeplacement = datedFolder + "/" + packName + "." + environnementSourceNom;
            String dossierDestinationApresDeplacement = datedFolder + "/" + packName + "." + environnementDestinationNom;
            tools.deplacerDossier(dossierSourceAvantDeplacement, dossierSourceApresDeplacement);
            tools.deplacerDossier(dossierDestinationAvantDeplacement, dossierDestinationApresDeplacement);
            //Zipper le dossier daté
            new FolderZiper().zipFolder(datedFolder, datedFolder + ".zip");

            VerificationDeltaChampsDTO champs = new VerificationDeltaChampsDTO();
            champs.setEnvironnementDestinationName(environnementDestinationNom);
            champs.setEnvironnementSourceName(environnementSourceNom);
            champs.setPackName(packName);
            champs.setUrlCompressedFolder(datedFolder + ".zip");
            champs.setNameCompressedFolder(date + "_" + environnementSourceNom + "_" + environnementDestinationNom + ".zip");

            request.getSession().setAttribute("VerificationDeltaChamps", champs);
            servlets.AfficherMessageEtatAvancement.executionEnCours.put(connectedUser, false);
            servlets.AfficherMessageEtatAvancement.setLogmessage("", Tools.getConnectedLogin());
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }


        return mapping.findForward("ResultatComparaisonDelta");
    }
}
