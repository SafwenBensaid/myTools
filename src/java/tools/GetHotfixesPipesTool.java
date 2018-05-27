/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import entitiesMysql.Livraison;
import entitiesTrac.Ticket;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import javax.persistence.Query;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

/**
 *
 * @author 04486
 */
public class GetHotfixesPipesTool {

    public Map<String, List<Livraison>> getAllHotfixes(int... iterations) {
        Map<String, Map<String, String>> hotfixMap = null;
        try {
            hotfixMap = new ManipulationObjectsTool().getAllHotfixTicketsDetails();
        } catch (Exception ex) {
            ex.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(ex));
            return new HashMap<String, List<Livraison>>();
        }
        Map<String, List<Livraison>> mapLivraisons = triHotfixSelonType(hotfixMap);
        return mapLivraisons;
    }

    public Map<String, List<Livraison>> getAllHotfixesAharmoniserUpgrade(int... iterations) {
        Map<String, Map<String, String>> hotfixMap = null;
        try {
            hotfixMap = new ManipulationObjectsTool().getTicketsHotfixAHarmoniserUpgrade();
        } catch (Exception ex) {
            ex.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(ex));
            return new HashMap<String, List<Livraison>>();
        }
        Map<String, List<Livraison>> mapLivraisons = triHotfixSelonType(hotfixMap);
        return mapLivraisons;
    }

    private Map<Integer, Livraison> getLivraisonDetailsFromDataBase(Set<String> listeNumeroLivraison) {
        Map<Integer, Livraison> maplivraisonsDetails = new LinkedHashMap<Integer, Livraison>();
        if (!listeNumeroLivraison.isEmpty()) {
            DataBaseTools dbTools = null;
            try {
                dbTools = new DataBaseTools(Configuration.puOvTools);
                StringBuilder query = new StringBuilder("SELECT l FROM Livraison l WHERE l.numeroLivraison in(");
                int i = 0;
                for (String numLiv : listeNumeroLivraison) {
                    if (i < listeNumeroLivraison.size() - 1) {
                        query.append(numLiv);
                        query.append(", ");
                    } else {
                        query.append(numLiv);
                        query.append(" )");
                    }
                    i++;
                }
                Query q = dbTools.em.createQuery(query.toString());
                q.setHint(QueryHints.REFRESH, HintValues.TRUE);
                List<Livraison> listeLivraisons = (List<Livraison>) q.getResultList();
                for (Livraison livraison : listeLivraisons) {
                    maplivraisonsDetails.put(livraison.getNumeroLivraison(), livraison);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Tools.traiterException(Tools.getStackTrace(ex));
            } finally {
                dbTools.closeRessources();
            }
        }
        return maplivraisonsDetails;
    }

    private Map<String, List<Livraison>> triHotfixSelonType(Map<String, Map<String, String>> hotfixMap) {
        Map<String, List<Livraison>> mapLivraisons = new LinkedHashMap<String, List<Livraison>>();
        List<Livraison> listeOV_HF_NON_QUALIFIEE = new ArrayList<Livraison>();
        List<Livraison> listeOV_HF_QUALIFIEE = new ArrayList<Livraison>();
        List<Livraison> listeEXPLOITATION_HF_NON_DEPLOYEE = new ArrayList<Livraison>();
        List<Livraison> listeEXPLOITATION_HF_DEPLOYEE = new ArrayList<Livraison>();
        List<Livraison> liste_HF_HARM_UPGRADE = new ArrayList<Livraison>();
        Livraison livAux = null;
        Map<Integer, Livraison> maplivraisonsDetails = getLivraisonDetailsFromDataBase(hotfixMap.keySet());
        String contenuLivrables = null;
        for (Map.Entry<String, Map<String, String>> entry : hotfixMap.entrySet()) {
            if (maplivraisonsDetails.containsKey(Integer.parseInt(entry.getKey()))) {
                livAux = maplivraisonsDetails.get(Integer.parseInt(entry.getKey()));
            } else {
                Tools.showConsolLog(entry.getKey());
                livAux = new Livraison();
                livAux.setNumeroLivraison(Integer.parseInt(entry.getKey()));
                livAux.setNumeroAnomalie(Integer.parseInt(entry.getValue().get("ticket_origine")));
                livAux.setOwner(entry.getValue().get("owner"));
                livAux.setReporter(entry.getValue().get("reporter"));
                contenuLivrables = entry.getValue().get("contenu_des_livrables");
                if (contenuLivrables == null) {
                    contenuLivrables = "";
                }
                livAux.setContenuLivrables(contenuLivrables);
                livAux.setValide(false);
            }
            livAux.setType(entry.getValue().get("type"));
            try {
                SimpleDateFormat parseFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                Date dateDep = parseFormat.parse(entry.getValue().get("date_deploiement"));
                livAux.setDateDeploiement(dateDep);
            } catch (Exception ex) {
                Tools.showConsolLog("#" + livAux.getNumeroLivraison() + "$" + entry.getValue().get("date_deploiement") + "$" + "\n\n");
                if (livAux.getDateDeploiement() == null) {
                    livAux.setDateDeploiement(new Date());
                    ex.printStackTrace();
                    tools.Tools.traiterException("GetHotfixesPipesTool#" + livAux.getNumeroLivraison() + "$" + entry.getValue().get("date_deploiement") + "$" + "\n\n" + tools.Tools.getStackTrace(ex));
                }
            }
            try {
                SimpleDateFormat parseFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                Date dateEnvoiPourDep = parseFormat.parse(entry.getValue().get("date_envoi_pour_deploiement"));
                livAux.setDateEnvoiProd(dateEnvoiPourDep);
            } catch (Exception ex) {
                if (livAux.getDateEnvoiProd() == null) {
                    livAux.setDateEnvoiProd(new Date());
                    ex.printStackTrace();
                    tools.Tools.traiterException("GetHotfixesPipesTool#" + livAux.getNumeroLivraison() + "$" + entry.getValue().get("date_envoi_pour_deploiement") + "$" + "\n\n" + tools.Tools.getStackTrace(ex));
                }
            }
            if (livAux.getMessageTrac() == null) {
                livAux.setMessageTrac("");
            }
            DataBaseTools dbTools = null;
            try {
                dbTools = new DataBaseTools(Configuration.puOvTools);
                dbTools.update(livAux);
            } catch (Exception ex) {
                ex.printStackTrace();
                Tools.traiterException(Tools.getStackTrace(ex));
            } finally {
                dbTools.closeRessources();
            }


            if (entry.getValue().get("etat").equals("OV_HF_NON_QUALIFIEE")) {
                listeOV_HF_NON_QUALIFIEE.add(livAux);
            } else if (entry.getValue().get("etat").equals("OV_HF_QUALIFIEE")) {
                listeOV_HF_QUALIFIEE.add(livAux);
            } else if (entry.getValue().get("etat").equals("EXPLOITATION_HF_NON_DEPLOYEE")) {
                listeEXPLOITATION_HF_NON_DEPLOYEE.add(livAux);
            } else if (entry.getValue().get("etat").equals("EXPLOITATION_HF_DEPLOYEE")) {
                listeEXPLOITATION_HF_DEPLOYEE.add(livAux);
            } else if (entry.getValue().get("etat").equals("HARMONISATION_UPGRADE")) {
                liste_HF_HARM_UPGRADE.add(livAux);
            }
        }
        mapLivraisons.put("OV_HF_NON_QUALIFIEE", listeOV_HF_NON_QUALIFIEE);
        mapLivraisons.put("OV_HF_QUALIFIEE", listeOV_HF_QUALIFIEE);
        mapLivraisons.put("EXPLOITATION_HF_NON_DEPLOYEE", listeEXPLOITATION_HF_NON_DEPLOYEE);
        mapLivraisons.put("EXPLOITATION_HF_DEPLOYEE", listeEXPLOITATION_HF_DEPLOYEE);
        mapLivraisons.put("HARMONISATION_UPGRADE", liste_HF_HARM_UPGRADE);
        return mapLivraisons;
    }

    //ConvertTimeTracToJavaDate
    public Date ConvertTimeTracToJavaDate(long TimeMicroSecond) {
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        formatter.setTimeZone(TimeZone.getTimeZone("Africa/Tunis"));
        String dateOuvertureMillisString = Long.toString(TimeMicroSecond).substring(0, 13);
        long dateOuvertureMillisLong = Long.parseLong(dateOuvertureMillisString);
        String dateOuvertureDateString = formatter.format(new Date(dateOuvertureMillisLong));

        //convertir la date en JavaDate
        Date dateOuvertureJavaDate = new Date();
        try {
            dateOuvertureJavaDate = formatter.parse(dateOuvertureDateString);
        } catch (ParseException ex) {
            tools.Tools.traiterException("Probleme formattage date " + tools.Tools.getStackTrace(ex));
        }
        return dateOuvertureJavaDate;
    }
    //Calculer la diffÃ©rence entre deux Date

    public static Map<TimeUnit, Long> compareDate(Date date1, Date date2) {
        long diffInMillies = date2.getTime() - date1.getTime();
        List<TimeUnit> units = new ArrayList<>(EnumSet.allOf(TimeUnit.class));
        Collections.reverse(units);

        Map<TimeUnit, Long> result = new LinkedHashMap<>();
        long milliesRest = diffInMillies;
        for (TimeUnit unit : units) {
            long diff = unit.convert(milliesRest, TimeUnit.MILLISECONDS);
            long diffInMilliesForUnit = unit.toMillis(diff);
            milliesRest = milliesRest - diffInMilliesForUnit;
            result.put(unit, diff);
        }
        result.remove(TimeUnit.SECONDS);
        result.remove(TimeUnit.MILLISECONDS);
        result.remove(TimeUnit.MICROSECONDS);
        result.remove(TimeUnit.NANOSECONDS);
        return result;
    }

    public Map<String, List<Map<String, Object>>> triCobsSelonType(Map<Integer, Map<String, Object>> cobTicketsMap) {
        Map<String, List<Map<String, Object>>> mapCobsTriee = new HashMap<>();
        Map<Integer, Long> unsortedTicketMap = new HashMap<>();
        Map<Integer, Long> sortedTicketMap = new HashMap<>();
        for (Map.Entry<Integer, Map<String, Object>> entry : cobTicketsMap.entrySet()) {
            Ticket tickAnomalie = (Ticket) entry.getValue().get("Ticket");
            Long DateCreation = tickAnomalie.getTime();
            unsortedTicketMap.put(tickAnomalie.getId(), DateCreation);
        }
        sortedTicketMap = Tools.sortByComparator(unsortedTicketMap);
        Map<Integer, Map<String, Object>> cobTicketsMapSorted = new LinkedHashMap<Integer, Map<String, Object>>();
        for (Integer idTicketAnomalie : sortedTicketMap.keySet()) {
            cobTicketsMapSorted.put(idTicketAnomalie, cobTicketsMap.get(idTicketAnomalie));
        }
        for (Map.Entry<Integer, Map<String, Object>> entry : cobTicketsMapSorted.entrySet()) {
            List<Map<String, Object>> listeAux = null;
            Ticket tickAnomalie = (Ticket) entry.getValue().get("Ticket");
            Date dateOuvertureJavaDate = ConvertTimeTracToJavaDate(tickAnomalie.getTime());
            //Calculer la difference entre la date d'ouverture et la date actuelle
            Map<TimeUnit, Long> ticketAge = compareDate(dateOuvertureJavaDate, new Date());
            if (ticketAge.get(TimeUnit.DAYS) <= 30) {
                String cle = null;
                if (entry.getValue().get("action").equals("A DEFINIR") && !tickAnomalie.getStatus().equals("closed")) {
                    cle = "DEMANDE_COB";
                } else if (entry.getValue().get("action").equals("COB PRET POUR APPLICATION")) {
                    cle = "COB_PRET";
                } else if (entry.getValue().get("action").equals("COB ANNULEE") || entry.getValue().get("action").equals("COB REPORTEE")) {
                    cle = "COB_ANNULE_REPORTE";
                } else if (entry.getValue().get("action").equals("COB APPLIQUE")) {
                    cle = "COB_APPLIQUE";
                }
                listeAux = mapCobsTriee.get(cle);
                if (listeAux == null) {
                    listeAux = new ArrayList<Map<String, Object>>();
                }
                listeAux.add(entry.getValue());
                mapCobsTriee.put(cle, listeAux);
            }
        }
        return mapCobsTriee;
    }

    public Map<String, List<Map<String, Object>>> triDemandesMetierSelonStatus(Map<Integer, Map<String, Object>> demandesMetierDetails) {
        //SORT Projects map by creation time
        Map<Integer, Long> unsortedProjectsMap = new HashMap<Integer, Long>();
        for (Integer idTicket : demandesMetierDetails.keySet()) {
            Long dateOuverture = ((Ticket) demandesMetierDetails.get(idTicket).get("Ticket")).getTime();
            unsortedProjectsMap.put(idTicket, dateOuverture);
        }
        Map<Integer, Long> sortedProjectsMap = Tools.sortByComparator(unsortedProjectsMap);
        Map<Integer, Map<String, Object>> ProjectsSortedMap = new LinkedHashMap();
        for (Integer idTicketAnomalie : sortedProjectsMap.keySet()) {
            ProjectsSortedMap.put(idTicketAnomalie, demandesMetierDetails.get(idTicketAnomalie));
        }
        Map<String, List<Map<String, Object>>> mapDemandesMetierTriee = new HashMap<String, List<Map<String, Object>>>();
        for (Map.Entry<Integer, Map<String, Object>> entry : ProjectsSortedMap.entrySet()) {
            List<Map<String, Object>> listeAux = null;
            Ticket ticket = (Ticket) entry.getValue().get("Ticket");
            String cle = ticket.getStatus();
            listeAux = mapDemandesMetierTriee.get(cle);
            if (listeAux == null) {
                listeAux = new ArrayList<Map<String, Object>>();
            }
            listeAux.add(entry.getValue());
            mapDemandesMetierTriee.put(cle, listeAux);
        }
        return mapDemandesMetierTriee;
    }
}
