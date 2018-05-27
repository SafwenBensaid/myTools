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
@Table(name = "etudeintersection")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Etudeintersection.findAll", query = "SELECT e FROM Etudeintersection e"),
    @NamedQuery(name = "Etudeintersection.findById", query = "SELECT e FROM Etudeintersection e WHERE e.id = :id"),
    @NamedQuery(name = "Etudeintersection.findByTypeObjet", query = "SELECT e FROM Etudeintersection e WHERE e.typeObjet = :typeObjet"),
    @NamedQuery(name = "Etudeintersection.findByNomObjet", query = "SELECT e FROM Etudeintersection e WHERE e.nomObjet = :nomObjet"),
    @NamedQuery(name = "Etudeintersection.findByCircuit", query = "SELECT e FROM Etudeintersection e WHERE e.circuit = :circuit"),
    @NamedQuery(name = "Etudeintersection.findByNomPack", query = "SELECT e FROM Etudeintersection e WHERE e.nomPack = :nomPack"),
    @NamedQuery(name = "Etudeintersection.findByRevisionIntersection", query = "SELECT e FROM Etudeintersection e WHERE e.revisionIntersection = :revisionIntersection"),
    @NamedQuery(name = "Etudeintersection.findByLivraisonIntersection", query = "SELECT e FROM Etudeintersection e WHERE e.livraisonIntersection = :livraisonIntersection"),
    @NamedQuery(name = "Etudeintersection.findByAnomalieIntersection", query = "SELECT e FROM Etudeintersection e WHERE e.anomalieIntersection = :anomalieIntersection"),
    @NamedQuery(name = "Etudeintersection.findByAnomalieIntersectionPriority", query = "SELECT e FROM Etudeintersection e WHERE e.anomalieIntersectionPriority = :anomalieIntersectionPriority"),
    @NamedQuery(name = "Etudeintersection.findByLivraisonHotfix", query = "SELECT e FROM Etudeintersection e WHERE e.livraisonHotfix = :livraisonHotfix"),
    @NamedQuery(name = "Etudeintersection.findByNomBrancheIndividuelle", query = "SELECT e FROM Etudeintersection e WHERE e.nomBrancheIndividuelle = :nomBrancheIndividuelle"),
    @NamedQuery(name = "Etudeintersection.findByUser", query = "SELECT e FROM Etudeintersection e WHERE e.user = :user"),
    @NamedQuery(name = "Etudeintersection.findByDateEtude", query = "SELECT e FROM Etudeintersection e WHERE e.dateEtude = :dateEtude")})
public class Etudeintersection implements Serializable, Comparable<Etudeintersection> {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "typeObjet")
    private String typeObjet;
    @Basic(optional = false)
    @Column(name = "nomObjet")
    private String nomObjet;
    @Basic(optional = false)
    @Column(name = "circuit")
    private String circuit;
    @Column(name = "nomPack")
    private String nomPack;
    @Basic(optional = false)
    @Column(name = "revisionIntersection")
    private String revisionIntersection;
    @Basic(optional = false)
    @Column(name = "livraisonIntersection")
    private String livraisonIntersection;
    @Column(name = "anomalieIntersection")
    private String anomalieIntersection;
    @Column(name = "anomalieIntersectionPriority")
    private String anomalieIntersectionPriority;
    @Column(name = "livraisonHotfix")
    private String livraisonHotfix;
    @Column(name = "nomBrancheIndividuelle")
    private String nomBrancheIndividuelle;
    @Column(name = "user")
    private String user;
    @Column(name = "dateEtude")
    @Temporal(TemporalType.DATE)
    private Date dateEtude;

    public Etudeintersection() {
    }

    public Etudeintersection(Integer id) {
        this.id = id;
    }

    public Etudeintersection(String typeObjet, String nomObjet, String circuit, String nomPack, String revisionIntersection, String livraisonIntersection, String anomalieIntersection, String anomalieIntersectionPriority, String livraisonHotfix, String nomBrancheIndividuelle, String user, Date dateEtude) {
        this.typeObjet = typeObjet;
        this.nomObjet = nomObjet;
        this.circuit = circuit;
        this.nomPack = nomPack;
        this.revisionIntersection = revisionIntersection;
        this.livraisonIntersection = livraisonIntersection;
        this.anomalieIntersection = anomalieIntersection;
        this.anomalieIntersectionPriority = anomalieIntersectionPriority;
        this.livraisonHotfix = livraisonHotfix;
        this.nomBrancheIndividuelle = nomBrancheIndividuelle;
        this.user = user;
        this.dateEtude = dateEtude;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTypeObjet() {
        return typeObjet;
    }

    public void setTypeObjet(String typeObjet) {
        this.typeObjet = typeObjet;
    }

    public String getNomObjet() {
        return nomObjet;
    }

    public void setNomObjet(String nomObjet) {
        this.nomObjet = nomObjet;
    }

    public String getCircuit() {
        return circuit;
    }

    public void setCircuit(String circuit) {
        this.circuit = circuit;
    }

    public String getNomPack() {
        return nomPack;
    }

    public void setNomPack(String nomPack) {
        this.nomPack = nomPack;
    }

    public String getRevisionIntersection() {
        return revisionIntersection;
    }

    public void setRevisionIntersection(String revisionIntersection) {
        this.revisionIntersection = revisionIntersection;
    }

    public String getLivraisonIntersection() {
        return livraisonIntersection;
    }

    public void setLivraisonIntersection(String livraisonIntersection) {
        this.livraisonIntersection = livraisonIntersection;
    }

    public String getAnomalieIntersection() {
        return anomalieIntersection;
    }

    public void setAnomalieIntersection(String anomalieIntersection) {
        this.anomalieIntersection = anomalieIntersection;
    }

    public String getAnomalieIntersectionPriority() {
        return anomalieIntersectionPriority;
    }

    public void setAnomalieIntersectionPriority(String anomalieIntersectionPriority) {
        this.anomalieIntersectionPriority = anomalieIntersectionPriority;
    }

    public String getLivraisonHotfix() {
        return livraisonHotfix;
    }

    public void setLivraisonHotfix(String livraisonHotfix) {
        this.livraisonHotfix = livraisonHotfix;
    }

    public String getNomBrancheIndividuelle() {
        return nomBrancheIndividuelle;
    }

    public void setNomBrancheIndividuelle(String nomBrancheIndividuelle) {
        this.nomBrancheIndividuelle = nomBrancheIndividuelle;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Date getDateEtude() {
        return dateEtude;
    }

    public void setDateEtude(Date dateEtude) {
        this.dateEtude = dateEtude;
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
        if (!(object instanceof Etudeintersection)) {
            return false;
        }
        Etudeintersection other = (Etudeintersection) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entitiesMysql.Etudeintersection[ id=" + id + " ]";
    }

    @Override
    public int compareTo(Etudeintersection o) {
        String revisionString1 = ((Etudeintersection) o).getTypeObjet() + ">" + ((Etudeintersection) o).getNomObjet();
        String revisionString2 = this.getTypeObjet() + ">" + this.getNomObjet();
        return revisionString2.compareTo(revisionString1);

    }
}
