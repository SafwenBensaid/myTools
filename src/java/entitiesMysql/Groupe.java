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

/**
 *
 * @author 04486
 */
@Entity
@Table(name = "groupe")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Groupe.findAll", query = "SELECT g FROM Groupe g"),
    @NamedQuery(name = "Groupe.findByNom", query = "SELECT g FROM Groupe g WHERE g.nom = :nom"),
    @NamedQuery(name = "Groupe.findByDescription", query = "SELECT g FROM Groupe g WHERE g.description = :description")})
public class Groupe implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "nom")
    private String nom;
    @Column(name = "description")
    private String description;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "groupe")
    private List<UsersHasGroupe> usersHasGroupeList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "groupe")
    private List<GroupeHasFonctionalite> groupeHasFonctionaliteList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "groupe")
    private List<GroupeHasEnvironnement> groupeHasEnvironnementList;

    public Groupe() {
    }

    public Groupe(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlTransient
    public List<UsersHasGroupe> getUsersHasGroupeList() {
        return usersHasGroupeList;
    }

    public void setUsersHasGroupeList(List<UsersHasGroupe> usersHasGroupeList) {
        this.usersHasGroupeList = usersHasGroupeList;
    }

    @XmlTransient
    public List<GroupeHasFonctionalite> getGroupeHasFonctionaliteList() {
        return groupeHasFonctionaliteList;
    }

    public void setGroupeHasFonctionaliteList(List<GroupeHasFonctionalite> groupeHasFonctionaliteList) {
        this.groupeHasFonctionaliteList = groupeHasFonctionaliteList;
    }

    @XmlTransient
    public List<GroupeHasEnvironnement> getGroupeHasEnvironnementList() {
        return groupeHasEnvironnementList;
    }

    public void setGroupeHasEnvironnementList(List<GroupeHasEnvironnement> groupeHasEnvironnementList) {
        this.groupeHasEnvironnementList = groupeHasEnvironnementList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nom != null ? nom.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Groupe)) {
            return false;
        }
        Groupe other = (Groupe) object;
        if ((this.nom == null && other.nom != null) || (this.nom != null && !this.nom.equals(other.nom))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entitiesMysql.Groupe[ nom=" + nom + " ]";
    }
}
