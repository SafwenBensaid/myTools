/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataBaseTracRequests;

import dto.CoupleDTO;
import dto.TripleDTO;
import entitiesTrac.Ticket;
import entitiesTrac.TicketChange;
import entitiesTrac.TicketCustom;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;
import tools.DataBaseTools;
import tools.Tools;
import tools.UpdateTicketsTools;

/**
 *
 * @author 04486
 */
public class DataBaseTracGenericRequests<T> {

    public static String persistObjectListIntoTheDataBase(List<Object> objectsListToBeInsertedOnTheDataBaseTrac, DataBaseTools dbTools, int... iterations) {
        String resultat = "OK";
        try {
            dbTools.updateObjectList(objectsListToBeInsertedOnTheDataBaseTrac);
        } catch (Exception ex) {
            if (dbTools.em.getTransaction().isActive()) {
                dbTools.em.getTransaction().rollback();
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex1) {
                ex.printStackTrace();
            }
            if (iterations.length == 0) {
                dbTools.closeRessources();
                persistObjectListIntoTheDataBase(objectsListToBeInsertedOnTheDataBaseTrac, dbTools, 0);
            } else if (iterations[0] < 10) {
                dbTools.closeRessources();
                persistObjectListIntoTheDataBase(objectsListToBeInsertedOnTheDataBaseTrac, dbTools, iterations[0] + 1);
            } else {
                dbTools.closeRessources();
                resultat = "KO";
                ex.printStackTrace();
                Tools.traiterException(Tools.getStackTrace(ex));
            }
        }
        return resultat;
    }

