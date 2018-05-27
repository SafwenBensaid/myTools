/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import dataBaseTracRequests.DataBaseTracGenericRequests;
import dto.CoupleDTO;
import entitiesMysql.AuditAnomalie;
import entitiesMysql.AuditAnomaliePK;
import entitiesTrac.Ticket;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;
import tools.*;
import static tools.Tools.sendEMail;

/**
 *
 * @author 04486
 */
public class AutorisationHotfixServlet extends HttpServlet {

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
            throws ServletException, IOException, ParseException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            String resultat = "";
            String action = request.getParameter("action");
            if (action.equals("load")) {
                Map<String, Map<Integer, Map<String, Object>>> ticketAnomalieToDisplayMap = new HashMap<String, Map<Integer, Map<String, Object>>>();

                //GET all tickets from Audit (Audit)
                Map<Integer, Map<Integer, AuditAnomalie>> pisteAuditAllAnomaliesMap = getPisteAuditAllAnomalie();

                //Remove tickets rejected depuis plus que 24h
                for (Iterator<Map.Entry<Integer, Map<Integer, AuditAnomalie>>> it = pisteAuditAllAnomaliesMap.entrySet().iterator(); it.hasNext();) {
                    Map.Entry<Integer, Map<Integer, AuditAnomalie>> entry = it.next();
                    for (Iterator<Map.Entry<Integer, AuditAnomalie>> iter = entry.getValue().entrySet().iterator(); iter.hasNext();) {
                        Map.Entry<Integer, AuditAnomalie> subEntry = iter.next();
                        if (subEntry.getKey().equals(0)) {
                            AuditAnomalie auditAnomalie = subEntry.getValue();
                            //Retenir uniquement les rejets datant de 24h 
                            Map<TimeUnit, Long> ageRejet = compareDate(ConvertStringDateToJavaDate(auditAnomalie.getDateAuth()), new Date());
                            if (ageRejet.get(TimeUnit.DAYS) >= 1) {
                                iter.remove();
                            }
                        }
                    }
                }
                //GET all tickets image de la piste d'audit (auhorised/rejected) from trac(trac)
                //1)
                List<Integer> idAnomalieHfAuthorisedList = new ArrayList<Integer>();
                idAnomalieHfAuthorisedList.addAll(pisteAuditAllAnomaliesMap.keySet());
                //2)
                List<Integer> ticketAnomalieHotfixList = new DataBaseTracGenericRequests<Integer>().getList_TYPE_OfnamedQuery(Configuration.puAnomalies, "TicketCustom.findAllTicketAnomalieHotfixWithoutLivraison", null);

                idAnomalieHfAuthorisedList.addAll(ticketAnomalieHotfixList);
                Map<Integer, Map<String, Object>> globalResultMap = Tools.analyseTicketsAnomalies(idAnomalieHfAuthorisedList);
                Map<Integer, Map<String, Object>> ticketAnomalieAuthorisedorRejectedHotfixMap = getAllTicketAnomalieAuthorisedorRejected(globalResultMap, idAnomalieHfAuthorisedList);

                //GET tickets NOT Authorised (trac)
                Map<Integer, Map<String, Object>> tickeAnomalieNotAuthorisedMap = getAllAnomalieNotAuthorised(ticketAnomalieAuthorisedorRejectedHotfixMap, ticketAnomalieHotfixList);

                //Dispatch tickets Authorised/Rejected 
                Map<Integer, Map<String, Object>> tickeAnomalieAuthorisedMap = new HashMap<Integer, Map<String, Object>>();
                Map<Integer, Map<String, Object>> tickeAnomalieRejectedMap = new HashMap<Integer, Map<String, Object>>();
                try {
                    Object value = new Object();
                    for (Integer idTicketAnomalie : ticketAnomalieAuthorisedorRejectedHotfixMap.keySet()) {
                        String typeAnomalie = ((Ticket) ticketAnomalieAuthorisedorRejectedHotfixMap.get(idTicketAnomalie).get("Ticket")).getType();
                        if (pisteAuditAllAnomaliesMap.containsKey(idTicketAnomalie)) {
                            if (((typeAnomalie.equals("FONCTIONNELLE")) && (pisteAuditAllAnomaliesMap.get(idTicketAnomalie).containsKey(2)) && (pisteAuditAllAnomaliesMap.get(idTicketAnomalie).containsKey(1)))
                                    || ((typeAnomalie.equals("TECHNIQUE")) && (pisteAuditAllAnomaliesMap.get(idTicketAnomalie).containsKey(2)))) {
                                for (String name : ticketAnomalieAuthorisedorRejectedHotfixMap.get(idTicketAnomalie).keySet()) {
                                    value = ticketAnomalieAuthorisedorRejectedHotfixMap.get(idTicketAnomalie).get(name);
                                    if (tickeAnomalieAuthorisedMap.containsKey(idTicketAnomalie)) {
                                        tickeAnomalieAuthorisedMap.get(idTicketAnomalie).put(name, value);
                                    } else {
                                        Map<String, Object> tickeAnomalieAuthorisedDetailsMap = new HashMap<String, Object>();
                                        tickeAnomalieAuthorisedDetailsMap.put(name, value);
                                        tickeAnomalieAuthorisedMap.put(idTicketAnomalie, tickeAnomalieAuthorisedDetailsMap);
                                    }
                                }
                            } else if (pisteAuditAllAnomaliesMap.get(idTicketAnomalie).containsKey(0)) {
                                for (String name : ticketAnomalieAuthorisedorRejectedHotfixMap.get(idTicketAnomalie).keySet()) {
                                    value = ticketAnomalieAuthorisedorRejectedHotfixMap.get(idTicketAnomalie).get(name);
                                    if (tickeAnomalieRejectedMap.containsKey(idTicketAnomalie)) {
                                        tickeAnomalieRejectedMap.get(idTicketAnomalie).put(name, value);
                                    } else {
                                        Map<String, Object> tickeAnomalieRejectedDetailsMap = new HashMap<String, Object>();
                                        tickeAnomalieRejectedDetailsMap.put(name, value);
                                        tickeAnomalieRejectedMap.put(idTicketAnomalie, tickeAnomalieRejectedDetailsMap);
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception ex) {
                    Tools.traiterException(Tools.getStackTrace(ex));
                }

                //Supprimer les tickets autorisés/rejetés a partir de la liste initiale (pipe 1 vers pipe 2)
                for (Iterator<Map.Entry<Integer, Map<String, Object>>> it = tickeAnomalieNotAuthorisedMap.entrySet().iterator(); it.hasNext();) {
                    Map.Entry<Integer, Map<String, Object>> entry = it.next();
                    if (tickeAnomalieAuthorisedMap.containsKey(entry.getKey()) || tickeAnomalieRejectedMap.containsKey(entry.getKey())) {
                        it.remove();
                    }
                }

                //SORT NotAuthorised map by creation time
                Map<Integer, Long> unsortedNotAuthorisedMap = new HashMap<Integer, Long>();
                for (Integer idTicketAnomalie : tickeAnomalieNotAuthorisedMap.keySet()) {
                    Long dateOuverture = ((Ticket) tickeAnomalieNotAuthorisedMap.get(idTicketAnomalie).get("Ticket")).getTime();
                    unsortedNotAuthorisedMap.put(idTicketAnomalie, dateOuverture);
                }
                Map<Integer, Long> sortedNotAuthorisedMap = Tools.sortByComparator(unsortedNotAuthorisedMap);
                Map<Integer, Map<String, Object>> tickeAnomalieNotAuthorisedSortedMap = new LinkedHashMap();
                for (Integer idTicketAnomalie : sortedNotAuthorisedMap.keySet()) {
                    tickeAnomalieNotAuthorisedSortedMap.put(idTicketAnomalie, tickeAnomalieNotAuthorisedMap.get(idTicketAnomalie));
                }
                //SORT Authorised map by authorisation time
                Map<Integer, Long> unsortedAuthorisedMap = new HashMap<Integer, Long>();
                for (Integer idTicketAnomalie : tickeAnomalieAuthorisedMap.keySet()) {
                    if (pisteAuditAllAnomaliesMap.containsKey(idTicketAnomalie)) {
                        if (pisteAuditAllAnomaliesMap.get(idTicketAnomalie).containsKey(2)) {
                            String stringAuthorisationDate = pisteAuditAllAnomaliesMap.get(idTicketAnomalie).get(2).getDateAuth();
                            Date javaAuthorisationDate = ConvertStringDateToJavaDate(stringAuthorisationDate);
                            long longAuthorisationDate = javaAuthorisationDate.getTime();
                            unsortedAuthorisedMap.put(idTicketAnomalie, longAuthorisationDate);
                        }
                    }
                }
                Map<Integer, Long> sortedAuthorisedMap = Tools.sortByComparator(unsortedAuthorisedMap);
                Map<Integer, Map<String, Object>> tickeAnomalieAuthorisedSortedMap = new LinkedHashMap<Integer, Map<String, Object>>();
                for (Integer idTicketAnomalie : sortedAuthorisedMap.keySet()) {
                    tickeAnomalieAuthorisedSortedMap.put(idTicketAnomalie, tickeAnomalieAuthorisedMap.get(idTicketAnomalie));
                }
                //SORT Rejected map by Rejection time
                Map<Integer, Long> unsortedRejectedMap = new HashMap<Integer, Long>();
                for (Integer idTicketAnomalie : tickeAnomalieRejectedMap.keySet()) {
                    if (pisteAuditAllAnomaliesMap.containsKey(idTicketAnomalie)) {
                        if (pisteAuditAllAnomaliesMap.get(idTicketAnomalie).containsKey(0)) {
                            String stringRejectionDate = pisteAuditAllAnomaliesMap.get(idTicketAnomalie).get(0).getDateAuth();
                            Date javaRejectionDate = ConvertStringDateToJavaDate(stringRejectionDate);
                            long longRejectionDate = javaRejectionDate.getTime();
                            unsortedRejectedMap.put(idTicketAnomalie, longRejectionDate);
                        }
                    }
                }
                Map<Integer, Long> sortedRejectedMap = Tools.sortByComparator(unsortedRejectedMap);
                Map<Integer, Map<String, Object>> tickeAnomalieRejectedSortedMap = new LinkedHashMap();
                for (Integer idTicketAnomalie : sortedRejectedMap.keySet()) {
                    tickeAnomalieRejectedSortedMap.put(idTicketAnomalie, tickeAnomalieRejectedMap.get(idTicketAnomalie));
                }

                //Prepare DisplayMap avec 3 cles (java)
                ticketAnomalieToDisplayMap.put("NOT AUTORISED", tickeAnomalieNotAuthorisedSortedMap);
                ticketAnomalieToDisplayMap.put("AUTORISED", tickeAnomalieAuthorisedSortedMap);
                ticketAnomalieToDisplayMap.put("REJECTED", tickeAnomalieRejectedSortedMap);
                Tools tools = new Tools();
                resultat = getHtmlHotfixTable(tools.hasRole("AUTORISATION_HF_MOA"), tools.hasRole("AUTORISATION_HF_MOE"), ticketAnomalieToDisplayMap, pisteAuditAllAnomaliesMap);
            } else {
                //recuperer les checked box (html)
                String ticketsAvalMOA = request.getParameter("ticketsAvalMOA");
                String ticketsAvalMOE = request.getParameter("ticketsAvalMOE");
                String ticketsRejet = request.getParameter("ticketsRejet");
                String motifRejet = request.getParameter("motifRejet");
                //submit les mise a jour (audit + trac en cas de rejet seulement)
                resultat = updateTickets(ticketsAvalMOA, ticketsAvalMOE, ticketsRejet, motifRejet);
            }
            out.println(resultat);
        } finally {
            out.close();
        }
    }

    public Map<Integer, Map<Integer, AuditAnomalie>> getPisteAuditAllAnomalie() {
        DataBaseTools dbTools = new DataBaseTools(Configuration.puOvTools);
        Map<Integer, Map<Integer, AuditAnomalie>> pisteAuditAllAnomaliesMap = new HashMap<Integer, Map<Integer, AuditAnomalie>>();
        Query q = dbTools.em.createNamedQuery("AuditAnomalie.findAll");
        q.setHint(QueryHints.REFRESH, HintValues.TRUE);
        List<AuditAnomalie> pisteAuditAllAnomalieList = (List<AuditAnomalie>) q.getResultList();
        dbTools.closeRessources();
        try {
            Integer idTicketAnomalie = 0;
            for (AuditAnomalie auditAnomalie : pisteAuditAllAnomalieList) {
                idTicketAnomalie = ((Integer) auditAnomalie.getAuditAnomaliePK().getIdAnomalie());
                if (pisteAuditAllAnomaliesMap.containsKey(idTicketAnomalie)) {
                    pisteAuditAllAnomaliesMap.get(idTicketAnomalie).put(auditAnomalie.getAuditAnomaliePK().getEtatAuth(), auditAnomalie);
                } else {
                    Map<Integer, AuditAnomalie> pisteAuditAnomaliesDetailsMap = new HashMap<Integer, AuditAnomalie>();
                    pisteAuditAnomaliesDetailsMap.put(auditAnomalie.getAuditAnomaliePK().getEtatAuth(), auditAnomalie);
                    pisteAuditAllAnomaliesMap.put(idTicketAnomalie, pisteAuditAnomaliesDetailsMap);
                }
            }
        } catch (Exception ex) {
            Tools.traiterException(Tools.getStackTrace(ex));
        }
        return pisteAuditAllAnomaliesMap;
    }

    public Map<Integer, Map<String, Object>> getAllTicketAnomalieAuthorisedorRejected(Map<Integer, Map<String, Object>> globalResultMap, List<Integer> idAnomalieHfAuthorisedList) {
        //Remove ticket APPLIQUEE SUR PROD
        Map<Integer, Map<String, Object>> TicketCustomAnomalieHotfixAuthorisedorRejectedMap = new HashMap<Integer, Map<String, Object>>();
        for (Integer idTicket : idAnomalieHfAuthorisedList) {
            if (globalResultMap.containsKey(idTicket)) {
                String priority = ((Ticket) globalResultMap.get(idTicket).get("Ticket")).getPriority();
                String status = ((Ticket) globalResultMap.get(idTicket).get("Ticket")).getStatus();
                String version = ((Ticket) globalResultMap.get(idTicket).get("Ticket")).getVersion();
                if (!priority.equals("APPLIQUEE SUR PROD") && !status.equals("closed") && (version.equals("HOT FIXE POUR MISE EN PROD") || version.equals("ACTION A CHAUD POUR MISE EN PROD"))) {
                    TicketCustomAnomalieHotfixAuthorisedorRejectedMap.put(idTicket, globalResultMap.get(idTicket));
                }
            }
        }
        return TicketCustomAnomalieHotfixAuthorisedorRejectedMap;
    }
    //Anomalies Not Authorised

    private Map<Integer, Map<String, Object>> getAllAnomalieNotAuthorised(Map<Integer, Map<String, Object>> tickeCustomAnomalieHotfixNotAuthorisedMap, List<Integer> TicketAnomalieHotfixList) {
        try {
            //Remove ticket Non Curatif
            for (Integer idTicket : TicketAnomalieHotfixList) {
                if (tickeCustomAnomalieHotfixNotAuthorisedMap.containsKey(idTicket)) {
                    String mode_traitement = tickeCustomAnomalieHotfixNotAuthorisedMap.get(idTicket).get("mode_traitement").toString();
                    if (!mode_traitement.equals("CURATIF")) {
                        tickeCustomAnomalieHotfixNotAuthorisedMap.remove(idTicket);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(ex));
        }
        return tickeCustomAnomalieHotfixNotAuthorisedMap;
    }

    //ConvertTimeTracToStringDate
    public static String ConvertTimeTracToJavaDate(long timeTrac) {
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        formatter.setTimeZone(TimeZone.getTimeZone("Africa/Tunis"));
        String dateOuvertureMillisString = Long.toString(timeTrac);
        long dateOuvertureMillisLong = Long.parseLong(dateOuvertureMillisString);
        String stringDate = formatter.format(new Date(dateOuvertureMillisLong));
        return stringDate;
    }

    //ConvertTimeTracToStringDate
    public static String ConvertTimeTracToJavaDate(String timeTrac) {
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        formatter.setTimeZone(TimeZone.getTimeZone("Africa/Tunis"));
        long dateOuvertureMillisLong = Long.parseLong(timeTrac);
        String stringDate = formatter.format(new Date(dateOuvertureMillisLong));
        return stringDate;
    }

    //ConvertStringDateToJavaDate
    public static Date ConvertStringDateToJavaDate(String stringDate) {
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        formatter.setTimeZone(TimeZone.getTimeZone("Africa/Tunis"));
        Date javaDate = new Date();
        try {
            javaDate = formatter.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return javaDate;
    }

    //Calculer la différence entre deux Date (resultat en TimeUnit)
    public static Map<TimeUnit, Long> compareDate(Date date1, Date date2) {
        long milliesRest = date2.getTime() - date1.getTime();
        List<TimeUnit> units = new ArrayList<TimeUnit>(EnumSet.allOf(TimeUnit.class));
        Collections.reverse(units);
        Map<TimeUnit, Long> result = new LinkedHashMap<TimeUnit, Long>();
        for (TimeUnit unit : units) {
            long diff = unit.convert(milliesRest, TimeUnit.MILLISECONDS);
            long diffInMilliesForUnit = unit.toMillis(diff);
            milliesRest = milliesRest - diffInMilliesForUnit;
            result.put(unit, diff);
        }
        result.remove(TimeUnit.SECONDS);
        result.remove(TimeUnit.MILLISECONDS);
        result.remove(TimeUnit.MICROSECONDS);
        result.remove(TimeUnit.NANOSECONDS);
        return result;
    }

    public static String getHtmlHotfixTable(boolean authorizedMoA, boolean authorizedMoE, Map<String, Map<Integer, Map<String, Object>>> ticketAnomalieToDisplayMap, Map<Integer, Map<Integer, AuditAnomalie>> pisteAuditAllAnomaliesMap) {
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
        sbResult.append("<th>Version/Type</th>");
        sbResult.append("<th>Créée par</th>");
        sbResult.append("<th>Créé le</th>");
        sbResult.append("<th>Priorité</th>");
        sbResult.append("<th>Motif_HF</th>");
        sbResult.append("<th>Aval MOA</th>");
        sbResult.append("<th>Aval MOE</th>");
        sbResult.append("<th>Rejet</th>");

        largeur = 12;
        nbrreportings = 3;

        sbResult.append("</tr>");
        sbResult.append("</thead>");
        sbResult.append("<tbody>");
        for (int i = 0; i < nbrreportings; i++) {
            if (i == 0) {
                sbResult.append("<tr class='titre'><td colspan='" + largeur + "'>Tickets Hotfix en attente d'autorisation</td></tr>");
                cle = "NOT AUTORISED";
            } else if (i == 1) {
                sbResult.append("<tr class='titre'><td colspan='" + largeur + "'>Tickets Hotfix Autorisés en attente de prise en charge</td></tr>");
                cle = "AUTORISED";
            } else if (i == 2) {
                sbResult.append("<tr class='titre'><td colspan='" + largeur + "'>Tickets Hotfix Rejetés durant la journée</td></tr>");
                cle = "REJECTED";
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
                    sbResult.append(ticketAnomalies.getVersion().replace(" POUR MISE EN PROD", "").replace("ACTION", "ACT°") + " (" + ticketAnomalies.getType().substring(0, 4) + ")");
                    sbResult.append("</td>");

                    sbResult.append("<td>");
                    sbResult.append(ticketAnomalies.getReporter());
                    sbResult.append("</td>");

                    sbResult.append("<td>");
                    sbResult.append(ConvertTimeTracToJavaDate(ticketAnomalies.getTime() / 1000));
                    sbResult.append("</td>");

                    sbResult.append("<td>");
                    sbResult.append(ticketAnomalies.getPriority().replace("DEVELOPPEMENT", "POUR DEV"));
                    sbResult.append("</td>");

                    sbResult.append("<td style='padding-bottom: 2px; padding-top: 2px; margin: auto'>");
                    sbResult.append("<a class='conteneur_info_bull'>");
                    sbResult.append("<img class='info-icon' src='images/info.png' alt='info'>");
                    sbResult.append("<span>");
                    sbResult.append(ticketCustomDetailsAux.get("motivation_hf"));
                    sbResult.append("</span>");
                    sbResult.append("</a>");
                    sbResult.append("</td>");

                    //******************************** debut checkbox aval MOA ********************************
                    sbResult.append("<td style='padding-bottom: 2px; padding-top: 2px;  margin: auto'>");
                    if (authorizedMoA) {
                        if (i == 0) {
                            if (pisteAuditAllAnomaliesMap.containsKey(idAnomalie)) {
                                if (pisteAuditAllAnomaliesMap.get(idAnomalie).containsKey(1)) {
                                    sbResult.append("<center><img src='images/valider.png'  height='22' width='22' margin-left='40%'></center>");
                                } else {
                                    sbResult.append("<input type='checkbox' id='A" + idAnomalie + "' class='active_checkbox'");
                                    sbResult.append("/>");
                                    sbResult.append("<label for='A" + idAnomalie + "' class='css-label bout1'></label>");
                                    sbResult.append("<input type='hidden' value='aval' id='ticketValide' name='ticketValide' class='ticketValide'/>");
                                }
                            } else {
                                sbResult.append("<input type='checkbox' id='A" + idAnomalie + "' class='active_checkbox'");
                                sbResult.append("/>");
                                sbResult.append("<label for='A" + idAnomalie + "' class='css-label bout1'></label>");
                                sbResult.append("<input type='hidden' value='aval' id='ticketValide' name='ticketValide' class='ticketValide'/>");
                            }
                        } else if (i == 1) {
                            if (pisteAuditAllAnomaliesMap.containsKey(idAnomalie)) {
                                if (pisteAuditAllAnomaliesMap.get(idAnomalie).containsKey(1)) {
                                    AuditAnomalie auditAnomalie = (AuditAnomalie) pisteAuditAllAnomaliesMap.get(idAnomalie).get(1);
                                    sbResult.append(auditAnomalie.getAuthoriser() + "<br>" + auditAnomalie.getDateAuth());
                                }
                            }
                        }
                    } else {
                        if (i == 0) {
                            if (pisteAuditAllAnomaliesMap.containsKey(idAnomalie)) {
                                if (pisteAuditAllAnomaliesMap.get(idAnomalie).containsKey(1)) {
                                    sbResult.append("<center><img src='images/valider.png'  height='22' width='22' margin-left='40%'></center>");
                                } else {
                                    sbResult.append("<center><img src='images/bloquer.png'  height='22' width='22'></center>");
                                }
                            } else {
                                sbResult.append("<center><img src='images/bloquer.png'  height='22' width='22'></center>");
                            }
                        } else if (i == 1) {
                            if (pisteAuditAllAnomaliesMap.containsKey(idAnomalie)) {
                                if (pisteAuditAllAnomaliesMap.get(idAnomalie).containsKey(1)) {
                                    AuditAnomalie auditAnomalie = (AuditAnomalie) pisteAuditAllAnomaliesMap.get(idAnomalie).get(1);
                                    sbResult.append(auditAnomalie.getAuthoriser() + "<br>" + auditAnomalie.getDateAuth());
                                }
                            }
                        }
                    }
                    sbResult.append("</td>");
                    //******************************** fin checkbox aval MOA **********************************

                    //******************************** debut checkbox aval MOE ********************************
                    sbResult.append("<td style='padding-bottom: 2px; padding-top: 2px;  margin: auto'>");
                    if (authorizedMoE) {
                        if (i == 0) {
                            if (pisteAuditAllAnomaliesMap.containsKey(idAnomalie)) {
                                if (pisteAuditAllAnomaliesMap.get(idAnomalie).containsKey(2)) {
                                    sbResult.append("<center><img src='images/valider.png'  height='22' width='22' margin-left='40%'></center>");
                                } else {
                                    sbResult.append("<input type='checkbox' id='E" + idAnomalie + "' class='active_checkbox'");
                                    sbResult.append("/>");
                                    sbResult.append("<label for='E" + idAnomalie + "' class='css-label bout1'></label>");
                                    sbResult.append("<input type='hidden' value='aval' id='ticketValide' name='ticketValide' class='ticketValide'/>");
                                }
                            } else {
                                sbResult.append("<input type='checkbox' id='E" + idAnomalie + "' class='active_checkbox'");
                                sbResult.append("/>");
                                sbResult.append("<label for='E" + idAnomalie + "' class='css-label bout1'></label>");
                                sbResult.append("<input type='hidden' value='aval' id='ticketValide' name='ticketValide' class='ticketValide'/>");
                            }
                        } else if (i == 1) {
                            if (pisteAuditAllAnomaliesMap.containsKey(idAnomalie)) {
                                if (pisteAuditAllAnomaliesMap.get(idAnomalie).containsKey(2)) {
                                    AuditAnomalie auditAnomalie = (AuditAnomalie) pisteAuditAllAnomaliesMap.get(idAnomalie).get(2);
                                    sbResult.append(auditAnomalie.getAuthoriser() + "<br>" + auditAnomalie.getDateAuth());
                                }
                            }
                        }
                    } else {
                        if (i == 0) {
                            if (pisteAuditAllAnomaliesMap.containsKey(idAnomalie)) {
                                if (pisteAuditAllAnomaliesMap.get(idAnomalie).containsKey(2)) {
                                    sbResult.append("<center><img src='images/valider.png'  height='22' width='22' margin-left='40%'></center>");
                                } else {
                                    sbResult.append("<center><img src='images/bloquer.png'  height='22' width='22'></center>");
                                }
                            } else {
                                sbResult.append("<center><img src='images/bloquer.png'  height='22' width='22'></center>");
                            }
                        } else if (i == 1) {
                            if (pisteAuditAllAnomaliesMap.containsKey(idAnomalie)) {
                                if (pisteAuditAllAnomaliesMap.get(idAnomalie).containsKey(2)) {
                                    AuditAnomalie auditAnomalie = (AuditAnomalie) pisteAuditAllAnomaliesMap.get(idAnomalie).get(2);
                                    sbResult.append(auditAnomalie.getAuthoriser() + "<br>" + auditAnomalie.getDateAuth());
                                }
                            }
                        }
                    }
                    sbResult.append("</td>");
                    //******************************** fin checkbox aval MOE ********************************

                    //******************************** debut checkbox rejet *********************************                    
                    sbResult.append("<td style='padding-bottom: 2px; padding-top: 2px;  margin: auto; padding-left:0px;'>");
                    if (authorizedMoA || authorizedMoE) {
                        if (i == 0) {
                            sbResult.append("<input type='checkbox' id='R" + idAnomalie + "' class='active_checkbox'");
                            sbResult.append("/>");
                            sbResult.append("<label for='R" + idAnomalie + "' class='css-label bout2'></label>");
                            sbResult.append("<input type='hidden' value='rejet' id='ticketRejet' name='ticketRejet' class='ticketRejet'/>");
                        } else if (i == 2) {
                            if (pisteAuditAllAnomaliesMap.containsKey(idAnomalie)) {
                                if (pisteAuditAllAnomaliesMap.get(idAnomalie).containsKey(0)) {
                                    AuditAnomalie auditAnomalie = (AuditAnomalie) pisteAuditAllAnomaliesMap.get(idAnomalie).get(0);
                                    sbResult.append(auditAnomalie.getAuthoriser() + "<br>" + auditAnomalie.getDateAuth());
                                }
                            }
                        }
                    } else {
                        if (i == 0) {
                            sbResult.append("<center><img src='images/trash.png'  height='22' width='22'></center>");
                        } else if (i == 2) {
                            if (pisteAuditAllAnomaliesMap.containsKey(idAnomalie)) {
                                if (pisteAuditAllAnomaliesMap.get(idAnomalie).containsKey(0)) {
                                    AuditAnomalie auditAnomalie = (AuditAnomalie) pisteAuditAllAnomaliesMap.get(idAnomalie).get(0);
                                    sbResult.append(auditAnomalie.getAuthoriser() + "<br>" + auditAnomalie.getDateAuth());
                                }
                            }
                        }
                    }
                    sbResult.append("</td>");
                    //******************************** fin checkbox rejet ********************************

                    sbResult.append("</tr>");
                }
            } else {
                sbResult.append("<tr><td colspan='" + largeur + "'>Aucun ticket a traiter</td></tr>");
            }
        }
        sbResult.append("</tbody>");
        sbResult.append("</table>");
        sbResult.append("<br>");
        if (authorizedMoA || authorizedMoE) {
            sbResult.append("<p><center><span id='messageResultatPersist' class='vert clignotant'></span></center></p>");
            sbResult.append("<div class='center'>");
            sbResult.append("<input type='button' class='boutonValiderStandard' id='validerTransfertHfVersPROD' value='Autoriser la prise en charge HOTFIX' onclick='validerTickets();' />");
            sbResult.append("</div>");
        }
        return sbResult.toString();
    }

    //mise à jour des tickets (trac + piste audit) après validation de la page
    private String updateTickets(String ticketsAvalMOA, String ticketsAvalMOE, String ticketsRejet, String motifRejet) {
        String resultat = "";
        List<AuditAnomalie> AnomalieHfToBePersistedList = new ArrayList<AuditAnomalie>();
        boolean resultatValidation = true;
        DataBaseTools dbToolsMySql = new DataBaseTools(Configuration.puOvTools);
        //get connected user
        String connectedUser = Tools.getConnectedLogin();
        //GET all tickets from Audit (Audit)
        Map<Integer, Map<Integer, AuditAnomalie>> pisteAuditAllAnomaliesMap = getPisteAuditAllAnomalie();
        try {
            List<Integer> AnomalieAcceptedMoaList = new ArrayList<Integer>();
            //******************************** update mysql AvalMOA ********************************
            if (!ticketsAvalMOA.isEmpty()) {
                String[] tabAuxAval = ticketsAvalMOA.split("A");
                for (String idAnomalie : tabAuxAval) {
                    if (!idAnomalie.isEmpty()) {
                        AnomalieAcceptedMoaList.add(Integer.parseInt(idAnomalie));
                        AuditAnomaliePK auditAnomaliePK = new AuditAnomaliePK();
                        auditAnomaliePK.setIdAnomalie(Integer.parseInt(idAnomalie));
                        auditAnomaliePK.setEtatAuth(1);
                        AuditAnomalie auditAnomalie = new AuditAnomalie();
                        auditAnomalie.setAuditAnomaliePK(auditAnomaliePK);
                        auditAnomalie.setAuthoriser(connectedUser);
                        auditAnomalie.setDateAuth(ConvertTimeTracToJavaDate(System.currentTimeMillis()));
                        auditAnomalie.setMotifRejet("");
                        AnomalieHfToBePersistedList.add(auditAnomalie);
                    }
                }
                Map<Integer, Map<String, Object>> ticketAnomaliesAcceptedMoaMap = Tools.analyseTicketsAnomalies(AnomalieAcceptedMoaList);
                //******************************** update trac MOA ********************************
                for (String idAnomalie : tabAuxAval) {
                    if (!idAnomalie.isEmpty()) {
                        Map<String, Object> ticketCustomDetailsAux = ticketAnomaliesAcceptedMoaMap.get(Integer.parseInt(idAnomalie));
                        Ticket ticketAnomalies = (Ticket) (ticketCustomDetailsAux.get("Ticket"));
                        String pu = Configuration.puAnomalies;
                        int idTicketAnomalie = ticketAnomalies.getId();
                        String messageTrac = "== Ticket accepté par le responsable MOA ==";
                        String newOwner = null;
                        String newPriority = null;
                        String newStatus = null;
                        String newVersion = null;
                        //******************************** insertion trac custom field aval_moa ********************************
                        List<CoupleDTO> customFieldList = null;
                        customFieldList = new ArrayList<CoupleDTO>();
                        customFieldList.add(new CoupleDTO("aval_moa", connectedUser));
                        DataBaseTracGenericRequests.updateTicketTracGeneral(pu, idTicketAnomalie, connectedUser, messageTrac, newOwner, newPriority, newStatus, newVersion, customFieldList);

                        //******************************** send notification acceptance MOA ********************************
                        boolean existeDansPisteAudit = pisteAuditAllAnomaliesMap.containsKey(Integer.parseInt(idAnomalie));
                        if ((!existeDansPisteAudit) || (existeDansPisteAudit && !pisteAuditAllAnomaliesMap.get(Integer.parseInt(idAnomalie)).containsKey(2))) {

                            String oldVersion = ticketAnomalies.getVersion();
                            String oldType = ticketAnomalies.getType();
                            String oldOwner = ticketAnomalies.getOwner();
                            String oldReporter = ticketAnomalies.getReporter();
                            String motivationHotfix = ticketCustomDetailsAux.get("motivation_hf").toString();

                            String objet = "HOTFIX #" + idTicketAnomalie + " en attente de validation par le responsable MOE";
                            String mailOwner = "";
                            String mailReporter = "";
                            String mailAuthoriser = "";
                            //get mail Owner
                            try {
                                if (Configuration.usersMap.containsKey(oldOwner)) {
                                    mailOwner = Configuration.usersMap.get(oldOwner).getEmail();
                                } else {
                                    Tools.traiterException("Ticket ANOMALIE:" + ticketAnomalies.getId() + "\n\n\n" + "\nl utilisateur : " + oldOwner + " n est pas declare dans la base de donnees de parametrages, veuillez l ajouter");
                                }
                            } catch (Exception e1) {
                                Tools.traiterException("Ticket ANOMALIE:" + ticketAnomalies.getId() + "\n\n\n" + tools.Tools.getStackTrace(e1) + "\nl utilisateur : " + oldOwner + " n est pas declare dans la base de donnees de parametrages, veuillez l ajouter");
                            }

                            //get mail Reporter
                            try {
                                if (Configuration.usersMap.containsKey(oldReporter)) {
                                    mailReporter = Configuration.usersMap.get(oldReporter).getEmail();
                                } else {
                                    Tools.traiterException("Ticket ANOMALIE:" + ticketAnomalies.getId() + "\n\n\n" + "\nl utilisateur : " + oldReporter + " n est pas declare dans la base de donnees de parametrages, veuillez l ajouter");
                                }
                            } catch (Exception e1) {
                                Tools.traiterException("Ticket ANOMALIE:" + ticketAnomalies.getId() + "\n\n\n" + tools.Tools.getStackTrace(e1) + "\nl utilisateur : " + oldReporter + " n est pas declare dans la base de donnees de parametrages, veuillez l ajouter");
                            }

                            //get mail Authoriser
                            try {
                                if (Configuration.usersMap.containsKey(connectedUser)) {
                                    mailAuthoriser = Configuration.usersMap.get(connectedUser).getEmail();
                                } else {
                                    Tools.traiterException("Ticket ANOMALIE:" + ticketAnomalies.getId() + "\n\n\n" + "\nl utilisateur : " + connectedUser + " n est pas declare dans la base de donnees de parametrages, veuillez l ajouter");
                                }
                            } catch (Exception e1) {
                                Tools.traiterException("Ticket ANOMALIE:" + ticketAnomalies.getId() + "\n\n\n" + tools.Tools.getStackTrace(e1) + "\nl utilisateur : " + connectedUser + " n est pas declare dans la base de donnees de parametrages, veuillez l ajouter");
                            }

                            String sender = "OV.Management.Solutions@biat.com.tn";
                            //TEST
                            //String[] to = {"safwen.bensaid@biat.com.tn"};
                            //String[] cc = {"safwen.bensaid@biat.com.tn"};
                            ///////////////////////////////////

                            //PROD
                            String[] to = {"mohsen.ouertani@biat.com.tn", "ferjani.riahi@biat.com.tn", "abdelkhalek.chaari@biat.com.tn", "moncef.mallek@biat.com.tn"};
                            String[] cc = {"fares.hamza@biat.com.tn", "molka.borchani@biat.com.tn", "C24OV@biat.com.tn", mailReporter, mailOwner, mailAuthoriser};
                            ///////////////////////////////////
                            String[] bcc = new String[]{};
                            String emailContent = generateNotificationHtml("avalMOA", connectedUser, idTicketAnomalie, "", oldVersion, oldType, motivationHotfix);
                            try {
                                sendEMail(objet, sender, to, cc, bcc, emailContent, false);
                            } catch (Exception exep) {
                                Tools.traiterException("Problème envoi email notification rejet:\n" + tools.Tools.getStackTrace(exep));
                            }
                        }
                    }
                }
            }


            List<Integer> AnomalieAcceptedMoeList = new ArrayList<Integer>();
            //******************************** update mysql AvalMOE ********************************
            if (!ticketsAvalMOE.isEmpty()) {
                String[] tabAuxAval = ticketsAvalMOE.split("E");
                for (String idAnomalie : tabAuxAval) {
                    if (!idAnomalie.isEmpty()) {
                        AnomalieAcceptedMoeList.add(Integer.parseInt(idAnomalie));
                        AuditAnomaliePK auditAnomaliePK = new AuditAnomaliePK();
                        auditAnomaliePK.setIdAnomalie(Integer.parseInt(idAnomalie));
                        auditAnomaliePK.setEtatAuth(2);

                        AuditAnomalie auditAnomalie = new AuditAnomalie();
                        auditAnomalie.setAuditAnomaliePK(auditAnomaliePK);
                        auditAnomalie.setAuthoriser(connectedUser);
                        auditAnomalie.setDateAuth(ConvertTimeTracToJavaDate(System.currentTimeMillis()));
                        auditAnomalie.setMotifRejet("");

                        AnomalieHfToBePersistedList.add(auditAnomalie);
                    }
                }

                Map<Integer, Map<String, Object>> ticketAnomaliesAcceptedMoeMap = Tools.analyseTicketsAnomalies(AnomalieAcceptedMoeList);
                //******************************** update trac MOE ********************************
                for (String idAnomalie : tabAuxAval) {
                    if (!idAnomalie.isEmpty()) {
                        Map<String, Object> ticketCustomDetailsAux = ticketAnomaliesAcceptedMoeMap.get(Integer.parseInt(idAnomalie));
                        Ticket ticketAnomalies = (Ticket) (ticketCustomDetailsAux.get("Ticket"));
                        String pu = Configuration.puAnomalies;
                        int idTicketAnomalie = ticketAnomalies.getId();
                        String messageTrac = "== Ticket accepté par le responsable MOE ==";
                        String newOwner = null;
                        String newPriority = null;
                        String newStatus = null;
                        String newVersion = null;
                        //******************************** insertion trac custom field aval_moe ********************************
                        List<CoupleDTO> customFieldList = null;
                        customFieldList = new ArrayList<CoupleDTO>();
                        customFieldList.add(new CoupleDTO("aval_moe", connectedUser));
                        DataBaseTracGenericRequests.updateTicketTracGeneral(pu, idTicketAnomalie, connectedUser, messageTrac, newOwner, newPriority, newStatus, newVersion, customFieldList);

                        //******************************** send notification acceptance MOE ********************************
                        boolean existeDansPisteAudit = pisteAuditAllAnomaliesMap.containsKey(Integer.parseInt(idAnomalie));
                        if ((!existeDansPisteAudit) || (existeDansPisteAudit && !pisteAuditAllAnomaliesMap.get(Integer.parseInt(idAnomalie)).containsKey(1))) {
                            String oldVersion = ticketAnomalies.getVersion();
                            String oldType = ticketAnomalies.getType();
                            String oldOwner = ticketAnomalies.getOwner();
                            String oldReporter = ticketAnomalies.getReporter();
                            String motivationHotfix = ticketCustomDetailsAux.get("motivation_hf").toString();

                            String objet = "HOTFIX #" + idTicketAnomalie + " en attente de validation par le responsable MOA";
                            String mailOwner = "";
                            String mailReporter = "";
                            String mailAuthoriser = "";

                            //get mail Owner
                            try {
                                if (Configuration.usersMap.containsKey(oldOwner)) {
                                    mailOwner = Configuration.usersMap.get(oldOwner).getEmail();
                                } else {
                                    Tools.traiterException("Ticket ANOMALIE:" + ticketAnomalies.getId() + "\n\n\n" + "\nl utilisateur : " + oldOwner + " n est pas declare dans la base de donnees de parametrages, veuillez l ajouter");
                                }
                            } catch (Exception e1) {
                                Tools.traiterException("Ticket ANOMALIE:" + ticketAnomalies.getId() + "\n\n\n" + tools.Tools.getStackTrace(e1) + "\nl utilisateur : " + oldOwner + " n est pas declare dans la base de donnees de parametrages, veuillez l ajouter");
                            }

                            //get mail Reporter
                            try {
                                if (Configuration.usersMap.containsKey(oldReporter)) {
                                    mailReporter = Configuration.usersMap.get(oldReporter).getEmail();
                                } else {
                                    Tools.traiterException("Ticket ANOMALIE:" + ticketAnomalies.getId() + "\n\n\n" + "\nl utilisateur : " + oldReporter + " n est pas declare dans la base de donnees de parametrages, veuillez l ajouter");
                                }
                            } catch (Exception e1) {
                                Tools.traiterException("Ticket ANOMALIE:" + ticketAnomalies.getId() + "\n\n\n" + tools.Tools.getStackTrace(e1) + "\nl utilisateur : " + oldReporter + " n est pas declare dans la base de donnees de parametrages, veuillez l ajouter");
                            }

                            //get mail Authoriser
                            try {
                                if (Configuration.usersMap.containsKey(connectedUser)) {
                                    mailAuthoriser = Configuration.usersMap.get(connectedUser).getEmail();
                                } else {
                                    Tools.traiterException("Ticket ANOMALIE:" + ticketAnomalies.getId() + "\n\n\n" + "\nl utilisateur : " + connectedUser + " n est pas declare dans la base de donnees de parametrages, veuillez l ajouter");
                                }
                            } catch (Exception e1) {
                                Tools.traiterException("Ticket ANOMALIE:" + ticketAnomalies.getId() + "\n\n\n" + tools.Tools.getStackTrace(e1) + "\nl utilisateur : " + connectedUser + " n est pas declare dans la base de donnees de parametrages, veuillez l ajouter");
                            }

                            //Get mail Domaine by milestone
                            String getMailParMilestone = "";
                            String[] destinationPrincipale = new String[100];
                            try {
                                if (Configuration.allMilestonesEmailMap.containsKey(ticketAnomalies.getMilestone())) {
                                    getMailParMilestone = Configuration.allMilestonesEmailMap.get(ticketAnomalies.getMilestone());
                                    destinationPrincipale = getMailParMilestone.split(" ");
                                } else {
                                    Tools.traiterException("le milestone :" + ticketAnomalies.getMilestone() + " n est pas declare dans la base de donnees de parametrages, veuillez l'ajouter");
                                    destinationPrincipale[0] = mailReporter;
                                }
                            } catch (Exception e1) {
                                Tools.traiterException("le milestone :" + ticketAnomalies.getMilestone() + " n est pas declare dans la base de donnees de parametrages, veuillez l'ajouter");
                            }

                            String sender = "OV.Management.Solutions@biat.com.tn";

                            //TEST
                            //String[] to = {"safwen.bensaid@biat.com.tn"};
                            //String[] cc = {"safwen.bensaid@biat.com.tn"};
                            ///////////////////////////////////

                            //PROD
                            String[] to = destinationPrincipale;
                            String[] cc = {"fares.hamza@biat.com.tn", "molka.borchani@biat.com.tn", "C24OV@biat.com.tn", "mohsen.ouertani@biat.com.tn", "ferjani.riahi@biat.com.tn", mailReporter, mailOwner, mailAuthoriser};
                            ///////////////////////////////////

                            String[] bcc = new String[]{};
                            String emailContent = null;
                            if (!oldType.equals("TECHNIQUE")) {
                                emailContent = generateNotificationHtml("avalMOE", connectedUser, idTicketAnomalie, "", oldVersion, oldType, motivationHotfix);
                            }
                            try {
                                sendEMail(objet, sender, to, cc, bcc, emailContent, false);
                            } catch (Exception exep) {
                                Tools.traiterException("Problème envoi email notification rejet:\n" + tools.Tools.getStackTrace(exep));
                            }
                        }
                    }
                }
            }

            List<Integer> AnomalieRejectedList = new ArrayList<Integer>();
            //******************************** update mysql Rejet ********************************
            if (!ticketsRejet.isEmpty()) {
                String[] tabAuxRejet = ticketsRejet.split("R");
                for (String idAnomalie : tabAuxRejet) {
                    if (!idAnomalie.isEmpty()) {
                        AnomalieRejectedList.add(Integer.parseInt(idAnomalie));

                        AuditAnomaliePK auditAnomaliePK = new AuditAnomaliePK();
                        auditAnomaliePK.setIdAnomalie(Integer.parseInt(idAnomalie));
                        auditAnomaliePK.setEtatAuth(0);

                        AuditAnomalie auditAnomalie = new AuditAnomalie();
                        auditAnomalie.setAuditAnomaliePK(auditAnomaliePK);
                        auditAnomalie.setAuthoriser(connectedUser);
                        auditAnomalie.setDateAuth(ConvertTimeTracToJavaDate(System.currentTimeMillis()));
                        auditAnomalie.setMotifRejet(motifRejet);
                        AnomalieHfToBePersistedList.add(auditAnomalie);
                    }
                }

                Map<Integer, Map<String, Object>> ticketAnomaliesRejectedMap = Tools.analyseTicketsAnomalies(AnomalieRejectedList);
                //******************************** update trac Rejet ********************************
                for (String idAnomalie : tabAuxRejet) {
                    if (!idAnomalie.isEmpty()) {
                        Map<String, Object> ticketCustomDetailsAux = ticketAnomaliesRejectedMap.get(Integer.parseInt(idAnomalie));
                        Ticket ticketAnomalies = (Ticket) (ticketCustomDetailsAux.get("Ticket"));
                        Integer idTicketAnomalie = ticketAnomalies.getId();
                        //******************************** send notification rejet ********************************
                        String oldVersion = ticketAnomalies.getVersion();
                        String oldType = ticketAnomalies.getType();
                        String oldOwner = ticketAnomalies.getOwner();
                        String oldReporter = ticketAnomalies.getReporter();
                        String objet = "Ticket " + oldVersion.replace(" POUR MISE EN PROD", "") + " (" + oldType + ") " + " #" + idTicketAnomalie + " rejeté par le comité release"; //$3

                        String mailOwner = "";
                        String mailReporter = "";
                        try {
                            mailOwner = Configuration.usersMap.get(oldOwner).getEmail();
                        } catch (Exception e1) {
                            Tools.traiterException("Ticket ANOMALIE:" + ticketAnomalies.getId() + "\n\n\n" + tools.Tools.getStackTrace(e1) + "\nl utilisateur : " + oldOwner + " n est pas declare dans la base de donnees de parametrages, veuillez l ajouter");
                        }
                        try {
                            mailReporter = Configuration.usersMap.get(oldReporter).getEmail();
                        } catch (Exception e1) {
                            Tools.traiterException("Ticket ANOMALIE:" + ticketAnomalies.getId() + "\n\n\n" + tools.Tools.getStackTrace(e1) + "\nl utilisateur : " + oldReporter + " n est pas declare dans la base de donnees de parametrages, veuillez l ajouter");
                        }
                        String sender = "OV.Management.Solutions@biat.com.tn";
                        String[] to = {mailReporter, mailOwner};
                        String[] cc = {"C24OV@biat.com.tn"};
                        String[] bcc = new String[]{};
                        String emailContent = generateNotificationHtml("Rejet", "", idTicketAnomalie, motifRejet, oldVersion, oldType, "");
                        try {
                            sendEMail(objet, sender, to, cc, bcc, emailContent, false);
                        } catch (Exception exep) {
                            Tools.traiterException("Problème envoi email notification rejet:\n" + tools.Tools.getStackTrace(exep));
                        }
                        //set owner and version
                        String pu = Configuration.puAnomalies;
                        String messageTrac = "== Motif de rejet: == \n [[BR]]" + motifRejet;
                        String newOwner = oldReporter;
                        String newPriority = null;
                        String newStatus = null;
                        String newVersion = "A REVISER";
                        List<CoupleDTO> customFieldList = null;
                        DataBaseTracGenericRequests.updateTicketTracGeneral(pu, idTicketAnomalie, connectedUser, messageTrac, newOwner, newPriority, newStatus, newVersion, customFieldList);
                    }
                }
            }
            dbToolsMySql.updateObjectList(AnomalieHfToBePersistedList);
        } catch (Exception ex) {
            ex.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(ex));
            resultatValidation = false;
        } finally {
            dbToolsMySql.closeRessources();
        }

        if (resultatValidation) {
            resultat = "mise à  jour des tickets effetuée avec succés";
        } else {
            resultat = "echec de mise à  jour des tickets des tickets";
        }
        return resultat;
    }

    private String generateNotificationHtml(String typeNotification, String authoriser, Integer idTicketAnomalie, String motifRejet, String version, String type, String motivationHotfix) {
        StringBuilder sbResult = new StringBuilder();

        sbResult.append("\n<p style='margin:0cm;margin-bottom:.0001pt;background:white'><b><span style='font-size:10.0pt;font-family:Arial,sans-serif;color:#1f497d'>");
        sbResult.append("\nBonjour,");
        sbResult.append("\n</span></b><span style='font-size:10pt;font-family:Arial,sans-serif'></span></p>");
        sbResult.append("\n<br>");
        sbResult.append("\n<br>");
        if (typeNotification.equals("Rejet")) {
            sbResult.append("\nNous vous informons que l'anomalie ").append(version).append(" (").append(type).append(") " + "<a href='http://172.28.70.74/trac/anomalies_t24/ticket/").append(idTicketAnomalie).append("'>#").append(idTicketAnomalie).append("</a> a été rejetée par le comité release pour le motif suivant: ").append(motifRejet);
        } else if (typeNotification.equals("avalMOA")) {
            sbResult.append("\nNous vous informons que le HOTFIX " + "<a href='http://172.28.70.74/trac/anomalies_t24/ticket/").append(idTicketAnomalie).append("'>#").append(idTicketAnomalie).append("</a> vient d'être autorisé par le responsable MOA Mr ").append(authoriser.toUpperCase().replace(".", " ")).append(", <span style='text-decoration:underline;'> reste à statuer sur sa validation définitive par le responsable <b> MOE </b> via la plateforme <a href='http://172.28.70.74:8090/OVTOOLS/AutorisationDesHotfix.do'>OVTOOLS.</a> </span>");
        } else if (typeNotification.equals("avalMOE")) {
            sbResult.append("\nNous vous informons que le HOTFIX " + "<a href='http://172.28.70.74/trac/anomalies_t24/ticket/").append(idTicketAnomalie).append("'>#").append(idTicketAnomalie).append("</a> vient d'être autorisé par le responsable MOE Mr ").append(authoriser.toUpperCase().replace(".", " ")).append(", <span style='text-decoration:underline;'> reste à statuer sur sa validation définitive par le responsable <b> MOA </b> via la plateforme <a href='http://172.28.70.74:8090/OVTOOLS/AutorisationDesHotfix.do'>OVTOOLS.</a> </span>");
        }
        sbResult.append("\n</p>");
        if (typeNotification.equals("Rejet")) {
            sbResult.append("\n<p>");
            sbResult.append("\nNB: Le champ VERSION a été changé 'A REVISER' sur lr ticket TRAC.");
            sbResult.append("\n</p>");
        } else if (typeNotification.equals("avalMOA") || typeNotification.equals("avalMOE") || !motivationHotfix.contains("Actions à chaud curatives")) {
            sbResult.append("\n<p>");
            sbResult.append("\n<i><span style='text-decoration:underline;'>Motivation de l'urgence: </span><i><b>").append(motivationHotfix).append("</b>");
            sbResult.append("\n</p>");
        }
        sbResult.append("\n<br>");
        sbResult.append("\n<br>");
        sbResult.append("\n</table>");
        sbResult.append("\n");
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
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(AutorisationHotfixServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(AutorisationHotfixServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
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
