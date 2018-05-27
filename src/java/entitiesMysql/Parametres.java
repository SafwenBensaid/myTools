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
@Table(name = "parametres")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Parametres.findAll", query = "SELECT p FROM Parametres p"),
    @NamedQuery(name = "Parametres.findByCle", query = "SELECT p FROM Parametres p WHERE p.cle = :cle"),
    @NamedQuery(name = "Parametres.findByValeur", query = "SELECT p FROM Parametres p WHERE p.valeur = :valeur")})
public class Parametres implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "cle")
    private String cle;
    @Basic(optional = false)
    @Column(name = "valeur")
    private String valeur;

    public Parametres() {
    }

    public Parametres(String cle) {
        this.cle = cle;
    }

    public Parametres(String cle, String valeur) {
        this.cle = cle;
        this.valeur = valeur;
    }

    public String getCle() {
        return cle;
    }

    public void setCle(String cle) {
        this.cle = cle;
    }

    public String getValeur() {
        return valeur;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cle != null ? cle.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Parametres)) {
            return false;
        }
        Parametres other = (Parametres) object;
        if ((this.cle == null && other.cle != null) || (this.cle != null && !this.cle.equals(other.cle))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Parametres[ cle=" + cle + " ]";
    }
}
