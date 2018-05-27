/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import dto.EnvironnementDTO;
import entitiesMysql.ObjetsHorsReferentiel;
import java.io.*;
import java.util.*;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;
import tools.*;
import t24Scripts.*;

/**
 *
 * @author 04486
 */
public class ServletInitialisationReferentielAction extends HttpServlet {

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
    StringBuilder resultat;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            //Vider les variables de la session
            SessionTools.clearSessionVariables(request);

            String connectedUser = Tools.getConnectedLogin();

            servlets.AfficherMessageEtatAvancement.executionEnCours.put(connectedUser, true);

            Tools tools = new Tools();
            Configuration.initialisation();
            EnvironnementDTO environnement = Configuration.environnementDTOMapUserHasEnvironnement.get(connectedUser).get("VERSIONNING");
            resultat = new StringBuilder();

            Map<String, List<String>> mapObjetsDepot = getObjectsFromDeposit(environnement);

            DataBaseTools dbTools = new DataBaseTools(Configuration.puOvTools);
            List<ObjetsHorsReferentiel> listeAcocherReleaseGLOB = (List<ObjetsHorsReferentiel>) getObjectsFromDatabase(dbTools, "ReleaseGLOB");
            List<ObjetsHorsReferentiel> listeAcocherReleaseELEM = (List<ObjetsHorsReferentiel>) getObjectsFromDatabase(dbTools, "ReleaseELEM");
            List<ObjetsHorsReferentiel> listeAcocherProjetGLOB = (List<ObjetsHorsReferentiel>) getObjectsFromDatabase(dbTools, "ProjetGLOB");
            List<ObjetsHorsReferentiel> listeAcocherProjetELEM = (List<ObjetsHorsReferentiel>) getObjectsFromDatabase(dbTools, "ProjetELEM");
            List<ObjetsHorsReferentiel> listeAcocherProductionGLOB = (List<ObjetsHorsReferentiel>) getObjectsFromDatabase(dbTools, "ProductionGLOB");
            List<ObjetsHorsReferentiel> listeAcocherProductionELEM = (List<ObjetsHorsReferentiel>) getObjectsFromDatabase(dbTools, "ProductionELEM");
            dbTools.closeRessources();

            String jsonObjetsDepot = tools.objectToJsonString(mapObjetsDepot);

            request.getSession().setAttribute("JsonObjetsDepot", jsonObjetsDepot);
            request.getSession().setAttribute("ListeAcocherReleaseGLOB", listeAcocherReleaseGLOB);
            request.getSession().setAttribute("ListeAcocherReleaseELEM", listeAcocherReleaseELEM);
            request.getSession().setAttribute("ListeAcocherProjetGLOB", listeAcocherProjetGLOB);
            request.getSession().setAttribute("ListeAcocherProjetELEM", listeAcocherProjetELEM);
            request.getSession().setAttribute("ListeAcocherProductionGLOB", listeAcocherProductionGLOB);
            request.getSession().setAttribute("ListeAcocherProductionELEM", listeAcocherProductionELEM);

            servlets.AfficherMessageEtatAvancement.executionEnCours.put(connectedUser, false);

            /* TODO output your page here. You may use following sample code. */
            out.println(resultat.toString());
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

