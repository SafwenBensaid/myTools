/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.util.List;

/**
 *
 * @author 04486
 */
public class ResultatAnalyseIntersectionDTO {

    private String typeObj;
    private String nomObj;
    private List<NumeLivraisonRevisionDTO> listeNumLivraisonRevision;

    public String getTypeObj() {
        return typeObj;
    }

    public void setTypeObj(String typeObj) {
        this.typeObj = typeObj;
    }

    public String getNomObj() {
        return nomObj;
    }

    public void setNomObj(String nomObj) {
        this.nomObj = nomObj;
    }

    public List<NumeLivraisonRevisionDTO> getListeNumLivraisonRevision() {
        return listeNumLivraisonRevision;
    }

    public void setListeNumLivraisonRevision(List<NumeLivraisonRevisionDTO> listeNumLivraisonRevision) {
        this.listeNumLivraisonRevision = listeNumLivraisonRevision;
    }
}
