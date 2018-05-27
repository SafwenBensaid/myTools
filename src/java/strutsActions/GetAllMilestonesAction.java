/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package strutsActions;

import dataBaseTracRequests.DataBaseTracRequests;
import entitiesMysql.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import strutsForms.GetAllMilestonesForm;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;
import tools.*;

/**
 *
 * @author 04486
 */
public class GetAllMilestonesAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        //Vider les variables de la session
        SessionTools.clearSessionVariables(request);

        String acteur = request.getParameter("acteur");
        if (acteur == null) {
            return mapping.findForward("accueil");
        }

        try {
            String connectedUser = Tools.getConnectedLogin();
            /*
             //supprimer la variable stockée de la session
             try {
             request.getSession().removeAttribute("detailsLivraison" + connectedUser);
             } catch (Exception exep) {
             exep.printStackTrace();
             tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
             }
             */
            Tools.clearMapTicketsEnCours(connectedUser);
            servlets.AfficherMessageEtatAvancement.executionEnCours.put(connectedUser, true);
            servlets.AfficherMessageEtatAvancement.setLogmessage("Chargement des paramètres", Tools.getConnectedLogin());

            Configuration.initialisation();

            GetAllMilestonesForm inF = (GetAllMilestonesForm) form;
            DataBaseTools dbTools = new DataBaseTools(Configuration.puOvTools);
            Query q;
            Tools.showConsolLog("--------- getAllMilestoneAction -----------");
            Map<String, List<Map<String, Object>>> mapPipeTickets = new DataBaseTracRequests().getAllPipeTicketsRequestT24(acteur, Configuration.puLivraisons);

            q = dbTools.em.createNamedQuery("Niveauprojet.findAllNiveauProjets");
            q.setHint(QueryHints.REFRESH, HintValues.TRUE);
            List<Niveauprojet> niveauProjetList = (List<Niveauprojet>) q.getResultList();
            String[] niveauProjetArray = new String[niveauProjetList.size()];
            niveauProjetArray = niveauProjetList.toArray(niveauProjetArray);
            inF.setNiveauProjetList(niveauProjetArray);
            request.getSession().setAttribute("niveauProjetArray", niveauProjetArray);
            request.getSession().setAttribute("mapPipeTickets", mapPipeTickets);
            dbTools.closeRessources();

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
            if (acteur.equals("OV")) {
                return mapping.findForward("gestionDesLivraisonsOV");
            } else if (acteur.equals("IE")) {
                return mapping.findForward("gestionDesLivraisonsIE");
            } else if (acteur.equals("CDD")) {
                return mapping.findForward("gestionDesLivraisonsCDD");
            } else {
                return null;
            }
        } catch (Exception ex) {
            return mapping.findForward("accueil");
        }

    }
    /*
     public static void main(String[]args){
        
     DataBaseRequests req = new DataBaseRequests();
     String allPipesTickets= req.getAllPipeTickets();
     Tools tools = new Tools();
     LinkedHashMap<String,LinkedProperties> mapPipeTickets=tools.transformAllPipeTickets(allPipesTickets);        
     }
     */
}
