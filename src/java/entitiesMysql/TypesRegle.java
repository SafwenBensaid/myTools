/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entitiesMysql;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author 04486
 */
@Entity
@Table(name = "types_regle")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TypesRegle.findAll", query = "SELECT t FROM TypesRegle t"),
    @NamedQuery(name = "TypesRegle.findByCle", query = "SELECT t FROM TypesRegle t WHERE t.cle = :cle"),
    @NamedQuery(name = "TypesRegle.findByRegle", query = "SELECT t FROM TypesRegle t WHERE t.regle = :regle"),
    @NamedQuery(name = "TypesRegle.findByNature", query = "SELECT t FROM TypesRegle t WHERE t.nature = :nature"),
    @NamedQuery(name = "TypesRegle.findByRemarque", query = "SELECT t FROM TypesRegle t WHERE t.remarque = :remarque")})
public class TypesRegle implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "cle")
    private String cle;
    @Basic(optional = false)
    @Column(name = "regle")
    private String regle;
    @Basic(optional = false)
    @Column(name = "nature")
    private String nature;
    @Column(name = "remarque")
    private String remarque;

    public TypesRegle() {
    }

    public TypesRegle(String cle) {
        this.cle = cle;
    }

    public TypesRegle(String cle, String regle, String nature) {
        this.cle = cle;
        this.regle = regle;
        this.nature = nature;
    }

    public String getCle() {
        return cle;
    }

    public void setCle(String cle) {
        this.cle = cle;
    }

    public String getRegle() {
        return regle;
    }

    public void setRegle(String regle) {
        this.regle = regle;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getRemarque() {
        return remarque;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cle != null ? cle.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TypesRegle)) {
            return false;
        }
        TypesRegle other = (TypesRegle) object;
        if ((this.cle == null && other.cle != null) || (this.cle != null && !this.cle.equals(other.cle))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entitiesMysql.TypesRegle[ cle=" + cle + " ]";
    }
}
