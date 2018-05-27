/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import dto.UserDTO;
import entitiesMysql.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import springSecurity.SpringSecurityTools;
import threads.ManageLogThread;
import tools.*;

/**
 *
 * @author 04486
 */
public class ServletGestionUtilisateur extends HttpServlet {

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
            String modif = request.getParameter("modif");
            String matricule = request.getParameter("matricule");
            Configuration.initialisation();
            if (modif.equals("creation") || modif.equals("suppression")) {
                Map<String, String> matriculeLoginMap = Configuration.matriculeLoginMap;
                Map<String, String> loginMatriculeMap = Configuration.loginMatriculeMap;

                UserDTO userDTO = SpringSecurityTools.getUserSearch(matricule, "");
                String userName = userDTO.getNom().toLowerCase();

                if (modif.equals("creation")) {
                    //if matricule does Not exist in DB and userName does Not exist in DB
                    if (!matriculeLoginMap.containsKey(matricule) && !loginMatriculeMap.containsKey(userName)) {
                        Tools.insertNewUserIntoDB(matricule);
                        out.print("Nouvel utilisateur " + userName + " ajouté avec succès");
                    } else {
                        out.print("utilisateur dejà existant");
                    }
                } else if (modif.equals("suppression")) {
                    DataBaseTools dbTools = new DataBaseTools(Configuration.puOvTools);
                    Users userToBeDeleted = dbTools.em.find(Users.class, matricule);
                    dbTools.remove(userToBeDeleted);
                    dbTools.closeRessources();
                }
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        } finally {
            try {
                out.flush();
                out.close();
            } catch (Exception exp) {
            }
            Configuration.intitOk = false;
            Configuration.initialisation();
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
