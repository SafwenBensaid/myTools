/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import dataBaseTracRequests.DataBaseTracRequests;
import entitiesMysql.Livraison;
import entitiesTrac.Ticket;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tools.*;

/**
 *
 * @author 04486
 */
public class RelanceHotfixServlet extends HttpServlet {

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
            Map<String, List<Livraison>> mapLivraisons = new GetHotfixesPipesTool().getAllHotfixes();
            //Get Anomalies des livraisons à relancer
            List<Integer> listTicketsAnomalie = new ArrayList<>();
            if (mapLivraisons.get("OV_HF_NON_QUALIFIEE").size() > 0) {
                for (Livraison liv : mapLivraisons.get("OV_HF_NON_QUALIFIEE")) {
                    listTicketsAnomalie.add(liv.getNumeroAnomalie());
                }
            }
            Map<Integer, Map<String, Object>> mapAnomalies = Tools.analyseTicketsAnomalies(listTicketsAnomalie);
            if (action.equals("load")) {
                resultat = getHtmlHotfixTable(mapLivraisons, mapAnomalies);
            } else {
                String ticketsValides = request.getParameter("tickets_valides");
                resultat = relacerAllHotfixes(ticketsValides, mapLivraisons);
            }
            out.println(resultat);
        } finally {
            out.close();
        }
    }

