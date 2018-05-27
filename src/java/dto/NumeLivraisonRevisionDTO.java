/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

/**
 *
 * @author 04486
 */
public class NumeLivraisonRevisionDTO {

    private String numLivraison;
    private String revision;
    private String nomProjet;
    private String packName;

    public NumeLivraisonRevisionDTO(String numLivraison, String revision, String nomProjet, String packName) {
        this.numLivraison = numLivraison;
        this.revision = revision;
        this.nomProjet = nomProjet;
        this.packName = packName;
    }

    public String getNumLivraison() {
        return numLivraison;
    }

    public void setNumLivraison(String numLivraison) {
        this.numLivraison = numLivraison;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getNomProjet() {
        return nomProjet;
    }

    public void setNomProjet(String nomProjet) {
        this.nomProjet = nomProjet;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }
}
