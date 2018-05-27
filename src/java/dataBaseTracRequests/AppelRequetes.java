/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataBaseTracRequests;

import entitiesTrac.Ticket;
import entitiesTrac.TicketChange;
import entitiesTrac.TicketCustom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import tools.Configuration;
import tools.DataBaseTools;
import tools.Tools;

/**
 *
 * @author 04486
 */
public class AppelRequetes {

    public static List<Integer> getTicketsLivraisonsIdCddHarmCP(DataBaseTools dbTools) {
        List<Integer> ticketsIdList = null;
        StringBuilder querySb = new StringBuilder();
        querySb.append("SELECT t.id  ");
        querySb.append("FROM `ticket` t ");
        querySb.append("LEFT JOIN ticket_custom biatprod ");
        querySb.append("ON (t.id = biatprod.ticket AND biatprod.name = 'biatprod') ");
        querySb.append("LEFT JOIN ticket_custom nature_liv ");
        querySb.append("ON (t.id = nature_liv.ticket AND nature_liv.name = 'nature_liv') ");
        querySb.append("LEFT JOIN ticket_custom biatref ");
        querySb.append("ON (t.id = biatref.ticket AND biatref.name = 'biatref') ");
        querySb.append("LEFT JOIN ticket_custom biatdev2 ");
        querySb.append("ON (t.id = biatdev2.ticket AND biatdev2.name = 'biatdev2') ");
        querySb.append("LEFT JOIN ticket_custom biatass2 ");
        querySb.append("ON (t.id = biatass2.ticket AND biatass2.name = 'biatass2') ");
        querySb.append("WHERE t.priority='DEPLOYEE' ");
        querySb.append("AND t.status NOT IN ('closed') ");
        querySb.append("AND t.type IN ('HOT FIXE PROD','ACTION A CHAUD PROD') ");
        querySb.append("AND biatprod.value = '1' ");
        querySb.append("AND nature_liv.value IN ('HARMONISATION_C.PROJET') ");
        querySb.append("AND biatref.value = '1' ");
        querySb.append("AND biatdev2.value = '0' ");
        querySb.append("AND biatass2.value = '1' ");
        querySb.append("ORDER BY t.changetime ");
        try {
            ticketsIdList = new DataBaseTracGenericRequests<Integer>().executeQueryRequest(dbTools, querySb, "NQ");
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return ticketsIdList;
    }

    public static List<Integer> getTicketsLivraisonsIdCddHarmCR(DataBaseTools dbTools) {
        List<Integer> ticketsIdList = null;
        StringBuilder querySb = new StringBuilder();
        querySb.append("SELECT t.id ");
        querySb.append("FROM `ticket` t ");
        querySb.append("LEFT JOIN ticket_custom biatprod ");
        querySb.append("ON (t.id = biatprod.ticket AND biatprod.name = 'biatprod') ");
        querySb.append("LEFT JOIN ticket_custom nature_liv ");
        querySb.append("ON (t.id = nature_liv.ticket AND nature_liv.name = 'nature_liv') ");
        querySb.append("LEFT JOIN ticket_custom biatref ");
        querySb.append("ON (t.id = biatref.ticket AND biatref.name = 'biatref') ");
        querySb.append("LEFT JOIN ticket_custom biatdevr ");
        querySb.append("ON (t.id = biatdevr.ticket AND biatdevr.name = 'biatdevr') ");
        querySb.append("LEFT JOIN ticket_custom biattem ");
        querySb.append("ON (t.id = biattem.ticket AND biattem.name = 'biattem') ");
        querySb.append("LEFT JOIN ticket_custom biatql1 ");
        querySb.append("ON (t.id = biatql1.ticket AND biatql1.name = 'biatql1') ");
        querySb.append("LEFT JOIN ticket_custom biatcertif ");
        querySb.append("ON (t.id = biatcertif.ticket AND biatcertif.name = 'biatcertif') ");
        querySb.append("WHERE t.priority='DEPLOYEE' ");
        querySb.append("AND t.status NOT IN ('closed') ");
        querySb.append("AND t.type IN ('HOT FIXE PROD','ACTION A CHAUD PROD') ");
        querySb.append("AND biatprod.value = '1' ");
        querySb.append("AND nature_liv.value IN ('HARMONISATION_C.RELEASE') ");
        querySb.append("AND biatref.value = '1' ");
        querySb.append("AND biatdevr.value = '0' ");
        querySb.append("AND biattem.value = '1' ");
        querySb.append("AND biatql1.value = '1' ");
        querySb.append("AND biatcertif.value = '1' ");
        querySb.append("ORDER BY t.changetime ");
        try {
            ticketsIdList = new DataBaseTracGenericRequests<Integer>().executeQueryRequest(dbTools, querySb, "NQ");
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return ticketsIdList;
    }

    public static List<TicketChange> getDeployementDateRequest(String pu, List<Integer> listeIdTickets, String newValue, int... iterations) {
        List<TicketChange> listeTicketCustom = null;
        DataBaseTools dbTools = new DataBaseTools(pu);
        try {
            StringBuilder querySb = new StringBuilder("SELECT t FROM TicketChange t WHERE t.field = 'priority' and t.newvalue = '" + newValue + "' and t.ticket in(");
            for (int i = 0; i < listeIdTickets.size(); i++) {
                if (i < listeIdTickets.size() - 1) {
                    querySb.append(listeIdTickets.get(i));
                    querySb.append(", ");
                } else {
                    querySb.append(listeIdTickets.get(i));
                    querySb.append(" )");
                }
            }
            listeTicketCustom = new DataBaseTracGenericRequests<TicketChange>().executeQueryRequest(dbTools, querySb, "Q");
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        } finally {
            dbTools.closeRessources();
        }
        return listeTicketCustom;
    }

    public static List<Ticket> getTicketsHotfixAHarmoniser(String pu, int... iterations) {
        List<Ticket> hFaHarmoniserProdCoche = new ArrayList<Ticket>();
        List<Ticket> hFaHarmoniserRefNonCoche = new ArrayList<Ticket>();
        try {
            String namedQuery1 = "TicketCustom.findAllHotfixAHarmoniserProdCocheOV";
            String namedQuery2 = "TicketCustom.findAllHotfixAHarmoniserRefNonCocheOV";
            hFaHarmoniserProdCoche = new DataBaseTracGenericRequests<Ticket>().getList_TYPE_OfnamedQuery(pu, namedQuery1, null);
            hFaHarmoniserRefNonCoche = new DataBaseTracGenericRequests<Ticket>().getList_TYPE_OfnamedQuery(pu, namedQuery2, null);
            hFaHarmoniserProdCoche.retainAll(hFaHarmoniserRefNonCoche);
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return hFaHarmoniserProdCoche;
    }

    public static List<TicketCustom> getTicketCustomListOfNamedQuery(String pu, String siteTrac, String namedQuery) {
        List<TicketCustom> listeTicketCustom = null;
        try {
            listeTicketCustom = new DataBaseTracGenericRequests<TicketCustom>().getList_TYPE_OfnamedQuery(pu, namedQuery, null);
            for (TicketCustom tickCus : listeTicketCustom) {
                tickCus = (TicketCustom) Tools.reformTlivTicketOrigine(tickCus, siteTrac);
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return listeTicketCustom;
    }

    public static Map<Integer, Map<String, Object>> getTicketCustomByTicketIdAndNames(List<Integer> listeIdTicketsUnique, String pu, String siteTrac, String[] cles, int... iterations) {
        DataBaseTools dbTools = null;
        Map<Integer, Map<String, Object>> ticketMap = null;
        try {
            dbTools = new DataBaseTools(pu);
            List<TicketCustom> ticketCustomList = null;
            List<TicketCustom> ticketCustomListGlobal = new ArrayList<>();
            Map<Integer, Integer> oredrMap = new LinkedHashMap<>();
            Tools.showConsolLog("Start : listeIdTicketsLivraisonsUnique.size(): " + listeIdTicketsUnique.size());
            int cmp = 0;
            StringBuilder querySb = null;
            while (cmp < listeIdTicketsUnique.size()) {
                querySb = new StringBuilder("SELECT t FROM TicketCustom t WHERE t.ticket IN (");
                for (int i = cmp; i < cmp + 300; i++) {
                    if (i < listeIdTicketsUnique.size()) {
                        oredrMap.put(listeIdTicketsUnique.get(i), i);
                        if ((i < cmp + 299) && (i < listeIdTicketsUnique.size() - 1)) {
                            querySb.append(listeIdTicketsUnique.get(i));
                            querySb.append(", ");
                        } else {
                            querySb.append(listeIdTicketsUnique.get(i));
                            querySb.append(" ) AND t.name IN (");
                            break;
                        }
                    }
                }
                for (int i = 0; i < cles.length; i++) {
                    if (i < cles.length - 1) {
                        querySb.append("'");
                        querySb.append(cles[i]);
                        querySb.append("', ");
                    } else {
                        querySb.append("'");
                        querySb.append(cles[i]);
                        querySb.append("' ) ");
                    }
                }
                Tools.showConsolLog(querySb.toString());
                ticketCustomList = new DataBaseTracGenericRequests<TicketCustom>().executeQueryRequest(dbTools, querySb, "Q");
                ticketCustomListGlobal.addAll(ticketCustomList);
                cmp += 300;
            }
            for (TicketCustom tickCus : ticketCustomListGlobal) {
                tickCus = (TicketCustom) Tools.reformTlivTicketOrigine(tickCus, siteTrac);
                //pour faire le tri des resultats de la requete selon la liste entree IN
                int ordre;
                try {
                    ordre = oredrMap.get(tickCus.getTicket());
                } catch (Exception e) {
                    ordre = 0;
                }
                tickCus.setOrdre(ordre);
            }
            try {
                Collections.sort(ticketCustomListGlobal);
            } catch (Exception exep) {
                exep.printStackTrace();
                tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
            }


            Tools.showConsolLog("Nombre de tickets:" + ticketCustomListGlobal.size());
            ticketMap = DataBaseTracRequests.traiterTicketCustom(ticketCustomListGlobal);
            dbTools.closeRessources();
            System.gc();
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return ticketMap;
    }

    public static Map<String, Object> getAnomaliesPrioritieByAnomalieIds(List<Integer> numAnomaliesList, int... iterations) {
        String resltat = null;
        DataBaseTools dbTools = null;
        Map<String, Object> coupleAnomaliePriorite = new HashMap<String, Object>();
        try {
            Set<Integer> numAnomaliesSet = new TreeSet<Integer>();
            Boolean zeroExists = false;
            for (Integer numAnomalie : numAnomaliesList) {
                if (numAnomalie == 0) {
                    zeroExists = true;
                } else {
                    numAnomaliesSet.add(numAnomalie);
                }
            }
            coupleAnomaliePriorite.put("0", "Opération OV manuelle");
            numAnomaliesList.clear();
            numAnomaliesList.addAll(numAnomaliesSet);
            dbTools = new DataBaseTools(Configuration.puAnomalies);
            List<Ticket> ticketAnomaliesList = null;
            List<Ticket> ticketAnomaliesGlobalList = new ArrayList<Ticket>();
            int cmp = 0;
            StringBuilder querySb;
            while (cmp < numAnomaliesList.size()) {
                querySb = new StringBuilder();
                querySb.append("SELECT t FROM Ticket t WHERE t.id IN (");
                for (int i = cmp; i < cmp + 300; i++) {
                    if (i < numAnomaliesList.size()) {
                        if ((i < cmp + 299) && (i < numAnomaliesList.size() - 1)) {
                            querySb.append(numAnomaliesList.get(i));
                            querySb.append(", ");
                        } else {
                            querySb.append(numAnomaliesList.get(i));
                            querySb.append(" )");
                        }
                    }
                }
                ticketAnomaliesList = new DataBaseTracGenericRequests<Ticket>().executeQueryRequest(dbTools, querySb, "Q");
                ticketAnomaliesGlobalList.addAll(ticketAnomaliesList);
                cmp += 300;
            }
            dbTools.closeRessources();
            for (Ticket ticketAnomalie : ticketAnomaliesGlobalList) {
                coupleAnomaliePriorite.put(ticketAnomalie.getId().toString(), ticketAnomalie.getPriority());
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return coupleAnomaliePriorite;
    }
    
    public static List<TicketCustom> getHotfixAharmoniserUpgrade(List<Integer> ticketList, int... iterations) {
        DataBaseTools dbTools = new DataBaseTools(Configuration.puLivraisons);
        List<TicketCustom> ticketHarmList = null;
        try {
            Set<Integer> numTicketSet = new TreeSet<Integer>();
            for (Integer numticket : ticketList) {
                if (numticket > 0) {
                     numTicketSet.add(numticket);
                }
            }
            ticketList.clear();
            ticketList.addAll(numTicketSet);
            
            int cmp = 0;
            StringBuilder querySb;
            while (cmp < ticketList.size()) {
                querySb = new StringBuilder();
                querySb.append("SELECT t FROM TicketCustom t WHERE t.ticket IN (");
                for (int i = cmp; i < cmp + 300; i++) {
                    if (i < ticketList.size()) {
                        if ((i < cmp + 299) && (i < ticketList.size() - 1)) {
                            querySb.append(ticketList.get(i));
                            querySb.append(", ");
                        } else {
                            querySb.append(ticketList.get(i));
                            querySb.append(" )");
                        }
                    }
                }
                ticketHarmList = new DataBaseTracGenericRequests<TicketCustom>().executeQueryRequest(dbTools, querySb, "Q");
                cmp += 300;
            }
            dbTools.closeRessources();
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return ticketHarmList;
    }
}