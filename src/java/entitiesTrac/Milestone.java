/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entitiesTrac;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author 04486
 */
@Entity
@Table(name = "milestone")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Milestone.findAll", query = "SELECT m FROM Milestone m"),
    @NamedQuery(name = "Milestone.findAllMilestoneNames", query = "SELECT m.name FROM Milestone m"),
    @NamedQuery(name = "Milestone.findByDue", query = "SELECT m FROM Milestone m WHERE m.due = :due"),
    @NamedQuery(name = "Milestone.findByCompleted", query = "SELECT m FROM Milestone m WHERE m.completed = :completed")})
public class Milestone implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Lob
    @Column(name = "name")
    private String name;
    @Column(name = "due")
    private BigInteger due;
    @Column(name = "completed")
    private BigInteger completed;
    @Lob
    @Column(name = "description")
    private String description;

    public Milestone() {
    }

    public Milestone(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigInteger getDue() {
        return due;
    }

    public void setDue(BigInteger due) {
        this.due = due;
    }

    public BigInteger getCompleted() {
        return completed;
    }

    public void setCompleted(BigInteger completed) {
        this.completed = completed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        if (!(object instanceof Milestone)) {
            return false;
        }
        Milestone other = (Milestone) object;
        if ((this.name == null && other.name != null) || (this.name != null && !this.name.equals(other.name))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Milestone[ name=" + name + " ]";
    }
}
