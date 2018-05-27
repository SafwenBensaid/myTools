/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entitiesTrac;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "session_attribute")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SessionAttribute.findAll", query = "SELECT s FROM SessionAttribute s"),
    @NamedQuery(name = "SessionAttribute.findByAuthenticated", query = "SELECT s FROM SessionAttribute s WHERE s.sessionAttributePK.authenticated = :authenticated")})
public class SessionAttribute implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SessionAttributePK sessionAttributePK;
    @Lob
    @Column(name = "value")
    private String value;

    public SessionAttribute() {
    }

    public SessionAttribute(SessionAttributePK sessionAttributePK) {
        this.sessionAttributePK = sessionAttributePK;
    }

    public SessionAttribute(String sid, int authenticated, String name) {
        this.sessionAttributePK = new SessionAttributePK(sid, authenticated, name);
    }

    public SessionAttributePK getSessionAttributePK() {
        return sessionAttributePK;
    }

    public void setSessionAttributePK(SessionAttributePK sessionAttributePK) {
        this.sessionAttributePK = sessionAttributePK;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sessionAttributePK != null ? sessionAttributePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SessionAttribute)) {
            return false;
        }
        SessionAttribute other = (SessionAttribute) object;
        if ((this.sessionAttributePK == null && other.sessionAttributePK != null) || (this.sessionAttributePK != null && !this.sessionAttributePK.equals(other.sessionAttributePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.SessionAttribute[ sessionAttributePK=" + sessionAttributePK + " ]";
    }
}
