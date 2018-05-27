/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataBaseTracRequests;

import dto.DetailsLivraisonDTO;
import dto.CoupleDTO;
import entitiesMysql.*;
import entitiesTrac.Ticket;
import entitiesTrac.TicketCustom;
import java.util.*;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;
import tools.Configuration;
import tools.DataBaseTools;
import tools.ManipulationObjectsTool;
import tools.Tools;

/**
 *
 * @author 04486
 */
public class DataBaseTracRequests {

    static Map<String, String> livraisonAnomalie = new HashMap<String, String>();
    static Map<String, String> anomalieIdPriority = new HashMap<String, String>();
    /*
     public static void insertOrUpdateLivraison(Livraison livraison, DataBaseTools dbTools) {
     dbTools.update(livraison);
     }
     */

    public static void updateUserPassword(String envName, String userName, String newPassword, DataBaseTools dbTools) {
        try {
            Query q = dbTools.em.createNamedQuery("UsersHasEnvironnement.findByUsersLoginAndEnvironnementNom");
            q.setParameter("usersLogin", userName);
            q.setParameter("environnementNom", envName);
            q.setHint(QueryHints.REFRESH, HintValues.TRUE);
            UsersHasEnvironnement userHaveEnv = (UsersHasEnvironnement) q.getSingleResult();
            userHaveEnv.setBrowsrPassword(newPassword);
            dbTools.StoreObjectIntoDataBase(userHaveEnv);
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }

    public static void relancerHotfixSurINV(Livraison livraison, String connectedUser) {
        List<Object> objectsListToBeInsertedOnTheDataBaseTracLivraisons = new ArrayList<Object>();
        try {
            String pu = Configuration.puLivraisons;
            int numTicketLivraison = livraison.getNumeroLivraison();
            String messageTrac = livraison.getMessageTrac();
            String newOwner = "riadh anouar ben dakhlia";
            String newPriority = "PRET POUR DEPLOIEMENT";
            String newStatus = "new";
            String newVersion = null;
            List<CoupleDTO> customFieldList = null;
            if (messageTrac.isEmpty()) {
                messageTrac = "== A redéployer ce ticket sur INV, voir le dernier message de l'équipe OV ci-dessus ==";
            } else {
                messageTrac = messageTrac.replaceAll("PROD", "INV");
            }
            DataBaseTracGenericRequests.updateTicketTracGeneral(pu, numTicketLivraison, connectedUser, messageTrac, newOwner, newPriority, newStatus, newVersion, customFieldList);
        } catch (Exception exp) {
            exp.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exp));
        }
    }

    public static void deleteGroup(DataBaseTools dbTools, String NomGroupe) {
        try {
            Query q = dbTools.em.createNamedQuery("Groupe.findByNom");
            q.setParameter("nom", NomGroupe);
            q.setHint(QueryHints.REFRESH, HintValues.TRUE);
            Groupe g = (Groupe) q.getSingleResult();
            dbTools.remove(g);
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }

    public static String analyseTicketAnomalie(int numTicketAnomalie, List<String> envReleaseList, List<String> envProjetList, String action, int... iterations) {

        String resultat = null;
        Map<String, String> mapAnalyseTicket = null;
        //get connected user
        String connectedUser = Tools.getConnectedLogin();
        try {
            List<Integer> listeIdTicketsAnomaliesUnique = new ArrayList<Integer>();
            listeIdTicketsAnomaliesUnique.add(numTicketAnomalie);
            String[] cles = new String[]{"t_liv", "mode_traitement"};

            Map<Integer, Map<String, Object>> globalResultMap = AppelRequetes.getTicketCustomByTicketIdAndNames(listeIdTicketsAnomaliesUnique, Configuration.puAnomalies, Configuration.tracAnomalies, cles);

            String cloture = null;
            String numLivraisons[];
            try {
                cloture = "1";
                String t_liv = (String) globalResultMap.get(numTicketAnomalie).get("t_liv");
                String prioriteAnomalie = ((Ticket) globalResultMap.get(numTicketAnomalie).get("Ticket")).getPriority();
                String versionAnomalie = ((Ticket) globalResultMap.get(numTicketAnomalie).get("Ticket")).getVersion();
                String mode_traitement = (String) globalResultMap.get(numTicketAnomalie).get("mode_traitement");
                if (action.equals("analyse") || action.equals("analyseEtCloture")) {
                    if (t_liv.equals("")) {
                        if (versionAnomalie.equals("ACTION A CHAUD POUR MISE EN PROD") && mode_traitement.equals("PALLIATIF")) {
                            mapAnalyseTicket = remplirMap("Ticket A CHAUD PALLIATIF", "", "", cloture);
                        } else {
                            mapAnalyseTicket = remplirMap("En attente de création d'un ticket de livraison", "", "", cloture);
                        }
                    } else {
                        numLivraisons = t_liv.trim().split("-");
                        try {
                            mapAnalyseTicket = analyseTicketLivraison(Integer.parseInt(numLivraisons[numLivraisons.length - 1]), envReleaseList, envProjetList);
                            if (prioriteAnomalie.equals("RETOURNEE")) {
                                mapAnalyseTicket.put("MESSAGE", "Ticket Retourné: ".concat(mapAnalyseTicket.get("MESSAGE")));
                                mapAnalyseTicket.put("CLOTURE", "0");
                            }
                        } catch (Exception ex) {
                            mapAnalyseTicket = remplirMap("Veuillez vérifier le champ t_liv de l'anomalie", "", "", "0");
                        }
                    }
                    resultat = Tools.objectToJsonString(mapAnalyseTicket);
                }
                if (action.equals("cloture") || (action.equals("analyseEtCloture") && mapAnalyseTicket.get("CLOTURE").equals("1"))) {
                    //Tester si anomalie déjà cloturée
                    String status = ((Ticket) globalResultMap.get(numTicketAnomalie).get("Ticket")).getStatus();
                    if (!status.equals("closed")) {
                        //Champs modifiés
                        String messageTrac = "== Ticket Cloturé ==";
                        String newOwner = null;
                        String newPriority = "ANNULEE";
                        String newStatus = "closed";
                        String newVersion = null;
                        List<CoupleDTO> customFieldList = null;

                        //Cloture Anomalie        
                        DataBaseTracGenericRequests.updateTicketTracGeneral(Configuration.puAnomalies, numTicketAnomalie, connectedUser, messageTrac, newOwner, newPriority, newStatus, newVersion, customFieldList);
                        //Cloture Livraison
                        if (!t_liv.equals("")) {
                            numLivraisons = t_liv.trim().split("-");
                            try {
                                DataBaseTracGenericRequests.updateTicketTracGeneral(Configuration.puLivraisons, Integer.parseInt(numLivraisons[numLivraisons.length - 1]), connectedUser, messageTrac, newOwner, newPriority, newStatus, newVersion, customFieldList);
                            } catch (Exception ex) {
                                resultat = "Veuillez vérifier le champ t_liv de l'anomalie";
                            }
                        }
                        resultat = "Clôture effectuée avec succès";
                    } else {
                        resultat = "Le ticket est déjà cloturé";
                    }
                } else if (action.equals("analyseEtCloture") && mapAnalyseTicket.get("CLOTURE").equals("0")) {
                    resultat = "Ce ticket ne doit pas être clôturé. \n Pour plus d'informations veuillez contacter l'équipe OV. \n ".concat(mapAnalyseTicket.get("MESSAGE"));
                }
            } catch (Exception ex) {
                mapAnalyseTicket = remplirMap("Ticket d'anomalie inexistant", "", "", "0");
                resultat = Tools.objectToJsonString(mapAnalyseTicket);
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return resultat;
    }

    public static Map<String, String> analyseTicketLivraison(int numTicketLivraison, List<String> envReleaseList, List<String> envProjetList, int... iterations) {
        Map<String, String> mapAnalyseTicket = null;
        Map<Integer, Map<String, Object>> ticketMap = null;
        try {
            String message = null;
            String titre = null;
            String environnements = null;
            String biatRef = null;
            String cloture = null;
            String[] cles = new String[]{"biatref"};
            List<Integer> listTicketsLivraison = new ArrayList<Integer>();
            listTicketsLivraison.add(numTicketLivraison);
            ticketMap = AppelRequetes.getTicketCustomByTicketIdAndNames(listTicketsLivraison, Configuration.puLivraisons, Configuration.tracLivraisons, cles);
            Ticket ticketLivraison = (Ticket) ticketMap.get(numTicketLivraison).get("Ticket");
            biatRef = (String) ticketMap.get(numTicketLivraison).get("biatref");

            String type = ticketLivraison.getType();
            String priority = ticketLivraison.getPriority();
            cloture = "1";
            if (priority.equals("NON ACCEPTEE")) {
                mapAnalyseTicket = remplirMap("Ticket de livraison non accepté", "", "", cloture);
            } else if (priority.equals("REMPLACEE")) {
                cloture = "0";
                mapAnalyseTicket = remplirMap("Ticket de livraison remplacé", "", "", cloture);
            } else if (priority.equals("MISE EN ATTENTE")) {
                mapAnalyseTicket = remplirMap("Ticket de livraison mis en attente", "", "", cloture);
            } else if (priority.equals("ANNULEE")) {
                mapAnalyseTicket = remplirMap("Ticket de livraison annulé", "", "", cloture);
            } else if (priority.equals("LIVRAISON CONFIRMEE")) {
                mapAnalyseTicket = remplirMap("Le ticket est en cours de développement", "", "developpement", cloture);
            } else if (priority.equals("PROBLEME DE PACKAGING") || priority.equals("OBJET LIVREE")) {
                if (priority.equals("PROBLEME DE PACKAGING")) {
                    message = "Le ticket est en problème de packaging, il est à la charge des développeurs";
                    titre = "developpement";
                } else {
                    message = "Le ticket est en cours de packaging";
                    titre = "packaging";
                }
                if (type.equals("QUALIFICATION") || type.equals("CERTIFICATION")) {
                    environnements = "DEVR";
                } else if (type.equals("HOT FIXE TEST") || type.equals("ACTION A CHAUD TEST")) {
                    environnements = "DEVH";
                } else if (type.equals("QUALIFICATION_PROJET")) {
                    environnements = "DEV2";
                }
                mapAnalyseTicket = remplirMap(message, environnements, titre, cloture);
            } else if (priority.equals("PROBLEME DE DEPLOIEMENT") || priority.equals("PRET POUR DEPLOIEMENT")) {
                cloture = "0";
                if (priority.equals("PROBLEME DE DEPLOIEMENT")) {
                    message = "Le ticket est en problème de déploiement, il est à la charge de OV";
                    titre = "packaging";
                } else {
                    message = "Le ticket est en cours d'intégration";
                    titre = "integration";
                }
                if (type.equals("QUALIFICATION") || type.equals("CERTIFICATION")) {
                    environnements = "DEVR;ASS";
                } else if (type.equals("HOT FIXE TEST") || type.equals("ACTION A CHAUD TEST")) {
                    environnements = "DEVH;ASS";
                } else if (type.equals("QUALIFICATION_PROJET")) {
                    environnements = "DEV2;ASS2";
                }
                mapAnalyseTicket = remplirMap(message, environnements, titre, cloture);
            } else if (priority.equals("DEPLOYEE")) {
                cloture = "0";
                if (type.equals("QUALIFICATION") || type.equals("CERTIFICATION")) {
                    environnements = "DEVR;ASS";
                    for (String env : envReleaseList) {
                        environnements += ";" + env;
                    }
                    message = "Le ticket a été déployé sur les environnements du circuit Release";
                } else if (type.equals("HOT FIXE TEST") || type.equals("ACTION A CHAUD TEST")) {
                    cloture = "1";
                    environnements = "DEVH;ASS;INV";
                    message = "Le ticket a été déployé sur les environnements du circuit Hotfix";
                } else if (type.equals("QUALIFICATION_PROJET")) {
                    environnements = "DEV2;ASS2";
                    for (String env : envProjetList) {
                        environnements += ";" + env;
                    }
                    message = "Le ticket a été déployé sur les environnements du circuit Projet";
                } else if (type.equals("HOT FIXE PROD") || type.equals("ACTION A CHAUD PROD")) {
                    if (biatRef.equals("1")) {
                        environnements = "TOUS";
                        message = "Le ticket a été déployé sur prod et harmonisé sur tous les environnements";
                    } else {
                        environnements = "DEVH;ASS;INV";
                        message = "Le ticket a été déployé sur prod mais il n'est pas encore harmonisé";
                    }
                } else if (type.equals("RELEASE PROD")) {
                    environnements = "TOUS";
                    message = "Le ticket a été déployé sur prod et harmonisé sur tous les environnements";
                }
                titre = "";
                mapAnalyseTicket = remplirMap(message, environnements, titre, cloture);
            } else {
                mapAnalyseTicket = remplirMap("Ticket de livraison en problème", "", "", cloture);
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
            mapAnalyseTicket = remplirMap("Ticket de livraison inexistant", "", "", "0");
        }
        return mapAnalyseTicket;
    }

    public static Map<String, String> remplirMap(String message, String Environnements, String titre, String cloture) {
        Map<String, String> mapAnalyseTicket = new HashMap<String, String>();
        try {
            mapAnalyseTicket.put("MESSAGE", message);
            mapAnalyseTicket.put("ENVIRONNEMENTS", Environnements);
            mapAnalyseTicket.put("TITRE", titre);
            mapAnalyseTicket.put("CLOTURE", cloture);
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return mapAnalyseTicket;
    }

    public static void createNewGroupe(DataBaseTools dbTools, String groupName) {
        try {
            Groupe groupe = new Groupe(groupName);
            dbTools.persist(groupe);
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }

    public static void miseAJourUserHasGroup(DataBaseTools dbTools, String userlogin, List<String> checkedBoxesList) {
        //suppression des groupes qui ne figurent pas dans la liste cible
        try {
            Query q = dbTools.em.createNamedQuery("UsersHasGroupe.findByUsersLogin");
            q.setParameter("usersLogin", userlogin);
            q.setHint(QueryHints.REFRESH, HintValues.TRUE);
            List<UsersHasGroupe> listeAllUsersHasGroup = (List<UsersHasGroupe>) q.getResultList();

            for (UsersHasGroupe tobeDel : listeAllUsersHasGroup) {
                if (!checkedBoxesList.contains(tobeDel.getGroupe().getNom())) {
                    dbTools.em.remove(tobeDel);
                }
            }
            dbTools.em.getTransaction().commit();
            if (!dbTools.em.getTransaction().isActive()) {
                dbTools.em.getTransaction().begin();


            }
            Users user = dbTools.em.find(Users.class, userlogin);
            List<UsersHasGroupe> listeUserGroupes = new ArrayList<UsersHasGroupe>();
            UsersHasGroupe userhasgroup = null;
            for (String groupName : checkedBoxesList) {
                userhasgroup = new UsersHasGroupe();
                userhasgroup.setDescription("");
                userhasgroup.setUsers(Configuration.usersMap.get(userlogin));
                userhasgroup.setGroupe(Configuration.groupMap.get(groupName.trim()));
                userhasgroup.setUsersHasGroupePK(new UsersHasGroupePK(userlogin, groupName));
                listeUserGroupes.add(userhasgroup);
            }
            /*
             for(UsersHasGroupe grf : listeGroupeHasFonctionnalites){
             dbTools.em.persist(grf);
             }*/

            user.getUsersHasGroupeList()
                    .clear();
            user.setUsersHasGroupeList(listeUserGroupes);

            dbTools.em.merge(user);

            dbTools.em.getTransaction()
                    .commit();
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }

    public static void miseAJourListeFonctionnalites(DataBaseTools dbTools, String groupName, List<String> checkedBoxesList) {
        //suppression des groupes qui ne figurent pas dans la liste cible
        try {
            Query q = dbTools.em.createNamedQuery("GroupeHasFonctionalite.findByGroupeNom");
            q.setParameter("groupeNom", groupName);
            q.setHint(QueryHints.REFRESH, HintValues.TRUE);
            List<GroupeHasFonctionalite> listeAllGroupeHasFonctionnalites = (List<GroupeHasFonctionalite>) q.getResultList();

            for (GroupeHasFonctionalite tobeDel : listeAllGroupeHasFonctionnalites) {
                if (!checkedBoxesList.contains(tobeDel.getFonctionalite().getName())) {
                    dbTools.em.remove(tobeDel);
                }
            }
            dbTools.em.getTransaction().commit();
            if (!dbTools.em.getTransaction().isActive()) {
                dbTools.em.getTransaction().begin();


            }
            Groupe groupe = dbTools.em.find(Groupe.class, groupName);
            List<GroupeHasFonctionalite> listeGroupeHasFonctionnalites = new ArrayList<GroupeHasFonctionalite>();
            GroupeHasFonctionalite grpHasFctAux = null;
            for (String fctName : checkedBoxesList) {
                grpHasFctAux = new GroupeHasFonctionalite();
                grpHasFctAux.setDescription("");
                grpHasFctAux.setFonctionalite(Configuration.allFonctionalitesMap.get(fctName.trim()));
                grpHasFctAux.setGroupe(Configuration.groupMap.get(groupName));
                grpHasFctAux.setGroupeHasFonctionalitePK(new GroupeHasFonctionalitePK(groupName, fctName));
                listeGroupeHasFonctionnalites.add(grpHasFctAux);
            }
            /*
             for(GroupeHasFonctionalite grf : listeGroupeHasFonctionnalites){
             dbTools.em.persist(grf);
             }*/

            groupe.getGroupeHasFonctionaliteList()
                    .clear();
            groupe.setGroupeHasFonctionaliteList(listeGroupeHasFonctionnalites);

            dbTools.em.merge(groupe);

            dbTools.em.getTransaction()
                    .commit();
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }

    public UsersHasEnvironnement findUserHasEnvByIdAndEnv(DataBaseTools dbTools, String id, String env) {
        try {
            Query q = dbTools.em.createNamedQuery("UsersHasEnvironnement.findByUserAndEnvironnementName").setParameter("usersLogin", id).setParameter("environnementNom", env);
            q.setHint(QueryHints.REFRESH, HintValues.TRUE);
            return (UsersHasEnvironnement) q.getSingleResult();
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
            return null;
        }
    }

    public void updateUserHasEnvironnement(DataBaseTools dbTools, String id, String bLogin, String bPass, String valeur1, String valeur2, String table, String environnementName) {
        try {
            //  Query q = em.createNativeQuery("UPDATE " + table.toLowerCase() + " SET " + bLogin + "='" + valeur + "' WHERE users_login='" + id +"' AND environnement_nom='"+environnementName+ "';");
            String requete = "UPDATE " + table.toLowerCase() + " SET " + bLogin + "='" + valeur1 + "', " + bPass + "='" + valeur2 + "' WHERE users_login='" + id + "' AND environnement_nom='" + environnementName + "';";
            Query q = dbTools.em.createNativeQuery(requete);
            int s = q.executeUpdate();
            dbTools.em.getTransaction().commit();
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }
    /*
     ****************************************************************************************************
     */
//dbRequests.updatefonctionnalite(dbTools, nom, "name", "description", "type", nom, description, type);

    public void updatefonctionnalite(DataBaseTools dbTools, String id, String description, String type) {
        try {
            Fonctionalite fonctionalite = dbTools.em.find(Fonctionalite.class, id);
            fonctionalite.setDescription(description);

            fonctionalite.setType(type);

            dbTools.em.merge(fonctionalite);

            dbTools.em.getTransaction()
                    .commit();
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }

    public void updateTypesRegle(DataBaseTools dbTools, String cle, String regle, String nature) {
        try {
            TypesRegle typesRegle = dbTools.em.find(TypesRegle.class, cle);
            typesRegle.setRegle(regle);

            typesRegle.setNature(nature);

            dbTools.em.merge(typesRegle);

            dbTools.em.getTransaction()
                    .commit();
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }
    /*
     ****************************************************************************************************
     */

    public static void miseAJourListeEnvironnements(DataBaseTools dbTools, String groupName, List<String> checkedBoxesList) {
        //suppression des groupes qui ne figurent pas dans la liste cible
        try {
            Query q = dbTools.em.createNamedQuery("GroupeHasEnvironnement.findByGroupeName");
            q.setParameter("groupeName", groupName);
            List<GroupeHasEnvironnement> listeAllGroupeHasEnvironnement = (List<GroupeHasEnvironnement>) q.getResultList();

            //effacer les environnements que l'utilisateur a décoché
            for (GroupeHasEnvironnement tobeDel : listeAllGroupeHasEnvironnement) {
                if (!checkedBoxesList.contains(tobeDel.getEnvironnement().getNom())) {
                    dbTools.em.remove(tobeDel);
                }
            }
            dbTools.em.getTransaction().commit();
            if (!dbTools.em.getTransaction().isActive()) {
                dbTools.em.getTransaction().begin();


            }
            Groupe groupe = dbTools.em.find(Groupe.class, groupName);
            List<GroupeHasEnvironnement> listeGroupeHasEnvironnement = new ArrayList<GroupeHasEnvironnement>();
            GroupeHasEnvironnement grpHasenvAux = null;
            for (String envName : checkedBoxesList) {
                grpHasenvAux = new GroupeHasEnvironnement();
                grpHasenvAux.setDescription("");
                grpHasenvAux.setEnvironnement(Configuration.allEnvironnementMap.get(envName.trim()));
                grpHasenvAux.setGroupe(Configuration.groupMap.get(groupName));
                grpHasenvAux.setGroupeHasEnvironnementPK(new GroupeHasEnvironnementPK(groupName, envName));
                listeGroupeHasEnvironnement.add(grpHasenvAux);
            }
            /*
             for(GroupeHasEnvironnement gre : listeGroupeHasEnvironnement){
             dbTools.em.persist(gre);
             }
             */

            groupe.getGroupeHasEnvironnementList()
                    .clear();
            groupe.setGroupeHasEnvironnementList(listeGroupeHasEnvironnement);

            dbTools.em.merge(groupe);

            dbTools.em.getTransaction()
                    .commit();
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }

    public void deleteObjetsHorsReferentiel(String circuit, DataBaseTools dbTools) {
        try {
            Query q = dbTools.em.createNamedQuery("ObjetsHorsReferentiel.deleteByCircuit");
            q.setParameter("circuit", circuit);
            q.executeUpdate();
            dbTools.em.getTransaction().commit();
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }

    public Map<String, String> getTicketCustomLivraisonByTicketLivraisonIdAndName(int numLivraison, String pu, String siteTrac, String[] cles) {
        String resltat = null;
        List<Integer> listeIdTickets = new ArrayList<Integer>();
        listeIdTickets.add(numLivraison);
        Map<Integer, Map<String, Object>> globalResultMap = AppelRequetes.getTicketCustomByTicketIdAndNames(listeIdTickets, pu, siteTrac, cles);
        Map<String, Object> ticketLivraisonMap = null;
        Map<String, String> resultMap = new HashMap<String, String>();
        Ticket ticket = null;
        try {
            ticketLivraisonMap = globalResultMap.get(numLivraison);
            if (ticketLivraisonMap.isEmpty()) {
                resultMap.put("CONTENU", "VIDE");
            } else {
                ticket = (Ticket) ticketLivraisonMap.get("Ticket");
                resultMap.put("CONTENU", "NON_VIDE");
                resultMap.put("projet", (String) ticketLivraisonMap.get("projet"));
                resultMap.put("ticket_origine", (String) ticketLivraisonMap.get("ticket_origine"));
                resultMap.put("nature_trait", (String) ticketLivraisonMap.get("nature_trait"));
                try {
                    resultMap.put("contenu_des_livrables", (String) ticketLivraisonMap.get("contenu_des_livrables"));
                } catch (Exception e) {
                    resultMap.put("contenu_des_livrables", "A DEFINIR");
                }
                resultMap.put("nature_liv", (String) ticketLivraisonMap.get("nature_liv"));
                resultMap.put("type", ticket.getType());
                resultMap.put("priority", ticket.getPriority());
                resultMap.put("milestone", ticket.getMilestone());
                resultMap.put("component", ticket.getComponent());
            }
            Tools.showConsolLog(resultMap.toString());
            System.gc();
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return resultMap;
    }

    public void storeNbrOfFilesByPackName(String packName, int nbrOfFiles, DataBaseTools dbTools) {
        NbrObjParPack nbrObjParPack = new NbrObjParPack(packName, nbrOfFiles);
        dbTools.update(nbrObjParPack);
    }

    public int getNbrOfFilesByPackName(String packName, DataBaseTools dbTools) {
        int nbrObj = -1;


        try {
            NbrObjParPack nbrObjParPack = dbTools.em.find(NbrObjParPack.class, packName);
            if (nbrObjParPack
                    != null) {
                nbrObj = nbrObjParPack.getNbrObjets();
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return nbrObj;
    }

    public int getNbrOfFilesInPack(String packName, DataBaseTools dbTools) {
        int nbr = -1;


        try {
            NbrObjParPack nbrObjParPack = dbTools.em.find(NbrObjParPack.class, packName);
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
            Tools.showConsolLog("Le pack n'existe pas");
        }
        return nbr;
    }

    public static String sendHotfixToBeDeployed(HttpServletRequest request, String connectedUser, String numTicketLivraison, String messageTracParam, String circuit, String contenuDesLivrables, boolean sendTicketToIE, boolean writeTextOnTicket, boolean insertTicketLivraison, boolean nePasEcraserLivrable) {
        String resultat = "OK";
        try {
            String pu = Configuration.puLivraisons;
            int numTicket = Integer.parseInt(numTicketLivraison);
            String messageTrac = messageTracParam;
            String newOwner = null;
            String newPriority = null;
            String newStatus = null;
            String newVersion = null;
            List<CoupleDTO> customFieldList = null;

            if (sendTicketToIE == true) {
                customFieldList = new ArrayList<CoupleDTO>();
                newOwner = "riadh anouar ben dakhlia";
                newPriority = "PRET POUR DEPLOIEMENT";
                //set the developpement and the assembly envirements
                if (contenuDesLivrables.equals("MULTIPACKS")) {
                    customFieldList.add(new CoupleDTO("contenu_des_livrables", "MULTIPACKS"));
                }
                if (circuit.equals("RELEASE")) {
                    customFieldList.add(new CoupleDTO("biatdevr", "1"));
                    customFieldList.add(new CoupleDTO("biattem", "1"));
                } else if (circuit.equals("PROJET")) {
                    customFieldList.add(new CoupleDTO("biatdev2", "1"));
                    customFieldList.add(new CoupleDTO("biatass2", "1"));
                } else if (circuit.equals("HOTFIX")) {
                    customFieldList.add(new CoupleDTO("biatdevh", "1"));
                    customFieldList.add(new CoupleDTO("biattem", "1"));
                }
            }
            if (writeTextOnTicket == false) {
                messageTrac = null;
            }

            DataBaseTracGenericRequests.updateTicketTracGeneral(pu, numTicket, connectedUser, messageTrac, newOwner, newPriority, newStatus, newVersion, customFieldList);

            if (insertTicketLivraison == true) {
                DetailsLivraisonDTO detailsLivraison = Configuration.usersDetailsLivraisonsMap.get(connectedUser);
                //insertion de l'objet Livraison dans la base
                DataBaseTools dbTools = new DataBaseTools(Configuration.puOvTools);
                Livraison liv = dbTools.em.find(Livraison.class, numTicket);
                if (liv
                        == null) {
                    liv = new Livraison(Integer.parseInt(detailsLivraison.getNumTicket()));
                }
                String selectedMnemonic = detailsLivraison.getMnemonicCompany();
                String packName = detailsLivraison.getPackName();
                int nbrIter = detailsLivraison.getNbrIterationDeploiement();
                String objetsT24 = detailsLivraison.getListeObjets();
                if (!nePasEcraserLivrable) {
                    byte[] serializedLivraisonList = Tools.createLivraisonList(selectedMnemonic, packName, nbrIter, objetsT24);
                    liv.setLivrables(serializedLivraisonList);
                }

                liv.setNumeroAnomalie(Integer.parseInt(detailsLivraison.getAnomalie()));
                liv.setContenuLivrables(detailsLivraison.getContenuDesLivrables());
                liv.setNomPack(packName);

                liv.setOwner(detailsLivraison.getOwner());
                liv.setReporter(detailsLivraison.getReporter());
                liv.setCompanyDeploiement(selectedMnemonic);

                liv.setNombreIterations(nbrIter);

                liv.setType(detailsLivraison.getPhase());
                if (detailsLivraison.getCircuit()
                        .equals("RELEASE") || detailsLivraison.getCircuit().equals("PROJET")) {
                    liv.setMessageTrac(messageTracParam);
                } else {
                    liv.setMessageTrac(messageTracParam.replace("INV", "PROD"));
                }

                liv.setListeObjets(objetsT24);

                liv.setResultatetudeintersectionCR(
                        "");
                liv.setResultatetudeintersectionCP(
                        "");
                liv.setValide(
                        false);
                liv.setCloturee(
                        false);
                dbTools.update(liv);

                dbTools.closeRessources();
            }
        } catch (Exception exp) {
            exp.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exp));
        }
        return resultat;
    }

    public static void sendOneHotfixToProd(Livraison livraison) {
        try {
            String pu = Configuration.puLivraisons;
            int numTicket = livraison.getNumeroLivraison();
            String connectedUser = "OVTOOLS";
            String messageTrac = livraison.getMessageTrac().replaceAll("INV ET CRT", "PROD").replaceAll("INV et CRT", "PROD").replaceAll("INV", "PROD").replaceAll("CRT", "PROD");
            String newOwner = "riadh anouar ben dakhlia";
            String newPriority = "PRET POUR DEPLOIEMENT";
            String newStatus = "new";
            String newVersion = null;
            List<CoupleDTO> customFieldList = null;
            DataBaseTracGenericRequests.updateTicketTracGeneral(pu, numTicket, connectedUser, messageTrac, newOwner, newPriority, newStatus, newVersion, customFieldList);
        } catch (Exception exp) {
            exp.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exp));
        }
    }

    public Map<String, Object> getAnomaliesByLivraisons(List<Integer> numLivraisonsList) {
        Map<String, Object> coupleLivraisonAnomalie = new HashMap<String, Object>();
        try {
            Set<Integer> numLivraisonsSet = new TreeSet<Integer>();

            Boolean zeroExists = false;
            for (Integer numLivraison : numLivraisonsList) {
                if (numLivraison == 0) {
                    zeroExists = true;
                } else {
                    numLivraisonsSet.add(numLivraison);
                }
            }
            if (zeroExists) {
                coupleLivraisonAnomalie.put("0000", "0");
            }
            numLivraisonsList.clear();
            numLivraisonsList.addAll(numLivraisonsSet);

            String[] cles = new String[]{"ticket_origine"};
            Map<Integer, Map<String, Object>> globalResultMap = AppelRequetes.getTicketCustomByTicketIdAndNames(numLivraisonsList, Configuration.puLivraisons, Configuration.tracLivraisons, cles);
            for (Map.Entry<Integer, Map<String, Object>> entry : globalResultMap.entrySet()) {
                coupleLivraisonAnomalie.put(entry.getKey().toString(), entry.getValue().get("ticket_origine"));
            }
            Tools.showConsolLog(coupleLivraisonAnomalie.toString());
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return coupleLivraisonAnomalie;
    }

    public Map<String, List<Map<String, Object>>> getAllPipeTicketsHraccessRequestHR(String acteur, String pu, int... iterations) {
        //clé:décision / valeur : liste de tickets
        List<Ticket> listeTicketsPipes = null;
        List<Ticket> listeTicketsPipesAHarmoniser = null;
        Map<String, List<Map<String, Object>>> resultMap = new LinkedHashMap<String, List<Map<String, Object>>>();
        try {
            Map<Integer, Map<String, Object>> ticketMap = null;
            List<Integer> ticketsIdList = null;
            //Release
            System.out.println("ACTEUR:::" + acteur);
            if (acteur == null) {
                listeTicketsPipes = new ArrayList<Ticket>();
                tools.Tools.traiterException("la variable acteur a été passée null");
            } else {
                String namedQuery = null;
                if (acteur.equals("OVHR")) {
                    namedQuery = "Ticket.findAllPipeTicketsHraccessOV";
                } else if (acteur.equals("IEHR")) {
                    namedQuery = "Ticket.findAllPipeTicketsHraccessIandE";
                }


                listeTicketsPipes = new DataBaseTracGenericRequests<Ticket>().getList_TYPE_OfnamedQuery(pu, namedQuery, null);
                listeTicketsPipesAHarmoniser = new DataBaseTracGenericRequests<Ticket>().getList_TYPE_OfnamedQuery(pu, "Ticket.findAllHotfixHraccessAHarmoniser", null);
                if (listeTicketsPipesAHarmoniser != null) {
                    if (!listeTicketsPipesAHarmoniser.isEmpty()) {
                        try {
                            listeTicketsPipes.addAll(listeTicketsPipesAHarmoniser);
                        } catch (Exception exep) {
                            exep.printStackTrace();
                            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
                        }
                    } else {
                        tools.Tools.traiterException("DataBaseTracRequest : listeTicketsPipesAHarmoniser empty");
                    }
                } else {
                    tools.Tools.traiterException("DataBaseTracRequest : listeTicketsPipesAHarmoniser null");
                }

            }
            if (!listeTicketsPipes.isEmpty()) {

                ticketsIdList = new ArrayList<Integer>();
                for (Ticket tick : listeTicketsPipes) {
                    ticketsIdList.add(tick.getId());
                }
                Map<String, Map<String, String>> mapTicketsDateEnvoiPourDeploiement = new ManipulationObjectsTool().getDeployementDate(pu, ticketsIdList, "objet_livre");

                Map<String, Map<String, String>> mapTicketsDateEnvoiPourDeploiementIE = new ManipulationObjectsTool().getDeployementDate(pu, ticketsIdList, "pret_pour_mise_en_prod");

                String[] cles = new String[]{"nature_traitement"};
                String namedQuery = "TicketCustom.findAllTicketsCustomOfOpenTickets";
                //ticketMap = ToolsNewTrac.executeQueryThenAnalyseTickets(persistenceUnit, namedQuery, cles, null);
                ticketMap = AppelRequetes.getTicketCustomByTicketIdAndNames(ticketsIdList, pu, "HR ACCESS", cles);
                String natureLiv = "";
                for (Ticket tick : listeTicketsPipes) {
                    String nature_traitement = (String) (ticketMap.get(tick.getId()).get("nature_traitement"));
                    String decision = "";
                    String deliveryTime = "";
                    String deliveryTimeIE = "";
                    String responsable = "";
                    String ticketApplProd = "";
                    String hfSurCrt = "";
                    try {
                        deliveryTime = mapTicketsDateEnvoiPourDeploiement.get(tick.getId().toString()).get("time");
                    } catch (Exception e) {
                    }
                    try {
                        deliveryTimeIE = mapTicketsDateEnvoiPourDeploiementIE.get(tick.getId().toString()).get("time");
                    } catch (Exception e) {
                    }
                    try {
                        responsable = mapTicketsDateEnvoiPourDeploiement.get(tick.getId().toString()).get("responsable");
                    } catch (Exception e) {
                    }

                    if (tick.getStatus().equals("objet_livre")) {
                        if (nature_traitement.equals("RELEASE")) {
                            decision = "PIPE_OV_RELEASE";
                        }
                        if (nature_traitement.equals("HOTFIX")) {
                            decision = "PIPE_OV_HOTFIX";
                        }
                        if (nature_traitement.equals("ACTION A CHAUD")) {
                            decision = "PIPE_OV_ACTION_A_CHAUD";
                        }
                    } else if (tick.getStatus().equals("deployee_sur_prod")) {
                        if (nature_traitement.equals("RELEASE")) {
                            decision = "PIPE_OV_HARM_CR";
                        }
                    } else if (tick.getStatus().equals("pret_pour_mise_en_prod")) {
                        if (nature_traitement.equals("RELEASE")) {
                            decision = "PIPE_IE_RELEASE";
                        }
                        if (nature_traitement.equals("HOTFIX")) {
                            decision = "PIPE_IE_HOTFIX";
                        }
                        if (nature_traitement.equals("ACTION A CHAUD")) {
                            decision = "PIPE_IE_ACTION_A_CHAUD";
                        }
                    }

                    sortMapDecisionsNewTrac(resultMap, decision, tick, nature_traitement, tick.getOwner(), tick.getReporter(), deliveryTime, deliveryTimeIE, responsable);
                }
                System.out.println("END GET PIPE TICKETS");
            }
            System.gc();
        } catch (Exception exep) {

            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));


        }
        return resultMap;
    }

    public Map<String, List<Map<String, Object>>> getAllPipeTicketsRequestT24(String acteur, String pu) {
        //clé:décision / valeur : liste de tickets
        List<Ticket> listeTicketsPipes = null;
        Map<String, List<Map<String, Object>>> resultMap = new LinkedHashMap<>();
        try {
            Map<Integer, Map<String, Object>> ticketMap = null;
            List<Integer> ticketsIdList = null;
            //Release
            Tools.showConsolLog("ACTEUR:::" + acteur);

            if (acteur == null) {
                listeTicketsPipes = new ArrayList<>();
                tools.Tools.traiterException("la variable acteur a été passée null au web service");
            } else {
                String namedQuery = null;
                Map<String, Object> paramMap = null;
                if (acteur.equals("OV")) {
                    namedQuery = "Ticket.findAllPipeTicketsOV";
                } else if (acteur.equals("IE")) {
                    namedQuery = "Ticket.findAllPipeTicketsIandE";
                } else if (acteur.equals("OVHR")) {
                    namedQuery = "Ticket.findAllPipeTicketsHraccessOV";
                } else if (acteur.equals("IEHR")) {
                    namedQuery = "Ticket.findAllPipeTicketsHraccessIandE";
                } else if (acteur.equals("CDD")) {
                    String connectedUser = Tools.getConnectedLogin();
                    boolean isAdmin = Configuration.usersGroupMap.get(connectedUser).contains("ADMIN");
                    namedQuery = "Ticket.findAllPipeTicketsCDDByOwner";
                    paramMap = new LinkedHashMap<>();
                    if (isAdmin) {
                        paramMap.put("owner", "%");
                    } else {
                        paramMap.put("owner", connectedUser);
                    }
                }
                listeTicketsPipes = new DataBaseTracGenericRequests<Ticket>().getList_TYPE_OfnamedQuery(pu, namedQuery, paramMap);
                if (!acteur.equals("CDD")) {
                    listeTicketsPipes.addAll(AppelRequetes.getTicketsHotfixAHarmoniser(pu));
                }
            }
            if (!listeTicketsPipes.isEmpty()) {
                resultMap = new LinkedHashMap<>();

                ticketsIdList = new ArrayList<>();
                for (Ticket tick : listeTicketsPipes) {
                    ticketsIdList.add(tick.getId());
                }

                Map<String, Map<String, String>> mapTicketsDateEnvoiPourDeploiement = new ManipulationObjectsTool().getDeployementDate(pu, ticketsIdList, "OBJET LIVREE");

                Map<String, Map<String, String>> mapTicketsDateEnvoiPourDeploiementIE = new ManipulationObjectsTool().getDeployementDate(pu, ticketsIdList, "PRET POUR DEPLOIEMENT");

                String[] cles = new String[]{"projet", "ticket_origine", "nature_liv", "contenu_des_livrables", "ticket_appl_prod", "hf_sur_crt", "biatdev2", "biatdevr"};
                ticketMap = AppelRequetes.getTicketCustomByTicketIdAndNames(ticketsIdList, Configuration.puLivraisons, Configuration.tracLivraisons, cles);

                String natureLiv = "";
                for (Ticket tick : listeTicketsPipes) {
                    String projet = (String) (ticketMap.get(tick.getId()).get("projet"));
                    String anomalie = (String) (ticketMap.get(tick.getId()).get("ticket_origine"));
                    String contenuDesLivrables = (String) (ticketMap.get(tick.getId()).get("contenu_des_livrables"));
                    String decision = "";
                    String deliveryTime = "";
                    String deliveryTimeIE = "";
                    String responsable = "";
                    String ticketApplProd = "";
                    String hfSurCrt = "";
                    String biatdev2 = "";
                    String biatdevr = "";
                    try {
                        deliveryTime = mapTicketsDateEnvoiPourDeploiement.get(tick.getId().toString()).get("time");
                    } catch (Exception e) {
                    }
                    try {
                        deliveryTimeIE = mapTicketsDateEnvoiPourDeploiementIE.get(tick.getId().toString()).get("time");
                    } catch (Exception e) {
                    }
                    try {
                        responsable = mapTicketsDateEnvoiPourDeploiement.get(tick.getId().toString()).get("responsable");
                    } catch (Exception e) {
                    }
                    try {
                        ticketApplProd = (String) (ticketMap.get(tick.getId()).get("ticket_appl_prod"));
                    } catch (Exception e) {
                    }
                    try {
                        hfSurCrt = (String) (ticketMap.get(tick.getId()).get("hf_sur_crt"));
                    } catch (Exception e) {
                    }

                    try {
                        biatdev2 = (String) (ticketMap.get(tick.getId()).get("biatdev2"));
                    } catch (Exception e) {
                    }
                    try {
                        biatdevr = (String) (ticketMap.get(tick.getId()).get("biatdevr"));
                    } catch (Exception e) {
                    }
                    if (tick.getPriority().equals("DEPLOYEE")) {
                        decision = "HARMONISATION_HF_OV";
                    } else {
                        if (tick.getType().equals("HOT FIXE PROD")) {
                            natureLiv = ticketMap.get(tick.getId()).get("nature_liv").toString();
                            if (natureLiv.equals("HARMONISATION_C.RELEASE")) {
                                decision = "HARMONISATION_C.RELEASE";
                            } else if (natureLiv.equals("HARMONISATION_C.PROJET")) {
                                decision = "HARMONISATION_C.PROJET";
                            } else if (natureLiv.equals("HARMONISATION_C.UPGRADE")) {
                                decision = "HARMONISATION_C.UPGRADE";
                            } else if (natureLiv.equals("CORRECTIF")) {
                                decision = "HF.PROD";
                            }
                        } else {
                            decision = tick.getType();
                        }
                    }
                    sortMapDecisions(resultMap, decision, tick, projet, anomalie, contenuDesLivrables, tick.getOwner(), tick.getReporter(), deliveryTime, deliveryTimeIE, responsable, ticketApplProd, hfSurCrt, biatdev2, biatdevr);
                }
                Tools.showConsolLog("END GET PIPE TICKETS");
            }
            System.gc();
        } catch (Exception exep) {

            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));


        }
        return resultMap;
    }

    public void sortMapDecisions(Map<String, List<Map<String, Object>>> mapDecisions, String decision, Ticket tick, String projet, String anomalie, String contenuDesLivrables, String owner, String reporter, String deliveryTime, String deliveryTimeIE, String responsable, String ticketApplProd, String hfSurCrt, String biatdev2, String biatdevr) {
        Map<String, Object> ticketDetails = new LinkedHashMap<String, Object>();
        ticketDetails.put("Ticket", tick);
        ticketDetails.put("projet", projet);
        ticketDetails.put("ticket_origine", anomalie);
        ticketDetails.put("contenu_des_livrables", contenuDesLivrables);
        ticketDetails.put("owner", owner);
        ticketDetails.put("deliveryTime", deliveryTime);
        ticketDetails.put("deliveryTimeIE", deliveryTimeIE);
        ticketDetails.put("reporter", reporter);
        ticketDetails.put("responsable", responsable);
        ticketDetails.put("ticket_appl_prod", ticketApplProd);
        ticketDetails.put("hf_sur_crt", hfSurCrt);
        ticketDetails.put("biatdevr", biatdevr);
        ticketDetails.put("biatdev2", biatdev2);

        if (mapDecisions.containsKey(decision)) {
            mapDecisions.get(decision).add(ticketDetails);
        } else {
            List<Map<String, Object>> listAux = new LinkedList<Map<String, Object>>();
            listAux.add(ticketDetails);
            mapDecisions.put(decision, listAux);
        }
    }

    public static Map<Integer, Map<String, Object>> traiterTicketCustom(List<TicketCustom> ticketCustomList) {
        Map<Integer, Map<String, Object>> ticketMap = new LinkedHashMap<Integer, Map<String, Object>>();
        try {
            Integer numTicket;
            for (TicketCustom ticketCustom : ticketCustomList) {
                numTicket = ticketCustom.getTicket();
                //Tools.showConsolLog(numTicketAnomalie+"  "+ticketCustomLivraisons.getName());
                if (ticketMap.containsKey(numTicket)) {
                    //Tools.showConsolLog("\n\n\n\n"+ticketCustomLivraisons.getName()+"   "+ ticketCustomLivraisons.getValue());
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

    public void sortMapDecisionsNewTrac(Map<String, List<Map<String, Object>>> mapDecisions, String decision, Ticket tick, String nature_traitement, String owner, String reporter, String deliveryTime, String deliveryTimeIE, String responsable) {
        Map<String, Object> ticketDetails = new LinkedHashMap<String, Object>();
        ticketDetails.put("Ticket", tick);
        ticketDetails.put("nature_traitement", nature_traitement);
        ticketDetails.put("owner", owner);
        ticketDetails.put("deliveryTime", deliveryTime);
        ticketDetails.put("deliveryTimeIE", deliveryTimeIE);
        ticketDetails.put("reporter", reporter);
        ticketDetails.put("responsable", responsable);

        if (mapDecisions.containsKey(decision)) {
            mapDecisions.get(decision).add(ticketDetails);
        } else {
            List<Map<String, Object>> listAux = new LinkedList<Map<String, Object>>();
            listAux.add(ticketDetails);
            mapDecisions.put(decision, listAux);
        }
    }
}