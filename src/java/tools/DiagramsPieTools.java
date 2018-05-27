/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import dto.*;
import dataBaseTracRequests.AppelRequetes;
import entitiesTrac.Ticket;
import entitiesTrac.TicketCustom;
import java.util.*;

/**
 *
 * @author 04486
 */
public class DiagramsPieTools {

    private static String numReleaseEnCours;
    static Map<Integer, Map<String, Object>> mapTicketsCustomIncomplete = new LinkedHashMap<Integer, Map<String, Object>>();
    /*
     public static void main(String[] args) {
     Configuration.initialisation();
     servlets.AfficherMessageEtatAvancement.setLogmessage("Connexion à la base de données des Anomalies TRAC");
     numReleaseEnCours = Configuration.parametresList.get("releaseEnCours");
     DataBaseTools dbTools;
     Query q;
     dbTools = new DataBaseTools(Configuration.anomaliesPU);

     q = dbTools.em.createNamedQuery("TicketCustom.findByRelease");
     //q.setParameter("ReleaseEnCours", numReleaseEnCours);

     List<TicketCustom> globalTickList = (List<TicketCustom>) q.getResultList();
     dbTools.closeRessources();

     DataBaseRequests req = new DataBaseRequests();
     String[] cles = new String[]{"t_liv", "projet"};
     mapTicketsCustomIncomplete = req.getTicketCustomAnomaliesByTicketId(globalTickList, cles);
     servlets.AfficherMessageEtatAvancement.setLogmessage("Analyse de tous les tickets Anomalies de la Release " + numReleaseEnCours);
     //Map de tous les tickets
     Map<String, Integer> priorityMap = triAnomaliesPriority(globalTickList);
     //Les Maps distribués par nom de domaine
     DiagramsPieTools diagrammeToos = new DiagramsPieTools();
     Map<String, PriorityTicketDTO> mapDomainePriorityTicketDTO = diagrammeToos.sortTicketsByDomaine(globalTickList);
     Map<String, Map<String, Integer>> anomaliesPriorityNumber = diagrammeToos.getAnomaliesPriorityNumber(mapDomainePriorityTicketDTO);
     anomaliesPriorityNumber.put("GLOBAL", priorityMap);


     }
     */

