package test;

import dataBaseTracRequests.AppelRequetes;
import dataBaseTracRequests.DataBaseTracGenericRequests;
import dataBaseTracRequests.GenericDataBaseRequestReport;
import dto.TicketChangeDTO;
import entitiesTrac.Ticket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import tools.Configuration;

public class Main {

    public static void main(String[] args) {
        //Cloture des ticket
        List<Integer> listAllIdTickets = new ArrayList<>();
        //2)
        List<Integer> ticketAnomalieHotfixList = new DataBaseTracGenericRequests<Integer>().getList_TYPE_OfnamedQuery(Configuration.puAnomalies, "TicketCustom.findAllClosed", null);

        //listAllIdTickets.addAll(ticketAnomalieHotfixList);
        String persistanceUnit = Configuration.puAnomalies;
        String siteTrac = Configuration.tracAnomalies;
        String[] cles = new String[]{"t_liv"};
        //Map<Integer, Map<String, Object>> globalResultMap = AppelRequetes.getTicketCustomByTicketIdAndNames(listAllIdTickets, persistanceUnit, siteTrac, cles);
        //Date
        Map<String, List<String>> ticketElementsMapIn = new HashMap<>();
        Map<String, List<String>> ticketElementsMapNotIn = new HashMap<>();
        Map<String, List<String>> ticketCustomElementsMapIn = new HashMap<>();
        Map<String, List<String>> ticketCustomElementsMapNotIn = new HashMap<>();
        TicketChangeDTO ticketChangeDTO = null;
        String[] fieldsToBeDisplayed = null;
        String resultat = "";
        String dateDepart = "01-01-2010 00:00";
        String dateFin = "01-01-2018 00:00";
        GenericDataBaseRequestReport genericReq = new GenericDataBaseRequestReport();
        //ticketElementsMapIn.put("version", Arrays.asList(new String[]{"ACTION A CHAUD POUR MISE EN PROD"}));
        ticketElementsMapIn.put("status", Arrays.asList(new String[]{"closed"}));
        //ticketCustomElementsMapIn.put("mode_traitement", Arrays.asList(new String[]{"PALLIATIF "}));
        ticketChangeDTO = new TicketChangeDTO("status", dateDepart, dateFin, "closed");
        cles = new String[]{"projet", "mode_traitement", "marge_resolution", "t_liv"};
        fieldsToBeDisplayed = new String[]{"numeroAnomalie", "milestone", "owner", "reporter", "dateDeploiement", "summary", "description"};

        resultat = genericReq.executeGenericRequest(persistanceUnit, ticketElementsMapIn, ticketElementsMapNotIn, ticketCustomElementsMapIn, ticketCustomElementsMapNotIn, ticketChangeDTO, cles, fieldsToBeDisplayed, siteTrac);
        /*
        int i = 0;
        for (Map.Entry<Integer, Map<String, Object>> entry : globalResultMap.entrySet()) {
            i++;
            Map<String, Object> ticketsMap = entry.getValue();
            Ticket ticketAnomalie = (Ticket) ticketsMap.get("Ticket");
            //Integer ticketLivraisonId = (Integer) ticketsMap.get("t_liv");

            System.out.print(i + ") " + ticketAnomalie.getId() + ">");
            System.out.println(ticketsMap.get("t_liv"));
                  
        }
*/
    }
}