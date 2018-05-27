/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package strutsForms;

/**
 *
 * @author 04486
 */
public class ComparaisonObjetsForm extends org.apache.struts.action.ActionForm {

    private String[] environnementList;
    private String selectedDepot;
    private String selectedDepot2;
    private String environnementSourceName1;
    private String environnementSourceName2;
    private String nomPack;
    private String tri;
    private String textAreaObjectList;

    public ComparaisonObjetsForm() {
        super();
        // TODO Auto-generated constructor stub
    }

    public String getEnvironnementSourceName1() {
        return environnementSourceName1;
    }

    public void setEnvironnementSourceName1(String environnementSourceName1) {
        this.environnementSourceName1 = environnementSourceName1;
    }

    public String getEnvironnementSourceName2() {
        return environnementSourceName2;
    }

    public void setEnvironnementSourceName2(String environnementSourceName2) {
        this.environnementSourceName2 = environnementSourceName2;
    }

    public String getTri() {
        return tri;
    }

    public void setTri(String tri) {
        this.tri = tri;
    }

    public String getTextAreaObjectList() {
        return textAreaObjectList;
    }

    public void setTextAreaObjectList(String textAreaObjectList) {
        this.textAreaObjectList = textAreaObjectList;
    }

    public String[] getEnvironnementList() {
        return environnementList;
    }

    public void setEnvironnementList(String[] environnementList) {
        this.environnementList = environnementList;
    }

    public String getNomPack() {
        return nomPack;
    }

    public void setNomPack(String nomPack) {
        this.nomPack = nomPack;
    }

    public String getSelectedDepot() {
        return selectedDepot;
    }

    public void setSelectedDepot(String selectedDepot) {
        this.selectedDepot = selectedDepot;
    }

    public String getSelectedDepot2() {
        return selectedDepot2;
    }

    public void setSelectedDepot2(String selectedDepot2) {
        this.selectedDepot2 = selectedDepot2;
    }
    /**
     * This is the action called from the Struts framework.
     *
     * @param mapping The ActionMapping used to select this instance.
     * @param request The HTTP Request we are processing.
     * @return
     */
}
