/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package strutsActions;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import tools.Configuration;
import dto.DetailsLivraisonDTO;
import entitiesMysql.Livraison;
import t24Scripts.T24Scripts;
import tools.DataBaseTools;
import tools.Tools;

/**
 *
 * @author 04486
 */
public class VersionnerPackAction extends Action {

    String scriptVersionning;
    String resultatVersionning;
    String revision;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, InterruptedException {

        String logException = "";
        try {
            String connectedUser = Tools.getConnectedLogin();
            servlets.AfficherMessageEtatAvancement.executionEnCours.put(connectedUser, true);
            String[] resultatVersionningAuxRevision = new String[2];
            Configuration.initialisation();
            DetailsLivraisonDTO detailsLivraison = Configuration.usersDetailsLivraisonsMap.get(connectedUser);
            if (detailsLivraison == null) {
                logException += " detailsLivraison : null";
            } else {
                logException += detailsLivraison.toString();
            }
            if (detailsLivraison.getCircuit().equals("RELEASE")) {
                scriptVersionning = "SVNVERS_RELEASE";
            } else if (detailsLivraison.getCircuit().equals("PROJET")) {
                scriptVersionning = "SVNVERS_PROJET";
            } else if (detailsLivraison.getCircuit().equals("UPGRADE")) {
                scriptVersionning = "SVNVERS_UPGRADE";
            }
            //Niveauprojet niveauprojet = new DataBaseRequests().getNiveauProjetByAbreviation(detailsLivraison.getNiveauProjet());
            String niveauProjetAbreviation = Configuration.getAbreviationProjetParNiveauProjet(detailsLivraison.getNiveauProjet().trim());
            if (detailsLivraison.isCusExists() == false) {
                resultatVersionningAuxRevision = T24Scripts.versionnerPack(scriptVersionning, detailsLivraison.getNumTicket(), niveauProjetAbreviation, detailsLivraison.getPackName(), connectedUser);
                resultatVersionning = resultatVersionningAuxRevision[0];
                revision = resultatVersionningAuxRevision[1];
            } else {
                resultatVersionningAuxRevision = T24Scripts.versionnerPack(scriptVersionning, detailsLivraison.getNumTicket(), niveauProjetAbreviation, detailsLivraison.getPackName() + ".BNK", connectedUser);
                resultatVersionning = resultatVersionningAuxRevision[0];
                revision = resultatVersionningAuxRevision[1];
                resultatVersionningAuxRevision = T24Scripts.versionnerPack(scriptVersionning, detailsLivraison.getNumTicket(), niveauProjetAbreviation, detailsLivraison.getPackName() + ".TN1", connectedUser);
                resultatVersionning += "%_%" + resultatVersionningAuxRevision[0];
                revision += "%_%" + resultatVersionningAuxRevision[1];
            }
            detailsLivraison.setRevison(revision);
            detailsLivraison.setResultatVersionning(resultatVersionning);


            //insertion de l'objet Livraison dans la base
            DataBaseTools dbTools = new DataBaseTools(Configuration.puOvTools);
            Livraison liv = dbTools.em.find(Livraison.class, Integer.parseInt(detailsLivraison.getNumTicket()));
            if (liv == null) {
                liv = new Livraison(Integer.parseInt(detailsLivraison.getNumTicket()));
            }

            String selectedMnemonic = detailsLivraison.getMnemonicCompany();
            String packName = detailsLivraison.getPackName();
            int nbrIter = detailsLivraison.getNbrIterationDeploiement();
            String objetsT24 = detailsLivraison.getListeObjets();
            boolean nePasEcraserLivrable = detailsLivraison.isNePasEcraserLivrable();
            if (!nePasEcraserLivrable) {
                byte[] serializedLivraisonList = Tools.createLivraisonList(selectedMnemonic, packName, nbrIter, objetsT24);
                liv.setLivrables(serializedLivraisonList);
            }
            liv.setNumeroAnomalie(Integer.parseInt(detailsLivraison.getAnomalie().trim()));
            liv.setContenuLivrables(detailsLivraison.getContenuDesLivrables());
            liv.setOwner(detailsLivraison.getOwner());
            liv.setReporter(detailsLivraison.getReporter());
            liv.setNomPack(packName);
            liv.setNombreIterations(nbrIter);
            liv.setMessageTrac("");
            liv.setListeObjets(objetsT24);
            liv.setType(detailsLivraison.getPhase());
            liv.setCompanyDeploiement(selectedMnemonic);
            dbTools.update(liv);
            dbTools.closeRessources();

            Configuration.usersDetailsLivraisonsMap.put(connectedUser, detailsLivraison);

            servlets.AfficherMessageEtatAvancement.executionEnCours.put(connectedUser, false);
            servlets.AfficherMessageEtatAvancement.setLogmessage("", Tools.getConnectedLogin());
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(logException + "\n\n\n\n" + tools.Tools.getStackTrace(exep));
        }
        return mapping.findForward("resultatVersionnerPack");
    }
}
