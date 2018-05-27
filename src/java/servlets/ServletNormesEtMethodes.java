/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import entitiesMysql.*;
import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import dataBaseTracRequests.DataBaseTracRequests;
import tools.Configuration;
import tools.DataBaseTools;
import tools.Tools;

/**
 *
 * @author 04494
 */
public class ServletNormesEtMethodes extends HttpServlet {

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

        String circuitGLOB = request.getParameter("circuitGLOB");
        String circuitELEM = request.getParameter("circuitELEM");
        String typesGLOB = request.getParameter("typesGLOB");
        String typesELEM = request.getParameter("typesELEM");

        miseAjourDataBase(circuitGLOB, typesGLOB);
        miseAjourDataBase(circuitELEM, typesELEM);

    }

    public static void miseAjourDataBase(String circuit, String types) {
        try {
            String[] arrayTypes = types.split("@");
            Tools.showConsolLog("Circuit to be Removed from database: " + circuit);
            DataBaseTools dbTools = new DataBaseTools(Configuration.puOvTools);
            DataBaseTracRequests dbReq = new DataBaseTracRequests();
            dbReq.deleteObjetsHorsReferentiel(circuit, dbTools);

            for (String type : arrayTypes) {
                ObjetsHorsReferentiel objet = new ObjetsHorsReferentiel(type, circuit);
                dbTools.StoreObjectIntoDataBase(objet);
            }
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
