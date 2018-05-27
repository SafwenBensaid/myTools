/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package threads;

import dto.QuadripleDTO;
import dataBaseTracRequests.DataBaseTracGenericRequests;
import dataBaseTracRequests.AppelRequetes;
import static dataBaseTracRequests.DataBaseTracGenericRequests.getTicketById;
import dto.AutomatisationHarmonisationCddDTO;
import dto.CoupleDTO;
import dto.EnvironnementDTO;
import dto.HarmonisationDTO;
import dto.TicketFilsDTO;
import entitiesMysql.Livraison;
import entitiesTrac.Ticket;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Query;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;
import strutsActions.EtudeIntersectionInputObjetsAction;
import t24Scripts.PM;
import t24Scripts.T24Scripts;
import tools.Configuration;
import dataBaseTracRequests.DataBaseTracRequests;
import dto.livraison.*;
import java.text.DateFormat;
import static threads.AutomatisationDeploiementIeThread.alertParEmail;
import tools.DataBaseTools;
import tools.DeploiementParalleleTools;
import tools.EtudeIntersectionTools;
import tools.FtpTools;
import tools.GetHotfixesPipesTool;
import tools.ManipulationObjectsTool;
import tools.Tools;
import static tools.Tools.sendEMail;

/**
 *
 * @author 04486
 */
public class GestionHotfixProdThread extends Thread {

    private String startSendingHfProdDateString;
    private String endSendingHfProdDateString;
    private String startHarmonisationHfProdDateString;
    private String harmonisationHfProd;
    private String sendingHfProd;
    private String endSendingEmailProdDateString;
    private static Set<Livraison> livraisonsBloquees = null;
    //private List<HarmonisationDTO> listLivraisonsToBeDeployedByCDD = new ArrayList<HarmonisationDTO>();
    private List<AutomatisationHarmonisationCddDTO> listDetailsLivraisonsEmailCDD = new ArrayList<>();
    static public boolean alertEnvoyeeBloqueCob = false;
    private Map<Integer, Map<String, Object>> ticketMapHfAdaptationCR = null;
    private Map<Integer, Map<String, Object>> ticketMapHfAdaptationCP = null;
    public boolean threadIsAlive = true;

