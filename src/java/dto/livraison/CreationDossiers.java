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
public class CreationDossiers implements Serializable {

    private String creationDossiersChemin;
    private String creationDossiersDroitR;
    private String creationDossiersDroitW;
    private String creationDossiersDroitX;

    public CreationDossiers() {
    }

    public CreationDossiers(String creationDossiersChemin, String creationDossiersDroitR, String creationDossiersDroitW, String creationDossiersDroitX) {
        this.creationDossiersChemin = creationDossiersChemin;
        this.creationDossiersDroitR = creationDossiersDroitR;
        this.creationDossiersDroitW = creationDossiersDroitW;
        this.creationDossiersDroitX = creationDossiersDroitX;
    }

    public String getCreationDossiersChemin() {
        return creationDossiersChemin;
    }

    public void setCreationDossiersChemin(String creationDossiersChemin) {
        this.creationDossiersChemin = creationDossiersChemin;
    }

    public String getCreationDossiersDroitR() {
        return creationDossiersDroitR;
    }

    public void setCreationDossiersDroitR(String creationDossiersDroitR) {
        this.creationDossiersDroitR = creationDossiersDroitR;
    }

    public String getCreationDossiersDroitW() {
        return creationDossiersDroitW;
    }

    public void setCreationDossiersDroitW(String creationDossiersDroitW) {
        this.creationDossiersDroitW = creationDossiersDroitW;
    }

    public String getCreationDossiersDroitX() {
        return creationDossiersDroitX;
    }

    public void setCreationDossiersDroitX(String creationDossiersDroitX) {
        this.creationDossiersDroitX = creationDossiersDroitX;
    }

    @Override
    public String toString() {
        StringBuilder resultat = new StringBuilder();
        resultat.append("	<div class='center'>");
        resultat.append("		<table  class='tablePrincipale'>");
        resultat.append("			<tr>");
        resultat.append("				<td class='conteneurWrapper'>");
        resultat.append("					<div id='wrapper'>");
        resultat.append("						<div class='accordionButton on'>CREATION DOSSIERS<span class='errorBold'></span></div>");
        resultat.append("						<div class='accordionContent' style='display: block;'>");
        resultat.append("							<fieldset class='fieldSetResultatDeploiement'>");
        resultat.append("								<li>");
        resultat.append("									<span class = 'titre'>Chemin: </span>");
        resultat.append("									").append(creationDossiersChemin);
        resultat.append("								</li>");
        resultat.append("								<li>");
        resultat.append("									<span class = 'titre'>Droits: </span>");
        resultat.append("									").append(creationDossiersDroitR).append(creationDossiersDroitW).append(creationDossiersDroitX);
        resultat.append("								</li>");
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
            String[] commandes = new String[3];
            commandes[0] = "mkdir -p " + creationDossiersChemin;
            commandes[1] = "chmod " + creationDossiersDroitR + creationDossiersDroitW + creationDossiersDroitX + " " + creationDossiersChemin;
            commandes[2] = "if test -d " + creationDossiersChemin + "; then echo 'FOLDER EXISTS'; else echo 'FOLDER DOES NOT EXISTS' ;fi";
            ExecuterCommandeJshOrT24Thread th = new ExecuterCommandeJshOrT24Thread(envDto, commandes, "CREATION_DOSSIER", "");
            th.setName("Thread " + envDto.getNom() + ", CREATION_DOSSIER");
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
        resultat.append("						<div class='accordionButton on'>CREATION DOSSIERS</div>");
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
