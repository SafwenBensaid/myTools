/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package strutsForms;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author 04486
 */
public class GestionLivraisonsInputPacksEnvIE_Form extends org.apache.struts.action.ActionForm {

    private String circuit;
    private String milestone;
    private String numLivraison;
    private String niveauProjet;
    private String numAnomalie;
    private String phase;
    private String selectedCircuit;
    private String priority;
    private String component;
    private String natureTraitement;
    private String natureLivraison;
    private String contenuDesLivrables;

    public String getCircuit() {
        return circuit;
    }

    public void setCircuit(String circuit) {
        this.circuit = circuit;
    }

    public String getMilestone() {
        return milestone;
    }

    public void setMilestone(String milestone) {
        this.milestone = milestone;
    }

    public String getNumLivraison() {
        return numLivraison;
    }

    public void setNumLivraison(String numLivraison) {
        this.numLivraison = numLivraison;
    }

    public String getNiveauProjet() {
        return niveauProjet;
    }

    public void setNiveauProjet(String niveauProjet) {
        this.niveauProjet = niveauProjet;
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

    public String getContenuDesLivrables() {
        return contenuDesLivrables;
    }

    public void setContenuDesLivrables(String contenuDesLivrables) {
        this.contenuDesLivrables = contenuDesLivrables;
    }

    public GestionLivraisonsInputPacksEnvIE_Form() {
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

        return errors;
    }
}
