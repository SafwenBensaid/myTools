/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import entitiesMysql.Livraison;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tools.*;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

/**
 *
 * @author 04486
 */
public class SuiviHotfixServlet extends HttpServlet {

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
    private static Map<String, Date> lastExecutionDate;
    private static Map<String, String> lastExecutionResult;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String haveRole = null;
        Tools tools = new Tools();
        boolean hasRoleValidationHotfix = tools.hasRole("VALIDATION_HOTFIX");
        if (hasRoleValidationHotfix) {
            haveRole = "HAVE_ROLE_VALIDATION_HOTFIX";
        } else {
            haveRole = "DONT_HAVE_ROLE_VALIDATION_HOTFIX";
        }
        if (lastExecutionDate == null) {
            lastExecutionDate = new HashMap<String, Date>();
        }
        if (lastExecutionResult == null) {
            lastExecutionResult = new HashMap<String, String>();
        }
        if (!lastExecutionDate.containsKey(haveRole)) {
            lastExecutionDate.put(haveRole, new Date());
        }
        try {
            String resultat = "";
            String action = request.getParameter("action");
            if (action.equals("load")) {
                if (!lastExecutionResult.containsKey(haveRole) || Tools.testTempsEcoule(lastExecutionDate.get(haveRole), 60)) {
                    Map<String, List<Livraison>> mapLivraisons = new GetHotfixesPipesTool().getAllHotfixes();
                    resultat = getHtmlHotfixTable(hasRoleValidationHotfix, mapLivraisons);
                    lastExecutionResult.put(haveRole, resultat);
                    lastExecutionDate.put(haveRole, new Date());
                } else {
                    resultat = lastExecutionResult.get(haveRole);
                }
            } else {
                String ticketsValides = request.getParameter("tickets_valides");
                resultat = validateHotfixes(ticketsValides);
                lastExecutionResult = new HashMap<String, String>();
            }
            out.println(resultat);
        } finally {
            out.close();
        }
    }

    private boolean InvalideAllHotfixes(DataBaseTools dbTools) {
        boolean success = true;
        String[] listeTicketsValides;
        StringBuilder querySb;
        Query q = null;

        querySb = new StringBuilder("UPDATE Livraison SET valide=" + false + " WHERE 1=1");
        try {
            q = dbTools.em.createQuery(querySb.toString());
            q.executeUpdate();
            dbTools.em.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(ex));
            success = false;
        }
        return success;
    }

    public Map<String, Map<String, String>> jsonStringToMap(String json) {
        Map<String, Map<String, String>> map = new LinkedHashMap<String, Map<String, String>>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            //convert JSON string to Map
            map = mapper.readValue(json, new TypeReference<LinkedHashMap<String, Map<String, String>>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public String getHtmlHotfixTable(boolean authorizedAdmin, Map<String, List<Livraison>> mapLivraisons) {
        SimpleDateFormat parseFormatyyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat parseFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        parseFormat.setTimeZone(TimeZone.getTimeZone("GMT+1"));
        String dateStringAux = null;
        String dateSendProdStringAux = null;

        int comp = 0;
        String cle = null;
        int largeur = 0;
        int nbrreportings = 0;
        String titre = "";
        StringBuilder sbResult = new StringBuilder();
        sbResult.append("<table id='tableTicketsHotfix' class='roundCornerTable'>");
        sbResult.append("<thead>");
        sbResult.append("<tr>");
        sbResult.append("<th>Nbr</th>");
        sbResult.append("<th>Anomalie</th>");
        sbResult.append("<th>Livraison</th>");
        sbResult.append("<th>Responsable</th>");
        sbResult.append("<th>Type</th>");
        sbResult.append("<th>Envoyé le</th>");
        sbResult.append("<th>Déployé le</th>");
        sbResult.append("<th>Contenu des livrables</th>");
        sbResult.append("<th>Message</th>");

        sbResult.append("<th>Autoriser</th>");
        largeur = 10;
        nbrreportings = 4;

        sbResult.append("</tr>");
        sbResult.append("</thead>");
        sbResult.append("<tbody>");
        for (int i = 0; i < nbrreportings; i++) {

            if (i == 0) {
                sbResult.append("<tr class='titre'><td colspan='" + largeur + "'>Tickets Hotfix en cours de test</td></tr>");
                cle = "OV_HF_NON_QUALIFIEE";
            } else if (i == 1) {
                sbResult.append("<tr class='titre'><td colspan='" + largeur + "'>Tickets Hotfix qualifiés</td></tr>");
                cle = "OV_HF_QUALIFIEE";
            } else if (i == 2) {
                sbResult.append("<tr class='titre'><td colspan='" + largeur + "'>Tickets Hotfix en attente de déploiement sur PROD</td></tr>");
                cle = "EXPLOITATION_HF_NON_DEPLOYEE";
            } else if (i == 3) {
                sbResult.append("<tr class='titre'><td colspan='" + largeur + "'>Tickets Hotfix déployés sur PROD en attente d'harmonisation</td></tr>");
                cle = "EXPLOITATION_HF_DEPLOYEE";
            }
            comp = 0;
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
                    sbResult.append(comp + ")");
                    sbResult.append("</td>");

                    sbResult.append("<td>");
                    sbResult.append("<a class='lien numAnmalie'  onclick=\"openTracTicketInNewTab($(this).html(),'ANOMALIE');\">");
                    sbResult.append("#" + liv.getNumeroAnomalie());
                    sbResult.append("</a>");
                    sbResult.append("</td>");

                    sbResult.append("<td>");
                    sbResult.append("<a class='lien numLivraison'   onclick=\"openTracTicketInNewTab($(this).html(),'LIVRAISON');\">");
                    sbResult.append("#" + liv.getNumeroLivraison());
                    sbResult.append("</a>");
                    sbResult.append("</td>");

                    sbResult.append("<td>");
                    sbResult.append(liv.getReporter());
                    sbResult.append("</td>");

                    sbResult.append("<td>");
                    sbResult.append(liv.getType());
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

                    sbResult.append("<td style='padding-bottom: 2px; padding-top: 2px; margin: auto'>");
                    sbResult.append("<a class='conteneur_info_bull'>");
                    sbResult.append("<img class='info-icon' src='images/info.png' alt='info'>");
                    sbResult.append("<span>");
                    sbResult.append(liv.getMessageTrac());
                    sbResult.append("</span>");
                    sbResult.append("</a>");
                    sbResult.append("</td>");


                    sbResult.append("<td style='padding-top: 2px; padding-bottom: 2px; margin: auto' id='#" + liv.getNumeroLivraison() + "'>");
                    if (liv.getMessageTrac().trim().length() > 0) {
                        if (i < 2) {
                            if (authorizedAdmin) {
                                sbResult.append("<input type='checkbox' name='" + liv.getNumeroLivraison() + "' id='" + liv.getNumeroLivraison() + "' class='active_checkbox'");
                                if (liv.getValide()) {
                                    sbResult.append("checked");
                                }
                                sbResult.append("/>");
                                sbResult.append("<label for='" + liv.getNumeroLivraison() + "' class='css-label'></label>");
                                sbResult.append("<input type='hidden' value='' id='ticketValide' name='ticketValide' class='ticketValide'/>");
                            } else {
                                if (liv.getValide()) {
                                    sbResult.append("<center><img src='images/valider.png'  height='22' width='22'></center>");
                                } else {
                                    sbResult.append("<center><img src='images/bloquer.png'  height='22' width='22'></center>");
                                }
                            }
                        }
                    }
                    sbResult.append("</td>");

                    sbResult.append("</tr>");
                }
            } else {
                sbResult.append("<tr><td colspan='" + largeur + "'>Aucun ticket à traiter</td></tr>");
            }
        }
        sbResult.append("</tbody>");
        sbResult.append("</table>");
        sbResult.append("<br>");
        if (authorizedAdmin) {
            sbResult.append("<p><center><span id='messageResultatPersist' class='vert clignotant'></span></center></p>");
            sbResult.append("<div class='center'>");
            sbResult.append("<input type='button' class='boutonValiderStandard' id='validerTransfertHfVersPROD' value='Valider le transfert vers PROD' onclick='validerTickets();' />");
            sbResult.append("</div>");
        }
        return sbResult.toString();

    }

    private boolean validateAllHotfixes(DataBaseTools dbTools, String ticketsValides) {
        boolean success = true;
        String resultat = null;
        String[] listeTicketsValides;
        StringBuilder querySb;
        Query q = null;
        try {
            listeTicketsValides = ticketsValides.split("#");
            if (ticketsValides.trim().length() > 0) {
                querySb = new StringBuilder("UPDATE Livraison SET valide=" + true + " WHERE numeroLivraison in (");
                for (int i = 0; i < listeTicketsValides.length; i++) {
                    if (listeTicketsValides[i].trim().length() > 0) {
                        if (i < listeTicketsValides.length - 1) {
                            querySb.append("'");
                            querySb.append(listeTicketsValides[i]);
                            querySb.append("', ");
                        } else {
                            querySb.append("'");
                            querySb.append(listeTicketsValides[i]);
                            querySb.append("' )");
                        }
                    }
                }
                if (!dbTools.em.getTransaction().isActive()) {
                    dbTools.em.getTransaction().begin();
                }
                q = dbTools.em.createQuery(querySb.toString());
                q.executeUpdate();
                dbTools.em.getTransaction().commit();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(ex));
            success = false;
        }
        return success;
    }

    private String validateHotfixes(String ticketsValides) {
        String resultat = null;
        DataBaseTools dbTools = new DataBaseTools(Configuration.puOvTools);
        boolean resultatinitialisation = InvalideAllHotfixes(dbTools);
        boolean resultatValidation = validateAllHotfixes(dbTools, ticketsValides);
        dbTools.closeRessources();
        if (resultatinitialisation && resultatValidation == true) {
            resultat = "Les tickets selectionnés ont été validés et seront transférés à la PROD dès leur qualification";
        } else {
            resultat = "echec de validation des tickets";
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
