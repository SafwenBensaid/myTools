/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import dto.EnvironnementDTO;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tools.Configuration;
import dataBaseTracRequests.DataBaseTracRequests;
import t24Scripts.T24Scripts;
import tools.Tools;

/**
 *
 * @author 04486
 */
public class GetTicketDetailsById extends HttpServlet {

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
        String connectedUser = Tools.getConnectedLogin();
        servlets.AfficherMessageEtatAvancement.executionEnCours.put(connectedUser, true);
        try {
            String abreviation = "";
            String erreurs = "";
            String champs = "";
            String niveauProjet = "";
            String numeroAnomalie = "";
            String natureTraitement = "";
            String natureLivraison = "";
            String packName = "";
            String contenuDesLivrables = "";
            boolean livraisonProjet = false;
            boolean livraisonHotfix = false;
            boolean livraisonRelease = false;
            boolean livraisonUpgrade = false;
            String type = "";
            String priority = "";
            String milestone = "";
            String component = "";

            servlets.AfficherMessageEtatAvancement.setLogmessage("Analyse du ticket TRAC", Tools.getConnectedLogin());
            String ticketIdString = request.getParameter("ticketId");
            String acteur = request.getParameter("acteur");
            int ticketId = Integer.parseInt(ticketIdString);
            Tools.showConsolLog("ticketId: " + ticketId);

            DataBaseTracRequests requete = new DataBaseTracRequests();
            String[] cles = new String[]{"projet", "ticket_origine", "nature_trait", "nature_liv", "contenu_des_livrables"};
            Map<String, String> mapResultat = requete.getTicketCustomLivraisonByTicketLivraisonIdAndName(ticketId, Configuration.puLivraisons, Configuration.tracLivraisons, cles);


            String contenu = mapResultat.get("CONTENU");
            if (contenu.equals("VIDE")) {
                erreurs += "Le numéro de livraison #" + ticketId + " ne correspond à aucun ticket<br/>";
            } else {
                niveauProjet = mapResultat.get("projet");
                numeroAnomalie = mapResultat.get("ticket_origine");
                natureTraitement = mapResultat.get("nature_trait");
                natureLivraison = mapResultat.get("nature_liv");
                contenuDesLivrables = mapResultat.get("contenu_des_livrables");
                type = mapResultat.get("type");
                priority = mapResultat.get("priority");
                milestone = mapResultat.get("milestone");
                component = mapResultat.get("component");

                //Nom du pack
                if (type.equals("QUALIFICATION") || type.equals("CERTIFICATION")) {
                    packName += "LIVR.";
                    livraisonRelease = true;
                } else if (type.equals("HOT FIXE TEST") || type.equals("ACTION A CHAUD TEST") || type.equals("HOT FIXE PROD")) {
                    packName += "LIVH.";
                    livraisonHotfix = true;
                } else if (type.equals("QUALIFICATION_PROJET")) {
                    packName += "LIVP.";
                    livraisonProjet = true;
                } else if (type.equals("QUALIFICATION_UPGRADE") || type.equals("CERTIFICATION_UPGRADE")) {
                    packName += "LIVU.";
                    livraisonUpgrade = true;
                }
                //test compatibilité type vs priorité
                if (priority.equals("OBJET LIVREE") || priority.equals("PRET POUR DEPLOIEMENT")) {
                    if (!type.equals("QUALIFICATION") && !type.equals("CERTIFICATION") && !type.equals("HOT FIXE TEST") && !type.equals("ACTION A CHAUD TEST") && !type.equals("QUALIFICATION_PROJET") && !type.equals("HOT FIXE PROD") && !type.equals("CERTIFICATION_UPGRADE") && !type.equals("QUALIFICATION_UPGRADE")) {
                        erreurs += "Veuillez vérifier le champ 'Type' (" + type + ") dans le ticket Trac<br/>";
                    }
                }

                /* ANIS                
                 if (priority.equals("DEPLOYEE")) {
                 if (type.equals("HOT FIXE PROD")) {
                 erreurs += "Veuillez vérifier le champ 'Type' (" + type + ") dans le ticket Trac<br/>";
                 }
                 }
                 */

                //Vérifier l'existance du niveau projet dans la base
                if (livraisonHotfix == true) {
                    abreviation = "MAIN";
                } else {
                    abreviation = Configuration.getAbreviationProjetParNiveauProjet(niveauProjet);
                }
                if (abreviation == null) {
                    erreurs += "Le niveau projet est vide ou ne figure pas dans la base de données de paramétrages<br/>";
                } else if (abreviation.trim().length() == 0) {
                    if (livraisonRelease == true || livraisonProjet == true || livraisonUpgrade == true) {
                        erreurs += "Le niveau projet est vide ou ne figure pas dans la base de données de paramétrages<br/>";
                    }
                } else {
                    //Vérifie l'existance de la branche individuelle dans l'environnement de versionning
                    erreurs += brancheIndividuelleExist(abreviation);
                    if (livraisonRelease == true) {
                        if (!Configuration.projetsActifsCircuitReleaseList.contains(abreviation)) {
                            erreurs += "Le niveau projet '" + Configuration.getNomProjetParAbreviation(abreviation) + "' n'appartient pas au circuit Release<br/>";
                        }
                    } else if (livraisonProjet == true) {
                        if (!Configuration.projetsActifsCircuitProjetList.contains(abreviation)) {
                            erreurs += "Le niveau projet '" + Configuration.getNomProjetParAbreviation(abreviation) + "' n'appartient pas au circuit Projet<br/>";
                        }
                    } else if (livraisonUpgrade == true) {
                        if (!Configuration.projetsActifsCircuitUpgradeList.contains(abreviation)) {
                            erreurs += "Le niveau projet '" + Configuration.getNomProjetParAbreviation(abreviation) + "' n'appartient pas au circuit Upgrade<br/>";
                        }
                    }

                    if (livraisonHotfix == true) {
                        packName += "MAIN." + ticketId;
                    } else {
                        packName += abreviation + "." + ticketId;
                    }
                }
            }
            if (acteur.equals("OV") && !type.equals("HOT FIXE PROD") && contenuDesLivrables.trim().equals("A DEFINIR")) {
                //if (contenuDesLivrables == null ||(!type.equals("HOT FIXE PROD") && contenuDesLivrables.trim().equals("A DEFINIR"))) {
                erreurs += "Veuillez définir le contenu des livrables sur le ticket trac<br/>";
            }
            champs = niveauProjet + "%_%" + numeroAnomalie + "%_%" + type + "%_%" + priority + "%_%" + milestone + "%_%" + component + "%_%" + natureTraitement + "%_%" + natureLivraison + "%_%" + packName + "%_%" + contenuDesLivrables;
            Tools.showConsolLog(champs + "%*%" + erreurs);
            //servlets.AfficherMessageEtatAvancement.setLogmessage("");
            servlets.AfficherMessageEtatAvancement.executionEnCours.put(connectedUser, false);
            out.println(champs + "$*$" + erreurs);
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
    /*
     public static void main(String[] args){
     Configuration.initialisation();
     GetTicketDetailsById g = new  GetTicketDetailsById();
     Tools.showConsolLog(g.brancheIndividuelleExist("BFIM"));
     }
     */

    public String brancheIndividuelleExist(String projectAbreviation) {
        try {
            T24Scripts t24Scripts = new T24Scripts();
            String connectedUser = Tools.getConnectedLogin();
            EnvironnementDTO envirVersionning = Configuration.environnementDTOMapUserHasEnvironnement.get(connectedUser).get("VERSIONNING");
            String brancheIndividuellePath = "/VERSDATA/LWorkings/LW_RELEASE/depot_release/branches/DELTA.PROJET/" + projectAbreviation + "/TAF-FULLPACK";
            boolean folderExists = t24Scripts.testExistanceDossier(envirVersionning, brancheIndividuellePath, "");
            if (folderExists == false) {
                return "Veuillez créer le répertoire suivant dans l'environnement de versionning:<br/>" + brancheIndividuellePath + "<br/>";
            } else {
                return "";
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        } finally {
            return "";
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
