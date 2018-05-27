/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import entitiesMysql.DelaisGi;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;
import tools.Configuration;
import tools.DataBaseTools;

/**
 *
 * @author 04486
 */
public class DelaisGestionIncidentServlet extends HttpServlet {

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
                DataBaseTools dbTools = new DataBaseTools(Configuration.puOvTools);
                Map<String, Map<String, String>> delaisGestionIncidentMap = chargerTousLesDelaisGestionIncident(dbTools);
                resultat = getHtmlTableDelais(delaisGestionIncidentMap);
            } else {
                String priorite = request.getParameter("priorite");
                String type = request.getParameter("type");
                String delais = request.getParameter("delais");
                resultat = updateDelais(priorite, type, delais);
                try {
                    out.close();
                } catch (Exception exp) {
                }
            }
            out.println(resultat);
        } finally {
            out.close();
        }
    }

    private String updateDelais(String priorite, String type, String delais) {
        //******************************** update mysql AvalMOA ********************************
        DataBaseTools dbToolsCommit = new DataBaseTools(Configuration.puOvTools);
        List<DelaisGi> delaisToBePersistedList = new ArrayList<DelaisGi>();
        DataBaseTools dbTools = new DataBaseTools(Configuration.puOvTools);
        Query q = dbTools.em.createNamedQuery("DelaisGi.findAll");
        List<DelaisGi> delaisGiList = (List<DelaisGi>) q.getResultList();
        q.setHint(QueryHints.REFRESH, HintValues.TRUE);
        for (DelaisGi delaisGi : delaisGiList) {
            if (delaisGi.getDelaisGiPK().getPriorite().equals(priorite) && delaisGi.getDelaisGiPK().getType().equals(type)) {
                delaisGi.setDelais(delais);
                delaisToBePersistedList.add(delaisGi);
            }
        }
        dbToolsCommit.updateObjectList(delaisToBePersistedList);
        return "Mise a jour effectuée avec succès";
    }

    public static Map<String, Map<String, String>> chargerTousLesDelaisGestionIncident(DataBaseTools dbTools) {
        Map<String, Map<String, String>> delaisGestionIncident = new HashMap<String, Map<String, String>>();
        try {
            Query q = dbTools.em.createNamedQuery("DelaisGi.findAll");
            q.setHint(QueryHints.REFRESH, HintValues.TRUE);
            List<DelaisGi> delaisGiList = (List<DelaisGi>) q.getResultList();
            for (DelaisGi delaisGi : delaisGiList) {
                String priority = delaisGi.getDelaisGiPK().getPriorite();
                String type = delaisGi.getDelaisGiPK().getType();
                String delais = delaisGi.getDelais();
                if (delaisGestionIncident.containsKey(type)) {
                    delaisGestionIncident.get(type).put(priority, delais);
                } else {
                    Map<String, String> delaisGiMapAux = new HashMap<String, String>();
                    delaisGiMapAux.put(priority, delais);
                    delaisGestionIncident.put(type, delaisGiMapAux);
                }
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return delaisGestionIncident;
    }

    public static String getHtmlTableDelais(Map<String, Map<String, String>> delaisGestionIncident) {
        int comp = 0;
        String cle = null;
        int largeur = 0;
        int nbrreportings = 0;
        largeur = 12;
        nbrreportings = 3;
        StringBuilder sbResult = new StringBuilder();

        Map<String, String> immediatMap = delaisGestionIncident.get("immediat");
        Map<String, String> progressifMap = delaisGestionIncident.get("progressif");
        Map<String, String> degressifMap = delaisGestionIncident.get("degressif");

        sbResult.append("<table id='tableParamDelaisGI' class='tableParamStyle roundCornerTable dpc'>");
        sbResult.append("<thead>");
        sbResult.append("<tr>");
        sbResult.append("<th style='padding: 0px'>");
        sbResult.append("<div class='diagonal'>");
        sbResult.append("<div class='b1'>Priorité</div>");
        sbResult.append("<div class='b2'>Propagation</div>");
        sbResult.append("</div>");
        sbResult.append("<div class='clear'></div>");
        sbResult.append("</th>");
        sbResult.append("<th>");
        sbResult.append("Bloquante");
        sbResult.append("</th>");
        sbResult.append("<th>");
        sbResult.append("majeure");
        sbResult.append("</th>");
        sbResult.append("<th>");
        sbResult.append("mineure");
        sbResult.append("</th>");
        sbResult.append("</tr>");
        sbResult.append("</thead>");
        sbResult.append("<tbody>");

        sbResult.append("<tr id='immediat' class='containId aff'>");
        sbResult.append("<td class='propagation'>Immédiat</td>");
        sbResult.append("<td class='tdContenu' id='bloquante' ondblclick='toInputParam($(this).find(\"span:first-child\"));'> <span class='valueOf'>" + immediatMap.get("bloquante") + "</span> </td>");
        sbResult.append("<td class='tdContenu' id='majeure' ondblclick='toInputParam($(this).find(\"span:first-child\"));'> <span class='valueOf'>" + immediatMap.get("majeure") + "</span> </td>");
        sbResult.append("<td class='tdContenu' id='mineure' ondblclick='toInputParam($(this).find(\"span:first-child\"));'> <span class='valueOf'>" + immediatMap.get("mineure") + "</span> </td>");
        sbResult.append("</tr>");

        sbResult.append("<tr id='progressif' class='containId aff'>");
        sbResult.append("<td class='propagation'>Progressif</td>");
        sbResult.append("<td class='tdContenu' id='bloquante' ondblclick='toInputParam($(this).find(\"span:first-child\"));'> <span class='valueOf'>" + progressifMap.get("bloquante") + "</span> </td>");
        sbResult.append("<td class='tdContenu' id='majeure' ondblclick='toInputParam($(this).find(\"span:first-child\"));'> <span class='valueOf'>" + progressifMap.get("majeure") + "</span> </td>");
        sbResult.append("<td class='tdContenu' id='mineure' ondblclick='toInputParam($(this).find(\"span:first-child\"));'> <span class='valueOf'>" + progressifMap.get("mineure") + "</span> </td>");
        sbResult.append("</tr>");

        sbResult.append("<tr id='degressif' class='containId aff'>");
        sbResult.append("<td class='propagation'>Dégressif</td>");
        sbResult.append("<td class='tdContenu' id='bloquante' ondblclick='toInputParam($(this).find(\"span:first-child\"));'> <span class='valueOf'>" + degressifMap.get("bloquante") + "</span> </td>");
        sbResult.append("<td class='tdContenu' id='majeure' ondblclick='toInputParam($(this).find(\"span:first-child\"));'> <span class='valueOf'>" + degressifMap.get("majeure") + "</span> </td>");
        sbResult.append("<td class='tdContenu' id='mineure' ondblclick='toInputParam($(this).find(\"span:first-child\"));'> <span class='valueOf'>" + degressifMap.get("mineure") + "</span> </td>");
        sbResult.append("</tr>");

        sbResult.append("</tbody>");
        sbResult.append("</table>");

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
