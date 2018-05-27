/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entitiesTrac;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author 04486
 */
@IdClass(TicketCustomPK.class)
@Entity
@Table(name = "ticket_custom")
@XmlRootElement
@NamedQueries({
    /*ANOMALIE*/
    @NamedQuery(name = "Ticket.findAllPipeTicketsCDDByOwner", query = "SELECT DISTINCT(t.ticketPointer) FROM TicketCustom t WHERE ((t.ticketPointer.priority IN ('LIVRAISON CONFIRMEE', 'PROBLEME DE PACKAGING') AND t.ticketPointer.type IN ('QUALIFICATION','CERTIFICATION','QUALIFICATION_PROJET','HOT FIXE TEST', 'QUALIFICATION_UPGRADE', 'CERTIFICATION_UPGRADE', 'ACTION A CHAUD TEST') ) OR (t.ticketPointer.priority IN ('LIVRAISON CONFIRMEE') AND t.ticketPointer.type IN ('HOT FIXE PROD') AND t.value = 'HARMONISATION_C.UPGRADE' )) AND t.ticketPointer.status!='closed' AND t.ticketPointer.owner like :owner AND t.name='nature_liv' ORDER BY t.ticketPointer.owner, t.ticketPointer.changetime"),
    @NamedQuery(name = "TicketCustom.findAllTicketAnomalieHotfixWithoutLivraison", query = "SELECT DISTINCT(t.ticket) FROM TicketCustom t WHERE t.ticketPointer.status IN ('new', 'assigned', 'reopened', 'accepted') AND t.ticketPointer.owner <> 'release.comite' AND t.ticketPointer.priority <> 'INFORMATION REQUISE' AND t.ticketPointer.version IN ('HOT FIXE POUR MISE EN PROD','ACTION A CHAUD POUR MISE EN PROD') AND (t.name='t_liv' AND t.value='')"),
    @NamedQuery(name = "TicketCustom.findAllTicketAnomalieMaintenanceWithoutLivraison", query = "SELECT DISTINCT(t.ticket) FROM TicketCustom t WHERE t.ticketPointer.status IN ('new', 'assigned', 'reopened', 'accepted') AND t.ticketPointer.version IN ('RELEASE A QUALIFIER','RELEASE A CERTIFIER')                AND (t.name='t_liv' AND t.value='')"),
    @NamedQuery(name = "TicketCustom.findAllCobs", query = "SELECT t.ticket FROM TicketCustom t WHERE t.ticketPointer.type='COB' AND t.name ='action' AND t.ticketPointer.status !='closed'"),
    @NamedQuery(name = "TicketCustom.findAllClosed", query = "SELECT t.ticket FROM TicketCustom t WHERE t.ticketPointer.status ='closed'"),
    @NamedQuery(name = "TicketCustom.findAllCircuitProjet", query = "SELECT t FROM TicketCustom t WHERE t.name ='t_liv_certif' AND t.ticketPointer.status !='closed' AND t.ticketPointer.owner <> 'release.comite' AND t.ticketPointer.version ='PROJET A QUALIFIER' "),
    @NamedQuery(name = "TicketCustom.findByRelease", query = "SELECT t FROM TicketCustom t WHERE t.name ='t_liv_certif' AND t.ticketPointer.status !='closed' AND t.ticketPointer.owner <> 'release.comite' AND (t.ticketPointer.version ='RELEASE A CERTIFIER' OR  t.ticketPointer.version ='RELEASE A QUALIFIER')"),
    /*Fin ANOMALIE*/
    /*LIVRAISON*/
    @NamedQuery(name = "TicketCustom.findAllHotfixAHarmoniserProdCocheOV", query = "SELECT t.ticketPointer FROM TicketCustom t WHERE t.ticketPointer.priority='DEPLOYEE' AND t.ticketPointer.status!='closed' AND t.ticketPointer.type IN ('ACTION A CHAUD PROD','HOT FIXE PROD') AND t.name ='biatprod' AND t.value='1' ORDER BY t.ticketPointer.changetime"),
    @NamedQuery(name = "TicketCustom.findAllHotfixAHarmoniserRefNonCocheOV", query = "SELECT t.ticketPointer FROM TicketCustom t WHERE t.ticketPointer.priority='DEPLOYEE' AND t.ticketPointer.status!='closed' AND t.ticketPointer.type IN ('ACTION A CHAUD PROD','HOT FIXE PROD') AND t.name ='biatref' AND t.value='0' ORDER BY t.ticketPointer.changetime"),
    @NamedQuery(name = "TicketCustom.findAllHotfixAHarmoniserUpgrade0", query = "SELECT t FROM TicketCustom t WHERE t.ticketPointer.priority='DEPLOYEE' AND t.ticketPointer.type IN ('ACTION A CHAUD PROD','HOT FIXE PROD') AND t.name ='harm_upgrade' AND t.value='0' ORDER BY t.ticketPointer.changetime"),
    @NamedQuery(name = "TicketCustom.findAllHotfixTickets", query = "SELECT t FROM TicketCustom t WHERE "
            + "((((t.ticketPointer.type='HOT FIXE TEST' OR t.ticketPointer.type='ACTION A CHAUD TEST') and t.ticketPointer.priority='DEPLOYEE'              and t.ticketPointer.status='closed')OR "
            + "((t.ticketPointer.type='HOT FIXE PROD'   OR t.ticketPointer.type='ACTION A CHAUD PROD') and t.ticketPointer.priority='OBJET LIVREE'          and t.ticketPointer.status='reopened')OR "
            + "((t.ticketPointer.type='HOT FIXE PROD'   OR t.ticketPointer.type='ACTION A CHAUD PROD') and t.ticketPointer.priority='PRET POUR DEPLOIEMENT' and t.ticketPointer.status='new')OR "
            + "((t.ticketPointer.type='HOT FIXE PROD'   OR t.ticketPointer.type='ACTION A CHAUD PROD') and t.ticketPointer.priority='DEPLOYEE'              and t.ticketPointer.status IN ('new','assigned'))) "
            + "and t.name in ('contenu_des_livrables','ticket_origine','nature_liv')) order by t.ticketPointer.changetime"),
    /*Fin LIVRAISON*/
    /*GESTION DES DEMANDES*/
    @NamedQuery(name = "TicketCustom.findAllCustomFields", query = "SELECT DISTINCT(t.name) FROM TicketCustom t WHERE t.ticketPointer.status IN ('NOUVEAU_BESOIN', 'VALIDE_METIER', 'VALIDE_IMPACT', 'VALIDE_MOA', 'VALIDE_MOE', 'VALIDE_CS', 'VALIDE_PMO', 'NON_VALIDE')"),    
    @NamedQuery(name = "TicketCustom.findAllDemandesMetier", query = "SELECT DISTINCT(t.ticket) FROM TicketCustom t WHERE t.ticketPointer.status IN ('NOUVEAU_BESOIN', 'VALIDE_METIER', 'VALIDE_IMPACT', 'VALIDE_MOA', 'VALIDE_MOE', 'VALIDE_CS', 'VALIDE_PMO', 'NON_VALIDE')"),

    /*Fin GESTION DES DEMANDES*/
    @NamedQuery(name = "TicketCustom.findAll", query = "SELECT t FROM TicketCustom t"),
    @NamedQuery(name = "TicketCustom.findAllTicketEnCours", query = "SELECT t FROM TicketCustom t WHERE t.ticketPointer.status <>'closed'"),
    @NamedQuery(name = "TicketCustom.findAllTicketsCustomOfDEVT24Liv", query = "SELECT t FROM TicketCustom t WHERE t.ticketPointer.status != 'closed' AND t.ticketPointer.priority = 'LIVRAISON CONFIRMEE'"),
    @NamedQuery(name = "TicketCustom.findAllTicketsCustomOfOVT24Liv", query = "SELECT t FROM TicketCustom t WHERE t.ticketPointer.status != 'closed' AND t.ticketPointer.priority = 'OBJET LIVREE'"),
    @NamedQuery(name = "TicketCustom.findAllTicketsCustomOfIET24Liv", query = "SELECT t FROM TicketCustom t WHERE t.ticketPointer.status != 'closed' AND t.ticketPointer.priority = 'PRET POUR DEPLOIEMENT'"),
    @NamedQuery(name = "TicketCustom.findAllTicketsCustomOfTestT24Ano", query = "SELECT t FROM TicketCustom t WHERE t.ticketPointer.status != 'closed' AND t.ticketPointer.priority IN ('A QUALIFIER', 'A CERTIFIER')"),
    @NamedQuery(name = "TicketCustom.findAllTicketsCustomOfProdT24Ano", query = "SELECT t FROM TicketCustom t WHERE t.ticketPointer.status != 'closed' AND t.ticketPointer.priority IN ('QUALIFIEE')"),
    @NamedQuery(name = "TicketCustom.findAllTicketsCustomOfDEV", query = "SELECT t FROM TicketCustom t, Permission p WHERE t.ticketPointer.owner = p.permissionPK.username and p.permissionPK.action='developeurs' and t.ticketPointer.status  IN ('new', 'assigned', 'accepted', 'reopened', 'problem_packaging', 'retournee')"),
    @NamedQuery(name = "TicketCustom.findAllTicketsCustomOfTEST", query = "SELECT t FROM TicketCustom t, Permission p WHERE t.ticketPointer.owner = p.permissionPK.username and p.permissionPK.action='testeurs' and t.ticketPointer.status  IN ('new', 'assigned', 'reopened', 'a_qualifier', 'besoin_info')"),
    @NamedQuery(name = "TicketCustom.findAllTicketsCustomOfOV", query = "SELECT t FROM TicketCustom t WHERE t.ticketPointer.status  IN ('probleme_deploiement', 'objet_livre')"),
    @NamedQuery(name = "TicketCustom.findAllTicketsCustomOfIE", query = "SELECT t FROM TicketCustom t WHERE t.ticketPointer.status  IN ('pret_pour_deploiement', 'pret_pour_mise_en_prod')"),
    @NamedQuery(name = "TicketCustom.findAllTicketsCustomOfPretPourProd", query = "SELECT t FROM TicketCustom t WHERE t.ticketPointer.status  IN ('qualifiee')"),
    @NamedQuery(name = "TicketCustom.findAllTicketsCustomOfOpenTickets", query = "SELECT t FROM TicketCustom t WHERE t.ticketPointer.status != 'closed'"),
    @NamedQuery(name = "TicketCustom.findByTicketAndName", query = "SELECT t FROM TicketCustom t WHERE t.ticket = :ticket and t.name = :name"),
    @NamedQuery(name = "TicketCustom.findByTicket", query = "SELECT t FROM TicketCustom t WHERE t.ticket = :ticket")})
