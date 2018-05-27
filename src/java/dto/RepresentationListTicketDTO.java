/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.util.*;

/**
 *
 * @author 04486
 */
public class RepresentationListTicketDTO {

    private List<Map<String, Object>> ListTickets;

    public RepresentationListTicketDTO(List<Map<String, Object>> ListTickets) {
        this.ListTickets = ListTickets;
    }

    public List<Map<String, Object>> getListTickets() {
        return ListTickets;
    }

    public void setListTickets(List<Map<String, Object>> ListTickets) {
        this.ListTickets = ListTickets;
    }
}
