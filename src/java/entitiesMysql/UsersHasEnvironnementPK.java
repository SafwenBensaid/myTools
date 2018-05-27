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
public class UsersHasEnvironnementPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "users_login")
    private String usersLogin;
    @Basic(optional = false)
    @Column(name = "environnement_nom")
    private String environnementNom;

    public UsersHasEnvironnementPK() {
    }

    public UsersHasEnvironnementPK(String usersLogin, String environnementNom) {
        this.usersLogin = usersLogin;
        this.environnementNom = environnementNom;
    }

    public String getUsersLogin() {
        return usersLogin;
    }

    public void setUsersLogin(String usersLogin) {
        this.usersLogin = usersLogin;
    }

    public String getEnvironnementNom() {
        return environnementNom;
    }

    public void setEnvironnementNom(String environnementNom) {
        this.environnementNom = environnementNom;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (usersLogin != null ? usersLogin.hashCode() : 0);
        hash += (environnementNom != null ? environnementNom.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsersHasEnvironnementPK)) {
            return false;
        }
        UsersHasEnvironnementPK other = (UsersHasEnvironnementPK) object;
        if ((this.usersLogin == null && other.usersLogin != null) || (this.usersLogin != null && !this.usersLogin.equals(other.usersLogin))) {
            return false;
        }
        if ((this.environnementNom == null && other.environnementNom != null) || (this.environnementNom != null && !this.environnementNom.equals(other.environnementNom))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entitiesMysql.UsersHasEnvironnementPK[ usersLogin=" + usersLogin + ", environnementNom=" + environnementNom + " ]";
    }
}
