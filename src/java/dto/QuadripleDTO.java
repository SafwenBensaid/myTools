/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

/**
 *
 * @author 04486
 */
public class QuadripleDTO {

    private String valeur1;
    private String valeur2;
    private String valeur3;
    private String valeur4;

    public QuadripleDTO(String valeur1, String valeur2, String valeur3, String valeur4) {
        this.valeur1 = valeur1;
        this.valeur2 = valeur2;
        this.valeur3 = valeur3;
        this.valeur4 = valeur4;
    }

    public String getValeur1() {
        return valeur1;
    }

    public void setValeur1(String valeur1) {
        this.valeur1 = valeur1;
    }

    public String getValeur2() {
        return valeur2;
    }

    public void setValeur2(String valeur2) {
        this.valeur2 = valeur2;
    }

    public String getValeur3() {
        return valeur3;
    }

    public void setValeur3(String valeur3) {
        this.valeur3 = valeur3;
    }

    public String getValeur4() {
        return valeur4;
    }

    public void setValeur4(String valeur4) {
        this.valeur4 = valeur4;
    }
}
