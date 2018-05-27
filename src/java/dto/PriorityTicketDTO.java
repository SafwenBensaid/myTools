/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import entitiesTrac.Ticket;
import java.util.*;

/**
 *
 * @author 04486
 */
public class PriorityTicketDTO {

    private Map<String, List<Ticket>> mapDomainePriorityTicketDTO;

    public Map<String, List<Ticket>> getMapDomainePriorityTicketDTO() {
        return mapDomainePriorityTicketDTO;
    }

    public void setMapDomainePriorityTicketDTO(Map<String, List<Ticket>> mapDomainePriorityTicketDTO) {
        this.mapDomainePriorityTicketDTO = mapDomainePriorityTicketDTO;
    }
}
