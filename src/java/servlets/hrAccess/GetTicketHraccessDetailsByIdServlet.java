/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets.hrAccess;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tools.Tools;

/**
 *
 * @author 04486
 */
public class GetTicketHraccessDetailsByIdServlet extends HttpServlet {

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
        String connectedUser = Tools.getConnectedLogin();
        servlets.AfficherMessageEtatAvancement.executionEnCours.put(connectedUser, true);
        try {
            String ticketIdString = request.getParameter("ticketId");
            String natureTraitement = request.getParameter("natureTraitement");
            String packName = "";
            String erreurs = "";
            String champs = "";
            int ticketId = Integer.parseInt(ticketIdString);
            Tools.showConsolLog("ticketId: " + ticketId);
            servlets.AfficherMessageEtatAvancement.setLogmessage("Analyse du ticket TRAC", Tools.getConnectedLogin());

            //Nom du pack
            if (natureTraitement.equals("RELEASE")) {
                packName += "LIVR.";
            } else if (natureTraitement.equals("HOTFIX")) {
                packName += "LIVH.";
            }

            champs = natureTraitement + "%_%" + packName;
            Tools.showConsolLog(champs + "%*%" + erreurs);
            servlets.AfficherMessageEtatAvancement.executionEnCours.put(connectedUser, false);
            out.println(champs + "$*$" + erreurs);
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        } finally {
            try {
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
