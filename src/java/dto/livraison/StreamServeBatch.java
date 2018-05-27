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
public class StreamServeBatch implements Serializable {

    private String streamServeBatchexport;
    private String streamServeBatchprojetSS;
    private String streamServeBatchlienTagSS;

    public StreamServeBatch() {
    }

    public StreamServeBatch(String streamServeBatchexport, String streamServeBatchprojetSS, String streamServeBatchlienTagSS) {
        this.streamServeBatchexport = streamServeBatchexport;
        this.streamServeBatchprojetSS = streamServeBatchprojetSS;
        this.streamServeBatchlienTagSS = streamServeBatchlienTagSS;
    }

    public String getStreamServeBatchexport() {
        return streamServeBatchexport;
    }

    public void setStreamServeBatchExport(String streamServeBatchexport) {
        this.streamServeBatchexport = streamServeBatchexport;
    }

    public String getStreamServeBatchprojetSS() {
        return streamServeBatchprojetSS;
    }

    public void setStreamServeBatchprojetSS(String streamServeBatchprojetSS) {
        this.streamServeBatchprojetSS = streamServeBatchprojetSS;
    }

    public String getStreamServeBatchlienTagSS() {
        return streamServeBatchlienTagSS;
    }

    public void setStreamServeBatchlienTagSS(String streamServeBatchlienTagSS) {
        this.streamServeBatchlienTagSS = streamServeBatchlienTagSS;
    }

    @Override
    public String toString() {
        StringBuilder resultat = new StringBuilder();
        resultat.append("	<div class='center'>");
        resultat.append("		<table  class='tablePrincipale'>");
        resultat.append("			<tr>");
        resultat.append("				<td class='conteneurWrapper'>");
        resultat.append("					<div id='wrapper'>");
        resultat.append("						<div class='accordionButton on'>STREAMSERV BATCH<span class='errorBold'></span></div>");
        resultat.append("						<div class='accordionContent' style='display: block;'>");
        resultat.append("							<fieldset class='fieldSetResultatDeploiement'>");
        resultat.append("								<li>");
        resultat.append("									<span class = 'titre'>Chemin de l'export: </span>");
        resultat.append("									").append(streamServeBatchexport);
        resultat.append("								</li>");
        resultat.append("								<li>");
        resultat.append("									<span class = 'titre'>Projet Stream Serve: </span>");
        resultat.append("									").append(streamServeBatchprojetSS);
        resultat.append("								</li>");
        resultat.append("								<li>");
        resultat.append("									<span class = 'titre'>Lien du Tag: </span>");
        resultat.append("									").append(streamServeBatchlienTagSS);
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
