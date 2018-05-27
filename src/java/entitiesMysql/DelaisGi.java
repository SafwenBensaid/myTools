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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author 04494
 */
@Entity
@Table(name = "delais_gi")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DelaisGi.findAll", query = "SELECT d FROM DelaisGi d"),
    @NamedQuery(name = "DelaisGi.findByPriorite", query = "SELECT d FROM DelaisGi d WHERE d.delaisGiPK.priorite = :priorite"),
    @NamedQuery(name = "DelaisGi.findByType", query = "SELECT d FROM DelaisGi d WHERE d.delaisGiPK.type = :type"),
    @NamedQuery(name = "DelaisGi.findByDelais", query = "SELECT d FROM DelaisGi d WHERE d.delais = :delais")})
public class DelaisGi implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DelaisGiPK delaisGiPK;
    @Basic(optional = false)
    @Column(name = "delais")
    private String delais;

    public DelaisGi() {
    }

    public DelaisGi(DelaisGiPK delaisGiPK) {
        this.delaisGiPK = delaisGiPK;
    }

    public DelaisGi(DelaisGiPK delaisGiPK, String delais) {
        this.delaisGiPK = delaisGiPK;
        this.delais = delais;
    }

    public DelaisGi(String priorite, String type) {
        this.delaisGiPK = new DelaisGiPK(priorite, type);
    }

    public DelaisGiPK getDelaisGiPK() {
        return delaisGiPK;
    }

    public void setDelaisGiPK(DelaisGiPK delaisGiPK) {
        this.delaisGiPK = delaisGiPK;
    }

    public String getDelais() {
        return delais;
    }

    public void setDelais(String delais) {
        this.delais = delais;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (delaisGiPK != null ? delaisGiPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DelaisGi)) {
            return false;
        }
        DelaisGi other = (DelaisGi) object;
        if ((this.delaisGiPK == null && other.delaisGiPK != null) || (this.delaisGiPK != null && !this.delaisGiPK.equals(other.delaisGiPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entitiesMysql.DelaisGi[ delaisGiPK=" + delaisGiPK + " ]";
    }
}
