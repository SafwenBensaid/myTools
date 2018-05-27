/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataBaseTracRequests;

import dto.TicketChangeDTO;
import entitiesTrac.Ticket;
import entitiesTrac.TicketChange;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import tools.Configuration;
import tools.DataBaseTools;
import tools.GenericTools;

/**
 *
 * @author 04486
 */
public class GenericDataBaseRequestReport {

    public String executeGenericRequest(String persistanceUnit, Map<String, List<String>> ticketElementsMapIn, Map<String, List<String>> ticketElementsMapNotIn, Map<String, List<String>> ticketCustomElementsMapIn, Map<String, List<String>> ticketCustomElementsMapNotIn, TicketChangeDTO ticketChangeDTO, String[] cles, String[] fieldsToBeDisplayed, String siteTrac) {
        String resultat = "";
        Map<Integer, Map<String, Object>> globalResultMap = new HashMap<>();
        Map<Integer, Long> dateDeploiementMap = new HashMap<>();
        List<Integer> listAllIdTickets = new ArrayList<>();
        try {
            StringBuilder querySb = new GenericDataBaseRequestReport().createQueryString(ticketElementsMapIn, ticketElementsMapNotIn, ticketCustomElementsMapIn, ticketCustomElementsMapNotIn, ticketChangeDTO);
            DataBaseTools dbTools = new DataBaseTools(persistanceUnit);
            List<TicketChange> listAllIdTicketChange = new DataBaseTracGenericRequests<TicketChange>().executeQueryRequest(dbTools, querySb, "Q");
            dbTools.closeRessources();
            //affichage
            for (TicketChange t : listAllIdTicketChange) {
                System.out.println(t.getTicket());
                listAllIdTickets.add(t.getTicket());
                dateDeploiementMap.put(t.getTicket(), t.getTime());
            }
            //get all ticket custom
            if (listAllIdTickets == null || listAllIdTickets.isEmpty()) {
                return "<span class = 'rouge'>Aucun ticket à afficher</span>";
            }
            globalResultMap = AppelRequetes.getTicketCustomByTicketIdAndNames(listAllIdTickets, persistanceUnit, siteTrac, cles);
            traiterTickets(globalResultMap, dateDeploiementMap, siteTrac);
            resultat = GenericTools.displayTable(globalResultMap, fieldsToBeDisplayed, siteTrac);
        } catch (Exception ex) {
            Logger.getLogger(GenericDataBaseRequestReport.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultat;
    }

    public static void traiterTickets(Map<Integer, Map<String, Object>> globalResultMap, Map<Integer, Long> dateDeploiementMap, String siteTrac) {
        for (Map.Entry<Integer, Map<String, Object>> entry : globalResultMap.entrySet()) {
            Map<String, Object> ticketsMap = entry.getValue();
            Ticket ticket = (Ticket) ticketsMap.get("Ticket");
            Integer ticketId = ticket.getId();
            GenericTools gtTick = new GenericTools<Ticket>(Ticket.class);
            Map<String, Object> dtoMapTick = gtTick.convertDtoToMap(ticket);
            ticketsMap.putAll(dtoMapTick);
            if (dateDeploiementMap.containsKey(ticketId)) {
                ticketsMap.put("dateDeploiement", dateDeploiementMap.get(ticketId) / 1000);
            }
            if (siteTrac.equals(Configuration.tracAnomalies)) {
                ticketsMap.put("numeroAnomalie", ticketId);
            }
        }
    }

    private long convertDateTotracDate(String stringDate, String dateFormatterString) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormatterString);
        Date d;
        long milliseconds = 0;
        try {
            d = sdf.parse(stringDate);
            milliseconds = d.getTime() * 1000;
        } catch (ParseException ex) {
            Logger.getLogger(GenericDataBaseRequestReport.class.getName()).log(Level.SEVERE, null, ex);
        }
        return milliseconds;
    }

    public StringBuilder createQueryString(Map<String, List<String>> ticketElementsMapIn, Map<String, List<String>> ticketElementsMapNotIn, Map<String, List<String>> ticketCustomElementsMapIn, Map<String, List<String>> ticketCustomElementsMapNotIn, TicketChangeDTO ticketChangeDTO) {
        long t1 = 0;
        long t2 = 0;
        if (ticketChangeDTO != null) {
            t1 = convertDateTotracDate(ticketChangeDTO.getDate1(), Configuration.HEURE_DATE_FORMAT);
            t2 = convertDateTotracDate(ticketChangeDTO.getDate2(), Configuration.HEURE_DATE_FORMAT);
        }
        StringBuilder querySb = new StringBuilder();
        querySb.append("SELECT DISTINCT(tc) FROM TicketCustom t ");

        if (ticketChangeDTO != null) {
            querySb.append("INNER JOIN TicketChange tc ON t.ticket = tc.ticket ");
        }
        querySb.append("WHERE ");
        createTicketCondition(querySb, "IN", ticketElementsMapIn);
        createTicketCondition(querySb, "NOT IN", ticketElementsMapNotIn);
        if (querySb.lastIndexOf(" AND ") == querySb.length() - 5) {
            querySb.replace(querySb.length() - 5, querySb.length() - 1, "");
        }
        if (!ticketCustomElementsMapIn.isEmpty() || !ticketCustomElementsMapNotIn.isEmpty()) {
            if (!querySb.toString().endsWith("WHERE ")) {
                querySb.append(" AND (");
            } else {
                querySb.append(" (");
            }
            createTicketCustomCondition(querySb, "IN", ticketCustomElementsMapIn);
            createTicketCustomCondition(querySb, "NOT IN", ticketCustomElementsMapNotIn);
            if (querySb.lastIndexOf(" OR ") == querySb.length() - 4) {
                querySb.replace(querySb.length() - 4, querySb.length() - 1, "");
            }
            querySb.append(")");
        }
        if (ticketChangeDTO != null) {
            querySb.append(" AND tc.field ='").append(ticketChangeDTO.getField()).append("' AND tc.newvalue='").append(ticketChangeDTO.getNewvalue()).append("' AND tc.time BETWEEN ").append(t1).append(" AND ").append(t2).append(" ORDER BY tc.time DESC");
        }
        System.out.println(querySb.toString());
        return querySb;
    }

    private void createTicketCondition(StringBuilder querySb, String conditionType, Map<String, List<String>> tickMap) {
        if (!tickMap.isEmpty()) {
            for (Map.Entry<String, List<String>> entry : tickMap.entrySet()) {
                querySb.append("t.ticketPointer.").append(entry.getKey()).append(" ").append(conditionType).append(" (");
                for (String val : entry.getValue()) {
                    querySb.append("'").append(val).append("'").append(",");
                }
                if (querySb.lastIndexOf(",") == querySb.length() - 1) {
                    querySb.deleteCharAt(querySb.length() - 1);
                }
                querySb.append(") AND ");
            }
        }
    }

    private void createTicketCustomCondition(StringBuilder querySb, String conditionType, Map<String, List<String>> tickMap) {
        if (!tickMap.isEmpty()) {
            for (Map.Entry<String, List<String>> entry : tickMap.entrySet()) {
                querySb.append("( t.name = ").append("'").append(entry.getKey()).append("'").append(" and t.value ").append(conditionType).append(" (");
                for (String val : entry.getValue()) {
                    querySb.append("'").append(val).append("'").append(",");
                }
                if (querySb.lastIndexOf(",") == querySb.length() - 1) {
                    querySb.deleteCharAt(querySb.length() - 1);
                }
                querySb.append(") ) OR ");
            }
        }
    }
}