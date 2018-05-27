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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author 04486
 */
@Entity
@Table(name = "ticket")
@XmlRootElement
@NamedQueries({
    /*LIVRAISON*/
    //@NamedQuery(name = "Ticket.findAllPipeTicketsCDDByOwner", query = "SELECT t FROM Ticket t WHERE t.priority IN ('LIVRAISON CONFIRMEE', 'PROBLEME DE PACKAGING') AND t.type IN ('QUALIFICATION','CERTIFICATION','QUALIFICATION_PROJET','HOT FIXE TEST', 'QUALIFICATION_UPGRADE', 'CERTIFICATION_UPGRADE', 'ACTION A CHAUD TEST') AND t.status!='closed' AND t.owner like :owner ORDER BY t.owner, t.changetime"),
    @NamedQuery(name = "Ticket.findAllPipeTicketsOV", query = "SELECT t FROM Ticket t WHERE t.priority IN ('OBJET LIVREE', 'PROBLEME DE DEPLOIEMENT') AND t.status!='closed' AND t.type IN ('QUALIFICATION','CERTIFICATION','QUALIFICATION_PROJET','HOT FIXE TEST','HOT FIXE PROD','ACTION A CHAUD TEST','ACTION A CHAUD PROD', 'QUALIFICATION_UPGRADE', 'CERTIFICATION_UPGRADE') ORDER BY t.changetime"),
    @NamedQuery(name = "Ticket.findAllPipeTicketsIandE", query = "SELECT t FROM Ticket t WHERE t.priority='PRET POUR DEPLOIEMENT' AND t.status!='closed' AND t.type IN ('QUALIFICATION','CERTIFICATION','QUALIFICATION_PROJET','HOT FIXE TEST','HOT FIXE PROD','ACTION A CHAUD TEST','ACTION A CHAUD PROD', 'QUALIFICATION_UPGRADE', 'CERTIFICATION_UPGRADE') ORDER BY t.changetime"),
    /*Fin LIVRAISON*/
    @NamedQuery(name = "Ticket.findAll", query = "SELECT t FROM Ticket t"),
    @NamedQuery(name = "Ticket.findTicketsEnCours", query = "SELECT t FROM Ticket t WHERE t.status <> 'closed'"),
    @NamedQuery(name = "Ticket.countAll", query = "SELECT  MAX (t.id) FROM Ticket t"),
    @NamedQuery(name = "Ticket.findById", query = "SELECT t FROM Ticket t WHERE t.id = :id"),
    @NamedQuery(name = "Ticket.findByTime", query = "SELECT t FROM Ticket t WHERE t.time = :time"),
    @NamedQuery(name = "Ticket.findByChangetime", query = "SELECT t FROM Ticket t WHERE t.changetime = :changetime"),
    @NamedQuery(name = "Ticket.findAllPipeTicketsHraccessOV", query = "SELECT t FROM Ticket t WHERE t.status='objet_livre' ORDER BY t.changetime"),
    @NamedQuery(name = "Ticket.findAllPipeTicketsHraccessIandE", query = "SELECT t FROM Ticket t WHERE t.status='pret_pour_mise_en_prod' ORDER BY t.changetime"),
    @NamedQuery(name = "Ticket.findAllHotfixHraccessAHarmoniser", query = "SELECT t FROM Ticket t WHERE t.status='deployee_sur_prod' ORDER BY t.changetime")})
public class Ticket implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "type")
    private String type;
    @Column(name = "time")
    private Long time;
    @Column(name = "changetime")
    private Long changetime;
    @Column(name = "component")
    private String component;
    @Column(name = "severity")
    private String severity;
    @Column(name = "priority")
    private String priority;
    @Column(name = "owner")
    private String owner;
    @Column(name = "reporter")
    private String reporter;
    @Column(name = "cc")
    private String cc;
    @Column(name = "version")
    private String version;
    @Column(name = "milestone")
    private String milestone;
    @Column(name = "status")
    private String status;
    @Column(name = "resolution")
    private String resolution;
    @Column(name = "summary")
    private String summary;
    @Column(name = "description")
    private String description;
    @Column(name = "keywords")
    private String keywords;

    public Ticket() {
    }

    public Ticket(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Long getChangetime() {
        return changetime;
    }

    public void setChangetime(Long changetime) {
        this.changetime = changetime;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
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

    public String getCc() {
        if (cc == null) {
            return null;
        } else {
            return cc.replaceAll("\\.", " ").replaceAll(",", "");
        }
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMilestone() {
        return milestone;
    }

    public void setMilestone(String milestone) {
        this.milestone = milestone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
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
        if (!(object instanceof Ticket)) {
            return false;
        }
        Ticket other = (Ticket) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Ticket{" + "id=" + id + ", type=" + type + ", time=" + time + ", changetime=" + changetime + ", component=" + component + ", severity=" + severity + ", priority=" + priority + ", owner=" + owner + ", reporter=" + reporter + ", cc=" + cc + ", version=" + version + ", milestone=" + milestone + ", status=" + status + ", resolution=" + resolution + ", summary=" + summary + ", description=" + description + ", keywords=" + keywords + '}';
    }
}
