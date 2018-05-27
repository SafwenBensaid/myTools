/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entitiesMysql;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author 04486
 */
@Entity
@Table(name = "nbr_obj_par_pack")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NbrObjParPack.findAll", query = "SELECT n FROM NbrObjParPack n"),
    @NamedQuery(name = "NbrObjParPack.findByPackName", query = "SELECT n FROM NbrObjParPack n WHERE n.packName = :packName"),
    @NamedQuery(name = "NbrObjParPack.findByNbrObjets", query = "SELECT n FROM NbrObjParPack n WHERE n.nbrObjets = :nbrObjets")})
public class NbrObjParPack implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "packName")
    private String packName;
    @Basic(optional = false)
    @Column(name = "nbrObjets")
    private int nbrObjets;

    public NbrObjParPack() {
    }

    public NbrObjParPack(String packName) {
        this.packName = packName;
    }

    public NbrObjParPack(String packName, int nbrObjets) {
        this.packName = packName;
        this.nbrObjets = nbrObjets;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public int getNbrObjets() {
        return nbrObjets;
    }

    public void setNbrObjets(int nbrObjets) {
        this.nbrObjets = nbrObjets;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (packName != null ? packName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NbrObjParPack)) {
            return false;
        }
        NbrObjParPack other = (NbrObjParPack) object;
        if ((this.packName == null && other.packName != null) || (this.packName != null && !this.packName.equals(other.packName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entitiesMysql.NbrObjParPack[ packName=" + packName + " ]";
    }
}
