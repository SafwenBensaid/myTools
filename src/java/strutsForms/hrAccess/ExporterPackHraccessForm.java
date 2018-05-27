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
public class ExporterPackHraccessForm extends org.apache.struts.action.ActionForm {

    private String numLivraison;
    private String circuit;
    private String suffixe;
    private String textAreaObjectList;
    private String natureTraitement;
    private String packName;

    public String getCircuit() {
        return circuit;
    }

    public String getNumLivraison() {
        return numLivraison;
    }

    public String getSuffixe() {
        return suffixe;
    }

    public String getTextAreaObjectList() {
        return textAreaObjectList;
    }

    public String getNatureTraitement() {
        return natureTraitement;
    }

    public String getPackName() {
        return packName;
    }

    public void setCircuit(String circuit) {
        this.circuit = circuit;
    }

    public void setNumLivraison(String numLivraison) {
        this.numLivraison = numLivraison;
    }

    public void setSuffixe(String suffixe) {
        this.suffixe = suffixe;
    }

    public void setTextAreaObjectList(String textAreaObjectList) {
        this.textAreaObjectList = textAreaObjectList;
    }

    public void setNatureTraitement(String natureTraitement) {
        this.natureTraitement = natureTraitement;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    /**
     * @return
     */
    public ExporterPackHraccessForm(String numLivraison, String circuit, String suffixe, String textAreaObjectList, String natureTraitement, String packName) {
        this.numLivraison = numLivraison;
        this.circuit = circuit;
        this.suffixe = suffixe;
        this.textAreaObjectList = textAreaObjectList;
        this.natureTraitement = natureTraitement;
        this.packName = packName;
    }

    /**
     *
     */
    public ExporterPackHraccessForm() {
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
        /*if (getName() == null || getName().length() < 1) {
         errors.add("name", new ActionMessage("error.name.required"));
         // TODO: add 'error.name.required' key to your resources
         }*/
        return errors;
    }
}
