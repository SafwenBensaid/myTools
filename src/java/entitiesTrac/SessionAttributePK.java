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
public class SessionAttributePK implements Serializable {

    @Basic(optional = false)
    @Lob
    @Column(name = "sid")
    private String sid;
    @Basic(optional = false)
    @Column(name = "authenticated")
    private int authenticated;
    @Basic(optional = false)
    @Lob
    @Column(name = "name")
    private String name;

    public SessionAttributePK() {
    }

    public SessionAttributePK(String sid, int authenticated, String name) {
        this.sid = sid;
        this.authenticated = authenticated;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sid != null ? sid.hashCode() : 0);
        hash += (int) authenticated;
        hash += (name != null ? name.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SessionAttributePK)) {
            return false;
        }
        SessionAttributePK other = (SessionAttributePK) object;
        if ((this.sid == null && other.sid != null) || (this.sid != null && !this.sid.equals(other.sid))) {
            return false;
        }
        if (this.authenticated != other.authenticated) {
            return false;
        }
        if ((this.name == null && other.name != null) || (this.name != null && !this.name.equals(other.name))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.SessionAttributePK[ sid=" + sid + ", authenticated=" + authenticated + ", name=" + name + " ]";
    }
}
