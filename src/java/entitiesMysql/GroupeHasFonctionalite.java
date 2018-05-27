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
@Table(name = "groupe_has_fonctionalite")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GroupeHasFonctionalite.findAll", query = "SELECT g FROM GroupeHasFonctionalite g"),
    @NamedQuery(name = "GroupeHasFonctionalite.findByGroupeNom", query = "SELECT g FROM GroupeHasFonctionalite g WHERE g.groupeHasFonctionalitePK.groupeNom = :groupeNom"),
    @NamedQuery(name = "GroupeHasFonctionalite.findByFonctionaliteName", query = "SELECT g FROM GroupeHasFonctionalite g WHERE g.groupeHasFonctionalitePK.fonctionaliteName = :fonctionaliteName"),
    @NamedQuery(name = "GroupeHasFonctionalite.findByDescription", query = "SELECT g FROM GroupeHasFonctionalite g WHERE g.description = :description")})
public class GroupeHasFonctionalite implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected GroupeHasFonctionalitePK groupeHasFonctionalitePK;
    @Basic(optional = false)
    @Column(name = "description")
    private String description;
    @JoinColumn(name = "groupe_nom", referencedColumnName = "nom", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Groupe groupe;
    @JoinColumn(name = "fonctionalite_name", referencedColumnName = "name", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Fonctionalite fonctionalite;

    public GroupeHasFonctionalite() {
    }

    public GroupeHasFonctionalite(GroupeHasFonctionalitePK groupeHasFonctionalitePK) {
        this.groupeHasFonctionalitePK = groupeHasFonctionalitePK;
    }

    public GroupeHasFonctionalite(GroupeHasFonctionalitePK groupeHasFonctionalitePK, String description) {
        this.groupeHasFonctionalitePK = groupeHasFonctionalitePK;
        this.description = description;
    }

    public GroupeHasFonctionalite(String groupeNom, String fonctionaliteName) {
        this.groupeHasFonctionalitePK = new GroupeHasFonctionalitePK(groupeNom, fonctionaliteName);
    }

    public GroupeHasFonctionalitePK getGroupeHasFonctionalitePK() {
        return groupeHasFonctionalitePK;
    }

    public void setGroupeHasFonctionalitePK(GroupeHasFonctionalitePK groupeHasFonctionalitePK) {
        this.groupeHasFonctionalitePK = groupeHasFonctionalitePK;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Groupe getGroupe() {
        return groupe;
    }

    public void setGroupe(Groupe groupe) {
        this.groupe = groupe;
    }

    public Fonctionalite getFonctionalite() {
        return fonctionalite;
    }

    public void setFonctionalite(Fonctionalite fonctionalite) {
        this.fonctionalite = fonctionalite;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (groupeHasFonctionalitePK != null ? groupeHasFonctionalitePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GroupeHasFonctionalite)) {
            return false;
        }
        GroupeHasFonctionalite other = (GroupeHasFonctionalite) object;
        if ((this.groupeHasFonctionalitePK == null && other.groupeHasFonctionalitePK != null) || (this.groupeHasFonctionalitePK != null && !this.groupeHasFonctionalitePK.equals(other.groupeHasFonctionalitePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entitiesMysql.GroupeHasFonctionalite[ groupeHasFonctionalitePK=" + groupeHasFonctionalitePK + " ]";
    }
}
