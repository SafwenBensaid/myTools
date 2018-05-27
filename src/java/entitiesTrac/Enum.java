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
@Table(name = "enum")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Enum.findAll", query = "SELECT e FROM Enum e")})
public class Enum implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EnumPK enumPK;
    @Lob
    @Column(name = "value")
    private String value;

    public Enum() {
    }

    public Enum(EnumPK enumPK) {
        this.enumPK = enumPK;
    }

    public Enum(String type, String name) {
        this.enumPK = new EnumPK(type, name);
    }

    public EnumPK getEnumPK() {
        return enumPK;
    }

    public void setEnumPK(EnumPK enumPK) {
        this.enumPK = enumPK;
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
        hash += (enumPK != null ? enumPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Enum)) {
            return false;
        }
        Enum other = (Enum) object;
        if ((this.enumPK == null && other.enumPK != null) || (this.enumPK != null && !this.enumPK.equals(other.enumPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Enum[ enumPK=" + enumPK + " ]";
    }
}
