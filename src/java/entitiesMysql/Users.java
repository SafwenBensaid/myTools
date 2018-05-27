/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entitiesMysql;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import tools.Tools;

/**
 *
 * @author 04486
 */
@Entity
@Table(name = "users")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Users.findAll", query = "SELECT u FROM Users u"),
    @NamedQuery(name = "Users.findAllNames", query = "SELECT u.nomPrenom FROM Users u"),
    @NamedQuery(name = "Users.findByLogin", query = "SELECT u FROM Users u WHERE u.login = :login"),
    @NamedQuery(name = "Users.findByNomPrenom", query = "SELECT u FROM Users u WHERE u.nomPrenom = :nomPrenom"),
    @NamedQuery(name = "Users.findByEmail", query = "SELECT u FROM Users u WHERE u.email = :email")})
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "login")
    private String login;
    @Basic(optional = false)
    @Column(name = "matricule")
    private String matricule;
    @Basic(optional = false)
    @Column(name = "nomPrenom")
    private String nomPrenom;
    @Basic(optional = false)
    @Column(name = "dateCreation")
    private String dateCreation;
    @Basic(optional = false)
    @Column(name = "email")
    private String email;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "users")
    private List<UsersHasGroupe> usersHasGroupeList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "users")
    private List<UsersHasEnvironnement> usersHasEnvironnementList;

    public Users() {
    }

    public Users(String login) {
        this.login = login;
    }

    public Users(String login, String nomPrenom, String email) {
        this.login = login;
        this.nomPrenom = nomPrenom;
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getNomPrenom() {
        return nomPrenom;
    }

    public void setNomPrenom(String nomPrenom) {
        this.nomPrenom = nomPrenom;
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getEmail() {
        if (email != null && email.length() > 0) {
            return email;
        } else {
            return Tools.getEmailByLogin(matricule);
        }
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @XmlTransient
    public List<UsersHasGroupe> getUsersHasGroupeList() {
        return usersHasGroupeList;
    }

    public void setUsersHasGroupeList(List<UsersHasGroupe> usersHasGroupeList) {
        this.usersHasGroupeList = usersHasGroupeList;
    }

    @XmlTransient
    public List<UsersHasEnvironnement> getUsersHasEnvironnementList() {
        return usersHasEnvironnementList;
    }

    public void setUsersHasEnvironnementList(List<UsersHasEnvironnement> usersHasEnvironnementList) {
        this.usersHasEnvironnementList = usersHasEnvironnementList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (login != null ? login.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Users)) {
            return false;
        }
        Users other = (Users) object;
        if ((this.login == null && other.login != null) || (this.login != null && !this.login.equals(other.login))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Users{" + "login=" + login + ", matricule=" + matricule + ", email=" + email + '}';
    }
}
