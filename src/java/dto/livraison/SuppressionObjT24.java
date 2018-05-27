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
public class SuppressionObjT24 implements Serializable {

    private String supressionObjetsT24;

    public SuppressionObjT24() {
    }

    public SuppressionObjT24(String supressionObjetsT24) {
        this.supressionObjetsT24 = supressionObjetsT24;
    }

    public String getSupressionObjetsT24() {
        return supressionObjetsT24;
    }

    public void setSupressionObjetsT24(String supressionObjetsT24) {
        this.supressionObjetsT24 = supressionObjetsT24;
    }

    @Override
    public String toString() {
        StringBuilder resultat = new StringBuilder();
        resultat.append("	<div class='center'>");
        resultat.append("		<table  class='tablePrincipale'>");
        resultat.append("			<tr>");
        resultat.append("				<td class='conteneurWrapper'>");
        resultat.append("					<div id='wrapper'>");
        resultat.append("						<div class='accordionButton on'>SUPRESSION OBJETS T24<span class='errorBold'></span></div>");
        resultat.append("						<div class='accordionContent' style='display: block;'>");
        resultat.append("							<fieldset class='fieldSetResultatDeploiement'>");
        resultat.append("									").append(supressionObjetsT24.trim().replaceAll("\n", "<br>"));
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
