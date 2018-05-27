/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package strutsForms;

/**
 *
 * @author 04486
 */
public class EtudeIntersectionInputObjetsForm extends org.apache.struts.action.ActionForm {

    private String[] environnementList;

    public EtudeIntersectionInputObjetsForm() {
        super();
        // TODO Auto-generated constructor stub
    }

    public String[] getEnvironnementList() {
        return environnementList;
    }

    public void setEnvironnementList(String[] environnementList) {
        this.environnementList = environnementList;
    }
    /**
     * This is the action called from the Struts framework.
     *
     * @param mapping The ActionMapping used to select this instance.
     * @param request The HTTP Request we are processing.
     * @return
     */
}
