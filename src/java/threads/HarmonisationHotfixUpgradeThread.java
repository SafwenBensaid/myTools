/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package threads;

import dataBaseTracRequests.AppelRequetes;
import dataBaseTracRequests.DataBaseTracGenericRequests;
import static dataBaseTracRequests.DataBaseTracGenericRequests.getTicketById;
import dto.AutomatisationHarmonisationUpgradeDTO;
import dto.CoupleDTO;
import dto.QuadripleDTO;
import dto.StructureResultatAnalyseIntersectionDTO;
import dto.livraison.StreamServeBatch;
import dto.livraison.StreamServeTransactionnel;
import dto.livraison.T24;
import entitiesMysql.Livraison;
import entitiesTrac.Ticket;
import entitiesTrac.TicketCustom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Query;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;
import static servlets.GestionDemandesMetiersServlet.objectsListToBePersistedOnTheDataBaseTrac;
import strutsActions.EtudeIntersectionInputObjetsAction;
import tools.Configuration;
import tools.DataBaseTools;
import tools.EtudeIntersectionTools;
import tools.GetHotfixesPipesTool;
import tools.Tools;

/**
 *
 * @author trac.admin
 */
public class HarmonisationHotfixUpgradeThread extends Thread {

    private String harmonisationCU;
    private String startHarmonisationHfUpgradeDateString;
    public boolean threadIsAlive = true;
    private List<AutomatisationHarmonisationUpgradeDTO> listDetailsLivraisonsEmailUpgrade = new ArrayList<>();
    private Map<Integer, Map<String, Object>> ticketMapHfAdaptationCU = null;

