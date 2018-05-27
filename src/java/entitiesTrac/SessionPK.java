/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entitiesTrac;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;

/**
 *
 * @author 04486
 */
@Embeddable
public class SessionPK implements Serializable {

    @Basic(optional = false)
    @Lob
    @Column(name = "sid")
    private String sid;
    @Basic(optional = false)
    @Column(name = "authenticated")
    private int authenticated;

    public SessionPK() {
    }

    public SessionPK(String sid, int authenticated) {
        this.sid = sid;
        this.authenticated = authenticated;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public int getAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(int authenticated) {
        this.authenticated = authenticated;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sid != null ? sid.hashCode() : 0);
        hash += (int) authenticated;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SessionPK)) {
            return false;
        }
        SessionPK other = (SessionPK) object;
        if ((this.sid == null && other.sid != null) || (this.sid != null && !this.sid.equals(other.sid))) {
            return false;
        }
        if (this.authenticated != other.authenticated) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.SessionPK[ sid=" + sid + ", authenticated=" + authenticated + " ]";
    }
}