    public Map<String, PriorityTicketDTO> sortTicketsByDomaine(String filtre) {
        String nomProjetComplet = null;
        Map<String, PriorityTicketDTO> mapDomainePriorityTicketDTO = new LinkedHashMap<String, PriorityTicketDTO>();
        //for (TicketCustom ticket : globalTicketList) {
        Ticket ticket = null;
        Map<String, Object> mapTicketsCustomIncompleteAux = null;

        try {
            // Le null pointer exception a été corrigé en ajoutant le contrôle suivant: (La map ticketMap est nulle, DataBaseRequests.java l 409)
            for (Integer numAnomalie : mapTicketsCustomIncomplete.keySet()) {

                StringBuilder domaineOuProjet = new StringBuilder();
                StringBuilder priority = new StringBuilder();
                PriorityTicketDTO priorityTicketAux = null;
                //StringBuilderTools.clearStringBuilder(domaine);
                //StringBuilderTools.clearStringBuilder(priority);
                mapTicketsCustomIncompleteAux = (Map<String, Object>) mapTicketsCustomIncomplete.get(numAnomalie);

                ticket = (Ticket) mapTicketsCustomIncompleteAux.get("Ticket");

                if (filtre.equals("projet") || filtre.equals("circuitProjet")) {
                    domaineOuProjet.append("PROJET ");
                    nomProjetComplet = Configuration.getAbreviationProjetParNiveauProjet(mapTicketsCustomIncompleteAux.get("projet").toString());
                    if (filtre.equals("projet")) {
                        //CR
                        //Tools.showConsolLog(mapTicketsCustomIncompleteAux.get("projet").toString());
                        if (Configuration.projetsActifsCircuitReleaseList.contains(nomProjetComplet)) {
                            domaineOuProjet.append(mapTicketsCustomIncompleteAux.get("projet"));
                        } else {
                            domaineOuProjet.append("<span class='rouge'>");
                            domaineOuProjet.append(mapTicketsCustomIncompleteAux.get("projet"));
                            domaineOuProjet.append("</span>");
                        }
                    } else {
                        //CP                    
                        if (Configuration.projetsActifsCircuitProjetList.contains(nomProjetComplet)) {
                            domaineOuProjet.append(mapTicketsCustomIncompleteAux.get("projet"));
                        } else {
                            domaineOuProjet.append("<span class='rouge'>");
                            domaineOuProjet.append(mapTicketsCustomIncompleteAux.get("projet"));
                            domaineOuProjet.append("</span>");
                        }
                    }



                } else if (filtre.equals("domaine") || filtre.equals("maintenance")) {
                    domaineOuProjet.append(ticket.getMilestone());
                }

                priority.append(ticket.getPriority());
                //Tools.showConsolLog(domaine+" / "+priority);
                List<Ticket> listeTickets = null;
                if (mapDomainePriorityTicketDTO.containsKey(domaineOuProjet.toString())) {
                    priorityTicketAux = mapDomainePriorityTicketDTO.get(domaineOuProjet.toString());
                    if (priorityTicketAux.getMapDomainePriorityTicketDTO().containsKey(priority.toString())) {
                        priorityTicketAux.getMapDomainePriorityTicketDTO().get(priority.toString()).add(ticket);
                    } else {
                        listeTickets = new ArrayList<Ticket>();
                        listeTickets.add(ticket);
                        priorityTicketAux.getMapDomainePriorityTicketDTO().put(priority.toString(), listeTickets);
                    }
                } else {
                    priorityTicketAux = new PriorityTicketDTO();
                    mapDomainePriorityTicketDTO.put(domaineOuProjet.toString(), priorityTicketAux);
                    priorityTicketAux.setMapDomainePriorityTicketDTO(new LinkedHashMap<String, List<Ticket>>());
                    listeTickets = new ArrayList<Ticket>();
                    listeTickets.add(ticket);
                    priorityTicketAux.getMapDomainePriorityTicketDTO().put(priority.toString(), listeTickets);

                }
            }
        } catch (Exception exep) {
            String msgException = "";
            if (ticket != null) {
                msgException += "\n\nanomalie: " + ticket.getId();
            } else {
                msgException += "\n\nticket : NULLLLL";
            }

            if (ticket != null) {
                msgException += "\n\nanomalieXX: " + ticket.getId();
            } else {
                msgException += "\n\nanomalieXX : NULLLLL";
            }

            if (ticket.getPriority() != null) {
                msgException += "\n\npriority: " + ticket.getPriority();
            } else {
                msgException += "\n\npriority : NULLLLL";
            }


            exep.printStackTrace();
            tools.Tools.traiterException(msgException + "\n\n" + tools.Tools.getStackTrace(exep));
        }
        return mapDomainePriorityTicketDTO;
    }

