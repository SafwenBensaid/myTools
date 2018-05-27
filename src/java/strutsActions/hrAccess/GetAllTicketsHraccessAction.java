/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package strutsActions.hrAccess;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import tools.Configuration;
import dataBaseTracRequests.DataBaseTracRequests;
import tools.SessionTools;
import tools.Tools;

/**
 *
 * @author 04494
 */
public class GetAllTicketsHraccessAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        //Vider les variables de la session
        SessionTools.clearSessionVariables(request);
        String acteur = request.getParameter("acteur");
        if (acteur == null) {
            return mapping.findForward("accueil");
        }
        String connectedUser = Tools.getConnectedLogin();
        try {
            servlets.AfficherMessageEtatAvancement.executionEnCours.put(connectedUser, true);
            servlets.AfficherMessageEtatAvancement.setLogmessage("Chargement des paramètres", Tools.getConnectedLogin());

            Configuration.initialisation();
            Tools.showConsolLog("--------- getAllTicketsHraccessAction -----------");
            Map<String, List<Map<String, Object>>> mapPipeTicketsHraccess = new DataBaseTracRequests().getAllPipeTicketsHraccessRequestHR(acteur, Configuration.puHraccess);
            request.getSession().setAttribute("mapPipeTicketsHraccess", mapPipeTicketsHraccess);
            servlets.AfficherMessageEtatAvancement.setLogmessage("", Tools.getConnectedLogin());
            servlets.AfficherMessageEtatAvancement.executionEnCours.put(connectedUser, false);
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }

        try {
            //Dans l'affichage du tableau des livraisons en cliquant sur le menu ou en finalisant une
            //livraison, on utilisece bloque.
            //Sinon lorsqu'il s'agit juste de notifier dans le wiki, la redirection se fait au niveau de 
            //javascript function "submitDataGestionDesLivraisonsInput"
            if (acteur.equals("OVHR")) {
                return mapping.findForward("gestionDesLivraisonsHraccessOV");
            } else if (acteur.equals("IEHR")) {
                return mapping.findForward("gestionDesLivraisonsHraccessIE");
            } else {
                return mapping.findForward("accueil");
            }
        } catch (Exception ex) {
            return mapping.findForward("accueil");
        }

    }
}
