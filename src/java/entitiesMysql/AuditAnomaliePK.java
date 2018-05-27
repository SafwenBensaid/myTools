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
public class AuditAnomaliePK implements Serializable {

    @Basic(optional = false)
    @Column(name = "id_anomalie")
    private int idAnomalie;
    @Basic(optional = false)
    @Column(name = "etat_auth")
    private int etatAuth;

    public AuditAnomaliePK() {
    }

    public AuditAnomaliePK(int idAnomalie, int etatAuth) {
        this.idAnomalie = idAnomalie;
        this.etatAuth = etatAuth;
    }

    public int getIdAnomalie() {
        return idAnomalie;
    }

    public void setIdAnomalie(int idAnomalie) {
        this.idAnomalie = idAnomalie;
    }

    public int getEtatAuth() {
        return etatAuth;
    }

    public void setEtatAuth(int etatAuth) {
        this.etatAuth = etatAuth;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idAnomalie;
        hash += (int) etatAuth;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AuditAnomaliePK)) {
            return false;
        }
        AuditAnomaliePK other = (AuditAnomaliePK) object;
        if (this.idAnomalie != other.idAnomalie) {
            return false;
        }
        if (this.etatAuth != other.etatAuth) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entitiesMysql.AuditAnomaliePK[ idAnomalie=" + idAnomalie + ", etatAuth=" + etatAuth + " ]";
    }
}
