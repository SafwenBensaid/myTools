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
@Table(name = "niveauprojet")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Niveauprojet.findAll", query = "SELECT n FROM Niveauprojet n ORDER BY n.actif, n.abreviation"),
    @NamedQuery(name = "Niveauprojet.findAllNiveauProjets", query = "SELECT n.nom FROM Niveauprojet n ORDER BY  n.abreviation"),
    @NamedQuery(name = "Niveauprojet.findAllAbreviationsByActifs", query = "SELECT n.abreviation FROM Niveauprojet n WHERE n.actif = :actif ORDER BY  n.abreviation"),
    @NamedQuery(name = "Niveauprojet.findByNom", query = "SELECT n FROM Niveauprojet n WHERE n.nom = :nom"),
    @NamedQuery(name = "Niveauprojet.findAbreviationByNom", query = "SELECT n.abreviation FROM Niveauprojet n WHERE n.nom = :nom"),
    @NamedQuery(name = "Niveauprojet.findByAbreviation", query = "SELECT n FROM Niveauprojet n WHERE n.abreviation = :abreviation"),
    @NamedQuery(name = "Niveauprojet.findNameByAbreviation", query = "SELECT n.nom FROM Niveauprojet n WHERE n.abreviation = :abreviation"),
    @NamedQuery(name = "Niveauprojet.findByActif", query = "SELECT n FROM Niveauprojet n WHERE n.actif = :actif"),
    @NamedQuery(name = "Niveauprojet.findByReleaseprevue", query = "SELECT n FROM Niveauprojet n WHERE n.releaseprevue = :releaseprevue"),
    @NamedQuery(name = "Niveauprojet.findByMetier", query = "SELECT n FROM Niveauprojet n WHERE n.metier = :metier"),
    @NamedQuery(name = "Niveauprojet.findByTypeprojet", query = "SELECT n FROM Niveauprojet n WHERE n.typeprojet = :typeprojet"),
    @NamedQuery(name = "Niveauprojet.findBySitedeveloppement", query = "SELECT n FROM Niveauprojet n WHERE n.sitedeveloppement = :sitedeveloppement")})
public class Niveauprojet implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "nom")
    private String nom;
    @Basic(optional = false)
    @Column(name = "abreviation")
    private String abreviation;
    @Basic(optional = false)
    @Column(name = "actif")
    private String actif;
    @Column(name = "releaseprevue")
    private String releaseprevue;
    @Column(name = "metier")
    private String metier;
    @Column(name = "typeprojet")
    private String typeprojet;
    @Column(name = "sitedeveloppement")
    private String sitedeveloppement;

    public Niveauprojet() {
    }

    public Niveauprojet(String nom) {
        this.nom = nom;
    }

    public Niveauprojet(String nom, String abreviation, String actif) {
        this.nom = nom;
        this.abreviation = abreviation;
        this.actif = actif;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAbreviation() {
        return abreviation;
    }

    public void setAbreviation(String abreviation) {
        this.abreviation = abreviation;
    }

    public String getActif() {
        return actif;
    }

    public void setActif(String actif) {
        this.actif = actif;
    }

    public String getReleaseprevue() {
        return releaseprevue;
    }

    public void setReleaseprevue(String releaseprevue) {
        this.releaseprevue = releaseprevue;
    }

    public String getMetier() {
        return metier;
    }

    public void setMetier(String metier) {
        this.metier = metier;
    }

    public String getTypeprojet() {
        return typeprojet;
    }

    public void setTypeprojet(String typeprojet) {
        this.typeprojet = typeprojet;
    }

    public String getSitedeveloppement() {
        return sitedeveloppement;
    }

    public void setSitedeveloppement(String sitedeveloppement) {
        this.sitedeveloppement = sitedeveloppement;
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
        if (!(object instanceof Niveauprojet)) {
            return false;
        }
        Niveauprojet other = (Niveauprojet) object;
        if ((this.nom == null && other.nom != null) || (this.nom != null && !this.nom.equals(other.nom))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entitiesMysql.Niveauprojet[ nom=" + nom + " ]";
    }
}
