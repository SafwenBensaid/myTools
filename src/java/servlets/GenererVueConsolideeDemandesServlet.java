/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import dataBaseTracRequests.AppelRequetes;
import dataBaseTracRequests.DataBaseTracGenericRequests;
import entitiesTrac.Ticket;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tools.Configuration;

/**
 *
 * @author trac.admin
 */
public class GenererVueConsolideeDemandesServlet extends HttpServlet {

    Map<Integer, Map<String, Object>> ticketsEnCoursGestionDesDemandes;
    StringBuilder htmlPopup = new StringBuilder();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            String param = request.getParameter("param");
            response.getWriter().println(afficherVueConsolideeDemandes(param));
        } finally {
            response.getWriter().close();
        }
    }

    private synchronized String afficherVueConsolideeDemandes(String param) {
        String pu = Configuration.puGestionDesDemandes;
        ticketsEnCoursGestionDesDemandes = chargerTousLesTicketsEnCoursGestionDesIncidents(pu);
        return generateHtmlCode(param).toString();
    }

    public static Map<Integer, Map<String, Object>> chargerTousLesTicketsEnCoursGestionDesIncidents(String pu) {
        String[] customFieldTab = new String[]{"impact_reglementaire", "impact_pnb", "impact_qs_client", "impact_productivite", "impact_risque", "impact_performance", "impact_autres", "contraintesImpact", "metier_concerne", "client_impact", "date_realisation"};
        List<Integer> listDemandesMetier = new DataBaseTracGenericRequests<Integer>().getList_TYPE_OfnamedQuery(pu, "TicketCustom.findAllDemandesMetier", null);
        Map<Integer, Map<String, Object>> demandesMetierDetails = AppelRequetes.getTicketCustomByTicketIdAndNames(listDemandesMetier, pu, Configuration.tracGestionDemandes, customFieldTab);
        return demandesMetierDetails;
    }

    public StringBuilder generateHtmlCode(String param) {
        StringBuilder htmlSb = new StringBuilder();
        htmlSb.append("<table id='tableTicketsHotfix' class='roundCornerTable statistiquesTable'>");
        htmlSb.append("<thead>");
        htmlSb.append("<tr>");
        htmlSb.append("<th class='traitEpaisBas'>");
        htmlSb.append("</th>");
        htmlSb.append("<th colspan='3'class='traitEpaisGauche traitEpaisBas'>");
        htmlSb.append("Métier");
        htmlSb.append("</th>");
        htmlSb.append("<th colspan='3'class='traitEpaisGauche traitEpaisBas'>");
        htmlSb.append("MOA");
        htmlSb.append("</th>");
        htmlSb.append("<th colspan='3'class='traitEpaisGauche traitEpaisBas'>");
        htmlSb.append("MOE");
        htmlSb.append("</th>");
        htmlSb.append("<th colspan='3'class='traitEpaisGauche traitEpaisBas'>");
        htmlSb.append("PMO");
        htmlSb.append("</th>");
        htmlSb.append("</tr>");
        htmlSb.append("</thead>");
        htmlSb.append("<tbody>");
        htmlSb.append("<tr class='titre'>");
        if (param.equals("composant")) {
            htmlSb.append("<td class='traitEpaisBas coin'>Domaine</td>");
        } else if (param.equals("responsable")) {
            htmlSb.append("<td class='traitEpaisBas coin'>Responsable</td>");
        }
        htmlSb.append("<td class='traitEpaisGauche traitEpaisBas'>Bloquant</td>");
        htmlSb.append("<td class='traitEpaisBas'>Majeur</td>");
        htmlSb.append("<td class='traitEpaisBas'>Mineur</td>");
        htmlSb.append("<td class='traitEpaisGauche traitEpaisBas'>Bloquant</td>");
        htmlSb.append("<td class='traitEpaisBas'>Majeur</td>");
        htmlSb.append("<td class='traitEpaisBas'>Mineur</td>");
        htmlSb.append("<td class='traitEpaisGauche traitEpaisBas'>Bloquant</td>");
        htmlSb.append("<td class='traitEpaisBas'>Majeur</td>");
        htmlSb.append("<td class='traitEpaisBas'>Mineur</td>");
        htmlSb.append("<td class='traitEpaisGauche traitEpaisBas'>Bloquant</td>");
        htmlSb.append("<td class='traitEpaisBas'>Majeur</td>");
        htmlSb.append("<td class='traitEpaisBas'>Mineur</td>");
        htmlSb.append("</tr>");
        htmlSb.append(genererStructureHtml(param));
        htmlSb.append("</tbody>");
        htmlSb.append("</table>");
        htmlSb.append(htmlPopup);
        htmlPopup.setLength(0);
        return htmlSb;
    }

    private StringBuilder genererStructureHtml(String param) {
        String milestone, reporter, status, priority, key = "";

        List<String> statusToDisplay = new ArrayList<String>();
        statusToDisplay.add("NOUVEAU_BESOIN");
        statusToDisplay.add("VALIDE_METIER");
        statusToDisplay.add("VALIDE_IMPACT");
        statusToDisplay.add("VALIDE_MOA");
        statusToDisplay.add("VALIDE_MOE");

        List<String> prioritiesToDisplay = new ArrayList<String>();
        prioritiesToDisplay.add("BLOQUANTE");
        prioritiesToDisplay.add("MAJEURE");
        prioritiesToDisplay.add("MINEURE");

        StringBuilder sbResult = new StringBuilder();

        if (param.equals("composant")) {

            Map<String, Map<Integer, Object>> milestoneMap = new HashMap<String, Map<Integer, Object>>();
            Map<String, Map<Integer, Object>> sortedMilestoneMap = new LinkedHashMap<>();


            try {
                if (ticketsEnCoursGestionDesDemandes.size() > 0) {
                    //Construire une map des tickets par Milestone
                    for (Map.Entry<Integer, Map<String, Object>> entryTicket : ticketsEnCoursGestionDesDemandes.entrySet()) {
                        Ticket ticket = (Ticket) entryTicket.getValue().get("Ticket");
                        milestone = ticket.getMilestone();
                        if (milestoneMap.containsKey(milestone)) {
                            milestoneMap.get(milestone).put(ticket.getId(), ticket);
                        } else {
                            Map<Integer, Object> ticketDetailsMap = new HashMap<Integer, Object>();
                            ticketDetailsMap.put(ticket.getId(), ticket);
                            milestoneMap.put(milestone, ticketDetailsMap);
                        }
                    }

                    //Parcourir les tickets par Composants    
                    for (Map.Entry<String, Map<Integer, Object>> entryMilestone : milestoneMap.entrySet()) {
                        sbResult.append("<tr>");
                        sbResult.append("<td class='projectName' title='" + entryMilestone.getKey().toUpperCase() + "'>").append(entryMilestone.getKey().toUpperCase()).append("</td>");

                        String classeTrait;
                        //Initialisation sortedMilestoneMap
                        for (String allowedStates : statusToDisplay) {
                            for (String allowedPriority : prioritiesToDisplay) {
                                Map<Integer, Object> defaultMap = new HashMap<>();
                                defaultMap.put(0, entryMilestone.getKey().toUpperCase());
                                sortedMilestoneMap.put(allowedStates + "#" + allowedPriority, defaultMap);
                            }
                        }

                        //Trier les tickets de ce composant par état avancement et par priorité
                        for (Map.Entry<Integer, Object> entryTicket : entryMilestone.getValue().entrySet()) {
                            status = ((Ticket) entryTicket.getValue()).getStatus();
                            priority = ((Ticket) entryTicket.getValue()).getPriority();
                            if (!status.isEmpty() && prioritiesToDisplay.contains(priority) && statusToDisplay.contains(status)) {
                                key = status + "#" + priority;
                                sortedMilestoneMap.get(key).put(entryTicket.getKey(), entryTicket.getValue());
                            }
                        }

                        //Préparer les colonnes html de ce milestone par couple status prioriy (tr + popup)
                        int compteurCouleur = 0;
                        int compteurId = 0;
                        for (Map.Entry<String, Map<Integer, Object>> entryTicketMap : sortedMilestoneMap.entrySet()) {
                            if (entryTicketMap.getKey().split("#")[1].equals("BLOQUANTE")) {
                                compteurCouleur = 1;
                            }
                            String idCase = entryMilestone.getKey().replaceAll(" ", "").replaceAll("/", "") + "_" + compteurId;
                            if (compteurCouleur == 1) {
                                classeTrait = " traitEpaisGauche";
                            } else {
                                classeTrait = "";
                            }
                            int nbrTickets = entryTicketMap.getValue().size() - 1;
                            String contenuCase;
                            if (nbrTickets == 0) {
                                contenuCase = "0";
                            } else {
                                contenuCase = "<a href='#' id='" + idCase + "' class='nbrTickets'  onclick='openPopup(this);'>";
                                contenuCase += nbrTickets;
                                contenuCase += "</a>";
                                generatePopupHtml(idCase, entryTicketMap.getValue());
                            }
                            sbResult.append("<td class='couleur").append(compteurCouleur).append(classeTrait).append("'>").append(contenuCase).append("</td>");
                            compteurCouleur++;
                            compteurId++;
                        }
                        sbResult.append("</tr>");
                        sortedMilestoneMap.clear();
                    }
                } else {
                    sbResult.append("<tr> <td colspan='10'>  Aucun Besoin Métier à afficher. </td> </tr>");
                }
            } catch (Exception exep) {
                tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
            }
        }
        return sbResult;
    }

    private void generatePopupHtml(String idPopup, Map<Integer, Object> ticketsMap) {
        htmlPopup.append("<div class='").append(idPopup).append("'  style='display: none'>");
        htmlPopup.append("<p class='titrePopup'>Domaine: ").append(ticketsMap.get(0).toString()).append("</p>");
        htmlPopup.append("<table class='tableTicketsSummary tableDefilante'>");
        htmlPopup.append("<thead>");
        htmlPopup.append("<tr class='tableTicketsSummaryTr'>");
        htmlPopup.append("<th class='tableTicketsSummaryTh width1'>Ticket</th>");
        htmlPopup.append("<th class='tableTicketsSummaryTh width3'>Rapporteur</th>");
        htmlPopup.append("<th class='tableTicketsSummaryTh width3'>Responsable</th>");
        htmlPopup.append("<th class='tableTicketsSummaryTh width3'>Priorité</th>");
        htmlPopup.append("<th class='tableTicketsSummaryTh width3'>Impact reglementaire</th>");
        htmlPopup.append("<th class='tableTicketsSummaryTh width3'>Metier concerné</th>");
        htmlPopup.append("<th class='tableTicketsSummaryTh width3'>Date de realisation souhaité</th>");
        htmlPopup.append("<th class='tableTicketsSummaryTh width3'>Summary</th>");
        htmlPopup.append("</tr>");
        htmlPopup.append("</thead>");
        htmlPopup.append("<tbody>");
        for (Map.Entry<Integer, Object> entryComponent : ticketsMap.entrySet()) {
            if (entryComponent.getKey() > 0) {
                Ticket ticket = (Ticket) entryComponent.getValue();
                String ticketId = ticket.getId().toString();
                String rapporteur = ticket.getReporter();
                String responsable = ticket.getOwner();
                String priority = ticket.getPriority();
                String impact_reglementaire = (String) ticketsEnCoursGestionDesDemandes.get(ticket.getId()).get("impact_reglementaire");
                String metier_concerne = (String) ticketsEnCoursGestionDesDemandes.get(ticket.getId()).get("metier_concerne");
                String date_realisation = (String) ticketsEnCoursGestionDesDemandes.get(ticket.getId()).get("date_realisation");
                String summary = ticket.getSummary();
                htmlPopup.append("<tr class='tableTicketsSummaryTr'>");
                htmlPopup.append("<td class='tableTicketsSummaryTd width1'><a href='http://172.28.70.246/trac/gestion_demandes/ticket/" + ticketId + "'  target='_blank'>#" + ticketId + "</a></td>");
                htmlPopup.append("<td class='tableTicketsSummaryTd width3'><div class='coupe'>" + rapporteur + "</div></td>");
                htmlPopup.append("<td class='tableTicketsSummaryTd width3'><div class='coupe'>" + responsable + "</div></td>");
                htmlPopup.append("<td class='tableTicketsSummaryTd width3'><div class='coupe'>" + priority + "</div></td>");
                htmlPopup.append("<td class='tableTicketsSummaryTd width3'><div class='coupe'>" + impact_reglementaire + "</div></td>");
                htmlPopup.append("<td class='tableTicketsSummaryTd width3'><div class='coupe'>" + metier_concerne + "</div></td>");
                htmlPopup.append("<td class='tableTicketsSummaryTd width3'><div class='coupe'>" + date_realisation + "</div></td>");
                htmlPopup.append("<td class='tableTicketsSummaryTd width3'>");
                htmlPopup.append("<a class='conteneur_info_bull'>");
                htmlPopup.append("<img class='info-icon' src='images/info.png' alt='info'>");
                htmlPopup.append("<span style='margin-left:-200px;'>");
                htmlPopup.append(summary);
                htmlPopup.append("</span>");
                htmlPopup.append("</a>");
                htmlPopup.append("</td>");
                htmlPopup.append("</tr>");
            }
        }
        htmlPopup.append("</tbody>");
        htmlPopup.append("</table>");
        htmlPopup.append("</div>");
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
