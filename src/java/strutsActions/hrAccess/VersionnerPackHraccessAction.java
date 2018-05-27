/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package strutsActions.hrAccess;

import dto.DetailsLivraisonHraccessDTO;
import dto.EnvironnementDTO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
public class VersionnerPackHraccessAction extends org.apache.struts.action.Action {

    private String resultatExecutionAux;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        String connectedUser = Tools.getConnectedLogin();
        Configuration.initialisation();

        try {
            DetailsLivraisonHraccessDTO detailsLivraisonHraccess = (DetailsLivraisonHraccessDTO) request.getSession().getAttribute("detailsLivraisonHraccess");

            T24Scripts t24Scripts = new T24Scripts();
            EnvironnementDTO envirVersionning = Configuration.environnementDTOMapUserHasEnvironnement.get(connectedUser).get("VERSIONNING");

            servlets.AfficherMessageEtatAvancement.executionEnCours.put(connectedUser, true);
            servlets.AfficherMessageEtatAvancement.setLogmessage("Versioning du pack sur le dépot HRaccess", Tools.getConnectedLogin());

            String pathScriptsHr = "/VERSDATA/SCRIPTS_HR/";
            String commande = "SVNVERS_HR_TRUNK " + detailsLivraisonHraccess.getPackName() + " " + detailsLivraisonHraccess.getNumLivraison();
            String resultatExecution = t24Scripts.executerCommandeListEnvironnement(envirVersionning, pathScriptsHr, commande);
            //Analyse log resultat Versionning
            String revision = "";
            resultatExecutionAux = resultatExecution;
            String[] resultatExecutionTab;
            if (resultatExecution.contains("Committed revision")) {
                int positionCommitedRevision = resultatExecution.lastIndexOf("Committed revision");
                resultatExecution = resultatExecution.substring(positionCommitedRevision, resultatExecution.length() - 1);
                revision = resultatExecution.replace(".", "");
                revision = revision.split("\n")[0];
                revision = revision.split(" ")[2];

                String resultatExecutionSauvegarde = resultatExecution;
                resultatExecutionTab = resultatExecutionAux.split("\n");
                resultatExecution = "";
                for (int i = 0; i < resultatExecutionTab.length; i++) {
                    if (resultatExecutionTab[i].trim().startsWith("At revision") || resultatExecutionTab[i].trim().startsWith("Committed revision") || resultatExecutionTab[i].trim().startsWith("VERSIONNING REUSSI")) {
                        resultatExecution += resultatExecutionTab[i].trim() + "\n";
                    }
                }
                if (resultatExecutionSauvegarde.indexOf("Committed revision") == resultatExecutionSauvegarde.lastIndexOf("Committed revision")) {
                    resultatExecution += "\n\n<b>Attention: Le pack a été versionné sur une seule branche!!!</b>";
                }
                revision = revision.replace("\n", "");
                revision = revision.trim();
            } else {
                resultatExecutionTab = resultatExecutionAux.split("\n");
                resultatExecution = "";
                for (int i = 0; i < resultatExecutionTab.length; i++) {
                    if (resultatExecutionTab[i].trim().startsWith("At revision") || resultatExecutionTab[i].trim().startsWith("Committed revision") || resultatExecutionTab[i].trim().startsWith("VERSIONNING REUSSI")) {
                        resultatExecution += resultatExecutionTab[i].trim() + "\n";
                    }
                }
                resultatExecution += "\n\n<b>Attention: Le pack n'a généré aucune révision!!!</b>";
                revision = "AUCUNE";
            }
            String conclusion = "Le versionning du pack " + detailsLivraisonHraccess.getPackName() + " a été effectué convenablement sur le dépot SVN Hraccess";
            String messageTrac = "Le packaging de la LIVRAISON QUALIFICATION TICKET " + detailsLivraisonHraccess.getNumLivraison() + " s est effectue correctement.  [[BR]]"
                    + "Le correctif en question a ete deploye convenablement sur RECETTE [[BR]]nb: Le pack est versionne sur le depot_HRaccess au niveau du trunk a la revision r" + revision;
            detailsLivraisonHraccess.setResultatVersionning(conclusion);
            detailsLivraisonHraccess.setRevision(revision);
            detailsLivraisonHraccess.setMessageTrac(messageTrac);
            servlets.AfficherMessageEtatAvancement.executionEnCours.put(connectedUser, false);
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return mapping.findForward("resultatExecutionHraccess");
    }
}
