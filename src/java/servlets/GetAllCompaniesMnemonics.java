/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import dto.EnvironnementDTO;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import t24Scripts.T24Scripts;
import tools.Configuration;
import tools.Tools;

/**
 *
 * @author 04486
 */
public class GetAllCompaniesMnemonics extends HttpServlet {

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
    public static List<String> listMnemonic = null;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String connectedUser = Tools.getConnectedLogin();
        try {
            servlets.AfficherMessageEtatAvancement.executionEnCours.put(connectedUser, true);
            servlets.AfficherMessageEtatAvancement.setLogmessage("Analyse du Mnemonic", connectedUser);
            String envName = "ASS";
            EnvironnementDTO env = Configuration.environnementDTOMapUserHasEnvironnement.get("OVTOOLS").get(envName);
            //test d'authentification
            T24Scripts t24Scripts = new T24Scripts();
            Map<String, String> errorMap = new HashMap<String, String>();
            String resultatTestAuthentification = "";
            if (listMnemonic == null) {
                resultatTestAuthentification = t24Scripts.testLoginPasswordSystemeEtBrowser(env, Tools.getConnectedLogin()).trim();
            } else {
                resultatTestAuthentification = "TEST CONNEXION OK";
            }

            if (!resultatTestAuthentification.equals("TEST CONNEXION OK")) {
                errorMap.put("errorName", "Erreur d'authentification");
                errorMap.put("errorValue", resultatTestAuthentification);
                request.getSession().setAttribute("erreurNameErreurValue", errorMap);
                //echec d'authentification => redirection vers la page d'erreurs
                out.println("ECHEC AUTHENTIFICATION");
            } else {
                //connexion OK
                if (listMnemonic == null) {
                    listMnemonic = getAllMnemonics(env);
                }
                Configuration.initialisation();
                String mnemonic = request.getParameter("mnemonic");
                if (listMnemonic.contains(mnemonic)) {
                    out.println("MNEMONIC OK");
                } else {
                    out.println("MNEMONIC incorrect");
                }
            }
            //servlets.AfficherMessageEtatAvancement.setLogmessage("");
            servlets.AfficherMessageEtatAvancement.executionEnCours.put(connectedUser, false);

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

    public List<String> getAllMnemonics(EnvironnementDTO env) {
        T24Scripts t24Scripts = new T24Scripts();
        String resultatVerifObjets = t24Scripts.executerCommandeEnvironnementJSH(env, "LIST F.COMPANY MNEMONIC\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n", "Records Listed");
        return analyseListMnemonic(resultatVerifObjets);
    }

    public List<String> analyseListMnemonic(String ch) {
        List<String> liste = new ArrayList<String>();
        try {
            ch = ch.replace("\r", "");
            String[] tab = ch.split("\n");
            String aux = "";

            for (int i = 0; i < tab.length - 1; i++) {
                if (tab[i].length() > 0 && tab[i].trim().startsWith("TN00")) {
                    aux = tab[i].trim();
                    liste.add(aux.substring(13, 16));
                }
            }
            if (tab[tab.length - 1].length() > 0 && tab[tab.length - 1].trim().startsWith("TN00")) {
                aux = tab[tab.length - 1].trim();
                liste.add(aux.substring(13, 16));
            }
            liste.add("TN0021512");
            liste.add("TN0021513");
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return liste;
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
