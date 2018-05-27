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
public class GetAllMilestonesForm extends org.apache.struts.action.ActionForm {

    private String circuit;
    private String numLivraison;
    private String niveauProjetList[];
    private String niveauProjet;
    private String suffixe;
    private String textAreaObjectList;

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

    public GetAllMilestonesForm() {
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
        /*
         if (getName() == null || getName().length() < 1) {
         errors.add("Name", new ActionMessage("error.name.required"));
         // TODO: add 'error.name.required' key to your resources
         }*/
        return errors;
    }
}
