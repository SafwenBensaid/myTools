/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package strutsActions;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import dto.DetailsLivraisonDTO;
import java.io.IOException;
import java.util.Map;
import tools.Configuration;
import tools.GenererMessageTrac;
import tools.Tools;

/**
 *
 * @author 04486
 */
public class GenererMessageTracAction extends Action {

    String messageTrac;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, InterruptedException {
        String logException = "";
        String connectedUser = Tools.getConnectedLogin();
        try {
            servlets.AfficherMessageEtatAvancement.executionEnCours.put(connectedUser, true);
            servlets.AfficherMessageEtatAvancement.setLogmessage("Génération du message TRAC", Tools.getConnectedLogin());

            DetailsLivraisonDTO detailsLivraison = Configuration.usersDetailsLivraisonsMap.get(connectedUser);
            if (detailsLivraison == null) {
                logException += " detailsLivraison : null\n";
                logException += " connecteduser:" + connectedUser + "\n";
                if (Configuration.usersDetailsLivraisonsMap == null) {
                    logException += "Configuration.usersDetailsLivraisonsMap==null\n";
                } else {
                    logException += " size Configuration.usersDetailsLivraisonsMap:" + Configuration.usersDetailsLivraisonsMap.size() + "\n";
                    for (Map.Entry<String, DetailsLivraisonDTO> entry : Configuration.usersDetailsLivraisonsMap.entrySet()) {
                        logException += " -------\n";
                        logException += entry.getKey() + "\n";
                        logException += entry.getValue().toString() + "\n";
                    }
                }

                logException += " connecteduser:" + connectedUser + "\n";
            } else {
                logException += detailsLivraison.toString();
            }
            GenererMessageTrac g = new GenererMessageTrac();
            messageTrac = g.genererMessageTrac(detailsLivraison).replaceAll("é", "e").replaceAll("è", "e").replaceAll("ê", "e").replaceAll("à", "a").replaceAll("ç", "c");

            servlets.AfficherMessageEtatAvancement.setLogmessage("", Tools.getConnectedLogin());
            detailsLivraison.setMessageTrac(messageTrac);
            Configuration.usersDetailsLivraisonsMap.put(connectedUser, detailsLivraison);
            servlets.AfficherMessageEtatAvancement.executionEnCours.put(connectedUser, false);
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(logException + "\n\n\n\n" + tools.Tools.getStackTrace(exep));
        }
        return mapping.findForward("resultatMessageTrac");
    }
}