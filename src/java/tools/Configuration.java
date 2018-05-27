/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import dto.CoupleDTO;
import dto.DetailsLivraisonDTO;
import dto.EnvironnementDTO;
import dto.UserDTO;
import servlets.AfficherMessageEtatAvancement;
import static servlets.AfficherMessageEtatAvancement.logMessageMap;
import entitiesMysql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.persistence.Query;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import t24Scripts.T24Scripts;
import threads.AutomatisationDeploiementIeThread;
import threads.AutomatisationDeploiementOvThread;
import threads.GestionHotfixProdThread;
import threads.HarmonisationHotfixUpgradeThread;
import threads.ManageLogThread;
import threads.PipeSystemeThread;

/**
 *
 * @author 04486
 */
public class Configuration {

    public static Map<String, Map<String, Environnement>> environnementMapDutyGroup = null;
    public static Map<String, Map<String, EnvironnementDTO>> environnementDTOMapUserHasEnvironnement = null;
    public static Map<String, List<String>> usersGroupMap = null;
    public static Map<String, Environnement> allEnvironnementMap = null;
    public static Map<String, List<Fonctionalite>> allFonctionalitesMapOrdredByType = null;
    public static Map<String, Fonctionalite> allFonctionalitesMap = null;
    public static Map<String, Groupe> groupMap = null;
    public static Map<String, Integer> priorityList = null;
    public static Set<String> statusSet = null;
    public static Map<String, String> parametresList = null;
    public static List<String> projetsActifsCircuitProjetList = null;
    public static List<String> projetsActifsCircuitReleaseList = null;
    public static List<String> projetsActifsCircuitUpgradeList = null;
    public static List<String> projetsActifsCircuitReleaseEtProjetList = null;
    public static Map<String, Map<String, Map<String, Integer>>> restoreMap = new LinkedHashMap<>();
    public static Map<String, Users> usersMap = null;
    public static List<String> usersNamesList = null;
    public static List<String> listMnemonic = null;
    public static Map<String, List<String>> mapObjetsParProjet = null;
    public static Map<String, String> abrevNomProjProp = new LinkedHashMap<>();
    public static Map<String, String> nomProjAbreviationProp = new LinkedHashMap<>();
    private static Set<String> ensembleSetObjetsProjet = new HashSet<>();
    private static Set<String> ensembleSetObjetsRelease = new HashSet<>();
    public static Map<String, String> typesReglesMap = new LinkedHashMap<>();
    public static Map<String, Map<String, String>> typesReglesOrderedByRemarquesMap = new LinkedHashMap<>();
    public static Map<String, String> observationReglesMap = new LinkedHashMap<>();
    public static Map<String, TypesRegle> alltypesReglesMap = new LinkedHashMap<>();
    public static boolean intitOk = false;
    public static Map<String, String> etatCircuitMap = null;
    public static Map<String, String> mapChiffres = null;
    private static ManageLogThread manageLogThread = null;
    private static GestionHotfixProdThread gestionHotfixProdThread = null;
    private static HarmonisationHotfixUpgradeThread HarmonisationHotfixUpgradeThread = null;
    public static AutomatisationDeploiementIeThread automatisationDeploiementIeThread = null;
    public static AutomatisationDeploiementOvThread automatisationDeploiementOvThread = null;
    public static Map<String, String> persistanceUnitsMap;
    public static Map<String, String> matriculeLoginMap;
    public static Map<String, String> loginMatriculeMap;
    public static Map<String, Set<GrantedAuthority>> allFonctionalitesMapOrdredByMatricule;
    public static Map<String, String> mapEnvNameCheckBoxName = null;
    //key:matricule
    public static Map<String, UserDTO> usersDtoMap = new HashMap<>();
    public static Map<String, String> livraisonsEnCoursMap;
    public static Map<String, String> livraisonsEnCoursMapIE;
    public static String tracAnomalies = "ANOMALIES_T24";
    public static String tracLivraisons = "LIVRAISONS_T24";
    public static String tracHraccess = "HR_ACCESS";
    public static String tracMxp = "MXP";
    public static String tracBfiCartagoIntranet = "BFI_CARTAGO_INTRANET";
    public static String tracContentieux = "CONTENTIEUX";
    public static String tracGtiWinserge = "GTI_WINSERGE";
    public static String tracOgc = "OGC";
    public static String tracBfiTitre = "BFI_TITRE";
    public static String tracIcr = "ICR";
    public static String tracGestionDemandes = "GESTION_DEMANDES";
    public static String puAnomalies = "dbAnomalieNewTracPU";
    public static String puLivraisons = "dbLivraisonNewTracPU";
    public static String puHraccess = "dbTracHrAccessPU";
    public static String puMxp = "dbMxpPU";
    public static String puBfiCartagoIntranet = "dbBfiCartagoIntranetPU";
    public static String puContentieux = "dbContentieuxPU";
    public static String puGtiWinserge = "dbGtiWinsergePU";
    public static String puOgc = "dbOgcPU";
    public static String puBfiTitre = "dbBfiTitrePU";
    public static String puIcr = "dbICRPU";
    public static String puOvTools = "mysqlDataBasePU";
    public static String puGestionDesDemandes = "dbGestionDesDemandesOvtoolsPU";
    public static String puGestionDesIncidents = "dbGestionIncidentsPU";
    public static Map<String, CoupleDTO> mapFields = null;
    public static Map<String, CoupleDTO> mapProjetsTrac = null;
    public static final String HEURE_FORMAT = "HH:mm";
    public static final String HEURE_DATE_FORMAT = "dd-MM-yyyy H:m";
    public static Map<String, DetailsLivraisonDTO> usersDetailsLivraisonsMap = null;
    public static boolean mustInitialiseThreadHF = true;
    public static boolean mustInitialiseThreadHarmUpdate = true;
    public static boolean mustInitialiseThreadIE = true;
    public static boolean mustInitialiseThreadOV = true;
    public static Map<String, String> mapCorrespondanceTypesLivrable = null;
    public static Map<String, String> allMilestonesEmailMap = null;

