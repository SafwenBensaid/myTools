/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package strutsForms;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

/**
 *
 * @author 04486
 */
public class CreateObjectFileForm extends org.apache.struts.action.ActionForm {

    private String circuit;
    private String milestoneList[];
    private String milestone;
    private String numLivraison;
    private String niveauProjetList[];
    private String niveauProjet;
    private String numAnomalie;
    private String suffixe;
    private String textAreaObjectList;
    private String fileName;
    private String phase;
    private String mnemonic;
    private String autreMnemonic;
    private String selectedCircuit;
    private String priority;
    private String component;
    private String natureTraitement;
    private String natureLivraison;
    private String packName;
    private String packNameHidden;
    private String contenuDesLivrables;
    private String textAreaManuel;
    private boolean sendTicketToIE;
    private boolean writeTextOnTicket;
    private boolean nePasEcraserLivrable;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMilestone() {
        return milestone;
    }

    public void setMilestone(String milestone) {
        this.milestone = milestone;
    }

    public String getNiveauProjet() {
        return niveauProjet;
    }

    public void setNiveauProjet(String niveauProjet) {
        this.niveauProjet = niveauProjet;
    }

    public String getSuffixe() {
        return suffixe;
    }

    public void setSuffixe(String suffixe) {
        this.suffixe = suffixe;
    }

    public String getTextAreaObjectList() {
        return textAreaObjectList;
    }

    public void setTextAreaObjectList(String textAreaObjectList) {
        this.textAreaObjectList = textAreaObjectList;
    }

    public String[] getMilestoneList() {
        return milestoneList;
    }

    public void setMilestoneList(String[] milestoneList) {
        this.milestoneList = milestoneList;
    }

    public String getCircuit() {
        return circuit;
    }

    public void setCircuit(String circuit) {
        this.circuit = circuit;
    }

    public String getNumLivraison() {
        return numLivraison;
    }

    public void setNumLivraison(String numLivraison) {
        this.numLivraison = numLivraison;
    }

    public String[] getNiveauProjetList() {
        return niveauProjetList;
    }

    public void setNiveauProjetList(String[] niveauProjetList) {
        this.niveauProjetList = niveauProjetList;
    }

    public String getNumAnomalie() {
        return numAnomalie;
    }

    public void setNumAnomalie(String numAnomalie) {
        this.numAnomalie = numAnomalie;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public void setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;
    }

    public String getAutreMnemonic() {
        return autreMnemonic;
    }

    public void setAutreMnemonic(String autreMnemonic) {
        this.autreMnemonic = autreMnemonic;
    }

    public String getSelectedCircuit() {
        return selectedCircuit;
    }

    public void setSelectedCircuit(String selectedCircuit) {
        this.selectedCircuit = selectedCircuit;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getNatureTraitement() {
        return natureTraitement;
    }

    public void setNatureTraitement(String natureTraitement) {
        this.natureTraitement = natureTraitement;
    }

    public String getNatureLivraison() {
        return natureLivraison;
    }

    public void setNatureLivraison(String natureLivraison) {
        this.natureLivraison = natureLivraison;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public String getPackNameHidden() {
        return packNameHidden;
    }

    public void setPackNameHidden(String packNameHidden) {
        this.packNameHidden = packNameHidden;
    }

    public String getContenuDesLivrables() {
        return contenuDesLivrables;
    }

    public void setContenuDesLivrables(String contenuDesLivrables) {
        this.contenuDesLivrables = contenuDesLivrables;
    }

    public String getTextAreaManuel() {
        return textAreaManuel;
    }

    public void setTextAreaManuel(String textAreaManuel) {
        this.textAreaManuel = textAreaManuel;
    }

    public boolean isSendTicketToIE() {
        return sendTicketToIE;
    }

    public void setSendTicketToIE(boolean sendTicketToIE) {
        this.sendTicketToIE = sendTicketToIE;
    }

    public boolean isWriteTextOnTicket() {
        return writeTextOnTicket;
    }

    public void setWriteTextOnTicket(boolean writeTextOnTicket) {
        this.writeTextOnTicket = writeTextOnTicket;
    }

    public boolean isNePasEcraserLivrable() {
        return nePasEcraserLivrable;
    }

    public void setNePasEcraserLivrable(boolean nePasEcraserLivrable) {
        this.nePasEcraserLivrable = nePasEcraserLivrable;
    }

    public CreateObjectFileForm() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * This is the action called from the Struts framework.
     *
     * @param mapping The ActionMapping used to select this instance.
     * @param request The HTTP Request we are processing.
     * @return
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        if (getSuffixe() == null || getSuffixe().length() < 1) {
            errors.add("Suffixe", new ActionMessage("error.name.required"));
            // TODO: add 'error.name.required' key to your resources
        }
        return errors;
    }
}
