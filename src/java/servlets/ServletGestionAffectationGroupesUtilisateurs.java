/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import entitiesMysql.Groupe;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tools.Configuration;
import tools.DataBaseTools;
import dataBaseTracRequests.DataBaseTracRequests;

/**
 *
 * @author 04486
 */
public class ServletGestionAffectationGroupesUtilisateurs extends HttpServlet {

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
        DataBaseTools dbTools = null;
        try {
            String typeReq = request.getParameter("typeModf");
            dbTools = new DataBaseTools(Configuration.puOvTools);
            String resultat = "";
            if (typeReq.equals("affichageGroupes")) {
                String userLogin = request.getParameter("userLogin").trim();
                String classCheckBox = request.getParameter("classCheckBox").trim();

                out.println("<div class='centre'>");
                out.println("<div class='titre'>La liste des groupes affectés à l'utilisateur <u>" + userLogin + "</u></div><br>");
                out.println("</div>");
                out.println("<center>");
                out.println("<table class='roundCornerTable' id='tableDroitsEnvironnements'>");
                out.println("<thead>");
                out.println("<tr>");
                out.println("<th>Groupe</th>");
                out.println("<th>Avoir le droit</th>");
                out.println("</tr>");
                out.println("</thead>");
                out.println("<tbody>");

                for (Map.Entry<String, Groupe> groupEntry : Configuration.groupMap.entrySet()) {
                    out.print("<tr>");
                    out.print("<td>" + groupEntry.getKey() + "</td>");
                    String options = "";
                    if (groupEntry.getKey().equals("SIMPLE_USER")) {
                        options = "disabled checked onclick='return false;'";
                    } else if (Configuration.usersGroupMap.get(userLogin).contains(groupEntry.getKey())) {
                        options = "checked";
                    }
                    out.print("<td><center><input " + options + " class='" + classCheckBox + "' type='checkbox' id='" + groupEntry.getKey() + "' value='" + groupEntry.getKey() + "'></center></td>");
                    out.print("</tr>");
                }

                out.println("</tbody>");
                out.println("</table>");
                out.println("<br/><input type='button' style='width:100px' class='boutonGenerer'  value='Enregistrer' onclick='miseAJourUsersHasGroupes(\"" + userLogin + "\",\"" + classCheckBox + "\");' />");
                out.println("<span class='resultatInsertion vert logInsertion' id='logInsertion'>Mise à jour effectuée avec succès</span>");
                out.println("</center>");
            } else if (typeReq.equals("majGroupes")) {
                String userLogin = request.getParameter("userLogin").trim();
                String checked = request.getParameter("checked").trim();
                String[] checkedBoxesArray = checked.split("@");
                List<String> groupList = new ArrayList<String>(Arrays.asList(checkedBoxesArray));
                DataBaseTracRequests.miseAJourUserHasGroup(dbTools, userLogin, groupList);
            }
            Configuration.chargerTousLesUsersGroups(dbTools);
            //intitOk = false;
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        } finally {
            try {
                dbTools.closeRessources();
                out.close();
            } catch (Exception exp) {
            }
        }
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
