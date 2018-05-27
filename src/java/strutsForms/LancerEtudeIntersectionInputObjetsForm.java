/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package strutsForms;

/**
 *
 * @author 04486
 */
public class LancerEtudeIntersectionInputObjetsForm extends org.apache.struts.action.ActionForm {

    private String textAreaObjectList;
    private String objetsSource;
    private String environnementSourceName;
    private String nomPack;
    private String numHotfix;
    private String userName;
    private String circuit;
    private String numLivraison;

    public LancerEtudeIntersectionInputObjetsForm() {
        super();
        // TODO Auto-generated constructor stub
    }

    public String getTextAreaObjectList() {
        return textAreaObjectList;
    }

    public void setTextAreaObjectList(String textAreaObjectList) {
        this.textAreaObjectList = textAreaObjectList;
    }

    public String getObjetsSource() {
        return objetsSource;
    }

    public void setObjetsSource(String objetsSource) {
        this.objetsSource = objetsSource;
    }

    public String getEnvironnementSourceName() {
        return environnementSourceName;
    }

    public void setEnvironnementSourceName(String environnementSourceName) {
        this.environnementSourceName = environnementSourceName;
    }

    public String getNomPack() {
        return nomPack;
    }

    public void setNomPack(String nomPack) {
        this.nomPack = nomPack;
    }

    public String getNumHotfix() {
        return numHotfix;
    }

    public void setNumHotfix(String numHotfix) {
        this.numHotfix = numHotfix;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
    /**
     * This is the action called from the Struts framework.
     *
     * @param mapping The ActionMapping used to select this instance.
     * @param request The HTTP Request we are processing.
     * @return
     */
}
