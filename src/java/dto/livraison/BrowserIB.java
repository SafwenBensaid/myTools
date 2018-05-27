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
public class BrowserIB implements Serializable {

    private String browserIBexport;
    private String browserIBtag;

    public BrowserIB() {
    }

    public BrowserIB(String browserIBexport, String browserIBtag) {
        this.browserIBexport = browserIBexport;
        this.browserIBtag = browserIBtag;
    }

    public String getBrowserIBexport() {
        return browserIBexport;
    }

    public void setBrowserIBexport(String browserIBexport) {
        this.browserIBexport = browserIBexport;
    }

    public String getBrowserIBtag() {
        return browserIBtag;
    }

    public void setBrowserIBtag(String browserIBtag) {
        this.browserIBtag = browserIBtag;
    }

    @Override
    public String toString() {
        StringBuilder resultat = new StringBuilder();
        resultat.append("	<div class='center'>");
        resultat.append("		<table  class='tablePrincipale'>");
        resultat.append("			<tr>");
        resultat.append("				<td class='conteneurWrapper'>");
        resultat.append("					<div id='wrapper'>");
        resultat.append("						<div class='accordionButton on'>BROWSER IB<span class='errorBold'></span></div>");
        resultat.append("						<div class='accordionContent' style='display: block;'>");
        resultat.append("							<fieldset class='fieldSetResultatDeploiement'>");
        resultat.append("								<li>");
        resultat.append("									<span class = 'titre'>Chemin de l'export sur le partage:</span>");
        resultat.append("									").append(browserIBexport);
        resultat.append("								</li>");
        resultat.append("								<li>");
        resultat.append("									<span class = 'titre'>Lien du tag:</span>");
        resultat.append("									").append(browserIBtag);
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
