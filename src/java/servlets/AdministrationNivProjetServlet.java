/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import entitiesMysql.Niveauprojet;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import tools.Configuration;
import tools.CrudTools;
import tools.Tools;

/**
 *
 * @author karim
 */
public class AdministrationNivProjetServlet extends HttpServlet {

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
        String typeReq = request.getParameter("typeModf");
        String resultat;
        try {
            if (typeReq.equals("supp")) {
                String valeur = request.getParameter("id");
                response.setContentType("text/xml");
                response.setHeader("Cache-Control", "no-cache");
                CrudTools<Niveauprojet> d = new CrudTools<>(Configuration.puOvTools);
                Niveauprojet u = d.findNivProjet(valeur);
                resultat = "votre id au serv = " + valeur + "\n nous l'avons supprimer";
                d.Remove(u);
                d.closeRessources();
                PrintWriter out = response.getWriter();
                Tools.showConsolLog("/**************************** servlets");
                out.println("<message>" + resultat + "</message>");
                try {
                    out.close();
                } catch (Exception exp) {
                }
            } else if (typeReq.equals("modf")) {
                String champ = request.getParameter("champ");
                String valeur = request.getParameter("val");
                String id = request.getParameter("id");
                CrudTools<Niveauprojet> d = new CrudTools<Niveauprojet>(Configuration.puOvTools);
                d.update(id, champ, valeur, "Niveauprojet");
                d.closeRessources();
                PrintWriter out = response.getWriter();
                Tools.showConsolLog("/updaaaaaaaaaaaaaaate**************************** servlets ");
                resultat = "mise a jours avec succée de champ" + champ + " : " + valeur;
                out.println("<message>" + resultat + "</message>");
                try {
                    out.close();
                } catch (Exception exp) {
                }
            } else {
                Tools.showConsolLog("//////////////////////////Ajouuuuuuuuuut//////////////////////////////////");
                response.setContentType("text/json");
                String json = request.getParameter("json");
                JSONObject jsonData = (JSONObject) JSONValue.parse(json);
                //récupération des données
                String abreviation = ((String) jsonData.get("abreviation"));
                String nom = ((String) jsonData.get("nom"));
                String actif = ((String) jsonData.get("actif"));
                String releaseprevue = ((String) jsonData.get("releaseprevue"));
                String metier = ((String) jsonData.get("metier"));
                String typeprojet = ((String) jsonData.get("typeprojet"));
                String sitedeveloppement = ((String) jsonData.get("sitedeveloppement"));
                //String id = ((String)jsonData.get("ID"));
                //ajout dans la base de données
                Tools.showConsolLog("//////////////////////////instance//////////////////////////////////");
                CrudTools<Niveauprojet> d = new CrudTools<Niveauprojet>(Configuration.puOvTools);
                Niveauprojet u = new Niveauprojet();
                Tools.showConsolLog("//////////////////////////util//////////////////////////////////");
                //u.setId(Integer.parseInt(id));
                u.setAbreviation(abreviation);
                u.setActif(actif);
                u.setNom(nom);
                u.setMetier(metier);
                u.setReleaseprevue(releaseprevue);
                u.setSitedeveloppement(sitedeveloppement);
                u.setTypeprojet(typeprojet);
                d.persist(u);
                String status = "fiiiin";
                d.closeRessources();
                Tools.showConsolLog("login========== " + status);
                PrintWriter out = response.getWriter();
                JSONObject result = new JSONObject();

                //result.put("message",status);
                String jsonResult = JSONObject.toJSONString(result);
                out.println(jsonResult);
            }
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
