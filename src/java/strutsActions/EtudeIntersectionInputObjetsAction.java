/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package strutsActions;

import dto.StructureResultatAnalyseIntersectionDTO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import dto.*;
import tools.EtudeIntersectionTools;
import java.util.ArrayList;
import java.util.*;
import strutsForms.*;
import tools.Configuration;
import tools.Tools;

/**
 *
 * @author 04486
 */
public class EtudeIntersectionInputObjetsAction extends Action {

    List<StructureResultatAnalyseIntersectionDTO> listStructureAnalyseIntersection;
    String packName;
    String numeroHotFix;
    String environnementSourceName;
    String sourceEtude;
    String resutatEtudeIntersection = "";

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, InterruptedException {

        String resultat = "";
        String connectedUser = Tools.getConnectedLogin();
        servlets.AfficherMessageEtatAvancement.executionEnCours.put(connectedUser, true);
        Date debut = new Date();
        Tools.showConsolLog("Début étude intersection: " + debut.toString());
        try {
            Configuration.initialisation();

            LancerEtudeIntersectionInputObjetsForm inF = (LancerEtudeIntersectionInputObjetsForm) form;
            packName = inF.getNomPack();
            numeroHotFix = inF.getNumHotfix();
            sourceEtude = inF.getObjetsSource();
            environnementSourceName = inF.getEnvironnementSourceName();
            EtudeIntersectionTools etudeIntersectionTools = new EtudeIntersectionTools(numeroHotFix);
            try {
                Tools tools = new Tools();
                resultat = etudeIntersectionTools.traiterEtudeIntersectionInput(inF.getTextAreaObjectList(), packName, environnementSourceName, connectedUser, sourceEtude);
                if (resultat.contains("Dossier inéxistant")) {
                    String messageErreur = "Le dossier <b>" + "PACK.TAF/" + packName + "</b> n'existe pas sur l'environnement " + environnementSourceName;
                    return Tools.redirectionPageErreurs("Dossier inéxistant", messageErreur, mapping, request, response, connectedUser);
                } else {
                    try {
                        //test format Objets
                        etudeIntersectionTools.separerObjets(resultat);
                    } catch (Exception e) {
                        return Tools.redirectionPageErreurs("Objets mal formés", "Les objets doivent respecter ce format: <b>TYPE>OBJET</b>", mapping, request, response, connectedUser);
                    }
                }
                ////////////
                resutatEtudeIntersection = etudeIntersection(etudeIntersectionTools, tools, connectedUser, resultat).getValeur4();
                Tools.showConsolLog("______________");
            } catch (Exception exep) {
                exep.printStackTrace();
                tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
            }



        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        Date fin = new Date();
        Tools.showConsolLog("Fin étude intersection: " + fin.toString());
        request.getSession().setAttribute("resutatEtudeIntersection", resutatEtudeIntersection);
        servlets.AfficherMessageEtatAvancement.executionEnCours.put(connectedUser, false);
        servlets.AfficherMessageEtatAvancement.setLogmessage("", Tools.getConnectedLogin());
        return mapping.findForward("ResutatEtudeIntersection");
    }

    public QuadripleDTO etudeIntersection(EtudeIntersectionTools etudeIntersectionTools, Tools tools, String connectedUser, String lesObjets) {
        Configuration.chargerTousObjetsDeChaqueProjet(connectedUser);
        List<StructureResultatAnalyseIntersectionDTO> listStructureAnalyseIntersectionRelease = null;
        List<StructureResultatAnalyseIntersectionDTO> listStructureAnalyseIntersectionProjet = null;
        List<StructureResultatAnalyseIntersectionDTO> listStructureAnalyseIntersectionUpgrade = null;

        List<StructureResultatAnalyseIntersectionDTO> listStructureAnalyseIntersectionProjetBI = null;
        List<StructureResultatAnalyseIntersectionDTO> listStructureAnalyseIntersectionReleaseBI = null;
        List<StructureResultatAnalyseIntersectionDTO> listStructureAnalyseIntersectionUpgradeBI = null;


        try {
            listStructureAnalyseIntersectionRelease = etudeIntersectionTools.separerObjets(lesObjets);
            listStructureAnalyseIntersectionProjet = etudeIntersectionTools.separerObjets(lesObjets);
            listStructureAnalyseIntersectionUpgrade = etudeIntersectionTools.separerObjets(lesObjets);
        } catch (Exception exep) {
            exep.printStackTrace();
            Tools.traiterException(Tools.getStackTrace(exep));
        }

        //Etude intersection CR branches individuelles//
        listStructureAnalyseIntersectionReleaseBI = new ArrayList<>();
        for (String nomProjetActifCircuitRelease : Configuration.projetsActifsCircuitReleaseList) {
            Tools.showConsolLog("Projet: " + nomProjetActifCircuitRelease);
            listStructureAnalyseIntersectionReleaseBI = etudeIntersectionTools.analyseIntersectionBI(listStructureAnalyseIntersectionReleaseBI, listStructureAnalyseIntersectionRelease, tools, nomProjetActifCircuitRelease, connectedUser);
        }
        List<ResultatAnalyseIntersectionDTO> resultatAnalyseIntersectionRelease = etudeIntersectionTools.analyserLesStructuresDeLog(listStructureAnalyseIntersectionReleaseBI, "RELEASE", connectedUser);


        //Etude intersection CP branches individuelles//
        listStructureAnalyseIntersectionProjetBI = new ArrayList<>();
        for (String nomProjetActifCircuitProjet : Configuration.projetsActifsCircuitProjetList) {
            Tools.showConsolLog("Projet: " + nomProjetActifCircuitProjet);
            listStructureAnalyseIntersectionProjetBI = etudeIntersectionTools.analyseIntersectionBI(listStructureAnalyseIntersectionProjetBI, listStructureAnalyseIntersectionProjet, tools, nomProjetActifCircuitProjet, connectedUser);
        }
        List<ResultatAnalyseIntersectionDTO> resultatAnalyseIntersectionProjet = etudeIntersectionTools.analyserLesStructuresDeLog(listStructureAnalyseIntersectionProjetBI, "PROJET", connectedUser);






        //Etude intersection CU branches individuelles//
        listStructureAnalyseIntersectionUpgradeBI = new ArrayList<>();
        for (String nomProjetActifCircuitUpgrade : Configuration.projetsActifsCircuitUpgradeList) {
            Tools.showConsolLog("Projet: " + nomProjetActifCircuitUpgrade);
            listStructureAnalyseIntersectionUpgradeBI = etudeIntersectionTools.analyseIntersectionBI(listStructureAnalyseIntersectionUpgradeBI, listStructureAnalyseIntersectionUpgrade, tools, nomProjetActifCircuitUpgrade, connectedUser);
        }
        List<ResultatAnalyseIntersectionDTO> resultatAnalyseIntersectionUpgrade = etudeIntersectionTools.analyserLesStructuresDeLog(listStructureAnalyseIntersectionUpgradeBI, "UPGRADE", connectedUser);






        QuadripleDTO quadripleDTO = etudeIntersectionTools.analyserResultatEtudeIntersectionFinal(resultatAnalyseIntersectionRelease, resultatAnalyseIntersectionProjet, resultatAnalyseIntersectionUpgrade, packName, etudeIntersectionTools.getNumeroTicketTrac(), connectedUser, new Date());
        //tripleDTO.valeur1 : résultat étude d'intersection du CR
        //tripleDTO.valeur2 : résultat étude d'intersection du CP
        //tripleDTO.valeur3 : résultat étude d'intersection du CU
        //tripleDTO.valeur4 : résultat étude d'intersection globale
        return quadripleDTO;
    }
}
