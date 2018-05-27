/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tools.Tools;
import static tools.Tools.sendEMail;

/**
 *
 * @author 04494
 */
@WebServlet(name = "CatchProblemeUserSelfService", urlPatterns = {"/CatchProblemeUserSelfService"})
public class CatchProblemeUserSelfService extends HttpServlet {

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
        String circuit = request.getParameter("circuit");
        String devServerName = null;
        try {
            String connectedUser = Tools.getConnectedLogin();
            StringBuilder message = new StringBuilder();
            if (circuit.equals("RELEASE")) {
                devServerName = "DEVR";
            } else if (circuit.equals("PROJET")) {
                devServerName = "DEV2";
            } else if (circuit.equals("HOTFIX")) {
                devServerName = "DEVH";
            }
            Tools.showConsolLog("___________*****************___________________");
            Tools.showConsolLog("___________*****************___________________");
            Tools.showConsolLog("____________TimeOut Detected___________");
            Tools.showConsolLog("___________*****************___________________");
            Tools.showConsolLog("___________*****************___________________");
            Tools.showConsolLog("____________TimeOut Detected___________");
            Tools.showConsolLog("___________*****************___________________");
            Tools.showConsolLog("___________*****************___________________");

            message.append("Probleme de TimeOut Self Service: ");
            message.append(", Utilisateur ").append(connectedUser);
            message.append(", Circuit " + circuit);
            message.append(", Environnement " + devServerName);

            sendEMail("Exception Plateforme Ov Management Solutions", "OvManagementSolutions@biat.com.tn", new String[]{"safwen.bensaid@biat.com.tn", "faten.slim@biat.com.tn"}, new String[]{}, new String[]{}, message.toString(), true);
        } finally {
            out.close();
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
