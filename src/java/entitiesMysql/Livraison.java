/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entitiesMysql;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import tools.ManipulationObjectsTool;

/**
 *
 * @author 04486
 */
@Entity
@Table(name = "livraison")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Livraison.findAll", query = "SELECT l FROM Livraison l"),
    @NamedQuery(name = "Livraison.findByListNumeroLivraison", query = "SELECT l FROM Livraison l WHERE l.numeroLivraison in :numeroLivraisonsList"),
    @NamedQuery(name = "Livraison.findByNumeroLivraison", query = "SELECT l FROM Livraison l WHERE l.numeroLivraison = :numeroLivraison"),
    @NamedQuery(name = "Livraison.findByNumeroAnomalie", query = "SELECT l FROM Livraison l WHERE l.numeroAnomalie = :numeroAnomalie"),
    @NamedQuery(name = "Livraison.findByHarmCr", query = "SELECT l FROM Livraison l WHERE l.harmCr = :harmCr"),
    @NamedQuery(name = "Livraison.findByHarmCp", query = "SELECT l FROM Livraison l WHERE l.harmCp = :harmCp"),
    @NamedQuery(name = "Livraison.findByType", query = "SELECT l FROM Livraison l WHERE l.type = :type"),
    @NamedQuery(name = "Livraison.findByContenuLivrables", query = "SELECT l FROM Livraison l WHERE l.contenuLivrables = :contenuLivrables"),
    @NamedQuery(name = "Livraison.findByNomPack", query = "SELECT l FROM Livraison l WHERE l.nomPack = :nomPack"),
    @NamedQuery(name = "Livraison.findByOwner", query = "SELECT l FROM Livraison l WHERE l.owner = :owner"),
    @NamedQuery(name = "Livraison.findByReporter", query = "SELECT l FROM Livraison l WHERE l.reporter = :reporter"),
    @NamedQuery(name = "Livraison.findByCompanyDeploiement", query = "SELECT l FROM Livraison l WHERE l.companyDeploiement = :companyDeploiement"),
    @NamedQuery(name = "Livraison.findByNombreIterations", query = "SELECT l FROM Livraison l WHERE l.nombreIterations = :nombreIterations"),
    @NamedQuery(name = "Livraison.findByDateDeploiement", query = "SELECT l FROM Livraison l WHERE l.dateDeploiement = :dateDeploiement"),
    @NamedQuery(name = "Livraison.findByDateEnvoiProd", query = "SELECT l FROM Livraison l WHERE l.dateEnvoiProd = :dateEnvoiProd"),
    @NamedQuery(name = "Livraison.findByValide", query = "SELECT l FROM Livraison l WHERE l.valide = :valide"),
    @NamedQuery(name = "Livraison.findByCloturee", query = "SELECT l FROM Livraison l WHERE l.cloturee = :cloturee"),
    @NamedQuery(name = "Livraison.findByBloquerHarmonisation", query = "SELECT l FROM Livraison l WHERE l.bloquerHarmonisation = :bloquerHarmonisation")})