    public String preparationDiagrammePieGenerique(String filtre, int... iterations) {
        String jsonText = null;
        List<TicketCustom> globalTickList = null;
        try {
            Configuration.initialisation();
            numReleaseEnCours = Configuration.parametresList.get("releaseEnCours");
            String circuit = null;
            String namedQuery = null;
            if (filtre.equals("circuitProjet")) {
                circuit = "projet";
                namedQuery = "TicketCustom.findAllCircuitProjet";
            } else {
                circuit = "release";
                namedQuery = "TicketCustom.findByRelease";
            }
            try {
                globalTickList = AppelRequetes.getTicketCustomListOfNamedQuery(Configuration.puAnomalies, Configuration.tracAnomalies, namedQuery);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            String[] cles = new String[]{"t_liv", "projet"};
            Set<Integer> setIdTicketAnomalies = new TreeSet<Integer>();

            for (TicketCustom ticketCustomAnomalies : globalTickList) {
                if (!ticketCustomAnomalies.getTicketPointer().getOwner().equals("release.comite")) {
                    setIdTicketAnomalies.add(ticketCustomAnomalies.getTicket());
                }
            }
            List<Integer> listeIdTicketAnomalies = new ArrayList<Integer>(setIdTicketAnomalies);
            Tools.showConsolLog("nbr d'objets: " + listeIdTicketAnomalies.size());

            if (listeIdTicketAnomalies.size() > 0) {

                mapTicketsCustomIncomplete = AppelRequetes.getTicketCustomByTicketIdAndNames(listeIdTicketAnomalies, Configuration.puAnomalies, Configuration.tracAnomalies, cles);

                if (filtre.equals("maintenance")) {

                    Iterator<Map.Entry<Integer, Map<String, Object>>> iter = mapTicketsCustomIncomplete.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry<Integer, Map<String, Object>> entry = iter.next();
                        if (!entry.getValue().get("projet").toString().equals("MAINTENANCE")) {
                            iter.remove();
                        }
                    }
                }

                servlets.AfficherMessageEtatAvancement.setLogmessage("Analyse de tous les tickets Anomalies de la Release " + numReleaseEnCours, Tools.getConnectedLogin());
                //Map de tous les tickets
                Map<String, Integer> priorityMap = triAnomaliesPriority(filtre);
                //Les Maps distribués par nom de domaine
                Map<String, PriorityTicketDTO> mapDomainePriorityTicketDTO = sortTicketsByDomaine(filtre);
                Map<String, Map<String, Integer>> anomaliesPriorityNumber = getAnomaliesPriorityNumber(mapDomainePriorityTicketDTO, filtre);
                anomaliesPriorityNumber.put("GLOBAL", priorityMap);
                jsonText = convertHashMapToJsonObject(anomaliesPriorityNumber, mapDomainePriorityTicketDTO, filtre);
            } else {
                jsonText = "AUCUN_TICKET_A_TRAITER";
            }
        } catch (Exception exep) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex1) {
                ex1.printStackTrace();
            }
            if (iterations.length == 0) {
                return preparationDiagrammePieGenerique(filtre, 0);
            } else if (iterations[0] < 10) {
                return preparationDiagrammePieGenerique(filtre, (iterations[0] + 1));
            } else {
                exep.printStackTrace();
                tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
            }
        }
        return jsonText;
    }

    private Map<String, Map<String, Integer>> getAnomaliesPriorityNumber(Map<String, PriorityTicketDTO> mapDomainePriorityTicketDTO, String filtre) {
        Map<String, Map<String, Integer>> domainsPriorityMap = new LinkedHashMap<String, Map<String, Integer>>();
        try {
            for (String nomDomaine : mapDomainePriorityTicketDTO.keySet()) {
                //Map<String, Integer> priorityMap = new HashMap<String, Integer>();
                Map<String, Integer> priorityMap = new LinkedHashMap<String, Integer>();

                Iterator it = Configuration.priorityList.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pairs = (Map.Entry) it.next();
                    priorityMap.put(pairs.getKey().toString(), (Integer) pairs.getValue());
                }
                //Tools.showConsolLog("****** " + nomDomaine + " ******");
                for (String nomPriority : mapDomainePriorityTicketDTO.get(nomDomaine).getMapDomainePriorityTicketDTO().keySet()) {
                    //Tools.showConsolLog("+++ " + nomPriority + " +++");
                    priorityMap.put(nomPriority, mapDomainePriorityTicketDTO.get(nomDomaine).getMapDomainePriorityTicketDTO().get(nomPriority).size());
                }
                domainsPriorityMap.put(nomDomaine, priorityMap);
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return domainsPriorityMap;
    }

    private static Map<String, Integer> triAnomaliesPriority(String filtre) {
        Map<String, Integer> priorityMap = new LinkedHashMap<String, Integer>();
        try {
            Iterator it = Configuration.priorityList.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pairs = (Map.Entry) it.next();
                priorityMap.put(pairs.getKey().toString(), (Integer) pairs.getValue());
            }


            String priority = "";
            int nombreOccurences = 0;
            Map<String, Object> ticketCustomDetailsAux;
            Ticket ticketAnomalies;
            for (Integer numTicketAnomalie : mapTicketsCustomIncomplete.keySet()) {
                ticketCustomDetailsAux = mapTicketsCustomIncomplete.get(numTicketAnomalie);
                ticketAnomalies = (Ticket) (ticketCustomDetailsAux.get("Ticket"));
                if (!ticketAnomalies.getOwner().equals("release.comite")) {
                    priority = ticketAnomalies.getPriority();
                    if (filtre.equals("maintenance")) {
                        if (ticketCustomDetailsAux.get("projet").toString().equals("MAINTENANCE")) {
                            if (priorityMap.containsKey(priority)) {
                                priorityMap.put(priority, priorityMap.get(priority) + 1);
                            } else {
                                priorityMap.put(priority, 1);
                            }
                        }
                    } else {
                        if (priorityMap.containsKey(priority)) {
                            priorityMap.put(priority, priorityMap.get(priority) + 1);
                        } else {
                            priorityMap.put(priority, 1);
                        }
                    }
                }
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return priorityMap;
    }

    private static String getContenuTableHTML(String nomDomaine, Map<String, PriorityTicketDTO> mapDomainePriorityTicketDTO, String filtre) {
        StringBuilder contenuTableHTML = new StringBuilder();
        Map<String, Object> ticketCustomDetails = null;
        Integer numTicketAnomalie = null;
        try {
            PriorityTicketDTO priorityTicketDTO = mapDomainePriorityTicketDTO.get(nomDomaine);
            //Préparation des tickets et des TicketsCustom
            //(numTick:(tick:TicketAnomalie,propTickCustom i: valTickCustom i))
            StringBuilder tickLivraison;
            for (String priorityAux : Configuration.priorityList.keySet()) {
                for (String priority : priorityTicketDTO.getMapDomainePriorityTicketDTO().keySet()) {
                    if (priorityAux.equals(priority)) {
                        contenuTableHTML.append("<div class='titrePriority'><span class='titreSpan'>Priorité: </span><span class='valeurSpan'>" + priority + "</span></div>");
                        contenuTableHTML.append("<table class='tableTicketsSummary'>");
                        contenuTableHTML.append("<thead>");
                        contenuTableHTML.append("<tr class='tableTicketsSummaryTr'>");
                        contenuTableHTML.append("<th class='tableTicketsSummaryTh width1'>Livraison</th>");
                        contenuTableHTML.append("<th class='tableTicketsSummaryTh width1'>Anomalie</th>");
                        contenuTableHTML.append("<th class='tableTicketsSummaryTh width6'>Summary</th>");
                        contenuTableHTML.append("<th class='tableTicketsSummaryTh width2'>Reporter</th>");
                        contenuTableHTML.append("<th class='tableTicketsSummaryTh width2'>Owner</th>");
                        if (filtre.equals("projet") || filtre.equals("circuitProjet")) {
                            contenuTableHTML.append("<th class='tableTicketsSummaryTh width2'>Domaine</th>");
                        } else if (filtre.equals("domaine") || filtre.equals("maintenance")) {
                            contenuTableHTML.append("<th class='tableTicketsSummaryTh width2'>Niveau Projet</th>");
                        }
                        contenuTableHTML.append("<th class='tableTicketsSummaryTh width2'>Priority</th>");
                        contenuTableHTML.append("</tr>");
                        contenuTableHTML.append("</thead>");
                        contenuTableHTML.append("<tbody>");
                        List<Ticket> listeTicketsCustomAnomalies = priorityTicketDTO.getMapDomainePriorityTicketDTO().get(priority);
                        //Map<String, Map<String, Object>> ticketDetailsMap = traiterTicketCustomAnomalies(listeTicketsCustomAnomalies);
                        //for (String numTicketAnomalie : ticketDetailsMap.keySet()) {
                        Ticket ticketAnomalies;
                        for (Ticket tick : listeTicketsCustomAnomalies) {
                            numTicketAnomalie = tick.getId();
                            ticketCustomDetails = mapTicketsCustomIncomplete.get(numTicketAnomalie);
                            ticketAnomalies = (Ticket) (ticketCustomDetails.get("Ticket"));

                            //Traitement Ticket de livraison

                            String tickLivraisonLien = "";
                            Object tickLivraisonObject = ticketCustomDetails.get("t_liv");
                            if (tickLivraisonObject != null) {
                                String[] tabString = ((String) ticketCustomDetails.get("t_liv")).split("-");
                                if (tabString.length > 0) {
                                    tickLivraison = new StringBuilder(tabString[tabString.length - 1]);
                                } else {
                                    tickLivraison = new StringBuilder("");
                                }
                            } else {
                                tickLivraison = new StringBuilder("");
                            }
                            if (tickLivraison.length() > 0) {
                                tickLivraisonLien = "#" + tickLivraison;
                            } else {
                                tickLivraisonLien = "";
                            }
                            contenuTableHTML.append("<tr class='tableTicketsSummaryTr'>");
                            contenuTableHTML.append("<td class='tableTicketsSummaryTd width1'><a href='http://172.28.70.74/trac/livraisons_t24/ticket/" + tickLivraison.toString() + "'  target='_blank'>" + tickLivraisonLien + "</a></th>");
                            contenuTableHTML.append("<td class='tableTicketsSummaryTd width1'><a href='http://172.28.70.74/trac/anomalies_t24/ticket/" + numTicketAnomalie + "'  target='_blank'>#" + numTicketAnomalie + "</a></th>");
                            contenuTableHTML.append("<td class='tableTicketsSummaryTd width6'>" + ticketAnomalies.getSummary() + "</th>");
                            contenuTableHTML.append("<td class='tableTicketsSummaryTd width2'><div class='coupe'>" + ticketAnomalies.getReporter() + "</div></th>");
                            contenuTableHTML.append("<td class='tableTicketsSummaryTd width1'><div class='coupe'>" + ticketAnomalies.getOwner() + "</div></th>");

                            if (filtre.equals("projet") || filtre.equals("circuitProjet")) {
                                contenuTableHTML.append("<td class='tableTicketsSummaryTd width2'>" + ticketAnomalies.getMilestone().replace("DOMAINE", "").trim() + "</th>");
                            } else if (filtre.equals("domaine") || filtre.equals("maintenance")) {
                                contenuTableHTML.append("<td class='tableTicketsSummaryTd width2'>" + (String) ticketCustomDetails.get("projet") + "</th>");
                            }
                            contenuTableHTML.append("<td class='tableTicketsSummaryTd width2'>" + ticketAnomalies.getPriority() + "</th>");
                            contenuTableHTML.append("</tr>");
                        }
                        contenuTableHTML.append("</tbody>");
                        contenuTableHTML.append("</table>");
                    }
                }
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            String msg = "numTicketAnomalie:::" + numTicketAnomalie;
            if (ticketCustomDetails != null) {
                msg += "\n\nticketCustomDetails:::" + ticketCustomDetails.toString();
            } else {
                msg += "\n\nticketCustomDetails::: NULLLLLLLLL";
            }
            msg += "\n\n" + tools.Tools.getStackTrace(exep);
            tools.Tools.traiterException(msg);
        }
        return contenuTableHTML.toString();
    }

    private static String convertHashMapToJsonObject(Map<String, Map<String, Integer>> globalTicketsMap, Map<String, PriorityTicketDTO> mapDomainePriorityTicketDTO, String filtre) {
        List<Object> globalLst = new ArrayList<Object>();
        try {
            String titreAccordionButton = "";
            String titreDiagramme = "";
            String contenuTableHTML = "";
            for (String nomDomaine : globalTicketsMap.keySet()) {
                Map<String, Integer> priorityMap = globalTicketsMap.get(nomDomaine);
                if (nomDomaine.equals("GLOBAL")) {
                    if (filtre.equals("circuitProjet")) {
                        titreDiagramme = "Diagramme: Etat global du circuit projet";
                        titreAccordionButton = "ETAT GLOBAL DU CIRCUIT PROJET";
                    } else {
                        titreDiagramme = "Diagramme: Etat global de la Release " + numReleaseEnCours;
                        titreAccordionButton = "ETAT GLOBAL DE LA RELEASE " + numReleaseEnCours;
                    }
                } else {
                    titreDiagramme = "Diagramme: Etat des tickets du " + nomDomaine;
                    titreAccordionButton = nomDomaine;
                }
                List<Map<String, Object>> listOfHashMap = new ArrayList<Map<String, Object>>();
                Iterator iter1 = priorityMap.entrySet().iterator();
                while (iter1.hasNext()) {
                    Map.Entry ent = (Map.Entry) iter1.next();
                    Map<String, Object> mapElmnt = new HashMap<String, Object>();
                    mapElmnt.put("data", ent.getValue());
                    mapElmnt.put("label", ent.getKey());
                    listOfHashMap.add(mapElmnt);
                }
                Map<String, Object> mapCompletAvecDescription = new HashMap<String, Object>();
                mapCompletAvecDescription.put("titreAccordionButton", titreAccordionButton);
                mapCompletAvecDescription.put("titreDiagramme", titreDiagramme);
                mapCompletAvecDescription.put("dataDiagramme", listOfHashMap);

                if (nomDomaine.equals("GLOBAL")) {
                    globalLst.add(0, mapCompletAvecDescription);
                } else {
                    //get Table HTML
                    contenuTableHTML = getContenuTableHTML(nomDomaine, mapDomainePriorityTicketDTO, filtre);
                    //fin
                    mapCompletAvecDescription.put("contenuTableHTML", contenuTableHTML);
                    globalLst.add(mapCompletAvecDescription);
                }
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return Tools.objectToJsonString(globalLst);
    }
}
