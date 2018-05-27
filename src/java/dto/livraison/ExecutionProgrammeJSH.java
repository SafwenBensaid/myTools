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
public class ExecutionProgrammeJSH implements Serializable {

    private String executionProgrammeJsh;

    public ExecutionProgrammeJSH() {
    }

    public ExecutionProgrammeJSH(String executionProgrammeJsh) {
        this.executionProgrammeJsh = executionProgrammeJsh;
    }

    public String getExecutionProgrammeJsh() {
        return executionProgrammeJsh;
    }

    public void setExecutionProgrammeJsh(String executionProgrammeJsh) {
        this.executionProgrammeJsh = executionProgrammeJsh;
    }

    @Override
    public String toString() {
        StringBuilder resultat = new StringBuilder();
        resultat.append("	<div class='center'>");
        resultat.append("		<table  class='tablePrincipale'>");
        resultat.append("			<tr>");
        resultat.append("				<td class='conteneurWrapper'>");
        resultat.append("					<div id='wrapper'>");
        resultat.append("						<div class='accordionButton on'>EXECUTION PROGRAMME JSH<span class='errorBold'></span></div>");
        resultat.append("						<div class='accordionContent' style='display: block;'>");
        resultat.append("							<fieldset class='fieldSetResultatDeploiement'>");
        resultat.append("									").append(executionProgrammeJsh.trim().replaceAll("\n", "<br>"));
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
            ExecuterCommandeJshOrT24Thread th = new ExecuterCommandeJshOrT24Thread(envDto, new String[]{executionProgrammeJsh}, "JSH", "");
            th.setName("Thread " + envDto.getNom() + ", EXECUTION_PROGRAMME_JSH");
            listeThreads.add(th);
        }
        new DeploiementParalleleTools().launchThreads(listeThreads);
        //fin test cob
        //Collecter le code HTML du résultat
        List<String> envDepOkList = new ArrayList<>();
        StringBuilder resultatHTML = new StringBuilder();
        String problems = "";
        for (ExecuterCommandeJshOrT24Thread th : listeThreads) {
            if (!(th.resultatHTML.isEmpty()) && !(th.resultatHTML == null)) {
                if (th.resultatHTML.length() <= 500) {
                    resultatHTML.append(th.resultatHTML);
                } else {
                    resultatHTML.append(th.resultatHTML.substring(0, 500));
                }
            }
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
        resultat.append("						<div class='accordionButton on'>EXECUTION PROGRAMME JSH</div>");
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