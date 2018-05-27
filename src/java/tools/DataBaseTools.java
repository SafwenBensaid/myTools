/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import static servlets.AfficherMessageEtatAvancement.logMessageMapInitialised;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Persistence;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import org.eclipse.persistence.jpa.JpaEntityManager;
import static servlets.AfficherMessageEtatAvancement.deploiementEnCours;
import threads.ManageLogThread;
import static servlets.GestionDemandesMetiersServlet.objectsListToBePersistedOnTheDataBaseTrac;
import static servlets.GestionDemandesMetiersServlet.objectsListToBeMergedOnTheDataBaseTrac;

/**
 *
 * @author 04486
 */
public class DataBaseTools {

    public static Map<String, EntityManagerFactory> emfMap = null;
    @PersistenceContext
    public EntityManager em;
    private long openConnexionTime = 0;
    private String persistanceUnit;

    public DataBaseTools(String persistanceUnit) {
        this.persistanceUnit = persistanceUnit;
        Map<String, String> persistanceUnitsMap = null;
        setIdConnexion();
        //get caller methode name
        String callerMethodName = "";
        try {
            String callerClass = new Exception().getStackTrace()[1].getClassName().toString();
            String callerMethod = new Exception().getStackTrace()[1].getMethodName().toString();
            callerMethodName = callerClass + "." + callerMethod;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        ManageLogThread.ecrireLogFichier("Connexion_" + persistanceUnit, "\n" + openConnexionTime + " : " + callerMethodName, persistanceUnit);
        try {
            String connectedUser = null;
            try {
                connectedUser = Tools.getConnectedLogin();
            } catch (Exception ex) {
            }
            persistanceUnitsMap = Configuration.persistanceUnitsMap;
            if (logMessageMapInitialised && Configuration.persistanceUnitsMap != null && Configuration.persistanceUnitsMap.containsKey(persistanceUnit) && connectedUser != null) {
                if (connectedUser != null) {
                    if (deploiementEnCours.get(connectedUser) != null) {
                        Map<String, Boolean> mapDepUser = deploiementEnCours.get(connectedUser);
                        boolean depEnCours = false;
                        for (Map.Entry<String, Boolean> entry : mapDepUser.entrySet()) {
                            depEnCours = depEnCours || entry.getValue();
                        }
                        /*
                         if (depEnCours == false) {
                         servlets.AfficherMessageEtatAvancement.setLogmessage("Connexion à la base de données " + Configuration.persistanceUnitsMap.get(persistanceUnit), connectedUser);
                         }
                         */
                    }
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (emfMap == null) {
            emfMap = new HashMap<String, EntityManagerFactory>();
        }
        try {
            if (emfMap.get(persistanceUnit) == null) {
                emfMap.put(persistanceUnit, Persistence.createEntityManagerFactory(persistanceUnit));
            }
            em = emfMap.get(persistanceUnit).createEntityManager();
            clearCache(em);
            em.getTransaction().begin();
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException("Probleme de connexion à la base de données " + persistanceUnit + "\n" + tools.Tools.getStackTrace(exep));
        }
    }

    private void setIdConnexion() {
        openConnexionTime = System.currentTimeMillis();
    }

    public static void clearCache(EntityManager em) {
        try {
            ((JpaEntityManager) em.getDelegate()).getServerSession().getIdentityMapAccessor().initializeAllIdentityMaps();
            ((JpaEntityManager) em.getDelegate()).getServerSession().getIdentityMapAccessor().clearQueryCache();
            em.clear();
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }

    public void persist(Object objet) {
        try {
            if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
            }
            em.persist(objet);
            em.getTransaction().commit();
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
            em.getTransaction().rollback();
        }
    }

    public void updateObjectList(List<? extends Object> objectList) {
        try {
            if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
            }
            for (Object objet : objectList) {
                em.merge(objet);
            }
            em.getTransaction().commit();
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
            em.getTransaction().rollback();
        }
    }

    public void update(Object objet) {
        try {
            if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
            }
            em.merge(objet);
            em.getTransaction().commit();
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
            em.getTransaction().rollback();
        }
    }

    public void closeRessources() {
        //get caller methode name
        String callerMethodName = "";
        try {
            String callerClass = new Exception().getStackTrace()[1].getClassName().toString();
            String callerMethod = new Exception().getStackTrace()[1].getMethodName().toString();
            callerMethodName = callerClass + "." + callerMethod;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        ManageLogThread.ecrireLogFichier("Deconnexion_" + persistanceUnit, "\n" + openConnexionTime + " : " + callerMethodName, persistanceUnit);
        if (logMessageMapInitialised) {
            String connectedUser = null;
            try {
                connectedUser = Tools.getConnectedLogin();
            } catch (Exception ex) {
            }
            // si un dep // est en cours, ne pas ecraser le log
            if (connectedUser != null) {
                if (deploiementEnCours.get(connectedUser) != null) {
                    Map<String, Boolean> mapDepUser = deploiementEnCours.get(connectedUser);
                    boolean depEnCours = false;
                    for (Map.Entry<String, Boolean> entry : mapDepUser.entrySet()) {
                        depEnCours = depEnCours || entry.getValue();
                    }
                }
            }
        }
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
        if (em.isOpen()) {
            em.close();
        }
    }

    public void StoreObjectIntoDataBase(Object objet) {
        try {
            persist(objet);
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }

    public void remove(Object object) {
        try {
            if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
            }
            em.remove(object);
            em.getTransaction().commit();
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
            em.getTransaction().rollback();
        }
    }

    public void updateDataBaseTrac(DataBaseTools dbTools, int... iterations) {
        try {
            //Base de données TRAC
            if (!dbTools.em.getTransaction().isActive()) {
                dbTools.em.getTransaction().begin();
            }

            if (objectsListToBePersistedOnTheDataBaseTrac != null && !objectsListToBePersistedOnTheDataBaseTrac.isEmpty()) {
                for (Object obj : objectsListToBePersistedOnTheDataBaseTrac) {
                    dbTools.em.persist(obj);
                }
            }

            if (objectsListToBeMergedOnTheDataBaseTrac != null && !objectsListToBeMergedOnTheDataBaseTrac.isEmpty()) {
                for (Object obj : objectsListToBeMergedOnTheDataBaseTrac) {
                    dbTools.em.merge(obj);
                }
            }

        } catch (Exception ex) {
            if (dbTools.em.getTransaction().isActive()) {
                dbTools.em.getTransaction().rollback();
            }
            if (dbTools.em.getTransaction().isActive()) {
                dbTools.em.getTransaction().rollback();
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex1) {
                ex.printStackTrace();
            }
            if (iterations.length == 0) {
                updateDataBaseTrac(dbTools, 0);
            } else if (iterations[0] < 10) {
                System.out.println(iterations[0] + 1);
                updateDataBaseTrac(dbTools, iterations[0] + 1);
            } else {
                ex.printStackTrace();
                Tools.traiterException(Tools.getStackTrace(ex));
            }
        } finally {
            dbTools.em.getTransaction().commit();
            dbTools.closeRessources();
        }
    }
}
