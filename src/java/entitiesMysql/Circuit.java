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
@Table(name = "circuit")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Circuit.findAll", query = "SELECT c FROM Circuit c"),
    @NamedQuery(name = "Circuit.findByNom", query = "SELECT c FROM Circuit c WHERE c.nom = :nom"),
    @NamedQuery(name = "Circuit.findByActif", query = "SELECT c FROM Circuit c WHERE c.actif = :actif")})
public class Circuit implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "nom")
    private String nom;
    @Basic(optional = false)
    @Column(name = "actif")
    private String actif;

    public Circuit() {
    }

    public Circuit(String nom) {
        this.nom = nom;
    }

    public Circuit(String nom, String actif) {
        this.nom = nom;
        this.actif = actif;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getActif() {
        return actif;
    }

    public void setActif(String actif) {
        this.actif = actif;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nom != null ? nom.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Circuit)) {
            return false;
        }
        Circuit other = (Circuit) object;
        if ((this.nom == null && other.nom != null) || (this.nom != null && !this.nom.equals(other.nom))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Circuit{" + "nom=" + nom + ", actif=" + actif + '}';
    }
}