//modif    
    public String getHtmlHotfixTable(Map<String, List<Livraison>> mapLivraisons, Map<Integer, Map<String, Object>> mapAnomalies) {
        SimpleDateFormat parseFormatyyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat parseFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        parseFormat.setTimeZone(TimeZone.getTimeZone("GMT+1"));
        String dateStringAux;
        String dateSendProdStringAux;
        Tools tools = new Tools();
        String connectedUser = Tools.getConnectedLogin();
        int comp;
        String cle;
        int largeur;
        StringBuilder sbResult = new StringBuilder();
        sbResult.append("<table id='tableTicketsHotfix' class='roundCornerTable'>");
        sbResult.append("<thead>");
        sbResult.append("<tr>");
        sbResult.append("<th>Nbr</th>");
        sbResult.append("<th>Anomalie</th>");
        sbResult.append("<th>Livraison</th>");
        sbResult.append("<th>Type</th>");
        sbResult.append("<th>Owner</th>");
        sbResult.append("<th>reporter</th>");
        sbResult.append("<th>Envoyé le</th>");
        sbResult.append("<th>Déployé le</th>");
        sbResult.append("<th>Contenu des livrables</th>");
        sbResult.append("<th>Relancer</th>");
        largeur = 10;

        sbResult.append("</tr>");
        sbResult.append("</thead>");
        sbResult.append("<tbody>");
        sbResult.append("<tr class='titre'><td colspan='").append(largeur).append("'>Tickets Hotfix A relancer</td></tr>");
        cle = "OV_HF_NON_QUALIFIEE";
        comp = 0;
        List<String> illegibleAuthorisers = new ArrayList<>();
        if (mapLivraisons.get(cle).size() > 0) {
            for (Livraison liv : mapLivraisons.get(cle)) {
                comp++;
                dateStringAux = parseFormat.format(liv.getDateDeploiement());
                dateSendProdStringAux = parseFormat.format(liv.getDateEnvoiProd());
                if (Tools.isTimeToDo(parseFormatyyyyMMdd.format(liv.getDateEnvoiProd()), parseFormatyyyyMMdd.format(new Date()), "yyyy-MM-dd", false)) {
                    sbResult.append("<tr class='couleur5'>");
                } else {
                    sbResult.append("<tr>");
                }

                sbResult.append("<td>");
                sbResult.append(comp).append(")");
                sbResult.append("</td>");

                sbResult.append("<td>");
                sbResult.append("<a class='lien numAnmalie'  onclick=\"openTracTicketInNewTab($(this).html(),'ANOMALIE');\">");
                sbResult.append("#").append(liv.getNumeroAnomalie());
                sbResult.append("</a>");
                sbResult.append("</td>");

                sbResult.append("<td>");
                sbResult.append("<a class='lien numLivraison'   onclick=\"openTracTicketInNewTab($(this).html(),'LIVRAISON');\">");
                sbResult.append("#").append(liv.getNumeroLivraison());
                sbResult.append("</a>");
                sbResult.append("</td>");

                sbResult.append("<td>");
                sbResult.append(liv.getType());
                sbResult.append("</td>");

                sbResult.append("<td>");
                sbResult.append(liv.getOwner());
                sbResult.append("</td>");

                sbResult.append("<td>");
                sbResult.append(liv.getReporter());
                sbResult.append("</td>");

                sbResult.append("<td>");
                sbResult.append(dateSendProdStringAux);
                sbResult.append("</td>");

                sbResult.append("<td>");
                sbResult.append(dateStringAux);
                sbResult.append("</td>");

                sbResult.append("<td>");
                sbResult.append(liv.getContenuLivrables());
                sbResult.append("</td>");

                sbResult.append("<td style='padding-top: 2px; padding-bottom: 2px; margin: auto' id='#").append(liv.getNumeroLivraison()).append("'>");

                Ticket TicketAnomalie = (Ticket) (mapAnomalies.get(liv.getNumeroAnomalie()).get("Ticket"));
                illegibleAuthorisers.add(liv.getOwner());
                illegibleAuthorisers.add(liv.getReporter());
                illegibleAuthorisers.add(TicketAnomalie.getOwner());
                illegibleAuthorisers.add(TicketAnomalie.getReporter());
                if (illegibleAuthorisers.contains(connectedUser) || tools.hasRole("VALIDATION_HOTFIX")) {
                    sbResult.append("<input type='checkbox' name='").append(liv.getNumeroLivraison()).append("' id='").append(liv.getNumeroLivraison()).append("' class='active_checkbox'/>");
                    sbResult.append("<label for='").append(liv.getNumeroLivraison()).append("' class='css-label'></label>");
                    sbResult.append("<input type='hidden' value='' id='ticketValide' name='ticketValide' class='ticketValide'/>");
                } else {
                    if (liv.getValide()) {
                        sbResult.append("<center><img src='images/valider.png'  height='22' width='22' style='opacity: 0.3;'></center>");
                    } else {
                        sbResult.append("<center><img src='images/bloquer.png'  height='22' width='22' style='opacity: 0.3;'></center>");
                    }
                }

                sbResult.append("</td>");
                sbResult.append("</tr>");
            }
        } else {
            sbResult.append("<tr><td colspan='").append(largeur).append("'>Aucun ticket à traiter</td></tr>");
        }

        sbResult.append("</tbody>");
        sbResult.append("</table>");
        sbResult.append("<br>");
        if (illegibleAuthorisers.contains(connectedUser) || tools.hasRole("VALIDATION_HOTFIX")) {
            sbResult.append("<p><center><span id='messageResultatPersist' class='vert clignotant'></span></center></p>");
            sbResult.append("<div class='center'>");
            sbResult.append("<input type='button' class='boutonValiderStandard' id='validerTransfertHfVersPROD' value='Valider le redéploiement sur INV' onclick='validerTickets();' />");
            sbResult.append("</div>");
        }
        return sbResult.toString();

    }

//new    
    private String relacerAllHotfixes(String ticketsValides, Map<String, List<Livraison>> mapLivraisons) {
        String resultat = null;
        boolean success = true;
        String[] listeTicketsValides;
        String connectedUser = Tools.getConnectedLogin();
        try {
            Map<String, Livraison> mapLivraisonsArelancer = new HashMap<String, Livraison>();
            for (Livraison liv : mapLivraisons.get("OV_HF_NON_QUALIFIEE")) {
                mapLivraisonsArelancer.put(liv.getNumeroLivraison().toString(), liv);
            }
            listeTicketsValides = ticketsValides.split("#");
            if (ticketsValides.trim().length() > 0) {
                for (int i = 0; i < listeTicketsValides.length; i++) {
                    if (listeTicketsValides[i].trim().length() > 0) {
                        if (mapLivraisonsArelancer.containsKey(listeTicketsValides[i])) {
                            Livraison liv = mapLivraisonsArelancer.get(listeTicketsValides[i]);
                            DataBaseTracRequests.relancerHotfixSurINV(liv, connectedUser);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(ex));
            success = false;
        }
        if (success) {
            resultat = "Les tickets selectionnés ont été relancés pour redéploiement et Test sur INV";
        } else {
            resultat = "echec de relance des tickets";
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
