/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dto.livraison;

import java.io.Serializable;

/**
 *
 * @author 04486
 */
public class BrowserT24 implements Serializable {

    private String browserT24export;
    private String browserT24tag;

    public BrowserT24() {
    }

    public BrowserT24(String browserT24export, String browserT24tag) {
        this.browserT24export = browserT24export;
        this.browserT24tag = browserT24tag;
    }

    public String getBrowserT24export() {
        return browserT24export;
    }

    public void setBrowserT24export(String browserT24export) {
        this.browserT24export = browserT24export;
    }

    public String getBrowserT24tag() {
        return browserT24tag;
    }

    public void setBrowserT24tag(String browserT24tag) {
        this.browserT24tag = browserT24tag;
    }

    @Override
    public String toString() {
        StringBuilder resultat = new StringBuilder();
        resultat.append("	<div class='center'>");
        resultat.append("		<table  class='tablePrincipale'>");
        resultat.append("			<tr>");
        resultat.append("				<td class='conteneurWrapper'>");
        resultat.append("					<div id='wrapper'>");
        resultat.append("						<div class='accordionButton on'>BROWSER T24<span class='errorBold'></span></div>");
        resultat.append("						<div class='accordionContent' style='display: block;'>");
        resultat.append("							<fieldset class='fieldSetResultatDeploiement'>");
        resultat.append("								<li>");
        resultat.append("									<span class = 'titre'>Chemin de l'export sur le partage:</span>");
        resultat.append("									").append(browserT24export);
        resultat.append("								</li>");
        resultat.append("								<li>");
        resultat.append("									<span class = 'titre'>Lien du tag:</span>");
        resultat.append("									").append(browserT24tag);
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
}
