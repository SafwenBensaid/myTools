/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entitiesMysql;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author 04486
 */
@Entity
@Table(name = "audit")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Audit.findAll", query = "SELECT a FROM Audit a"),
    @NamedQuery(name = "Audit.findById", query = "SELECT a FROM Audit a WHERE a.id = :id"),
    @NamedQuery(name = "Audit.findByUpdateTime", query = "SELECT a FROM Audit a WHERE a.updateTime = :updateTime"),
    //@NamedQuery(name = "Audit.inserIntoAudit", query = "INSERT INTO Audit (updateTime,action) VALUES (NOW(),'OPERATION_ON_DB')"),
    @NamedQuery(name = "Audit.findActionByMaxUpdateTime", query = "SELECT t.action FROM Audit t WHERE t.id = (SELECT max(t1.id) from Audit t1)"),
    @NamedQuery(name = "Audit.findByAction", query = "SELECT a FROM Audit a WHERE a.action = :action")})
public class Audit implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "updateTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;
    @Basic(optional = false)
    @Column(name = "action")
    private String action;

    public Audit() {
    }

    public Audit(Integer id) {
        this.id = id;
    }

    public Audit(Integer id, Date updateTime, String action) {
        this.id = id;
        this.updateTime = updateTime;
        this.action = action;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Audit)) {
            return false;
        }
        Audit other = (Audit) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entitiesMysql.Audit[ id=" + id + " ]";
    }
}
