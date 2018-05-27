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
public class VerificationDeltaForm extends org.apache.struts.action.ActionForm {

    private String environnementSourceName;
    private String[] environnementList;
    private String dossierSource;
    private String environnementDestinationName;

    public String getEnvironnementSourceName() {
        return environnementSourceName;
    }

    public void setEnvironnementSourceName(String environnementSourceName) {
        this.environnementSourceName = environnementSourceName;
    }

    public String getEnvironnementDestinationName() {
        return environnementDestinationName;
    }

    public void setEnvironnementDestinationName(String environnementDestinationName) {
        this.environnementDestinationName = environnementDestinationName;
    }

    public String[] getEnvironnementList() {
        return environnementList;
    }

    public void setEnvironnementList(String[] environnementList) {
        this.environnementList = environnementList;
    }

    public VerificationDeltaForm() {
        super();
        // TODO Auto-generated constructor stub
    }

    public String getDossierSource() {
        return dossierSource;
    }

    public void setDossierSource(String dossierSource) {
        this.dossierSource = dossierSource;
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
