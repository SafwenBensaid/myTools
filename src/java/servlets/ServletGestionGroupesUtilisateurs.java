/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import entitiesMysql.Environnement;
import entitiesMysql.Fonctionalite;
import entitiesMysql.GroupeHasEnvironnement;
import entitiesMysql.GroupeHasFonctionalite;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tools.Configuration;
import dataBaseTracRequests.DataBaseTracRequests;
import tools.DataBaseTools;

/**
 *
 * @author 04486
 */
public class ServletGestionGroupesUtilisateurs extends HttpServlet {

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


            String typeReq = request.getParameter("typeModf");
            DataBaseTools dbTools = new DataBaseTools(Configuration.puOvTools);
            String resultat = "";
            if (typeReq.equals("supp")) {
                String id = request.getParameter("id").trim();
                DataBaseTracRequests.deleteGroup(dbTools, id);
                Configuration.getAllGroupes(dbTools);
            } else if (typeReq.equals("affichageEnvironnements")) {
                String groupeName = request.getParameter("groupeName").trim();
                String classCheckBox = request.getParameter("classCheckBox").trim();

                out.println("<div class='centre'>");
                out.println("<div class='titre'>La liste d'environnements dont le groupe <u>" + groupeName + "</u> a le droit</div><br>");
                out.println("</div>");
                out.println("<center>");
                out.println("<table class='roundCornerTable' id='tableDroitsEnvironnements'>");
                out.println("<thead>");
                out.println("<tr>");
                out.println("<th>Environnement</th>");
                out.println("<th>Avoir le droit</th>");
                out.println("</tr>");
                out.println("</thead>");
                out.println("<tbody>");


                List<GroupeHasEnvironnement> listeGroupeHasEnvironnement = Configuration.groupMap.get(groupeName).getGroupeHasEnvironnementList();
                List<String> listeNomsEnvironnementsDroitGroupe = new ArrayList<String>();
                for (GroupeHasEnvironnement ge : listeGroupeHasEnvironnement) {
                    listeNomsEnvironnementsDroitGroupe.add(ge.getEnvironnement().getNom());
                }

                for (Map.Entry<String, Environnement> envEntry : Configuration.allEnvironnementMap.entrySet()) {
                    out.print("<tr>");
                    out.print("<td>" + envEntry.getKey() + "</td>");
                    String options = "";
                    if (envEntry.getKey().equals("VERSIONNING")) {
                        options = "checked onclick='return false;'";
                    } else if (listeNomsEnvironnementsDroitGroupe.contains(envEntry.getKey())) {
                        options = "checked";
                    }
                    out.print("<td><center><input " + options + " class='" + classCheckBox + "' type='checkbox' id='" + envEntry.getKey() + "' value='" + envEntry.getKey() + "'></center></td>");
                    out.print("</tr>");
                }

                out.println("</tbody>");
                out.println("</table>");
                out.println("<br/><input type='button' style='width:100px' class='boutonGenerer'  value='Enregistrer' onclick='miseAJourGroupeHasEnvironnements(\"" + groupeName + "\",\"" + classCheckBox + "\");' />");
                out.println("<div class='resultatInsertion vert' id='logInsertion'><br>Mise à jour effectuée avec succès</div>");
                out.println("</center>");
            } else if (typeReq.equals("affichageFonctionnalites")) {
                String groupeName = request.getParameter("groupeName").trim();
                String classCheckBox = request.getParameter("classCheckBox").trim();

                List<GroupeHasFonctionalite> listeGroupeHasFonctionnalites = Configuration.groupMap.get(groupeName).getGroupeHasFonctionaliteList();

                List<String> listeNomsFonctionnalitesDroitGroupe = new ArrayList<String>();
                for (GroupeHasFonctionalite gf : listeGroupeHasFonctionnalites) {
                    listeNomsFonctionnalitesDroitGroupe.add(gf.getFonctionalite().getName());
                }
                out.println("<div class='centre'>");

                out.println("<div class='titre'>La liste de fonctionnalités dont le groupe <u>" + groupeName + "</u> a le droit</div><br>");
                out.println("</div>");
                out.println("<center>");
                out.println("<table class='roundCornerTable' id='tableDroitsFonctionnalites'>");
                out.println("<thead>");
                out.println("<tr>");
                out.println("<th>Fonctionnalité</th>");
                out.println("<th>Avoir le droit</th>");
                out.println("</tr>");
                out.println("</thead>");
                out.println("<tbody>");
                for (Map.Entry<String, List<Fonctionalite>> fonctEntry : Configuration.allFonctionalitesMapOrdredByType.entrySet()) {
                    out.print("<tr class='titre'>");
                    out.print("<td colspan='2'>");
                    if (fonctEntry.getKey().equals("BOUTON")) {
                        out.println("Boutons");
                    } else if (fonctEntry.getKey().equals("MENU")) {
                        out.println("Menus");
                    } else if (fonctEntry.getKey().equals("SOUS_MENU")) {
                        out.println("Sous menus");
                    }
                    out.print("</td>");
                    out.print("</tr>");
                    for (Fonctionalite fonct : fonctEntry.getValue()) {
                        out.print("<tr>");
                        out.print("<td>" + fonct.getName() + "</td>");
                        String options = "";
                        if (listeNomsFonctionnalitesDroitGroupe.contains(fonct.getName())) {
                            options = "checked";
                        }
                        out.print("<td><center><input " + options + " class='" + classCheckBox + "' type='checkbox' id='" + fonct.getName() + "' value='" + fonct.getName() + "'></center></td>");
                        out.print("</tr>");
                    }
                }
                out.println("</tbody>");
                out.println("</table>");
                out.println("<br/><input type='button' style='width:100px' class='boutonGenerer'  value='Enregistrer' onclick='miseAJourGroupeHasFonctionnalites(\"" + groupeName + "\",\"" + classCheckBox + "\");' />");
                out.println("<div class='resultatInsertion vert' id='logInsertion'><br>Mise à jour effectuée avec succès</div>");
                out.println("</center>");

            } else if (typeReq.equals("majEnvironnements")) {
                String groupeName = request.getParameter("groupeName").trim();
                String checked = request.getParameter("checked").trim();
                String[] checkedBoxesArray = checked.split("@");
                List<String> envList = new ArrayList<String>(Arrays.asList(checkedBoxesArray));
                DataBaseTracRequests.miseAJourListeEnvironnements(dbTools, groupeName, envList);
            } else if (typeReq.equals("majFonctionnalites")) {
                String groupeName = request.getParameter("groupeName").trim();
                String checked = request.getParameter("checked").trim();
                String[] checkedBoxesArray = checked.split("@");
                List<String> envList = new ArrayList<String>(Arrays.asList(checkedBoxesArray));
                DataBaseTracRequests.miseAJourListeFonctionnalites(dbTools, groupeName, envList);
            } else if (typeReq.equals("addNewGroupe")) {
                String groupeName = request.getParameter("groupeName").trim();
                DataBaseTracRequests.createNewGroupe(dbTools, groupeName);


                out.print("<tr id='" + groupeName + "' class='containId'>");
                out.print("<td class='imageContenu'></td>");
                out.print("<td class='imageContenu'><img src='images/remove.png' class='imageRemove' onclick='removeUserHasEnv(this);'/></td>");
                out.print("<td class='tdContenu' id='groupe'>" + groupeName + "</td>");

                out.print("<td class='tdContenu'><center>");
                out.print("<a href='#?w=630' rel='popup_gestion_environnements' id='" + groupeName + "' class='poplight' onclick=remplirPopupGestion(this,'affichageEnvironnements','popup_gestion_environnements','environnements');>");
                out.print("<img class ='icone' src='images/clouds.png'/>");
                out.print("</a>");
                out.print("</center></td>");

                out.print("<td class='tdContenu'><center>");
                out.print("<a href='#?w=630' rel='popup_gestion_fonctionnalites' id='" + groupeName + "' class='poplight' onclick=remplirPopupGestion(this,'affichageFonctionnalites','popup_gestion_fonctionnalites','fonctionnalites');>");
                out.print("<img class ='icone' src='images/fonctionnalites.png'/>");
                out.print("</a>");
                out.print("</center></td>");

                out.print("</tr>");
            }

            Configuration.getAllGroupes(dbTools);

            dbTools.closeRessources();
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        } finally {
            try {
                out.close();
            } catch (Exception exp) {
            }
        }

        /*
         response.setContentType("text/html;charset=UTF-8");
         PrintWriter out = response.getWriter();
         try {
        
         out.println("<!DOCTYPE html>");
         out.println("<html>");
         out.println("<head>");
         out.println("<title>Servlet ServletGestionGroupesUtilisateurs</title>");            
         out.println("</head>");
         out.println("<body>");
         out.println("<h1>Servlet ServletGestionGroupesUtilisateurs at " + request.getContextPath() + "</h1>");
         out.println("</body>");
         out.println("</html>");
         } finally {            
         out.close();
         }
         */
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
