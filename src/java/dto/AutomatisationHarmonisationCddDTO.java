/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

/**
 *
 * @author 04486
 */
public class AutomatisationHarmonisationCddDTO {

    private String envDev;
    private String numLivraisonHotfix;
    private String anomalie;
    private String numTicketLivraisonFils;
    private String pathPackSauvegarde;
    private String objetsDeployes;
    private String objetsImpactes;

    public AutomatisationHarmonisationCddDTO(String envDev, String numLivraisonHotfix, String anomalie, String numTicketLivraisonFils, String pathPackSauvegarde, String objetsDeployes, String objetsImpactes) {
        this.envDev = envDev;
        this.numLivraisonHotfix = numLivraisonHotfix;
        this.anomalie = anomalie;
        this.numTicketLivraisonFils = numTicketLivraisonFils;
        this.pathPackSauvegarde = pathPackSauvegarde;
        this.objetsDeployes = objetsDeployes;
        this.objetsImpactes = objetsImpactes;
    }

    public String getEnvDev() {
        return envDev;
    }

    public void setEnvDev(String envDev) {
        this.envDev = envDev;
    }

    public String getNumLivraisonHotfix() {
        return numLivraisonHotfix;
    }

    public void setNumLivraisonHotfix(String numLivraisonHotfix) {
        this.numLivraisonHotfix = numLivraisonHotfix;
    }

    public String getAnomalie() {
        return anomalie;
    }

    public void setAnomalie(String anomalie) {
        this.anomalie = anomalie;
    }

    public String getNumTicketLivraisonFils() {
        return numTicketLivraisonFils;
    }

    public void setNumTicketLivraisonFils(String numTicketLivraisonFils) {
        this.numTicketLivraisonFils = numTicketLivraisonFils;
    }

    public String getPathPackSauvegarde() {
        return pathPackSauvegarde;
    }

    public void setPathPackSauvegarde(String pathPackSauvegarde) {
        this.pathPackSauvegarde = pathPackSauvegarde;
    }

    public String getObjetsDeployes() {
        return objetsDeployes;
    }

    public void setObjetsDeployes(String objetsDeployes) {
        this.objetsDeployes = objetsDeployes;
    }

    public String getObjetsImpactes() {
        return objetsImpactes;
    }

    public void setObjetsImpactes(String objetsImpactes) {
        this.objetsImpactes = objetsImpactes;
    }
}
