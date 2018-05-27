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
public class DelaisGiPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "priorite")
    private String priorite;
    @Basic(optional = false)
    @Column(name = "type")
    private String type;

    public DelaisGiPK() {
    }

    public DelaisGiPK(String priorite, String type) {
        this.priorite = priorite;
        this.type = type;
    }

    public String getPriorite() {
        return priorite;
    }

    public void setPriorite(String priorite) {
        this.priorite = priorite;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (priorite != null ? priorite.hashCode() : 0);
        hash += (type != null ? type.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DelaisGiPK)) {
            return false;
        }
        DelaisGiPK other = (DelaisGiPK) object;
        if ((this.priorite == null && other.priorite != null) || (this.priorite != null && !this.priorite.equals(other.priorite))) {
            return false;
        }
        if ((this.type == null && other.type != null) || (this.type != null && !this.type.equals(other.type))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entitiesMysql.DelaisGiPK[ priorite=" + priorite + ", type=" + type + " ]";
    }
}
