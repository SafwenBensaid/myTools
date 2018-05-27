/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package strutsActions;

import dto.CoupleDTO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import java.util.*;
import strutsForms.*;
import tools.Configuration;
import tools.Tools;

/**
 *
 * @author 04486
 */
public class ChargementParametresListesDeroulantes extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, InterruptedException {

        //Vider les variables de la session
        //      SessionTools.clearSessionVariables(request);

        String connectedUser = Tools.getConnectedLogin();
        servlets.AfficherMessageEtatAvancement.executionEnCours.put(connectedUser, true);

        Configuration.initialisation();

        //PREPARER LES LISTES DEROULANTES
        Set<String> envNameSet = Configuration.environnementDTOMapUserHasEnvironnement.get(connectedUser).keySet();
        String[] arrayEnvName = envNameSet.toArray(new String[0]);


        List<String> usersList = Configuration.usersNamesList;
        String[] arrayUsersName = new String[usersList.size()];
        arrayUsersName = usersList.toArray(arrayUsersName);

        List<String> projetsActifs = new ArrayList<String>(Configuration.projetsActifsCircuitReleaseEtProjetList);
        projetsActifs.add(0, "C.PROJET");
        projetsActifs.add(0, "C.RELEASE");
        projetsActifs.add(0, "C.UPGRADE");
        projetsActifs.add(0, "TRUNK");
        String[] depotList = projetsActifs.toArray(new String[projetsActifs.size()]);

        String destination = "";
        try {
            if (form instanceof VerificationDeltaForm) {
                VerificationDeltaForm inF = (VerificationDeltaForm) form;
                inF.setEnvironnementList(arrayEnvName);
                destination = "VerificationDelta";
            } else if (form instanceof PreparationFormerUnPackForm) {
                PreparationFormerUnPackForm inF = (PreparationFormerUnPackForm) form;
                inF.setEnvironnementList(arrayEnvName);
                inF.setDepotList(depotList);
                destination = "FormerUnPack";
            } else if (form instanceof PreparationComparaisonObjetsForm) {
                PreparationComparaisonObjetsForm inF = (PreparationComparaisonObjetsForm) form;
                inF.setEnvironnementList(arrayEnvName);
                inF.setDepotList(depotList);
                destination = "ComparaisonObjets";
            } else if (form instanceof EtudeIntersectionInputObjetsForm) {
                EtudeIntersectionInputObjetsForm inF = (EtudeIntersectionInputObjetsForm) form;
                inF.setEnvironnementList(arrayEnvName);
                destination = "EtudeIntersection";
            } else if (form instanceof LoginForm) {
                destination = "login";
            } else if (form instanceof GestionLivraisonsInputPacksEnvIE_Form) {
                GestionLivraisonsInputPacksEnvIE_Form inF = (GestionLivraisonsInputPacksEnvIE_Form) form;

                String priority = inF.getPriority();
                String type = inF.getPhase();
                String natureLivraison = inF.getNatureLivraison();
                String circuit;
                if (priority.trim().equals("PRET POUR DEPLOIEMENT") && type.trim().equals("HOT FIXE PROD") && natureLivraison.equals("HARMONISATION_C.PROJET")) {
                    circuit = "HARMONISATION_C.PROJET";
                } else if (priority.trim().equals("PRET POUR DEPLOIEMENT") && type.trim().equals("HOT FIXE PROD") && natureLivraison.equals("HARMONISATION_C.RELEASE")) {
                    circuit = "HARMONISATION_C.RELEASE";
                } else {
                    circuit = inF.getCircuit();
                }

                CoupleDTO circuit_livraison = new CoupleDTO(circuit, inF.getNumLivraison());
                request.getSession().setAttribute("circuit_livraison", circuit_livraison);
                destination = "InputDeployerNPacksSurNEnvironnements";
            } else if (form instanceof InitialisationUserHasEnvironnementForm) {
                InitialisationUserHasEnvironnementForm inF = (InitialisationUserHasEnvironnementForm) form;
                inF.setArrayEnvironnements(arrayEnvName);
                inF.setArrayUsersLogin(arrayUsersName);
                destination = "ResultatUserHasEnvironnement";
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        // Attention: si j'utilise cette Action, je dois encore ajouter  /VerificationDeltaForm.do  aux paramètrages de spring 
        // security comme dans l'étude d'intersection par exemple

        servlets.AfficherMessageEtatAvancement.executionEnCours.put(connectedUser, false);
        servlets.AfficherMessageEtatAvancement.setLogmessage("", Tools.getConnectedLogin());
        return mapping.findForward(destination);
    }
}
