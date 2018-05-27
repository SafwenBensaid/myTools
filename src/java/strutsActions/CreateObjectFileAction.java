/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package strutsActions;

import tools.AnalysePack;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import strutsForms.CreateObjectFileForm;
import t24Scripts.PM;
import tools.Configuration;
import dto.*;
import dataBaseTracRequests.DataBaseTracGenericRequests;
import entitiesMysql.*;
import entitiesTrac.Ticket;
import java.util.*;
import t24Scripts.T24Scripts;
import dataBaseTracRequests.DataBaseTracRequests;
import static threads.AutomatisationDeploiementIeThread.alertParEmail;
import tools.DataBaseTools;
import tools.Tools;
import tools.FtpTools;

/**
 *
 * @author 04486
 */
public class CreateObjectFileAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, InterruptedException {

        String connectedUser = Tools.getConnectedLogin();
        servlets.AfficherMessageEtatAvancement.executionEnCours.put(connectedUser, true);
        Configuration.initialisation();

        String fileName = "";
        String devServerName = "";
        String username = "";
        String password = "";
        String objectList = "";
        String mnemonicCompany = "";
        String analyseEnvironnement = "";
        PM pm = null;
        String resultatAnalysePack = null;
        String textAreaManuel = null;
        boolean sendTicketToIE;
        boolean writeTextOnTicket;
        boolean nePasEcraserLivrable = false;

