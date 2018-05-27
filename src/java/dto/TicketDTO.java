/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.io.Serializable;
import java.util.*;

/**
 *
 * @author 04486
 */
public class TicketDTO<TICKET, TICKET_CUSTOM> implements Serializable {

    private Integer idTicket;
    private TICKET ticket;
    private HashMap<String, TICKET_CUSTOM> ticketCustomMap;

    public TicketDTO(Integer idTicket, TICKET ticket, HashMap<String, TICKET_CUSTOM> ticketCustomMap) {
        this.idTicket = idTicket;
        this.ticket = ticket;
        this.ticketCustomMap = ticketCustomMap;
    }

    public Integer getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(Integer idTicket) {
        this.idTicket = idTicket;
    }

    public TICKET getTicket() {
        return ticket;
    }

    public void setTicket(TICKET ticket) {
        this.ticket = ticket;
    }

    public HashMap<String, TICKET_CUSTOM> getTicketCustomMap() {
        return ticketCustomMap;
    }

    public void setTicketCustomMap(HashMap<String, TICKET_CUSTOM> ticketCustomMap) {
        this.ticketCustomMap = ticketCustomMap;
    }
}
