/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import entitiesTrac.Ticket;
import entitiesTrac.TicketChange;
import entitiesTrac.TicketCustom;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author 04486
 */
public class UpdateTicketsTools {

    public List<Object> objectsListToBeMergedOnTheDataBaseTrac;

    public UpdateTicketsTools() {
        objectsListToBeMergedOnTheDataBaseTrac = new ArrayList<Object>();
    }

    public void updateTicket(Object ticket, Integer idTicket, String author, String fieldName, String oldValue, String newValue, long timeTrac) {
        if (oldValue == null) {
            oldValue = "";
            Tools.traiterException("Attention, le champ " + fieldName + " du ticket " + ticket + " a été null, on l'a caste à vide");
        }
        try {
            if (!oldValue.equals(newValue)) {
                createAndStoreTicketChangeIntoDB(ticket, idTicket, timeTrac, author, fieldName, oldValue, newValue);
            }
            if (!(ticket instanceof TicketChange)) {
                updateChangeTime(ticket, timeTrac);
                objectsListToBeMergedOnTheDataBaseTrac.add(ticket);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Tools.traiterException(Tools.getStackTrace(ex));
        }
    }

    private void createAndStoreTicketChangeIntoDB(Object ticket, Integer idTicket, Long time, String author, String field, String oldvalue, String newvalue) {
        try {
            TicketChange ticketChange = new TicketChange();
            ticketChange.setTicket(idTicket);
            ticketChange.setTime(time);
            ticketChange.setAuthor(author);
            ticketChange.setField(field);
            ticketChange.setOldvalue(oldvalue);
            ticketChange.setNewvalue(newvalue);
            objectsListToBeMergedOnTheDataBaseTrac.add(ticketChange);

        } catch (Exception ex) {
            ex.printStackTrace();
            Tools.traiterException(Tools.getStackTrace(ex));
        }
    }

    private void updateChangeTime(Object ticket, Long time) {
        try {
            Ticket tick = null;
            if (ticket instanceof Ticket) {
                tick = (Ticket) ticket;
            } else if (ticket instanceof TicketCustom) {
                tick = ((TicketCustom) ticket).getTicketPointer();
            }
            tick.setChangetime(time);
            objectsListToBeMergedOnTheDataBaseTrac.add(tick);
        } catch (Exception ex) {
            ex.printStackTrace();
            Tools.traiterException(Tools.getStackTrace(ex));
        }
    }

    public static long generateTracDateNow() {
        String currentTimeMillis = String.valueOf(System.currentTimeMillis()) + "000";
        return Long.parseLong(currentTimeMillis);
    }
}