public class Livraison implements Serializable, Comparable<Livraison> {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "numero_livraison")
    private Integer numeroLivraison;
    @Basic(optional = false)
    @Column(name = "numero_anomalie")
    private Integer numeroAnomalie;
    @Column(name = "harm_cr")
    private Integer harmCr;
    @Column(name = "harm_cp")
    private Integer harmCp;
    @Column(name = "type")
    private String type;
    @Basic(optional = false)
    @Column(name = "contenu_livrables")
    private String contenuLivrables;
    @Column(name = "nom_pack")
    private String nomPack;
    @Column(name = "owner")
    private String owner;
    @Column(name = "reporter")
    private String reporter;
    @Column(name = "company_deploiement")
    private String companyDeploiement;
    @Basic(optional = false)
    @Column(name = "nombre_iterations")
    private int nombreIterations;
    @Basic(optional = false)
    @Lob
    @Column(name = "message_trac")
    private String messageTrac;
    @Lob
    @Column(name = "liste_objets")
    private String listeObjets;
    @Lob
    @Column(name = "resultat_etude_intersection_CR")
    private String resultatetudeintersectionCR;
    @Lob
    @Column(name = "resultat_etude_intersection_CP")
    private String resultatetudeintersectionCP;
    @Lob
    @Column(name = "resultat_etude_intersection_CU")
    private String resultatetudeintersectionCU;
    
    @Column(name = "date_deploiement")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateDeploiement;
    @Column(name = "date_envoi_prod")
    @Temporal(TemporalType.DATE)
    private Date dateEnvoiProd;
    @Basic(optional = false)
    @Column(name = "valide")
    private boolean valide;
    @Basic(optional = false)
    @Column(name = "harm1probleme")
    private boolean harm1probleme;
    @Basic(optional = false)
    @Column(name = "cloturee")
    private boolean cloturee;
    @Basic(optional = false)
    @Column(name = "bloquerHarmonisation")
    private boolean bloquerHarmonisation;
    @Lob
    @Column(name = "livrables")
    private byte[] livrables;

    public Livraison() {
    }

    public Livraison(Integer numeroLivraison) {
        this.numeroLivraison = numeroLivraison;
    }

    public Livraison(Integer numeroLivraison, Integer numeroAnomalie, String contenuLivrables, int nombreIterations, String messageTrac, boolean valide, boolean cloturee, boolean bloquerHarmonisation) {
        this.numeroLivraison = numeroLivraison;
        this.numeroAnomalie = numeroAnomalie;
        this.contenuLivrables = contenuLivrables;
        this.nombreIterations = nombreIterations;
        this.messageTrac = messageTrac;
        this.valide = valide;
        this.cloturee = cloturee;
        this.bloquerHarmonisation = bloquerHarmonisation;
    }

    public Integer getNumeroLivraison() {
        return numeroLivraison;
    }

    public void setNumeroLivraison(Integer numeroLivraison) {
        this.numeroLivraison = numeroLivraison;
    }

    public Integer getNumeroAnomalie() {
        return numeroAnomalie;
    }

    public void setNumeroAnomalie(Integer numeroAnomalie) {
        this.numeroAnomalie = numeroAnomalie;
    }

    public Integer getHarmCr() {
        return harmCr;
    }

    public void setHarmCr(Integer harmCr) {
        this.harmCr = harmCr;
    }

    public Integer getHarmCp() {
        return harmCp;
    }

    public void setHarmCp(Integer harmCp) {
        this.harmCp = harmCp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContenuLivrables() {
        return contenuLivrables;
    }

    public void setContenuLivrables(String contenuLivrables) {
        this.contenuLivrables = contenuLivrables;
    }

    public String getNomPack() {
        return nomPack;
    }

    public void setNomPack(String nomPack) {
        this.nomPack = nomPack;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public String getCompanyDeploiement() {
        return companyDeploiement;
    }

    public void setCompanyDeploiement(String companyDeploiement) {
        this.companyDeploiement = companyDeploiement;
    }

    public int getNombreIterations() {
        return nombreIterations;
    }

    public void setNombreIterations(int nombreIterations) {
        this.nombreIterations = nombreIterations;
    }

    public String getMessageTrac() {
        return messageTrac;
    }

    public void setMessageTrac(String messageTrac) {
        this.messageTrac = messageTrac;
    }

    public String getListeObjets() {
        return listeObjets;
    }

    public void setListeObjets(String listeObjets) {
        this.listeObjets = listeObjets;
    }

    public String getResultatetudeintersectionCR() {
        if (resultatetudeintersectionCR != null) {
            resultatetudeintersectionCR = resultatetudeintersectionCR.trim();
        }
        return resultatetudeintersectionCR;
    }

    public void setResultatetudeintersectionCR(String resultatetudeintersectionCR) {
        this.resultatetudeintersectionCR = resultatetudeintersectionCR;
    }

    public String getResultatetudeintersectionCP() {
        if (resultatetudeintersectionCP != null) {
            resultatetudeintersectionCP = resultatetudeintersectionCP.trim();
        }
        return resultatetudeintersectionCP;
    }

    public void setResultatetudeintersectionCP(String resultatetudeintersectionCP) {
        this.resultatetudeintersectionCP = resultatetudeintersectionCP;
    }

    public String getResultatetudeintersectionCU() {
        return resultatetudeintersectionCU;
    }

    public void setResultatetudeintersectionCU(String resultatetudeintersectionCU) {
        this.resultatetudeintersectionCU = resultatetudeintersectionCU;
    }
    
    public Date getDateDeploiement() {
        return dateDeploiement;
    }

    public void setDateDeploiement(Date dateDeploiement) {
        this.dateDeploiement = dateDeploiement;
    }

    public Date getDateEnvoiProd() {
        return dateEnvoiProd;
    }

    public void setDateEnvoiProd(Date dateEnvoiProd) {
        this.dateEnvoiProd = dateEnvoiProd;
    }

    public boolean getValide() {
        return valide;
    }

    public void setValide(boolean valide) {
        this.valide = valide;
    }

    public boolean isHarm1probleme() {
        return harm1probleme;
    }

    public void setHarm1probleme(boolean harm1probleme) {
        this.harm1probleme = harm1probleme;
    }

    public boolean getCloturee() {
        return cloturee;
    }

    public void setCloturee(boolean cloturee) {
        this.cloturee = cloturee;
    }

    public boolean getBloquerHarmonisation() {
        return bloquerHarmonisation;
    }

    public void setBloquerHarmonisation(boolean bloquerHarmonisation) {
        this.bloquerHarmonisation = bloquerHarmonisation;
    }

    public List<Object> getLivrables() {
        if (livrables == null) {
            return null;
        } else {
            ManipulationObjectsTool manTools = new ManipulationObjectsTool();
            Object res = manTools.convertByteToObject(livrables, numeroLivraison);
            List<Object> listGenericLivraisonDTO = null;
            try {
                listGenericLivraisonDTO = (List<Object>) res;
            } catch (Exception exep) {
                exep.printStackTrace();
                tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
            }
            return listGenericLivraisonDTO;
        }
    }

    public void setLivrables(byte[] livrables) {
        this.livrables = livrables;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (numeroLivraison != null ? numeroLivraison.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Livraison)) {
            return false;
        }
        Livraison other = (Livraison) object;
        if ((this.numeroLivraison == null && other.numeroLivraison != null) || (this.numeroLivraison != null && !this.numeroLivraison.equals(other.numeroLivraison))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entitiesMysql.Livraison[ numeroLivraison=" + numeroLivraison + " ]";
    }

    @Override
    public int compareTo(Livraison o) {
        int numLivraison1 = ((Livraison) o).getNumeroLivraison();
        int numLivraison2 = this.getNumeroLivraison();
        //ascending order
        return numLivraison1 - numLivraison2;
    }
}
