/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package threads;

import dataBaseTracRequests.DataBaseTracGenericRequests;
import dto.CoupleDTO;
import entitiesMysql.Livraison;
import entitiesTrac.Ticket;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import tools.Configuration;
import tools.DataBaseTools;
import dataBaseTracRequests.DataBaseTracRequests;
import tools.DeploiementParalleleTools;
import tools.ManipulationObjectsTool;
import tools.Tools;

/**
 *
 * @author 04486
 */
public class AutomatisationDeploiementIeThread extends Thread {

    static public Map<String, Boolean> alertEnvoyeeCircuitBloqueCobMap;
    Set<Integer> alertEnvoyeeLivraisonBloqueeSet;
    static public Set<Integer> livraisonsActionManuelleSet;
    public static int compteur = 0;
    public boolean threadIsAlive = true;

    public AutomatisationDeploiementIeThread() {
        alertEnvoyeeCircuitBloqueCobMap = new HashMap<>();
        alertEnvoyeeLivraisonBloqueeSet = new TreeSet<>();
        livraisonsActionManuelleSet = new TreeSet<>();
    }

    @Override
    public void run() {
        while (threadIsAlive) {
            compteur++;
            if (Tools.isTimeToDo("18:30", "18:33", Configuration.HEURE_FORMAT, true)) {
                envoyerRapportEmail();
                try {
                    Thread.sleep(180000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(AutomatisationDeploiementIeThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (!alertEnvoyeeCircuitBloqueCobMap.containsKey("HOTFIX")) {
                alertEnvoyeeCircuitBloqueCobMap.put("HOTFIX", false);
            }
            if (!alertEnvoyeeCircuitBloqueCobMap.containsKey("RELEASE")) {
                alertEnvoyeeCircuitBloqueCobMap.put("RELEASE", false);
            }
            if (!alertEnvoyeeCircuitBloqueCobMap.containsKey("PROJET")) {
                alertEnvoyeeCircuitBloqueCobMap.put("PROJET", false);
            }
            if (!alertEnvoyeeCircuitBloqueCobMap.containsKey("UPGRADE")) {
                alertEnvoyeeCircuitBloqueCobMap.put("UPGRADE", false);
            }
            try {
                afficherLog("------------------ START THREAD DEPLOIEMENT I&E SERVEURS TEST ------------------");
                DataBaseTools dbTools = new DataBaseTools(Configuration.puOvTools);
                Configuration.chargerTousLesCircuitsDetails(dbTools);
                dbTools.closeRessources();

                String connectedUser = "OVTOOLS";
                Map<String, List<Map<String, Object>>> mapPipeTickets = new DataBaseTracRequests().getAllPipeTicketsRequestT24("IE", Configuration.puLivraisons);
                List<Map<String, Object>> listHotfix = mapPipeTickets.get("HOT FIXE TEST");
                List<Map<String, Object>> listActionsAChaudTest = mapPipeTickets.get("ACTION A CHAUD TEST");
                if (listActionsAChaudTest != null) {
                    if (listHotfix != null) {
                        listHotfix.addAll(listActionsAChaudTest);
                    } else {
                        listHotfix = listActionsAChaudTest;
                    }
                }
                List<Map<String, Object>> listRelease = mapPipeTickets.get(Configuration.parametresList.get("phaseRelease"));
                List<Map<String, Object>> listProjet = mapPipeTickets.get("QUALIFICATION_PROJET");

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
                boolean globalOn = false;
                try {
                    globalOn = Configuration.etatCircuitMap.get("GLOBAL").trim().equals("ON");
                } catch (Exception e) {
                }

                if (globalOn) {
                    if (threadIsAlive) {
                        if (!(Configuration.etatCircuitMap.get("HF_DEP_TEST") == null) && Configuration.etatCircuitMap.get("HF_DEP_TEST").trim().equals("ON")) {
                            deploiementDesTicketsIeParCircuit("HOTFIX", connectedUser, listHotfix);
                        } else {
                            afficherLog("!!!!!!!!!!!!!!!! CIRCUIT HOTFIX EN ARRET !!!!!!!!!!!!!!!!");
                        }
                    }
                    if (threadIsAlive) {
                        if (!(Configuration.etatCircuitMap.get("CR_DEP_TEST") == null) && Configuration.etatCircuitMap.get("CR_DEP_TEST").trim().equals("ON")) {
                            deploiementDesTicketsIeParCircuit("RELEASE", connectedUser, listRelease);
                        } else {
                            afficherLog("!!!!!!!!!!!!!!!! CIRCUIT RELEASE EN ARRET !!!!!!!!!!!!!!!!");
                        }
                    }
                    if (threadIsAlive) {
                        if (!(Configuration.etatCircuitMap.get("CP_DEP_TEST") == null) && (Configuration.etatCircuitMap.get("CP_DEP_TEST").trim().equals("ON"))) {
                            deploiementDesTicketsIeParCircuit("PROJET", connectedUser, listProjet);
                        } else {
                            afficherLog("!!!!!!!!!!!!!!!! CIRCUIT PROJET EN ARRET !!!!!!!!!!!!!!!!");
                        }
                    }
                    if (threadIsAlive) {
                        if (!(Configuration.etatCircuitMap.get("CU_DEP_TEST") == null) && Configuration.etatCircuitMap.get("CU_DEP_TEST").trim().equals("ON")) {
                            deploiementDesTicketsIeParCircuit("UPGRADE", connectedUser, listUpgrade);
                        } else {
                            afficherLog("!!!!!!!!!!!!!!!! CIRCUIT UPGRADE EN ARRET !!!!!!!!!!!!!!!!");
                        }
                    }
                }

            } catch (Exception exep) {
                String msg = "";
                if (Configuration.etatCircuitMap == null) {
                    msg = "Configuration.etatCircuitMap = null";
                } else {
                    for (Map.Entry<String, String> entry : Configuration.etatCircuitMap.entrySet()) {
                        msg += entry.getKey() + " " + entry.getValue() + "\n";
                    }
                }
                exep.printStackTrace();
                tools.Tools.traiterException(msg + tools.Tools.getStackTrace(exep));
            }
            try {
                //sleep très important, à ne pas enlever
                Thread.sleep(60000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ManageLogThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void deploiementDesTicketsIeParCircuit(String circuit, String connectedUser, List<Map<String, Object>> listTickets) {
        try {
            if (listTickets != null) {

                Set<Map<String, Object>> setTickets = new HashSet<>();
                if (listTickets != null && !listTickets.isEmpty()) {
                    setTickets.addAll(listTickets);

                    String log = "###CIRCUIT:" + circuit + ": liste des tickets (";
                    String removedTickets = "";
                    for (Map<String, Object> mapDetails : listTickets) {
                        Ticket ticket = (Ticket) mapDetails.get("Ticket");
                        Integer numTicketLivraison = ticket.getId();
                        log += numTicketLivraison + ": ";

                        //Supprimer les tickets contenants des actions manuelles à partir de la liste initiale 
                        if (livraisonsActionManuelleSet.contains(numTicketLivraison)) {
                            setTickets.remove(mapDetails);
                            removedTickets += numTicketLivraison.toString() + ", ";
                            if (Configuration.livraisonsEnCoursMapIE.containsKey(String.valueOf(numTicketLivraison))) {
                                Configuration.livraisonsEnCoursMapIE.remove(String.valueOf(numTicketLivraison));
                            }
                        }
                    }
                    log += ")";
                    afficherLog(log);
                    afficherLog("Tickets contenants des actions manuelles: " + removedTickets);
                }

                if (setTickets != null && !setTickets.isEmpty()) {
                    String[] resTab = null;
                    String[] tabEnvNameDestinationDeploiement = null;
                    String[] tabEnvNameDestinationDeploiementLivraison = null;
                    String envConcat = null;
                    String environnementSourceName = null;
                    if (circuit.equals("HOTFIX")) {
                        envConcat = Configuration.parametresList.get("environnementsCircuitHotfix");
                        environnementSourceName = "ASS";
                    } else if (circuit.equals("RELEASE")) {
                        envConcat = Configuration.parametresList.get("environnementsCircuitRelease");
                        environnementSourceName = "ASS";
                    } else if (circuit.equals("PROJET")) {
                        envConcat = Configuration.parametresList.get("environnementsCircuitProjet");
                        environnementSourceName = "ASS2";
                    } else if (circuit.equals("UPGRADE")) {
                        envConcat = Configuration.parametresList.get("environnementsCircuitUpgrade");
                        environnementSourceName = "ASSU";
                    }
                    tabEnvNameDestinationDeploiement = Tools.separerEnvironnementsNoms(envConcat);
                    //test exécution cob
                    resTab = new DeploiementParalleleTools().testCobEnCours(tabEnvNameDestinationDeploiement, connectedUser);
                    if (resTab != null) {
                        if (alertEnvoyeeCircuitBloqueCobMap.get(circuit) == false) {
                            alertParEmail(null, "Activité des livraisons Bloquée", "Bonjour,<br>L'activité des livraisons sur le circuit " + circuit + " est bloquée " + resTab[1]);
                            afficherLog("!!! Activité Bloquée sur le circuit " + circuit + " :COB en cours, " + resTab[1]);
                            alertEnvoyeeCircuitBloqueCobMap.put(circuit, true);
                        }
                        return;
                    } else {
                        alertEnvoyeeCircuitBloqueCobMap.put(circuit, false);
                        for (Map<String, Object> mapDetails : setTickets) {
                            if (!threadIsAlive) {
                                break;
                            }
                            boolean messageAlreadyInserred = false;
                            List<String> auxEnvDeploiement = new LinkedList<String>(Arrays.asList(tabEnvNameDestinationDeploiement));
                            if (circuit.equals("HOTFIX")) {
                                try {
                                    String hfSurCrt = (String) mapDetails.get("hf_sur_crt");
                                    if (hfSurCrt.equals("1")) {
                                        auxEnvDeploiement.add("CRT");
                                    }
                                } catch (Exception ex) {
                                }
                            }
                            tabEnvNameDestinationDeploiementLivraison = auxEnvDeploiement.toArray(new String[auxEnvDeploiement.size()]);
                            Ticket ticket = (Ticket) mapDetails.get("Ticket");
                            Integer numTicketLivraison = ticket.getId();
                            DataBaseTools dbTools = new DataBaseTools(Configuration.puOvTools);
                            Livraison liv = dbTools.em.find(Livraison.class, numTicketLivraison);
                            dbTools.closeRessources();
                            afficherLog("!!! START DEPLOIEMENT LIVRAISON " + numTicketLivraison);
                            if (liv == null || liv.getLivrables() == null) {
                                if (!alertEnvoyeeLivraisonBloqueeSet.contains(numTicketLivraison)) {
                                    alertParEmail(ticket, "Activité des livraisons Bloquée", "Bonjour,<br>L'activité des livraisons sur le circuit " + circuit + " est bloquée, prière de passer la livraison <a href='http://172.28.70.74/trac/livraisons_t24/ticket/" + numTicketLivraison + "'>#" + numTicketLivraison + "</a> manuellement.");
                                    afficherLog("!!! ECHEC DEPLOIEMENT LIVRAISON " + numTicketLivraison + ": Ticket à traiter manuellement!");
                                    ecrireJournal("DEPLOIEMENT_MANUEL", circuit, mapDetails);
                                }
                                alertEnvoyeeLivraisonBloqueeSet.add(numTicketLivraison);
                                afficherLog("liv == null || liv.getLivrables() == null  => QUIT");
                                return;
                            }
                            if (livraisonsActionManuelleSet.contains(liv.getNumeroLivraison())) {
                                afficherLog("livraisonsActionManuelleList contien " + liv.getNumeroLivraison() + "  =>  QUIT");
                                if (Configuration.livraisonsEnCoursMapIE.containsKey(String.valueOf(numTicketLivraison))) {
                                    Configuration.livraisonsEnCoursMapIE.remove(String.valueOf(numTicketLivraison));
                                }
                                continue;
                            }
                            Configuration.livraisonsEnCoursMapIE.put(numTicketLivraison.toString(), "OVTOOLS: Déploiement automatique");
                            ManipulationObjectsTool manObjTools = new ManipulationObjectsTool();
                            Map<String, String[]> resultMap = manObjTools.traiterLivrable(liv, "IE", connectedUser, tabEnvNameDestinationDeploiementLivraison, environnementSourceName, true);

                            String problemesDeploiementsString = null;
                            if (resultMap.containsKey("PROBLEME") && resultMap.get("PROBLEME")[0].length() > 0 && resultMap.get("PROBLEME")[1].length() > 0) {
                                //problème d'authentification, prob d'existance de pack ou ...
                                resTab = resultMap.get("PROBLEME");
                                //return Tools.redirectionPageErreurs(resTab[0], resTab[1], mapping, request, response, connectedUser);
                                if (!alertEnvoyeeLivraisonBloqueeSet.contains(liv.getNumeroLivraison())) {
                                    String msg = "Bonjour,<br>L'activité des livraisons sur le circuit " + circuit + " est bloquée au niveau de la livraison <a href='http://172.28.70.74/trac/livraisons_t24/ticket/" + numTicketLivraison + "'>#" + numTicketLivraison + "</a>. Voici le message d'erreur: ";
                                    msg += resTab[1];
                                    alertParEmail(ticket, "Activité des livraisons Bloquée", msg);
                                    afficherLog("!!! ECHEC DEPLOIEMENT LIVRAISON " + numTicketLivraison + ": message d'erreur:" + msg);
                                    DataBaseTracGenericRequests.updateTicketTracGeneral(Configuration.puLivraisons, numTicketLivraison, "OVTOOLS", "\n\n{{{\n#!html\n<b>Activité des livraisons Bloquée</b><br>" + msg + "\n}}}", null, null, null, null, null);
                                }
                                alertEnvoyeeLivraisonBloqueeSet.add(liv.getNumeroLivraison());

                                if (Configuration.livraisonsEnCoursMapIE.containsKey(String.valueOf(numTicketLivraison))) {
                                    Configuration.livraisonsEnCoursMapIE.remove(String.valueOf(numTicketLivraison));
                                }

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
                                    newPriority = "PROBLEME DE DEPLOIEMENT";
                                    newOwner = "raafet dormok";
                                    alertParEmail(ticket, "Problème de déploiement " + liv.getNumeroLivraison(), "Bonjour,<br>La livraison <a href='http://172.28.70.74/trac/livraisons_t24/ticket/" + numTicketLivraison + "'>#" + numTicketLivraison + "</a> présente un problème de déploiement.");
                                    afficherLog("!!! ECHEC DEPLOIEMENT LIVRAISON " + numTicketLivraison + ": Problème de déploiement!");
                                    ecrireJournal("DEPLOIEMENT_AUTOMATIQUE", circuit, mapDetails);
                                } else {
                                    if (!messageTrac.contains("<span class='errorBold'> (A traiter manuellement)") && !messageTrac.contains("Ce ticket sera traite manuellement")) {
                                        //si il n'existe pas d'action manuelle non traitée
                                        newPriority = "DEPLOYEE";
                                        alertParEmail(ticket, "Déploiement automatique réussi"
                                                + "", "Bonjour,<br>La livraison <a href='http://172.28.70.74/trac/livraisons_t24/ticket/" + numTicketLivraison + "'>#" + numTicketLivraison + "</a> a été déployée automatiquement sur les environnements de test du circuit " + circuit + " avec succès.", "safwen.bensaid@biat.com.tn");
                                        afficherLog("!!! SUCCES DEPLOIEMENT LIVRAISON " + numTicketLivraison);
                                        ecrireJournal("DEPLOIEMENT_AUTOMATIQUE", circuit, mapDetails);
                                    } else {
                                        //si il existe une action manuelle non traitée
                                        if (livraisonsActionManuelleSet.contains(liv.getNumeroLivraison())) {
                                            messageAlreadyInserred = true;
                                        } else {
                                            livraisonsActionManuelleSet.add(liv.getNumeroLivraison());
                                        }
                                        Configuration.livraisonsEnCoursMapIE.remove(String.valueOf(numTicketLivraison));
                                        alertParEmail(ticket, "Activité des livraisons Bloquée", "Bonjour,<br>L'activité des livraisons sur le circuit " + circuit + " est bloquée, car la livraison <a href='http://172.28.70.74/trac/livraisons_t24/ticket/" + numTicketLivraison + "'>#" + numTicketLivraison + "</a> contient une action manuelle.<br>Prière d'effectuer l'action manuelle en question et de changer la priorité à 'DEPLOYEE'.");
                                        afficherLog("!!! SUCCES DEPLOIEMENT LIVRAISON " + numTicketLivraison + ": Actions manuelles à terminer!");
                                        ecrireJournal("DEPLOIEMENT_MANUEL", circuit, mapDetails);
                                    }
                                }
                                if (Configuration.livraisonsEnCoursMapIE.containsKey(String.valueOf(numTicketLivraison))) {
                                    Configuration.livraisonsEnCoursMapIE.remove(String.valueOf(numTicketLivraison));
                                }

                                if (messageAlreadyInserred == false) {
                                    DataBaseTracGenericRequests.updateTicketTracGeneral(Configuration.puLivraisons, numTicketLivraison, "OVTOOLS", messageTrac, newOwner, newPriority, null, newVersion, listeCustomFields);
                                }
                            }
                        }

                    }
                }
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }

    public static void alertParEmail(Ticket ticket, String titre, String emailContent, String... dest) {
        String mailOwner = "";
        String mailReporter = "";
        if (ticket != null) {
            String owner = ticket.getCc();
            String reporter = ticket.getReporter();

            try {
                mailOwner = Configuration.usersMap.get(owner).getEmail();
            } catch (Exception e1) {
                Tools.traiterException("Ticket:" + ticket.toString() + "<br><br><br>" + tools.Tools.getStackTrace(e1) + "<br><br><br>l utilisateur : " + owner + " n est pas declare dans la base de donnees de parametrages, veuillez l ajouter");
            }
            try {
                mailReporter = Configuration.usersMap.get(reporter).getEmail();
            } catch (Exception e1) {
                Tools.traiterException("Ticket:" + ticket.getId() + "\n\n\n" + tools.Tools.getStackTrace(e1) + "\nl utilisateur : " + reporter + " n est pas declare dans la base de donnees de parametrages, veuillez l ajouter");
            }
        }
        System.out.println("*********************************");
        String[] a = null;
        String[] cc = null;
        String[] cci = null;
        if (dest.length > 0) {
            a = dest;
            cc = new String[]{};
            cci = new String[]{};
        } else {
            a = new String[]{"sinda.rahmouni@biat.com.tn", "mongi.guesmi@biat.com.tn"};
            cc = new String[]{"riadh.bendakhlia@biat.com.tn", "C24OV@biat.com.tn", mailReporter, mailOwner};
            cci = new String[]{};
        }

        tools.Tools.sendEMail(titre, "OV.Management.Solutions@biat.com.tn", a, cc, cci, emailContent, false);
        System.out.println("*********************************");
    }

    private void ecrireJournal(String filename, String circuit, Map<String, Object> mapDetails) {
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String date = formatter.format(new Date());
        Ticket ticket = (Ticket) mapDetails.get("Ticket");
        Integer livraison = ticket.getId();
        String contenuLivrables = "";
        try {
            contenuLivrables = mapDetails.get("contenu_des_livrables").toString();
        } catch (Exception ex) {
        }
        String anomalie = mapDetails.get("ticket_origine").toString();
        String projet = mapDetails.get("projet").toString();
        String deliveryTimeIE = mapDetails.get("deliveryTimeIE").toString();
        String responsable = mapDetails.get("responsable").toString();
        String texte = circuit + " # " + livraison + " # " + anomalie + " # " + projet + " # " + contenuLivrables + " # " + deliveryTimeIE + " # " + date + " # " + responsable + "\n";
        ManageLogThread.ecrireLogFichier(filename, texte);
    }

    private static void afficherLog(String texte) {
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String date = formatter.format(new Date());
        ManageLogThread.ecrireLogFichier("LOG_DEPLOIEMENT_AUTOMATIQUE_IE", "\n" + date + "   " + texte);
    }

    public void envoyerRapportEmail() {
        String deploiementAutomatique = "DEPLOIEMENT_AUTOMATIQUE";
        String deploiementManuel = "DEPLOIEMENT_MANUEL";
        DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        String date = formatter.format(new Date());
        String deploiementAutomatiqueFile = Configuration.parametresList.get("espaceLocal") + System.getProperty("file.separator") + "LOGS" + System.getProperty("file.separator") + date + System.getProperty("file.separator") + deploiementAutomatique + ".txt";
        String deploiementManuelFile = Configuration.parametresList.get("espaceLocal") + System.getProperty("file.separator") + "LOGS" + System.getProperty("file.separator") + date + System.getProperty("file.separator") + deploiementManuel + ".txt";
        List<String[]> livraisonsAutomatiquesDetailsList = lectureFichierLog(deploiementAutomatiqueFile);
        List<String[]> livraisonsManuellesDetailsList = lectureFichierLog(deploiementManuelFile);
        int nbrLivAutomatiques = livraisonsAutomatiquesDetailsList.size();
        int nbrLivManuelles = livraisonsManuellesDetailsList.size();
        int nbrLivGlobal = nbrLivAutomatiques + nbrLivManuelles;
        if (nbrLivGlobal > 0) {
            float pourcentage = nbrLivAutomatiques * 100 / nbrLivGlobal;
            String emailContent = generationTableHtml(livraisonsAutomatiquesDetailsList, livraisonsManuellesDetailsList, nbrLivAutomatiques, nbrLivManuelles, pourcentage);
            tools.Tools.sendEMail("Rapport d'automatisation des livraisons T24", "OV.Management.Solutions@biat.com.tn", new String[]{"CH_DirProj@biat.com.tn", "C24OV@biat.com.tn", "Pilier.technique@biat.com.tn"}, new String[]{}, new String[]{"sinda.rahmouni@biat.com.tn"}, emailContent, false);
        }
    }

    private static List<String[]> lectureFichierLog(String fileName) {
        //lecture du fichier texte
        List<String[]> livraisonDetailsList = new ArrayList<>();
        try {
            InputStream ips = new FileInputStream(fileName);
            InputStreamReader ipsr = new InputStreamReader(ips);
            BufferedReader br = new BufferedReader(ipsr);
            String ligne;
            while ((ligne = br.readLine()) != null) {
                String[] livraisonDetailsArray = ligne.split(" # ");
                livraisonDetailsList.add(livraisonDetailsArray);
            }
            br.close();
        } catch (Exception e) {
            //fichier non existant
        }
        return livraisonDetailsList;
    }

    private static String generationTableHtml(List<String[]> livraisonsAutomatiquesDetailsList, List<String[]> livraisonsManuellesDetailsList, int nbrLivAutomatiques, int nbrLivManuelles, float pourcentage) {
        StringBuilder sbResult = new StringBuilder();
        sbResult.append("\n<style type='text/css'>");
        sbResult.append("\n    .thTable, .tdTable{");
        sbResult.append("\n        border-left: 1px solid #35427E;");
        sbResult.append("\n        border-top: 1px solid #35427E;");
        sbResult.append("\n    }");
        sbResult.append("\n    table{");
        sbResult.append("\n        border-right: 1px solid #35427E;");
        sbResult.append("\n        border-bottom: 1px solid #35427E;");
        sbResult.append("\n        font-size:13px;");
        sbResult.append("\n    }");
        sbResult.append("\n    a{");
        sbResult.append("\n        text-decoration: underline;");
        sbResult.append("\n        cursor: pointer;");
        sbResult.append("\n        color:#1e0fbe;");
        sbResult.append("\n    }");
        sbResult.append("\n    .message{");
        sbResult.append("\n        color: #666666;");
        sbResult.append("\n    }");
        sbResult.append("\n</style>");

        sbResult.append("\n<p class='message'>");
        sbResult.append("\n    Bonjour,");
        sbResult.append("\n</p>");
        sbResult.append("\n<p class='message'>");
        sbResult.append("Ci-dessous les livraisons qui ont été déployées aujourd'hui sur les environnements de test.");
        sbResult.append("\n</p>");
        sbResult.append("\n");
        sbResult.append("<ul>");
        sbResult.append("<li>Nombre de livraisons déployées automatiquement: ").append(nbrLivAutomatiques).append("</li>");
        sbResult.append("<li>Nombre de livraisons déployées manuellement: ").append(nbrLivManuelles).append("</li>");
        sbResult.append("<li>Pourcentage d'automatisation: ").append(pourcentage).append("%</li></ul>");
        if (nbrLivAutomatiques > 0) {
            sbResult.append("<ul><li>Liste des livraisons déployées automatiquement:</li></ul></p><p></p>");
            sbResult.append("\n");
            sbResult.append("\n<table cellspacing='0' cellpadding='5' >");
            sbResult.append("\n    <thead>");
            sbResult.append("\n        <tr style='background-color: #FDD999; color: #35427E'>");

            sbResult.append("\n            <th class='thTable'>Livraison</th>");
            sbResult.append("\n            <th class='thTable'>Anomalie</th>");
            sbResult.append("\n            <th class='thTable'>Circuit</th>");
            sbResult.append("\n            <th class='thTable'>Projet</th>");
            sbResult.append("\n            <th class='thTable'>Contenu des livrables</th>");
            sbResult.append("\n            <th class='thTable'>Date de réception</th>");
            sbResult.append("\n            <th class='thTable'>Date de déploiement</th>");
            sbResult.append("\n            <th class='thTable'>Responsable</th>");
            sbResult.append("\n            <th style='padding: 0px;border: none'></th>");
            sbResult.append("\n        </tr>");
            sbResult.append("\n    </thead>");
            sbResult.append("\n    <tbody>");
            for (String[] livraisonDetailsArray : livraisonsAutomatiquesDetailsList) {
                String circuit = livraisonDetailsArray[0];
                String livraison = livraisonDetailsArray[1];
                String anomalie = livraisonDetailsArray[2];
                String projet = livraisonDetailsArray[3];
                String contenuDesLivrables = livraisonDetailsArray[4];
                String dateDeLivraison = livraisonDetailsArray[5];
                String dateDeDeploiement = livraisonDetailsArray[6];
                String responsable = livraisonDetailsArray[7];
                sbResult.append("\n        <tr style='background-color: #faebd7; color: #666666'>");
                sbResult.append("\n            <td class='tdTable'><a href='http://172.28.70.74/trac/livraisons_t24/ticket/").append(livraison).append("' >#").append(livraison).append("</a></td>");
                sbResult.append("\n            <td class='tdTable'><a href='http://172.28.70.74/trac/anomalies_t24/ticket/").append(anomalie).append("' >#").append(anomalie).append("</a></td>");
                sbResult.append("\n            <td class='tdTable'>").append(circuit).append("</td>");
                sbResult.append("\n            <td class='tdTable'>").append(projet).append("</td>");
                sbResult.append("\n            <td class='tdTable'>").append(contenuDesLivrables).append("</td>");
                sbResult.append("\n            <td class='tdTable'>").append(dateDeLivraison).append("</td>");
                sbResult.append("\n            <td class='tdTable'>").append(dateDeDeploiement).append("</td>");
                sbResult.append("\n            <td class='tdTable'>").append(responsable).append("</td>");
                sbResult.append("\n            <td style='padding: 0px;border: none'></td>");
                sbResult.append("\n        </tr>");
            }
            sbResult.append("\n    </tbody>");
            sbResult.append("\n</table>");
        }
        if (nbrLivManuelles > 0) {
            sbResult.append("<ul><li>Liste des livraisons déployées manuellement (présentant des actions manuelles):</li></ul></p><p></p>");
            sbResult.append("\n");
            sbResult.append("\n<table cellspacing='0' cellpadding='5' >");
            sbResult.append("\n    <thead>");
            sbResult.append("\n        <tr style='background-color: #FDD999; color: #35427E'>");
            sbResult.append("\n            <th class='thTable'>Livraison</th>");
            sbResult.append("\n            <th class='thTable'>Anomalie</th>");
            sbResult.append("\n            <th class='thTable'>Circuit</th>");
            sbResult.append("\n            <th class='thTable'>Projet</th>");
            sbResult.append("\n            <th class='thTable'>Contenu des livrables</th>");
            sbResult.append("\n            <th class='thTable'>Date de réception</th>");
            sbResult.append("\n            <th class='thTable'>Date de déploiement</th>");
            sbResult.append("\n            <th class='thTable'>Responsable</th>");
            sbResult.append("\n            <th style='padding: 0px;border: none'></th>");
            sbResult.append("\n        </tr>");
            sbResult.append("\n    </thead>");
            sbResult.append("\n    <tbody>");
            for (String[] livraisonDetailsArray : livraisonsManuellesDetailsList) {
                String circuit = livraisonDetailsArray[0];
                String livraison = livraisonDetailsArray[1];
                String anomalie = livraisonDetailsArray[2];
                String projet = livraisonDetailsArray[3];
                String contenuDesLivrables = livraisonDetailsArray[4];
                String dateDeLivraison = livraisonDetailsArray[5];
                String dateDeDeploiement = livraisonDetailsArray[6];
                String responsable = livraisonDetailsArray[7];
                sbResult.append("\n        <tr style='background-color: #faebd7; color: #666666'>");
                sbResult.append("\n            <td class='tdTable'><a href='http://172.28.70.74/trac/livraisons_t24/ticket/").append(livraison).append("' >#").append(livraison).append("</a></td>");
                sbResult.append("\n            <td class='tdTable'><a href='http://172.28.70.74/trac/anomalies_t24/ticket/").append(anomalie).append("' >#").append(anomalie).append("</a></td>");
                sbResult.append("\n            <td class='tdTable'>").append(circuit).append("</td>");
                sbResult.append("\n            <td class='tdTable'>").append(projet).append("</td>");
                sbResult.append("\n            <td class='tdTable'>").append(contenuDesLivrables).append("</td>");
                sbResult.append("\n            <td class='tdTable'>").append(dateDeLivraison).append("</td>");
                sbResult.append("\n            <td class='tdTable'>").append(dateDeDeploiement).append("</td>");
                sbResult.append("\n            <td class='tdTable'>").append(responsable).append("</td>");
                sbResult.append("\n            <td style='padding: 0px;border: none'></td>");
                sbResult.append("\n        </tr>");
            }
            sbResult.append("\n    </tbody>");
            sbResult.append("\n</table>");
        }
        sbResult.append("\n<br>");
        sbResult.append("\n<p style='font-size: 11px;color:#1f497d'>");
        sbResult.append("\n    Ceci est un message automatique, merci de ne pas y répondre.");
        sbResult.append("\n</p>");
        sbResult.append("\n--");
        sbResult.append("\n<p style='margin:0cm;margin-bottom:.0001pt;background:white'>");
        sbResult.append("\n    <b>");
        sbResult.append("\n        <span style='font-size:9.0pt;font-family:Arial,sans-serif;color:#1f497d'>");
        sbResult.append("\n            <a href='http://172.28.70.74:8090/OVTOOLS'>OV Management Solutions</a>");
        sbResult.append("\n        </span>");
        sbResult.append("\n    </b>");
        sbResult.append("\n</p>");
        sbResult.append("\n<p style='margin:0cm 0cm 0.0001pt;background-color:white;background-repeat:initial'>");
        sbResult.append("\n    <span style='font-size:10.0pt;font-family:Arial,sans-serif;color:#1f497d'>");
        sbResult.append("\n        Direction Méthodes et Outils");
        sbResult.append("\n    </span>");
        sbResult.append("\n</p>");
        return sbResult.toString();
    }
}
