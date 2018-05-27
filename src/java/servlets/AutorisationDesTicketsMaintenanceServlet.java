/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import dataBaseTracRequests.DataBaseTracGenericRequests;
import dto.CoupleDTO;
import entitiesMysql.AuthMaintenance;
import entitiesTrac.Ticket;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;
import static servlets.AutorisationHotfixServlet.*;
import tools.Configuration;

import tools.DataBaseTools;
import tools.Tools;

/**
 *
 * @author 04494
 */
@WebServlet(name = "AutorisationDesTicketsMaintenanceServlet", urlPatterns = {"/AutorisationDesTicketsMaintenanceServlet"})
public class AutorisationDesTicketsMaintenanceServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            String resultat = "";
            String action = request.getParameter("action");
            if (action.equals("load")) {
                Map<String, Map<Integer, Map<String, Object>>> ticketAnomalieToDisplayMap = new HashMap<String, Map<Integer, Map<String, Object>>>();

                //GET all tickets from Audit (Audit)
                Map<Integer, AuthMaintenance> pisteAuditAnomaliesMainAuthorisedMap = getPisteAuditAnomalieMaintenanceAuthorised();

                //GET all tickets image de la piste d'audit (auhorised) from trac(trac)
                //1) Autorised
                List<Integer> idAnomalieMainAuthorisedList = new ArrayList<Integer>();
                idAnomalieMainAuthorisedList.addAll(pisteAuditAnomaliesMainAuthorisedMap.keySet());
                //2) Not authorised
                List<Integer> idAnomalieMainNotAuthorisedList = new DataBaseTracGenericRequests<Integer>().getList_TYPE_OfnamedQuery(Configuration.puAnomalies, "TicketCustom.findAllTicketAnomalieMaintenanceWithoutLivraison", null);
                List<Integer> globalList = new ArrayList<>();
                globalList.addAll(idAnomalieMainNotAuthorisedList);
                globalList.addAll(idAnomalieMainAuthorisedList);
                Map<Integer, Map<String, Object>> globalResultMap = Tools.analyseTicketsAnomalies(globalList);

                //GET tickets Authorised (trac)
                Map<Integer, Map<String, Object>> ticketAnomalieMainAuthorisedMap = getAllAnomalieMainAuthorised(globalResultMap, idAnomalieMainAuthorisedList);

                //GET tickets NOT Authorised (trac)
                Map<Integer, Map<String, Object>> tickeAnomalieMainNotAuthorisedMap = getAllAnomalieMainNotAuthorised(globalResultMap, idAnomalieMainNotAuthorisedList);

                //Supprimer les tickets autorised partir de la liste initiale (pipe 1 vers pipe 2)
                for (Iterator<Map.Entry<Integer, Map<String, Object>>> it = tickeAnomalieMainNotAuthorisedMap.entrySet().iterator(); it.hasNext();) {
                    Map.Entry<Integer, Map<String, Object>> entry = it.next();
                    if (ticketAnomalieMainAuthorisedMap.containsKey(entry.getKey())) {
                        it.remove();
                    }
                }

                //SORT NotAuthorised map by creation time
                Map<Integer, Long> unsortNotAuthorisedMap = new HashMap<Integer, Long>();
                for (Integer idTicketAnomalie : tickeAnomalieMainNotAuthorisedMap.keySet()) {
                    Long dateOuverture = ((Ticket) tickeAnomalieMainNotAuthorisedMap.get(idTicketAnomalie).get("Ticket")).getTime();
                    unsortNotAuthorisedMap.put(idTicketAnomalie, dateOuverture);
                }
                Map<Integer, Long> sortedNotAuthorisedMap = Tools.sortByComparator(unsortNotAuthorisedMap);
                Map<Integer, Map<String, Object>> tickeAnomalieNotAuthorisedSortedMap = new LinkedHashMap();
                for (Integer idTicketAnomalie : sortedNotAuthorisedMap.keySet()) {
                    tickeAnomalieNotAuthorisedSortedMap.put(idTicketAnomalie, tickeAnomalieMainNotAuthorisedMap.get(idTicketAnomalie));
                }

                //SORT Authorised map by authorisation time
                Map<Integer, Long> unsortAuthorisedMap = new HashMap<Integer, Long>();
                for (Integer idTicketAnomalie : ticketAnomalieMainAuthorisedMap.keySet()) {
                    if (pisteAuditAnomaliesMainAuthorisedMap.containsKey(idTicketAnomalie)) {
                        String stringAuthorisationDate = ((AuthMaintenance) pisteAuditAnomaliesMainAuthorisedMap.get(idTicketAnomalie)).getDateAuth();
                        long longAuthorisationDate = 0;
                        if (stringAuthorisationDate.trim().length() > 0) {
                            Date javaAuthorisationDate = ConvertStringDateToJavaDate(stringAuthorisationDate);
                            longAuthorisationDate = javaAuthorisationDate.getTime();
                        }
                        unsortAuthorisedMap.put(idTicketAnomalie, longAuthorisationDate);
                    }
                }
                Map<Integer, Long> sortedAuthorisedMap = Tools.sortByComparator(unsortAuthorisedMap);
                Map<Integer, Map<String, Object>> tickeAnomalieAuthorisedSortedMap = new LinkedHashMap<Integer, Map<String, Object>>();
                for (Integer idTicketAnomalie : sortedAuthorisedMap.keySet()) {
                    tickeAnomalieAuthorisedSortedMap.put(idTicketAnomalie, ticketAnomalieMainAuthorisedMap.get(idTicketAnomalie));
                }

                //Prepare DisplayMap avec 2 cles (java)
                ticketAnomalieToDisplayMap.put("NOT AUTORISED", tickeAnomalieNotAuthorisedSortedMap);
                ticketAnomalieToDisplayMap.put("AUTORISED", tickeAnomalieAuthorisedSortedMap);
                Tools tools = new Tools();
                resultat = getHtmlHotfixTable(tools.hasRole("AUTORISATION_MAINTENANCE"), ticketAnomalieToDisplayMap, pisteAuditAnomaliesMainAuthorisedMap);
            } else {
                //recuperer les checked box (html)
                String ticketsAuthorised = request.getParameter("ticketsAuthorised");
                //submit les mise a jour (audit + trac en cas de rejet seulement)
                resultat = updateTickets(ticketsAuthorised);
            }
            out.println(resultat);
        } finally {
            out.close();
        }
    }

    public Map<Integer, AuthMaintenance> getPisteAuditAnomalieMaintenanceAuthorised() {
        DataBaseTools dbTools = new DataBaseTools(Configuration.puOvTools);
        Map<Integer, AuthMaintenance> pisteAuditAnomaliesMaintenanceAuthorisedMap = new HashMap<Integer, AuthMaintenance>();
        Query q = dbTools.em.createNamedQuery("AuthMaintenance.findAll");
        q.setHint(QueryHints.REFRESH, HintValues.TRUE);
        List<AuthMaintenance> pisteAuditAnomaliesMaintenanceAuthorisedList = (List<AuthMaintenance>) q.getResultList();
        dbTools.closeRessources();
        try {
            for (AuthMaintenance authMaintenance : pisteAuditAnomaliesMaintenanceAuthorisedList) {
                pisteAuditAnomaliesMaintenanceAuthorisedMap.put(authMaintenance.getIdAnomalie(), authMaintenance);
            }
        } catch (Exception ex) {
            Tools.traiterException(Tools.getStackTrace(ex));
        }
        return pisteAuditAnomaliesMaintenanceAuthorisedMap;
    }

    //Anomalies Authorised
    public Map<Integer, Map<String, Object>> getAllAnomalieMainAuthorised(Map<Integer, Map<String, Object>> globalResultMap, List<Integer> idAnomalieMainAuthorisedList) {
        //Remove ticket APPLIQUEE SUR PROD
        Map<Integer, Map<String, Object>> TicketCustomAnomalieMainAuthorisedMap = new HashMap<Integer, Map<String, Object>>();
        try {
            for (Integer idTicket : idAnomalieMainAuthorisedList) {
                if (globalResultMap.containsKey(idTicket)) {
                    String priority = ((Ticket) globalResultMap.get(idTicket).get("Ticket")).getPriority();
                    String status = ((Ticket) globalResultMap.get(idTicket).get("Ticket")).getStatus();
                    String version = ((Ticket) globalResultMap.get(idTicket).get("Ticket")).getVersion();
                    if (!priority.equals("APPLIQUEE SUR PROD") && !status.equals("closed") && (version.equals("RELEASE A QUALIFIER") || version.equals("RELEASE A CERTIFIER"))) {
                        TicketCustomAnomalieMainAuthorisedMap.put(idTicket, globalResultMap.get(idTicket));
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(ex));
        }
        return TicketCustomAnomalieMainAuthorisedMap;
    }

    //Anomalies Not Authorised
    private Map<Integer, Map<String, Object>> getAllAnomalieMainNotAuthorised(Map<Integer, Map<String, Object>> globalResultMap, List<Integer> idAnomalieHfNotAuthorisedList) {
        Map<Integer, Map<String, Object>> TicketCustomAnomalieMainNotAuthorisedMap = new HashMap<Integer, Map<String, Object>>();
        try {
            //Filtrage des tickets NOT authorised
            for (Integer idTicket : idAnomalieHfNotAuthorisedList) {
                if (globalResultMap.containsKey(idTicket)) {
                    String projet = globalResultMap.get(idTicket).get("projet").toString();
                    if (projet.equals("MAINTENANCE")) {
                        TicketCustomAnomalieMainNotAuthorisedMap.put(idTicket, globalResultMap.get(idTicket));
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(ex));
        }
        return TicketCustomAnomalieMainNotAuthorisedMap;
    }

    public static String getHtmlHotfixTable(boolean authorizedUser, Map<String, Map<Integer, Map<String, Object>>> ticketAnomalieToDisplayMap, Map<Integer, AuthMaintenance> pisteAuditAllAnomaliesMap) {
        SimpleDateFormat parseFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        parseFormat.setTimeZone(TimeZone.getTimeZone("GMT+1"));

        int comp = 0;
        String cle = null;
        int largeur = 0;
        int nbrreportings = 0;
        StringBuilder sbResult = new StringBuilder();
        sbResult.append("<table id='tableTicketsHotfix' class='roundCornerTable'>");
        sbResult.append("<thead>");
        sbResult.append("<tr>");
        sbResult.append("<th>Nbr</th>");
        sbResult.append("<th>Anomalie</th>");
        sbResult.append("<th>Domaine</th>");
        sbResult.append("<th>Créée par</th>");
        sbResult.append("<th>Créé le</th>");
        sbResult.append("<th>Priorité</th>");
        sbResult.append("<th>Summary</th>");
        sbResult.append("<th>Aval PMO</th>");

        largeur = 10;
        nbrreportings = 2;

        sbResult.append("</tr>");
        sbResult.append("</thead>");
        sbResult.append("<tbody>");
        for (int i = 0; i < nbrreportings; i++) {
            if (i == 0) {
                sbResult.append("<tr class='titre'><td colspan='" + largeur + "'>Tickets MAINTENANCE en attente d'autorisation</td></tr>");
                cle = "NOT AUTORISED";
            } else if (i == 1) {
                sbResult.append("<tr class='titre'><td colspan='" + largeur + "'>Tickets MAINTENANCE Autorisés</td></tr>");
                cle = "AUTORISED";
            }
            comp = 0;

            if (!ticketAnomalieToDisplayMap.isEmpty()) {
                for (Integer idTicketAnomalies : ticketAnomalieToDisplayMap.get(cle).keySet()) {
                    Map<String, Object> ticketCustomDetailsAux = ticketAnomalieToDisplayMap.get(cle).get(idTicketAnomalies);
                    Ticket ticketAnomalies = (Ticket) (ticketCustomDetailsAux.get("Ticket"));
                    Integer idAnomalie = ((Integer) ticketAnomalies.getId());
                    comp++;
                    sbResult.append("<tr>");
                    sbResult.append("<td>");
                    sbResult.append(comp + ")");
                    sbResult.append("</td>");

                    sbResult.append("<td>");
                    sbResult.append("<a class='lien numAnmalie'  onclick=\"openTracTicketInNewTab($(this).html(),'ANOMALIE');\">");
                    sbResult.append("#" + idAnomalie);
                    sbResult.append("</a>");
                    sbResult.append("</td>");

                    sbResult.append("<td style ='word-wrap: break-word;'>");
                    sbResult.append(ticketAnomalies.getMilestone());
                    sbResult.append("</td>");

                    sbResult.append("<td>");
                    sbResult.append(ticketAnomalies.getReporter());
                    sbResult.append("</td>");

                    sbResult.append("<td>");
                    sbResult.append(ConvertTimeTracToJavaDate(ticketAnomalies.getTime() / 1000));
                    sbResult.append("</td>");

                    sbResult.append("<td>");
                    sbResult.append(ticketAnomalies.getPriority().replace("DEVELOPPEMENT", "POUR DEV").replace("INFORMATION", "INFO"));
                    sbResult.append("</td>");

                    sbResult.append("<td style='padding-bottom: 2px; padding-top: 2px; margin: auto'>");
                    sbResult.append("<a class='conteneur_info_bull'>");
                    sbResult.append("<img class='info-icon' src='images/info.png' alt='info'>");
                    sbResult.append("<span>");
                    sbResult.append(ticketAnomalies.getSummary());
                    sbResult.append("</span>");
                    sbResult.append("</a>");
                    sbResult.append("</td>");

                    //******************************** debut checkbox aval PMO ********************************
                    sbResult.append("<td style='padding-bottom: 2px; padding-top: 2px;  margin: auto'>");
                    if (authorizedUser) {
                        if (i == 0) {
                            if (pisteAuditAllAnomaliesMap.containsKey(idAnomalie)) {
                                sbResult.append("<center><img src='images/valider.png'  height='22' width='22' margin-left='40%'></center>");
                            } else {
                                sbResult.append("<input type='checkbox' id='A" + idAnomalie + "' class='active_checkbox'");
                                sbResult.append("/>");
                                sbResult.append("<label for='A" + idAnomalie + "' class='css-label bout1'></label>");
                                sbResult.append("<input type='hidden' value='aval' id='ticketValide' name='ticketValide' class='ticketValide'/>");
                            }
                        } else if (i == 1) {
                            if (pisteAuditAllAnomaliesMap.containsKey(idAnomalie)) {
                                if (pisteAuditAllAnomaliesMap.containsKey(idAnomalie)) {
                                    AuthMaintenance authMaintenance = (AuthMaintenance) pisteAuditAllAnomaliesMap.get(idAnomalie);
                                    sbResult.append(authMaintenance.getAuthoriser() + "<br>" + authMaintenance.getDateAuth());
                                }
                            }
                        }
                    } else {
                        if (i == 0) {
                            if (pisteAuditAllAnomaliesMap.containsKey(idAnomalie)) {
                                sbResult.append("<center><img src='images/valider.png'  height='22' width='22' margin-left='40%'></center>");
                            } else {
                                sbResult.append("<center><img src='images/bloquer.png'  height='22' width='22'></center>");
                            }
                        } else if (i == 1) {
                            if (pisteAuditAllAnomaliesMap.containsKey(idAnomalie)) {
                                AuthMaintenance authMaintenance = (AuthMaintenance) pisteAuditAllAnomaliesMap.get(idAnomalie);
                                sbResult.append(authMaintenance.getAuthoriser() + "<br>" + authMaintenance.getDateAuth());
                            }
                        }
                    }
                    sbResult.append("</td>");
                    //******************************** fin checkbox aval PMO **********************************

                    sbResult.append("</tr>");
                }
            } else {
                sbResult.append("<tr><td colspan='" + largeur + "'>Aucun ticket a traiter</td></tr>");
            }
        }
        sbResult.append("</tbody>");
        sbResult.append("</table>");
        sbResult.append("<br>");
        if (authorizedUser) {
            sbResult.append("<p><center><span id='messageResultatPersist' class='vert clignotant'></span></center></p>");
            sbResult.append("<div class='center'>");
            sbResult.append("<input type='button' class='boutonValiderStandard' id='validerTransfertHfVersPROD' value='Autoriser la prise en charge RELEASE' onclick='validerTickets();' />");
            sbResult.append("</div>");
        }
        return sbResult.toString();
    }

    //mise à jour des tickets (trac + piste audit) après validation de la page
    private String updateTickets(String ticketsAuthorised) {
        String resultat = "";
        List<AuthMaintenance> AnomalieMainToBePersistedList = new ArrayList<AuthMaintenance>();
        boolean resultatValidation = true;
        DataBaseTools dbToolsCommit = new DataBaseTools(Configuration.puOvTools);
        //get connected user
        String connectedUser = Tools.getConnectedLogin();

        try {
            List<Integer> AnomalieAuthorisedList = new ArrayList<Integer>();
            //******************************** update mysql AvalMOA ********************************
            if (!ticketsAuthorised.isEmpty()) {
                String[] tabAuxAval = ticketsAuthorised.split("A");
                for (String idAnomalie : tabAuxAval) {
                    if (!idAnomalie.isEmpty()) {
                        AnomalieAuthorisedList.add(Integer.parseInt(idAnomalie));
                        AuthMaintenance authMaintenance = new AuthMaintenance();
                        authMaintenance.setIdAnomalie(Integer.parseInt(idAnomalie));
                        authMaintenance.setAuthoriser(connectedUser);
                        authMaintenance.setDateAuth(ConvertTimeTracToJavaDate(System.currentTimeMillis()));
                        AnomalieMainToBePersistedList.add(authMaintenance);
                    }
                }
                List<Integer> globalList = new ArrayList<>();
                globalList.addAll(AnomalieAuthorisedList);
                globalList.addAll(AnomalieAuthorisedList);
                Map<Integer, Map<String, Object>> ticketAnomaliesAcceptedMoaMap = Tools.analyseTicketsAnomalies(globalList);
                //******************************** update trac MOA ********************************
                for (String idAnomalie : tabAuxAval) {
                    if (!idAnomalie.isEmpty()) {
                        Map<String, Object> ticketCustomDetailsAux = ticketAnomaliesAcceptedMoaMap.get(Integer.parseInt(idAnomalie));
                        Ticket ticketAnomalies = (Ticket) (ticketCustomDetailsAux.get("Ticket"));


                        String pu = Configuration.puAnomalies;
                        int numTicket = ticketAnomalies.getId();
                        String messageTrac = "== Ticket de MAINTENANCE authorisé par le responsable PMO pour integrer la release en cours==";
                        String newOwner = null;
                        String newPriority = null;
                        String newStatus = null;
                        String newVersion = null;
                        List<CoupleDTO> customFieldList = null;
                        DataBaseTracGenericRequests.updateTicketTracGeneral(pu, numTicket, connectedUser, messageTrac, newOwner, newPriority, newStatus, newVersion, customFieldList);
                    }
                }
            }
            dbToolsCommit.updateObjectList(AnomalieMainToBePersistedList);
        } catch (Exception ex) {
            ex.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(ex));
            resultatValidation = false;
        } finally {
            dbToolsCommit.closeRessources();
        }

        if (resultatValidation) {
            resultat = "mise à  jour des tickets effetuée avec succés";
        } else {
            resultat = "echec de mise à  jour des tickets des tickets";
        }
        return resultat;
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
