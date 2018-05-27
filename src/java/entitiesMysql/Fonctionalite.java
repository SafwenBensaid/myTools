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
@Table(name = "fonctionalite")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Fonctionalite.findAll", query = "SELECT f FROM Fonctionalite f"),
    @NamedQuery(name = "Fonctionalite.findByName", query = "SELECT f FROM Fonctionalite f WHERE f.name = :name"),
    @NamedQuery(name = "Fonctionalite.findByDescription", query = "SELECT f FROM Fonctionalite f WHERE f.description = :description"),
    @NamedQuery(name = "Fonctionalite.findByType", query = "SELECT f FROM Fonctionalite f WHERE f.type = :type")})
public class Fonctionalite implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @Column(name = "type")
    private String type;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fonctionalite")
    private List<GroupeHasFonctionalite> groupeHasFonctionaliteList;

    public Fonctionalite() {
    }

    public Fonctionalite(String name) {
        this.name = name;
    }

    public Fonctionalite(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @XmlTransient
    public List<GroupeHasFonctionalite> getGroupeHasFonctionaliteList() {
        return groupeHasFonctionaliteList;
    }

    public void setGroupeHasFonctionaliteList(List<GroupeHasFonctionalite> groupeHasFonctionaliteList) {
        this.groupeHasFonctionaliteList = groupeHasFonctionaliteList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (name != null ? name.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Fonctionalite)) {
            return false;
        }
        Fonctionalite other = (Fonctionalite) object;
        if ((this.name == null && other.name != null) || (this.name != null && !this.name.equals(other.name))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entitiesMysql.Fonctionalite[ name=" + name + " ]";
    }
}
