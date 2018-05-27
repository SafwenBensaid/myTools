/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package strutsForms;

/**
 *
 * @author 04486
 */
public class ConstitutionPackMultiprojetsForm extends org.apache.struts.action.ActionForm {

    public ConstitutionPackMultiprojetsForm() {
        super();
        // TODO Auto-generated constructor stub
    }
    private String nomPack;
    private String tri;
    private String projetsCiblesElements;
    private String stockProjetsElements;

    public String getNomPack() {
        return nomPack;
    }

    public void setNomPack(String nomPack) {
        this.nomPack = nomPack;
    }

    public String getProjetsCiblesElements() {
        return projetsCiblesElements;
    }

    public void setProjetsCiblesElements(String projetsCiblesElements) {
        this.projetsCiblesElements = projetsCiblesElements;
    }

    public String getStockProjetsElements() {
        return stockProjetsElements;
    }

    public void setStockProjetsElements(String stockProjetsElements) {
        this.stockProjetsElements = stockProjetsElements;
    }

    public String getTri() {
        return tri;
    }

    public void setTri(String tri) {
        this.tri = tri;
    }
}
