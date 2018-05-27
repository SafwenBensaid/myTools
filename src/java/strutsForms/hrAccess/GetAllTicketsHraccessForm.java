/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package strutsForms.hrAccess;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author 04494
 */
public class GetAllTicketsHraccessForm extends org.apache.struts.action.ActionForm {

    private String circuit;
    private String numLivraison;
    private String natureTraitement;
    private String suffixe;
    private String textAreaObjectList;

    public GetAllTicketsHraccessForm(String circuit, String numLivraison, String natureTraitement, String suffixe, String textAreaObjectList) {
        this.circuit = circuit;
        this.numLivraison = numLivraison;
        this.natureTraitement = natureTraitement;
        this.suffixe = suffixe;
        this.textAreaObjectList = textAreaObjectList;
    }

    public GetAllTicketsHraccessForm() {
        super();
        // TODO Auto-generated constructor stub
    }

    public String getCircuit() {
        return circuit;
    }

    public String getNumLivraison() {
        return numLivraison;
    }

    public String getNatureTraitement() {
        return natureTraitement;
    }

    public String getSuffixe() {
        return suffixe;
    }

    public String getTextAreaObjectList() {
        return textAreaObjectList;
    }

    public void setCircuit(String circuit) {
        this.circuit = circuit;
    }

    public void setNumLivraison(String numLivraison) {
        this.numLivraison = numLivraison;
    }

    public void setNatureTraitement(String natureTraitement) {
        this.natureTraitement = natureTraitement;
    }

    public void setSuffixe(String suffixe) {
        this.suffixe = suffixe;
    }

    public void setTextAreaObjectList(String textAreaObjectList) {
        this.textAreaObjectList = textAreaObjectList;
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
         errors.add("name", new ActionMessage("error.name.required"));
         // TODO: add 'error.name.required' key to your resources
         }*/
        return errors;
    }
}
