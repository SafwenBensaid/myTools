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
@Table(name = "users_has_groupe")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UsersHasGroupe.findAll", query = "SELECT u FROM UsersHasGroupe u"),
    @NamedQuery(name = "UsersHasGroupe.findByUsersLogin", query = "SELECT u FROM UsersHasGroupe u WHERE u.usersHasGroupePK.usersLogin = :usersLogin"),
    @NamedQuery(name = "UsersHasGroupe.findByGroupeNom", query = "SELECT u FROM UsersHasGroupe u WHERE u.usersHasGroupePK.groupeNom = :groupeNom"),
    @NamedQuery(name = "UsersHasGroupe.findByDescription", query = "SELECT u FROM UsersHasGroupe u WHERE u.description = :description")})
public class UsersHasGroupe implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected UsersHasGroupePK usersHasGroupePK;
    @Basic(optional = false)
    @Column(name = "description")
    private String description;
    @JoinColumn(name = "users_login", referencedColumnName = "login", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Users users;
    @JoinColumn(name = "groupe_nom", referencedColumnName = "nom", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Groupe groupe;

    public UsersHasGroupe() {
    }

    public UsersHasGroupe(UsersHasGroupePK usersHasGroupePK) {
        this.usersHasGroupePK = usersHasGroupePK;
    }

    public UsersHasGroupe(UsersHasGroupePK usersHasGroupePK, String description) {
        this.usersHasGroupePK = usersHasGroupePK;
        this.description = description;
    }

    public UsersHasGroupe(String usersLogin, String groupeNom) {
        this.usersHasGroupePK = new UsersHasGroupePK(usersLogin, groupeNom);
    }

    public UsersHasGroupePK getUsersHasGroupePK() {
        return usersHasGroupePK;
    }

    public void setUsersHasGroupePK(UsersHasGroupePK usersHasGroupePK) {
        this.usersHasGroupePK = usersHasGroupePK;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public Groupe getGroupe() {
        return groupe;
    }

    public void setGroupe(Groupe groupe) {
        this.groupe = groupe;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (usersHasGroupePK != null ? usersHasGroupePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsersHasGroupe)) {
            return false;
        }
        UsersHasGroupe other = (UsersHasGroupe) object;
        if ((this.usersHasGroupePK == null && other.usersHasGroupePK != null) || (this.usersHasGroupePK != null && !this.usersHasGroupePK.equals(other.usersHasGroupePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entitiesMysql.UsersHasGroupe[ usersHasGroupePK=" + usersHasGroupePK + " ]";
    }
}
