/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package threads;

import dataBaseTracRequests.DataBaseTracGenericRequests;
import dataBaseTracRequests.DataBaseTracRequests;
import dto.CoupleDTO;
import dto.EnvironnementDTO;
import entitiesMysql.Livraison;
import entitiesTrac.Ticket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import servlets.GestionLivraisonControlerServlet;
import static threads.AutomatisationDeploiementIeThread.alertParEmail;
import tools.Configuration;
import tools.DataBaseTools;
import tools.ManipulationObjectsTool;
import tools.Tools;

/**
 *
 * @author 04486
 */
public class AutomatisationDeploiementOvThread extends Thread {

    static public Map<String, Boolean> alertEnvoyeeCircuitBloqueMap;
    static public Set<Integer> alertEnvoyeeLivraisonBloqueeSet;
    static public Set<Integer> livraisonActionManuelleSet;
    public boolean threadIsAlive = true;

    public AutomatisationDeploiementOvThread() {
        alertEnvoyeeCircuitBloqueMap = new HashMap<>();
        alertEnvoyeeLivraisonBloqueeSet = new TreeSet<>();
        livraisonActionManuelleSet = new TreeSet<>();
    }

    @Override
    public void run() {
        while (threadIsAlive) {

            if (!alertEnvoyeeCircuitBloqueMap.containsKey("HOTFIX")) {
                alertEnvoyeeCircuitBloqueMap.put("HOTFIX", false);
            }
            if (!alertEnvoyeeCircuitBloqueMap.containsKey("RELEASE")) {
                alertEnvoyeeCircuitBloqueMap.put("RELEASE", false);
            }
            if (!alertEnvoyeeCircuitBloqueMap.containsKey("PROJET")) {
                alertEnvoyeeCircuitBloqueMap.put("PROJET", false);
            }
            if (!alertEnvoyeeCircuitBloqueMap.containsKey("UPGRADE")) {
                alertEnvoyeeCircuitBloqueMap.put("UPGRADE", false);
            }
            Map<String, List<Map<String, Object>>> mapPipeTickets = new DataBaseTracRequests().getAllPipeTicketsRequestT24("OV", Configuration.puLivraisons);
            List<Map<String, Object>> listRelease = mapPipeTickets.get(Configuration.parametresList.get("phaseRelease"));
            List<Map<String, Object>> listProjet = mapPipeTickets.get("QUALIFICATION_PROJET");
            List<Map<String, Object>> listHotfix = mapPipeTickets.get("HOT FIXE TEST");
            List<Map<String, Object>> listActionsAChaudTest = mapPipeTickets.get("ACTION A CHAUD TEST");
            if (listActionsAChaudTest != null) {
                if (listHotfix != null) {
                    listHotfix.addAll(listActionsAChaudTest);
                } else {
                    listHotfix = listActionsAChaudTest;
                }
            }
            List<Map<String, Object>> listUpgrade = null;
            String phaseUpgrade = Configuration.parametresList.get("phaseUpgrade");
            if (phaseUpgrade.equals("QUALIFICATION_UPGRADE")) {
                listUpgrade = mapPipeTickets.get("QUALIFICATION_UPGRADE");
            } else if (phaseUpgrade.equals("CERTIFICATION_UPGRADE")) {
                listUpgrade = mapPipeTickets.get("CERTIFICATION_UPGRADE");
            } else {
                Tools.alertParEmail("PROBLEME PARAMETRAGE", "Veuillez vérifier le champ <b>phaseupgrade</b> dans la base de données de paramétrage, elle doit être QUALIFICATION_UPGRADE ou CERTIFICATION_UPGRADE");
            }
            //ajouter la pipe d'harmonisation upgrade
            List<Map<String, Object>> listHarmonistaionUpgrade = mapPipeTickets.get("HARMONISATION_C.UPGRADE");
            if (listHarmonistaionUpgrade != null) {
                if (listUpgrade != null) {
                    listUpgrade.addAll(listHarmonistaionUpgrade);
                } else {
                    listUpgrade = listHarmonistaionUpgrade;
                }
            }

            afficherLog("------------------ START THREAD DEPLOIEMENT OV SERVEURS ASSEMBLY ------------------");
            DataBaseTools dbTools = new DataBaseTools(Configuration.puOvTools);
            Configuration.chargerTousLesCircuitsDetails(dbTools);
            dbTools.closeRessources();
            String connectedUser = "OVTOOLS1";
            String devServerName = null;
            String testDepServerName = null;

            if (Configuration.etatCircuitMap.get("GLOBAL").trim().equals("ON") && Configuration.etatCircuitMap.get("LIVRAISON_SELF_SERVICE").trim().equals("ON")) {
                if (threadIsAlive) {
                    if (Configuration.etatCircuitMap.get("HF_PRISE_EN_CHARGE").trim().equals("ON")) {
                        devServerName = "DEVH";
                        testDepServerName = "ASS";
                        deploiementDesTicketsOvParCircuit("HOTFIX", devServerName, testDepServerName, connectedUser, listHotfix);
                    } else {
                        afficherLog("!!!!!!!!!!!!!!!! CIRCUIT HOTFIX OV EN ARRET !!!!!!!!!!!!!!!!");
                    }
                }
                if (threadIsAlive) {
                    if (Configuration.etatCircuitMap.get("CR_PRISE_EN_CHARGE").trim().equals("ON")) {
                        devServerName = "DEVR";
                        testDepServerName = "ASS";
                        deploiementDesTicketsOvParCircuit("RELEASE", devServerName, testDepServerName, connectedUser, listRelease);
                    } else {
                        afficherLog("!!!!!!!!!!!!!!!! CIRCUIT RELEASE OV EN ARRET !!!!!!!!!!!!!!!!");
                    }
                }
                if (threadIsAlive) {
                    if (Configuration.etatCircuitMap.get("CP_PRISE_EN_CHARGE").trim().equals("ON")) {
                        devServerName = "DEV2";
                        testDepServerName = "ASS2";
                        deploiementDesTicketsOvParCircuit("PROJET", devServerName, testDepServerName, connectedUser, listProjet);
                    } else {
                        afficherLog("!!!!!!!!!!!!!!!! CIRCUIT PROJET OV EN ARRET !!!!!!!!!!!!!!!!");
                    }
                }
                if (threadIsAlive) {
                    if (Configuration.etatCircuitMap.get("CU_PRISE_EN_CHARGE").trim().equals("ON")) {
                        devServerName = "DEVU";
                        testDepServerName = "ASSU";
                        deploiementDesTicketsOvParCircuit("UPGRADE", devServerName, testDepServerName, connectedUser, listUpgrade);
                    } else {
                        afficherLog("!!!!!!!!!!!!!!!! CIRCUIT UPGRADE OV EN ARRET !!!!!!!!!!!!!!!!");
                    }
                }
            }

            try {
                //sleep très important, à ne pas enlever
                Thread.sleep(180000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ManageLogThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void deploiementDesTicketsOvParCircuit(String circuit, String devServerName, String testDepServerName, String connectedUser, List<Map<String, Object>> listTickets) {
        String[] tabEnvNameDestinationDeploiementLivraison = null;
        String[] resTab = null;
        try {

            Set<Map<String, Object>> setTickets = new HashSet<>();
            if (listTickets != null && !listTickets.isEmpty()) {
                setTickets.addAll(listTickets);

                String log = "###CIRCUIT:" + circuit + ": liste des tickets (";
                String removedTickets = "";
                for (Map<String, Object> mapDetails : listTickets) {
                    Ticket ticket = (Ticket) mapDetails.get("Ticket");
                    Integer numTicketLivraison = ticket.getId();
                    log += numTicketLivraison + ", ";

                    //Supprimer les tickets contenants des actions manuelles à partir de la liste initiale 
                    if (livraisonActionManuelleSet.contains(numTicketLivraison)) {
                        setTickets.remove(mapDetails);
                        removedTickets += numTicketLivraison.toString() + ", ";
                        if (Configuration.livraisonsEnCoursMap.containsKey(String.valueOf(numTicketLivraison))) {
                            Configuration.livraisonsEnCoursMap.remove(String.valueOf(numTicketLivraison));
                        }
                    }

                    //Supprimer les tickets en problème de déploiement à partir de la liste initiale
                    if (ticket.getPriority().equals("PROBLEME DE DEPLOIEMENT")) {
                        setTickets.remove(mapDetails);
                        removedTickets += numTicketLivraison.toString() + ", ";
                    }
                }
                log += ")";
                afficherLog(log);
                afficherLog("Tickets contenants des actions manuelles: " + removedTickets);
            }

            if (setTickets != null && !setTickets.isEmpty()) {
                String resultatTestAuthentificationGlobal = callTestAuthentification(devServerName, testDepServerName, connectedUser);
                if (resultatTestAuthentificationGlobal.contains("Problème")) {
                    alertEnvoyeeCircuitBloqueMap.put(circuit, true);
                    alertParEmail(null, "Activité de recette automatique des livraisons Bloquée", "Bonjour,<br>L'activité de recette automatique des livraisons sur le circuit " + circuit + " est bloquée " + resultatTestAuthentificationGlobal, "C24OV@biat.com.tn");
                } else {
                    alertEnvoyeeCircuitBloqueMap.put(circuit, false);
                    tabEnvNameDestinationDeploiementLivraison = new String[]{testDepServerName};

                    for (Map<String, Object> mapDetails : setTickets) {
                        if (!threadIsAlive) {
                            break;
                        }
                        Ticket ticket = (Ticket) mapDetails.get("Ticket");
                        Integer numTicketLivraison = ticket.getId();

                        DataBaseTools dbTools = new DataBaseTools(Configuration.puOvTools);
                        Livraison liv = dbTools.em.find(Livraison.class, numTicketLivraison);
                        dbTools.closeRessources();
                        afficherLog("!!! START RECETTE LIVRAISON " + numTicketLivraison);

                        if (liv == null || liv.getLivrables() == null) {
                            if (!alertEnvoyeeLivraisonBloqueeSet.contains(numTicketLivraison)) {
                                alertParEmail(ticket, "Activité de recette automatique des livraisons Bloquée", "Bonjour,<br>L'activité de recette automatique des livraisons sur le circuit " + circuit + " est bloquée, prière de passer la livraison <a href='http://172.28.70.74/trac/livraisons_t24/ticket/" + numTicketLivraison + "'>#" + numTicketLivraison + "</a> manuellement.", "C24OV@biat.com.tn");
                            }
                            alertEnvoyeeLivraisonBloqueeSet.add(numTicketLivraison);
                            afficherLog("liv == null || liv.getLivrables() == null  => QUIT    Livraison: #" + numTicketLivraison);
                            return;
                        }
                        Configuration.livraisonsEnCoursMap.put(String.valueOf(numTicketLivraison), connectedUser);
                        ManipulationObjectsTool manObjTools = new ManipulationObjectsTool();
                        Map<String, String[]> resultMap = manObjTools.traiterLivrable(liv, "OV", connectedUser, tabEnvNameDestinationDeploiementLivraison, devServerName, true);

                        String problemesDeploiementsString = null;
                        if (resultMap.containsKey("PROBLEME") && resultMap.get("PROBLEME")[0].length() > 0 && resultMap.get("PROBLEME")[1].length() > 0) {
                            //problème d'authentification, prob d'existance de pack ou ...
                            resTab = resultMap.get("PROBLEME");
                            //return Tools.redirectionPageErreurs(resTab[0], resTab[1], mapping, request, response, connectedUser);
                            if (!alertEnvoyeeLivraisonBloqueeSet.contains(liv.getNumeroLivraison())) {
                                String msg = "Bonjour,<br>L'activité des livraisons sur le circuit " + circuit + " est bloquée au niveau de la livraison <a href='http://172.28.70.74/trac/livraisons_t24/ticket/" + numTicketLivraison + "'>#" + numTicketLivraison + "</a>. Voici le message d'erreur: ";
                                msg += resTab[1];
                                alertParEmail(ticket, "Activité des livraisons Bloquée", msg, "C24OV@biat.com.tn");
                                DataBaseTracGenericRequests.updateTicketTracGeneral(Configuration.puLivraisons, numTicketLivraison, "OVTOOLS", "\n\n{{{\n#!html\n<b>Activité des livraisons Bloquée</b><br>" + msg + "\n}}}", null, null, null, null, null);
                            }
                            alertEnvoyeeLivraisonBloqueeSet.add(liv.getNumeroLivraison());
                            return;
                        } else {
                            String messageTrac = resultMap.get("RESULTAT_HTML_COMPLET")[0];
                            String[] envDepOkArray = resultMap.get("ENV_DEP_OK");
                            List<String> envDepOkList = Arrays.asList(envDepOkArray);
                            problemesDeploiementsString = resultMap.get("RESULTAT")[1];

                            //cocher les cases à cocher            
                            List<CoupleDTO> listeCustomFields = new ArrayList<>();
                            for (String envName : tabEnvNameDestinationDeploiementLivraison) {
                                String envNameBox = null;
                                if (Configuration.mapEnvNameCheckBoxName.containsKey(envName)) {
                                    envNameBox = Configuration.mapEnvNameCheckBoxName.get(envName);
                                } else {
                                    envNameBox = "biat" + envName.toLowerCase();
                                }
                                if (envDepOkList.contains(envName)) {
                                    listeCustomFields.add(new CoupleDTO(envNameBox, "1"));
                                } else {
                                    listeCustomFields.add(new CoupleDTO(envNameBox, "0"));
                                }
                            }
                            String newPriority = null;
                            String newOwner = null;
                            String newVersion = null;
                            if (problemesDeploiementsString != null && problemesDeploiementsString.trim().length() > 0) {
                                newPriority = "PROBLEME DE PACKAGING";
                                newOwner = ticket.getCc();
                                alertParEmail(ticket, "PROBLEME DE PACKAGING " + liv.getNumeroLivraison(), "Bonjour,<br>La livraison <a href='http://172.28.70.74/trac/livraisons_t24/ticket/" + numTicketLivraison + "'>#" + numTicketLivraison + "</a> présente un problème de packaging.");
                                afficherLog("!!! ECHEC RECETTE LIVRAISON " + numTicketLivraison + ": problème de packaging!");
                            } else {
                                //pour ne pas retraiter le ticket
                                livraisonActionManuelleSet.add(liv.getNumeroLivraison());
                                if (!messageTrac.contains("<span class='errorBold'> (A traiter manuellement)") && !messageTrac.contains("Ce ticket sera traite manuellement") && !messageTrac.contains("POUR VERIFICATION OV")) {
                                    //si il n'existe pas d'action manuelle non traitée
                                    newPriority = "PRET POUR DEPLOIEMENT";
                                    newOwner = "riadh anouar ben dakhlia";
                                    alertParEmail(ticket, "Recette automatique réussie", "Bonjour,<br>La recette de la livraison <a href='http://172.28.70.74/trac/livraisons_t24/ticket/" + numTicketLivraison + "'>#" + numTicketLivraison + "</a> a été effectuée sur l'environnements de packaging du circuit " + circuit + " avec succès.", "C24OV@biat.com.tn");
                                    afficherLog("!!! SUCCES RECETTE LIVRAISON " + numTicketLivraison);
                                } else {
                                    //si il existe une action manuelle non traitée
                                    Configuration.livraisonsEnCoursMap.remove(String.valueOf(numTicketLivraison));
                                    alertParEmail(ticket, "Recette technique automatique des livraisons Bloquée", "Bonjour,<br>La recette technique des livraisons sur le circuit " + circuit + " est bloquée, car la livraison <a href='http://172.28.70.74/trac/livraisons_t24/ticket/" + numTicketLivraison + "'>#" + numTicketLivraison + "</a> contient une action manuelle.<br>Prière d'effectuer l'action manuelle en question et de changer la priorité à 'PRET POUR DEPLOIEMENT'.", "C24OV@biat.com.tn");
                                    afficherLog("!!! SUCCES RECETTE LIVRAISON " + numTicketLivraison + ": Actions manuelles à terminer!");
                                }
                                //versionning
                                if (circuit.equals("RELEASE") || circuit.equals("PROJET") || circuit.equals("UPGRADE")) {
                                    String projet = (String) mapDetails.get("projet");
                                    String resultatVersionning = manObjTools.versionning(liv, circuit, projet, String.valueOf(numTicketLivraison), connectedUser, messageTrac);
                                    messageTrac += resultatVersionning;
                                }
                            }
                            DataBaseTracGenericRequests.updateTicketTracGeneral(Configuration.puLivraisons, numTicketLivraison, "OVTOOLS", messageTrac, newOwner, newPriority, null, newVersion, listeCustomFields);
                        }

                        if (Configuration.livraisonsEnCoursMap.containsKey(String.valueOf(numTicketLivraison))) {
                            Configuration.livraisonsEnCoursMap.remove(String.valueOf(numTicketLivraison));
                        }
                        Thread.sleep(60000);
                    }
                }
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }

    }

    private String callTestAuthentification(String devServerName, String testDepServerName, String connectedUser) {
        String resultatTestAuthentificationGlobal = "";
        boolean pb1 = false;
        EnvironnementDTO envirTestDep = null;
        try {
            envirTestDep = Configuration.environnementDTOMapUserHasEnvironnement.get(connectedUser).get(testDepServerName);
        } catch (Exception ex) {
            pb1 = true;
            resultatTestAuthentificationGlobal += "Problème d'authentification : Vous n'avez pas un utilisateur sur l'environnement <b>" + testDepServerName + "</b>.<br>Veuillez le créer à traver le menu <b>T24 -> Administration -> Profils utilisateurs</b>";
        }
        boolean pb2 = false;
        EnvironnementDTO environnementDev = null;
        try {
            environnementDev = Configuration.environnementDTOMapUserHasEnvironnement.get(connectedUser).get(devServerName);
        } catch (Exception ex) {
            pb2 = true;
            resultatTestAuthentificationGlobal += "Problème d'authentification : Vous n'avez pas un utilisateur sur l'environnement <b>" + devServerName + "</b>.<br>Veuillez le créer à traver le menu <b>T24 -> Administration -> Profils utilisateurs</b>";
        }
        if (!pb1 && !pb2) {
            String[] resTab = GestionLivraisonControlerServlet.testAuthentification(envirTestDep, environnementDev, connectedUser);
            if (resTab != null) {
                resultatTestAuthentificationGlobal = resTab[1];
            }
        }
        return resultatTestAuthentificationGlobal;
    }

    private static void afficherLog(String texte) {
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String date = formatter.format(new Date());
        ManageLogThread.ecrireLogFichier("LOG_RECETTE_AUTOMATIQUE_OV", "\n" + date + "   " + texte);
    }
}
