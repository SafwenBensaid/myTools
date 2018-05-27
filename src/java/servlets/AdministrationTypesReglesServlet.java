/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import entitiesMysql.TypesRegle;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tools.Configuration;
import dataBaseTracRequests.DataBaseTracRequests;
import tools.DataBaseTools;

/**
 *
 * @author Imen
 */
public class AdministrationTypesReglesServlet extends HttpServlet {

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
        String typeReq = request.getParameter("typeModf");
        DataBaseTools dbTools = new DataBaseTools(Configuration.puOvTools);
        DataBaseTracRequests dbRequests = new DataBaseTracRequests();

        try {
            if (typeReq.equals("supp")) {
                String cle = request.getParameter("cle").trim();
                TypesRegle tr = dbTools.em.find(TypesRegle.class, cle);
                dbTools.remove(tr);
            } else if (typeReq.equals("add")) {
                String cle = request.getParameter("cle");
                String regle = request.getParameter("regle");
                String nature = request.getParameter("nature");
                TypesRegle tr = new TypesRegle();
                tr.setCle(cle.replaceAll(" ", ".").toUpperCase());
                tr.setRegle(regle);
                tr.setNature(nature);
                dbTools.StoreObjectIntoDataBase(tr);

            } else if (typeReq.equals("edit")) {
                String cle = request.getParameter("cle").trim();
                String regle = request.getParameter("regle").trim();
                String nature = request.getParameter("nature").trim();
                dbRequests.updateTypesRegle(dbTools, cle, regle, nature);
            }

            Configuration.loadAllTypesRegles(dbTools);
            dbTools.closeRessources();
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
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
