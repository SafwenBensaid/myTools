/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entitiesMysql;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author 04494
 */
@Entity
@Table(name = "objets_hors_referentiel")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ObjetsHorsReferentiel.findAll", query = "SELECT o FROM ObjetsHorsReferentiel o"),
    @NamedQuery(name = "ObjetsHorsReferentiel.findByTypeObjet", query = "SELECT o FROM ObjetsHorsReferentiel o WHERE o.objetsHorsReferentielPK.typeObjet = :typeObjet"),
    @NamedQuery(name = "ObjetsHorsReferentiel.deleteByCircuit", query = "DELETE FROM ObjetsHorsReferentiel o WHERE o.objetsHorsReferentielPK.circuit = :circuit"),
    @NamedQuery(name = "ObjetsHorsReferentiel.findByCircuit", query = "SELECT o FROM ObjetsHorsReferentiel o WHERE o.objetsHorsReferentielPK.circuit = :circuit")})
public class ObjetsHorsReferentiel implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ObjetsHorsReferentielPK objetsHorsReferentielPK;

    public ObjetsHorsReferentiel() {
    }

    public ObjetsHorsReferentiel(ObjetsHorsReferentielPK objetsHorsReferentielPK) {
        this.objetsHorsReferentielPK = objetsHorsReferentielPK;
    }

    public ObjetsHorsReferentiel(String typeObjet, String circuit) {
        this.objetsHorsReferentielPK = new ObjetsHorsReferentielPK(typeObjet, circuit);
    }

    public ObjetsHorsReferentielPK getObjetsHorsReferentielPK() {
        return objetsHorsReferentielPK;
    }

    public void setObjetsHorsReferentielPK(ObjetsHorsReferentielPK objetsHorsReferentielPK) {
        this.objetsHorsReferentielPK = objetsHorsReferentielPK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (objetsHorsReferentielPK != null ? objetsHorsReferentielPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ObjetsHorsReferentiel)) {
            return false;
        }
        ObjetsHorsReferentiel other = (ObjetsHorsReferentiel) object;
        if ((this.objetsHorsReferentielPK == null && other.objetsHorsReferentielPK != null) || (this.objetsHorsReferentielPK != null && !this.objetsHorsReferentielPK.equals(other.objetsHorsReferentielPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entitiesMysql.ObjetsHorsReferentiel[ objetsHorsReferentielPK=" + objetsHorsReferentielPK + " ]";
    }
}
