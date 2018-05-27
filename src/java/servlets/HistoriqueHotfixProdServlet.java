/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import dataBaseTracRequests.GenericDataBaseRequestReport;
import dto.TicketChangeDTO;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tools.Configuration;

public class HistoriqueHotfixProdServlet extends HttpServlet {

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
            String dateDepart = request.getParameter("date_depart");
            String dateFin = request.getParameter("date_fin");
            String projet = request.getParameter("projet");
            String typeRequete = request.getParameter("typeRequete");
            //test ordre dates
            SimpleDateFormat parseFormat = new SimpleDateFormat(Configuration.HEURE_DATE_FORMAT);
            Date dateDepartDate;
            try {
                //Vérification de conformité des dates entrées en input
                dateDepartDate = parseFormat.parse(dateDepart);
                Date dateFinDate = parseFormat.parse(dateFin);
                if (dateDepartDate.after(dateFinDate)) {
                    out.println("<span class = 'rouge'>La date de départ ne doit pas être supérieure à la date de fin</span>");
                    return;
                }
                //Definir le site TRAC selon le Type de requete
                if (!typeRequete.isEmpty()) {
                    if (typeRequete.equals("PalliatifsUniquement")) {
                        projet = "ANOMALIES_T24";
                    }
                }

                //Déclaration des variables
                String resultat = "";
                GenericDataBaseRequestReport genericReq = new GenericDataBaseRequestReport();
                Map<String, List<String>> ticketElementsMapIn = new HashMap<>();
                Map<String, List<String>> ticketElementsMapNotIn = new HashMap<>();
                Map<String, List<String>> ticketCustomElementsMapIn = new HashMap<>();
                Map<String, List<String>> ticketCustomElementsMapNotIn = new HashMap<>();
                TicketChangeDTO ticketChangeDTO = null;
                String[] cles = null;
                String[] fieldsToBeDisplayed = null;
                String siteTrac = "";
                String persistanceUnit = "";

                //Spécification des critères de selection selon le projet TRAC
                switch (projet) {
                    case "LIVRAISONS_T24": {
                        ticketElementsMapIn.put("type", Arrays.asList(new String[]{"HOT FIXE PROD", "ACTION A CHAUD PROD"}));
                        ticketElementsMapIn.put("priority", Arrays.asList(new String[]{"DEPLOYEE"}));
                        ticketCustomElementsMapIn.put("nature_liv", Arrays.asList(new String[]{"CORRECTIF"}));
                        ticketCustomElementsMapIn.put("biatprod", Arrays.asList(new String[]{"1"}));
                        ticketChangeDTO = new TicketChangeDTO("biatprod", dateDepart, dateFin, "1");
                        cles = new String[]{"ticket_origine", "contenu_des_livrables", "summary", "description"};
                        fieldsToBeDisplayed = new String[]{"id", "ticket_origine", "type", "milestone", "dateDeploiement", "contenu_des_livrables", "cc", "reporter", "summary", "description"};
                    }
                    break;
                    case "ANOMALIES_T24": {
                        ticketElementsMapIn.put("version", Arrays.asList(new String[]{"ACTION A CHAUD POUR MISE EN PROD"}));
                        ticketElementsMapIn.put("priority", Arrays.asList(new String[]{"APPLIQUEE SUR PROD"}));
                        ticketCustomElementsMapIn.put("mode_traitement", Arrays.asList(new String[]{"PALLIATIF "}));
                        ticketChangeDTO = new TicketChangeDTO("priority", dateDepart, dateFin, "APPLIQUEE SUR PROD");
                        cles = new String[]{"projet", "mode_traitement", "marge_resolution"};
                        fieldsToBeDisplayed = new String[]{"numeroAnomalie", "milestone", "owner", "reporter", "dateDeploiement", "summary", "description"};
                    }
                    break;
                    case "HR_ACCESS":
                    case "MXP":
                    case "BFI_CARTAGO_INTRANET":
                    case "CONTENTIEUX":
                    case "GTI_WINSERGE":
                    case "OGC":
                    case "BFI_TITRE":
                    case "ICR": {
                        ticketCustomElementsMapIn.put("nature_traitement", Arrays.asList(new String[]{"HOTFIX", "ACTION A CHAUD"}));
                        ticketChangeDTO = new TicketChangeDTO("status", dateDepart, dateFin, "deployee_sur_prod");
                        cles = new String[]{"projet", "nature_traitement", "captain_dev"};
                        fieldsToBeDisplayed = new String[]{"id", "nature_traitement", "milestone", "dateDeploiement", "owner", "reporter", "summary", "description"};
                    }
                    break;
                    default:
                        throw new IllegalArgumentException("Invalid project " + projet);
                }
                if (Configuration.mapProjetsTrac.containsKey(projet)) {
                    siteTrac = projet;
                    persistanceUnit = Configuration.mapProjetsTrac.get(projet).getValeur2();
                    resultat = genericReq.executeGenericRequest(persistanceUnit, ticketElementsMapIn, ticketElementsMapNotIn, ticketCustomElementsMapIn, ticketCustomElementsMapNotIn, ticketChangeDTO, cles, fieldsToBeDisplayed, siteTrac);
                }
                out.println(resultat);
            } catch (ParseException ex) {
                Logger.getLogger(HistoriqueHotfixProdServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        } finally {
            out.close();
        }
    }
    /*
     public static void main(String[] args) {
     try {
     Configuration.initialisation();
     String dateDepart = "01-06-2010 00:00";
     String dateFin = "17-08-2017 00:00";
     //test ordre dates
     SimpleDateFormat parseFormat = new SimpleDateFormat(Configuration.HEURE_DATE_FORMAT);
     Date dateDepartDate;
     System.out.println("start");
     try {
     dateDepartDate = parseFormat.parse(dateDepart);
     Date dateFinDate = parseFormat.parse(dateFin);
     if (dateDepartDate.after(dateFinDate)) {
     System.out.println("<span class = 'rouge'>La date de départ ne doit pas être supérieure à la date de fin</span>");
     return;
     }
     } catch (ParseException ex) {
     Logger.getLogger(HistoriqueHotfixProdServlet.class.getName()).log(Level.SEVERE, null, ex);
     }


     //Déclaration des variables
     String resultat = "";
     GenericDataBaseRequestReport genericReq = new GenericDataBaseRequestReport();
     Map<String, List<String>> ticketElementsMapIn = new HashMap<>();
     Map<String, List<String>> ticketElementsMapNotIn = new HashMap<>();
     Map<String, List<String>> ticketCustomElementsMapIn = new HashMap<>();
     Map<String, List<String>> ticketCustomElementsMapNotIn = new HashMap<>();
     TicketChangeDTO ticketChangeDTO = null;
     String[] cles = null;
     String[] fieldsToBeDisplayed = null;
     String titre = "Liste de Hotfix déployés sur PROD depuis...";
     String siteTrac = "";
     String persistanceUnit = "";

     // generic
     ticketCustomElementsMapIn.put("nature_traitement", Arrays.asList(new String[]{"HOTFIX", "ACTION A CHAUD"}));
     //ticketCustomElementsMapNotIn.put("prod", Arrays.asList(new String[]{"1"}));
     ticketChangeDTO = new TicketChangeDTO("status", dateDepart, dateFin, "deployee_sur_prod");
     cles = new String[]{"projet", "nature_traitement", "captain_dev"};
     fieldsToBeDisplayed = new String[]{"id", "nature_traitement", "milestone", "dateDeploiement", "owner", "reporter", "summary", "description"};
            
     siteTrac = Configuration.tracGtiWinserge;
     persistanceUnit = Configuration.puGtiWinserge;
     resultat = genericReq.executeGenericRequest(persistanceUnit, ticketElementsMapIn, ticketElementsMapNotIn, ticketCustomElementsMapIn, ticketCustomElementsMapNotIn, ticketChangeDTO, cles, fieldsToBeDisplayed, siteTrac);
     // out.println(resultat);
     System.err.println(resultat);

     } finally {
     System.out.println("end");
     }
     }*/
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
