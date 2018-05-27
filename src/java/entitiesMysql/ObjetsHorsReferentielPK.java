/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entitiesMysql;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author 04494
 */
@Embeddable
public class ObjetsHorsReferentielPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "typeObjet")
    private String typeObjet;
    @Basic(optional = false)
    @Column(name = "circuit")
    private String circuit;

    public ObjetsHorsReferentielPK() {
    }

    public ObjetsHorsReferentielPK(String typeObjet, String circuit) {
        this.typeObjet = typeObjet;
        this.circuit = circuit;
    }

    public String getTypeObjet() {
        return typeObjet;
    }

    public void setTypeObjet(String typeObjet) {
        this.typeObjet = typeObjet;
    }

    public String getCircuit() {
        return circuit;
    }

    public void setCircuit(String circuit) {
        this.circuit = circuit;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (typeObjet != null ? typeObjet.hashCode() : 0);
        hash += (circuit != null ? circuit.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ObjetsHorsReferentielPK)) {
            return false;
        }
        ObjetsHorsReferentielPK other = (ObjetsHorsReferentielPK) object;
        if ((this.typeObjet == null && other.typeObjet != null) || (this.typeObjet != null && !this.typeObjet.equals(other.typeObjet))) {
            return false;
        }
        if ((this.circuit == null && other.circuit != null) || (this.circuit != null && !this.circuit.equals(other.circuit))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entitiesMysql.ObjetsHorsReferentielPK[ typeObjet=" + typeObjet + ", circuit=" + circuit + " ]";
    }
}
