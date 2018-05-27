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
 * @author 04494
 */
@Entity
@Table(name = "auth_maintenance")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AuthMaintenance.findAll", query = "SELECT a FROM AuthMaintenance a"),
    @NamedQuery(name = "AuthMaintenance.findByAuthoriser", query = "SELECT a FROM AuthMaintenance a WHERE a.authoriser = :authoriser"),
    @NamedQuery(name = "AuthMaintenance.findByIdAnomalie", query = "SELECT a FROM AuthMaintenance a WHERE a.idAnomalie = :idAnomalie"),
    @NamedQuery(name = "AuthMaintenance.findByDateAuth", query = "SELECT a FROM AuthMaintenance a WHERE a.dateAuth = :dateAuth")})
public class AuthMaintenance implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "authoriser")
    private String authoriser;
    @Id
    @Basic(optional = false)
    @Column(name = "id_anomalie")
    private Integer idAnomalie;
    @Basic(optional = false)
    @Column(name = "date_auth")
    private String dateAuth;

    public AuthMaintenance() {
    }

    public AuthMaintenance(Integer idAnomalie) {
        this.idAnomalie = idAnomalie;
    }

    public AuthMaintenance(Integer idAnomalie, String authoriser, String dateAuth) {
        this.idAnomalie = idAnomalie;
        this.authoriser = authoriser;
        this.dateAuth = dateAuth;
    }

    public String getAuthoriser() {
        return authoriser;
    }

    public void setAuthoriser(String authoriser) {
        this.authoriser = authoriser;
    }

    public Integer getIdAnomalie() {
        return idAnomalie;
    }

    public void setIdAnomalie(Integer idAnomalie) {
        this.idAnomalie = idAnomalie;
    }

    public String getDateAuth() {
        return dateAuth;
    }

    public void setDateAuth(String dateAuth) {
        this.dateAuth = dateAuth;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAnomalie != null ? idAnomalie.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AuthMaintenance)) {
            return false;
        }
        AuthMaintenance other = (AuthMaintenance) object;
        if ((this.idAnomalie == null && other.idAnomalie != null) || (this.idAnomalie != null && !this.idAnomalie.equals(other.idAnomalie))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entitiesMysql.AuthMaintenance[ idAnomalie=" + idAnomalie + " ]";
    }
}
