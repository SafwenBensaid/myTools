/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entitiesMysql;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author 04486
 */
@Entity
@Table(name = "users_has_environnement")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UsersHasEnvironnement.findAll", query = "SELECT u FROM UsersHasEnvironnement u"),
    @NamedQuery(name = "UsersHasEnvironnement.findByUsersLogin", query = "SELECT u FROM UsersHasEnvironnement u WHERE u.usersHasEnvironnementPK.usersLogin = :usersLogin"),
    @NamedQuery(name = "UsersHasEnvironnement.findByEnvironnementNom", query = "SELECT u FROM UsersHasEnvironnement u WHERE u.usersHasEnvironnementPK.environnementNom = :environnementNom"),
    @NamedQuery(name = "UsersHasEnvironnement.findByUsersLoginAndEnvironnementNom", query = "SELECT u FROM UsersHasEnvironnement u WHERE u.usersHasEnvironnementPK.usersLogin = :usersLogin AND u.usersHasEnvironnementPK.environnementNom = :environnementNom"),
    @NamedQuery(name = "UsersHasEnvironnement.findByBrowserLogin", query = "SELECT u FROM UsersHasEnvironnement u WHERE u.browserLogin = :browserLogin"),
    @NamedQuery(name = "UsersHasEnvironnement.findByBrowsrPassword", query = "SELECT u FROM UsersHasEnvironnement u WHERE u.browsrPassword = :browsrPassword"),
    @NamedQuery(name = "UsersHasEnvironnement.findByUserAndEnvironnementName", query = "SELECT u FROM UsersHasEnvironnement u WHERE u.usersHasEnvironnementPK.usersLogin = :usersLogin and u.usersHasEnvironnementPK.environnementNom = :environnementNom")})
public class UsersHasEnvironnement implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected UsersHasEnvironnementPK usersHasEnvironnementPK;
    @Basic(optional = false)
    @Column(name = "browser_login")
    private String browserLogin;
    @Basic(optional = false)
    @Column(name = "browsr_password")
    private String browsrPassword;
    @JoinColumn(name = "users_login", referencedColumnName = "login", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Users users;
    @JoinColumn(name = "environnement_nom", referencedColumnName = "nom", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Environnement environnement;

    public UsersHasEnvironnement() {
    }

    public UsersHasEnvironnement(UsersHasEnvironnementPK usersHasEnvironnementPK) {
        this.usersHasEnvironnementPK = usersHasEnvironnementPK;
    }

    public UsersHasEnvironnement(UsersHasEnvironnementPK usersHasEnvironnementPK, String browserLogin, String browsrPassword) {
        this.usersHasEnvironnementPK = usersHasEnvironnementPK;
        this.browserLogin = browserLogin;
        this.browsrPassword = browsrPassword;
    }

    public UsersHasEnvironnement(String usersLogin, String environnementNom) {
        this.usersHasEnvironnementPK = new UsersHasEnvironnementPK(usersLogin, environnementNom);
    }

    public UsersHasEnvironnementPK getUsersHasEnvironnementPK() {
        return usersHasEnvironnementPK;
    }

    public void setUsersHasEnvironnementPK(UsersHasEnvironnementPK usersHasEnvironnementPK) {
        this.usersHasEnvironnementPK = usersHasEnvironnementPK;
    }

    public String getBrowserLogin() {
        return browserLogin;
    }

    public void setBrowserLogin(String browserLogin) {
        this.browserLogin = browserLogin;
    }

    public String getBrowsrPassword() {
        return browsrPassword;
    }

    public void setBrowsrPassword(String browsrPassword) {
        this.browsrPassword = browsrPassword;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public Environnement getEnvironnement() {
        return environnement;
    }

    public void setEnvironnement(Environnement environnement) {
        this.environnement = environnement;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (usersHasEnvironnementPK != null ? usersHasEnvironnementPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsersHasEnvironnement)) {
            return false;
        }
        UsersHasEnvironnement other = (UsersHasEnvironnement) object;
        if ((this.usersHasEnvironnementPK == null && other.usersHasEnvironnementPK != null) || (this.usersHasEnvironnementPK != null && !this.usersHasEnvironnementPK.equals(other.usersHasEnvironnementPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entitiesMysql.UsersHasEnvironnement[ usersHasEnvironnementPK=" + usersHasEnvironnementPK + " ]";
    }
}
