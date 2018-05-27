/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dto.livraison;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author 04486
 */
public class ServiceT24 implements Serializable {

    String serviceT24service;
    String serviceT24ordre;
    String serviceT24stage;

    public ServiceT24() {
    }

    public ServiceT24(String serviceT24service, String serviceT24ordre, String serviceT24stage) {
        this.serviceT24service = serviceT24service;
        this.serviceT24ordre = serviceT24ordre;
        this.serviceT24stage = serviceT24stage;
    }

    public String getServiceT24service() {
        return serviceT24service;
    }

    public void setServiceT24service(String serviceT24service) {
        this.serviceT24service = serviceT24service;
    }

    public String getServiceT24ordre() {
        return serviceT24ordre;
    }

    public void setServiceT24ordre(String serviceT24ordre) {
        this.serviceT24ordre = serviceT24ordre;
    }

    public String getServiceT24stage() {
        return serviceT24stage;
    }

    public void setServiceT24stage(String serviceT24stage) {
        this.serviceT24stage = serviceT24stage;
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
        resultat.append("						<div class='accordionButton on'>DOLLAR U<span class='errorBold'></span></div>");
        resultat.append("						<div class='accordionContent' style='display: block;'>");
        resultat.append("							<fieldset class='fieldSetResultatDeploiement'>");
        resultat.append("								<legend class='legend1'>").append(serviceT24service).append("</legend>");
        resultat.append("								<li>");
        resultat.append("									<span class = 'titre'>Ordre: </span>");
        resultat.append("									").append(serviceT24ordre);
        resultat.append("								</li>");
        resultat.append("								<li>");
        resultat.append("									<span class = 'titre'>Stage: </span>");
        resultat.append("									").append(serviceT24stage);
        resultat.append("								</li>									");
        resultat.append("							</fieldset>");
        resultat.append("							<fieldset class='fieldSetResultatDeploiement'>");
        resultat.append("                                                               <legend class='legend1'>Actions à faire</legend>");
        resultat.append("                                                               La procédure de génération de l'enchainement $UNIVERS doit être livré comme suit :");
        resultat.append("                                                               <ul>");
        resultat.append("                                                                   <li>Exécutez en mode jsh de la routine BIAT.BP-BIAT.CDD.GEN.FILE.UNIVERSE qui permet de générer les septs fichiers du $U sous le dossier bnk de l'environnement cible.</li>");
        resultat.append("                                                                   <li>Récupérer les 7 fichiers du répertoire bnk de l'environnement en question et les intégrer au niveau de $UNIVERS.</li>");
        resultat.append("                                                                   <li>Envoyer à OV les 7 fichiers pour versionning (Sous /DEPT24/mailing_ov du serveur Version, lancer le script ./notification_nouvel_enchainnement [$1 $2 $3 $4 $5]).</li>");
        resultat.append("                                                               </ul>");
        resultat.append("                                                               <b>Avec:</b><br>");
        resultat.append("                                                               $1 : Login système ex: t24xxx<br>");
        resultat.append("                                                               $2 : Nom de l'environnement ex: biatxxx<br>");
        resultat.append("                                                               $3 : Adresse IP de l’env ex: 172.28.70.xx<br>");
        resultat.append("                                                               $4 : ABBREVIATION de l'env ex: XXX<br>");
        resultat.append("                                                               $5 : Numero du ticket ex: xxxxx<br>");
        resultat.append("							</fieldset>");
        resultat.append("						</div>");
        resultat.append("					</div>");
        resultat.append("				</td>");
        resultat.append("			</tr>");
        resultat.append("		</table>");
        resultat.append("	</div>");
        return resultat.toString();
    }

    public Map<String, String[]> traiter() {
        Map<String, String[]> resultMap = new HashMap<>();
        StringBuilder resultat = new StringBuilder();
        resultat.append("	<div class='center'>");
        resultat.append("		<table  class='tablePrincipale'>");
        resultat.append("			<tr>");
        resultat.append("				<td class='conteneurWrapper'>");
        resultat.append("					<div id='wrapper'>");
        resultat.append("						<div class='accordionButton on'>DOLLAR U</div>");
        resultat.append("						<div class='accordionContent' style='display: block;'>");
        resultat.append("							<fieldset class='fieldSetResultatDeploiement'>");
        resultat.append("								<legend class='legend1'>").append(serviceT24service).append("</legend>");
        resultat.append("								<li>");
        resultat.append("									<span class = 'titre'>Ordre: </span>");
        resultat.append("									").append(serviceT24ordre);
        resultat.append("								</li>");
        resultat.append("								<li>");
        resultat.append("									<span class = 'titre'>Stage: </span>");
        resultat.append("									").append(serviceT24stage);
        resultat.append("								</li>");
        resultat.append("							</fieldset>");
        resultat.append("							<fieldset class='fieldSetResultatDeploiement'>");
        resultat.append("                                                               <legend class='legend1'>Actions à faire</legend>");
        resultat.append("                                                               La procédure de génération de l'enchainement $UNIVERS doit être livré comme suit :");
        resultat.append("                                                               <ul>");
        resultat.append("                                                                   <li>Exécutez en mode jsh de la routine BIAT.BP-BIAT.CDD.GEN.FILE.UNIVERSE qui permet de générer les septs fichiers du $U sous le dossier bnk de l'environnement cible.</li>");
        resultat.append("                                                                   <li>Récupérer les 7 fichiers du répertoire bnk de l'environnement en question et les intégrer au niveau de $UNIVERS.</li>");
        resultat.append("                                                                   <li>Envoyer à OV les 7 fichiers pour versionning (Sous /DEPT24/mailing_ov du serveur Version, lancer le script ./notification_nouvel_enchainnement [$1 $2 $3 $4 $5]).</li>");
        resultat.append("                                                               </ul>");
        resultat.append("                                                               <b>Avec:</b><br>");
        resultat.append("                                                               $1 : Login système ex: t24xxx<br>");
        resultat.append("                                                               $2 : Nom de l'environnement ex: biatxxx<br>");
        resultat.append("                                                               $3 : Adresse IP de l’env ex: 172.28.70.xx<br>");
        resultat.append("                                                               $4 : ABBREVIATION de l'env ex: XXX<br>");
        resultat.append("                                                               $5 : Numero du ticket ex: xxxxx<br>");
        resultat.append("							</fieldset>");
        resultat.append("						</div>");
        resultat.append("					</div>");
        resultat.append("				</td>");
        resultat.append("			</tr>");
        resultat.append("		</table>");
        resultat.append("	</div>");

        List<String> envDepOkList = new ArrayList<>();
        resultMap.put("PROBLEME", new String[]{"", ""});
        resultMap.put("RESULTAT", new String[]{resultat.toString(), ""});
        resultMap.put("RESULTAT_HTML_COMPLET", new String[]{resultat.toString()});
        resultMap.put("ENV_DEP_OK", envDepOkList.toArray(new String[envDepOkList.size()]));

        return resultMap;
    }
}
