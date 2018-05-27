/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

/**
 *
 * @author 04486
 */
public class DetailsLivraisonDTO {

    private String packName;
    private String circuit;
    private String anomalie;
    private String niveauProjet;
    private String numTicket;
    private String resultatAnalysePack;
    private boolean cusExists;
    private String serverName;
    private String resultatDeploiement;
    private String resultatVersionning;
    private String revison;
    private String messageTrac;
    private String phase;
    private String mnemonicCompany;
    private String contenuDesLivrables;
    private int nbrIterationDeploiement;
    private String listeObjets;
    private String owner;
    private String reporter;
    private boolean nePasEcraserLivrable;

    public DetailsLivraisonDTO() {
    }

    public DetailsLivraisonDTO(String packName, String circuit, String anomalie, String phase, String niveauProjet, String numTicket, String resultatAnalysePack, boolean cusExists, String serverName, String resultatDeploiement, String resultatVersionning, String revison, String messageTrac, String mnemonicCompany, String contenuDesLivrables, String listeObjets, String owner, String reporter, boolean nePasEcraserLivrable) {
        this.packName = packName;
        this.circuit = circuit;
        this.anomalie = anomalie;
        this.niveauProjet = niveauProjet;
        this.numTicket = numTicket;
        this.resultatAnalysePack = resultatAnalysePack;
        this.cusExists = cusExists;
        this.serverName = serverName;
        this.resultatDeploiement = resultatDeploiement;
        this.resultatVersionning = resultatVersionning;
        this.revison = revison;
        this.messageTrac = messageTrac;
        this.phase = phase;
        this.mnemonicCompany = mnemonicCompany;
        this.contenuDesLivrables = contenuDesLivrables;
        this.nbrIterationDeploiement = 1;
        this.listeObjets = listeObjets;
        this.owner = owner;
        this.reporter = reporter;
        this.nePasEcraserLivrable = nePasEcraserLivrable;
    }

    public String getMessageTrac() {
        return messageTrac;
    }

    public void setMessageTrac(String messageTrac) {
        this.messageTrac = messageTrac;
    }

    public String getRevison() {
        return revison;
    }

    public void setRevison(String revison) {
        this.revison = revison;
    }

    public String getResultatVersionning() {
        return resultatVersionning;
    }

    public void setResultatVersionning(String resultatVersionning) {
        this.resultatVersionning = resultatVersionning;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public String getCircuit() {
        return circuit;
    }

    public void setCircuit(String circuit) {
        this.circuit = circuit;
    }

    public String getAnomalie() {
        return anomalie;
    }

    public void setAnomalie(String anomalie) {
        this.anomalie = anomalie;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public String getNiveauProjet() {
        return niveauProjet;
    }

    public void setNiveauProjet(String niveauProjet) {
        this.niveauProjet = niveauProjet;
    }

    public String getNumTicket() {
        return numTicket;
    }

    public void setNumTicket(String numTicket) {
        this.numTicket = numTicket;
    }

    public String getResultatAnalysePack() {
        return resultatAnalysePack;
    }

    public void setResultatAnalysePack(String resultatAnalysePack) {
        this.resultatAnalysePack = resultatAnalysePack;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getResultatDeploiement() {
        return resultatDeploiement;
    }

    public void setResultatDeploiement(String resultatDeploiement) {
        this.resultatDeploiement = resultatDeploiement;
    }

    public String getMnemonicCompany() {
        return mnemonicCompany;
    }

    public void setMnemonicCompany(String mnemonicCompany) {
        this.mnemonicCompany = mnemonicCompany;
    }

    public boolean isCusExists() {
        return cusExists;
    }

    public void setCusExists(boolean cusExists) {
        this.cusExists = cusExists;
    }

    public String getContenuDesLivrables() {
        return contenuDesLivrables;
    }

    public void setContenuDesLivrables(String contenuDesLivrables) {
        this.contenuDesLivrables = contenuDesLivrables;
    }

    public int getNbrIterationDeploiement() {
        return nbrIterationDeploiement;
    }

    public void setNbrIterationDeploiement(int nbrIterationDeploiement) {
        this.nbrIterationDeploiement = nbrIterationDeploiement;
    }

    public String getListeObjets() {
        return listeObjets;
    }

    public void setListeObjets(String listeObjets) {
        this.listeObjets = listeObjets;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public boolean isNePasEcraserLivrable() {
        return nePasEcraserLivrable;
    }

    public void setNePasEcraserLivrable(boolean nePasEcraserLivrable) {
        this.nePasEcraserLivrable = nePasEcraserLivrable;
    }

    @Override
    public String toString() {
        return "DetailsLivraisonDTO{" + "packName=" + packName + ", circuit=" + circuit + ", anomalie=" + anomalie + ", niveauProjet=" + niveauProjet + ", numTicket=" + numTicket + ", resultatAnalysePack=" + resultatAnalysePack + ", cusExists=" + cusExists + ", serverName=" + serverName + ", resultatDeploiement=" + resultatDeploiement + ", resultatVersionning=" + resultatVersionning + ", revison=" + revison + ", messageTrac=" + messageTrac + ", phase=" + phase + ", mnemonicCompany=" + mnemonicCompany + ", contenuDesLivrables=" + contenuDesLivrables + ", nbrIterationDeploiement=" + nbrIterationDeploiement + ", listeObjets=" + listeObjets + ", owner=" + owner + ", reporter=" + reporter + '}';
    }
}
