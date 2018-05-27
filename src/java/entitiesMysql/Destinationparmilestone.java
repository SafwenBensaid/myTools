/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entitiesMysql;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author 04494
 */
@Entity
@Table(name = "destinationparmilestone")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Destinationparmilestone.findAll", query = "SELECT d FROM Destinationparmilestone d"),
    @NamedQuery(name = "Destinationparmilestone.findByIdmilestone", query = "SELECT d FROM Destinationparmilestone d WHERE d.idmilestone = :idmilestone"),
    @NamedQuery(name = "Destinationparmilestone.findByMilestoneName", query = "SELECT d FROM Destinationparmilestone d WHERE d.milestoneName = :milestoneName"),
    @NamedQuery(name = "Destinationparmilestone.findByMilestoneEmail", query = "SELECT d FROM Destinationparmilestone d WHERE d.milestoneEmail = :milestoneEmail")})
public class Destinationparmilestone implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idmilestone")
    private Integer idmilestone;
    @Basic(optional = false)
    @Column(name = "milestone_name")
    private String milestoneName;
    @Basic(optional = false)
    @Column(name = "milestone_email")
    private String milestoneEmail;

    public Destinationparmilestone() {
    }

    public Destinationparmilestone(Integer idmilestone) {
        this.idmilestone = idmilestone;
    }

    public Destinationparmilestone(Integer idmilestone, String milestoneName, String milestoneEmail) {
        this.idmilestone = idmilestone;
        this.milestoneName = milestoneName;
        this.milestoneEmail = milestoneEmail;
    }

    public Integer getIdmilestone() {
        return idmilestone;
    }

    public void setIdmilestone(Integer idmilestone) {
        this.idmilestone = idmilestone;
    }

    public String getMilestoneName() {
        return milestoneName;
    }

    public void setMilestoneName(String milestoneName) {
        this.milestoneName = milestoneName;
    }

    public String getMilestoneEmail() {
        return milestoneEmail;
    }

    public void setMilestoneEmail(String milestoneEmail) {
        this.milestoneEmail = milestoneEmail;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idmilestone != null ? idmilestone.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Destinationparmilestone)) {
            return false;
        }
        Destinationparmilestone other = (Destinationparmilestone) object;
        if ((this.idmilestone == null && other.idmilestone != null) || (this.idmilestone != null && !this.idmilestone.equals(other.idmilestone))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entitiesMysql.Destinationparmilestone[ idmilestone=" + idmilestone + " ]";
    }
}
