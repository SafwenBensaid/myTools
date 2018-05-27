package servlets;

import entitiesMysql.Fonctionalite;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tools.Configuration;
import dataBaseTracRequests.DataBaseTracRequests;
import tools.DataBaseTools;

public class AdministrationFonctionnalitesServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String typeReq = request.getParameter("typeModf");
        DataBaseTools dbTools = new DataBaseTools(Configuration.puOvTools);
        DataBaseTracRequests dbRequests = new DataBaseTracRequests();

        try {
            if (typeReq.equals("supp")) {
                String foncName = request.getParameter("fonct").trim();
                Fonctionalite fonc = dbTools.em.find(Fonctionalite.class, foncName);
                dbTools.remove(fonc);
            } else if (typeReq.equals("add")) {
                String nomFonct = request.getParameter("nomFonct");
                String descriptionFonct = request.getParameter("descriptionFonct");
                String typeFonct = request.getParameter("typeFonct");
                Fonctionalite f = new Fonctionalite();
                f.setName(nomFonct.replaceAll(" ", "_").toUpperCase());
                f.setDescription(descriptionFonct);
                f.setType(typeFonct);
                dbTools.StoreObjectIntoDataBase(f);

            } else if (typeReq.equals("edit")) {
                String nom = request.getParameter("nom").trim();
                String description = request.getParameter("description").trim();
                String type = request.getParameter("type").trim();
                dbRequests.updatefonctionnalite(dbTools, nom, description, type);
            }
            Configuration.getAllFoncionnalites(dbTools);
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        } finally {
            dbTools.closeRessources();
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
