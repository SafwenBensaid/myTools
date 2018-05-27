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
public class AutreLivrable implements Serializable {

    private String autreLivrable;

    public AutreLivrable() {
    }

    public AutreLivrable(String autreLivrable) {
        this.autreLivrable = autreLivrable;
    }

    public String getAutreLivrable() {
        return autreLivrable;
    }

    public void setAutreLivrable(String autreLivrable) {
        this.autreLivrable = autreLivrable;
    }

    @Override
    public String toString() {
        StringBuilder resultat = new StringBuilder();
        resultat.append("	<div class='center'>");
        resultat.append("		<table  class='tablePrincipale'>");
        resultat.append("			<tr>");
        resultat.append("				<td class='conteneurWrapper'>");
        resultat.append("					<div id='wrapper'>");
        resultat.append("						<div class='accordionButton on'>AUTRE LIVRABLE<span class='errorBold'></span></div>");
        resultat.append("						<div class='accordionContent' style='display: block;'>");
        resultat.append("							<fieldset class='fieldSetResultatDeploiement'>");
        resultat.append("									").append(autreLivrable.trim().replaceAll("\n", "<br>"));
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
