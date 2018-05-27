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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author 04486
 */
@IdClass(TicketChangePK.class)
@Entity
@Table(name = "ticket_change")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TicketChange.findAll", query = "SELECT t FROM TicketChange t"),
    @NamedQuery(name = "TicketChange.findByTicket", query = "SELECT t FROM TicketChange t WHERE t.ticket = :ticket"),
    @NamedQuery(name = "TicketChange.findByTime", query = "SELECT t FROM TicketChange t WHERE t.time = :time"),
    @NamedQuery(name = "TicketChange.findByAuthor", query = "SELECT t FROM TicketChange t WHERE t.author = :author"),
    @NamedQuery(name = "TicketChange.findByField", query = "SELECT t FROM TicketChange t WHERE t.field = :field"),
    @NamedQuery(name = "TicketChange.findByOldvalue", query = "SELECT t FROM TicketChange t WHERE t.oldvalue = :oldvalue"),
    @NamedQuery(name = "TicketChange.findByNewvalue", query = "SELECT t FROM TicketChange t WHERE t.newvalue = :newvalue")})
public class TicketChange implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ticket")
    private Integer ticket;
    @ManyToOne
    @JoinColumn(name = "ticket", referencedColumnName = "id", insertable = false, updatable = false)
    private Ticket ticketPointer;
    @Id
    @Basic(optional = false)
    @Column(name = "time")
    private Long time;
    @Column(name = "author")
    private String author;
    @Id
    @Basic(optional = false)
    @Column(name = "field")
    private String field;
    @Column(name = "oldvalue")
    private String oldvalue;
    @Column(name = "newvalue")
    private String newvalue;

    public TicketChange() {
    }

    public TicketChange(Integer ticket) {
        this.ticket = ticket;
    }

    public Integer getTicket() {
        return ticket;
    }

    public void setTicket(Integer ticket) {
        this.ticket = ticket;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getOldvalue() {
        return oldvalue;
    }

    public void setOldvalue(String oldvalue) {
        this.oldvalue = oldvalue;
    }

    public String getNewvalue() {
        return newvalue;
    }

    public void setNewvalue(String newvalue) {
        this.newvalue = newvalue;
    }

    public Ticket getTicketPointer() {
        return ticketPointer;
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
        if (!(object instanceof TicketChange)) {
            return false;
        }
        TicketChange other = (TicketChange) object;
        if ((this.ticket == null && other.ticket != null) || (this.ticket != null && !this.ticket.equals(other.ticket))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return " ticket=" + ticket + "\n time=" + time + "\n author=" + author + "\n field=" + field + "\n oldvalue=" + oldvalue + "\n newvalue=" + newvalue + "\n_____________________________\n";
    }
}