    public List<T> executeQueryRequest(DataBaseTools dbTools, StringBuilder querySb, String typeQuery, int... iterations) {
        Query q = null;
        List<T> resultList = new LinkedList<>();
        try {
            if (!dbTools.em.getTransaction().isActive()) {
                dbTools.em.getTransaction().begin();
            }
            if (typeQuery.equals("NQ")) {
                q = dbTools.em.createNativeQuery(querySb.toString());
            } else if (typeQuery.equals("Q")) {
                q = dbTools.em.createQuery(querySb.toString());
                q.setHint(QueryHints.REFRESH, HintValues.TRUE);
            } else if (typeQuery.equals("NVQ_UPDATE")) {
                q = dbTools.em.createNativeQuery(querySb.toString());
                q.executeUpdate();
                dbTools.em.getTransaction().commit();
                return null;
            } else if (typeQuery.equals("NVQ_SELECT")) {
                q = dbTools.em.createNativeQuery(querySb.toString());
            }

            resultList = (List<T>) q.getResultList();
        } catch (Exception exep) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex1) {
                ex1.printStackTrace();
                tools.Tools.traiterException(tools.Tools.getStackTrace(ex1));
            }
            if (iterations.length == 0) {
                return executeQueryRequest(dbTools, querySb, typeQuery, 0);
            } else {
                if (iterations[0] < 10) {
                    return executeQueryRequest(dbTools, querySb, typeQuery, iterations[0] + 1);
                } else {
                    exep.printStackTrace();
                    tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
                }
            }
        }
        return resultList;
    }

    public static Ticket getTicketById(DataBaseTools dbTools, int numTicket, int... iterations) {
        Ticket ticket = null;
        try {
            ticket = dbTools.em.find(Ticket.class, numTicket);
        } catch (Exception exep) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex1) {
                exep.printStackTrace();
            }
            if (iterations.length == 0) {
                return getTicketById(dbTools, numTicket, 0);
            } else {
                if (iterations[0] < 10) {
                    return getTicketById(dbTools, numTicket, iterations[0] + 1);
                } else {
                    exep.printStackTrace();
                    tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
                }
            }
        }
        return ticket;
    }

    public static String updateTicketTracGeneral(String pu, int numTicket, String connectedUser, String messageTrac, String newOwner, String newPriority, String newStatus, String newVersion, List<CoupleDTO> customFieldList) {
        String resultat = "OK";
        List<Object> objectsListToBeInsertedOnTheDataBaseTracLivraisons = new ArrayList<Object>();
        DataBaseTools dbTools;
        try {
            dbTools = new DataBaseTools(pu);
            long timeTrac = UpdateTicketsTools.generateTracDateNow();
            Ticket ticket = getTicketById(dbTools, numTicket);
            UpdateTicketsTools utt = new UpdateTicketsTools();
            //INSERT INTO ticket_change("ticket", "time", "author", "field", "oldvalue", "newvalue")
            if (messageTrac != null) {
                TicketChange ticketChange = new TicketChange();
                ticketChange.setTicket(ticket.getId());
                ticketChange.setTime(timeTrac);
                ticketChange.setAuthor(connectedUser);
                ticketChange.setField("comment");

                StringBuilder querySb = new StringBuilder("SELECT (count(distinct(time))+1) FROM ticket_change WHERE ticket = " + numTicket);
                Long oldValue = new DataBaseTracGenericRequests<Long>().executeQueryRequest(dbTools, querySb, "NQ").get(0);
                ticketChange.setOldvalue(oldValue.toString());

                ticketChange.setNewvalue(messageTrac);
                utt.updateTicket(ticketChange, ticket.getId(), connectedUser, "comment", oldValue.toString(), messageTrac, timeTrac);
            }
            if (newOwner != null) {
                String oldOwner = ticket.getOwner();
                ticket.setOwner(newOwner);
                utt.updateTicket(ticket, ticket.getId(), connectedUser, "owner", oldOwner, newOwner, timeTrac);
            }
            if (newPriority != null) {
                String oldPriority = ticket.getPriority();
                ticket.setPriority(newPriority);
                utt.updateTicket(ticket, ticket.getId(), connectedUser, "priority", oldPriority, newPriority, timeTrac);
            }
            if (newStatus != null) {
                String oldStatus = ticket.getStatus();
                ticket.setStatus(newStatus);
                utt.updateTicket(ticket, ticket.getId(), connectedUser, "status", oldStatus, newStatus, timeTrac);
            }
            if (newVersion != null) {
                String oldVersion = ticket.getVersion();
                ticket.setVersion(newVersion);
                utt.updateTicket(ticket, ticket.getId(), connectedUser, "version", oldVersion, newVersion, timeTrac);
            }
            //définir les ticket custom dans la base
            if (customFieldList != null) {
                for (CoupleDTO cleval : customFieldList) {
                    TicketCustom inter = Tools.createTicketCustom(ticket, cleval.getValeur1(), cleval.getValeur2());
                    utt.updateTicket(inter, inter.getTicket(), connectedUser, cleval.getValeur1(), "", cleval.getValeur2(), timeTrac);
                }
            }
            objectsListToBeInsertedOnTheDataBaseTracLivraisons.addAll(utt.objectsListToBeMergedOnTheDataBaseTrac);
            persistObjectListIntoTheDataBase(objectsListToBeInsertedOnTheDataBaseTracLivraisons, dbTools);
            dbTools.closeRessources();
        } catch (Exception exp) {
            resultat = "KO";
            exp.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exp));
        }
        return resultat;
    }

    public static String updateTicketTracGenerique(String pu, int numTicket, String connectedUser, String messageTrac, List<TripleDTO> fieldList, List<TripleDTO> customFieldList) {
        String resultat = "OK";
        Boolean authorisedUpdate;
        String field, oldValue, newValue = "";
        List<Object> objectsListToBeInsertedOnTheDataBaseTracLivraisons = new ArrayList<Object>();
        DataBaseTools dbTools;

        try {
            dbTools = new DataBaseTools(pu);
            long timeTrac = UpdateTicketsTools.generateTracDateNow();
            Ticket ticket = getTicketById(dbTools, numTicket);
            UpdateTicketsTools utt = new UpdateTicketsTools();
            //INSERT INTO ticket_change("ticket", "time", "author", "field", "oldvalue", "newvalue")
            if (messageTrac != null) {
                TicketChange ticketChange = new TicketChange();
                ticketChange.setTicket(ticket.getId());
                ticketChange.setTime(timeTrac);
                ticketChange.setAuthor(connectedUser);
                ticketChange.setField("comment");

                StringBuilder querySb = new StringBuilder("SELECT (count(distinct(time))+1) FROM ticket_change WHERE ticket = " + numTicket);
                Long countChanges = new DataBaseTracGenericRequests<Long>().executeQueryRequest(dbTools, querySb, "NQ").get(0);
                ticketChange.setOldvalue(countChanges.toString());

                ticketChange.setNewvalue(messageTrac);
                utt.updateTicket(ticketChange, ticket.getId(), connectedUser, "comment", countChanges.toString(), messageTrac, timeTrac);
            }
            //update ticket dans la base
            if (fieldList != null) {
                for (TripleDTO fieldDetails : fieldList) {
                    authorisedUpdate = true;
                    field = fieldDetails.getValeur1();
                    oldValue = fieldDetails.getValeur2();
                    newValue = fieldDetails.getValeur3();
                    switch (field) {
                        case "summary":
                            ticket.setSummary(newValue);
                            break;
                        case "description":
                            ticket.setDescription(newValue);
                            break;
                        case "type":
                            ticket.setType(newValue);
                            break;
                        case "priority":
                            ticket.setPriority(newValue);
                            break;
                        case "milestone":
                            ticket.setMilestone(newValue);
                            break;
                        case "component":
                            ticket.setComponent(newValue);
                            break;
                        case "status":
                            ticket.setStatus(newValue);
                            break;
                        case "owner":
                            oldValue = ticket.getOwner();
                            ticket.setOwner(newValue);
                            break;
                        case "version":
                            oldValue = ticket.getVersion();
                            ticket.setVersion(newValue);
                            break;
                        default:
                            authorisedUpdate = false;
                            break;
                    }
                    if (authorisedUpdate) {
                        utt.updateTicket(ticket, ticket.getId(), connectedUser, field, oldValue, newValue, timeTrac);
                    }
                }
            }
            //update ticket custom dans la base
            if (customFieldList != null) {
                for (TripleDTO customFieldDetails : customFieldList) {
                    field = customFieldDetails.getValeur1();
                    oldValue = customFieldDetails.getValeur2();
                    newValue = customFieldDetails.getValeur3();
                    TicketCustom ticketCustom = Tools.createTicketCustom(ticket, field, newValue);
                    utt.updateTicket(ticketCustom, ticketCustom.getTicket(), connectedUser, field, oldValue, newValue, timeTrac);
                }
            }
            objectsListToBeInsertedOnTheDataBaseTracLivraisons.addAll(utt.objectsListToBeMergedOnTheDataBaseTrac);
            persistObjectListIntoTheDataBase(objectsListToBeInsertedOnTheDataBaseTracLivraisons, dbTools);
            dbTools.closeRessources();
        } catch (Exception exp) {
            resultat = "KO";
            exp.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exp));
        }
        return resultat;
    }

    public List<T> getList_TYPE_OfnamedQuery(String pu, String namedQuery, Map<String, Object> paramMap, int... iterations) {
        List<T> resultList = null;
        DataBaseTools dbTools = null;
        try {
            dbTools = new DataBaseTools(pu);
            Query q = dbTools.em.createNamedQuery(namedQuery);
            if (paramMap != null) {
                for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
                    q.setParameter(entry.getKey(), entry.getValue());
                }
            }
            q.setHint(QueryHints.REFRESH, HintValues.TRUE);
            resultList = (List<T>) q.getResultList();
            dbTools.closeRessources();
        } catch (Exception exep) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex1) {
                exep.printStackTrace();
            }
            if (iterations.length == 0) {
                dbTools.closeRessources();
                return getList_TYPE_OfnamedQuery(pu, namedQuery, paramMap, 0);
            } else {
                if (iterations[0] < 10) {
                    dbTools.closeRessources();
                    return getList_TYPE_OfnamedQuery(pu, namedQuery, paramMap, iterations[0] + 1);
                } else {
                    dbTools.closeRessources();
                    exep.printStackTrace();
                    tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
                }
            }
        }
        return resultList;
    }
}
