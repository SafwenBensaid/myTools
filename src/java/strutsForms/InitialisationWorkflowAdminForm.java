/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package strutsForms;

/**
 *
 * @author 04486
 */
public class InitialisationWorkflowAdminForm extends org.apache.struts.action.ActionForm {

    private String GLOBAL;
    private String CREATION_HOTFIX;
    private String CLOTURE_HOTFIX;
    private String CREATION_RELEASE;
    private String CLOTURE_RELEASE;
    private String CREATION_PROJET;
    private String CLOTURE_PROJET;
    private String HF_PROD;
    private String START_HF_PROD;
    private String END_HF_PROD;
    private String HF_HARM;

    public InitialisationWorkflowAdminForm() {
        super();
        // TODO Auto-generated constructor stub
    }

    public String getGLOBAL() {
        return GLOBAL;
    }

    public void setGLOBAL(String GLOBAL) {
        this.GLOBAL = GLOBAL;
    }

    public String getCREATION_HOTFIX() {
        return CREATION_HOTFIX;
    }

    public void setCREATION_HOTFIX(String CREATION_HOTFIX) {
        this.CREATION_HOTFIX = CREATION_HOTFIX;
    }

    public String getCLOTURE_HOTFIX() {
        return CLOTURE_HOTFIX;
    }

    public void setCLOTURE_HOTFIX(String CLOTURE_HOTFIX) {
        this.CLOTURE_HOTFIX = CLOTURE_HOTFIX;
    }

    public String getCREATION_RELEASE() {
        return CREATION_RELEASE;
    }

    public void setCREATION_RELEASE(String CREATION_RELEASE) {
        this.CREATION_RELEASE = CREATION_RELEASE;
    }

    public String getCLOTURE_RELEASE() {
        return CLOTURE_RELEASE;
    }

    public void setCLOTURE_RELEASE(String CLOTURE_RELEASE) {
        this.CLOTURE_RELEASE = CLOTURE_RELEASE;
    }

    public String getCREATION_PROJET() {
        return CREATION_PROJET;
    }

    public void setCREATION_PROJET(String CREATION_PROJET) {
        this.CREATION_PROJET = CREATION_PROJET;
    }

    public String getCLOTURE_PROJET() {
        return CLOTURE_PROJET;
    }

    public void setCLOTURE_PROJET(String CLOTURE_PROJET) {
        this.CLOTURE_PROJET = CLOTURE_PROJET;
    }

    public String getHF_PROD() {
        return HF_PROD;
    }

    public void setHF_PROD(String HF_PROD) {
        this.HF_PROD = HF_PROD;
    }

    public String getSTART_HF_PROD() {
        return START_HF_PROD;
    }

    public void setSTART_HF_PROD(String START_HF_PROD) {
        this.START_HF_PROD = START_HF_PROD;
    }

    public String getEND_HF_PROD() {
        return END_HF_PROD;
    }

    public void setEND_HF_PROD(String END_HF_PROD) {
        this.END_HF_PROD = END_HF_PROD;
    }

    public String getHF_HARM() {
        return HF_HARM;
    }

    public void setHF_HARM(String HF_HARM) {
        this.HF_HARM = HF_HARM;
    }
    /**
     * This is the action called from the Struts framework.
     *
     * @param mapping The ActionMapping used to select this instance.
     * @param request The HTTP Request we are processing.
     * @return
     */
}