    @Override
    public void run() {
        while (threadIsAlive) {
            try {
                String connectedUser = "OVTOOLS";
                Map<String, List<Livraison>> mapLivraisons = new GetHotfixesPipesTool().getAllHotfixesAharmoniserUpgrade();
                List<Livraison> listeHARMONISATION_UPGRADE = mapLivraisons.get("HARMONISATION_UPGRADE");
                initialisationVariablesUpgrade();

                if (harmonisationCU.trim().equals("ON") && !listeHARMONISATION_UPGRADE.isEmpty()) {
                    //if (Tools.isTimeToDo(startHarmonisationHfUpgradeDateString, null, Configuration.HEURE_FORMAT, true)) {
                    afficherLog("---------- LANCEMENT HARMONISATION DES HOTFIX SUR CU----------");
                    EtudeIntersectionInputObjetsAction etudeIntersection = new EtudeIntersectionInputObjetsAction();
                    Tools tools = new Tools();
                    EtudeIntersectionTools etudeIntersectionTools = null;
                    QuadripleDTO quadripleDTO = null;
                    System.out.println(listeHARMONISATION_UPGRADE.size() + " tickets à harmoniser UPGRADE:");
                    for (Livraison parentHF : listeHARMONISATION_UPGRADE) {
                        afficherLog("HARMONISATION UPGRADE ticket #" + parentHF.getNumeroLivraison());
                        System.out.println("HARMONISATION UPGRADE ticket #" + parentHF.getNumeroLivraison());
                        if (parentHF.getLivrables() != null && parentHF.isHarm1probleme() == false) {
                            StringBuilder resultatGlobal = new StringBuilder();
                            //Etude d'intersection 
                            etudeIntersectionTools = new EtudeIntersectionTools(String.valueOf(parentHF.getNumeroLivraison()));
                            String resultatEtudeIntersectionCU = "";
                            String resultatEtudeConversionCU = "";
                            String objectsList = "";
                            //Etude d'intersection STREAMSERVE
                            for (Object livDto : parentHF.getLivrables()) {
                                if (livDto instanceof T24) {
                                    T24 livT24 = (T24) livDto;
                                    objectsList = "";
                                    try {
                                        objectsList = etudeIntersectionTools.traiterEtudeIntersectionInput(null, livT24.getPackName(), "ASS", connectedUser, "PACK");
                                    } catch (Exception exep) {
                                        exep.printStackTrace();
                                        Tools.traiterException(Tools.getStackTrace(exep));
                                    }
                                    if (!objectsList.contains("Dossier inéxistant")) {
                                        //etude intersection
                                        quadripleDTO = etudeIntersection.etudeIntersection(etudeIntersectionTools, tools, connectedUser, objectsList);
                                        resultatEtudeIntersectionCU += quadripleDTO.getValeur3().trim();
                                        //Etude conversion R17
                                        List<String> listObjetsConversionManuelle = getListObjetsConversionManuelle();
                                        List<StructureResultatAnalyseIntersectionDTO> listObjets = etudeIntersectionTools.separerObjets(objectsList);
                                        List<String> listObjetsHotfixConversionManuelle = new ArrayList<>();
                                        List<String> listObjetsHotfixConversionAutomatique = new ArrayList<>();
                                        for (StructureResultatAnalyseIntersectionDTO objetT24 : listObjets) {
                                            String objetHotfix = objetT24.getTypeObj() + ">" + objetT24.getNomObj();
                                            if (listObjetsConversionManuelle.contains(objetHotfix)) {
                                                listObjetsHotfixConversionManuelle.add(objetHotfix);
                                            }
                                            if (objetT24.getTypeObj().endsWith(".BP")) {
                                                listObjetsHotfixConversionAutomatique.add(objetHotfix);
                                            }
                                        }
                                        if (!listObjetsHotfixConversionManuelle.isEmpty()) {
                                            resultatEtudeConversionCU += "<br> Les objets ci-dessous nécessitent une conversion UPGRADE manuelle : <br><b>";
                                            for (String objetT24 : listObjetsHotfixConversionManuelle) {
                                                resultatEtudeConversionCU += "<br>" + objetT24 + "<br>";
                                            }
                                            resultatEtudeConversionCU += "</b>";
                                        }
                                        if (!listObjetsHotfixConversionAutomatique.isEmpty()) {
                                            resultatEtudeConversionCU += "<br> Les objets ci-dessous nécessitent une conversion UPGRADE Automatique : <br><b>";
                                            for (String objetT24 : listObjetsHotfixConversionAutomatique) {
                                                resultatEtudeConversionCU += "<br>" + objetT24 + "<br>";
                                            }
                                            resultatEtudeConversionCU += "</b>";
                                        }
                                    }
                                } else if (livDto instanceof StreamServeTransactionnel || livDto instanceof StreamServeBatch) {
                                    //si field-contenu_des_livrables = STREAMSERV TRANSACTIONNEL => intersection CU
                                    resultatEtudeIntersectionCU += "A adapter la version STREAMSERV TRANSACTIONNEL sur le C.U";
                                }
                            }
                            parentHF.setResultatetudeintersectionCU(resultatEtudeIntersectionCU);
                            afficherLog("Resultat etude intersection CU ticket #" + parentHF.getNumeroLivraison());
                            afficherLog(resultatEtudeIntersectionCU);
                            afficherLog("Resultat etude Conversion CU ticket #" + parentHF.getNumeroLivraison());
                            afficherLog(resultatEtudeConversionCU);
                            DataBaseTools dbTools = new DataBaseTools(Configuration.puOvTools);
                            dbTools.update(parentHF);
                            dbTools.closeRessources();
                            resultatEtudeIntersectionCU = resultatEtudeIntersectionCU.trim();
                            resultatEtudeConversionCU = resultatEtudeConversionCU.trim();
                            String[] intersectionArray = null;
                            if (resultatEtudeIntersectionCU.length() == 0) {
                                resultatEtudeIntersectionCU = "<br>Aucune intersection<br>";
                            }
                            if (resultatEtudeConversionCU.length() == 0) {
                                resultatEtudeConversionCU = "<br>Aucun objet à adapter à travers une conversion UPGRADE manuelle<br>";
                            }
                            //fin
                            resultatGlobal.append("\n== Résultat de l'étude d'intersection avec le cicruit upgrade C.U ").append(":==").append(" \n{{{\n#!html\n");
                            resultatGlobal.append(resultatEtudeIntersectionCU).append("\n}}}\n\n");
                            resultatGlobal.append("\n== Résultat de l'étude de conversion pour UPGRADE ").append(":==").append(" \n{{{\n#!html\n");
                            resultatGlobal.append(resultatEtudeConversionCU).append("\n}}}\n\n");

                            //Getting Custom Fields for parentHF
                            List<Integer> numLivraisonsList = new ArrayList<>();
                            numLivraisonsList.add(parentHF.getNumeroLivraison());
                            String[] cles = new String[]{"ticket_origine", "inter_cr", "inter_cp", "projet", "contenu_des_livrables", "ticket_appl_prod"};
                            Map<Integer, Map<String, Object>> ticketCustomLivraisonsMap = AppelRequetes.getTicketCustomByTicketIdAndNames(numLivraisonsList, Configuration.puLivraisons, Configuration.tracLivraisons, cles);

                            //////////// Creation ticket Harm Upgrade ////////////
                            Integer filsHF = createTicketHarmonisation(ticketCustomLivraisonsMap.get(parentHF.getNumeroLivraison()), "LIVRAISON D'HARMONISATION DU CIRCUIT UPGRADE " + parentHF.getNumeroLivraison(), "HARMONISATION_C.UPGRADE");
                            //////////// updateTicketTrac ////////////
                            String old_t_liv = ticketCustomLivraisonsMap.get(parentHF.getNumeroLivraison()).get("ticket_appl_prod").toString();
                            updateTicketTracOV(parentHF, filsHF, "OVTOOLS", resultatGlobal.toString(), old_t_liv, intersectionArray);

                            //preparer DTO envoi email to CDD
                            AutomatisationHarmonisationUpgradeDTO ticketDto = new AutomatisationHarmonisationUpgradeDTO("DEVU", parentHF.getNumeroLivraison().toString(), parentHF.getNumeroAnomalie().toString(), filsHF.toString(), "pathPackSauvegarde", objectsList.replaceAll("\n", "<br>"), "", resultatEtudeIntersectionCU, resultatEtudeConversionCU);
                            listDetailsLivraisonsEmailUpgrade.add(ticketDto);
                            try {
                                afficherLog("4- sleep(1000*10*1)");
                                Thread.sleep(1000 * 60 * 1);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(ManageLogThread.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else {
                            Tools.traiterException("Attention, le Hotfix num #" + parentHF.getNumeroLivraison() + " n'a pas été harmonisée car il a été livré par le développeur manuellement, prière de le traiter manuellement.");
                        }
                    }

                    genererMessageHarmonisationUPGRADE();

                    //harmonistaion des tickets défalqués
                    //afficherLog("---------- PHASE2: HARMONISATION DES ENVIRONNEMENTS DE TEST ----------");
                    //harmonistaionDesTicketsDefalques(connectedUser);
                    //afficherLog("---------- PHASE3: HARMONISATION DES ENVIRONNEMENTS DE DEV ----------");
                    //harmonisationHotfixparCDD(connectedUser);
                    //}
                } else {
                    afficherLog("---------- HARMONISATION DES HOTFIX EN ARRET ----------");
                }
                try {
                    //sleep très important, à ne pas enlever
                    Thread.sleep(50000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ManageLogThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (Exception exep) {
                exep.printStackTrace();
                Tools.traiterException(Tools.getStackTrace(exep));
            }
        }
    }

    private void initialisationVariablesUpgrade() {
        try {
            DataBaseTools dbTools = new DataBaseTools(Configuration.puOvTools);
            Configuration.chargerTousLesCircuitsDetails(dbTools);
            dbTools.closeRessources();
            //Récupérer variables a patrir de la base de donnée 
            startHarmonisationHfUpgradeDateString = Configuration.etatCircuitMap.get("START_HARMONISATION_HF_UPGRADE");
            harmonisationCU = Configuration.etatCircuitMap.get("HARMONISATION_CU");
        } catch (Exception exep) {
            exep.printStackTrace();
            Tools.traiterException(Tools.getStackTrace(exep));
        }
    }

    public static List<String> getListObjetsConversionManuelle() {
        List<String> listObjetsConversionManuelle = new ArrayList<>();
        listObjetsConversionManuelle.add("BIAT.BP>BIAT.AGC.PCA.EXTRACT.OPPOSITION.CHQ");
        listObjetsConversionManuelle.add("BIAT.BP>BIAT.AGC.PCA.EXTRACT.OPPOSITION.CHQ.LOAD");
        listObjetsConversionManuelle.add("BIAT.BP>BIAT.AGC.PCA.EXTRACT.OPPOSITION.CHQ.SELECT");
        listObjetsConversionManuelle.add("BIAT.BP>BIAT.INTERFACE.MDP.CHQ.OPP");
        listObjetsConversionManuelle.add("BIAT.BP>BIAT.INTERFACE.MDP.CHQ.OPP.LOAD");
        listObjetsConversionManuelle.add("BIAT.BP>BIAT.INTERFACE.MDP.CHQ.OPP.SELECT");
        listObjetsConversionManuelle.add("BIAT.BP>BIAT.MDP.A.MAJ.MLEV");
        listObjetsConversionManuelle.add("BIAT.BP>BIAT.MDP.ALIM.CHQ.VOLES");
        listObjetsConversionManuelle.add("BIAT.BP>BIAT.MDP.C.COMP.MAN.CHQ.20.1");
        listObjetsConversionManuelle.add("BIAT.BP>BIAT.MDP.CHEQUE.EOD");
        listObjetsConversionManuelle.add("BIAT.BP>BIAT.MDP.CHEQUE.EOD.DEL.FT");
        listObjetsConversionManuelle.add("BIAT.BP>BIAT.MDP.CHEQUE.EOD.DEL.FT.LOAD");
        listObjetsConversionManuelle.add("BIAT.BP>BIAT.MDP.CHEQUE.EOD.DEL.FT.SELECT");
        listObjetsConversionManuelle.add("BIAT.BP>BIAT.MDP.CHEQUE.EOD.LOAD");
        listObjetsConversionManuelle.add("BIAT.BP>BIAT.MDP.CHEQUE.EOD.SELECT");
        listObjetsConversionManuelle.add("BIAT.BP>BIAT.MDP.CHQ.OPP.ENQ");
        listObjetsConversionManuelle.add("BIAT.BP>BIAT.MDP.DETECTER.OPPOSITION");
        listObjetsConversionManuelle.add("BIAT.BP>BIAT.MDP.EXEC.CHQ.30.JOB");
        listObjetsConversionManuelle.add("BIAT.BP>BIAT.MDP.EXEC.CHQ.30.JOB.LOAD");
        listObjetsConversionManuelle.add("BIAT.BP>BIAT.MDP.EXEC.CHQ.30.JOB.SELECT");
        listObjetsConversionManuelle.add("BIAT.BP>BIAT.MDP.EXEC.CHQ.31.JOB");
        listObjetsConversionManuelle.add("BIAT.BP>BIAT.MDP.EXEC.CHQ.31.JOB.LOAD");
        listObjetsConversionManuelle.add("BIAT.BP>BIAT.MDP.EXEC.CHQ.31.JOB.SELECT");
        listObjetsConversionManuelle.add("BIAT.BP>BIAT.MDP.EXEC.CHQ.32.JOB");
        listObjetsConversionManuelle.add("BIAT.BP>BIAT.MDP.EXEC.CHQ.32.JOB.LOAD");
        listObjetsConversionManuelle.add("BIAT.BP>BIAT.MDP.EXEC.CHQ.32.JOB.SELECT");
        listObjetsConversionManuelle.add("BIAT.BP>BIAT.MDP.EXEC.CHQ.33.JOB");
        listObjetsConversionManuelle.add("BIAT.BP>BIAT.MDP.EXEC.CHQ.33.JOB.LOAD");
        listObjetsConversionManuelle.add("BIAT.BP>BIAT.MDP.EXEC.CHQ.33.JOB.SELECT");
        listObjetsConversionManuelle.add("BIAT.BP>BIAT.MDP.I.CHQ.CERTIF.OPP");
        listObjetsConversionManuelle.add("BIAT.BP>BIAT.MDP.I.PAI.CHQ.CTRL.30");
        listObjetsConversionManuelle.add("BIAT.BP>BIAT.MDP.I.PAI.CHQ.CTRL.30.LOAD");
        listObjetsConversionManuelle.add("BIAT.BP>BIAT.MDP.I.PAI.CHQ.CTRL.30.SELECT");
        listObjetsConversionManuelle.add("BIAT.BP>BIAT.MDP.I.PAI.CHQ.CTRL.31");
        listObjetsConversionManuelle.add("BIAT.BP>BIAT.MDP.I.PAI.CHQ.CTRL.31.LOAD");
        listObjetsConversionManuelle.add("BIAT.BP>BIAT.MDP.I.PAI.CHQ.CTRL.31.SELECT");
        listObjetsConversionManuelle.add("BIAT.BP>BIAT.MDP.I.PAI.CHQ.CTRL.32");
        listObjetsConversionManuelle.add("BIAT.BP>BIAT.MDP.I.PAI.CHQ.CTRL.32.LOAD");
        listObjetsConversionManuelle.add("BIAT.BP>BIAT.MDP.I.PAI.CHQ.CTRL.32.SELECT");
        listObjetsConversionManuelle.add("BIAT.BP>BIAT.MDP.I.PAI.CHQ.CTRL.33");
        listObjetsConversionManuelle.add("BIAT.BP>BIAT.MDP.I.PAI.CHQ.CTRL.33.LOAD");
        listObjetsConversionManuelle.add("BIAT.BP>BIAT.MDP.I.PAI.CHQ.CTRL.33.SELECT");
        listObjetsConversionManuelle.add("BIAT.BP>BIAT.MDP.I.REF.FOUR.29");
        listObjetsConversionManuelle.add("BIAT.BP>BIAT.RTN.ME3165.STOP");
        listObjetsConversionManuelle.add("BIAT.BP>I_BIAT.AGC.PCA.EXTRACT.OPPOSITION.CHQ.COMMON");
        listObjetsConversionManuelle.add("BIAT.BP>I_BIAT.INTERFACE.MDP.CHQ.OPP.COMMON");
        listObjetsConversionManuelle.add("BIAT.BP>I_BIAT.MDP.CHEQUE.EOD.COMMON");
        listObjetsConversionManuelle.add("BIAT.BP>I_BIAT.MDP.EXEC.CHQ.30.JOB.COMMON");
        listObjetsConversionManuelle.add("BIAT.BP>I_BIAT.MDP.EXEC.CHQ.31.JOB.COMMON");
        listObjetsConversionManuelle.add("BIAT.BP>I_BIAT.MDP.EXEC.CHQ.32.JOB.COMMON");
        listObjetsConversionManuelle.add("BIAT.BP>I_BIAT.MDP.EXEC.CHQ.33.JOB.COMMON");
        listObjetsConversionManuelle.add("BIAT.BP>I_BIAT.MDP.I.PAI.CHQ.CTRL.30.COMMON");
        listObjetsConversionManuelle.add("BIAT.BP>I_BIAT.MDP.I.PAI.CHQ.CTRL.31.COMMON");
        listObjetsConversionManuelle.add("BIAT.BP>I_BIAT.MDP.I.PAI.CHQ.CTRL.32.COMMON");
        listObjetsConversionManuelle.add("BIAT.BP>I_BIAT.MDP.I.PAI.CHQ.CTRL.33.COMMON");
        listObjetsConversionManuelle.add("BIAT.BP>T24.GENERIC.EXTRACTOR.UPDATE.DATA");
        listObjetsConversionManuelle.add("ENQUIRY>CHQ.STOPPED");
        listObjetsConversionManuelle.add("ENQUIRY>CHEQUES.STOPPED-PAYMENT.STOP-LIST");
        listObjetsConversionManuelle.add("ENQUIRY>ME3165");
        listObjetsConversionManuelle.add("ENQUIRY>BIAT.ETAT.CHQ.OPP.MAIN.CPTE");
        listObjetsConversionManuelle.add("ENQUIRY>CHEQUES.STOPPED-LIST");
        listObjetsConversionManuelle.add("ENQUIRY>CHEQUES.STOPPED.TEST.METIER");
        listObjetsConversionManuelle.add("ENQUIRY>CHQ.PRESENTED");
        listObjetsConversionManuelle.add("ENQUIRY>CHEQUES.PRESENTED-LIST");
        listObjetsConversionManuelle.add("ENQUIRY>CHEQUES.PRESENTED.TEST.METIER");
        return listObjetsConversionManuelle;
    }

    public static void updateTicketTracOV(Livraison livraison, Integer filsHF, String connectedUser, String messageTrac, String old_t_liv, String... fieldsIntersection) {
        try {
            List<CoupleDTO> listeCustomFieldsParent = new ArrayList<>();
            listeCustomFieldsParent.add(new CoupleDTO("harm_upgrade", "1"));
            String separateur = "";
            if (!old_t_liv.isEmpty()) {
                separateur = " / ";
            }
            listeCustomFieldsParent.add(new CoupleDTO("ticket_appl_prod", old_t_liv + separateur + "CU:[http://172.28.70.74/trac/livraisons_t24/ticket/" + filsHF + " " + filsHF + "]"));
            //maj ticket parent
            DataBaseTracGenericRequests.updateTicketTracGeneral(Configuration.puLivraisons, livraison.getNumeroLivraison(), "OVTOOLS", messageTrac, null, null, null, null, listeCustomFieldsParent);

            //maj ticket fils
            DataBaseTracGenericRequests.updateTicketTracGeneral(Configuration.puLivraisons, filsHF, "OVTOOLS", messageTrac, null, null, null, null, null);
        } catch (Exception exep) {
            exep.printStackTrace();
            Tools.traiterException(Tools.getStackTrace(exep));
        }
    }

    public static void afficherLog(String texte) {
        try {
            DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String date = formatter.format(new Date());
            ManageLogThread.ecrireLogFichier("LOG_HARMONISTATION_HOTFIX_UPGRADE", "\n" + date + "   " + texte);
        } catch (Exception exep) {
            exep.printStackTrace();
            Tools.traiterException(Tools.getStackTrace(exep));
        }
    }

    public static Integer createTicketHarmonisation(Map<String, Object> ticketCustomLivraisonsMap, String summary, String natureLiv) {
        Integer idNewTicket = 0;
        objectsListToBePersistedOnTheDataBaseTrac = new ArrayList<>();
        try {
            Long timeTrac = Long.parseLong(String.valueOf(System.currentTimeMillis()) + "000");
            Ticket ticketLivraisonHotfixMaman = (Ticket) (ticketCustomLivraisonsMap.get("Ticket"));        //Calcul Id nouveau ticket
            DataBaseTools dbTools = new DataBaseTools(Configuration.puLivraisons);
            idNewTicket = getNbrTicketsLivraison(dbTools) + 1;
            //Calcul Trac Date Now
            afficherLog("Date Trac Actuelle: " + new Date() + " (" + timeTrac + ")");
            afficherLog("/n");
            afficherLog("##############################################################");
            afficherLog("Création du nouveau ticket #" + idNewTicket);
            afficherLog("##############################################################");
            //construction d'objet ticket vide
            Ticket ticketLivraisonHarm = new Ticket(idNewTicket);
            //remplir l'objet ticket
            //remplir le champs clé summary
            ticketLivraisonHarm.setSummary(summary);
            afficherLog("Summary du nouveau ticket: " + ticketLivraisonHarm.getSummary());
            //remplir le champs type
            ticketLivraisonHarm.setType("HOT FIXE PROD");
            afficherLog("Type du nouveau ticket: " + ticketLivraisonHarm.getType());
            //hériter le reste des champs a partir de l'anomalie correspondante
            ticketLivraisonHarm.setDescription(ticketLivraisonHotfixMaman.getDescription());
            if (natureLiv.equals("HARMONISATION_C.UPGRADE")) {
                ticketLivraisonHarm.setPriority("LIVRAISON CONFIRMEE");
            } else {
                ticketLivraisonHarm.setPriority("OBJET LIVREE");
            }
            ticketLivraisonHarm.setMilestone(ticketLivraisonHotfixMaman.getMilestone());
            ticketLivraisonHarm.setComponent(ticketLivraisonHotfixMaman.getComponent());
            ticketLivraisonHarm.setOwner(ticketLivraisonHotfixMaman.getCc().replaceAll(",", " "));
            ticketLivraisonHarm.setReporter("OvTools");
            ticketLivraisonHarm.setCc(ticketLivraisonHotfixMaman.getCc());
            ticketLivraisonHarm.setSeverity(ticketLivraisonHotfixMaman.getSeverity());
            ticketLivraisonHarm.setResolution(null);
            ticketLivraisonHarm.setTime(timeTrac);
            ticketLivraisonHarm.setChangetime(timeTrac);
            ticketLivraisonHarm.setStatus("new");
            ticketLivraisonHarm.setKeywords(ticketLivraisonHotfixMaman.getKeywords());
            //inserer l'objet ticket dans la BD
            afficherLog("Insertion de l'objet ticketLivraison #" + idNewTicket + " dans la base de donnée TRAC Livraison au niveau de la table 'ticket'");
            objectsListToBePersistedOnTheDataBaseTrac.add(ticketLivraisonHarm);
            ////////////////////////////// FIN TRAITEMENT OBJET TICKET ///////////////////////
            //créer les ticket_custom correspondantes a la livraison
            afficherLog("Insertion de l'objet ticketCustomLivraison #" + idNewTicket + " dans la base de donnée TRAC Livraison au niveau de la table 'ticket_custom'");
            createAndStoreTicketCustomLivraisonIntoList("circuit_projets", "", ticketLivraisonHarm);
            createAndStoreTicketCustomLivraisonIntoList("circuit_hotfix", "", ticketLivraisonHarm);
            createAndStoreTicketCustomLivraisonIntoList("circuit_release", "", ticketLivraisonHarm);
            createAndStoreTicketCustomLivraisonIntoList("num_revisions", "", ticketLivraisonHarm);
            createAndStoreTicketCustomLivraisonIntoList("env_transverses", "", ticketLivraisonHarm);
            createAndStoreTicketCustomLivraisonIntoList("depot_ov", "", ticketLivraisonHarm);
            createAndStoreTicketCustomLivraisonIntoList("biatdevh", "1", ticketLivraisonHarm);
            createAndStoreTicketCustomLivraisonIntoList("biatdev2", "0", ticketLivraisonHarm);
            createAndStoreTicketCustomLivraisonIntoList("megara", "0", ticketLivraisonHarm);
            createAndStoreTicketCustomLivraisonIntoList("inter_cr", ticketCustomLivraisonsMap.get("inter_cr").toString(), ticketLivraisonHarm);
            createAndStoreTicketCustomLivraisonIntoList("inter_cp", ticketCustomLivraisonsMap.get("inter_cp").toString(), ticketLivraisonHarm);
            createAndStoreTicketCustomLivraisonIntoList("biatcertif", "0", ticketLivraisonHarm);
            createAndStoreTicketCustomLivraisonIntoList("biatdevr", "0", ticketLivraisonHarm);
            createAndStoreTicketCustomLivraisonIntoList("preprod", "0", ticketLivraisonHarm);
            createAndStoreTicketCustomLivraisonIntoList("biattem", "0", ticketLivraisonHarm);
            createAndStoreTicketCustomLivraisonIntoList("biatql1", "0", ticketLivraisonHarm);
            createAndStoreTicketCustomLivraisonIntoList("biatass2", "0", ticketLivraisonHarm);
            createAndStoreTicketCustomLivraisonIntoList("biatql2", "0", ticketLivraisonHarm);
            createAndStoreTicketCustomLivraisonIntoList("biatprod", "1", ticketLivraisonHarm);
            createAndStoreTicketCustomLivraisonIntoList("biatmigd", "0", ticketLivraisonHarm);
            createAndStoreTicketCustomLivraisonIntoList("biatref", "1", ticketLivraisonHarm);
            createAndStoreTicketCustomLivraisonIntoList("biatif1", "0", ticketLivraisonHarm);
            createAndStoreTicketCustomLivraisonIntoList("biatrap", "0", ticketLivraisonHarm);
            createAndStoreTicketCustomLivraisonIntoList("biattf1", "0", ticketLivraisonHarm);
            createAndStoreTicketCustomLivraisonIntoList("regression", "0", ticketLivraisonHarm);
            createAndStoreTicketCustomLivraisonIntoList("ticket_origine", ticketCustomLivraisonsMap.get("ticket_origine").toString(), ticketLivraisonHarm);
            createAndStoreTicketCustomLivraisonIntoList("projet", ticketCustomLivraisonsMap.get("projet").toString(), ticketLivraisonHarm);
            createAndStoreTicketCustomLivraisonIntoList("developpement", "BIAT", ticketLivraisonHarm);
            createAndStoreTicketCustomLivraisonIntoList("t_liv_certif", "", ticketLivraisonHarm);
            createAndStoreTicketCustomLivraisonIntoList("nature_trait", "CURATIF", ticketLivraisonHarm);
            createAndStoreTicketCustomLivraisonIntoList("nature_liv", natureLiv, ticketLivraisonHarm);
            createAndStoreTicketCustomLivraisonIntoList("ticket_appl_prod", "[http://172.28.70.74/trac/livraisons_t24/ticket/" + ticketLivraisonHotfixMaman.getId().toString() + " " + ticketLivraisonHotfixMaman.getId().toString() + "]", ticketLivraisonHarm);
            createAndStoreTicketCustomLivraisonIntoList("contenu_des_livrables", ticketCustomLivraisonsMap.get("contenu_des_livrables").toString(), ticketLivraisonHarm);
            dbTools.updateDataBaseTrac(dbTools);
            dbTools.closeRessources();
        } catch (Exception ex) {
            ex.printStackTrace();
            Tools.traiterException(Tools.getStackTrace(ex));
        }
        return idNewTicket;
    }

    public static Integer getNbrTicketsLivraison(DataBaseTools dbTools) {
        Integer nbrTickets = null;
        try {
            Query q = dbTools.em.createNamedQuery("Ticket.countAll");
            q.setHint(QueryHints.REFRESH, HintValues.TRUE);
            nbrTickets = (Integer) q.getSingleResult();
        } catch (Exception ex) {
            ex.printStackTrace();
            Tools.traiterException(Tools.getStackTrace(ex));
        }
        return nbrTickets;
    }

    public static void createAndStoreTicketCustomLivraisonIntoList(String name, String value, Ticket ticketLivraison) {
        try {
            //construction d'objet ticketCustom vide
            TicketCustom ticketCustom = new TicketCustom();
            //remplir l'objet ticketCustom
            ticketCustom.setTicketPointer(ticketLivraison);
            ticketCustom.setTicket(ticketLivraison.getId());
            ticketCustom.setName(name);
            ticketCustom.setValue(value);
            //inserrer l'objet ticketCustom dans la liste
            objectsListToBePersistedOnTheDataBaseTrac.add(ticketCustom);
        } catch (Exception ex) {
            ex.printStackTrace();
            Tools.traiterException(Tools.getStackTrace(ex));
        }
    }

    private void genererMessageHarmonisationUPGRADE() {
        try {
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
            sbResult.append("Ci-dessous le rapport de l'étude d'intersection des HOTFIX appliqués sur RPOD avec le circuit UPGRADE.");
            sbResult.append("\n<br>");
            sbResult.append("\n</p><p></p>");
            sbResult.append("\n");
            sbResult.append("\n<table cellspacing='0' cellpadding='5' >");
            sbResult.append("\n    <thead>");
            sbResult.append("\n        <tr style='background-color: #FDD999; color: #35427E'>");
            sbResult.append("\n            <th class='thTable'>Anomalie</th>");
            sbResult.append("\n            <th class='thTable'>Livraison</th>");
            sbResult.append("\n            <th class='thTable'>Ticket Upgrade</th>");
            sbResult.append("\n            <th class='thTable'>Liste des objets déployés</th>");
            sbResult.append("\n            <th class='thTable'>Intersection C.UPGRADE</th>");
            sbResult.append("\n            <th class='thTable'>Impact UPGRADE R09-->R17</th>");
            sbResult.append("\n            <th style='padding: 0px;border: none'></th>");
            sbResult.append("\n        </tr>");
            sbResult.append("\n    </thead>");
            sbResult.append("\n    <tbody>");
            for (AutomatisationHarmonisationUpgradeDTO dto : listDetailsLivraisonsEmailUpgrade) {
                String tickAnomalie = dto.getAnomalie();
                String ticketLivraison = dto.getNumLivraisonHotfix();
                String ticketLivraisonFils = dto.getNumTicketLivraisonFils();
                String listObjDeployes = dto.getObjetsDeployes();
                String resultatEtudeIntersectionCU = dto.getResultatEtudeIntersectionCU();
                String resultatEtudeConversionCU = dto.getResultatEtudeConversionCU();
                sbResult.append("\n        <tr style='background-color: #faebd7; color: #666666'>");
                sbResult.append("\n            <td class='tdTable'><a href='http://172.28.70.74/trac/anomalies_t24/ticket/").append(tickAnomalie).append("' >#").append(tickAnomalie).append("</a></td>");
                sbResult.append("\n            <td class='tdTable'><a href='http://172.28.70.74/trac/livraisons_t24/ticket/").append(ticketLivraison).append("' >#").append(ticketLivraison).append("</a></td>");
                sbResult.append("\n            <td class='tdTable'><a href='http://172.28.70.74/trac/livraisons_t24/ticket/").append(ticketLivraisonFils).append("' >#").append(ticketLivraisonFils).append("</a></td>");
                sbResult.append("\n            <td class='tdTable'>").append(listObjDeployes).append("</td>");
                sbResult.append("\n            <td class='tdTable'>").append(resultatEtudeIntersectionCU).append("</td>");
                sbResult.append("\n            <td class='tdTable'>").append(resultatEtudeConversionCU).append("</td>");
                sbResult.append("\n            <td style='padding: 0px;border: none'></td>");
                sbResult.append("\n        </tr>");
            }
            sbResult.append("\n    </tbody>");
            sbResult.append("\n</table>");
            sbResult.append("\n<br>");

            //fin
            /*
             initQueriesUpgrade();
             //Hotfix CU qui nécessitent une adaptation sur DEVU
             if (!ticketMapHfAdaptationCU.isEmpty()) {
             sbResult.append("Ci-dessous la liste des hotfix qui doivent être adaptés sur C.U (DEVU):");
             sbResult.append("\n</p><p></p>");
             sbResult.append("\n");
             sbResult.append("\n<table cellspacing='0' cellpadding='5' >");
             sbResult.append("\n    <thead>");
             sbResult.append("\n        <tr style='background-color: #FDD999; color: #35427E'>");
             sbResult.append("\n            <th class='thTable'>Anomalie</th>");
             sbResult.append("\n            <th class='thTable'>Livraison</th>");
             sbResult.append("\n            <th class='thTable'>Ticket UPGRADE</th>");
             sbResult.append("\n            <th class='thTable'>Responsable</th>");
             sbResult.append("\n            <th class='thTable'>Environnement</th>");
             sbResult.append("\n            <th style='padding: 0px;border: none'></th>");
             sbResult.append("\n        </tr>");
             sbResult.append("\n    </thead>");
             sbResult.append("\n    <tbody>");
             for (Map.Entry<Integer, Map<String, Object>> entry : ticketMapHfAdaptationCU.entrySet()) {
             Map<String, Object> tickMap = entry.getValue();
             String tickAnomalie = (String) tickMap.get("ticket_origine");
             String ticketLivraison = (String) tickMap.get("ticket_appl_prod");
             Integer ticketLivraisonFils = entry.getKey();
             String envName = "DEVU";
             String developpeur = (String) tickMap.get("developpeur");
             sbResult.append("\n        <tr style='background-color: #faebd7; color: #666666'>");
             sbResult.append("\n            <td class='tdTable'><a href='http://172.28.70.74/trac/anomalies_t24/ticket/").append(tickAnomalie).append("' >#").append(tickAnomalie).append("</a></td>");
             sbResult.append("\n            <td class='tdTable'><a href='http://172.28.70.74/trac/livraisons_t24/ticket/").append(ticketLivraison).append("' >#").append(ticketLivraison).append("</a></td>");
             sbResult.append("\n            <td class='tdTable'><a href='http://172.28.70.74/trac/livraisons_t24/ticket/").append(ticketLivraisonFils).append("' >#").append(ticketLivraisonFils).append("</a></td>");
             sbResult.append("\n            <td class='tdTable'>").append(developpeur).append("</td>");
             sbResult.append("\n            <td class='tdTable'>").append(envName).append("</td>");
             sbResult.append("\n            <td style='padding: 0px;border: none'></td>");
             sbResult.append("\n        </tr>");
             }
             sbResult.append("\n    </tbody>");
             sbResult.append("\n</table>");
             }*/
            //fin
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
            String emailContent = sbResult.toString().trim();
            //tools.Tools.sendEMail("HARMONISATION DES HOTFIX PAR RAPPORT AU CIRCUIT UPGRADE", "OV.Management.Solutions@biat.com.tn", new String[]{"CH_DEV@biat.com.tn", "MONDHERM@biat.com.tn", "mohsen.ouertani@biat.com.tn", "sami.ferchichi@biat.com.tn", "hatem.chaabane@biat.com.tn", "meha.meftah@biat.com.tn", "t24_csh@biat.com.tn", "moncef.mallek@biat.com.tn"}, new String[]{"C24OV@biat.com.tn", "jamel.bahri@biat.com.tn"}, new String[]{}, emailContent, false);
            if (!listDetailsLivraisonsEmailUpgrade.isEmpty()) {
                tools.Tools.sendEMail("HARMONISATION DES HOTFIX SUR LE CIRCUIT UPGRADE", "OV.Management.Solutions@biat.com.tn", new String[]{"safwen.bensaid@biat.com.tn"}, new String[]{"safwen.bensaid@biat.com.tn"}, new String[]{}, emailContent, false);
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            Tools.traiterException(Tools.getStackTrace(exep));
        }
    }

    private void initQueriesUpgrade() {
        StringBuilder querySbCU = new StringBuilder("SELECT id as 'ticket', ");
        querySbCU.append("t.changetime AS Date_arrivee, ");
        querySbCU.append("o.value AS deployee_sur_prod, ");
        querySbCU.append("o1.value AS deployee_sur_certif, ");
        querySbCU.append("o2.value AS deployee_sur_qualif, ");
        querySbCU.append("o5.value AS deployee_sur_ref, ");
        querySbCU.append("o7.value AS deployee_sur_devr, ");
        querySbCU.append("o8.value AS nature_livraison ");
        querySbCU.append("FROM TICKET t ");
        querySbCU.append("LEFT OUTER JOIN ticket_custom o ON ");
        querySbCU.append("(t.id=o.ticket AND o.name='biatprod') ");
        querySbCU.append("LEFT OUTER JOIN ticket_custom o1 ON ");
        querySbCU.append("(t.id=o1.ticket AND o1.name='biatcertif') ");
        querySbCU.append("LEFT OUTER JOIN ticket_custom o2 ON ");
        querySbCU.append("(t.id=o2.ticket AND o2.name='biatql1') ");
        querySbCU.append("LEFT OUTER JOIN ticket_custom o5 ON ");
        querySbCU.append("(t.id=o5.ticket AND o5.name='biatref') ");
        querySbCU.append("LEFT OUTER JOIN ticket_custom o7 ON ");
        querySbCU.append("(t.id=o7.ticket AND o7.name='biatdevr') ");
        querySbCU.append("LEFT OUTER JOIN ticket_custom o8 ON ");
        querySbCU.append("      (t.id=o8.ticket AND o8.name='nature_liv') ");
        querySbCU.append("LEFT OUTER JOIN ticket_custom o9 ON ");
        querySbCU.append("       (t.id=o9.ticket AND o9.name='biattem') ");
        querySbCU.append("where o8.value IN ('HARMONISATION_C.UPGRADE') ");
        querySbCU.append("and coalesce(o.value,0) = '1' ");
        querySbCU.append("and coalesce(o1.value,0)  <> '1' ");
        querySbCU.append("and coalesce(o2.value,0)  <> '1' ");
        querySbCU.append("and coalesce(o5.value,0) = '1' ");
        querySbCU.append("and coalesce(o7.value,0) <> '1' ");
        querySbCU.append("and coalesce(o9.value,0)  <> '1' ");
        querySbCU.append("       and priority = 'LIVRAISON CONFIRMEE' ");
        querySbCU.append("order by Date_arrivee");
        ticketMapHfAdaptationCU = analyseTicketFilsUpgrade(querySbCU);
    }

    public static Map<Integer, Map<String, Object>> analyseTicketFilsUpgrade(StringBuilder querySbCP) {
        Map<Integer, Map<String, Object>> ticketMap = null;
        try {
            DataBaseTools dbTools = new DataBaseTools(Configuration.puLivraisons);
            List<Object[]> resultList = new DataBaseTracGenericRequests<Object[]>().executeQueryRequest(dbTools, querySbCP, "NVQ_SELECT");
            List<Integer> ticketsIdList = new ArrayList<>();
            for (Object[] tab : resultList) {
                Long l = (Long) tab[0];
                ticketsIdList.add(l.intValue());
            }
            dbTools.closeRessources();
            String[] cles = new String[]{"ticket_origine", "ticket_appl_prod"};
            ticketMap = AppelRequetes.getTicketCustomByTicketIdAndNames(ticketsIdList, Configuration.puLivraisons, Configuration.tracLivraisons, cles);
            for (Map.Entry<Integer, Map<String, Object>> entry : ticketMap.entrySet()) {
                Map<String, Object> tickMap = entry.getValue();
                int idTickParent = Integer.parseInt(tickMap.get("ticket_appl_prod").toString());
                dbTools = new DataBaseTools(Configuration.puLivraisons);
                Ticket ticket = getTicketById(dbTools, idTickParent);
                tickMap.put("developpeur", ticket.getCc());
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            Tools.traiterException(Tools.getStackTrace(exep));
        }
        return ticketMap;
    }
}
