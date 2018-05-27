/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package newTrac;

import dataBaseTracRequests.AppelRequetes;
import dataBaseTracRequests.DataBaseTracGenericRequests;
import entitiesTrac.Ticket;
import entitiesTrac.TicketCustom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import static servlets.AutorisationHotfixServlet.ConvertTimeTracToJavaDate;
import tools.Configuration;
import tools.Tools;

/**
 *
 * @author 04486
 */
public class ToolsNewTrac {

    public static Map<Integer, Map<String, Object>> executeQueryThenAnalyseTickets(String pu, String siteTrac, String namedQuery, String[] cles, String equivalentNaturetraitement) {
        Map<Integer, Map<String, Object>> mapTicketsCustomGlobal = new LinkedHashMap<Integer, Map<String, Object>>();
        List<TicketCustom> globalTickList = null;


        try {
            try {
                globalTickList = new DataBaseTracGenericRequests<TicketCustom>().getList_TYPE_OfnamedQuery(pu, namedQuery, null);
            } catch (Exception exep) {
                exep.printStackTrace();
                tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
            }
            Set<Integer> setIdTicket = new TreeSet<Integer>();

            for (TicketCustom ticketCustom : globalTickList) {
                setIdTicket.add(ticketCustom.getTicket());
            }
            List<Integer> listeIdTicket = new ArrayList<Integer>(setIdTicket);
            Tools.showConsolLog("nbr d'objets: " + listeIdTicket.size());

            if (listeIdTicket.size() > 0) {
                mapTicketsCustomGlobal = AppelRequetes.getTicketCustomByTicketIdAndNames(listeIdTicket, pu, siteTrac, cles);
                //ajout des champs suivants à la map (milestone, priority, status, ...)
                //c'est très important pour le tri
                Ticket ticket;
                for (Integer numTicketAnomalie : mapTicketsCustomGlobal.keySet()) {
                    Map<String, Object> ticketCustomDetailsAux = mapTicketsCustomGlobal.get(numTicketAnomalie);
                    ticket = (Ticket) (ticketCustomDetailsAux.get("Ticket"));
                    ticketCustomDetailsAux.put("milestone", ticket.getMilestone());
                    ticketCustomDetailsAux.put("status", ticket.getStatus());
                    ticketCustomDetailsAux.put("priority", ticket.getPriority());
                    ticketCustomDetailsAux.put("id", ticket.getId());
                    ticketCustomDetailsAux.put("summary", ticket.getSummary());
                    ticketCustomDetailsAux.put("owner", ticket.getOwner());
                    ticketCustomDetailsAux.put("reporter", ticket.getReporter());
                    ticketCustomDetailsAux.put("component", ticket.getComponent());
                    ticketCustomDetailsAux.put("version", ticket.getVersion());
                    ticketCustomDetailsAux.put("type", ticket.getType());
                    ticketCustomDetailsAux.put("changetime", ConvertTimeTracToJavaDate(ticket.getChangetime() / 1000));
                    // ce bloque est destiné pour adapter les projets livraison et anomalie T24 car ils ne disposent pas de champ nature_traitement
                    if (equivalentNaturetraitement != null) {
                        if (!ticketCustomDetailsAux.containsKey("nature_traitement")) {
                            if (equivalentNaturetraitement.equals("version")) {
                                ticketCustomDetailsAux.put("nature_traitement", ticket.getVersion());
                            } else if (equivalentNaturetraitement.equals("type")) {
                                ticketCustomDetailsAux.put("nature_traitement", ticket.getType());
                            }
                        }
                    }
                    mapTicketsCustomGlobal.put(numTicketAnomalie, ticketCustomDetailsAux);
                    //remplir la set par tous les status possibles qui existent dans les tickets
                    //cette ligne est nécessaire pour que si un statut n'existe pas dans la set qui est déjà initialisée, on l'ajoute
                    Configuration.statusSet.add(ticket.getStatus());
                }
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return mapTicketsCustomGlobal;
    }

    public static Map<Integer, Map<String, Object>> traiterTicketCustom(List<TicketCustom> ticketCustomList) {
        Map<Integer, Map<String, Object>> ticketMap = new HashMap<Integer, Map<String, Object>>();
        try {
            Integer numTicket = null;
            for (TicketCustom ticketCustom : ticketCustomList) {
                numTicket = ticketCustom.getTicket();
                //Tools.showConsolLog(numTicket+"  "+ticketCustom.getName());
                if (ticketMap.containsKey(numTicket)) {
                    //Tools.showConsolLog("\n\n\n\n"+ticketCustom.getName()+"   "+ ticketCustom.getValue());
                    ticketMap.get(numTicket).put(ticketCustom.getName(), ticketCustom.getValue());
                } else {
                    Map<String, Object> ticketCustomDetails = new HashMap<String, Object>();
                    ticketCustomDetails.put("Ticket", ticketCustom.getTicketPointer());
                    ticketCustomDetails.put(ticketCustom.getName(), ticketCustom.getValue());
                    ticketMap.put(numTicket, ticketCustomDetails);
                }
            }

            numTicket = null;
            System.gc();
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return ticketMap;
    }
}
