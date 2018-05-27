/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package newTrac;

import dto.RepresentationListTicketDTO;
import entitiesTrac.TicketCustom;
import java.util.*;
import tools.Configuration;
import tools.Tools;

/**
 *
 * @author 04486
 */
public class DiagramsPieToolsNewTrac {

    static Map<Integer, Map<String, Object>> mapTicketsCustomComplete = new LinkedHashMap<Integer, Map<String, Object>>();

    public String preparationDiagrammePieGenerique(String persistenceUnit, String circuit, String critereTri, String projetTrac, int... iterations) {
        String jsonText = null;
        List<String> clesList = new ArrayList<String>();
        boolean traitementGenerique = true;
        if (persistenceUnit.equals("dbGestionIncidentsPU")) {
            clesList.add("type_propagation");
            traitementGenerique = false;
        } else {
            clesList.add("nature_traitement");
        }
        String[] cles = new String[clesList.size()];
        clesList.toArray(cles);
        String namedQuery = "TicketCustom.findAllTicketsCustomOfOpenTickets";
        List<TicketCustom> globalTickList = null;
        ////mapTicketsCustomComplete

        try {
            Configuration.initialisation();
            mapTicketsCustomComplete = ToolsNewTrac.executeQueryThenAnalyseTickets(persistenceUnit, projetTrac, namedQuery, cles, null);
            if (mapTicketsCustomComplete.size() > 0) {
                if (traitementGenerique) {
                    //la map contient tous les tickets dont le status != closed donc supprimer le déchet
                    //CHF : HF   ou   AAC
                    //CR  : !HF  et  !AAC
                    Iterator<Map.Entry<Integer, Map<String, Object>>> iter = mapTicketsCustomComplete.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry<Integer, Map<String, Object>> entry = iter.next();
                        if (entry.getValue().containsKey("nature_traitement") && entry.getValue().get("nature_traitement") != null) {
                            String natureTraitement = entry.getValue().get("nature_traitement").toString();
                            if (circuit.equals("HOTFIX")) {
                                if (!natureTraitement.equals("HOTFIX") && !natureTraitement.equals("ACTION A CHAUD")) {
                                    iter.remove();
                                }
                            } else {
                                if (natureTraitement.equals("HOTFIX") || natureTraitement.equals("ACTION A CHAUD")) {
                                    iter.remove();
                                }
                            }
                        }
                    }
                }
                servlets.AfficherMessageEtatAvancement.setLogmessage("Analyse de tous les tickets ouverts", Tools.getConnectedLogin());
                //Map de tous les tickets
                //calculer le nombre de chaque statut
                Map<String, Integer> nbrSeloncriterMap = calculerNbrTicketsGlobalSelonStatut();
                //Les Maps distribués par critere de tri
                Map<String, Map<String, RepresentationListTicketDTO>> mapCritereTicket = triSelonCritereDeTri(critereTri);
                Map<String, Map<String, Integer>> nbrStatusParCritere = getTicketsStatusNumber(mapCritereTicket);
                nbrStatusParCritere.put("GLOBAL", nbrSeloncriterMap);
                jsonText = convertHashMapToJsonObject(nbrStatusParCritere, mapCritereTicket, critereTri, projetTrac, cles);
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
                return preparationDiagrammePieGenerique(persistenceUnit, circuit, critereTri, projetTrac, 0);
            } else if (iterations[0] < 10) {
                return preparationDiagrammePieGenerique(persistenceUnit, circuit, critereTri, projetTrac, (iterations[0] + 1));
            } else {
                exep.printStackTrace();
                tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
            }
        }
        return jsonText;
    }

    private static Map<String, Integer> calculerNbrTicketsGlobalSelonStatut() {
        Map<String, Integer> orderMapAux = new LinkedHashMap<String, Integer>();
        Map<String, Integer> orderMap = new LinkedHashMap<String, Integer>();
        try {
            int nombreOccurences = 0;
            Map<String, Object> ticketCustomDetailsAux;
            for (Integer numTicketAnomalie : mapTicketsCustomComplete.keySet()) {
                ticketCustomDetailsAux = mapTicketsCustomComplete.get(numTicketAnomalie);
                String statut = ticketCustomDetailsAux.get("status").toString();
                if (orderMapAux.containsKey(statut)) {
                    orderMapAux.put(statut, orderMapAux.get(statut) + 1);
                } else {
                    orderMapAux.put(statut, 1);
                }
            }
            //tri des status pour l'affichage
            for (String status : Configuration.statusSet) {
                if (orderMapAux.containsKey(status)) {
                    orderMap.put(status, orderMapAux.get(status));
                } else {
                    orderMap.put(status, 0);
                }
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return orderMap;
    }

    public Map<String, Map<String, RepresentationListTicketDTO>> triSelonCritereDeTri(String critereTri) {
        String nomProjetComplet = null;
        //Map<String, Map<String, KeyValueMapTicketsCustomDTO>> : Map<critereTri, Map<status, List<clé,valeur> > >
        Map<String, Map<String, RepresentationListTicketDTO>> mapCritereTicket = new LinkedHashMap<>();
        TicketCustom ticket = null;

        try {
            for (Integer numAnomalie : mapTicketsCustomComplete.keySet()) {
                Map<String, Object> representationTicket = (Map<String, Object>) mapTicketsCustomComplete.get(numAnomalie);
                RepresentationListTicketDTO listRepresentationTicketDTO = null;
                Map<String, RepresentationListTicketDTO> mapRepresentationListTicketDTO = null;
                List<Map<String, Object>> listRepresentationTicket = null;
                String status = representationTicket.get("status").toString();
                String elementCritereTri = representationTicket.get(critereTri).toString();
                if (mapCritereTicket.containsKey(elementCritereTri)) {
                    mapRepresentationListTicketDTO = mapCritereTicket.get(elementCritereTri);
                    if (mapRepresentationListTicketDTO.containsKey(status)) {
                        mapCritereTicket.get(elementCritereTri).get(status).getListTickets().add(representationTicket);
                    } else {
                        listRepresentationTicket = new ArrayList<Map<String, Object>>();
                        listRepresentationTicket.add(representationTicket);
                        listRepresentationTicketDTO = new RepresentationListTicketDTO(listRepresentationTicket);
                        mapRepresentationListTicketDTO.put(status, listRepresentationTicketDTO);
                        mapCritereTicket.put(elementCritereTri, mapRepresentationListTicketDTO);
                    }
                } else {
                    //chaque ticket est représenté par une map
                    listRepresentationTicket = new ArrayList<Map<String, Object>>();
                    listRepresentationTicket.add(representationTicket);
                    listRepresentationTicketDTO = new RepresentationListTicketDTO(listRepresentationTicket);
                    mapRepresentationListTicketDTO = new LinkedHashMap<String, RepresentationListTicketDTO>();
                    mapRepresentationListTicketDTO.put(status, listRepresentationTicketDTO);
                    mapCritereTicket.put(elementCritereTri, mapRepresentationListTicketDTO);
                }
            }
        } catch (Exception exep) {
            String msgException = "";
            if (ticket != null) {
                msgException += "\n\nanomalie: " + ticket.getTicket();
            } else {
                msgException += "\n\nticket : NULLLLL";
            }

            if (ticket.getTicketPointer() != null) {
                msgException += "\n\nanomalieXX: " + ticket.getTicketPointer().getId();
            } else {
                msgException += "\n\nanomalieXX : NULLLLL";
            }

            if (ticket.getTicketPointer().getPriority() != null) {
                msgException += "\n\npriority: " + ticket.getTicketPointer().getPriority();
            } else {
                msgException += "\n\npriority : NULLLLL";
            }


            exep.printStackTrace();
            tools.Tools.traiterException(msgException + "\n\n" + tools.Tools.getStackTrace(exep));
        }
        return mapCritereTicket;
    }

    private Map<String, Map<String, Integer>> getTicketsStatusNumber(Map<String, Map<String, RepresentationListTicketDTO>> mapCritereTicket) {
        //c'est destiné pour les diagrammes en pie
        //ça permet de calculer pour chaque diagramme le nbr de différents statuts
        //Map<String, Map<String, Integer>> diagrammePieNbrMap : Map<critereTri, Map<status, nbrTickets>>
        Map<String, Map<String, Integer>> nbrStatusParCritere = new LinkedHashMap<String, Map<String, Integer>>();
        Map<String, Integer> globalMap = new LinkedHashMap<String, Integer>();
        try {
            for (String critereTri : mapCritereTicket.keySet()) {
                Map<String, Integer> auxMap = new LinkedHashMap<String, Integer>();
                /*
                 for (String status : Configuration.statusList) {
                 auxMap.put(status, 0);
                 }
                 for (String status : mapCritereTicket.get(critereTri).keySet()) {
                 //Tools.showConsolLog("+++ " + nomPriority + " +++");
                 auxMap.put(status, mapCritereTicket.get(critereTri).get(status).getListTickets().size());
                 }*/
                for (String status : Configuration.statusSet) {
                    if (mapCritereTicket.get(critereTri).containsKey(status)) {
                        auxMap.put(status, mapCritereTicket.get(critereTri).get(status).getListTickets().size());
                    } else {
                        auxMap.put(status, 0);
                    }
                }
                nbrStatusParCritere.put(critereTri, auxMap);
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return nbrStatusParCritere;
    }

    private String convertHashMapToJsonObject(Map<String, Map<String, Integer>> nbrStatusParCritere, Map<String, Map<String, RepresentationListTicketDTO>> mapCritereTicket, String critereTri, String projetTrac, String[] cles) {
        List<Object> globalLst = new ArrayList<Object>();
        try {
            String titreAccordionButton = "";
            String titreDiagramme = "";
            String contenuTableHTML = "";
            for (String elementCritereTri : nbrStatusParCritere.keySet()) {
                Map<String, Integer> nbrStatusMap = nbrStatusParCritere.get(elementCritereTri);
                if (elementCritereTri.equals("GLOBAL")) {
                    titreDiagramme = "Diagramme: Etat global des tickets";
                    titreAccordionButton = "ETAT GLOBAL DES TICKETS";
                } else {
                    titreDiagramme = "Diagramme: Etat des tickets (" + critereTri + ": " + elementCritereTri + ")";
                    titreAccordionButton = elementCritereTri.toUpperCase();
                }
                List<Map<String, Object>> listOfHashMap = new ArrayList<Map<String, Object>>();
                Iterator iter1 = nbrStatusMap.entrySet().iterator();
                while (iter1.hasNext()) {
                    Map.Entry ent = (Map.Entry) iter1.next();
                    Map<String, Object> mapElmnt = new LinkedHashMap<String, Object>();
                    mapElmnt.put("data", ent.getValue());
                    String key = ent.getKey().toString();
                    if (key.length() > 25) {
                        key = key.substring(0, 23).concat("...");
                    }

                    mapElmnt.put("label", key);
                    listOfHashMap.add(mapElmnt);
                }
                Map<String, Object> mapCompletAvecDescription = new LinkedHashMap<String, Object>();
                mapCompletAvecDescription.put("titreAccordionButton", titreAccordionButton);
                mapCompletAvecDescription.put("titreDiagramme", titreDiagramme);
                mapCompletAvecDescription.put("dataDiagramme", listOfHashMap);

                if (elementCritereTri.equals("GLOBAL")) {
                    globalLst.add(0, mapCompletAvecDescription);
                } else {
                    //get Table HTML
                    contenuTableHTML = getContenuTableHTML(mapCritereTicket.get(elementCritereTri), critereTri, projetTrac, cles);
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

    public String genererTableT24Html(List<Map<String, Object>> ListTickets, String[] cles, String projetTracLivraison, String projetTracAnomalie) {
        List<String> clesList = Arrays.asList(cles);
        StringBuilder contenuTableHTML = new StringBuilder();
        contenuTableHTML.append("<table class='tableTicketsSummary'>");
        contenuTableHTML.append("<thead>");
        contenuTableHTML.append("<tr class='tableTicketsSummaryTr'>");
        contenuTableHTML.append("<th class='tableTicketsSummaryTh width1'>Livraison</th>");
        contenuTableHTML.append("<th class='tableTicketsSummaryTh width1'>Anomalie</th>");
        for (String cle : cles) {
            String classWidth;
            if (cle.equals("parents")) {
                classWidth = "width1";
            } else {
                classWidth = "width3";
            }
            contenuTableHTML.append("<th class='tableTicketsSummaryTh " + classWidth + "'><div class='coupe'>" + cle + "</div></td>");
        }

        contenuTableHTML.append("<th class='tableTicketsSummaryTh width3'>Compostant</th>");
        contenuTableHTML.append("<th class='tableTicketsSummaryTh width3'>Owner</th>");
        contenuTableHTML.append("<th class='tableTicketsSummaryTh width3'>Reporter</th>");
        contenuTableHTML.append("<th class='tableTicketsSummaryTh width3'>Jalon</th>");
        contenuTableHTML.append("<th class='tableTicketsSummaryTh width3'>Summary</th>");
        contenuTableHTML.append("</tr>");
        contenuTableHTML.append("</thead>");
        contenuTableHTML.append("<tbody>");


        for (Map<String, Object> tickMap : ListTickets) {
            String livraison = "";
            String anomalie = "";
            String id = Tools.traiterChamp(tickMap, "id");
            if (tickMap.containsKey("t_liv")) {
                anomalie = id;
                livraison = Tools.reformTlivTicketOrigine("t_liv", Tools.traiterChamp(tickMap, "t_liv"));
            } else if (tickMap.containsKey("ticket_origine")) {
                livraison = id;
                anomalie = Tools.reformTlivTicketOrigine("ticket_origine", Tools.traiterChamp(tickMap, "ticket_origine"));
            }
            String component = Tools.traiterChamp(tickMap, "component");
            String owner = Tools.traiterChamp(tickMap, "owner");
            String reporter = Tools.traiterChamp(tickMap, "reporter");
            String milestone = Tools.traiterChamp(tickMap, "milestone");
            String summary = Tools.traiterChamp(tickMap, "summary");
            contenuTableHTML.append("<tr class='tableTicketsSummaryTr'>");
            if (livraison.trim().length() > 0) {
                contenuTableHTML.append("<td class='tableTicketsSummaryTd width1'><a href='http://172.28.70.74/trac/").append(projetTracLivraison).append("/ticket/").append(livraison).append("'  target='_blank'>#").append(livraison).append("</a></td>");
            } else {
                contenuTableHTML.append("<td class='tableTicketsSummaryTd width1'></td>");
            }
            if (anomalie.trim().length() > 0) {
                contenuTableHTML.append("<td class='tableTicketsSummaryTd width1'><a href='http://172.28.70.74/trac/").append(projetTracAnomalie).append("/ticket/").append(anomalie).append("'  target='_blank'>#").append(anomalie).append("</a></td>");
            } else {
                contenuTableHTML.append("<td class='tableTicketsSummaryTd width1'></td>");
            }

            for (String cle : cles) {
                String valeur = Tools.traiterChamp(tickMap, cle);
                if (cle.equals("parents") && valeur.length() > 0) {
                    contenuTableHTML.append("<td class='tableTicketsSummaryTd width1'><a href='http://172.28.70.74/trac/" + projetTracAnomalie + "/ticket/" + valeur + "'  target='_blank'>#" + valeur + "</a></td>");
                } else {
                    String classWidth;
                    if (cle.equals("parents")) {
                        classWidth = "width1";
                    } else {
                        classWidth = "width3";
                    }
                    contenuTableHTML.append("<td class='tableTicketsSummaryTd " + classWidth + "'><div class='coupe'>" + valeur + "</div></td>");
                }
            }

            contenuTableHTML.append("<td class='tableTicketsSummaryTd width3'><div class='coupe'>").append(component).append("</div></td>");

            contenuTableHTML.append("<td class='tableTicketsSummaryTd width3'><div class='coupe'>").append(owner).append("</div></td>");
            contenuTableHTML.append("<td class='tableTicketsSummaryTd width3'><div class='coupe'>").append(reporter).append("</div></td>");
            contenuTableHTML.append("<td class='tableTicketsSummaryTd width3'><div class='coupe'>").append(milestone).append("</div></td>");

            contenuTableHTML.append("<td class='tableTicketsSummaryTd width3'>");
            contenuTableHTML.append("<a class='conteneur_info_bull'>");
            contenuTableHTML.append("<img class='info-icon' src='images/info.png' alt='info'>");
            contenuTableHTML.append("<span style='margin-left:-200px;'>");
            contenuTableHTML.append(summary);
            contenuTableHTML.append("</span>");
            contenuTableHTML.append("</a>");
            contenuTableHTML.append("</td>");
            contenuTableHTML.append("</tr>");
        }
        contenuTableHTML.append("</tbody>");
        contenuTableHTML.append("</table>");

        return contenuTableHTML.toString();
    }

    public String genererTableHtml(List<Map<String, Object>> ListTickets, String[] cles, String projetTrac, String... adrIP) {
        List<String> clesList = Arrays.asList(cles);
        StringBuilder contenuTableHTML = new StringBuilder();
        String adresseIP = null;
        if (adrIP.length > 0) {
            adresseIP = adrIP[0];
        } else {
            adresseIP = "172.28.70.74";
        }
        contenuTableHTML.append("<table class='tableTicketsSummary'>");
        contenuTableHTML.append("<thead>");
        contenuTableHTML.append("<tr class='tableTicketsSummaryTr'>");
        contenuTableHTML.append("<th class='tableTicketsSummaryTh width1'>Livraison</th>");
        for (String cle : cles) {
            String classWidth;
            if (cle.equals("parents")) {
                classWidth = "width1";
            } else {
                classWidth = "width3";
            }
            contenuTableHTML.append("<th class='tableTicketsSummaryTh " + classWidth + "'><div class='coupe'>" + cle + "</div></td>");
        }
        contenuTableHTML.append("<th class='tableTicketsSummaryTh width3'>Compostant</th>");
        contenuTableHTML.append("<th class='tableTicketsSummaryTh width3'>Owner</th>");
        contenuTableHTML.append("<th class='tableTicketsSummaryTh width3'>Reporter</th>");
        contenuTableHTML.append("<th class='tableTicketsSummaryTh width3'>Jalon</th>");
        contenuTableHTML.append("<th class='tableTicketsSummaryTh width3'>Summary</th>");
        contenuTableHTML.append("</tr>");
        contenuTableHTML.append("</thead>");
        contenuTableHTML.append("<tbody>");


        for (Map<String, Object> tickMap : ListTickets) {
            String id = Tools.traiterChamp(tickMap, "id");
            String component = Tools.traiterChamp(tickMap, "component");
            String owner = Tools.traiterChamp(tickMap, "owner");
            String reporter = Tools.traiterChamp(tickMap, "reporter");
            String milestone = Tools.traiterChamp(tickMap, "milestone");
            String summary = Tools.traiterChamp(tickMap, "summary");

            contenuTableHTML.append("<tr class='tableTicketsSummaryTr'>");
            contenuTableHTML.append("<td class='tableTicketsSummaryTd width1'><a href='http://" + adresseIP + "/trac/" + projetTrac + "/ticket/" + id + "'  target='_blank'>#" + id + "</a></td>");
            for (String cle : cles) {
                String valeur = Tools.traiterChamp(tickMap, cle);
                if (cle.equals("parents") && valeur.length() > 0) {
                    contenuTableHTML.append("<td class='tableTicketsSummaryTd width1'><a href='http://" + adresseIP + "/trac/" + projetTrac + "/ticket/" + valeur + "'  target='_blank'>#" + valeur + "</a></td>");
                } else {
                    String classWidth;
                    if (cle.equals("parents")) {
                        classWidth = "width1";
                    } else {
                        classWidth = "width3";
                    }
                    contenuTableHTML.append("<td class='tableTicketsSummaryTd " + classWidth + "'><div class='coupe'>" + valeur + "</div></td>");
                }
            }
            contenuTableHTML.append("<td class='tableTicketsSummaryTd width3'><div class='coupe'>" + component + "</div></td>");
            contenuTableHTML.append("<td class='tableTicketsSummaryTd width3'><div class='coupe'>" + owner + "</div></td>");
            contenuTableHTML.append("<td class='tableTicketsSummaryTd width3'><div class='coupe'>" + reporter + "</div></td>");
            contenuTableHTML.append("<td class='tableTicketsSummaryTd width3'><div class='coupe'>" + milestone + "</div></td>");
            contenuTableHTML.append("<td class='tableTicketsSummaryTd width3'>");
            contenuTableHTML.append("<a class='conteneur_info_bull'>");
            contenuTableHTML.append("<img class='info-icon' src='images/info.png' alt='info'>");
            contenuTableHTML.append("<span style='margin-left:-200px;'>");
            contenuTableHTML.append(summary);
            contenuTableHTML.append("</span>");
            contenuTableHTML.append("</a>");
            contenuTableHTML.append("</td>");
            contenuTableHTML.append("</tr>");
        }
        contenuTableHTML.append("</tbody>");
        contenuTableHTML.append("</table>");

        return contenuTableHTML.toString();
    }

    private String getContenuTableHTML(Map<String, RepresentationListTicketDTO> mapCritereTicketDuCritereActuel, String critereTri, String projetTrac, String[] cles) {
        StringBuilder contenuTableHTML = new StringBuilder();

        try {
            for (String statusAux : mapCritereTicketDuCritereActuel.keySet()) {
                List<Map<String, Object>> ListTickets = mapCritereTicketDuCritereActuel.get(statusAux).getListTickets();
                contenuTableHTML.append("<div class='titrePriority'><span class='titreSpan'>Statut: </span><span class='valeurSpan'>").append(statusAux).append("</span></div>");
                contenuTableHTML.append(genererTableHtml(ListTickets, cles, projetTrac));
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return contenuTableHTML.toString();
    }
}