    public List<ObjetsHorsReferentiel> getObjectsFromDatabase(DataBaseTools dbTools, String circuit) {
        List<ObjetsHorsReferentiel> typeObjetList = null;
        try {
            Query q = dbTools.em.createNamedQuery("ObjetsHorsReferentiel.findByCircuit");
            q.setParameter("circuit", circuit);
            q.setHint(QueryHints.REFRESH, HintValues.TRUE);
            typeObjetList = (List<ObjetsHorsReferentiel>) q.getResultList();
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return (typeObjetList);
    }

    public Map<String, List<String>> getObjectsFromDeposit(EnvironnementDTO environnement) {
        List<String> objectsListRelease = new ArrayList<String>();
        List<String> objectsListProjet = new ArrayList<String>();
        List<String> objectsListProduction = new ArrayList<String>();
        Map<String, List<String>> mapObjetsDepot = new LinkedHashMap<String, List<String>>();
        try {
            T24Scripts t24Scripts = new T24Scripts();
            String[] listeCommandes = new String[6];
            String dateActuelle = "" + System.currentTimeMillis();
            listeCommandes[0] = "cd " + Configuration.parametresList.get("cheminDepotRelese");
            listeCommandes[1] = "ls -F | grep / > /work/" + dateActuelle + "listOfTypesRelease.txt";
            listeCommandes[2] = "cd " + Configuration.parametresList.get("cheminDepotProjet");
            listeCommandes[3] = "ls -F | grep / > /work/" + dateActuelle + "listOfTypesProjet.txt";
            listeCommandes[4] = "cd " + Configuration.parametresList.get("cheminDepotTrunk");
            listeCommandes[5] = "ls -F | grep / > /work/" + dateActuelle + "listOfTypesProduction.txt";
            t24Scripts.executerCommandeListEnvironnement(environnement, "/work", listeCommandes);

            FtpTools ftpTools = new FtpTools();
            ftpTools.downloadFile(environnement, "/work/", dateActuelle + "listOfTypesRelease.txt");
            ftpTools.downloadFile(environnement, "/work/", dateActuelle + "listOfTypesProjet.txt");
            ftpTools.downloadFile(environnement, "/work/", dateActuelle + "listOfTypesProduction.txt");

            Tools tools = new Tools();
            String[] fileTabRelease = tools.convertFileContentToTab(Configuration.parametresList.get("espaceLocal") + System.getProperty("file.separator") + dateActuelle + "listOfTypesRelease.txt");
            String[] fileTabProjet = tools.convertFileContentToTab(Configuration.parametresList.get("espaceLocal") + System.getProperty("file.separator") + dateActuelle + "listOfTypesProjet.txt");
            String[] fileTabProduction = tools.convertFileContentToTab(Configuration.parametresList.get("espaceLocal") + System.getProperty("file.separator") + dateActuelle + "listOfTypesProduction.txt");

            for (int i = 0; i < fileTabRelease.length; i++) {
                if (fileTabRelease[i].length() > 0) {
                    String tab = fileTabRelease[i].substring(0, fileTabRelease[i].length() - 1);
                    objectsListRelease.add(tab);
                }
            }

            for (int i = 0; i < fileTabProjet.length; i++) {
                if (fileTabProjet[i].length() > 0) {
                    String tab = fileTabProjet[i].substring(0, fileTabProjet[i].length() - 1);
                    objectsListProjet.add(tab);
                }
            }

            for (int i = 0; i < fileTabProduction.length; i++) {
                if (fileTabProduction[i].length() > 0) {
                    String tab = fileTabProduction[i].substring(0, fileTabProduction[i].length() - 1);
                    objectsListProduction.add(tab);
                }
            }

            mapObjetsDepot.put("Release", objectsListRelease);
            mapObjetsDepot.put("Projet", objectsListProjet);
            mapObjetsDepot.put("Production", objectsListProduction);

            //formation des accordeons
            resultat.append("<div class='accordionButton' id='referentielRelease'>Administration du référentiel d'objets Release (" + objectsListRelease.size() + " types d'objets) </div>");
            resultat.append("<div class='accordionContent' id='contenuAccordeonRelease'>");
            resultat.append("</div>");

            resultat.append("<div class='accordionButton' id='referentielProjet'  >Administration du référentiel d'objets Projet (" + objectsListProjet.size() + " types d'objets) </div>");
            resultat.append("<div class='accordionContent' id='contenuAccordeonProjet'>");
            resultat.append("</div>");

            resultat.append("<div class='accordionButton' id='referentielPROD'  >Administration du référentiel d'objets PROD (" + objectsListProduction.size() + " types d'objets) </div>");
            resultat.append("<div class='accordionContent' id='contenuAccordeonHotfix'>");
            resultat.append("</div>");



        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }

        return mapObjetsDepot;


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
