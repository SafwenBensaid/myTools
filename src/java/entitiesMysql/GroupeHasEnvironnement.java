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
@Table(name = "groupe_has_environnement")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GroupeHasEnvironnement.findAll", query = "SELECT g FROM GroupeHasEnvironnement g"),
    @NamedQuery(name = "GroupeHasEnvironnement.findByGroupeName", query = "SELECT g FROM GroupeHasEnvironnement g WHERE g.groupeHasEnvironnementPK.groupeName = :groupeName"),
    @NamedQuery(name = "GroupeHasEnvironnement.findByEnvironnementNom", query = "SELECT g FROM GroupeHasEnvironnement g WHERE g.groupeHasEnvironnementPK.environnementNom = :environnementNom"),
    @NamedQuery(name = "GroupeHasEnvironnement.findByDescription", query = "SELECT g FROM GroupeHasEnvironnement g WHERE g.description = :description")})
public class GroupeHasEnvironnement implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected GroupeHasEnvironnementPK groupeHasEnvironnementPK;
    @Basic(optional = false)
    @Column(name = "description")
    private String description;
    @JoinColumn(name = "groupe_name", referencedColumnName = "nom", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Groupe groupe;
    @JoinColumn(name = "environnement_nom", referencedColumnName = "nom", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Environnement environnement;

    public GroupeHasEnvironnement() {
    }

    public GroupeHasEnvironnement(GroupeHasEnvironnementPK groupeHasEnvironnementPK) {
        this.groupeHasEnvironnementPK = groupeHasEnvironnementPK;
    }

    public GroupeHasEnvironnement(GroupeHasEnvironnementPK groupeHasEnvironnementPK, String description) {
        this.groupeHasEnvironnementPK = groupeHasEnvironnementPK;
        this.description = description;
    }

    public GroupeHasEnvironnement(String groupeName, String environnementNom) {
        this.groupeHasEnvironnementPK = new GroupeHasEnvironnementPK(groupeName, environnementNom);
    }

    public GroupeHasEnvironnementPK getGroupeHasEnvironnementPK() {
        return groupeHasEnvironnementPK;
    }

    public void setGroupeHasEnvironnementPK(GroupeHasEnvironnementPK groupeHasEnvironnementPK) {
        this.groupeHasEnvironnementPK = groupeHasEnvironnementPK;
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

    public Environnement getEnvironnement() {
        return environnement;
    }

    public void setEnvironnement(Environnement environnement) {
        this.environnement = environnement;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (groupeHasEnvironnementPK != null ? groupeHasEnvironnementPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GroupeHasEnvironnement)) {
            return false;
        }
        GroupeHasEnvironnement other = (GroupeHasEnvironnement) object;
        if ((this.groupeHasEnvironnementPK == null && other.groupeHasEnvironnementPK != null) || (this.groupeHasEnvironnementPK != null && !this.groupeHasEnvironnementPK.equals(other.groupeHasEnvironnementPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entitiesMysql.GroupeHasEnvironnement[ groupeHasEnvironnementPK=" + groupeHasEnvironnementPK + " ]";
    }
}
