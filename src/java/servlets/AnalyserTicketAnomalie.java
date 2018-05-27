/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tools.Configuration;
import dataBaseTracRequests.DataBaseTracRequests;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author 04486
 */
public class AnalyserTicketAnomalie extends HttpServlet {

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
    private List<String> detectCircuitEnvironnements(String cle) {
        String envConcat = "";
        List<String> listeEnv;
        String[] tabEnvNameCircuit = null;
        try {
            envConcat = Configuration.parametresList.get(cle);
            envConcat = envConcat.replace("et", ",");
            tabEnvNameCircuit = envConcat.split(",");
            for (int i = 0; i < tabEnvNameCircuit.length; i++) {
                tabEnvNameCircuit[i] = tabEnvNameCircuit[i].trim();
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        listeEnv = new ArrayList<String>(Arrays.asList(tabEnvNameCircuit));
        return listeEnv;
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            Configuration.initialisation();
            int ticketAnomalieId = -1;
            String ticketIdString = request.getParameter("ticketId");
            if (StringUtils.isNumericSpace(ticketIdString)) {
                ticketAnomalieId = Integer.parseInt(ticketIdString);
                String action = request.getParameter("action");
                String jsonResultat = DataBaseTracRequests.analyseTicketAnomalie(ticketAnomalieId, detectCircuitEnvironnements("environnementsCircuitRelease"), detectCircuitEnvironnements("environnementsCircuitProjet"), action);
                out.println(jsonResultat);
            } else {
                out.println("Le numéro de l'anomalie doit être un entier");
            }



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
