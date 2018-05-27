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
import strutsForms.hrAccess.ExporterPackHraccessForm;
import t24Scripts.T24Scripts;
import tools.Configuration;
import tools.Tools;

/**
 *
 * @author 04494
 */
public class ExporterPackHraccessAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, InterruptedException {

        boolean resultat = false;
        String connectedUser = Tools.getConnectedLogin();
        Configuration.initialisation();
        String conclusion = "";

        try {
            ExporterPackHraccessForm inF = (ExporterPackHraccessForm) form;
            String numLivraison = inF.getNumLivraison();
            String textAreaObjectList = inF.getTextAreaObjectList();
            String natureTraitement = inF.getNatureTraitement();
            String packName = inF.getPackName();

            T24Scripts t24Scripts = new T24Scripts();
            DetailsLivraisonHraccessDTO detailsLivraisonHraccess = null;
            EnvironnementDTO envirVersionning = Configuration.environnementDTOMapUserHasEnvironnement.get(connectedUser).get("VERSIONNING");
            EnvironnementDTO environnementSource = Configuration.environnementDTOMapUserHasEnvironnement.get(connectedUser).get("HR_DEV");
            EnvironnementDTO environnementDestination = Configuration.environnementDTOMapUserHasEnvironnement.get(connectedUser).get("HR_REC");
            servlets.AfficherMessageEtatAvancement.executionEnCours.put(connectedUser, true);
            servlets.AfficherMessageEtatAvancement.setLogmessage("Export du pack à partir de l'environnement " + environnementSource.getNom(), Tools.getConnectedLogin());

            String pathScriptsHr = "/VERSDATA/SCRIPTS_HR/";
            String commande = "EXPORT_HR \"" + textAreaObjectList + "\" " + packName + " " + environnementSource.getAbreviationNom() + " " + environnementSource.getUrl() + " " + environnementSource.getEnvUserName() + " " + environnementSource.getEnvPassword() + " " + environnementSource.getNom().replaceAll("HR_", "") + " " + environnementDestination.getNom().replaceAll("HR_", "");
            String resultatExecution = t24Scripts.executerCommandeListEnvironnement(envirVersionning, pathScriptsHr, commande);

            conclusion = "Une erreur s est produite lors de l export du pack " + packName + " a partir de l environnement " + environnementDestination.getNom() + ". Veuillez consulter le fichier de log.";
            //Analyse log resultat Export
            if (resultatExecution.contains("BBAD0010")) {
                resultat = true;
                conclusion = "L'export " + textAreaObjectList + " a été effectué convenablement à partir de l'environnement " + environnementSource.getNom() + ".\n Le pack " + packName + " a été créé sous le serveur VERSION";
            }
            detailsLivraisonHraccess = new DetailsLivraisonHraccessDTO(packName, numLivraison, conclusion, "", "", "", natureTraitement, "", "", textAreaObjectList, environnementSource.getNom(), "", "");
            request.getSession().setAttribute("detailsLivraisonHraccess", detailsLivraisonHraccess);
            servlets.AfficherMessageEtatAvancement.executionEnCours.put(connectedUser, false);
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        if (resultat) {
            return mapping.findForward("resultatExportHraccess");
        } else {
            return Tools.redirectionPageErreurs("Erreur Export", conclusion, mapping, request, response, connectedUser);
        }
    }
}
