/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entitiesTrac;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author 04486
 */
@Entity
@Table(name = "session")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Session.findAll", query = "SELECT s FROM Session s"),
    @NamedQuery(name = "Session.findByAuthenticated", query = "SELECT s FROM Session s WHERE s.sessionPK.authenticated = :authenticated"),
    @NamedQuery(name = "Session.findByLastVisit", query = "SELECT s FROM Session s WHERE s.lastVisit = :lastVisit")})
public class Session implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SessionPK sessionPK;
    @Column(name = "last_visit")
    private Integer lastVisit;

    public Session() {
    }

    public Session(SessionPK sessionPK) {
        this.sessionPK = sessionPK;
    }

    public Session(String sid, int authenticated) {
        this.sessionPK = new SessionPK(sid, authenticated);
    }

    public SessionPK getSessionPK() {
        return sessionPK;
    }

    public void setSessionPK(SessionPK sessionPK) {
        this.sessionPK = sessionPK;
    }

    public Integer getLastVisit() {
        return lastVisit;
    }

    public void setLastVisit(Integer lastVisit) {
        this.lastVisit = lastVisit;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sessionPK != null ? sessionPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Session)) {
            return false;
        }
        Session other = (Session) object;
        if ((this.sessionPK == null && other.sessionPK != null) || (this.sessionPK != null && !this.sessionPK.equals(other.sessionPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Session[ sessionPK=" + sessionPK + " ]";
    }
}
