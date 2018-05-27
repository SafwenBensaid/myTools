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
public class GroupeHasFonctionalitePK implements Serializable {

    @Basic(optional = false)
    @Column(name = "groupe_nom")
    private String groupeNom;
    @Basic(optional = false)
    @Column(name = "fonctionalite_name")
    private String fonctionaliteName;

    public GroupeHasFonctionalitePK() {
    }

    public GroupeHasFonctionalitePK(String groupeNom, String fonctionaliteName) {
        this.groupeNom = groupeNom;
        this.fonctionaliteName = fonctionaliteName;
    }

    public String getGroupeNom() {
        return groupeNom;
    }

    public void setGroupeNom(String groupeNom) {
        this.groupeNom = groupeNom;
    }

    public String getFonctionaliteName() {
        return fonctionaliteName;
    }

    public void setFonctionaliteName(String fonctionaliteName) {
        this.fonctionaliteName = fonctionaliteName;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (groupeNom != null ? groupeNom.hashCode() : 0);
        hash += (fonctionaliteName != null ? fonctionaliteName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GroupeHasFonctionalitePK)) {
            return false;
        }
        GroupeHasFonctionalitePK other = (GroupeHasFonctionalitePK) object;
        if ((this.groupeNom == null && other.groupeNom != null) || (this.groupeNom != null && !this.groupeNom.equals(other.groupeNom))) {
            return false;
        }
        if ((this.fonctionaliteName == null && other.fonctionaliteName != null) || (this.fonctionaliteName != null && !this.fonctionaliteName.equals(other.fonctionaliteName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entitiesMysql.GroupeHasFonctionalitePK[ groupeNom=" + groupeNom + ", fonctionaliteName=" + fonctionaliteName + " ]";
    }
}