        try {
            DetailsLivraisonDTO detailsLivraison;
            EnvironnementDTO envirTestDep = null;
            int objectNumber;
            EnvironnementDTO environnementDev;
            CreateObjectFileForm inF = (CreateObjectFileForm) form;


            Configuration.livraisonsEnCoursMap.put(inF.getNumLivraison().trim(), connectedUser);

            textAreaManuel = inF.getTextAreaManuel();
            sendTicketToIE = inF.isSendTicketToIE();
            writeTextOnTicket = inF.isWriteTextOnTicket();
            //get circuit
            Tools.showConsolLog("FORM: " + inF);
            Tools.showConsolLog("CIRCUIT: " + inF.getCircuit());
            if (inF.getCircuit().equals("RELEASE")) {
                fileName = "LIVR.";
                devServerName = "DEVR";
                envirTestDep = Configuration.environnementDTOMapUserHasEnvironnement.get(connectedUser).get("ASS");
            } else if (inF.getCircuit().equals("PROJET")) {
                fileName = "LIVP.";
                devServerName = "DEV2";
                envirTestDep = Configuration.environnementDTOMapUserHasEnvironnement.get(connectedUser).get("ASS2");
            } else if (inF.getCircuit().equals("HOTFIX")) {
                fileName = "LIVH.";
                devServerName = "DEVH";
                envirTestDep = Configuration.environnementDTOMapUserHasEnvironnement.get(connectedUser).get("ASS");
            } else if (inF.getCircuit().equals("UPGRADE")) {
                fileName = "LIVU.";
                devServerName = "DEVU";
                envirTestDep = Configuration.environnementDTOMapUserHasEnvironnement.get(connectedUser).get("ASSU");
            }
            analyseEnvironnement = inF.getCircuit();
            mnemonicCompany = inF.getMnemonic();
            if (mnemonicCompany.equals("Autre")) {
                mnemonicCompany = inF.getAutreMnemonic().trim();
            }
            Integer numeroAnomalie = Integer.parseInt(inF.getNumAnomalie().trim());
            // get owner and reporter from database anomalie
            //Ticket ticket = DataBaseTracGenericRequests.getTicketById(dbTools, numTicket);
            DataBaseTools dbTools = new DataBaseTools(Configuration.puAnomalies);
            Ticket ticketAnomalies = DataBaseTracGenericRequests.getTicketById(dbTools, numeroAnomalie);
            dbTools.closeRessources();
            //fin
            if (textAreaManuel != null) {
                if (textAreaManuel.trim().length() > 0) {
                    //insertion de l'objet Livraison dans la base
                    dbTools = new DataBaseTools(Configuration.puOvTools);
                    Livraison liv = dbTools.em.find(Livraison.class, Integer.parseInt(inF.getNumLivraison().trim()));
                    if (liv == null) {
                        liv = new Livraison(Integer.parseInt(inF.getNumLivraison().trim()));
                    } else {
                        nePasEcraserLivrable = inF.isNePasEcraserLivrable();
                    }
                    liv.setMessageTrac(textAreaManuel.trim());
                    liv.setNumeroAnomalie(numeroAnomalie);
                    liv.setContenuLivrables(inF.getContenuDesLivrables().trim());
                    liv.setOwner(ticketAnomalies.getOwner());
                    liv.setReporter(ticketAnomalies.getReporter());
                    dbTools.update(liv);
                    dbTools.closeRessources();

                    //cloturer le ticket trac (mise à jour ticket)
                    DataBaseTracRequests.sendHotfixToBeDeployed(request, connectedUser, inF.getNumLivraison().trim(), textAreaManuel.trim(), inF.getCircuit().trim(), inF.getContenuDesLivrables(), sendTicketToIE, writeTextOnTicket, false, nePasEcraserLivrable);
                    return mapping.findForward("HomeOV");
                }
            }

            dbTools = new DataBaseTools(Configuration.puOvTools);
            Livraison liv = dbTools.em.find(Livraison.class, Integer.parseInt(inF.getNumLivraison().trim()));
            if (liv != null) {
                nePasEcraserLivrable = inF.isNePasEcraserLivrable();
            }
            dbTools.closeRessources();
            //Select Environnement
            Tools tools = new Tools();
            environnementDev = Configuration.environnementDTOMapUserHasEnvironnement.get(connectedUser).get(devServerName);
            //test d'authentification
            T24Scripts t24Scripts = new T24Scripts();
            Map<String, String> errorMap = new HashMap<String, String>();
            String resultatTestAuthentification = "";
            resultatTestAuthentification = t24Scripts.testLoginPasswordSystemeEtBrowser(environnementDev, Tools.getConnectedLogin()).trim();
            if (!resultatTestAuthentification.equals("TEST CONNEXION OK")) {
                return Tools.redirectionPageErreurs("Erreur d'authentification", resultatTestAuthentification, mapping, request, response, connectedUser);
            }
            resultatTestAuthentification = t24Scripts.testLoginPasswordSystemeEtBrowser(envirTestDep, Tools.getConnectedLogin()).trim();
            if (!resultatTestAuthentification.equals("TEST CONNEXION OK")) {
                return Tools.redirectionPageErreurs("Erreur d'authentification", resultatTestAuthentification, mapping, request, response, connectedUser);
            }
            //fin test d'authentification
            try {
                fileName += Configuration.getAbreviationProjetParNiveauProjet(inF.getNiveauProjet().trim()) + ".";
                //get num livraison
                fileName += inF.getNumLivraison().trim() + ".";
            } catch (Exception exep) {
                exep.printStackTrace();
                Tools.traiterException(Tools.getStackTrace(exep));
                alertParEmail(null, "Probleme ticket " + inF.getNumLivraison(), "Bonjour,<br>Le niveau projet du ticket " + inF.getNumLivraison() + " contient un caractère spécial, à corriger dans trac.ini, base anomalie, base livraison et base ovtools (table niveau projet)", "C24OV@biat.com.tn");
            }

            //get suffixe
            if (inF.getSuffixe().length() > 0) {
                fileName += inF.getSuffixe().trim() + ".txt";
            } else {
                fileName += inF.getSuffixe().trim() + "txt";
            }
            objectList = inF.getTextAreaObjectList().trim();

            pm = new PM(environnementDev, fileName, mnemonicCompany, "PACK.TAF", connectedUser);
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


            //Deplacer le pack vers ASS/PACK.DEP
            FtpTools ftptools = new FtpTools();
            String packName = fileName.replace(".txt", "");

            ftptools.transferFolder(connectedUser, environnementDev, envirTestDep, "PACK.TAF", "PACK.TAF", "TAF-" + packName, true);


            boolean cusExists = false;
            AnalysePack analysepack = new AnalysePack();
            resultatAnalysePack = analysepack.analysePack(analyseEnvironnement, packName, objectNumber, true);
            if (resultatAnalysePack.contains("FILE.CONTROL de type CUS existe dans le pack") && analysepack.packTn1Vide == false) {
                cusExists = true;
            } else if (resultatAnalysePack.contains("FILE.CONTROL de type CUS existe dans le pack") && analysepack.packTn1Vide == true) {
                mnemonicCompany = "BNK";
                packName += ".BNK";
            }

            String contenuDesLivrables = inF.getContenuDesLivrables();
            if (cusExists) {
                contenuDesLivrables = "MULTIPACKS";
            }

            detailsLivraison = new DetailsLivraisonDTO(packName, inF.getCircuit().trim(), inF.getNumAnomalie().trim(), inF.getPhase().trim(), inF.getNiveauProjet().trim(), inF.getNumLivraison().trim(), resultatAnalysePack.trim(), cusExists, devServerName, "", "", "", "", mnemonicCompany, contenuDesLivrables, objectList, ticketAnomalies.getOwner(), ticketAnomalies.getReporter(), nePasEcraserLivrable);

            if (resultatAnalysePack.contains("Un EB.COMPOSITE.SCREEN existe dans le MENU")) {
                detailsLivraison.setNbrIterationDeploiement(2);
            }

            Configuration.usersDetailsLivraisonsMap.put(connectedUser, detailsLivraison);
            servlets.AfficherMessageEtatAvancement.setLogmessage("", Tools.getConnectedLogin());
            servlets.AfficherMessageEtatAvancement.executionEnCours.put(connectedUser, false);
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        if (resultatAnalysePack.length() > 0) {
            return mapping.findForward(null);
        } else {
            return mapping.findForward("deployerPackForm");
        }
    }
}
