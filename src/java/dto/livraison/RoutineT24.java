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
public class RoutineT24 implements Serializable {

    String executionRoutineT24;

    public RoutineT24() {
    }

    public RoutineT24(String executionRoutineT24) {
        this.executionRoutineT24 = executionRoutineT24;
    }

    public String getExecutionRoutineT24() {
        return executionRoutineT24;
    }

    public void setExecutionRoutineT24(String executionRoutineT24) {
        this.executionRoutineT24 = executionRoutineT24;
    }

    @Override
    public String toString() {
        StringBuilder resultat = new StringBuilder();
        resultat.append("	<div class='center'>");
        resultat.append("		<table  class='tablePrincipale'>");
        resultat.append("			<tr>");
        resultat.append("				<td class='conteneurWrapper'>");
        resultat.append("					<div id='wrapper'>");
        resultat.append("						<div class='accordionButton on'>EXECUTION ROUTINE T24<span class='errorBold'></span></div>");
        resultat.append("						<div class='accordionContent' style='display: block;'>");
        resultat.append("							<fieldset class='fieldSetResultatDeploiement'>");
        resultat.append("									").append(executionRoutineT24.trim());
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
            ExecuterCommandeJshOrT24Thread th = new ExecuterCommandeJshOrT24Thread(envDto, new String[]{executionRoutineT24}, "T24", null);
            th.setName("Thread " + envDto.getNom() + ", EXECUTION_ROUTINE_T24");
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
        resultat.append("						<div class='accordionButton on'>EXECUTION ROUTINE T24</div>");
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