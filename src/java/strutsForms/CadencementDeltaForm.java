/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package strutsForms;

/**
 *
 * @author 04494
 */
public class CadencementDeltaForm extends org.apache.struts.action.ActionForm {

    private String nomPack;

    public CadencementDeltaForm() {
        super();
    }

    public String getNomPack() {
        return nomPack;
    }

    public void setNomPack(String nomPack) {
        this.nomPack = nomPack;
    }
}
