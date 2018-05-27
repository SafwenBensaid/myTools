/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import java.util.*;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author 04486
 */
public class SessionTools {

    private static Set<String> sessionsList = null;

    public static void clearSessionVariables(HttpServletRequest request) {
        if (sessionsList == null) {
            sessionsList = new TreeSet<String>();
            sessionsList.add("erreurNameErreurValue");
            sessionsList.add("VerificationDeltaChamps");
            sessionsList.add("ResultatConstitutionPackMultiprojets");
            sessionsList.add("resultatDeploiement");
            sessionsList.add("resutatEtudeIntersection");
            sessionsList.add("allDepotObjectList");
            sessionsList.add("objetsHorsReferentiel");
            sessionsList.add("mapCircuits");
            //sessionsList.add("detailsLivraison");
            sessionsList.add("niveauProjetArray");
            sessionsList.add("mapPipeTickets");
            sessionsList.add("circuit_livraison");

            sessionsList.add("packName_companyMnemonic_nbrIter_liste");
            sessionsList.add("environnementsCiblesElements");
            sessionsList.add("problemesDeploiements");
            //safwen
            sessionsList.add("JsonObjetsDepot");
            sessionsList.add("ListeAcocherReleaseGLOB");
            sessionsList.add("ListeAcocherReleaseELEM");
            sessionsList.add("ListeAcocherProjetGLOB");
            sessionsList.add("ListeAcocherProjetELEM");
            sessionsList.add("ListeAcocherProductionGLOB");
            sessionsList.add("ListeAcocherProductionELEM");
            sessionsList.add("paramCob");
            //Hr Access
            sessionsList.add("mapPipeTicketsHraccess");
            sessionsList.add("detailsLivraisonHraccess");
        }
        for (String var : sessionsList) {
            try {
                request.getSession().removeAttribute(var);
            } catch (Exception exep) {
                exep.printStackTrace();
                tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
            }
        }
    }
}
