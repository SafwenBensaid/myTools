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
public class StreamServeTransactionnel implements Serializable {

    private String streamServeTransactionnelexport;
    private String streamServeTransactionnelprojetSS;
    private String streamServeTransactionnelLienTagSS;

    public StreamServeTransactionnel() {
    }

    public StreamServeTransactionnel(String streamServeTransactionnelexport, String streamServeTransactionnelprojetSS, String streamServeTransactionnelLienTagSS) {
        this.streamServeTransactionnelexport = streamServeTransactionnelexport;
        this.streamServeTransactionnelprojetSS = streamServeTransactionnelprojetSS;
        this.streamServeTransactionnelLienTagSS = streamServeTransactionnelLienTagSS;
    }

    public String getStreamServeTransactionnelexport() {
        return streamServeTransactionnelexport;
    }

    public void setStreamServeTransactionnelExport(String streamServeTransactionnelexport) {
        this.streamServeTransactionnelexport = streamServeTransactionnelexport;
    }

    public String getStreamServeTransactionnelprojetSS() {
        return streamServeTransactionnelprojetSS;
    }

    public void setStreamServeTransactionnelprojetSS(String streamServeTransactionnelprojetSS) {
        this.streamServeTransactionnelprojetSS = streamServeTransactionnelprojetSS;
    }

    public String getStreamServeTransactionnelLienTagSS() {
        return streamServeTransactionnelLienTagSS;
    }

    public void setStreamServeTransactionnelLienTagSS(String streamServeTransactionnelLienTagSS) {
        this.streamServeTransactionnelLienTagSS = streamServeTransactionnelLienTagSS;
    }

    @Override
    public String toString() {
        StringBuilder resultat = new StringBuilder();
        resultat.append("	<div class='center'>");
        resultat.append("		<table  class='tablePrincipale'>");
        resultat.append("			<tr>");
        resultat.append("				<td class='conteneurWrapper'>");
        resultat.append("					<div id='wrapper'>");
        resultat.append("						<div class='accordionButton on'>STREAMSERV TRANSACTIONNEL<span class='errorBold'></span></div>");
        resultat.append("						<div class='accordionContent' style='display: block;'>");
        resultat.append("							<fieldset class='fieldSetResultatDeploiement'>");
        resultat.append("								<li>");
        resultat.append("									<span class = 'titre'>Chemin de l'export: </span>");
        resultat.append("									").append(streamServeTransactionnelexport);
        resultat.append("								</li>");
        resultat.append("								<li>");
        resultat.append("									<span class = 'titre'>Projet Stream Serve: </span>");
        resultat.append("									").append(streamServeTransactionnelprojetSS);
        resultat.append("								</li>");
        resultat.append("								<li>");
        resultat.append("									<span class = 'titre'>Lien du Tag: </span>");
        resultat.append("									").append(streamServeTransactionnelLienTagSS);
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
