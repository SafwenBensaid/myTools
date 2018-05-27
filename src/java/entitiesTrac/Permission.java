/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entitiesTrac;

import java.io.Serializable;
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
@Table(name = "permission")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Permission.findAll", query = "SELECT p FROM Permission p")})
public class Permission implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PermissionPK permissionPK;

    public Permission() {
    }

    public Permission(PermissionPK permissionPK) {
        this.permissionPK = permissionPK;
    }

    public Permission(String username, String action) {
        this.permissionPK = new PermissionPK(username, action);
    }

    public PermissionPK getPermissionPK() {
        return permissionPK;
    }

    public void setPermissionPK(PermissionPK permissionPK) {
        this.permissionPK = permissionPK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (permissionPK != null ? permissionPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Permission)) {
            return false;
        }
        Permission other = (Permission) object;
        if ((this.permissionPK == null && other.permissionPK != null) || (this.permissionPK != null && !this.permissionPK.equals(other.permissionPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Permission[ permissionPK=" + permissionPK + " ]";
    }
}
