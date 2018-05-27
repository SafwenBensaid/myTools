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
 * @author 04494
 */
@Entity
@Table(name = "cadencement")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cadencement.findAll", query = "SELECT c FROM Cadencement c"),
    @NamedQuery(name = "Cadencement.findByType", query = "SELECT c FROM Cadencement c WHERE c.type = :type"),
    @NamedQuery(name = "Cadencement.findByOrdre", query = "SELECT c FROM Cadencement c WHERE c.ordre = :ordre"),
    @NamedQuery(name = "Cadencement.findByIteration", query = "SELECT c FROM Cadencement c WHERE c.iteration = :iteration")})
public class Cadencement implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "type")
    private String type;
    @Id
    @Basic(optional = false)
    @Column(name = "ordre")
    private Integer ordre;
    @Basic(optional = false)
    @Column(name = "iteration")
    private int iteration;

    public Cadencement() {
    }

    public Cadencement(Integer ordre) {
        this.ordre = ordre;
    }

    public Cadencement(Integer ordre, String type, int iteration) {
        this.ordre = ordre;
        this.type = type;
        this.iteration = iteration;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getOrdre() {
        return ordre;
    }

    public void setOrdre(Integer ordre) {
        this.ordre = ordre;
    }

    public int getIteration() {
        return iteration;
    }

    public void setIteration(int iteration) {
        this.iteration = iteration;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ordre != null ? ordre.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cadencement)) {
            return false;
        }
        Cadencement other = (Cadencement) object;
        if ((this.ordre == null && other.ordre != null) || (this.ordre != null && !this.ordre.equals(other.ordre))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entitiesMysql.Cadencement[ ordre=" + ordre + " ]";
    }
}
