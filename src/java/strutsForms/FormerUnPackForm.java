/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package strutsForms;

/**
 *
 * @author 04486
 */
public class FormerUnPackForm extends org.apache.struts.action.ActionForm {

    public FormerUnPackForm() {
        super();
        // TODO Auto-generated constructor stub
    }
    private String environnementSource;
    private String nomPack;
    private String textAreaObjectList;
    private String selectedDepot;

    public String getEnvironnementSource() {
        return environnementSource;
    }

    public void setEnvironnementSource(String environnementSource) {
        this.environnementSource = environnementSource;
    }

    public String getSelectedDepot() {
        return selectedDepot;
    }

    public void setSelectedDepot(String selectedDepot) {
        this.selectedDepot = selectedDepot;
    }

    public String getNomPack() {
        return nomPack;
    }

    public void setNomPack(String nomPack) {
        this.nomPack = nomPack;
    }

    public String getTextAreaObjectList() {
        return textAreaObjectList;
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
}
