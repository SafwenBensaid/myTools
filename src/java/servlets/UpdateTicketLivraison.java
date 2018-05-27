/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import dataBaseTracRequests.DataBaseTracGenericRequests;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tools.Configuration;
import dataBaseTracRequests.DataBaseTracRequests;
import tools.DataBaseTools;
import tools.Tools;

/**
 *
 * @author 04486
 */
public class UpdateTicketLivraison extends HttpServlet {

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
            String connectedUser = Tools.getConnectedLogin();
            String numTicketLivraison = request.getParameter("numTicketLivraison");
            String messageTrac = request.getParameter("messageTrac");
            String circuit = request.getParameter("circuit");
            String contenuDesLivrables = request.getParameter("contenuDesLivrables");
            boolean nePasEcraserLivrable = Boolean.valueOf(request.getParameter("nePasEcraserLivrable"));
            String resultat = DataBaseTracRequests.sendHotfixToBeDeployed(request, connectedUser, numTicketLivraison, messageTrac, circuit, contenuDesLivrables, true, true, true, nePasEcraserLivrable);
            //update contenu des livrables
            StringBuilder querySb = new StringBuilder("UPDATE livraison l SET contenu_livrables=( SELECT tc.value FROM traclivraisont24.ticket_custom tc WHERE l.numero_livraison = tc.ticket AND tc.name = 'contenu_des_livrables' ) WHERE l.numero_livraison = " + numTicketLivraison);
            DataBaseTools dbToolsOvtools = new DataBaseTools(Configuration.puOvTools);
            new DataBaseTracGenericRequests<Integer>().executeQueryRequest(dbToolsOvtools, querySb, "NVQ_UPDATE");
            if (Configuration.livraisonsEnCoursMap.containsKey(numTicketLivraison)) {
                Configuration.livraisonsEnCoursMap.remove(numTicketLivraison);
            }
            out.print(resultat);
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