    @Override
    public void run() {
        String connectedUser = "OVTOOLS";
        List<Integer> listeIdTicketsAnomalies = new ArrayList<>();
        Map<Integer, Map<String, Object>> ticketsToBeSendedByMailMap = null;
        String emailContent = "";
        String hotfixNumber = "";
        DataBaseTools dbTools = null;
        //String scriptEnvoiEmail = "notifierHotfixEnvoyesProd ";
        Query q = null;
        byte[] fichierEnByte = null;
        ManipulationObjectsTool manip = new ManipulationObjectsTool();
        Map<String, List<Livraison>> mapLivraisons = null;
        List<Livraison> listeOV_HF_QUALIFIEE = null;
        List<Livraison> listeEXPLOITATION_HF_NON_DEPLOYEE = null;
        List<Livraison> listeEXPLOITATION_HF_DEPLOYEE = null;
        List<Livraison> listeLivraisonsAEnvoyerParMail = null;
        T24Scripts t24Scripts = null;

        SimpleDateFormat parseFormatyyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
        Livraison livAux = null;
        while (threadIsAlive) {
            try {
                System.out.println("       ^^^^^^^^^^   BOUCLE WHILE AUTOMATISATION HOTFIX   ^^^^^^^^^^");
                mapLivraisons = new GetHotfixesPipesTool().getAllHotfixes();
                listeOV_HF_QUALIFIEE = mapLivraisons.get("OV_HF_QUALIFIEE");
                listeEXPLOITATION_HF_NON_DEPLOYEE = mapLivraisons.get("EXPLOITATION_HF_NON_DEPLOYEE");
                listeEXPLOITATION_HF_DEPLOYEE = mapLivraisons.get("EXPLOITATION_HF_DEPLOYEE");
                listeLivraisonsAEnvoyerParMail = new ArrayList<>();
                listeLivraisonsAEnvoyerParMail.addAll(listeEXPLOITATION_HF_NON_DEPLOYEE);
                listeLivraisonsAEnvoyerParMail.addAll(listeEXPLOITATION_HF_DEPLOYEE);

                initialisationVariables();
                while (sendingHfProd == null) {
                    initialisationVariables();
                    Thread.sleep(1000L);
                }
                if (sendingHfProd.trim().equals("ON")) {
                    //System.out.println(scriptEnvoiEmail);
                    if (ticketsToBeSendedByMailMap == null) {
                        ticketsToBeSendedByMailMap = new HashMap<Integer, Map<String, Object>>();
                    }
                    System.out.println("---------- TEST SEND HF TO PROD ----------");
                    if (Tools.isTimeToDo(startSendingHfProdDateString, endSendingHfProdDateString, Configuration.HEURE_FORMAT, true)) {
                        afficherLog("SEND HF TO PROD");
                        //Selection des hotfixes validees et non envoyées à la PROD
                        Iterator<Livraison> it = listeOV_HF_QUALIFIEE.iterator();
                        while (it.hasNext()) {
                            livAux = it.next();
                            if (livAux.getValide() == false || livAux.getDateEnvoiProd() == null) {
                                it.remove();
                            }
                        }

                        //comme ça la liste ne contient que les HF qualifiés et prêts à être envoyés à Riadh
                        System.out.println("On va envoyer " + listeOV_HF_QUALIFIEE.size() + " à Riadh");
                        afficherLog("On va envoyer " + listeOV_HF_QUALIFIEE.size() + " à Riadh");
                        if (listeOV_HF_QUALIFIEE.size() > 0) {
                            for (Livraison liv : listeOV_HF_QUALIFIEE) {
                                DataBaseTracRequests.sendOneHotfixToProd(liv);
                            }
                            listeOV_HF_QUALIFIEE.clear();
                        }
                    }

                    System.out.println("---------- TEST SEND EMAIL ----------");

                    if (Tools.isTimeToDo(endSendingHfProdDateString, endSendingEmailProdDateString, Configuration.HEURE_FORMAT, true) && listeLivraisonsAEnvoyerParMail.size() > 0) {
                        afficherLog("SEND EMAIL");
                        //envoi du message
                        Iterator<Livraison> it = listeLivraisonsAEnvoyerParMail.iterator();


                        while (it.hasNext()) {
                            livAux = it.next();
                            System.out.println("#" + livAux.getNumeroLivraison());
                            //System.out.println("liv: #"+livAux.getNumeroLivraison()+"    "+livAux.getDateEnvoiProd().toString());   

                            //if (livAux.getDateEnvoiProd() != null) {
                            //    it.remove();
                            //} else {
                            if (livAux.getDateEnvoiProd() == null) {
                                // si le ticket a été envoyée à Riadh manuellement
                                dbTools = new DataBaseTools(Configuration.puOvTools);
                                livAux.setDateEnvoiProd(livAux.getDateDeploiement());
                                dbTools.update(livAux);
                                dbTools.closeRessources();
                            }
                            if (!Tools.isTimeToDo(parseFormatyyyyMMdd.format(livAux.getDateEnvoiProd()), parseFormatyyyyMMdd.format(new Date()), "yyyy-MM-dd", false)) {
                                it.remove();
                            }
                            //}
                        }
                        System.out.println("L'email contient: " + listeLivraisonsAEnvoyerParMail.size() + " tickets");
                        //comme ça la liste ne contient que les HF prêts à être envoyés par mail
                        if (listeLivraisonsAEnvoyerParMail.size() > 0) {
                            hotfixNumber = Configuration.mapChiffres.get(String.valueOf(listeLivraisonsAEnvoyerParMail.size()));
                            for (Livraison liv : listeLivraisonsAEnvoyerParMail) {
                                listeIdTicketsAnomalies.add(liv.getNumeroAnomalie());
                            }
                            String[] cles = new String[]{"t_liv"};
                            ticketsToBeSendedByMailMap = AppelRequetes.getTicketCustomByTicketIdAndNames(listeIdTicketsAnomalies, Configuration.puAnomalies, Configuration.tracAnomalies, cles);
                            emailContent = generateMessageHtml(ticketsToBeSendedByMailMap, connectedUser).trim();
                            sendEMail(hotfixNumber + " HOTFIX A passer ce soir sur PROD", "OV.Management.Solutions@biat.com.tn", new String[]{"sami.lassoued@biat.com.tn", "abdelhakim.sidhom@biat.com.tn", "sinda.rahmouni@biat.com.tn", "mongi.guesmi@biat.com.tn", "riadh.bendakhlia@biat.com.tn"}, new String[]{"C24OV@biat.com.tn", "jamel.bahri@biat.com.tn", "moncef.mallek@biat.com.tn", "TPSI@biat.com.tn", "CH_Metier@biat.com.tn", "Pilier.technique@biat.com.tn"}, new String[]{}, emailContent, false);
                            //sendEMail(hotfixNumber + "HOTFIX A passer ce soir sur PROD","OV.Management.Solutions@biat.com.tn", new String[]{"safwen.bensaid@biat.com.tn"}, new String[]{}, new String[]{}, emailContent,false);
                            
                            //tools.Tools.sendEMail("corpsEmailnotificationHotfixEnvoyesProd", scriptEnvoiEmail + "\'" + hotfixNumber + "\'", emailContent);
                            ticketsToBeSendedByMailMap.clear();
                            listeIdTicketsAnomalies.clear();
                            try {
                                // après l'envoi d'email, sleep d'une minute pour que le temps d'envoi des HF soit dépassé
                                afficherLog("1- sleep(1000*60*5)");
                                sleep(1000 * 60 * 5);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(GestionHotfixProdThread.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } /*else {
                         tools.Tools.traiterException("ATTENTION: Le message des HOTFIX est vide!!!");
                         }*/
                    }

                    System.out.println("---------- TEST REINITIALISATION ----------");
                    if (Tools.isTimeToDo("01:00", "01:02", Configuration.HEURE_FORMAT, true)) {
                        afficherLog("REINITIALISATION");
                        //Dévalidation de tous les HOFIX
                        dbTools = new DataBaseTools(Configuration.puOvTools);
                        q = dbTools.em.createNamedQuery("Livraison.findAll");
                        q.setHint(QueryHints.REFRESH, HintValues.TRUE);
                        List<Livraison> allLivraisons = (List<Livraison>) q.getResultList();
                        //vérifier les anomalies qualifiees
                        for (Livraison liv : allLivraisons) {
                            liv.setValide(false);
                        }
                        dbTools.updateObjectList(allLivraisons);
                        dbTools.closeRessources();
                    }
                }
                if (harmonisationHfProd.trim().equals("ON")) {
                    System.out.println("---------- TEST HARMONISATION HF PROD ----------");

                    if (Tools.isTimeToDo(startHarmonisationHfProdDateString, null, Configuration.HEURE_FORMAT, true)) {
                        afficherLog("HARMONISATION HF PROD");
                        EtudeIntersectionInputObjetsAction etudeIntersection = new EtudeIntersectionInputObjetsAction();
                        Tools tools = new Tools();
                        EtudeIntersectionTools etudeIntersectionTools = null;
                        QuadripleDTO quadripleDTO = null;
                        System.out.println(listeEXPLOITATION_HF_DEPLOYEE.size() + " tickets à harmoniser:");
                        for (Livraison liv : listeEXPLOITATION_HF_DEPLOYEE) {
                            afficherLog("HARMONISATION ticket #" + liv.getNumeroLivraison());
                            System.out.println("ticket #" + liv.getNumeroLivraison());
                            //if (liv.getNomPack() != null && liv.getListeObjets() != null && liv.getListeObjets().length() > 0 && !liv.getContenuLivrables().equals("CREATION COMPANY") && liv.isHarm1probleme() == false && !liv.getContenuLivrables().equals("ID AVEC DATE") && !liv.getContenuLivrables().equals("MULTIPACKS")) {
                            if (liv.getLivrables() != null && liv.isHarm1probleme() == false) {
                                StringBuilder resultatGlobal = new StringBuilder();
                                StringBuilder resultatVersionning = new StringBuilder();
                                //////////// etude d'intersection //////////// 
                                etudeIntersectionTools = new EtudeIntersectionTools(String.valueOf(liv.getNumeroLivraison()));
                                String resultatEtudeIntersectionCR = "";
                                String resultatEtudeIntersectionCP = "";

                                //DEP sur INTG
                                String[] resTab = null;
                                List<String> envDepOkList = null;
                                ManipulationObjectsTool manObjTools = new ManipulationObjectsTool();
                                Map<String, String[]> resultMap = manObjTools.traiterLivrable(liv, "OV", connectedUser, new String[]{"INTG"}, "ASS", true);

                                //VERSIONNING+Etude d'intersection
                                for (Object livDto : liv.getLivrables()) {
                                    if (livDto instanceof T24) {
                                        T24 livT24 = (T24) livDto;
                                        String objectsList = null;
                                        try {
                                            objectsList = etudeIntersectionTools.traiterEtudeIntersectionInput(null, livT24.getPackName(), "ASS", connectedUser, "PACK");
                                        } catch (Exception exep) {
                                            exep.printStackTrace();
                                            Tools.traiterException(Tools.getStackTrace(exep));
                                        }
                                        if (!objectsList.contains("Dossier inéxistant")) {
                                            quadripleDTO = etudeIntersection.etudeIntersection(etudeIntersectionTools, tools, connectedUser, objectsList);
                                            resultatEtudeIntersectionCR += quadripleDTO.getValeur1().trim();
                                            resultatEtudeIntersectionCP += quadripleDTO.getValeur2().trim();
                                            //////////// versionning ////////////                                        
                                            String scriptVersionning1 = null;
                                            String scriptVersionning2 = null;
                                            if (quadripleDTO.getValeur1().length() == 0 && quadripleDTO.getValeur2().length() == 0) {
                                                //aucune intersection
                                                scriptVersionning1 = "SVNVERS_TOTAL";
                                            } else {
                                                if (quadripleDTO.getValeur1().length() > 0 || quadripleDTO.getValeur2().length() > 0) {
                                                    //intersection CR ou intersection CP
                                                    scriptVersionning1 = "SVNVERS_HOTFIX";
                                                }
                                                if (quadripleDTO.getValeur1().length() > 0 && quadripleDTO.getValeur2().length() == 0) {
                                                    //intersection CR
                                                    scriptVersionning2 = "SVNVERS_HF_PROJET";
                                                }
                                                if (quadripleDTO.getValeur1().length() == 0 && quadripleDTO.getValeur2().length() > 0) {
                                                    //intersection CP
                                                    scriptVersionning2 = "SVNVERS_HF_RELEASE";
                                                }
                                            }
                                            try {
                                                afficherLog("2- sleep(1000*10)");
                                                Thread.sleep(1000 * 10);
                                            } catch (InterruptedException ex) {
                                                Logger.getLogger(GestionHotfixProdThread.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                            resultatVersionning.append("\n== Résultat du versionning du pack ").append(livT24.getPackName()).append(" avec le script ").append(scriptVersionning1).append(":==").append(" \n[[BR]]\n");
                                            resultatVersionning.append(T24Scripts.versionnerPackHotFix(scriptVersionning1, livT24, connectedUser, liv.getNumeroLivraison()));

                                            try {
                                                afficherLog("3- sleep(1000*60*1)");
                                                Thread.sleep(1000 * 60 * 1);
                                            } catch (InterruptedException ex) {
                                                Logger.getLogger(GestionHotfixProdThread.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                            if (scriptVersionning2 != null) {
                                                resultatVersionning.append("\n----\n");
                                                resultatVersionning.append("== Résultat du versionning du pack ").append(livT24.getPackName()).append(" avec le script ").append(scriptVersionning2).append(":==").append(" \n[[BR]]\n");
                                                resultatVersionning.append(T24Scripts.versionnerPackHotFix(scriptVersionning2, livT24, connectedUser, liv.getNumeroLivraison()));
                                            }
                                        }
                                    } else if (livDto instanceof StreamServeTransactionnel || livDto instanceof StreamServeBatch) {
                                        //si field-contenu_des_livrables = STREAMSERV TRANSACTIONNEL => intersection CR et CP
                                        resultatEtudeIntersectionCR += "A adapter la version STREAMSERV TRANSACTIONNEL sur le C.R";
                                        resultatEtudeIntersectionCP += "A adapter la version STREAMSERV TRANSACTIONNEL sur le C.P";
                                    }
                                }
                                liv.setResultatetudeintersectionCR(resultatEtudeIntersectionCR);
                                liv.setResultatetudeintersectionCP(resultatEtudeIntersectionCP);
                                afficherLog("Resultat etude intersection CR ticket #" + liv.getNumeroLivraison());
                                afficherLog(resultatEtudeIntersectionCR);
                                afficherLog("Resultat etude intersection CP ticket #" + liv.getNumeroLivraison());
                                afficherLog(resultatEtudeIntersectionCP);
                                resultatGlobal.append(resultatVersionning);
                                dbTools = new DataBaseTools(Configuration.puOvTools);
                                dbTools.update(liv);
                                dbTools.closeRessources();
                                //cases à cocher intersection
                                resultatEtudeIntersectionCR = resultatEtudeIntersectionCR.trim();
                                resultatEtudeIntersectionCP = resultatEtudeIntersectionCP.trim();
                                String[] intersectionArray = null;
                                Set<String> intersectionSet = new TreeSet<>();
                                if (resultatEtudeIntersectionCR.length() > 0) {
                                    //intersection CR
                                    intersectionSet.add("inter_cr");
                                } else {
                                    resultatEtudeIntersectionCR = "<br>Aucune intersection<br>";
                                }
                                if (resultatEtudeIntersectionCP.length() > 0) {
                                    //intersection CP
                                    intersectionSet.add("inter_cp");
                                } else {
                                    resultatEtudeIntersectionCP = "<br>Aucune intersection<br>";
                                }
                                intersectionArray = intersectionSet.toArray(new String[intersectionSet.size()]);
                                //fin
                                t24Scripts = new T24Scripts();
                                resultatGlobal.append("\n== Résultat de l'étude d'intersection avec le cicruit release C.R ").append(":==").append(" \n{{{\n#!html\n");
                                resultatGlobal.append(resultatEtudeIntersectionCR).append("\n}}}\n\n");

                                resultatGlobal.append("\n== Résultat de l'étude d'intersection avec le cicruit projet C.P ").append(":==").append(" \n{{{\n#!html\n");
                                resultatGlobal.append(resultatEtudeIntersectionCP).append("\n}}}\n\n");

                                // Résultat DEP sur INTG

                                Integer numTicketLivraison = liv.getNumeroLivraison();
                                DataBaseTools tracLivDbTools = new DataBaseTools(Configuration.puLivraisons);
                                Ticket ticket = getTicketById(tracLivDbTools, numTicketLivraison);
                                tracLivDbTools.closeRessources();
                                if (resultMap.containsKey("PROBLEME") && resultMap.get("PROBLEME")[0].length() > 0 && resultMap.get("PROBLEME")[1].length() > 0) {
                                    //problème d'authentification, prob d'existance de pack ou ...
                                    resTab = resultMap.get("PROBLEME");
                                    resultatGlobal.append(resTab[0]).append(resTab[1]);
                                    alertParEmail(ticket, "Probleme Harmonisation OV #" + numTicketLivraison, resTab[0] + " " + resTab[1], "C24OV@biat.com.tn");
                                } else {
                                    String messageTrac = resultMap.get("RESULTAT_HTML_COMPLET")[0];
                                    String[] envDepOkArray = resultMap.get("ENV_DEP_OK");
                                    String problemesDeploiementsString = resultMap.get("RESULTAT")[1];
                                    envDepOkList = Arrays.asList(envDepOkArray);
                                    resultatGlobal.append(messageTrac);
                                    boolean actionManuelleNonTraitee = false;
                                    if (problemesDeploiementsString != null && problemesDeploiementsString.trim().length() > 0) {
                                        alertParEmail(ticket, "Problème de déploiement " + liv.getNumeroLivraison(), "Bonjour,<br>L'harmonisation de la livraison <a href='http://172.28.70.74/trac/livraisons_t24/ticket/" + numTicketLivraison + "'>#" + numTicketLivraison + "</a> présente un problème de déploiement.", "C24OV@biat.com.tn");
                                        afficherLog("PROBLEME DE DEPLOIEMENT HARMONISATION OV");
                                    } else {
                                        if (messageTrac.contains("<span class='errorBold'> (A traiter manuellement)") || messageTrac.contains("Ce ticket sera traite manuellement") || messageTrac.contains("interrompu") || messageTrac.contains("POUR VERIFICATION OV")) {
                                            //si il existe une action manuelle non traitée                                            
                                            actionManuelleNonTraitee = true;
                                        }
                                    }
                                    // si le contenu des liv !=objT24 ou prob de dep sur INTG, ne pas cocher HF_HARM_OK pour bloquer la défalquation
                                    String defalquerOk = "1";
                                    if (!envDepOkList.contains("INTG") || liv.getContenuLivrables().equals("CREATION COMPANY") || actionManuelleNonTraitee == true) {
                                        defalquerOk = "0";
                                        dbTools = new DataBaseTools(Configuration.puOvTools);
                                        liv.setHarm1probleme(true);
                                        dbTools.update(liv);
                                        dbTools.closeRessources();
                                        afficherLog("PROBLEME HARMONISTATION ticket #" + liv.getNumeroLivraison());
                                    }
                                    //////////// updateTicketTrac ////////////
                                    updateTicketTracOV(liv, "OVTOOLS", resultatGlobal.toString(), defalquerOk, intersectionArray);
                                    if (actionManuelleNonTraitee == true) {
                                        alertParEmail(ticket, "PROBLEME DE DEPLOIEMENT HARMONISATION OV" + numTicketLivraison, "Bonjour,<br>La livraison <a href='http://172.16.11.196/trac/livraisons_t24/ticket/" + numTicketLivraison + "'>#" + numTicketLivraison + "</a> contient une action manuelle, prière de la passer manuellement et de cocher l'environnement de développement", "C24OV@biat.com.tn");
                                    }
                                    try {
                                        afficherLog("4- sleep(1000*60*1)");
                                        Thread.sleep(1000 * 60 * 1);
                                    } catch (InterruptedException ex) {
                                        Logger.getLogger(ManageLogThread.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            } else {
                                Tools.traiterException("Attention, le Hotfix num #" + liv.getNumeroLivraison() + " n'a pas été harmonisée car il a été livré par le développeur manuellement, prière de le traiter manuellement.");
                            }

                        }
                        //harmonistaion des tickets défalqués                    
                        harmonistaionDesTicketsDefalques(connectedUser);
                        //harmonisation tickets par cdd
                        harmonisationHotfixparCDD(connectedUser);
                    }

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

    public void harmonisationHotfixparCDD(String connectedUser) {
        try {
            afficherLog("***** HARMONISATION Hotfix CDD *****");
            List<TicketFilsDTO> listTicketsNotT24 = new ArrayList<>();
            List<HarmonisationDTO> listLivraisonsToBeDeployedByCDD = getLivraisonsToBeDeployedByCdd(connectedUser);
            for (HarmonisationDTO harm : listLivraisonsToBeDeployedByCDD) {
                afficherLog("HARMONISATION Hotfix CDD ticket parent:" + harm.getLivParent().getNumeroLivraison() + " ticket fils:" + harm.getNumTicketLivraisonFils());
                boolean actionManuelleNonTraitee = deployTicketByCDD(harm.getCircuit(), harm.getLivParent(), harm.getNumTicketLivraisonFils(), connectedUser);
                if (actionManuelleNonTraitee == true) {
                    //Not T24 => ajout à l'email
                    String envName = null;
                    if (harm.getCircuit().equals("RELEASE")) {
                        envName = "DEVR";
                    } else {
                        envName = "DEV2";
                    }
                    TicketFilsDTO ticketFilsDto = new TicketFilsDTO(harm.getLivParent().getNumeroAnomalie(), harm.getLivParent().getNumeroLivraison(), harm.getNumTicketLivraisonFils(), envName, harm.getDeveloppeur());
                    listTicketsNotT24.add(ticketFilsDto);
                }
            }
            //send email to CDD
            if (!listDetailsLivraisonsEmailCDD.isEmpty()) {
                genererMessageHarmonisationCDD(listTicketsNotT24);
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            Tools.traiterException(Tools.getStackTrace(exep));
        }
    }

    public List<HarmonisationDTO> getLivraisonsToBeDeployedByCdd(String connectedUser) {
        List<HarmonisationDTO> listLivraisonsToBeDeployedByCDD = new ArrayList<HarmonisationDTO>();
        try {
            DataBaseTools dbToolsTrac = new DataBaseTools(Configuration.puLivraisons);
            DataBaseTools dbToolsOvtools = new DataBaseTools(Configuration.puOvTools);
            List<Integer> ticketsIdListRelease = AppelRequetes.getTicketsLivraisonsIdCddHarmCR(dbToolsTrac);
            listLivraisonsToBeDeployedByCDD.addAll(getLivraisonsToBeDeployedByCddByCircuit(ticketsIdListRelease, "RELEASE", connectedUser, dbToolsOvtools));
            List<Integer> ticketsIdListProjet = AppelRequetes.getTicketsLivraisonsIdCddHarmCP(dbToolsTrac);
            listLivraisonsToBeDeployedByCDD.addAll(getLivraisonsToBeDeployedByCddByCircuit(ticketsIdListProjet, "PROJET", connectedUser, dbToolsOvtools));
            dbToolsTrac.closeRessources();
            dbToolsOvtools.closeRessources();
        } catch (Exception exep) {
            exep.printStackTrace();
            Tools.traiterException(Tools.getStackTrace(exep));
        }
        return listLivraisonsToBeDeployedByCDD;
    }

    public List<HarmonisationDTO> getLivraisonsToBeDeployedByCddByCircuit(List<Integer> ticketsIdList, String circuit, String connectedUser, DataBaseTools dbTools) {
        List<HarmonisationDTO> listLivraisonsToBeDeployedByCDD = new ArrayList<HarmonisationDTO>();
        try {
            String[] cles = new String[]{"ticket_appl_prod", "contenu_des_livrables", "action_manuelle"};
            Map<Integer, Map<String, Object>> ticketMap = AppelRequetes.getTicketCustomByTicketIdAndNames(ticketsIdList, Configuration.puLivraisons, Configuration.tracLivraisons, cles);
            for (Map.Entry<Integer, Map<String, Object>> mapDetailsEntry : ticketMap.entrySet()) {
                Map<String, Object> mapDetails = mapDetailsEntry.getValue();
                String contenuDeslivrables = "";
                Livraison livParent = null;
                String developpeur = "";
                String actionManuelle = null;
                if (mapDetails.containsKey("contenu_des_livrables")) {
                    contenuDeslivrables = mapDetails.get("contenu_des_livrables").toString().trim();
                }
                if (mapDetails.containsKey("action_manuelle")) {
                    actionManuelle = mapDetails.get("action_manuelle").toString();
                }
                if (mapDetails.containsKey("ticket_appl_prod")) {
                    String numLivParentString = Tools.reformTlivTicketOrigine("ticket_appl_prod", Tools.traiterChamp(mapDetails, "ticket_appl_prod"));
                    Integer ticketLivraisonId = Integer.parseInt(numLivParentString);
                    livParent = dbTools.em.find(Livraison.class, ticketLivraisonId);
                    try {
                        developpeur = livParent.getOwner();
                    } catch (Exception ex) {
                    }
                }
                HarmonisationDTO harmDTO = new HarmonisationDTO(circuit, livParent, mapDetailsEntry.getKey(), connectedUser, contenuDeslivrables, developpeur, actionManuelle);
                listLivraisonsToBeDeployedByCDD.add(harmDTO);
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            Tools.traiterException(Tools.getStackTrace(exep));
        }
        return listLivraisonsToBeDeployedByCDD;
    }

    public void harmonistaionDesTicketsDefalques(String connectedUser) {
        try {
            livraisonsBloquees = new TreeSet<Livraison>();
            listDetailsLivraisonsEmailCDD.clear();
            Map<String, List<Map<String, Object>>> mapPipeTickets = new DataBaseTracRequests().getAllPipeTicketsRequestT24("OV", Configuration.puLivraisons);
            Map<String, List<Map<String, Object>>> mapPipeTicketsIE = new DataBaseTracRequests().getAllPipeTicketsRequestT24("IE", Configuration.puLivraisons);
            //Deployer tickets par I&E
            harmonistaionDesTicketsDefalquesParCircuit("RELEASE", connectedUser, mapPipeTickets.get("HARMONISATION_C.RELEASE"));
            harmonistaionDesTicketsDefalquesParCircuit("PROJET", connectedUser, mapPipeTickets.get("HARMONISATION_C.PROJET"));
            harmonistaionDesTicketsDefalquesParCircuit("RELEASE", connectedUser, mapPipeTicketsIE.get("HARMONISATION_C.RELEASE"));
            harmonistaionDesTicketsDefalquesParCircuit("PROJET", connectedUser, mapPipeTicketsIE.get("HARMONISATION_C.PROJET"));
            //Deployer tickets par CDD
            bloquerLivraisons();
        } catch (Exception exep) {
            exep.printStackTrace();
            Tools.traiterException(Tools.getStackTrace(exep));
        }
    }

    private void harmonistaionDesTicketsDefalquesParCircuit(String circuit, String connectedUser, List<Map<String, Object>> listHotfixHarm) {
        try {
            String envConcat = null;
            afficherLog("_______ HARMONISTATION TICKETS DEFALQUES " + circuit + " ________");
            if (circuit.equals("RELEASE")) {
                envConcat = Configuration.parametresList.get("environnementsCircuitRelease");
            } else {
                envConcat = "ASS2, " + Configuration.parametresList.get("environnementsCircuitProjet");
            }
            if (listHotfixHarm != null) {
                for (Map<String, Object> mapDetails : listHotfixHarm) {
                    Ticket ticket = (Ticket) mapDetails.get("Ticket");
                    Integer numTicketLivraisonFils = ticket.getId();
                    if (ticket.getPriority().equals("PROBLEME DE DEPLOIEMENT")) {
                        continue;
                    }
                    //récupérer le ticket parent
                    String numLivParentString = "";
                    if (mapDetails.containsKey("ticket_appl_prod")) {
                        numLivParentString = Tools.reformTlivTicketOrigine("ticket_appl_prod", Tools.traiterChamp(mapDetails, "ticket_appl_prod"));
                    }
                    int ticketLivraisonId = Integer.parseInt(numLivParentString);
                    afficherLog("--------------------------------------");
                    afficherLog("CIRCUIT:" + circuit + "  HARMONISTATION TICKETS DEFALQUES FILS:" + numTicketLivraisonFils + " PARENT: " + ticketLivraisonId);
                    //récupérer la livraison relative au ticket parent
                    DataBaseTools dbTools = new DataBaseTools(Configuration.puOvTools);
                    Livraison livParent = dbTools.em.find(Livraison.class, ticketLivraisonId);
                    if (livParent.getBloquerHarmonisation() || livParent.getContenuLivrables().equals("CREATION COMPANY")) {
                        // si le ticket présente une intersection ou a causé un pb de dep lors de l'harmonisation ou de type company, ne rien faire
                        continue;
                    }
                    // détecter les champs
                    String resultatEtudeIntersection = null;
                    if (circuit.equals("RELEASE")) {
                        resultatEtudeIntersection = livParent.getResultatetudeintersectionCR();
                    } else {
                        resultatEtudeIntersection = livParent.getResultatetudeintersectionCP();
                    }
                    if (resultatEtudeIntersection != null && resultatEtudeIntersection.length() > 0) {
                        //si le developpeur a déjà effectué l'harm cad devx coché => ne rien faire
                        String envDevCoche = "0";
                        if (circuit.equals("RELEASE")) {
                            envDevCoche = mapDetails.get("biatdevr").toString();
                        } else {
                            envDevCoche = mapDetails.get("biatdev2").toString();
                        }
                        if (envDevCoche.equals("0")) {
                            //intersection => redirection to Khalil
                            afficherLog("REDIRECTION TO KHALIL TICKETS DEFALQUES FILS:" + numTicketLivraisonFils + " PARENT: " + ticketLivraisonId);
                            String newOwner = null;
                            try {
                                DataBaseTools tracLivDbTools = new DataBaseTools(Configuration.puLivraisons);
                                Ticket ticketParent = getTicketById(tracLivDbTools, ticketLivraisonId);
                                tracLivDbTools.closeRessources();
                                newOwner = ticketParent.getCc();
                            } catch (Exception exep) {
                                newOwner = "mohamed khalil karray";
                                Tools.traiterException("Ticket fils qui a causé le pb: " + numTicketLivraisonFils + "<br><br>" + Tools.getStackTrace(exep));
                            }
                            redirectTicketIntersectionToCDD(circuit, livParent, numTicketLivraisonFils, connectedUser, newOwner);
                        } else {
                            Tools.traiterException("Attention, le Hotfix num #" + numTicketLivraisonFils + " n'a pas été envoyé au CDD car DEVx = 1");
                        }
                    } else {
                        //pas d'intersection => redirection to I&E
                        afficherLog("REDIRECTION TO I&E TICKETS DEFALQUES FILS:" + numTicketLivraisonFils + " PARENT: " + ticketLivraisonId);
                        boolean existeObjT24 = true;
                        if (resultatEtudeIntersection == null) {
                            existeObjT24 = false;
                        }
                        redirectTicketToIE(circuit, livParent, numTicketLivraisonFils, connectedUser, envConcat);
                        deployTicketByIE(circuit, livParent, numTicketLivraisonFils, connectedUser, envConcat, existeObjT24);
                    }
                    dbTools.closeRessources();
                }
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            Tools.traiterException(Tools.getStackTrace(exep));
        }
    }

    public boolean deployTicketByCDD(String circuit, Livraison livParent, Integer numTicketLivraisonFils, String connectedUser) {
        boolean actionManuelleNonTraitee = false;
        try {
            String log = "***** CDD HARM #" + numTicketLivraisonFils + " *****";
            afficherLog(log);
            EnvironnementDTO envDev = null;
            String folderName = "BACKUP_HOTFIXES";
            String serverDirSource = "./";
            String envDevName = null;
            if (circuit.equals("RELEASE")) {
                envDev = Configuration.environnementDTOMapUserHasEnvironnement.get(connectedUser).get("DEVR");
                envDevName = "DEVR";
            } else {
                envDev = Configuration.environnementDTOMapUserHasEnvironnement.get(connectedUser).get("DEV2");
                envDevName = "DEV2";
            }
            T24Scripts t24Scripts = new T24Scripts();
            StringBuilder resultatGlobal = new StringBuilder();
            List<CoupleDTO> listeCustomFields = null;
            //test authentification
            String resultatTestAuthentification = "";
            resultatTestAuthentification = t24Scripts.testLoginPasswordSystemeEtBrowser(envDev, connectedUser).trim();
            if (!resultatTestAuthentification.equals("TEST CONNEXION OK")) {
                resultatGlobal.append("\n== Problème d'authentification sur l'environnement ").append(envDev.getNom()).append(" :==").append("\n{{{\n#!html\n");
                resultatGlobal.append(resultatTestAuthentification).append("\n}}}\n");
            } else {
                //créer le dossier BACKUP_HOTFIXES s'il n'existe pas
                if (livParent.getLivrables() != null) {
                    for (Object livDto : livParent.getLivrables()) {
                        if (livDto instanceof T24) {
                            T24 livT24 = (T24) livDto;
                            String packName = livT24.getPackName();
                            String mnemonicCompany = livT24.getSelectedMnemonic();
                            String objectList = livT24.getObjetsT24();
                            FtpTools ftpTools = new FtpTools();
                            ftpTools.createFolder(folderName, serverDirSource, envDev);
                            String fileName = null;
                            try {
                                fileName = packName.replace("TAF-", "") + ".txt";
                            } catch (Exception exep) {
                                exep.printStackTrace();
                                Tools.traiterException("packName null:  Livraison parent:" + livParent.getNumeroLivraison() + "  Livraison fils:" + numTicketLivraisonFils + " \n" + Tools.getStackTrace(exep));
                            }
                            //PackMan depuis DEVx
                            int objectNumber;
                            List<String> listOfObjectsImpacted;
                            try {
                                PM pm = new PM(envDev, fileName, mnemonicCompany, folderName, connectedUser);
                                objectNumber = pm.preparerPackMan(objectList);
                                pm.PmFormerPack();
                            } catch (IOException ex) {
                                Logger.getLogger(GestionHotfixProdThread.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            //Lister les objets qui ont été touché sur l'env de dev
                            Tools tools = new Tools();
                            listOfObjectsImpacted = tools.listDirectoryFiles(envDev, folderName, "TAF-" + packName);
                            String objetsImpactes = "";
                            for (String objT24 : listOfObjectsImpacted) {
                                objetsImpactes += objT24 + "<br>";
                            }
                            //preparer DTO envoi email to CDD
                            AutomatisationHarmonisationCddDTO auxDto = new AutomatisationHarmonisationCddDTO(envDev.getNom(), livParent.getNumeroLivraison().toString(), livParent.getNumeroAnomalie().toString(), numTicketLivraisonFils.toString(), folderName + "/TAF-" + packName, objectList.replaceAll("\n", "<br>"), objetsImpactes);
                            listDetailsLivraisonsEmailCDD.add(auxDto);
                            //Deploiement sur DEVx
                            //List<TripleDTO> packName_companyMnemonic_nbrIter_liste = DeploiementParalleleTools.genererDtoList(new String[]{"TAF-" + livParent.getNomPack()}, new String[]{livParent.getCompanyDeploiement()}, new String[]{Integer.toString(livParent.getNombreIterations())});
                            String[] resTab = null;
                            List<String> envDepOkList = null;
                            // DEP sur DEVx
                            //Map<String, String[]> resultMap = new DeploiementParalleleTools().deploiementParalleleMultiPack(connectedUser, "ASS", "PACK.TAF", "", new String[]{"TAF-" + livParent.getNomPack()}, new String[]{envDevName}, packName_companyMnemonic_nbrIter_liste, true);
                            ManipulationObjectsTool manObjTools = new ManipulationObjectsTool();
                            Map<String, String[]> resultMap = manObjTools.traiterLivrable(livParent, "IE", connectedUser, new String[]{envDevName}, "ASS", true);
                            DataBaseTools tracLivDbTools = new DataBaseTools(Configuration.puLivraisons);
                            Ticket ticket = getTicketById(tracLivDbTools, numTicketLivraisonFils);
                            tracLivDbTools.closeRessources();
                            if (resultMap.containsKey("PROBLEME") && resultMap.get("PROBLEME")[0].length() > 0 && resultMap.get("PROBLEME")[1].length() > 0) {
                                //problème d'authentification, prob d'existance de pack ou ...
                                resTab = resultMap.get("PROBLEME");
                                resultatGlobal.append(resTab[0]).append(resTab[1]);
                                alertParEmail(ticket, "Probleme Harmonisation OV #" + numTicketLivraisonFils, resTab[0] + "  " + resTab[1], "C24OV@biat.com.tn");
                            } else {
                                String messageTrac = resultMap.get("RESULTAT_HTML_COMPLET")[0];
                                String[] envDepOkArray = resultMap.get("ENV_DEP_OK");
                                String problemesDeploiementsString = resultMap.get("RESULTAT")[1];
                                resultatGlobal.append(messageTrac);
                                envDepOkList = Arrays.asList(envDepOkArray);

                                if (problemesDeploiementsString != null && problemesDeploiementsString.trim().length() > 0) {
                                    alertParEmail(ticket, "PROBLEME DE DEPLOIEMENT HARMONISATION CDD " + numTicketLivraisonFils, "Bonjour,<br>L'harmonisation de la livraison <a href='http://172.28.70.74/trac/livraisons_t24/ticket/" + numTicketLivraisonFils + "'>#" + numTicketLivraisonFils + "</a> présente un problème de déploiement.");
                                    afficherLog("PROBLEME DE DEPLOIEMENT HARMONISATION CDD");
                                } else {
                                    if (messageTrac.contains("<span class='errorBold'> (A traiter manuellement)") || messageTrac.contains("Ce ticket sera traite manuellement")) {
                                        //si il existe une action manuelle non traitée
                                        alertParEmail(ticket, "PROBLEME DE DEPLOIEMENT HARMONISATION CDD " + numTicketLivraisonFils, "Bonjour,<br>La livraison <a href='http://172.28.70.74/trac/livraisons_t24/ticket/" + numTicketLivraisonFils + "'>#" + numTicketLivraisonFils + "</a> contient une action manuelle, prière de la passer manuellement et de cocher l'environnement de développement");
                                        actionManuelleNonTraitee = true;
                                    }
                                }
                                listeCustomFields = new ArrayList<>();
                                if (envDepOkList.contains(envDevName) && actionManuelleNonTraitee == false) {
                                    if (circuit.equals("RELEASE")) {
                                        listeCustomFields.add(new CoupleDTO("biatdevr", "1"));
                                    } else {
                                        listeCustomFields.add(new CoupleDTO("biatdev2", "1"));
                                    }
                                }
                            }
                        }
                    }
                }
            }
            String messageTrac = resultatGlobal.toString();
            DataBaseTracGenericRequests.updateTicketTracGeneral(Configuration.puLivraisons, numTicketLivraisonFils, "OVTOOLS", messageTrac, null, null, null, null, listeCustomFields);
        } catch (Exception exep) {
            exep.printStackTrace();
            Tools.traiterException(Tools.getStackTrace(exep));
        }
        return actionManuelleNonTraitee;
    }

    private void deployTicketByIE(String circuit, Livraison livParent, Integer numTicketLivraisonFils, String connectedUser, String envConcat, boolean existeObjT24) {
        try {
            afficherLog("DEPLOIEMENT TICKETS DEFALQUES FILS:" + numTicketLivraisonFils + " PARENT: " + livParent.getNumeroLivraison());
            if (existeObjT24 == false) {
                return;
            }
            String[] tabEnvNameCircuit = null;
            if (circuit.equals("RELEASE")) {
                tabEnvNameCircuit = Tools.separerEnvironnementsNoms(envConcat);
            } else {
                tabEnvNameCircuit = Tools.separerEnvironnementsNoms(envConcat);
            }
            StringBuilder resultatGlobal = new StringBuilder();
            // test cob sur tous les environnements
            boolean cobEnCours = true;
            while (cobEnCours) {
                String[] resTab = new DeploiementParalleleTools().testCobEnCours(tabEnvNameCircuit, connectedUser);
                if (resTab != null) {
                    afficherLog("DEPLOIEMENT TICKETS DEFALQUES FILS:" + numTicketLivraisonFils + " PARENT: " + livParent.getNumeroLivraison() + " L'harmonisation des Hotfixes est bloquée " + resTab[0] + " => " + resTab[1]);
                    System.out.println("L'harmonisation des Hotfixes est bloquée " + resTab[0] + " => " + resTab[1]);
                    cobEnCours = true;
                    try {
                        afficherLog("DEPLOIEMENT TICKETS DEFALQUES FILS:" + numTicketLivraisonFils + " PARENT: " + livParent.getNumeroLivraison() + " SLEEP 15 MIN");
                        //sleep 15 min en attente de défalquation
                        afficherLog("6- sleep(1000 * 60 * 15)");
                        Thread.sleep(1000 * 60 * 15);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(GestionHotfixProdThread.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    cobEnCours = false;
                }
            }



            T24Scripts t24Scripts = new T24Scripts();
            List<String> envProbAuthentification = new ArrayList<String>();
            for (String envName : tabEnvNameCircuit) {
                EnvironnementDTO env = Configuration.environnementDTOMapUserHasEnvironnement.get(connectedUser).get(envName);
                //test authentification
                String resultatTestAuthentification = "";
                resultatTestAuthentification = t24Scripts.testLoginPasswordSystemeEtBrowser(env, connectedUser).trim();
                if (!resultatTestAuthentification.equals("TEST CONNEXION OK")) {
                    resultatGlobal.append("\n== Problème d'authentification sur l'environnement ").append(envName).append(" :==").append("\n{{{\n#!html\n");
                    resultatGlobal.append(resultatTestAuthentification).append("\n}}}\n");
                    envProbAuthentification.add(envName);
                }
            }
            if (envProbAuthentification.isEmpty()) {
                //pas de problème d'authentification
                //List<TripleDTO> packName_companyMnemonic_nbrIter_liste = DeploiementParalleleTools.genererDtoList(new String[]{"TAF-" + livParent.getNomPack()}, new String[]{livParent.getCompanyDeploiement()}, new String[]{Integer.toString(livParent.getNombreIterations())});
                String[] resTab = null;
                List<String> envDepOkList = null;
                afficherLog("CIRCUIT:" + circuit + "  DEPLOIEMENT PARALLELE FILS:" + numTicketLivraisonFils + " PARENT: " + livParent.getNumeroLivraison());
                // DEP sur CR ou CP I&E
                //Map<String, String[]> resultMap = new DeploiementParalleleTools().deploiementParalleleMultiPack(connectedUser, "ASS", "PACK.TAF", "", new String[]{"TAF-" + livParent.getNomPack()}, tabEnvNameCircuit, packName_companyMnemonic_nbrIter_liste, true);

                ManipulationObjectsTool manObjTools = new ManipulationObjectsTool();
                Map<String, String[]> resultMap = manObjTools.traiterLivrable(livParent, "IE", connectedUser, tabEnvNameCircuit, "ASS", true);

                DataBaseTools tracLivDbTools = new DataBaseTools(Configuration.puLivraisons);
                Ticket ticket = getTicketById(tracLivDbTools, numTicketLivraisonFils);
                tracLivDbTools.closeRessources();
                if (resultMap.containsKey("PROBLEME") && resultMap.get("PROBLEME")[0].length() > 0 && resultMap.get("PROBLEME")[1].length() > 0) {
                    //problème d'authentification, prob d'existance de pack ou ...
                    resTab = resultMap.get("PROBLEME");
                    resultatGlobal.append(resTab[0]).append(resTab[1]);
                    alertParEmail(ticket, "Probleme Harmonisation OV #" + numTicketLivraisonFils, resTab[0] + "  " + resTab[1], "C24OV@biat.com.tn");
                } else {
                    String msgTrac = resultMap.get("RESULTAT_HTML_COMPLET")[0];
                    resultatGlobal.append(msgTrac);
                    String[] envDepOkArray = resultMap.get("ENV_DEP_OK");
                    envDepOkList = Arrays.asList(envDepOkArray);
                    String problemesDeploiementsString = resultMap.get("RESULTAT")[1];
                    envDepOkList = Arrays.asList(envDepOkArray);

                    //cocher les cases à cocher            
                    List<CoupleDTO> listeCustomFields = null;
                    if (envDepOkList != null) {
                        listeCustomFields = new ArrayList<CoupleDTO>();
                    }
                    for (String envName : tabEnvNameCircuit) {
                        String envNameBox = null;
                        if (Configuration.mapEnvNameCheckBoxName.containsKey(envName)) {
                            envNameBox = Configuration.mapEnvNameCheckBoxName.get(envName);
                        } else {
                            envNameBox = "biat" + envName.toLowerCase();
                        }
                        if (envDepOkList != null) {
                            if (envDepOkList.contains(envName)) {
                                listeCustomFields.add(new CoupleDTO(envNameBox, "1"));
                            } else {
                                listeCustomFields.add(new CoupleDTO(envNameBox, "0"));
                            }
                        }
                    }
                    boolean actionManuelleNonTraitee = false;
                    if (problemesDeploiementsString != null && problemesDeploiementsString.trim().length() > 0) {
                        alertParEmail(ticket, "Problème de déploiement " + numTicketLivraisonFils, "Bonjour,<br>L'harmonisation de la livraison <a href='http://172.28.70.74/trac/livraisons_t24/ticket/" + numTicketLivraisonFils + "'>#" + numTicketLivraisonFils + "</a> présente un problème de déploiement.", "C24OV@biat.com.tn");
                        afficherLog("PROBLEME DE DEPLOIEMENT HARMONISATION IE");
                        //il y a problème de deploiement
                        DataBaseTracGenericRequests.updateTicketTracGeneral(Configuration.puLivraisons, numTicketLivraisonFils, "OVTOOLS", "'''Brobleme de deploiement:'''[[BR]] " + resultatGlobal.toString(), "raafet dormok", "PROBLEME DE DEPLOIEMENT", null, null, listeCustomFields);
                        bloquerLivraison(livParent);
                    } else {
                        StringBuilder messageTrac = new StringBuilder();
                        String newPriority = null;
                        if (!msgTrac.contains("<span class='errorBold'> (A traiter manuellement)") && !msgTrac.contains("Ce ticket sera traite manuellement")) {
                            //deploiement ok => cloturer ticket                            
                            messageTrac.append("L'integration de livraison n° '''").append(livParent.getNumeroLivraison()).append(" ''' (Circuit HOTFIX) a été effectuée correctement sur le(s) environnement(s) suivant(s): '''").append(envConcat).append("'''[[BR]]");
                            newPriority = "DEPLOYEE";
                        } else {
                            newPriority = "PRET POUR DEPLOIEMENT";
                            messageTrac.append("\n{{{\n#!html\n </br><p><b>A Executer l'action manuelle mentionnee ci-dessous</b></p> \n}}}\n ");
                        }
                        messageTrac.append(resultatGlobal);
                        DataBaseTracGenericRequests.updateTicketTracGeneral(Configuration.puLivraisons, numTicketLivraisonFils, "OVTOOLS", messageTrac.toString(), null, newPriority, null, null, listeCustomFields);
                    }
                }
            } else {
                //notifier le problème d'authentification dans le ticket
                DataBaseTracGenericRequests.updateTicketTracGeneral(Configuration.puLivraisons, numTicketLivraisonFils, "OVTOOLS", "'''Brobleme d'authentification''[[BR]] " + resultatGlobal.toString(), null, null, null, null, null);
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            Tools.traiterException(Tools.getStackTrace(exep));
        }
    }

    private void redirectTicketToIE(String circuit, Livraison livParent, Integer numTicketLivraisonFils, String connectedUser, String envConcat) {
        try {
            String messageTrac = livParent.getMessageTrac().replaceAll("PROD", envConcat).replaceAll("INV", envConcat);
            List<CoupleDTO> listeCustomFields = new ArrayList<>();
            if (circuit.equals("RELEASE")) {
                listeCustomFields.add(new CoupleDTO("biattem", "1"));
            } else {
                listeCustomFields.add(new CoupleDTO("biatass2", "1"));
            }
            DataBaseTracGenericRequests.updateTicketTracGeneral(Configuration.puLivraisons, numTicketLivraisonFils, "OVTOOLS", messageTrac, "riadh anouar ben dakhlia", "PRET POUR DEPLOIEMENT", "assigned", null, listeCustomFields);
        } catch (Exception exep) {
            exep.printStackTrace();
            Tools.traiterException(Tools.getStackTrace(exep));
        }
    }

    private void redirectTicketIntersectionToCDD(String circuit, Livraison livParent, Integer numTicketLivraisonFils, String connectedUser, String newOwner) {
        try {
            String resultatEtudeIntersection = null;
            String packName = "";
            if (circuit.equals("RELEASE")) {
                resultatEtudeIntersection = livParent.getResultatetudeintersectionCR();
            } else {
                resultatEtudeIntersection = livParent.getResultatetudeintersectionCP();
            }
            if (livParent.getLivrables() != null) {
                for (Object livDto : livParent.getLivrables()) {
                    if (livDto instanceof T24) {
                        T24 livT24 = (T24) livDto;
                        packName += livT24.getPackName() + " - ";
                    }
                }
            }

            StringBuilder messageTrac = new StringBuilder();
            messageTrac.append(resultatEtudeIntersection);
            messageTrac.append("Ce HOTFIX présente une intersection avec le circuit ");
            messageTrac.append(circuit);
            messageTrac.append(" (Voir détails ci-dessus).[[BR]]");
            messageTrac.append("Les anomalies entrants en intersection avec ce Hotfix doivent être re-testés.[[BR]]");
            messageTrac.append("A adapter les objets qui présentent une intersection sur l'environnement de développement en question [[BR]][[BR]]");
            if (packName.length() > 0) {
                messageTrac.append("NB: Le nom du pack est le suivant: ").append(packName);
            }
            DataBaseTracGenericRequests.updateTicketTracGeneral(Configuration.puLivraisons, numTicketLivraisonFils, "OVTOOLS", messageTrac.toString(), newOwner, "LIVRAISON CONFIRMEE", null, null, null);
            bloquerLivraison(livParent);
        } catch (Exception exep) {
            exep.printStackTrace();
            Tools.traiterException(Tools.getStackTrace(exep));
        }
    }

    public static void updateTicketTracOV(Livraison livraison, String connectedUser, String messageTrac, String defalquerOk, String... fieldsIntersection) {
        try {
            List<CoupleDTO> listeCustomFields = new ArrayList<>();
            listeCustomFields.add(new CoupleDTO("biatref", defalquerOk));
            for (String checkedNamed : fieldsIntersection) {
                listeCustomFields.add(new CoupleDTO(checkedNamed, "1"));
            }
            DataBaseTracGenericRequests.updateTicketTracGeneral(Configuration.puLivraisons, livraison.getNumeroLivraison(), "OVTOOLS", messageTrac, null, null, null, null, listeCustomFields);
        } catch (Exception exep) {
            exep.printStackTrace();
            Tools.traiterException(Tools.getStackTrace(exep));
        }
    }

    private String generateMessageHtml(Map<Integer, Map<String, Object>> ticketsQualifieesMap, String connectedUser) {
        Ticket tickAnomalie = null;
        String ticketLivraisonString = null;
        String[] ticketsLivraisonTab = null;
        StringBuilder sbResult = new StringBuilder();
        try {
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
            sbResult.append("\n    Bonsoir,");
            sbResult.append("\n</p>");
            sbResult.append("\n<p class='message'>");
            sbResult.append(Configuration.mapChiffres.get(String.valueOf(ticketsQualifieesMap.size()))).append(" HOTFIX à passer ce soir sur PROD:");
            sbResult.append("\n</p>");
            sbResult.append("\n");
            sbResult.append("\n<table cellspacing='0' cellpadding='5' >");
            sbResult.append("\n    <thead>");
            sbResult.append("\n        <tr style='background-color: #FDD999; color: #35427E'>");
            sbResult.append("\n            <th class='thTable'>Anomalie</th>");
            sbResult.append("\n            <th class='thTable'>Livraison</th>");
            sbResult.append("\n            <th class='thTable'>Summary</th>");
            sbResult.append("\n            <th class='thTable'>Milestone</th>");
            sbResult.append("\n            <th class='thTable'>Priority</th>");
            sbResult.append("\n            <th class='thTable'>Owner</th>");
            sbResult.append("\n            <th class='thTable'>Impacts éventuels sur le COB</th>");
            sbResult.append("\n            <th style='padding: 0px;border: none'></th>");
            sbResult.append("\n        </tr>");
            sbResult.append("\n    </thead>");
            sbResult.append("\n    <tbody>");
            for (Map.Entry<Integer, Map<String, Object>> entry : ticketsQualifieesMap.entrySet()) {
                tickAnomalie = (Ticket) entry.getValue().get("Ticket");
                ticketsLivraisonTab = entry.getValue().get("t_liv").toString().split("-");
                ticketLivraisonString = ticketsLivraisonTab[ticketsLivraisonTab.length - 1];
                sbResult.append("\n        <tr style='background-color: #faebd7; color: #666666'>");
                sbResult.append("\n            <td class='tdTable'><a href='http://172.28.70.74/trac/anomalies_t24/ticket/").append(entry.getKey()).append("' >#").append(entry.getKey()).append("</a></td>");
                sbResult.append("\n            <td class='tdTable'><a href='http://172.28.70.74/trac/livraisons_t24/ticket/").append(ticketLivraisonString).append("' >#").append(ticketLivraisonString).append("</a></td>");
                sbResult.append("\n            <td class='tdTable' style='max-width: 500px'>");
                sbResult.append(tickAnomalie.getSummary());
                sbResult.append("\n            </td>");
                sbResult.append("\n            <td class='tdTable'>").append(tickAnomalie.getMilestone()).append("</td>");
                sbResult.append("\n            <td class='tdTable'>").append(tickAnomalie.getPriority()).append("</td>");
                sbResult.append("\n            <td class='tdTable'>").append(tickAnomalie.getOwner()).append("</td>");
                sbResult.append("\n            <td class='tdTable'>").append(analyseReStatRepLine(ticketLivraisonString, connectedUser)).append("</td>");
                sbResult.append("\n            <td style='padding: 0px;border: none'></td>");
                sbResult.append("\n        </tr>");
            }
            sbResult.append("\n    </tbody>");
            sbResult.append("\n</table>");

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
        } catch (Exception exep) {
            exep.printStackTrace();
            Tools.traiterException(Tools.getStackTrace(exep));
        }
        return sbResult.toString();
    }

    public String analyseReStatRepLine(String ticketLivraisonString, String connectedUser) {
        String resultat = "";
        try {
            int numLiv = Integer.parseInt(ticketLivraisonString);
            DataBaseTools dbToolsOvtools = new DataBaseTools(Configuration.puOvTools);
            Livraison livParent = dbToolsOvtools.em.find(Livraison.class, numLiv);
            dbToolsOvtools.closeRessources();
            Tools tools = new Tools();
            if (livParent.getLivrables() != null) {
                for (Object livDto : livParent.getLivrables()) {
                    if (livDto instanceof T24) {
                        T24 livT24 = (T24) livDto;
                        String objetsT24 = livT24.getObjetsT24();
                        objetsT24 = tools.traiterString(objetsT24, connectedUser);
                        String[] tab = objetsT24.split("\n");
                        for (String ch : tab) {
                            if (ch.contains("RE.STAT.REP.LINE") && ch.contains("BLCTOS")) {
                                resultat += ch + "<br>";
                            }
                        }
                    }
                }
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return resultat;
    }

    private void genererMessageHarmonisationCDD(List<TicketFilsDTO> listTicketsNotT24) {
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
            sbResult.append("Ci-dessous la liste des hotfix qui ont été harmonisés automatiquement.");
            sbResult.append("\n<br>");
            sbResult.append("Avant de procéder au déploiement des objets sur les environnements de développement, une sauvegarde d'objets a été effectuée.");
            sbResult.append("\n</p><p></p>");
            sbResult.append("\n");
            sbResult.append("\n<table cellspacing='0' cellpadding='5' >");
            sbResult.append("\n    <thead>");
            sbResult.append("\n        <tr style='background-color: #FDD999; color: #35427E'>");
            sbResult.append("\n            <th class='thTable'>Anomalie</th>");
            sbResult.append("\n            <th class='thTable'>Livraison</th>");
            sbResult.append("\n            <th class='thTable'>Ticket fils</th>");
            sbResult.append("\n            <th class='thTable'>Environnement</th>");
            sbResult.append("\n            <th class='thTable'>Path du pack sauvegardé</th>");
            sbResult.append("\n            <th class='thTable'>Liste d'objets déployés</th>");
            sbResult.append("\n            <th class='thTable'>Liste d'objets impactés</th>");
            sbResult.append("\n            <th style='padding: 0px;border: none'></th>");
            sbResult.append("\n        </tr>");
            sbResult.append("\n    </thead>");
            sbResult.append("\n    <tbody>");
            for (AutomatisationHarmonisationCddDTO dto : listDetailsLivraisonsEmailCDD) {
                String tickAnomalie = dto.getAnomalie();
                String ticketLivraison = dto.getNumLivraisonHotfix();
                String ticketLivraisonFils = dto.getNumTicketLivraisonFils();
                String envName = dto.getEnvDev();
                String pathPack = dto.getPathPackSauvegarde();
                String listObjDeployes = dto.getObjetsDeployes();
                String listObjsImpactes = dto.getObjetsImpactes();
                sbResult.append("\n        <tr style='background-color: #faebd7; color: #666666'>");
                sbResult.append("\n            <td class='tdTable'><a href='http://172.28.70.74/trac/anomalies_t24/ticket/").append(tickAnomalie).append("' >#").append(tickAnomalie).append("</a></td>");
                sbResult.append("\n            <td class='tdTable'><a href='http://172.28.70.74/trac/livraisons_t24/ticket/").append(ticketLivraison).append("' >#").append(ticketLivraison).append("</a></td>");
                sbResult.append("\n            <td class='tdTable'><a href='http://172.28.70.74/trac/livraisons_t24/ticket/").append(ticketLivraisonFils).append("' >#").append(ticketLivraisonFils).append("</a></td>");
                sbResult.append("\n            <td class='tdTable'>").append(envName).append("</td>");
                sbResult.append("\n            <td class='tdTable'>").append(pathPack).append("</td>");
                sbResult.append("\n            <td class='tdTable'>").append(listObjDeployes).append("</td>");
                sbResult.append("\n            <td class='tdTable'>").append(listObjsImpactes).append("</td>");
                sbResult.append("\n            <td style='padding: 0px;border: none'></td>");
                sbResult.append("\n        </tr>");
            }
            sbResult.append("\n    </tbody>");
            sbResult.append("\n</table>");
            sbResult.append("\n<br>");
            //Hotfix non harmonisés avec action manuelle
            if (!listTicketsNotT24.isEmpty()) {
                sbResult.append("Ci-dessous la liste des hotfix qui contiennent des actions manuelles et qui n'ont pas été harmonisés automatiquement.");
                sbResult.append("\n</p><p></p>");
                sbResult.append("\n");
                sbResult.append("\n<table cellspacing='0' cellpadding='5' >");
                sbResult.append("\n    <thead>");
                sbResult.append("\n        <tr style='background-color: #FDD999; color: #35427E'>");
                sbResult.append("\n            <th class='thTable'>Anomalie</th>");
                sbResult.append("\n            <th class='thTable'>Livraison</th>");
                sbResult.append("\n            <th class='thTable'>Ticket fils</th>");
                sbResult.append("\n            <th class='thTable'>Responsable</th>");
                sbResult.append("\n            <th class='thTable'>Environnement</th>");
                sbResult.append("\n            <th style='padding: 0px;border: none'></th>");
                sbResult.append("\n        </tr>");
                sbResult.append("\n    </thead>");
                sbResult.append("\n    <tbody>");
                for (TicketFilsDTO dto : listTicketsNotT24) {
                    Integer tickAnomalie = dto.getAnomalie();
                    Integer ticketLivraison = dto.getLivraison();
                    Integer ticketLivraisonFils = dto.getTicketFils();
                    String envName = dto.getEnvironnementName();
                    String developpeur = dto.getDeveloppeur();
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
                sbResult.append("\n<br>");
            }

            //fin
            initQueries();
            //Hotfix CR qui nécessitent une adaptation sur DEVR
            if (!ticketMapHfAdaptationCR.isEmpty()) {
                sbResult.append("Ci-dessous la liste des hotfix qui doivent être adaptés sur C.R (DEVR):");
                sbResult.append("\n</p><p></p>");
                sbResult.append("\n");
                sbResult.append("\n<table cellspacing='0' cellpadding='5' >");
                sbResult.append("\n    <thead>");
                sbResult.append("\n        <tr style='background-color: #FDD999; color: #35427E'>");
                sbResult.append("\n            <th class='thTable'>Anomalie</th>");
                sbResult.append("\n            <th class='thTable'>Livraison</th>");
                sbResult.append("\n            <th class='thTable'>Ticket fils</th>");
                sbResult.append("\n            <th class='thTable'>Responsable</th>");
                sbResult.append("\n            <th class='thTable'>Environnement</th>");
                sbResult.append("\n            <th style='padding: 0px;border: none'></th>");
                sbResult.append("\n        </tr>");
                sbResult.append("\n    </thead>");
                sbResult.append("\n    <tbody>");
                for (Map.Entry<Integer, Map<String, Object>> entry : ticketMapHfAdaptationCR.entrySet()) {
                    Map<String, Object> tickMap = entry.getValue();
                    String tickAnomalie = (String) tickMap.get("ticket_origine");
                    String ticketLivraison = (String) tickMap.get("ticket_appl_prod");
                    Integer ticketLivraisonFils = entry.getKey();
                    String envName = "DEVR";
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
                sbResult.append("\n<br>");
            }

            //fin

            //Hotfix CP qui nécessitent une adaptation sur DEV2
            if (!ticketMapHfAdaptationCP.isEmpty()) {
                sbResult.append("Ci-dessous la liste des hotfix qui doivent être adaptés sur C.P (DEV2):");
                sbResult.append("\n</p><p></p>");
                sbResult.append("\n");
                sbResult.append("\n<table cellspacing='0' cellpadding='5' >");
                sbResult.append("\n    <thead>");
                sbResult.append("\n        <tr style='background-color: #FDD999; color: #35427E'>");
                sbResult.append("\n            <th class='thTable'>Anomalie</th>");
                sbResult.append("\n            <th class='thTable'>Livraison</th>");
                sbResult.append("\n            <th class='thTable'>Ticket fils</th>");
                sbResult.append("\n            <th class='thTable'>Responsable</th>");
                sbResult.append("\n            <th class='thTable'>Environnement</th>");
                sbResult.append("\n            <th style='padding: 0px;border: none'></th>");
                sbResult.append("\n        </tr>");
                sbResult.append("\n    </thead>");
                sbResult.append("\n    <tbody>");
                for (Map.Entry<Integer, Map<String, Object>> entry : ticketMapHfAdaptationCP.entrySet()) {
                    Map<String, Object> tickMap = entry.getValue();
                    String tickAnomalie = (String) tickMap.get("ticket_origine");
                    String ticketLivraison = (String) tickMap.get("ticket_appl_prod");
                    Integer ticketLivraisonFils = entry.getKey();
                    String envName = "DEV2";
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
            }
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
            tools.Tools.sendEMail("Harmonisation des HOTFIX", "OV.Management.Solutions@biat.com.tn", new String[]{"CH_DEV@biat.com.tn", "MONDHERM@biat.com.tn", "mohsen.ouertani@biat.com.tn", "sami.ferchichi@biat.com.tn", "hatem.chaabane@biat.com.tn", "meha.meftah@biat.com.tn", "t24_csh@biat.com.tn", "moncef.mallek@biat.com.tn"}, new String[]{"C24OV@biat.com.tn", "jamel.bahri@biat.com.tn"}, new String[]{}, emailContent, false);
        } catch (Exception exep) {
            exep.printStackTrace();
            Tools.traiterException(Tools.getStackTrace(exep));
        }
    }

    private Map<String, Map<String, Object>> deleteNonQualifiedTickets(Map<String, Map<String, Object>> ticketsQualifieesMap) {
        try {
            Iterator<Map.Entry<String, Map<String, Object>>> iter = ticketsQualifieesMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, Map<String, Object>> entry = iter.next();
                Ticket tick = (Ticket) entry.getValue().get("Ticket");
                if (!tick.getPriority().equals("QUALIFIEE")) {
                    iter.remove();
                }
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            Tools.traiterException(Tools.getStackTrace(exep));
        }
        return ticketsQualifieesMap;
    }

    private void initialisationVariables() {
        try {
            DataBaseTools dbTools = new DataBaseTools(Configuration.puOvTools);
            Configuration.chargerTousLesCircuitsDetails(dbTools);
            dbTools.closeRessources();
            //Récupérer TempDamarrage et TempArret a patrir de la base de donnée 
            startSendingHfProdDateString = Configuration.etatCircuitMap.get("START_SENDING_HF_PROD");
            endSendingHfProdDateString = Configuration.etatCircuitMap.get("END_SENDING_HF_PROD");
            startHarmonisationHfProdDateString = Configuration.etatCircuitMap.get("START_HARMONISATION_HF_PROD");
            harmonisationHfProd = Configuration.etatCircuitMap.get("HARMONISATION_HF_PROD");
            sendingHfProd = Configuration.etatCircuitMap.get("SENDING_HF_PROD");
            ///////////////////////////////
            //System.out.println(endSendingHfProdDateString);

            SimpleDateFormat parseFormat = new SimpleDateFormat(Configuration.HEURE_FORMAT);
            Date endSendingHfProdDate;
            try {
                endSendingHfProdDate = parseFormat.parse(endSendingHfProdDateString);
                Calendar cal = Calendar.getInstance();
                cal.setTime(endSendingHfProdDate);
                cal.add(Calendar.MINUTE, 5);
                endSendingEmailProdDateString = parseFormat.format(cal.getTime());
            } catch (ParseException exep) {
                //afficher le contenu de toute la map des horaires
                String contenu = "";

                exep.printStackTrace();
                Tools.traiterException(Tools.getStackTrace(exep));
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            Tools.traiterException(Tools.getStackTrace(exep));
        }
    }

    private void bloquerLivraison(Livraison liv) {
        try {
            livraisonsBloquees.add(liv);
        } catch (Exception exep) {
            exep.printStackTrace();
            Tools.traiterException(Tools.getStackTrace(exep));
        }

    }

    private void bloquerLivraisons() {
        try {
            DataBaseTools dbTools = new DataBaseTools(Configuration.puOvTools);
            for (Livraison liv : livraisonsBloquees) {
                liv.setBloquerHarmonisation(true);
                dbTools.update(liv);
            }
            dbTools.closeRessources();
        } catch (Exception exep) {
            exep.printStackTrace();
            Tools.traiterException(Tools.getStackTrace(exep));
        }
    }

    public static void afficherLog(String texte) {
        try {
            DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String date = formatter.format(new Date());
            ManageLogThread.ecrireLogFichier("LOG_HARMONISTATION_HOTFIX", "\n" + date + "   " + texte);
        } catch (Exception exep) {
            exep.printStackTrace();
            Tools.traiterException(Tools.getStackTrace(exep));
        }
    }

    private void initQueries() {
        StringBuilder querySbCP = new StringBuilder("SELECT id as 'ticket', ");
        querySbCP.append("t.changetime as Date_arrivee, ");
        querySbCP.append("o.value AS deployee_sur_prod, ");
        querySbCP.append("o7.value AS deployee_sur_dev2, ");
        querySbCP.append("o5.value AS deployee_sur_ref, ");
        querySbCP.append("o6.value AS deployee_sur_ass2, ");
        querySbCP.append("o8.value AS nature_livraison ");
        querySbCP.append("FROM TICKET t ");
        querySbCP.append("LEFT OUTER JOIN ticket_custom o ON ");
        querySbCP.append("        (t.id=o.ticket AND o.name='biatprod') ");
        querySbCP.append("LEFT OUTER JOIN ticket_custom o5 ON ");
        querySbCP.append("        (t.id=o5.ticket AND o5.name='biatref') ");
        querySbCP.append("LEFT OUTER JOIN ticket_custom o6 ON ");
        querySbCP.append("       (t.id=o6.ticket AND o6.name='biatass2') ");
        querySbCP.append("LEFT OUTER JOIN ticket_custom o7 ON ");
        querySbCP.append("        (t.id=o7.ticket AND o7.name='biatdev2') ");
        querySbCP.append("LEFT OUTER JOIN ticket_custom o8 ON ");
        querySbCP.append("      (t.id=o8.ticket AND o8.name='nature_liv') ");
        querySbCP.append("where priority = 'LIVRAISON CONFIRMEE' ");
        querySbCP.append("and coalesce(o.value,0) = '1' ");
        querySbCP.append("and coalesce(o5.value,0) = '1' ");
        querySbCP.append("and coalesce(o6.value,0)  <> '1' ");
        querySbCP.append("and coalesce(o7.value,0) <> '1' ");
        querySbCP.append("and o8.value IN ('HARMONISATION_C.PROJET') ");
        querySbCP.append("order by Date_arrivee ");

        StringBuilder querySbCR = new StringBuilder("SELECT id as 'ticket', ");
        querySbCR.append("t.changetime AS Date_arrivee, ");
        querySbCR.append("o.value AS deployee_sur_prod, ");
        querySbCR.append("o1.value AS deployee_sur_certif, ");
        querySbCR.append("o2.value AS deployee_sur_qualif, ");
        querySbCR.append("o5.value AS deployee_sur_ref, ");
        querySbCR.append("o7.value AS deployee_sur_devr, ");
        querySbCR.append("o8.value AS nature_livraison ");
        querySbCR.append("FROM TICKET t ");
        querySbCR.append("LEFT OUTER JOIN ticket_custom o ON ");
        querySbCR.append("        (t.id=o.ticket AND o.name='biatprod') ");
        querySbCR.append("LEFT OUTER JOIN ticket_custom o1 ON ");
        querySbCR.append("        (t.id=o1.ticket AND o1.name='biatcertif') ");
        querySbCR.append("LEFT OUTER JOIN ticket_custom o2 ON ");
        querySbCR.append("        (t.id=o2.ticket AND o2.name='biatql1') ");
        querySbCR.append("LEFT OUTER JOIN ticket_custom o5 ON ");
        querySbCR.append("        (t.id=o5.ticket AND o5.name='biatref') ");
        querySbCR.append("LEFT OUTER JOIN ticket_custom o7 ON ");
        querySbCR.append("        (t.id=o7.ticket AND o7.name='biatdevr') ");
        querySbCR.append("LEFT OUTER JOIN ticket_custom o8 ON ");
        querySbCR.append("      (t.id=o8.ticket AND o8.name='nature_liv') ");
        querySbCR.append("LEFT OUTER JOIN ticket_custom o9 ON ");
        querySbCR.append("       (t.id=o9.ticket AND o9.name='biattem') ");
        querySbCR.append("where o8.value IN ('HARMONISATION_C.RELEASE') ");
        querySbCR.append("and coalesce(o.value,0) = '1' ");
        querySbCR.append("and coalesce(o1.value,0)  <> '1' ");
        querySbCR.append("and coalesce(o2.value,0)  <> '1' ");
        querySbCR.append("and coalesce(o5.value,0) = '1' ");
        querySbCR.append("and coalesce(o7.value,0) <> '1' ");
        querySbCR.append("and coalesce(o9.value,0)  <> '1' ");
        querySbCR.append("       and priority = 'LIVRAISON CONFIRMEE' ");
        querySbCR.append("order by Date_arrivee");

        ticketMapHfAdaptationCR = analyseTicketFils(querySbCR);
        ticketMapHfAdaptationCP = analyseTicketFils(querySbCP);
    }

    public static Map<Integer, Map<String, Object>> analyseTicketFils(StringBuilder querySbCP) {
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