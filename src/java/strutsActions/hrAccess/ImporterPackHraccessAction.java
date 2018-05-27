/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package strutsActions.hrAccess;

import dto.DetailsLivraisonHraccessDTO;
import dto.EnvironnementDTO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import t24Scripts.T24Scripts;
import tools.Configuration;
import tools.Tools;

/**
 *
 * @author 04494
 */
public class ImporterPackHraccessAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, InterruptedException {

        boolean resultat = false;
        String connectedUser = Tools.getConnectedLogin();
        servlets.AfficherMessageEtatAvancement.executionEnCours.put(connectedUser, true);
        Configuration.initialisation();
        String conclusion = "";

        try {
            DetailsLivraisonHraccessDTO detailsLivraisonHraccess = (DetailsLivraisonHraccessDTO) request.getSession().getAttribute("detailsLivraisonHraccess");

            T24Scripts t24Scripts = new T24Scripts();
            EnvironnementDTO environnementDestination = null;
            EnvironnementDTO envirVersionning = null;
            envirVersionning = Configuration.environnementDTOMapUserHasEnvironnement.get(connectedUser).get("VERSIONNING");
            environnementDestination = Configuration.environnementDTOMapUserHasEnvironnement.get(connectedUser).get("HR_REC");

            servlets.AfficherMessageEtatAvancement.executionEnCours.put(connectedUser, true);
            servlets.AfficherMessageEtatAvancement.setLogmessage("Import du pack dans l'environnement " + environnementDestination.getNom(), Tools.getConnectedLogin());

            String pathScriptsHr = "/VERSDATA/SCRIPTS_HR/";
            String commande = "IMPORT_HR " + detailsLivraisonHraccess.getPackName() + " " + detailsLivraisonHraccess.getNumLivraison() + " " + environnementDestination.getAbreviationNom() + " " + environnementDestination.getUrl() + " " + environnementDestination.getEnvUserName() + " " + environnementDestination.getEnvPassword();
            String resultatExecution = t24Scripts.executerCommandeListEnvironnement(envirVersionning, pathScriptsHr, commande);
            conclusion = "Une erreur s'est produite lors de l'import du pack " + detailsLivraisonHraccess.getPackName() + " sur l'environnement " + environnementDestination.getNom() + ". Veuillez consulter le fichier de log sous /VERSDATA/SCRIPTS_HR/LOG_IMPORT.";
            //Analyse log resultat Import
            if (resultatExecution.contains("BBAD0010")) {
                resultat = true;
                conclusion = "L'import du pack " + detailsLivraisonHraccess.getPackName() + " a été effectué convenablement sur l'environnement " + environnementDestination.getNom() + ".\n A enchainer avec le reste des actions manuelles";
            }
            detailsLivraisonHraccess.setResultatImport(conclusion);
            servlets.AfficherMessageEtatAvancement.executionEnCours.put(connectedUser, false);
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        if (resultat) {
            return mapping.findForward("resultatImportHraccess");
        } else {
            return Tools.redirectionPageErreurs("Erreur Import", conclusion, mapping, request, response, connectedUser);
        }
    }
}
