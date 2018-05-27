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
public class GroupeHasEnvironnementPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "groupe_name")
    private String groupeName;
    @Basic(optional = false)
    @Column(name = "environnement_nom")
    private String environnementNom;

    public GroupeHasEnvironnementPK() {
    }

    public GroupeHasEnvironnementPK(String groupeName, String environnementNom) {
        this.groupeName = groupeName;
        this.environnementNom = environnementNom;
    }

    public String getGroupeName() {
        return groupeName;
    }

    public void setGroupeName(String groupeName) {
        this.groupeName = groupeName;
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
        hash += (groupeName != null ? groupeName.hashCode() : 0);
        hash += (environnementNom != null ? environnementNom.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GroupeHasEnvironnementPK)) {
            return false;
        }
        GroupeHasEnvironnementPK other = (GroupeHasEnvironnementPK) object;
        if ((this.groupeName == null && other.groupeName != null) || (this.groupeName != null && !this.groupeName.equals(other.groupeName))) {
            return false;
        }
        if ((this.environnementNom == null && other.environnementNom != null) || (this.environnementNom != null && !this.environnementNom.equals(other.environnementNom))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entitiesMysql.GroupeHasEnvironnementPK[ groupeName=" + groupeName + ", environnementNom=" + environnementNom + " ]";
    }
}