    public static void allFonctionalitesMapOrdredByMatricule(DataBaseTools dbTools) {
        StringBuilder requete = new StringBuilder();
        requete.append("SELECT u.matricule, f.name, u.login ");
        requete.append("FROM users u ");

        requete.append("LEFT JOIN users_has_groupe ug ");
        requete.append("ON u.login = ug.users_login ");

        requete.append("LEFT JOIN groupe g ");
        requete.append("ON g.nom = ug.groupe_nom ");

        requete.append("LEFT JOIN groupe_has_fonctionalite gf ");
        requete.append("ON g.nom = gf.groupe_nom ");

        requete.append("LEFT JOIN fonctionalite f ");
        requete.append("ON f.name = gf.fonctionalite_name ");

        requete.append("where f.name <>  'NULL'");

        Query q = dbTools.em.createNativeQuery(requete.toString());
        List<Object[]> allFonctionalitesList = (List<Object[]>) q.getResultList();

        allFonctionalitesMapOrdredByMatricule = new LinkedHashMap<String, Set<GrantedAuthority>>();
        for (Object[] tab : allFonctionalitesList) {
            Set<GrantedAuthority> setGrantedAuthority;
            if (!allFonctionalitesMapOrdredByMatricule.containsKey(tab[0].toString())) {
                setGrantedAuthority = new HashSet<GrantedAuthority>();
            } else {
                setGrantedAuthority = allFonctionalitesMapOrdredByMatricule.get(tab[0].toString());
            }
            setGrantedAuthority.add(new GrantedAuthorityImpl(tab[1].toString()));
            allFonctionalitesMapOrdredByMatricule.put(tab[0].toString(), setGrantedAuthority);
        }
    }

    private static void initialisationVariables() {
        if (livraisonsEnCoursMap == null) {
            livraisonsEnCoursMap = new LinkedHashMap<String, String>();
        }
        if (usersDetailsLivraisonsMap == null) {
            usersDetailsLivraisonsMap = new LinkedHashMap();
        }
        if (livraisonsEnCoursMapIE == null) {
            livraisonsEnCoursMapIE = new LinkedHashMap<String, String>();
        }
        if (persistanceUnitsMap == null) {
            persistanceUnitsMap = new HashMap<String, String>();
            persistanceUnitsMap.put(puAnomalies, "trac anomalies");
            persistanceUnitsMap.put(puLivraisons, "trac livraisons");
            persistanceUnitsMap.put(puOvTools, "de paramétrage");
        }
        if (mapChiffres == null) {
            mapChiffres = new LinkedHashMap<String, String>();
            mapChiffres.put("1", "Un seul");
            mapChiffres.put("2", "Deux");
            mapChiffres.put("3", "Trois");
            mapChiffres.put("4", "Quatre");
            mapChiffres.put("5", "Cinq");
            mapChiffres.put("6", "Six");
            mapChiffres.put("7", "Sept");
            mapChiffres.put("8", "Huit");
            mapChiffres.put("9", "Neuf");
            mapChiffres.put("10", "Dix");
            mapChiffres.put("11", "Onze");
            mapChiffres.put("12", "Douze");
            mapChiffres.put("13", "Treize");
            mapChiffres.put("14", "Quatorze");
            mapChiffres.put("15", "Quinze");
            mapChiffres.put("16", "Seize");
            mapChiffres.put("17", "Dix-sept");
            mapChiffres.put("18", "Dix-huit");
            mapChiffres.put("19", "Dix-neuf");
            mapChiffres.put("20", "Vingt");
            mapChiffres.put("21", "Vingt-et-un");
            mapChiffres.put("22", "Vingt-deux");
            mapChiffres.put("23", "Vingt-trois");
            mapChiffres.put("24", "Vingt-quatre");
            mapChiffres.put("25", "Vingt-cinq");
            mapChiffres.put("26", "Vingt-six");
            mapChiffres.put("27", "Vingt-sept");
            mapChiffres.put("28", "Vingt-huit");
            mapChiffres.put("29", "Vingt-neuf");
            mapChiffres.put("30", "Trente");
        }
        if (mapEnvNameCheckBoxName == null) {
            mapEnvNameCheckBoxName = new HashMap<String, String>();
            mapEnvNameCheckBoxName.put("CRT", "biatcertif");
            mapEnvNameCheckBoxName.put("QL1", "biatql1");
            mapEnvNameCheckBoxName.put("MIGR", "biatmigd");
            mapEnvNameCheckBoxName.put("QL2", "biatql2");
            mapEnvNameCheckBoxName.put("TF1", "biattf1");
            mapEnvNameCheckBoxName.put("IF2", "biatif2");
            mapEnvNameCheckBoxName.put("MIGP", "biatmigp");
            mapEnvNameCheckBoxName.put("INV", "preprod");
            mapEnvNameCheckBoxName.put("DEVR", "biatdevr");
            mapEnvNameCheckBoxName.put("DEV2", "biatdev2");
            mapEnvNameCheckBoxName.put("ASS", "biattem");
            mapEnvNameCheckBoxName.put("ASS2", "biatass2");
        }
        if (mapFields == null) {
            mapFields = new HashMap<>();
            mapFields.put("bloquerHarmonisation", new CoupleDTO("Harmonisation bloquée", "width1"));
            mapFields.put("cc", new CoupleDTO("Owner", "width3"));
            mapFields.put("changetime", new CoupleDTO("Change Time", "width3"));
            mapFields.put("cloturee", new CoupleDTO("Cloturee", "width1"));
            mapFields.put("companyDeploiement", new CoupleDTO("Company", "width1"));
            mapFields.put("component", new CoupleDTO("Compostant", "width3"));
            mapFields.put("contenuLivrables", new CoupleDTO("Type du livrable", "width3"));
            mapFields.put("contenu_des_livrables", new CoupleDTO("Type du livrable", "width3"));
            mapFields.put("dateDeploiement", new CoupleDTO("Déployé le", "width2"));
            mapFields.put("dateEnvoiProd", new CoupleDTO("Date Envoi Prod", "width3"));
            mapFields.put("description", new CoupleDTO("Description", "width2"));
            mapFields.put("harm1probleme", new CoupleDTO("harm1probleme", "width1"));
            mapFields.put("harmCp", new CoupleDTO("harmCp", "width1"));
            mapFields.put("harmCr", new CoupleDTO("harmCr", "width1"));
            mapFields.put("id", new CoupleDTO("Livraison", "width1"));
            mapFields.put("keywords", new CoupleDTO("keywords", "width3"));
            mapFields.put("listeObjets", new CoupleDTO("Objets", "width3"));
            mapFields.put("messageTrac", new CoupleDTO("Message trac", "width3"));
            mapFields.put("milestone", new CoupleDTO("Jalon", "width3"));
            mapFields.put("nomPack", new CoupleDTO("Pack", "width3"));
            mapFields.put("nombreIterations", new CoupleDTO("nombreIterations", "width1"));
            mapFields.put("numeroAnomalie", new CoupleDTO("Anomalie", "width1"));
            mapFields.put("numeroLivraison", new CoupleDTO("Livraison", "width1"));
            mapFields.put("owner", new CoupleDTO("Owner", "width3"));
            mapFields.put("priority", new CoupleDTO("Priorité", "width3"));
            mapFields.put("reporter", new CoupleDTO("Reporter", "width3"));
            mapFields.put("resolution", new CoupleDTO("Resolution", "width3"));
            mapFields.put("resultatetudeintersectionCP", new CoupleDTO("resultatetudeintersectionCP", "width3"));
            mapFields.put("resultatetudeintersectionCR", new CoupleDTO("resultatetudeintersectionCR", "width3"));
            mapFields.put("severity", new CoupleDTO("severity", "width3"));
            mapFields.put("status", new CoupleDTO("Statut", "width3"));
            mapFields.put("summary", new CoupleDTO("Résumé", "width1"));
            mapFields.put("ticket_origine", new CoupleDTO("Anomalie", "width1"));
            mapFields.put("time", new CoupleDTO("Time", "width3"));
            mapFields.put("type", new CoupleDTO("Type", "width2"));
            mapFields.put("valide", new CoupleDTO("valide", "width3"));
            mapFields.put("version", new CoupleDTO("Version", "width3"));
            mapFields.put("nature_traitement", new CoupleDTO("nature_traitement", "width2"));
        }
        if (mapProjetsTrac == null) {
            mapProjetsTrac = new HashMap<>();
            mapProjetsTrac.put(tracAnomalies, new CoupleDTO("http://172.28.70.74/trac/anomalies_t24", puAnomalies));
            mapProjetsTrac.put(tracLivraisons, new CoupleDTO("http://172.28.70.74/trac/livraisons_t24", puLivraisons));
            mapProjetsTrac.put(tracHraccess, new CoupleDTO("http://172.28.70.74/trac/HR_ACCESS", puHraccess));
            mapProjetsTrac.put(tracMxp, new CoupleDTO("http://172.28.70.74/trac/MXP", puMxp));
            mapProjetsTrac.put(tracBfiCartagoIntranet, new CoupleDTO("http://172.28.70.74/trac/BFI_CARTAGO_INTRANET", puBfiCartagoIntranet));
            mapProjetsTrac.put(tracContentieux, new CoupleDTO("http://172.28.70.74/trac/contentieux", puContentieux));
            mapProjetsTrac.put(tracGtiWinserge, new CoupleDTO("http://172.28.70.74/trac/GTI_WINSERGE", puGtiWinserge));
            mapProjetsTrac.put(tracOgc, new CoupleDTO("http://172.28.70.74/trac/OGC", puOgc));
            mapProjetsTrac.put(tracBfiTitre, new CoupleDTO("http://172.28.70.74/trac/BFI_TITRE", puBfiTitre));
            mapProjetsTrac.put(tracIcr, new CoupleDTO("http://172.28.70.74/trac/ICR", puIcr));
            mapProjetsTrac.put("PROFILAGE", new CoupleDTO("http://172.28.70.246/trac/Profilage", "dbProfilagePU"));
            mapProjetsTrac.put("gestion_courrier", new CoupleDTO("http://172.28.70.246/trac/gestion_courrier", "dbGestionCourrierPU"));
            mapProjetsTrac.put("option_change", new CoupleDTO("http://172.28.70.246/trac/option_change", "dbOptionChangePU"));
            mapProjetsTrac.put("prospection_tre", new CoupleDTO("http://172.28.70.246/trac/prospection_tre", "dbProspectionTrePU"));
            mapProjetsTrac.put("DAV", new CoupleDTO("http://172.28.70.246/trac/DAV", "dbDavPU"));
            mapProjetsTrac.put(tracGestionDemandes, new CoupleDTO("http://172.28.70.246/trac/gestion_demandes", puGestionDesDemandes));
            mapProjetsTrac.put("GESTION_INCIDENTS_SI", new CoupleDTO("http://172.28.70.246/trac/GESTION_INCIDENTS_SI", puGestionDesIncidents));
        }
    }

