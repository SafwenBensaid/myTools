/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dto.livraison;

import java.io.File;
import java.io.Serializable;

/**
 *
 * @author 04486
 */
public class CreationCompte implements Serializable {

    private int numTicket;
    private File CREATIONCOMPTES;

    public CreationCompte() {
    }

    public CreationCompte(int numTicket, File CREATIONCOMPTES) {
        this.numTicket = numTicket;
        this.CREATIONCOMPTES = CREATIONCOMPTES;
    }

    public File getCREATIONCOMPTES() {
        return CREATIONCOMPTES;
    }

    public void setCREATIONCOMPTES(File CREATIONCOMPTES) {
        this.CREATIONCOMPTES = CREATIONCOMPTES;
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
        resultat.append("	<div class='center'>");
        resultat.append("		<table  class='tablePrincipale'>");
        resultat.append("			<tr>");
        resultat.append("				<td class='conteneurWrapper'>");
        resultat.append("					<div id='wrapper'>");
        resultat.append("						<div class='accordionButton on'>CREATION COMPTES<span class='errorBold'></span></div>");
        resultat.append("						<div class='accordionContent' style='display: block;'>");
        resultat.append("							<fieldset class='fieldSetResultatDeploiement'>");
        resultat.append("								<legend class='legend1'>Pièce jointe</legend>");
        resultat.append("								<li>");
        resultat.append("                                                                   <span class = 'titre'>Fichier: </span>");
        resultat.append("									<a href ='http://172.28.70.74/trac/livraisons_t24/attachment/ticket/").append(numTicket).append("/").append(CREATIONCOMPTES.getName()).append("'>").append(CREATIONCOMPTES.getName()).append("</a>");
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
