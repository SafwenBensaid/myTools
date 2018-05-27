/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dto.livraison;

import dto.EnvironnementDTO;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import threads.UploadFileThread;
import tools.Configuration;
import tools.DeploiementParalleleTools;

/**
 *
 * @author 04486
 */
public class TransfertFichier implements Serializable {

    private int numTicket;
    private File TRANSFERTFICHIERSfichier;
    private String TRANSFERTFICHIERSchemin;
    private String TRANSFERTFICHIERSModeTransfert;

    public TransfertFichier() {
    }

    public TransfertFichier(int numTicket, File TRANSFERTFICHIERSfichier, String TRANSFERTFICHIERSchemin, String TRANSFERTFICHIERSModeTransfert) {
        this.numTicket = numTicket;
        this.TRANSFERTFICHIERSfichier = TRANSFERTFICHIERSfichier;
        this.TRANSFERTFICHIERSchemin = TRANSFERTFICHIERSchemin;
        this.TRANSFERTFICHIERSModeTransfert = TRANSFERTFICHIERSModeTransfert;
    }

    public File getTRANSFERTFICHIERSfichier() {
        return TRANSFERTFICHIERSfichier;
    }

    public void setTRANSFERTFICHIERSfichier(File TRANSFERTFICHIERSfichier) {
        this.TRANSFERTFICHIERSfichier = TRANSFERTFICHIERSfichier;
    }

    public String getTRANSFERTFICHIERSchemin() {
        return TRANSFERTFICHIERSchemin;
    }

    public void setTRANSFERTFICHIERSchemin(String TRANSFERTFICHIERSchemin) {
        this.TRANSFERTFICHIERSchemin = TRANSFERTFICHIERSchemin;
    }

    public String getTRANSFERTFICHIERSModeTransfert() {
        return TRANSFERTFICHIERSModeTransfert;
    }

    public void setTRANSFERTFICHIERSModeTransfert(String TRANSFERTFICHIERSModeTransfert) {
        this.TRANSFERTFICHIERSModeTransfert = TRANSFERTFICHIERSModeTransfert;
    }

    public int getNumTicket() {
        return numTicket;
    }

    public void setNumTicket(int numTicket) {
        this.numTicket = numTicket;
    }

    @Override
    public String toString() {
        StringBuilder resultat = new StringBuilder();
        resultat.append("");
        resultat.append("	<div class='center'>");
        resultat.append("		<table  class='tablePrincipale'>");
        resultat.append("			<tr>");
        resultat.append("				<td class='conteneurWrapper'>");
        resultat.append("					<div id='wrapper'>");
        resultat.append("						<div class='accordionButton on'>TRANSFERT FICHIERS<span class='errorBold'></span></div>");
        resultat.append("						<div class='accordionContent' style='display: block;'>");
        resultat.append("							<fieldset class='fieldSetResultatDeploiement'>");
        resultat.append("								<legend class='legend1'>Pièce jointe</legend>");
        resultat.append("								<li>");
        resultat.append("                                                                   <span class = 'titre'>Fichier: </span>");
        resultat.append("                                                                   <a href ='http://172.28.70.74/trac/livraisons_t24/attachment/ticket/").append(numTicket).append("/").append(TRANSFERTFICHIERSfichier.getName()).append("'>").append(TRANSFERTFICHIERSfichier.getName()).append("</a>");
        resultat.append("								</li>");
        resultat.append("								<li>");
        resultat.append("                                                                   <span class = 'titre'>A deposer sous le dossier: </span>");
        resultat.append("									").append(TRANSFERTFICHIERSchemin);
        resultat.append("								</li>");
        resultat.append("								<li>");
        resultat.append("                                                                   <span class = 'titre'>Mode de transfert: </span>");
        resultat.append("									").append(TRANSFERTFICHIERSModeTransfert);
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
        List<UploadFileThread> listeThreads = new ArrayList<>();
        for (String envName : tabEnvNameDestinationDeploiementLivraison) {
            EnvironnementDTO envDto = Configuration.environnementDTOMapUserHasEnvironnement.get(connectedUser).get(envName);
            UploadFileThread th = new UploadFileThread(TRANSFERTFICHIERSfichier, TRANSFERTFICHIERSchemin, TRANSFERTFICHIERSModeTransfert, envDto);
            th.setName("Thread " + envDto.getNom() + ", TRANSFERT_FICHIERS");
            listeThreads.add(th);
        }
        new DeploiementParalleleTools().launchThreads(listeThreads);
        //fin test cob
        //Collecter le code HTML du résultat
        List<String> envDepOkList = new ArrayList<>();
        StringBuilder resultatHTML = new StringBuilder();
        String problems = "";
        for (UploadFileThread th : listeThreads) {
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
        resultat.append("						<div class='accordionButton on'>TRANSFERT FICHIERS</div>");
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
