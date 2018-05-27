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
public class ReversObjT24 implements Serializable {

    private String reverseObjetsT24;

    public ReversObjT24() {
    }

    public ReversObjT24(String reverseObjetsT24) {
        this.reverseObjetsT24 = reverseObjetsT24;
    }

    public String getReverseObjetsT24() {
        return reverseObjetsT24;
    }

    public void setReverseObjetsT24(String reverseObjetsT24) {
        this.reverseObjetsT24 = reverseObjetsT24;
    }

    @Override
    public String toString() {
        StringBuilder resultat = new StringBuilder();
        resultat.append("	<div class='center'>");
        resultat.append("		<table  class='tablePrincipale'>");
        resultat.append("			<tr>");
        resultat.append("				<td class='conteneurWrapper'>");
        resultat.append("					<div id='wrapper'>");
        resultat.append("						<div class='accordionButton on'>REVERSE OBJETS T24<span class='errorBold'></span></div>");
        resultat.append("						<div class='accordionContent' style='display: block;'>");
        resultat.append("							<fieldset class='fieldSetResultatDeploiement'>");
        resultat.append("									").append(reverseObjetsT24.trim().replaceAll("\n", "<br>"));
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