public class TicketCustom implements Serializable, Comparable<TicketCustom> {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Column(name = "value")
    private String value;
    @Id
    @Basic(optional = false)
    @Column(name = "ticket")
    private Integer ticket;
    @ManyToOne
    @JoinColumn(name = "ticket", referencedColumnName = "id", insertable = false, updatable = false)
    private Ticket ticketPointer;
    //ajouté par anis pour trier les resultats de req
    @Transient
    private int ordre;

    public TicketCustom() {
    }

    public TicketCustom(Integer ticket) {
        this.ticket = ticket;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getTicket() {
        return ticket;
    }

    public void setTicket(Integer ticket) {
        this.ticket = ticket;
    }

    public Ticket getTicketPointer() {
        return ticketPointer;
    }

    public int getOrdre() {
        return ordre;
    }

    public void setOrdre(int ordre) {
        this.ordre = ordre;
    }

    public void setTicketPointer(Ticket ticketPointer) {
        this.ticketPointer = ticketPointer;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ticket != null ? ticket.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TicketCustom)) {
            return false;
        }
        TicketCustom other = (TicketCustom) object;
        if ((this.ticket == null && other.ticket != null) || (this.ticket != null && !this.ticket.equals(other.ticket))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.TicketCustom[ ticket=" + ticket + " ]";
    }

    @Override
    public int compareTo(TicketCustom o) {
        return (this.getOrdre() < o.getOrdre() ? -1
                : (this.getOrdre() == o.getOrdre() ? 0 : 1));
    }
}
