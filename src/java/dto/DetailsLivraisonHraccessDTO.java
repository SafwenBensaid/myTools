/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

/**
 *
 * @author 04494
 */
public class DetailsLivraisonHraccessDTO {

    private String packName;
    private String numLivraison;
    private String resultatExport;
    private String resultatImport;
    private String resultatVersionning;
    private String revision;
    private String circuit;
    private String messageTrac;
    private String contenuDesLivrables;
    private String listeObjets;
    private String environnementSource;

    public DetailsLivraisonHraccessDTO(String packName, String numLivraison, String resultatExport, String resultatImport, String resultatVersionning, String revision, String circuit, String messageTrac, String contenuDesLivrables, String listeObjets, String environnementSource, String owner, String reporter) {
        this.packName = packName;
        this.numLivraison = numLivraison;
        this.resultatExport = resultatExport;
        this.resultatImport = resultatImport;
        this.resultatVersionning = resultatVersionning;
        this.revision = revision;
        this.circuit = circuit;
        this.messageTrac = messageTrac;
        this.contenuDesLivrables = contenuDesLivrables;
        this.listeObjets = listeObjets;
        this.environnementSource = environnementSource;
        this.owner = owner;
        this.reporter = reporter;
    }

    public void setNumLivraison(String numLivraison) {
        this.numLivraison = numLivraison;
    }

    public void setResultatExport(String resultatExport) {
        this.resultatExport = resultatExport;
    }

    public void setResultatImport(String resultatImport) {
        this.resultatImport = resultatImport;
    }

    public String getNumLivraison() {
        return numLivraison;
    }

    public String getResultatExport() {
        return resultatExport;
    }

    public String getResultatImport() {
        return resultatImport;
    }

    public String getCircuit() {
        return circuit;
    }

    public void setCircuit(String circuit) {
        this.circuit = circuit;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public void setResultatVersionning(String resultatVersionning) {
        this.resultatVersionning = resultatVersionning;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public void setMessageTrac(String messageTrac) {
        this.messageTrac = messageTrac;
    }

    public void setContenuDesLivrables(String contenuDesLivrables) {
        this.contenuDesLivrables = contenuDesLivrables;
    }

    public void setListeObjets(String listeObjets) {
        this.listeObjets = listeObjets;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public void setEnvironnementSource(String environnementSource) {
        this.environnementSource = environnementSource;
    }

    public String getPackName() {
        return packName;
    }

    public String getResultatVersionning() {
        return resultatVersionning;
    }

    public String getRevision() {
        return revision;
    }

    public String getMessageTrac() {
        return messageTrac;
    }

    public String getContenuDesLivrables() {
        return contenuDesLivrables;
    }

    public String getListeObjets() {
        return listeObjets;
    }

    public String getOwner() {
        return owner;
    }

    public String getReporter() {
        return reporter;
    }

    public String getEnvironnementSource() {
        return environnementSource;
    }
    private String owner;
    private String reporter;
}
