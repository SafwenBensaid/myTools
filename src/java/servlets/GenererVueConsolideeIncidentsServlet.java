package servlets;

import entitiesTrac.Ticket;
import entitiesTrac.TicketCustom;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;
import tools.DataBaseTools;

/**
 *
 * @author 04494
 */
@WebServlet(name = "GenererVueConsolideeIncidentsServlet", urlPatterns = {"/GenererVueConsolideeIncidentsServlet"})
public class GenererVueConsolideeIncidentsServlet extends HttpServlet {

    Map<Integer, Map<String, Object>> ticketsEnCoursGestionDesIncidents;
    StringBuilder htmlPopup = new StringBuilder();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            String param = request.getParameter("param");
            response.getWriter().println(afficherVueConsolideeIncidents(param));
        } finally {
            response.getWriter().close();
        }
    }

    private synchronized String afficherVueConsolideeIncidents(String param) {
        DataBaseTools dbTools = new DataBaseTools("dbGestionIncidentsPU");
        ticketsEnCoursGestionDesIncidents = chargerTousLesTicketsEnCoursGestionDesIncidents(dbTools);
        dbTools.closeRessources();
        return generateHtmlCode(param).toString();
    }

    public static Map<Integer, Map<String, Object>> chargerTousLesTicketsEnCoursGestionDesIncidents(DataBaseTools dbTools) {
        Query q = dbTools.em.createNamedQuery("TicketCustom.findAllTicketEnCours");
        q.setHint(QueryHints.REFRESH, HintValues.TRUE);
        List<TicketCustom> ticketsEnCoursGestionDesIncidents = (List<TicketCustom>) q.getResultList();
        Map<Integer, Map<String, Object>> ticketEnCoursMap = newTrac.ToolsNewTrac.traiterTicketCustom(ticketsEnCoursGestionDesIncidents);
        return ticketEnCoursMap;
    }

    public StringBuilder generateHtmlCode(String param) {
        StringBuilder htmlSb = new StringBuilder();
        htmlSb.append("<table id='tableTicketsHotfix' class='roundCornerTable statistiquesTable'>");
        htmlSb.append("<thead>");
        htmlSb.append("<tr>");
        htmlSb.append("<th class='traitEpaisBas'>");
        htmlSb.append("</th>");
        htmlSb.append("<th colspan='3'class='traitEpaisGauche traitEpaisBas'>");
        htmlSb.append("Incidents en attente <br> de prise en charge");
        htmlSb.append("</th>");
        htmlSb.append("<th colspan='3'class='traitEpaisGauche traitEpaisBas'>");
        htmlSb.append("Délais d'acceptation échu");
        htmlSb.append("</th>");
        htmlSb.append("<th colspan='3'class='traitEpaisGauche traitEpaisBas'>");
        htmlSb.append("Délais de résolution échu");
        htmlSb.append("</th>");
        htmlSb.append("</tr>");
        htmlSb.append("</thead>");
        htmlSb.append("<tbody>");
        htmlSb.append("<tr class='titre'>");
        if (param.equals("composant")) {
            htmlSb.append("<td class='traitEpaisBas coin'>Applications</td>");
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
        htmlSb.append("</tr>");
        htmlSb.append(genererStructureHtml(param));
        htmlSb.append("</tbody>");
        htmlSb.append("</table>");
        htmlSb.append(htmlPopup);
        htmlPopup.setLength(0);
        return htmlSb;
    }

    private StringBuilder genererStructureHtml(String param) {
        String owner, dpc_echu, dmt_echu, avancement, priority, state = "";

        List<String> prioritiesToDisplay = new ArrayList<String>();
        prioritiesToDisplay.add("bloquante");
        prioritiesToDisplay.add("majeure");
        prioritiesToDisplay.add("mineure");

        List<String> statesToDisplay = new ArrayList<String>();
        statesToDisplay.add("enAttentePriseEnCharge");
        statesToDisplay.add("dpcEchu");
        statesToDisplay.add("dmtEchu");

        StringBuilder sbResult = new StringBuilder();

        if (param.equals("composant")) {

            Map<String, Map<Integer, Object>> componentMap = new HashMap<String, Map<Integer, Object>>();
            Map<String, Map<Integer, Object>> avancementComponentMap = new LinkedHashMap<>();


            try {
                if (ticketsEnCoursGestionDesIncidents.size() > 0) {
                    //Construire une map des tickets par Composant
                    for (Map.Entry<Integer, Map<String, Object>> entryTicket : ticketsEnCoursGestionDesIncidents.entrySet()) {
                        Ticket ticket = (Ticket) entryTicket.getValue().get("Ticket");
                        owner = ticket.getComponent();
                        if (componentMap.containsKey(owner)) {
                            componentMap.get(owner).put(ticket.getId(), ticket);
                        } else {
                            Map<Integer, Object> ticketDetailsMap = new HashMap<Integer, Object>();
                            ticketDetailsMap.put(ticket.getId(), ticket);
                            componentMap.put(owner, ticketDetailsMap);
                        }
                    }

                    //Parcourir les tickets par Composants    
                    for (Map.Entry<String, Map<Integer, Object>> entryComponent : componentMap.entrySet()) {
                        sbResult.append("<tr>");
                        sbResult.append("<td class='projectName' title='" + entryComponent.getKey().toUpperCase() + "'>").append(entryComponent.getKey().toUpperCase()).append("</td>");

                        String classeTrait;
                        for (String allowedStates : statesToDisplay) {
                            for (String allowedPriority : prioritiesToDisplay) {
                                Map<Integer, Object> defaultMap = new HashMap<>();
                                defaultMap.put(0, entryComponent.getKey().toUpperCase());
                                avancementComponentMap.put(allowedStates + "_" + allowedPriority, defaultMap);
                            }
                        }

                        //Trier les tickets de ce composant par état avancement et par priorité
                        for (Map.Entry<Integer, Object> entryTicket : entryComponent.getValue().entrySet()) {
                            dpc_echu = (String) ticketsEnCoursGestionDesIncidents.get(entryTicket.getKey()).get("dpc_echu");
                            dmt_echu = (String) ticketsEnCoursGestionDesIncidents.get(entryTicket.getKey()).get("dmt_echu");
                            priority = ((Ticket) entryTicket.getValue()).getPriority();
                            if (prioritiesToDisplay.contains(priority)) {
                                if (!dpc_echu.isEmpty() && !dmt_echu.isEmpty()) {
                                    if (dpc_echu.equals("0") && dmt_echu.equals("0")) {
                                        state = "enAttentePriseEnCharge";
                                    } else if (dpc_echu.equals("1") && dmt_echu.equals("0")) {
                                        state = "dpcEchu";
                                    } else {
                                        state = "dmtEchu";
                                    }
                                }
                                avancement = state + "_" + priority;
                                avancementComponentMap.get(avancement).put(entryTicket.getKey(), entryTicket.getValue());
                            }
                        }

                        //Préparer les colonnes html de ce composant par avancement (tr + popup)
                        int compteurCouleur = 0;
                        int compteurId = 0;
                        for (Map.Entry<String, Map<Integer, Object>> entryAvancement : avancementComponentMap.entrySet()) {
                            if (entryAvancement.getKey().split("_")[1].equals("bloquante")) {
                                compteurCouleur = 1;
                            }
                            String idCase = entryComponent.getKey().replaceAll(" ", "").replaceAll("/", "") + "_" + compteurId;
                            if (compteurCouleur == 1) {
                                classeTrait = " traitEpaisGauche";
                            } else {
                                classeTrait = "";
                            }
                            int nbrTickets = entryAvancement.getValue().size() - 1;
                            String contenuCase;
                            if (nbrTickets == 0) {
                                contenuCase = "0";
                            } else {
                                contenuCase = "<a href='#' id='" + idCase + "' class='nbrTickets'  onclick='openPopup(this);'>";
                                contenuCase += nbrTickets;
                                contenuCase += "</a>";
                                generatePopupHtml(idCase, entryAvancement.getValue());
                            }
                            sbResult.append("<td class='couleur").append(compteurCouleur).append(classeTrait).append("'>").append(contenuCase).append("</td>");
                            compteurCouleur++;
                            compteurId++;
                        }
                        sbResult.append("</tr>");
                        avancementComponentMap.clear();
                    }
                } else {
                    sbResult.append("<tr> <td colspan='10'>  Aucun Incident en cours de résolution. </td> </tr>");
                }
            } catch (Exception exep) {
                tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
            }
        } else if (param.equals("responsable")) {
            Map<String, Map<Integer, Object>> ownerMap = new HashMap<String, Map<Integer, Object>>();
            Map<String, Map<Integer, Object>> avancementOwnerMap = new LinkedHashMap<>();


            try {
                if (ticketsEnCoursGestionDesIncidents.size() > 0) {
                    //Construire une map des tickets par Composant
                    for (Map.Entry<Integer, Map<String, Object>> entryTicket : ticketsEnCoursGestionDesIncidents.entrySet()) {
                        Ticket ticket = (Ticket) entryTicket.getValue().get("Ticket");
                        owner = ticket.getOwner();
                        if (ownerMap.containsKey(owner)) {
                            ownerMap.get(owner).put(ticket.getId(), ticket);
                        } else {
                            Map<Integer, Object> ticketDetailsMap = new HashMap<Integer, Object>();
                            ticketDetailsMap.put(ticket.getId(), ticket);
                            ownerMap.put(owner, ticketDetailsMap);
                        }
                    }

                    //Parcourir les tickets par Composants    
                    for (Map.Entry<String, Map<Integer, Object>> entryOwner : ownerMap.entrySet()) {
                        sbResult.append("<tr>");
                        sbResult.append("<td class='projectName' title='" + entryOwner.getKey().toUpperCase() + "'>").append(entryOwner.getKey().toUpperCase()).append("</td>");

                        String classeTrait;
                        for (String allowedStates : statesToDisplay) {
                            for (String allowedPriority : prioritiesToDisplay) {
                                Map<Integer, Object> defaultMap = new HashMap<>();
                                defaultMap.put(0, entryOwner.getKey().toUpperCase());
                                avancementOwnerMap.put(allowedStates + "_" + allowedPriority, defaultMap);
                            }
                        }

                        //Trier les tickets de ce composant par état avancement et par priorité
                        for (Map.Entry<Integer, Object> entryTicket : entryOwner.getValue().entrySet()) {
                            dpc_echu = (String) ticketsEnCoursGestionDesIncidents.get(entryTicket.getKey()).get("dpc_echu");
                            dmt_echu = (String) ticketsEnCoursGestionDesIncidents.get(entryTicket.getKey()).get("dmt_echu");
                            priority = ((Ticket) entryTicket.getValue()).getPriority();
                            if (prioritiesToDisplay.contains(priority)) {
                                if (!dpc_echu.isEmpty() && !dmt_echu.isEmpty()) {
                                    if (dpc_echu.equals("0") && dmt_echu.equals("0")) {
                                        state = "enAttentePriseEnCharge";
                                    } else if (dpc_echu.equals("1") && dmt_echu.equals("0")) {
                                        state = "dpcEchu";
                                    } else {
                                        state = "dmtEchu";
                                    }
                                }
                                avancement = state + "_" + priority;
                                avancementOwnerMap.get(avancement).put(entryTicket.getKey(), entryTicket.getValue());
                            }
                        }

                        //Préparer les colonnes html de ce composant par avancement (tr + popup)
                        int compteurCouleur = 0;
                        int compteurId = 0;
                        for (Map.Entry<String, Map<Integer, Object>> entryAvancement : avancementOwnerMap.entrySet()) {
                            if (entryAvancement.getKey().split("_")[1].equals("bloquante")) {
                                compteurCouleur = 1;
                            }
                            String idCase = entryOwner.getKey().replaceAll(" ", "").replaceAll("/", "") + "_" + compteurId;
                            if (compteurCouleur == 1) {
                                classeTrait = " traitEpaisGauche";
                            } else {
                                classeTrait = "";
                            }
                            int nbrTickets = entryAvancement.getValue().size() - 1;
                            String contenuCase;
                            if (nbrTickets == 0) {
                                contenuCase = "0";
                            } else {
                                contenuCase = "<a href='#' id='" + idCase + "' class='nbrTickets'  onclick='openPopup(this);'>";
                                contenuCase += nbrTickets;
                                contenuCase += "</a>";
                                generatePopupHtml(idCase, entryAvancement.getValue());
                            }
                            sbResult.append("<td class='couleur").append(compteurCouleur).append(classeTrait).append("'>").append(contenuCase).append("</td>");
                            compteurCouleur++;
                            compteurId++;
                        }
                        sbResult.append("</tr>");
                        avancementOwnerMap.clear();
                    }
                } else {
                    sbResult.append("<tr> <td colspan='10'>  Aucun Incident en cours de résolution. </td> </tr>");
                }
            } catch (Exception exep) {
                tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
            }
        }
        return sbResult;
    }

    private void generatePopupHtml(String idPopup, Map<Integer, Object> ticketsMap) {
        htmlPopup.append("<div class='").append(idPopup).append("'  style='display: none'>");
        htmlPopup.append("<p class='titrePopup'>Application: ").append(ticketsMap.get(0).toString()).append("</p>");
        htmlPopup.append("<table class='tableTicketsSummary tableDefilante'>");
        htmlPopup.append("<thead>");
        htmlPopup.append("<tr class='tableTicketsSummaryTr'>");
        htmlPopup.append("<th class='tableTicketsSummaryTh width1'>Ticket</th>");
        htmlPopup.append("<th class='tableTicketsSummaryTh width3'>Rapporteur</th>");
        htmlPopup.append("<th class='tableTicketsSummaryTh width3'>Responsable</th>");
        htmlPopup.append("<th class='tableTicketsSummaryTh width3'>Priorité</th>");
        htmlPopup.append("<th class='tableTicketsSummaryTh width3'>Impact</th>");
        htmlPopup.append("<th class='tableTicketsSummaryTh width3'>Délaid d'acceptation</th>");
        htmlPopup.append("<th class='tableTicketsSummaryTh width3'>Délais de résolution</th>");
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
                String impact = (String) ticketsEnCoursGestionDesIncidents.get(ticket.getId()).get("type_propagation");
                String delais_prise_en_chrage = (String) ticketsEnCoursGestionDesIncidents.get(ticket.getId()).get("delais_prise_en_chrage");
                String duree_resolution = (String) ticketsEnCoursGestionDesIncidents.get(ticket.getId()).get("duree_resolution");
                String summary = ticket.getSummary();
                htmlPopup.append("<tr class='tableTicketsSummaryTr'>");
                htmlPopup.append("<td class='tableTicketsSummaryTd width1'><a href='http://172.28.70.246/trac/GESTION_INCIDENTS_SI/ticket/" + ticketId + "'  target='_blank'>#" + ticketId + "</a></td>");
                htmlPopup.append("<td class='tableTicketsSummaryTd width3'><div class='coupe'>" + rapporteur + "</div></td>");
                htmlPopup.append("<td class='tableTicketsSummaryTd width3'><div class='coupe'>" + responsable + "</div></td>");
                htmlPopup.append("<td class='tableTicketsSummaryTd width3'><div class='coupe'>" + priority + "</div></td>");
                htmlPopup.append("<td class='tableTicketsSummaryTd width3'><div class='coupe'>" + impact + "</div></td>");
                htmlPopup.append("<td class='tableTicketsSummaryTd width3'><div class='coupe'>" + delais_prise_en_chrage + "</div></td>");
                htmlPopup.append("<td class='tableTicketsSummaryTd width3'><div class='coupe'>" + duree_resolution + "</div></td>");
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
