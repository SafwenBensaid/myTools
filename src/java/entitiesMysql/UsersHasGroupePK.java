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
 * @author 04486
 */
@Embeddable
public class UsersHasGroupePK implements Serializable {

    @Basic(optional = false)
    @Column(name = "users_login")
    private String usersLogin;
    @Basic(optional = false)
    @Column(name = "groupe_nom")
    private String groupeNom;

    public UsersHasGroupePK() {
    }

    public UsersHasGroupePK(String usersLogin, String groupeNom) {
        this.usersLogin = usersLogin;
        this.groupeNom = groupeNom;
    }

    public String getUsersLogin() {
        return usersLogin;
    }

    public void setUsersLogin(String usersLogin) {
        this.usersLogin = usersLogin;
    }

    public String getGroupeNom() {
        return groupeNom;
    }

    public void setGroupeNom(String groupeNom) {
        this.groupeNom = groupeNom;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (usersLogin != null ? usersLogin.hashCode() : 0);
        hash += (groupeNom != null ? groupeNom.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsersHasGroupePK)) {
            return false;
        }
        UsersHasGroupePK other = (UsersHasGroupePK) object;
        if ((this.usersLogin == null && other.usersLogin != null) || (this.usersLogin != null && !this.usersLogin.equals(other.usersLogin))) {
            return false;
        }
        if ((this.groupeNom == null && other.groupeNom != null) || (this.groupeNom != null && !this.groupeNom.equals(other.groupeNom))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entitiesMysql.UsersHasGroupePK[ usersLogin=" + usersLogin + ", groupeNom=" + groupeNom + " ]";
    }
}
