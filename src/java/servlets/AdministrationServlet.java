/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import entitiesMysql.Audit;
import entitiesMysql.Environnement;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import tools.Configuration;
import tools.CrudTools;
import tools.DataBaseTools;
import tools.Tools;

/**
 *
 * @author karim
 */
public class AdministrationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String typeReq = request.getParameter("typeModf");
            String resultat;
            if (typeReq.equals("supp")) {
                String valeur = request.getParameter("id");
                response.setContentType("text/xml");
                response.setHeader("Cache-Control", "no-cache");
                CrudTools<Environnement> d = new CrudTools<>(Configuration.puOvTools);
                Environnement u = d.findById("Environnement", valeur);
                resultat = "votre id au serv = " + valeur + "\n nous avons supprimé" + u.getAbreviationNom() + " avec succès";
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
                valeur = valeur.replace("ET_COMMERCIAL", "&");
                valeur = valeur.replace("DIESE", "#");
                valeur = valeur.replace("PLUS", "+");
                valeur = valeur.replace("DOLLAR", "$");
                String id = request.getParameter("id");
                CrudTools<Environnement> d = new CrudTools<Environnement>(Configuration.puOvTools);
                d.update(id, champ, valeur, "Environnement");
                d.closeRessources();
                PrintWriter out = response.getWriter();
                Tools.showConsolLog("/updaaaaaaaaaaaaaaate**************************** servlets ");
                resultat = "mise a jours avec succès de champ" + champ + " : " + valeur;
                out.println("<message>" + resultat + "</message>");
                try {
                    out.close();
                } catch (Exception exp) {
                }
            } else if (typeReq.equals("verifExistance")) {
                PrintWriter out = response.getWriter();
                String val = request.getParameter("val");
                String champ = request.getParameter("champ");
                CrudTools<Environnement> d = new CrudTools<Environnement>(Configuration.puOvTools);
                int t = d.find("Environnement", champ, val);
                String rep;
                if (t == 0) {
                    rep = "ok";
                } else if (t == 1) {
                    rep = "unique";
                } else {
                    rep = "fail";
                }
                Tools.showConsolLog("--------------------------------dans la bdd --: " + t);
                d.closeRessources();
                out.println(rep);

            } else {
                response.setContentType("text/json");
                String json = request.getParameter("json");
                JSONObject jsonData = (JSONObject) JSONValue.parse(json);
                //récupération des données
                String envUserName = ((String) jsonData.get("envUserName"));
                String nom = ((String) jsonData.get("nom"));
                String url = ((String) jsonData.get("url"));
                String type = ((String) jsonData.get("type"));
                String envPassword = ((String) jsonData.get("envPassword"));
                String abreviationNom = ((String) jsonData.get("abreviationNom"));


                //ajout dans la base de données
                CrudTools<Environnement> d = new CrudTools<Environnement>(Configuration.puOvTools);
                Environnement u = new Environnement();

                u.setAbreviationNom(abreviationNom);
                u.setNom(nom);
                u.setEnvPassword(envPassword);
                u.setEnvUserName(envUserName);
                u.setUrl(url);
                u.setType(type);
                u.setPort(21);
                d.persist(u);
                d.closeRessources();
                PrintWriter out = response.getWriter();
                JSONObject result = new JSONObject();

                //result.put("message",status);
                String jsonResult = JSONObject.toJSONString(result);
                out.println(jsonResult);
            }
            Audit audit = new Audit();
            audit.setUpdateTime(new Date());
            audit.setAction("OPERATION_ON_DB");
            DataBaseTools dbTools = new DataBaseTools(Configuration.puOvTools);
            dbTools.StoreObjectIntoDataBase(audit);
            dbTools.closeRessources();
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp); //To change body of generated methods, choose Tools | Templates.
        //doGet(req, resp);
    }
}
