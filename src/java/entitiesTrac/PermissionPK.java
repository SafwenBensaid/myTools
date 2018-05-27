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
public class PermissionPK implements Serializable {

    @Basic(optional = false)
    @Lob
    @Column(name = "username")
    private String username;
    @Basic(optional = false)
    @Lob
    @Column(name = "action")
    private String action;

    public PermissionPK() {
    }

    public PermissionPK(String username, String action) {
        this.username = username;
        this.action = action;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (username != null ? username.hashCode() : 0);
        hash += (action != null ? action.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PermissionPK)) {
            return false;
        }
        PermissionPK other = (PermissionPK) object;
        if ((this.username == null && other.username != null) || (this.username != null && !this.username.equals(other.username))) {
            return false;
        }
        if ((this.action == null && other.action != null) || (this.action != null && !this.action.equals(other.action))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.PermissionPK[ username=" + username + ", action=" + action + " ]";
    }
}
