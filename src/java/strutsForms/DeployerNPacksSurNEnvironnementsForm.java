/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package strutsForms;

/**
 *
 * @author 04486
 */
public class DeployerNPacksSurNEnvironnementsForm extends org.apache.struts.action.ActionForm {

    public DeployerNPacksSurNEnvironnementsForm() {
        super();
        // TODO Auto-generated constructor stub
    }
    private String environnementSourceName;
    private String dossierSourcePath;
    private String nomPack;
    private String autreMnemonic;
    private String nbrIter;
    private String environnementsCiblesElements;
    private String stockEnvironnementsElements;
    private String mnemonic;

    public String getEnvironnementSourceName() {
        return environnementSourceName;
    }

    public void setEnvironnementSourceName(String environnementSourceName) {
        this.environnementSourceName = environnementSourceName;
    }

    public String getDossierSourcePath() {
        return dossierSourcePath;
    }

    public void setDossierSourcePath(String dossierSourcePath) {
        this.dossierSourcePath = dossierSourcePath;
    }

    public String getNomPack() {
        return nomPack;
    }

    public void setNomPack(String nomPack) {
        this.nomPack = nomPack;
    }

    public String getAutreMnemonic() {
        return autreMnemonic;
    }

    public void setAutreMnemonic(String autreMnemonic) {
        this.autreMnemonic = autreMnemonic;
    }

    public String getNbrIter() {
        return nbrIter;
    }

    public void setNbrIter(String nbrIter) {
        this.nbrIter = nbrIter;
    }

    public String getEnvironnementsCiblesElements() {
        return environnementsCiblesElements;
    }

    public void setEnvironnementsCiblesElements(String environnementsCiblesElements) {
        this.environnementsCiblesElements = environnementsCiblesElements;
    }

    public String getStockEnvironnementsElements() {
        return stockEnvironnementsElements;
    }

    public void setStockEnvironnementsElements(String stockEnvironnementsElements) {
        this.stockEnvironnementsElements = stockEnvironnementsElements;
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public void setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;
    }
}
