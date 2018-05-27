/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package strutsForms.hrAccess;

/**
 *
 * @author 04494
 */
public class ImporterPackHraccessForm extends org.apache.struts.action.ActionForm {

    private String numLivraison;
    private String textAreaObjectList;
    private String natureTraitement;
    private String packName;

    public ImporterPackHraccessForm(String numLivraison, String textAreaObjectList, String natureTraitement, String packName) {
        this.numLivraison = numLivraison;
        this.textAreaObjectList = textAreaObjectList;
        this.natureTraitement = natureTraitement;
        this.packName = packName;
    }

    public String getNumLivraison() {
        return numLivraison;
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

    public void setNumLivraison(String numLivraison) {
        this.numLivraison = numLivraison;
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
}
