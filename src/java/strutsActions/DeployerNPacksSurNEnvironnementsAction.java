/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package strutsActions;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import java.util.*;
import strutsForms.*;
import tools.*;
import dto.*;

/**
 *
 * @author 04486
 */
public class DeployerNPacksSurNEnvironnementsAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, InterruptedException {


        //préparation des arguments
        String connectedUser = Tools.getConnectedLogin();
        DeployerNPacksSurNEnvironnementsForm inF = (DeployerNPacksSurNEnvironnementsForm) form;
        String environnementSourceName = inF.getEnvironnementSourceName();
        String cheminAbsoluPack = inF.getDossierSourcePath();
        String[] packsNamesList = inF.getNomPack().split("#_#");
        String[] mnemonicList = inF.getAutreMnemonic().split("#_#");
        String[] nbrIterList = inF.getNbrIter().split("#_#");
        String[] environnementsCiblesElements = inF.getEnvironnementsCiblesElements().split("#_#");
        String dossierDeBaseDuPack = "";
        if (environnementSourceName.equals("VERSIONNING")) {
            dossierDeBaseDuPack = "/" + cheminAbsoluPack;
        } else {
            dossierDeBaseDuPack = "PACK.TAF";
        }
        for (int i = 0; i < packsNamesList.length; i++) {
            packsNamesList[i] = packsNamesList[i].trim();
        }
        for (int i = 0; i < mnemonicList.length; i++) {
            mnemonicList[i] = mnemonicList[i].trim();
        }
        for (int i = 0; i < packsNamesList.length; i++) {
            nbrIterList[i] = nbrIterList[i].trim();
        }

        List<TripleDTO> packName_companyMnemonic_nbrIter_liste = DeploiementParalleleTools.genererDtoList(packsNamesList, mnemonicList, nbrIterList);
        Map<String, String[]> resultMap = new DeploiementParalleleTools().deploiementParalleleMultiPack(connectedUser, environnementSourceName, dossierDeBaseDuPack, cheminAbsoluPack, packsNamesList, environnementsCiblesElements, packName_companyMnemonic_nbrIter_liste, true);
        String resultatGlobalHTMLString = null;
        String problemesDeploiementsString = null;
        if (resultMap.containsKey("PROBLEME") && resultMap.get("PROBLEME")[0].length() > 0 && resultMap.get("PROBLEME")[1].length() > 0) {
            String[] resTab = resultMap.get("PROBLEME");
            return Tools.redirectionPageErreurs(resTab[0], resTab[1], mapping, request, response, connectedUser);
        } else {
            String[] resTab = resultMap.get("RESULTAT");
            resultatGlobalHTMLString = resTab[0];
            problemesDeploiementsString = resTab[1];
        }

        request.getSession().setAttribute("resultatDeploiement", resultatGlobalHTMLString);
        request.getSession().setAttribute("problemesDeploiements", problemesDeploiementsString);
        request.getSession().setAttribute("packName_companyMnemonic_nbrIter_liste", packName_companyMnemonic_nbrIter_liste);
        request.getSession().setAttribute("environnementsCiblesElements", environnementsCiblesElements);


        return mapping.findForward("ResultatDeployerNPacksSurNEnvironnements");
    }
}
