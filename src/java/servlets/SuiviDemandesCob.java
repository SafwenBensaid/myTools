/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import dataBaseTracRequests.DataBaseTracGenericRequests;
import dataBaseTracRequests.AppelRequetes;
import dto.CoupleDTO;
import entitiesTrac.Ticket;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tools.Configuration;
import tools.GetHotfixesPipesTool;
import tools.Tools;
import static tools.Tools.sendEMail;
import tools.UpdateTicketsTools;

/**
 *
 * @author 04486
 */
public class SuiviDemandesCob extends HttpServlet {

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

            List<Integer> listTicketsCob = new DataBaseTracGenericRequests<Integer>().getList_TYPE_OfnamedQuery(Configuration.puAnomalies, "TicketCustom.findAllCobs", null);

            if (action.equals("load")) {
                String[] cles = new String[]{"action", "env_cob", "heure_cob", "nex_w_day", "holiday", "date_deb_cob"};
                Map<Integer, Map<String, Object>> cobTicketsDetails = AppelRequetes.getTicketCustomByTicketIdAndNames(listTicketsCob, Configuration.puAnomalies, Configuration.tracAnomalies, cles);
                Map<String, List<Map<String, Object>>> mapCobsTriee = new GetHotfixesPipesTool().triCobsSelonType(cobTicketsDetails);
                sendNotificationNouvelleDemandeCOB(mapCobsTriee);
                Tools tools = new Tools();
                boolean hasRoleValidationCOB = tools.hasRole("VALIDATION_HOTFIX");
                resultat = getHtmlCobTable(hasRoleValidationCOB, mapCobsTriee);

            } else {
                //recuperer les checked box (html)
                String ticketsAvalider = request.getParameter("ticketsAvalider");
                String ticketsAppliqes = request.getParameter("ticketsAppliqes");
                //submit les mise a jour 
                resultat = updateTickets(ticketsAvalider, ticketsAppliqes, listTicketsCob);
            }
            out.println(resultat);
        } finally {
            out.close();
        }
    }

    private String updateTickets(String ticketsAvalider, String ticketsAppliqes, List<Integer> listTicketsCob) {
        String resultat = "";
        try {
            List<Object> objectsListToBeInsertedOnTheDataBaseTracAnomalie = new ArrayList<Object>();
            UpdateTicketsTools utt = new UpdateTicketsTools();
            //get time trac
            //get connected user
            String connectedUser = Tools.getConnectedLogin();

            String[] cles = new String[]{"action", "env_cob", "heure_cob", "nex_w_day", "holiday"};
            Map<Integer, Map<String, Object>> cobTicketsDetails = AppelRequetes.getTicketCustomByTicketIdAndNames(listTicketsCob, Configuration.puAnomalies, Configuration.tracAnomalies, cles);
            if (!ticketsAvalider.isEmpty()) {
                String[] tabAuxAvalider = ticketsAvalider.split("V");
                for (String idAnomalie : tabAuxAvalider) {
                    if (!idAnomalie.isEmpty()) {
                        Map<String, Object> mapDetails = cobTicketsDetails.get(Integer.parseInt(idAnomalie));
                        Ticket tickAnomalie = (Ticket) mapDetails.get("Ticket");
                        String pu = Configuration.puAnomalies;
                        int idTicketAnomalie = tickAnomalie.getId();
                        String messageTrac = "== Demande de COB Validée ==";
                        String newOwner = null;
                        String newPriority = null;
                        String newStatus = null;
                        String newVersion = null;
                        List<CoupleDTO> customFieldList = new ArrayList<CoupleDTO>();
                        customFieldList.add(new CoupleDTO("action", "COB PRET POUR APPLICATION"));
                        //Mise à jour du champs action = 'COB PRET POUR APPLICATION'
                        DataBaseTracGenericRequests.updateTicketTracGeneral(pu, idTicketAnomalie, connectedUser, messageTrac, newOwner, newPriority, newStatus, newVersion, customFieldList);

                        //******************************** send notification COB validé ********************************
                        String oldOwner = tickAnomalie.getOwner();
                        String oldReporter = tickAnomalie.getReporter();

                        String env_cob = mapDetails.get("env_cob").toString();
                        String nex_w_day = mapDetails.get("nex_w_day").toString();
                        String holiday = mapDetails.get("holiday").toString();
                        String heure_cob = mapDetails.get("heure_cob").toString();

                        //formattage date 
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                        //récupération de la date courante 
                        Date currentTime = new Date();
                        //on crée la chaîne à partir de la date  
                        String today = formatter.format(currentTime);

                        String objet = "Exécution d'un COB sur l'environnement de test " + env_cob + "  à la date du " + today + " #" + idTicketAnomalie;
                        String mailReporter = "";
                        //get mail Reporter
                        try {
                            if (Configuration.usersMap.containsKey(oldReporter)) {
                                mailReporter = Configuration.usersMap.get(oldReporter).getEmail();
                            } else {
                                Tools.traiterException("Ticket ANOMALIE:" + idTicketAnomalie + "\n\n\n" + "\nl utilisateur : " + oldReporter + " n est pas declare dans la base de donnees de parametrages, veuillez l ajouter");
                            }
                        } catch (Exception e1) {
                            Tools.traiterException("Ticket ANOMALIE:" + idTicketAnomalie + "\n\n\n" + tools.Tools.getStackTrace(e1) + "\nl utilisateur : " + oldReporter + " n est pas declare dans la base de donnees de parametrages, veuillez l ajouter");
                        }
                        String sender = "OV.Management.Solutions@biat.com.tn";
                        //TEST
                        //String[] to = {"safwen.bensaid@biat.com.tn"};
                        //String[] cc = {"safwen.bensaid@biat.com.tn"};
                        ///////////////////////////////////

                        //PROD
                        String[] to = {"mongi.guesmi@biat.com.tn", "riadh.bendakhlia@biat.com.tn", "fethi.cheikh@biat.com.tn", "SITEC@biat.com.tn", "DIE@biat.com.tn"};
                        String[] cc = {"CH_Metier@biat.com.tn", "Pilier.technique@biat.com.tn", "CH_DirProj@biat.com.tn", "C24OV_GRP@biat.com.tn", mailReporter};
                        ///////////////////////////////////
                        String[] bcc = new String[]{};
                        String emailContent = generateNotificationHtml("COB_VALIDE", connectedUser, idTicketAnomalie, env_cob, heure_cob, nex_w_day, holiday, oldReporter, today);
                        try {
                            sendEMail(objet, sender, to, cc, bcc, emailContent, false);
                        } catch (Exception ex) {
                            Tools.traiterException("Problème envoi email notification COB:\n" + ex.toString());
                        }
                    }
                }
                resultat = "Demande de COB validée. Envoi de notification en cours.";
            }

            if (!ticketsAppliqes.isEmpty()) {
                String[] tabAuxAppliqes = ticketsAppliqes.split("A");
                for (String idAnomalie : tabAuxAppliqes) {
                    if (!idAnomalie.isEmpty()) {
                        Map<String, Object> mapDetails = cobTicketsDetails.get(Integer.parseInt(idAnomalie));
                        Ticket ticketAnomalies = (Ticket) mapDetails.get("Ticket");
                        String pu = Configuration.puAnomalies;
                        int idTicketAnomalie = ticketAnomalies.getId();
                        String messageTrac = "== COB APPLIQUE ==";
                        String newOwner = null;
                        String newPriority = null;
                        String newStatus = null;
                        String newVersion = null;
                        List<CoupleDTO> customFieldList = new ArrayList<CoupleDTO>();
                        customFieldList.add(new CoupleDTO("action", "COB APPLIQUE"));
                        //Mise à jour du champs action = 'COB APPLIQUE'
                        DataBaseTracGenericRequests.updateTicketTracGeneral(pu, idTicketAnomalie, connectedUser, messageTrac, newOwner, newPriority, newStatus, newVersion, customFieldList);
                    }
                }
                resultat = "COB appliqué et tracé sur TRAC";
            }
        } catch (Exception ex) {
            Tools.traiterException("Problème validation COB:\n" + ex.toString());
        }
        return resultat;
    }

    private void sendNotificationNouvelleDemandeCOB(Map<String, List<Map<String, Object>>> mapCobsTriee) {
        if (mapCobsTriee.containsKey("DEMANDE_COB") && mapCobsTriee.get("DEMANDE_COB").size() > 0) {
            for (Map<String, Object> mapDetails : mapCobsTriee.get("DEMANDE_COB")) {
                Ticket tickAnomalie = (Ticket) mapDetails.get("Ticket");
                if (!tickAnomalie.getStatus().equals("accepted")) {
                    //******************************** send notification nouvelle demande COB ********************************
                    String oldOwner = tickAnomalie.getOwner();
                    String oldReporter = tickAnomalie.getReporter();
                    String env_cob = mapDetails.get("env_cob").toString();
                    String nex_w_day = mapDetails.get("nex_w_day").toString();
                    String holiday = mapDetails.get("holiday").toString();
                    String heure_cob = mapDetails.get("heure_cob").toString();
                    //get time trac
                    //get connected user
                    String connectedUser = Tools.getConnectedLogin();

                    //on crée l'objet en passant en paramétre une chaîne representant le format 
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    //récupération de la date courante 
                    Date currentTime = new Date();
                    //on crée la chaîne à partir de la date  
                    String today = formatter.format(currentTime);

                    String objet = "Nouvelle demande d'exécution d'un COB sur l'environnement de test " + env_cob + "  à la date du " + today + " #" + tickAnomalie.getId();
                    String mailOwner = "";
                    String mailReporter = "";
                    try {
                        mailOwner = Configuration.usersMap.get(oldOwner).getEmail();
                    } catch (Exception e1) {
                        Tools.traiterException("Ticket ANOMALIE:" + tickAnomalie.getId() + "\n\n\n" + tools.Tools.getStackTrace(e1) + "\nl utilisateur : " + oldOwner + " n est pas declare dans la base de donnees de parametrages, veuillez l ajouter");
                    }
                    try {
                        mailReporter = Configuration.usersMap.get(oldReporter).getEmail();
                    } catch (Exception e1) {
                        Tools.traiterException("Ticket ANOMALIE:" + tickAnomalie.getId() + "\n\n\n" + tools.Tools.getStackTrace(e1) + "\nl utilisateur : " + oldReporter + " n est pas declare dans la base de donnees de parametrages, veuillez l ajouter");
                    }
                    String sender = "OV.Management.Solutions@biat.com.tn";
                    //TEST
                    String[] to = {"safwen.bensaid@biat.com.tn"};
                    String[] cc = {"safwen.bensaid@biat.com.tn"};
                    ///////////////////////////////////

                    //PROD
                    //String[] to = {"C24OV@biat.com.tn"};
                    //String[] cc = {mailReporter};
                    ///////////////////////////////////
                    String[] bcc = new String[]{};
                    String emailContent = generateNotificationHtml("COB_DEMANDE", connectedUser, tickAnomalie.getId(), env_cob, heure_cob, nex_w_day, holiday, oldReporter, today);
                    try {
                        sendEMail(objet, sender, to, cc, bcc, emailContent, false);
                    } catch (Exception ex) {
                        Tools.traiterException("Problème envoi email notification nouvelle demande de COB:\n" + ex.toString());
                    }
                    String pu = Configuration.puAnomalies;
                    int numTicket = tickAnomalie.getId();
                    String messageTrac = "== Nouvelle demande de COB signalée à l'équipe OV ==";
                    String newOwner = "raafet dormok";
                    String newPriority = null;
                    String newStatus = "accepted";
                    String newVersion = null;
                    List<CoupleDTO> customFieldList = null;
                    DataBaseTracGenericRequests.updateTicketTracGeneral(pu, numTicket, connectedUser, messageTrac, newOwner, newPriority, newStatus, newVersion, customFieldList);
                }
            }
        }
    }

    private String generateNotificationHtml(String typeNotification, String authoriser, Integer idTicketAnomalie, String env_cob, String heure_cob, String nex_w_day, String holiday, String oldReporter, String today) {
        StringBuilder sbResult = new StringBuilder();

        sbResult.append("\n<p style='margin:0cm;margin-bottom:.0001pt;background:white'><b><span style='font-size:10.0pt;font-family:Arial,sans-serif;color:#1f497d'>");
        sbResult.append("\nBonjour,");
        sbResult.append("\n</span></b><span style='font-size:10pt;font-family:Arial,sans-serif'></span></p>");
        sbResult.append("<br>");
        if (typeNotification.equals("COB_VALIDE")) {
            sbResult.append("<br><p style='color:#1f497d'>Nous vous invitons à exécuter aujourd’hui le <b>").append(today).append("</b> un <b> COB </b> (avec $UNIVERS) sur l’environnement de test <b>").append(env_cob);
            if (!heure_cob.isEmpty()) {
                sbResult.append(" à partir de ").append(heure_cob);
            }
            sbResult.append("</b>.");
            sbResult.append("<br><br>La valeur <b>  NEXT WORKING DAY  = ").append(nex_w_day).append("</b>.");
        } else if (typeNotification.equals("COB_DEMANDE")) {
            sbResult.append("<br>Nouvelle demande de COB à exécuter aujourd’hui le ").append(today).append(" sur l’environnement de test ").append(env_cob);
            if (!heure_cob.isEmpty()) {
                sbResult.append(" à partir de ").append(heure_cob);
            }
            sbResult.append(".");
            sbResult.append("<br><br>La valeur <b>  NEXT WORKING DAY  = ").append(nex_w_day).append("</b>.");
        }
        sbResult.append("<br>");
        sbResult.append("<br>");
        sbResult.append("<br>L'exécution de ce COB a été demandée par Mr/Mme <i>").append(oldReporter.replace(".", " ").toUpperCase()).append("</i> (<b>voir ticket trac <a href='http://172.28.70.74/trac/anomalies_t24/ticket/").append(idTicketAnomalie).append("'>#").append(idTicketAnomalie).append("</a></b>)");
        sbResult.append("\n</p>");
        sbResult.append("\n<p style='font-size: 11px;color:#1f497d'>");
        sbResult.append("<br><br>Ceci est un message automatique, merci de ne pas y répondre.");
        sbResult.append("\n</p>");
        sbResult.append("\n--");
        sbResult.append("\n<p style='margin:0cm;margin-bottom:.0001pt;background:white'>");
        sbResult.append("\n    <b>");
        sbResult.append("\n        <span style='font-size:9.0pt;font-family:Arial,sans-serif;color:#1f497d'>");
        sbResult.append("\n            <a href='OV Management Solutions'>OV Management Solutions</a>");
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

    public String getHtmlCobTable(boolean authorizedAdmin, Map<String, List<Map<String, Object>>> mapCobsTriee) {
        int comp = 0;
        String cle = null;
        int largeur = 9;
        int nbrreportings = 4;
        String titre = "";
        StringBuilder sbResult = new StringBuilder();
        sbResult.append("<table id='tableTicketsHotfix' class='roundCornerTable'>");
        sbResult.append("<thead>");
        sbResult.append("<tr>");
        sbResult.append("<th>Nbr</th>");
        sbResult.append("<th>Demande</th>");
        sbResult.append("<th>Créée par</th>");
        sbResult.append("<th>Action</th>");
        sbResult.append("<th>Environnement</th>");
        sbResult.append("<th>Prochain jour ouvrable</th>");
        sbResult.append("<th>Holidays</th>");
        sbResult.append("<th>Validation_OV</th>");
        sbResult.append("<th>Confirmation_IE</th>");

        sbResult.append("</tr>");
        sbResult.append("</thead>");
        sbResult.append("<tbody style='background-color: white;'>");
        for (int i = 0; i < nbrreportings; i++) {
            if (i == 0) {
                sbResult.append("<tr class='titre'><td colspan='").append(largeur).append("'>Demandes de cob en cours</td></tr>");
                cle = "DEMANDE_COB";
            } else if (i == 1) {
                sbResult.append("<tr class='titre'><td colspan='").append(largeur).append("'>Demandes de cob validées</td></tr>");
                cle = "COB_PRET";
            } else if (i == 2) {
                sbResult.append("<tr class='titre'><td colspan='").append(largeur).append("'>Cobs appliqués</td></tr>");
                cle = "COB_APPLIQUE";
            } else if (i == 3) {
                sbResult.append("<tr class='titre'><td colspan='").append(largeur).append("'>Cobs annulés ou reportés</td></tr>");
                cle = "COB_ANNULE_REPORTE";
            }
            comp = 0;
            if (mapCobsTriee.containsKey(cle) && mapCobsTriee.get(cle).size() > 0) {
                for (Map<String, Object> mapDetails : mapCobsTriee.get(cle)) {
                    Ticket tickAnomalie = (Ticket) mapDetails.get("Ticket");
                    Integer idAnomalie = tickAnomalie.getId();
                    comp++;

                    //formattage date 
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    //récupération de la date courante 
                    Date currentTime = new Date();
                    //on crée la chaîne à partir de la date  
                    String today = formatter.format(currentTime);

                    if (!mapDetails.get("date_deb_cob").equals(today) && mapDetails.get("holiday").equals("")) {
                        sbResult.append("<tr>");
                    } else if (mapDetails.get("date_deb_cob").equals(today)) {
                        sbResult.append("<tr class='clignotant vert'>");
                    } else {
                        sbResult.append("<tr class='couleur5'>");
                    }

                    if (mapDetails.get("date_deb_cob").equals(today) && !mapDetails.get("holiday").equals("")) {
                        sbResult.append("<tr class='clignotantRouge rougeClair'>");
                    }

                    sbResult.append("<td>");
                    sbResult.append(comp).append(")");
                    sbResult.append("</td>");

                    sbResult.append("<td>");
                    sbResult.append("<a class='lien numAnmalie'  onclick=\"openTracTicketInNewTab($(this).html(),'ANOMALIE');\">");
                    sbResult.append("#").append(idAnomalie);
                    sbResult.append("</a>");
                    sbResult.append("</td>");

                    sbResult.append("<td>");
                    sbResult.append(tickAnomalie.getReporter());
                    sbResult.append("</td>");

                    sbResult.append("<td>");
                    sbResult.append(mapDetails.get("action"));
                    sbResult.append("</td>");

                    sbResult.append("<td>");
                    sbResult.append(mapDetails.get("env_cob"));
                    sbResult.append("</td>");

                    sbResult.append("<td>");
                    sbResult.append(mapDetails.get("nex_w_day"));
                    sbResult.append("</td>");


                    sbResult.append("<td style='padding-bottom: 2px; padding-top: 2px; margin: auto'>");
                    sbResult.append("<a class='conteneur_info_bull'>");
                    sbResult.append("<img class='info-icon' src='images/info.png' alt='info'>");
                    sbResult.append("<span>");
                    sbResult.append(mapDetails.get("holiday"));
                    sbResult.append("</span>");
                    sbResult.append("</a>");
                    sbResult.append("</td>");

                    sbResult.append("<td style='padding-top: 2px; padding-bottom: 2px; margin: auto' id='#" + idAnomalie + "'>");
                    if (i == 0) {
                        if (authorizedAdmin) {
                            sbResult.append("<input type='checkbox' name='" + idAnomalie + "' id='V" + idAnomalie + "' class='active_checkbox'");
                            sbResult.append("/>");
                            sbResult.append("<label for='V" + idAnomalie + "' class='css-label'></label>");
                            sbResult.append("<input type='hidden' value='' id='ticketValide' name='ticketValide' class='ticketValide'/>");
                        } else {
                            sbResult.append("<center><img src='images/bloquer.png'  height='22' width='22'></center>");
                        }
                    } else if (i == 1) {
                        sbResult.append("<center><img src='images/valider.png'  height='22' width='22'></center>");
                    }
                    sbResult.append("</td>");

                    sbResult.append("<td style='padding-top: 2px; padding-bottom: 2px; margin: auto' id='#" + idAnomalie + "'>");
                    if (i == 1) {
                        if (authorizedAdmin) {
                            sbResult.append("<input type='checkbox' name='" + idAnomalie + "' id='A" + idAnomalie + "' class='active_checkbox'");
                            sbResult.append("/>");
                            sbResult.append("<label for='A" + idAnomalie + "' class='css-label'></label>");
                            sbResult.append("<input type='hidden' value='' id='ticketValide' name='ticketValide' class='ticketValide'/>");
                        } else {
                            sbResult.append("<center><img src='images/bloquer.png'  height='22' width='22'></center>");
                        }
                    }
                    sbResult.append("</td>");
                    sbResult.append("</tr>");
                }
            } else {
                sbResult.append("<tr><td colspan='").append(largeur).append("'>Aucun ticket à traiter</td></tr>");
            }
        }
        sbResult.append("</tbody>");
        sbResult.append("</table>");

        if (authorizedAdmin) {
            sbResult.append("<p><center><span id='messageResultatPersist' class='vert clignotant'></span></center></p>");
            sbResult.append("<div class='center'>");
            sbResult.append("<br><br><input type='button' class='boutonValider' id='validerDemandeCob' value='Valider' onclick='validerTickets();' />");
            sbResult.append("</div>");
        }

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
