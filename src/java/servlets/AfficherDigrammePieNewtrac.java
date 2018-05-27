/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import newTrac.DiagramsPieToolsNewTrac;
import tools.Configuration;
import tools.DiagramsPieTools;
import tools.ManipulationObjectsTool;
import tools.Tools;

/**
 *
 * @author 04486
 */
public class AfficherDigrammePieNewtrac extends HttpServlet {

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
    private static Map<String, Date> executionHistoryMap;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String jsonText = null;
        try {
            String persistenceUnit = request.getParameter("persistenceUnit");
            String circuit = request.getParameter("circuit");
            String critereTri = request.getParameter("critereTri");
            String projetTrac = request.getParameter("projetTrac");

            String connectedUser = Tools.getConnectedLogin();
            servlets.AfficherMessageEtatAvancement.executionEnCours.put(connectedUser, true);
            jsonText = getOldResultOrCalculateIt(persistenceUnit, circuit, critereTri, projetTrac);
            servlets.AfficherMessageEtatAvancement.executionEnCours.put(connectedUser, false);
            //Tools.showConsolLog("-----------------\n" + jsonText + "\n----------------");
            out.println(jsonText);
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException("-----------------\n" + jsonText + "\n----------------\n" + tools.Tools.getStackTrace(exep));
        } finally {
            try {
                out.close();
            } catch (Exception exp) {
            }
        }
    }

    public synchronized String getOldResultOrCalculateIt(String persistenceUnit, String circuit, String critereTri, String projetTrac) {
        String filtre = persistenceUnit + "_" + circuit + "_" + critereTri;
        DiagramsPieToolsNewTrac diagramsPieTools = new DiagramsPieToolsNewTrac();
        String resultat = null;
        boolean execution = true;
        ManipulationObjectsTool man = new ManipulationObjectsTool();
        if (executionHistoryMap == null) {
            executionHistoryMap = new LinkedHashMap<String, Date>();
        }
        if (executionHistoryMap.containsKey(filtre)) {

            //s'il y a moins de 5 min que l'ancienne requete a été executée, retourner l'ancien résultat, sinon reexecuter la requete 
            if (!Tools.testTempsEcoule(executionHistoryMap.get(filtre), 300)) {
                File serialisedfile = new File(Configuration.parametresList.get("espaceLocal") + System.getProperty("file.separator") + "statistiques" + System.getProperty("file.separator") + filtre);
                try {
                    resultat = (String) man.deserialisation(serialisedfile);
                    execution = false;
                } catch (Exception ex) {
                    execution = true;
                }
            }
        }
        if (execution == true) {
            resultat = diagramsPieTools.preparationDiagrammePieGenerique(persistenceUnit, circuit, critereTri, projetTrac);
            executionHistoryMap.put(filtre, new Date());
            man.serialisation(resultat, filtre, "statistiques");
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