    private static void lancerInitialisation(DataBaseTools dbTools) {
        System.out.println("LANCER INITIALISATION");
        try {
            setUsersLoginMatriculeMap(dbTools);
            allFonctionalitesMapOrdredByMatricule(dbTools);
            setPriorityOrder();
            setTypeLivrableCorrespondance();
            servlets.AfficherMessageEtatAvancement.setLogmessage("Chargement des paramÃ¨tres", Tools.getConnectedLogin());
            chargerTousLesEnvironnements(dbTools);
            chargerTousLesParametres(dbTools);
            chargerTousLesProjetsActifsProjet(dbTools);
            chargerTousLesProjetsActifsRelease(dbTools);
            chargerTousLesProjetsActifsUpgrade(dbTools);
            chargerTousLesProjetsActifsReleaseetProjet();
            getAllUsersNames(dbTools);
            chargerTousLesNiveauxProjets(dbTools);
            loadAllTypesRegles(dbTools);
            getAllGroupes(dbTools);
            getAllFoncionnalites(dbTools);
            chargerTousLesUsersGroups(dbTools);
            chargerTousLesCircuitsDetails(dbTools);
            loadAllMilestones(dbTools);
            intitOk = true;
            Audit audit = new Audit();
            audit.setUpdateTime(new Date());
            if (Tools.getConnectedLogin().equals("anonymousUser")) {
                audit.setAction("OPERATION_ON_DB");
            } else {
                audit.setAction("OPERATION_LOAD_DB");
            }

            dbTools.StoreObjectIntoDataBase(audit);
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }

    private static void setUsersLoginMatriculeMap(DataBaseTools dbTools) {
        matriculeLoginMap = new HashMap<String, String>();
        loginMatriculeMap = new HashMap<String, String>();
        List<Users> usersList = null;
        Query q = dbTools.em.createNamedQuery("Users.findAll");
        q.setHint(QueryHints.REFRESH, HintValues.TRUE);
        usersList = (List<Users>) q.getResultList();
        for (Users user : usersList) {
            matriculeLoginMap.put(user.getMatricule(), user.getLogin());
            loginMatriculeMap.put(user.getLogin(), user.getMatricule());
        }
    }
    private static Date lastExecutionDate;

    public synchronized static void initialisation() {
        try {
            initialisationVariables();
            if (lastExecutionDate == null) {
                lastExecutionDate = new Date("01/01/2010");
            }
            if (!Tools.testTempsEcoule(lastExecutionDate, 10)) {
                return;
            }
            lastExecutionDate = new Date();
            System.out.println("$$$ Start Initialisation $$$");
            if (PipeSystemeThread.logMap == null) {
                PipeSystemeThread.logMap = new LinkedHashMap<String, StringBuilder>();
            }
            String lastOperationName = "";
            //System.out.println("1");
            DataBaseTools dbTools = new DataBaseTools(puOvTools);
            //System.out.println("2");
            if (intitOk == false) {
                //System.out.println("3");
                Query q = dbTools.em.createNativeQuery("TRUNCATE TABLE audit");
                //System.out.println("4");
                q.executeUpdate();
                dbTools.em.getTransaction().commit();
                //System.out.println("5");
                lancerInitialisation(dbTools);
                //System.out.println("6");            
            } else {
                //select lastUpdateTime
                //System.out.println("7");
                Query q = dbTools.em.createNamedQuery("Audit.findActionByMaxUpdateTime");
                q.setHint(QueryHints.REFRESH, HintValues.TRUE);
                //System.out.println("8");
                try {
                    //System.out.println("9");
                    lastOperationName = (String) q.getSingleResult();
                    //System.out.println("10");
                } catch (Exception exep) {
                    exep.printStackTrace();
                    //System.out.println("11");
                    lancerInitialisation(dbTools);
                    //System.out.println("12");
                }
                if (lastOperationName.equals("OPERATION_ON_DB")) {
                    //System.out.println("13");
                    lancerInitialisation(dbTools);
                }
            }
            //System.out.println("14");
            dbTools.closeRessources();
            System.out.println("$$$ End Initialisation $$$");
            try {
                if (AfficherMessageEtatAvancement.logMessageMap != null) {
                    String connectedUser = Tools.getConnectedLogin();
                    if (!connectedUser.equals("anonymousUser")) {
                        logMessageMap.put(connectedUser, new StringBuilder());
                    }
                }
            } catch (Exception exep) {
                exep.printStackTrace();
                tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
            }
            if (true) {
                if (manageLogThread == null) {
                    manageLogThread = new ManageLogThread();
                    manageLogThread.start();
                }

                if (!manageLogThread.isAlive()) {
                    manageLogThread.start();
                }

                if (gestionHotfixProdThread == null || !gestionHotfixProdThread.isAlive()) {
                    gestionHotfixProdThread = new GestionHotfixProdThread();
                    gestionHotfixProdThread.start();
                }
                if (mustInitialiseThreadHF == true) {
                    mustInitialiseThreadHF = false;
                    gestionHotfixProdThread.threadIsAlive = false;
                    try {
                        gestionHotfixProdThread.interrupt();
                    } catch (Exception e) {
                    }
                    gestionHotfixProdThread = new GestionHotfixProdThread();
                    gestionHotfixProdThread.start();
                }

                //HarmonisationHotfixUpgradeThread
                if (HarmonisationHotfixUpgradeThread == null || !HarmonisationHotfixUpgradeThread.isAlive()) {
                    HarmonisationHotfixUpgradeThread = new HarmonisationHotfixUpgradeThread();
                    HarmonisationHotfixUpgradeThread.start();
                }
                if (mustInitialiseThreadHarmUpdate == true) {
                    mustInitialiseThreadHarmUpdate = false;
                    HarmonisationHotfixUpgradeThread.threadIsAlive = false;
                    try {
                        HarmonisationHotfixUpgradeThread.interrupt();
                    } catch (Exception e) {
                    }
                    HarmonisationHotfixUpgradeThread = new HarmonisationHotfixUpgradeThread();
                    HarmonisationHotfixUpgradeThread.start();
                }

                if (automatisationDeploiementIeThread == null || !automatisationDeploiementIeThread.isAlive()) {
                    automatisationDeploiementIeThread = new AutomatisationDeploiementIeThread();
                    automatisationDeploiementIeThread.start();
                }
                if (mustInitialiseThreadIE == true) {
                    mustInitialiseThreadIE = false;
                    automatisationDeploiementIeThread.threadIsAlive = false;
                    try {
                        automatisationDeploiementIeThread.interrupt();
                    } catch (Exception e) {
                    }
                    automatisationDeploiementIeThread = new AutomatisationDeploiementIeThread();
                    automatisationDeploiementIeThread.start();
                }


                if (automatisationDeploiementOvThread == null || !automatisationDeploiementOvThread.isAlive()) {
                    automatisationDeploiementOvThread = new AutomatisationDeploiementOvThread();
                    automatisationDeploiementOvThread.start();
                }
                if (mustInitialiseThreadOV == true) {
                    mustInitialiseThreadOV = false;
                    automatisationDeploiementOvThread.threadIsAlive = false;
                    try {
                        automatisationDeploiementOvThread.interrupt();
                    } catch (Exception e) {
                    }
                    automatisationDeploiementOvThread = new AutomatisationDeploiementOvThread();
                    automatisationDeploiementOvThread.start();
                }
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }

    public static void chargerTousLesCircuitsDetails(DataBaseTools dbTools, int... iterations) {
        List<Circuit> verificationList = new ArrayList<>();
        etatCircuitMap = new LinkedHashMap<>();
        Query q = dbTools.em.createNamedQuery("Circuit.findAll");
        q.setHint(QueryHints.REFRESH, HintValues.TRUE);
        List<Circuit> actifList = (List<Circuit>) q.getResultList();
        boolean problemeLecture = false;
        for (Circuit c : actifList) {
            etatCircuitMap.put(c.getNom(), c.getActif());
            if (!c.getActif().equals("ON") && !c.getActif().equals("OFF")) {
                verificationList.add(c);
            }
            try {
                String testNotNull = Configuration.etatCircuitMap.get(c.getNom()).trim();
            } catch (Exception e) {
                problemeLecture = true;
            }
        }
        //tester le format des variables horaires
        boolean parseOK = testParseDate(verificationList);
        if (parseOK == false || problemeLecture == true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex1) {
                ex1.printStackTrace();
            }
            if (iterations.length == 0) {
                chargerTousLesCircuitsDetails(dbTools, 0);
            } else if (iterations[0] < 10) {
                chargerTousLesCircuitsDetails(dbTools, (iterations[0] + 1));
            } else {
                String messageErreur = "Le chargement des dates saisies dans l'ecran Ticket Workflow a rencontré un problème, veuillez vérifier le format des dates passé en paramétre.<br>Voici les valeurs:<br>";
                for (Circuit c : verificationList) {
                    messageErreur += c.toString() + "<br>";
                }
                tools.Tools.traiterException(messageErreur);
            }
        }
    }

    private static boolean testParseDate(List<Circuit> verificationList) {
        SimpleDateFormat parseFormat = new SimpleDateFormat(Configuration.HEURE_FORMAT);
        boolean parseOK = true;
        for (Circuit c : verificationList) {
            Date date;
            try {
                date = parseFormat.parse(c.getActif());
            } catch (ParseException exep) {
                parseOK = false;
                break;
            }
        }
        return parseOK;
    }

    public static void chargerTousLesUsersGroups(DataBaseTools dbTools) {
        try {
            usersGroupMap = new LinkedHashMap<String, List<String>>();
            StringBuilder requete = new StringBuilder();
            requete.append("SELECT u.login, g.nom ");
            requete.append("FROM users u ");
            requete.append("LEFT JOIN users_has_groupe ug ");
            requete.append("ON u.login = ug.users_login ");
            requete.append("LEFT JOIN groupe g ");
            requete.append("ON g.nom = ug.groupe_nom");

            Query q = dbTools.em.createNativeQuery(requete.toString());
            List<Object[]> userByGroupList = (List<Object[]>) q.getResultList();

            List<String> auxList = null;
            for (Object[] t : userByGroupList) {
                if (!usersGroupMap.containsKey(t[0].toString())) {
                    auxList = new ArrayList<String>();
                    auxList.add(t[1].toString());
                    usersGroupMap.put(t[0].toString(), auxList);
                } else {
                    auxList = usersGroupMap.get(t[0].toString());
                    auxList.add(t[1].toString());
                    usersGroupMap.put(t[0].toString(), auxList);
                }
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }

    public static void chargerTousLesEnvironnements(DataBaseTools dbTools) {
        try {
//        String connectedUser = Tools.getConnectedLogin();   
            List<Environnement> listeEnvironnementsPhysiques = null;

            StringBuilder requete = new StringBuilder();
            requete.append("SELECT u.login, e.nom ");
            requete.append("FROM environnement e ");
            requete.append("LEFT JOIN  groupe_has_environnement ge ");
            requete.append("ON e.nom = ge.environnement_nom ");
            requete.append("LEFT JOIN groupe g ");
            requete.append("ON g.nom = ge.groupe_name ");
            requete.append("LEFT JOIN users_has_groupe ug ");
            requete.append("ON g.nom = ug.groupe_nom ");
            requete.append("LEFT JOIN users u ");
            requete.append("ON ug.users_login = u.login ");
            requete.append("WHERE u.login <>  'NULL' ");
            requete.append("ORDER BY e.url AND u.login");

            //charger tous les environnements physiques de chaque utilisateur (selon le group cad les env physiques dont il a le droit)
            Query q = dbTools.em.createNativeQuery(requete.toString());
            List<Object[]> dutyEnvNameList = (List<Object[]>) q.getResultList();

            //charger tous les environnements physiques
            q = dbTools.em.createNamedQuery("Environnement.findAll");
            q.setHint(QueryHints.REFRESH, HintValues.TRUE);
            listeEnvironnementsPhysiques = (List<Environnement>) q.getResultList();
            allEnvironnementMap = new LinkedHashMap<String, Environnement>();
            for (Environnement env : listeEnvironnementsPhysiques) {
                allEnvironnementMap.put(env.getNom(), env);
            }

            //structuration du rÃ©sultat
            environnementMapDutyGroup = new LinkedHashMap<String, Map<String, Environnement>>();
            Map<String, Environnement> envMapAux;
            for (Object[] tab : dutyEnvNameList) {
                String userLogin = tab[0].toString();
                String envName = tab[1].toString();
                if (!environnementMapDutyGroup.containsKey(userLogin)) {
                    envMapAux = new LinkedHashMap<String, Environnement>();
                } else {
                    envMapAux = environnementMapDutyGroup.get(userLogin);
                }
                envMapAux.put(envName, allEnvironnementMap.get(envName));
                environnementMapDutyGroup.put(userLogin, envMapAux);
            }
            environnementMapDutyGroup.put("anonymousUser", new LinkedHashMap<String, Environnement>());
            //tri de la map selon l'adresse ip des environnements
            for (Map.Entry<String, Map<String, Environnement>> entry : environnementMapDutyGroup.entrySet()) {
                ValueComparator comparateur = new ValueComparator(entry.getValue());
                TreeMap<String, Environnement> mapTriee = new TreeMap<String, Environnement>(comparateur);
                mapTriee.putAll(entry.getValue());
                entry.setValue(mapTriee);
            }
            /////////////////////////////////////////////////////////////////////////////
            Environnement env = null;
            EnvironnementDTO envDto = null;
            Map<String, EnvironnementDTO> envMapAuxDto;
            environnementDTOMapUserHasEnvironnement = new LinkedHashMap<String, Map<String, EnvironnementDTO>>();

            //charger les browserUserName et les browserPasswords de chaque user
            q = dbTools.em.createNamedQuery("UsersHasEnvironnement.findAll");
            q.setHint(QueryHints.REFRESH, HintValues.TRUE);
            List<UsersHasEnvironnement> listeUsersHasEnvironnement = (List<UsersHasEnvironnement>) q.getResultList();

            for (UsersHasEnvironnement usersHasEnvironnement : listeUsersHasEnvironnement) {
                env = usersHasEnvironnement.getEnvironnement();
                envDto = new EnvironnementDTO(env.getNom(), env.getAbreviationNom(), env.getType(), env.getUrl(), env.getEnvUserName(), env.getEnvPassword(), usersHasEnvironnement.getBrowserLogin(), usersHasEnvironnement.getBrowsrPassword(), 21);

                if (!environnementDTOMapUserHasEnvironnement.containsKey(usersHasEnvironnement.getUsers().getLogin())) {
                    envMapAuxDto = new LinkedHashMap<String, EnvironnementDTO>();
                    envMapAuxDto.put(env.getNom(), envDto);
                    environnementDTOMapUserHasEnvironnement.put(usersHasEnvironnement.getUsers().getLogin().toString(), envMapAuxDto);
                } else {
                    envMapAuxDto = environnementDTOMapUserHasEnvironnement.get(usersHasEnvironnement.getUsers().getLogin());
                    envMapAuxDto.put(env.getNom(), envDto);
                    environnementDTOMapUserHasEnvironnement.put(usersHasEnvironnement.getUsers().getLogin(), envMapAuxDto);
                }
            }
            environnementDTOMapUserHasEnvironnement.put("anonymousUser", new LinkedHashMap<String, EnvironnementDTO>());
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }

    public static void loadAllMilestones(DataBaseTools dbTools) {
        allMilestonesEmailMap = new TreeMap<String, String>();
        Query q = dbTools.em.createNamedQuery("Destinationparmilestone.findAll");
        q.setHint(QueryHints.REFRESH, HintValues.TRUE);
        List<Destinationparmilestone> milestonesList = (List<Destinationparmilestone>) q.getResultList();
        String oldMiletoneEmail = null;
        for (Destinationparmilestone milestone : milestonesList) {
            if (allMilestonesEmailMap.containsKey(milestone.getMilestoneName())) {
                oldMiletoneEmail = allMilestonesEmailMap.get(milestone.getMilestoneName());
                allMilestonesEmailMap.put(milestone.getMilestoneName(), oldMiletoneEmail + " " + milestone.getMilestoneEmail());
            } else {
                allMilestonesEmailMap.put(milestone.getMilestoneName(), milestone.getMilestoneEmail());
            }
        }
    }

    public static void getAllFoncionnalites(DataBaseTools dbTools) {
        try {
            allFonctionalitesMap = new LinkedHashMap<String, Fonctionalite>();
            Query q = dbTools.em.createNamedQuery("Fonctionalite.findAll");
            q.setHint(QueryHints.REFRESH, HintValues.TRUE);
            List<Fonctionalite> fonctionnalitesList = (List<Fonctionalite>) q.getResultList();
            allFonctionalitesMapOrdredByType = new LinkedHashMap<String, List<Fonctionalite>>();
            List<Fonctionalite> auxFonct = null;
            for (Fonctionalite fct : fonctionnalitesList) {
                allFonctionalitesMap.put(fct.getName(), fct);

                if (allFonctionalitesMapOrdredByType.containsKey(fct.getType())) {
                    allFonctionalitesMapOrdredByType.get(fct.getType()).add(fct);
                } else {
                    auxFonct = new ArrayList<Fonctionalite>();
                    auxFonct.add(fct);
                    allFonctionalitesMapOrdredByType.put(fct.getType(), auxFonct);
                }
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }

    public static void getAllGroupes(DataBaseTools dbTools) {
        try {
            groupMap = new LinkedHashMap<String, Groupe>();
            Query q = dbTools.em.createNamedQuery("Groupe.findAll");
            q.setHint(QueryHints.REFRESH, HintValues.TRUE);
            List<Groupe> groupeList = (List<Groupe>) q.getResultList();
            for (Groupe groupe : groupeList) {
                groupMap.put(groupe.getNom(), groupe);
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }

    public static void chargerTousObjetsDeChaqueProjet(String connectedUser) {
        try {

            //connectedUser = Tools.getConnectedLogin();

            servlets.AfficherMessageEtatAvancement.setLogmessage("Chargement de tous les objets de toutes les Branches individuelles", connectedUser);
            mapObjetsParProjet = new LinkedHashMap<String, List<String>>();
            Tools tools = new Tools();
            T24Scripts t24Scripts = new T24Scripts();
            String resultatListerFichiers;
            FtpTools ftpTools = new FtpTools();
            String objectName;
            String projectName;
            String auxTab[];

            String commande = "find ";
            for (String projName : projetsActifsCircuitReleaseEtProjetList) {
                commande += " ./" + projName;
                mapObjetsParProjet.put(projName, new ArrayList<String>());
            }
            long millisecondsTime = System.currentTimeMillis();
            String fileName = "listObjets_" + millisecondsTime + ".txt";
            commande += " -type f |awk '{ print \"ls \",$1 }'|grep -v svn|bash > /work/" + fileName;

            Tools.showConsolLog(commande);

            EnvironnementDTO environnementSource = environnementDTOMapUserHasEnvironnement.get(connectedUser).get("VERSIONNING");
            boolean fileCreadted = false;
            int nbrtentativeTestExistanceFichier = 0;
            while (!fileCreadted && nbrtentativeTestExistanceFichier < 10) {
                resultatListerFichiers = t24Scripts.executerCommandeListEnvironnement(environnementSource, parametresList.get("cheminDepotDeltaProjets"), commande);
                //début test d'existance de fichier
                fileCreadted = t24Scripts.testExistanceFichier(environnementSource, fileName, "/work");
                //fin test d'existance de fichier
                nbrtentativeTestExistanceFichier++;
            }
            ftpTools.downloadFile(environnementSource, "/work", fileName);


            String[] fileTab = tools.convertFileContentToTab(parametresList.get("espaceLocal") + "/listObjets_" + millisecondsTime + ".txt", connectedUser);
            Tools.showConsolLog("__________________________");
            for (int i = 0; i < fileTab.length; i++) {
                if ((fileTab[i].trim().length() > 0)) {
                    if (fileTab[i].contains("/")) {
                        objectName = fileTab[i].trim();
                        auxTab = objectName.split("/");
                        objectName = auxTab[auxTab.length - 1];
                        projectName = auxTab[1];
                        Tools.showConsolLog(projectName + " :: " + objectName);
                        mapObjetsParProjet.get(projectName).add(objectName);
                        if (projetsActifsCircuitReleaseList.contains(projectName)) {
                            ensembleSetObjetsRelease.add(objectName);
                        } else if (projetsActifsCircuitProjetList.contains(projectName)) {
                            ensembleSetObjetsProjet.add(objectName);
                        }
                    }
                }
            }
            mapObjetsParProjet.put("RELEASE", new ArrayList<String>(ensembleSetObjetsRelease));
            mapObjetsParProjet.put("PROJET", new ArrayList<String>(ensembleSetObjetsProjet));
            //servlets.AfficherMessageEtatAvancement.setLogmessage("");
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }

    public static String getNomProjetParAbreviation(String abreviation) {
        String nomProjet = null;
        try {
            nomProjet = abrevNomProjProp.get(abreviation);
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return nomProjet;
    }

    public static String getAbreviationProjetParNiveauProjet(String niveauProjet) {
        String res = null;
        try {
            res = nomProjAbreviationProp.get(niveauProjet);
        } catch (Exception e) {
            res = niveauProjet;
        }
        return res;
    }

    public static void loadAllTypesRegles(DataBaseTools dbTools) {
        try {
            alltypesReglesMap.clear();
            typesReglesMap.clear();
            observationReglesMap.clear();
            typesReglesOrderedByRemarquesMap.clear();
            Query q = dbTools.em.createNamedQuery("TypesRegle.findAll");
            q.setHint(QueryHints.REFRESH, HintValues.TRUE);
            List<TypesRegle> listeObjetsRegles = (List<TypesRegle>) q.getResultList();
            for (TypesRegle obj : listeObjetsRegles) {
                if (obj.getNature().equals("TYPE_T24")) {
                    typesReglesMap.put(obj.getCle(), obj.getRegle());
                    Map<String, String> mapRegleDetails = null;
                    if (typesReglesOrderedByRemarquesMap.containsKey(obj.getRemarque())) {
                        mapRegleDetails = typesReglesOrderedByRemarquesMap.get(obj.getRemarque());
                    } else {
                        mapRegleDetails = new HashMap<>();
                    }
                    mapRegleDetails.put(obj.getCle(), obj.getRegle());
                    typesReglesOrderedByRemarquesMap.put(obj.getRemarque(), mapRegleDetails);
                } else {
                    observationReglesMap.put(obj.getCle(), obj.getRegle());
                }
                alltypesReglesMap.put(obj.getCle(), obj);
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }

    private static String testOs() {
        Process proc = null;
        String os = null;
        try {
            String untarCommand = "cmd /c  cmd.exe /K \"cd c:\\ && dir\"";
            proc = Runtime.getRuntime().exec(untarCommand);
            os = "WINDOWS";
        } catch (Exception ex) {
            os = "LINUX";
        }
        return os;
    }

    public static void chargerTousLesParametres(DataBaseTools dbTools) {
        try {
            parametresList = new LinkedHashMap<String, String>();
            Query q = dbTools.em.createNamedQuery("Parametres.findAll");
            q.setHint(QueryHints.REFRESH, HintValues.TRUE);
            List<Parametres> parametres = (List<Parametres>) q.getResultList();
            for (Parametres param : parametres) {
                parametresList.put(param.getCle(), param.getValeur());
            }
            String os = testOs();
            if (os.equals("WINDOWS")) {
                parametresList.put("espaceLocal", parametresList.get("espaceLocalWindows"));
                parametresList.put("OS", "WINDOWS");
            } else {
                parametresList.put("espaceLocal", parametresList.get("espaceLocalLinux"));
                parametresList.put("OS", "LINUX");
            }

            parametresList.put("espaceLocal", parametresList.get("espaceLocal").replace("/", System.getProperty("file.separator")));
            parametresList.put("espaceLocal", parametresList.get("espaceLocal").replace("\\", System.getProperty("file.separator")));
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }

    public static void chargerTousLesProjetsActifsProjet(DataBaseTools dbTools) {
        try {
            projetsActifsCircuitProjetList = new ArrayList<String>();
            Query q = dbTools.em.createNamedQuery("Niveauprojet.findAllAbreviationsByActifs");
            q.setParameter("actif", "CP");
            q.setHint(QueryHints.REFRESH, HintValues.TRUE);
            projetsActifsCircuitProjetList = (List<String>) q.getResultList();
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }

    public static void chargerTousLesProjetsActifsRelease(DataBaseTools dbTools) {
        try {
            projetsActifsCircuitReleaseList = new ArrayList<String>();
            Query q = dbTools.em.createNamedQuery("Niveauprojet.findAllAbreviationsByActifs");
            q.setParameter("actif", "CR");
            q.setHint(QueryHints.REFRESH, HintValues.TRUE);
            projetsActifsCircuitReleaseList = (List<String>) q.getResultList();
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }

    public static void chargerTousLesProjetsActifsUpgrade(DataBaseTools dbTools) {
        try {
            projetsActifsCircuitUpgradeList = new ArrayList<String>();
            Query q = dbTools.em.createNamedQuery("Niveauprojet.findAllAbreviationsByActifs");
            q.setParameter("actif", "CU");
            q.setHint(QueryHints.REFRESH, HintValues.TRUE);
            projetsActifsCircuitUpgradeList = (List<String>) q.getResultList();
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }

    public static void chargerTousLesProjetsActifsReleaseetProjet() {
        try {
            projetsActifsCircuitReleaseEtProjetList = new ArrayList<String>();
            projetsActifsCircuitReleaseEtProjetList.addAll(projetsActifsCircuitProjetList);
            projetsActifsCircuitReleaseEtProjetList.addAll(projetsActifsCircuitReleaseList);
            projetsActifsCircuitReleaseEtProjetList.addAll(projetsActifsCircuitUpgradeList);
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }

    public static void resetDeploiementMap(String connectedUser) {
        try {
            Map<String, Map<String, Integer>> userRestoreMap = restoreMap.get(connectedUser);
            for (Map.Entry<String, Map<String, Integer>> entryUsers : userRestoreMap.entrySet()) {
                for (Map.Entry<String, Integer> entryEnv : entryUsers.getValue().entrySet()) {
                    entryEnv.setValue(-1);
                }
            }
            restoreMap.put(connectedUser, userRestoreMap);
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }

    public static void getAllUsers() {
        try {
            List<Users> usersList = new ArrayList<>();
            usersMap = new LinkedHashMap<>();
            DataBaseTools dbTools = new DataBaseTools(puOvTools);
            if (allEnvironnementMap == null) {
                chargerTousLesEnvironnements(dbTools);
            }
            Query q = dbTools.em.createNamedQuery("Users.findAll");
            q.setHint(QueryHints.REFRESH, HintValues.TRUE);
            usersList = (List<Users>) q.getResultList();
            for (Users user : usersList) {
                usersMap.put(user.getLogin(), user);
            }
            dbTools.closeRessources();


            restoreMap = new LinkedHashMap<>();

            LinkedHashMap<String, Integer> restors = new LinkedHashMap<>();
            restors.put("RESTORE1", -1);
            restors.put("RESTORE2", -1);
            restors.put("RESTORE3", -1);

            LinkedHashMap<String, Map<String, Integer>> envRestors = new LinkedHashMap<>();
            for (Map.Entry<String, Environnement> entry : allEnvironnementMap.entrySet()) {
                envRestors.put(entry.getKey(), (Map<String, Integer>) restors.clone());
            }

            for (Users user : usersList) {
                restoreMap.put(user.getLogin(), (HashMap<String, Map<String, Integer>>) envRestors.clone());
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }

    public static void getAllUsersNames(DataBaseTools dbTools) {
        try {
            Query q = dbTools.em.createNamedQuery("Users.findAllNames");
            q.setHint(QueryHints.REFRESH, HintValues.TRUE);
            usersNamesList = (List<String>) q.getResultList();
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }

    public static void chargerTousLesNiveauxProjets(DataBaseTools dbTools) {
        List<Niveauprojet> projetList = null;
        try {
            Query q = dbTools.em.createNamedQuery("Niveauprojet.findAll");
            q.setHint(QueryHints.REFRESH, HintValues.TRUE);
            projetList = (List<Niveauprojet>) q.getResultList();
            for (Niveauprojet nivProj : projetList) {
                abrevNomProjProp.put(nivProj.getAbreviation(), nivProj.getNom());
                nomProjAbreviationProp.put(nivProj.getNom(), nivProj.getAbreviation());
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }

    public static void setPriorityOrder() {
        try {
            if (priorityList == null) {
                priorityList = new LinkedHashMap<>();
                priorityList.put("A DEFINIR", 0);
                priorityList.put("DEVELOPPEMENT", 0);
                priorityList.put("RETOURNEE", 0);
                priorityList.put("QUALIFIEE", 0);
                priorityList.put("CLARIFIEE", 0);
                priorityList.put("INFORMATION LIVREE", 0);
                priorityList.put("EN VEILLE", 0);
                priorityList.put("ANNULEE", 0);
                priorityList.put("CERTIFIEE", 0);
                priorityList.put("A QUALIFIER", 0);
                priorityList.put("A CERTIFIER", 0);
                priorityList.put("A LIVRER", 0);
                priorityList.put("REMPLACEE", 0);
                priorityList.put("APPLIQUEE SUR PROD", 0);
                priorityList.put("LIVRAISON CONFIRMEE", 0);
                priorityList.put("INFORMATION REQUISE", 0);
                priorityList.put("CONFIRMATION RETOUR DEV", 0);
                priorityList.put("POST POSEE", 0);
            }

            if (statusSet == null) {
                statusSet = new TreeSet<>();
                statusSet.add("a_adapter_sur_devr_avant_harmonisation");
                statusSet.add("a_qualifier");
                statusSet.add("accepted");
                statusSet.add("assigned");
                statusSet.add("besoin_info");
                statusSet.add("closed");
                statusSet.add("deployee_sur_prod");
                statusSet.add("deployee_sur_recette_pour_harmonisation");
                statusSet.add("info_livree");
                statusSet.add("new");
                statusSet.add("objet_livre");
                statusSet.add("objet_livre_pour_harmonisation");
                statusSet.add("pret_pour_deploiement");
                statusSet.add("pret_pour_deploiement_sur_recette_harmonisation");
                statusSet.add("pret_pour_mise_en_prod");
                statusSet.add("problem_packaging");
                statusSet.add("probleme_deploiement");
                statusSet.add("qualifiee");
                statusSet.add("retournee");
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }

    static void setTypeLivrableCorrespondance() {
        if (mapCorrespondanceTypesLivrable == null) {
            mapCorrespondanceTypesLivrable = new HashMap<>();
            mapCorrespondanceTypesLivrable.put("TransfertFichier", "TRANSFERT FICHIERS");
            mapCorrespondanceTypesLivrable.put("T24", "OBJETS T24");
            mapCorrespondanceTypesLivrable.put("SuppressionObjT24", "SUPRESSION OBJETS T24");
            mapCorrespondanceTypesLivrable.put("StreamServeTransactionnel", "STREAMSERV TRANSACTIONNEL");
            mapCorrespondanceTypesLivrable.put("StreamServeBatch", "STREAMSERV BATCH");
            mapCorrespondanceTypesLivrable.put("ServiceT24", "DOLLAR U");
            mapCorrespondanceTypesLivrable.put("RoutineT24", "EXECUTION ROUTINE T24");
            mapCorrespondanceTypesLivrable.put("ReversObjT24", "REVERSE OBJETS T24");
            mapCorrespondanceTypesLivrable.put("RebuildSystem", "REBUILD SYSTEM");
            mapCorrespondanceTypesLivrable.put("ExecutionProgrammeJSH", "EXECUTION PROGRAMME JSH");
            mapCorrespondanceTypesLivrable.put("CreationIndex", "CREATION INDEXES");
            mapCorrespondanceTypesLivrable.put("CreationDossiers", "CREATION DOSSIERS");
            mapCorrespondanceTypesLivrable.put("CreationCompte", "CREATION COMPTES");
            mapCorrespondanceTypesLivrable.put("BrowserT24", "BROWSER T24");
            mapCorrespondanceTypesLivrable.put("BrowserIB", "BROWSER IB");
            mapCorrespondanceTypesLivrable.put("AutreLivrable", "AUTRE LIVRABLE");
        }
    }
}

class ValueComparator implements Comparator<String> {

    Map<String, Environnement> base;

    public ValueComparator(Map<String, Environnement> base) {
        this.base = base;
    }

    public int compare(String a, String b) {
        int resultat;
        try {
            if (base.get(a).getUrl().compareTo(base.get(b).getUrl()) > 0) {
                resultat = 1;
            } else {
                resultat = -1;
            }
        } catch (Exception ex) {
            resultat = -1;
        }
        return resultat;
    }
}