/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dto.livraison;

import dto.EnvironnementDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import threads.ExecuterCommandeJshOrT24Thread;
import tools.Configuration;
import tools.DeploiementParalleleTools;

/**
 *
 * @author 04486
 */
public class RebuildSystem implements Serializable {

    private String rebuildSystem;

    public RebuildSystem() {
    }

    public RebuildSystem(String rebuildSystem) {
        this.rebuildSystem = rebuildSystem;
    }

    public String getRebuildSystem() {
        return rebuildSystem;
    }

    public void setRebuildSystem(String rebuildSystem) {
        this.rebuildSystem = rebuildSystem;
    }

    @Override
    public String toString() {
        StringBuilder resultat = new StringBuilder();
        resultat.append("	<div class='center'>");
        resultat.append("		<table  class='tablePrincipale'>");
        resultat.append("			<tr>");
        resultat.append("				<td class='conteneurWrapper'>");
        resultat.append("					<div id='wrapper'>");
        resultat.append("						<div class='accordionButton on'>REBUILD SYSTEM<span class='errorBold'></span></div>");
        resultat.append("						<div class='accordionContent' style='display: block;'>");
        resultat.append("							<fieldset class='fieldSetResultatDeploiement'>");
        resultat.append("									").append(rebuildSystem.trim().replaceAll("\n", "<br>"));
        resultat.append("							</fieldset>");
        resultat.append("						</div>");
        resultat.append("					</div>");
        resultat.append("				</td>");
        resultat.append("			</tr>");
        resultat.append("		</table>");
        resultat.append("	</div>");
        return resultat.toString();
    }

    public Map<String, String[]> traiter(String connectedUser, String[] tabEnvNameDestinationDeploiementLivraison) {
        Map<String, String[]> resultMap = new HashMap<>();
        List<ExecuterCommandeJshOrT24Thread> listeThreads = new ArrayList<>();
        for (String envName : tabEnvNameDestinationDeploiementLivraison) {
            EnvironnementDTO envDto = Configuration.environnementDTOMapUserHasEnvironnement.get(connectedUser).get(envName);
            String ss = rebuildSystem.replaceAll("STANDARD.SELECTION>", "");
            ExecuterCommandeJshOrT24Thread th = new ExecuterCommandeJshOrT24Thread(envDto, new String[]{ss}, "REBUILD_SYSTEM", null);
            th.setName("Thread " + envDto.getNom() + ", REBUILD_SYSTEM");
            listeThreads.add(th);
        }
        new DeploiementParalleleTools().launchThreads(listeThreads);
        //fin test cob
        //Collecter le code HTML du résultat
        List<String> envDepOkList = new ArrayList<>();
        StringBuilder resultatHTML = new StringBuilder();
        String problems = "";
        for (ExecuterCommandeJshOrT24Thread th : listeThreads) {
            resultatHTML.append(th.resultatHTML);
            if (th.problemExist == false) {
                envDepOkList.add(th.env.getNom());
            } else {
                problems += th.resultatCommande + "<br>";
            }
        }
        resultatHTML = getAllHtmlResult(resultatHTML);
        resultMap.put("PROBLEME", new String[]{"", ""});
        resultMap.put("RESULTAT", new String[]{resultatHTML.toString(), problems});
        resultMap.put("RESULTAT_HTML_COMPLET", new String[]{resultatHTML.toString()});
        resultMap.put("ENV_DEP_OK", envDepOkList.toArray(new String[envDepOkList.size()]));
        return resultMap;
    }

    private StringBuilder getAllHtmlResult(StringBuilder unionHTML) {
        StringBuilder resultat = new StringBuilder();
        resultat.append("	<div class='center'>");
        resultat.append("		<table  class='tablePrincipale'>");
        resultat.append("			<tr>");
        resultat.append("				<td class='conteneurWrapper'>");
        resultat.append("					<div id='wrapper'>");
        resultat.append("						<div class='accordionButton on'>REBUILD SYSTEM</div>");
        resultat.append("						<div class='accordionContent' style='display: block;'>");
        resultat.append(unionHTML);
        resultat.append("						</div>");
        resultat.append("					</div>");
        resultat.append("				</td>");
        resultat.append("			</tr>");
        resultat.append("		</table>");
        resultat.append("	</div>");
        return resultat;
    }
}
