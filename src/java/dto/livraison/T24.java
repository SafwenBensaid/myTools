/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dto.livraison;

import dto.TripleDTO;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import t24Scripts.T24Scripts;
import tools.Configuration;
import tools.DeploiementParalleleTools;

/**
 *
 * @author 04486
 */
public class T24 implements Serializable {

    private static final long serialVersionUID = 1L;
    private String selectedMnemonic;
    private String packName;
    private String nbrIter;
    private String objetsT24;
    private String warningsT24;

    public T24() {
    }

    public T24(String selectedMnemonic, String packName, String nbrIter, String objetsT24, String warningsT24) {
        this.selectedMnemonic = selectedMnemonic;
        this.packName = packName;
        this.nbrIter = nbrIter;
        this.objetsT24 = objetsT24;
        this.warningsT24 = warningsT24;
    }

    public String getSelectedMnemonic() {
        return selectedMnemonic;
    }

    public void setSelectedMnemonic(String selectedMnemonic) {
        this.selectedMnemonic = selectedMnemonic;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public String getNbrIter() {
        return nbrIter;
    }

    public void setNbrIter(String nbrIter) {
        this.nbrIter = nbrIter;
    }

    public String getObjetsT24() {
        return objetsT24;
    }

    public void setObjetsT24(String objetsT24) {
        this.objetsT24 = objetsT24;
    }

    public String getWarningsT24() {
        return warningsT24;
    }

    public void setWarningsT24(String warningsT24) {
        this.warningsT24 = warningsT24;
    }

    @Override
    public String toString() {
        StringBuilder resultat = new StringBuilder();
        resultat.append("	<div class='center'>");
        resultat.append("		<table  class='tablePrincipale'>");
        resultat.append("			<tr>");
        resultat.append("				<td class='conteneurWrapper'>");
        resultat.append("					<div id='wrapper'>");
        resultat.append("						<div class='accordionButton on'>OBJETS T24<span class='errorBold'></span></div>");
        resultat.append("						<div class='accordionContent' style='display: block;'>");
        resultat.append("							<fieldset class='fieldSetResultatDeploiement'>");
        resultat.append("								<legend class='legend1'>").append("TAF-" + packName).append("</legend>");
        resultat.append("								<li>");
        resultat.append("									<span class = 'titre'>Mnémonic du company: </span>");
        resultat.append("									").append(selectedMnemonic);
        resultat.append("								</li>");
        resultat.append("								<li>");
        resultat.append("									<span class = 'titre'>Nombre d'itérations: </span>");
        resultat.append("									").append(nbrIter);
        resultat.append("								</li>");
        resultat.append("								<li>");
        resultat.append("									<span class = 'titre'>Liste d'Objets T24:  </span>");
        resultat.append("								</li>										");
        resultat.append("								<p class='contenu'>");
        resultat.append("									").append(objetsT24.trim().replaceAll("\n", "<br>"));
        resultat.append("								</p>");
        resultat.append("							</fieldset>");
        if (warningsT24 != null && warningsT24.trim().length() > 0) {
            resultat.append("							<fieldset class='fieldSetResultatDeploiement'>");
            resultat.append("								<legend class='legend1'>Alertes</legend>");
            resultat.append("								<div class='alert-warning'>");
            resultat.append("									").append(warningsT24.trim().replaceAll("\n", "<br>"));
            resultat.append("								</div>");
            resultat.append("							</fieldset>");
        }
        resultat.append("						</div>");
        resultat.append("					</div>");
        resultat.append("				</td>");
        resultat.append("			</tr>");
        resultat.append("		</table>");
        resultat.append("	</div>");
        return resultat.toString();
    }

    public String toDo() {
        StringBuilder resultat = new StringBuilder();
        resultat.append("	<div class='center'>");
        resultat.append("		<table  class='tablePrincipale'>");
        resultat.append("			<tr>");
        resultat.append("				<td class='conteneurWrapper'>");
        resultat.append("					<div id='wrapper'>");
        resultat.append("						<div class='accordionButton on'>OBJETS T24<span class='errorBold'></span></div>");
        resultat.append("						<div class='accordionContent' style='display: block;'>");
        resultat.append("							<fieldset class='fieldSetResultatDeploiement'>");
        resultat.append("								<legend class='legend1'>").append("TAF-" + packName).append("</legend>");
        resultat.append("								<li>");
        resultat.append("									<span class = 'titre'>Mnémonic du company: </span>");
        resultat.append("									").append(selectedMnemonic);
        resultat.append("								</li>");
        resultat.append("								<li>");
        resultat.append("									<span class = 'titre'>Nombre d'itérations: </span>");
        resultat.append("									").append(nbrIter);
        resultat.append("								</li>");
        resultat.append("								<li>");
        resultat.append("									<span class = 'titre'>Chemin du pack: </span>");
        resultat.append("									").append("bnk.run/PACK.TAF/<b>").append(packName).append("</b>");
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

    public String getAlert() {
        StringBuilder resultat = new StringBuilder();
        resultat.append("	<div class='center'>");
        resultat.append("		<table  class='tablePrincipale'>");
        resultat.append("			<tr>");
        resultat.append("				<td class='conteneurWrapper'>");
        resultat.append("					<div id='wrapper'>");
        resultat.append("						<div class='accordionButton on'>ALERTE T24<span class='errorBold'></span></div>");
        resultat.append("						<div class='accordionContent' style='display: block;'>");
        resultat.append("							<fieldset class='fieldSetResultatDeploiement'>");
        resultat.append("								<legend class='legend1'>Message d'alerte</legend>");
        resultat.append("								<div class='alert-warning'>");
        resultat.append("									").append(warningsT24.trim().replaceAll("\n", "<br>"));
        resultat.append("								</div>");
        resultat.append("							</fieldset>");
        resultat.append("						</div>");
        resultat.append("					</div>");
        resultat.append("				</td>");
        resultat.append("			</tr>");
        resultat.append("		</table>");
        resultat.append("	</div>");
        return resultat.toString();
    }

    public Map<String, String[]> traiter(String connectedUser, String[] tabEnvNameDestinationDeploiementLivraison, String environnementSourceName) {
        String[] packsNamesList = new String[]{packName};
        String[] mnemonicList = new String[]{selectedMnemonic};
        String[] nbrIterList = new String[]{nbrIter};
        List<TripleDTO> packName_companyMnemonic_nbrIter_liste = DeploiementParalleleTools.genererDtoList(packsNamesList, mnemonicList, nbrIterList);
        Map<String, String[]> resultMap = new DeploiementParalleleTools().deploiementParalleleMultiPack(connectedUser, environnementSourceName, "PACK.TAF", "", packsNamesList, tabEnvNameDestinationDeploiementLivraison, packName_companyMnemonic_nbrIter_liste, false);
        if (warningsT24 != null && warningsT24.trim().length() > 0) {
            String messageTrac = resultMap.get("RESULTAT_HTML_COMPLET")[0];
            messageTrac += getAlert();
            resultMap.put("RESULTAT_HTML_COMPLET", new String[]{messageTrac});
        }
        return resultMap;
    }

    public String versionning(String circuit, String niveauProjet, String numTicket, String connectedUser) {
        String scriptVersionning = null;
        if (circuit.equals("RELEASE")) {
            scriptVersionning = "SVNVERS_RELEASE";
        } else if (circuit.equals("PROJET")) {
            scriptVersionning = "SVNVERS_PROJET";
        } else if (circuit.equals("UPGRADE")) {
            scriptVersionning = "SVNVERS_UPGRADE";
        }
        //Niveauprojet niveauprojet = new DataBaseRequests().getNiveauProjetByAbreviation(detailsLivraison.getNiveauProjet());
        String niveauProjetAbreviation = Configuration.getAbreviationProjetParNiveauProjet(niveauProjet);
        String[] resultatVersionningAuxRevision = T24Scripts.versionnerPack(scriptVersionning, numTicket, niveauProjetAbreviation, packName, connectedUser);
        String resultatVersionning = resultatVersionningAuxRevision[0];
        String revision = resultatVersionningAuxRevision[1];
        return resultatVersionning;
    }
}
