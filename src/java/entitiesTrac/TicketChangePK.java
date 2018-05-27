/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entitiesTrac;

import java.io.Serializable;

/**
 *
 * @author 04486
 */
public class TicketChangePK implements Serializable {

    private Integer ticket;
    private Long time;
    private String field;

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

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + (this.ticket != null ? this.ticket.hashCode() : 0);
        hash = 29 * hash + (this.time != null ? this.time.hashCode() : 0);
        hash = 29 * hash + (this.field != null ? this.field.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TicketChangePK other = (TicketChangePK) obj;
        if (this.ticket != other.ticket && (this.ticket == null || !this.ticket.equals(other.ticket))) {
            return false;
        }
        if (this.time != other.time && (this.time == null || !this.time.equals(other.time))) {
            return false;
        }
        if ((this.field == null) ? (other.field != null) : !this.field.equals(other.field)) {
            return false;
        }
        return true;
    }
}
