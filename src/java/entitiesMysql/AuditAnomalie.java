/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entitiesMysql;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author 04494
 */
@Entity
@Table(name = "audit_anomalie")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AuditAnomalie.findAll", query = "SELECT a FROM AuditAnomalie a"),
    @NamedQuery(name = "AuditAnomalie.findAllAuthorised", query = "SELECT a FROM AuditAnomalie a WHERE a.auditAnomaliePK.etatAuth <> 0 ORDER BY a.dateAuth"),
    @NamedQuery(name = "AuditAnomalie.findByAuthoriser", query = "SELECT a FROM AuditAnomalie a WHERE a.authoriser = :authoriser"),
    @NamedQuery(name = "AuditAnomalie.findByDateAuth", query = "SELECT a FROM AuditAnomalie a WHERE a.dateAuth = :dateAuth"),
    @NamedQuery(name = "AuditAnomalie.findByEtatAuth", query = "SELECT a FROM AuditAnomalie a WHERE a.auditAnomaliePK.etatAuth = :etatAuth"),
    @NamedQuery(name = "AuditAnomalie.findByMotifRejet", query = "SELECT a FROM AuditAnomalie a WHERE a.motifRejet = :motifRejet")})
public class AuditAnomalie implements Serializable, Comparable<AuditAnomalie>, Cloneable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AuditAnomaliePK auditAnomaliePK;
    @Basic(optional = false)
    @Column(name = "authoriser")
    private String authoriser;
    @Basic(optional = false)
    @Column(name = "date_auth")
    private String dateAuth;
    @Basic(optional = false)
    @Column(name = "motif_rejet")
    private String motifRejet;

    public AuditAnomalie() {
    }

    public AuditAnomalie(AuditAnomaliePK auditAnomaliePK) {
        this.auditAnomaliePK = auditAnomaliePK;
    }

    public AuditAnomalie(AuditAnomaliePK auditAnomaliePK, String authoriser, String dateAuth, String motifRejet) {
        this.auditAnomaliePK = auditAnomaliePK;
        this.authoriser = authoriser;
        this.dateAuth = dateAuth;
        this.motifRejet = motifRejet;
    }

    public AuditAnomalie(int idAnomalie, int etatAuth) {
        this.auditAnomaliePK = new AuditAnomaliePK(idAnomalie, etatAuth);
    }

    public AuditAnomaliePK getAuditAnomaliePK() {
        return auditAnomaliePK;
    }

    public void setAuditAnomaliePK(AuditAnomaliePK auditAnomaliePK) {
        this.auditAnomaliePK = auditAnomaliePK;
    }

    public String getAuthoriser() {
        return authoriser;
    }

    public void setAuthoriser(String authoriser) {
        this.authoriser = authoriser;
    }

    public String getDateAuth() {
        return dateAuth;
    }

    public void setDateAuth(String dateAuth) {
        this.dateAuth = dateAuth;
    }

    public String getMotifRejet() {
        return motifRejet;
    }

    public void setMotifRejet(String motifRejet) {
        this.motifRejet = motifRejet;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (auditAnomaliePK != null ? auditAnomaliePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AuditAnomalie)) {
            return false;
        }
        AuditAnomalie other = (AuditAnomalie) object;
        if ((this.auditAnomaliePK == null && other.auditAnomaliePK != null) || (this.auditAnomaliePK != null && !this.auditAnomaliePK.equals(other.auditAnomaliePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entitiesMysql.AuditAnomalie[ auditAnomaliePK=" + auditAnomaliePK + " ]";
    }

    public Object clone() {
        Object o = null;
        try {
            // On récupère l'instance à renvoyer par l'appel de la 
            // méthode super.clone()
            o = super.clone();
        } catch (CloneNotSupportedException cnse) {
            // Ne devrait jamais arriver car nous implémentons 
            // l'interface Cloneable
            cnse.printStackTrace(System.err);
        }
        // on renvoie le clone
        return o;
    }

    @Override
    public int compareTo(AuditAnomalie o) {
        String dateString = ((AuditAnomalie) o).getDateAuth();
        int date1 = Integer.parseInt(dateString);
        int date2 = Integer.parseInt(this.getDateAuth());

        //ascending order
        return date1 - date2;

    }
}
