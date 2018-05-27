/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

/**
 *
 * @author karim
 */
public class CrudTools<T> {

    public CrudTools(String pu) {
        emf = Persistence.createEntityManagerFactory(pu);
        em = emf.createEntityManager();
        em.getTransaction().begin();
    }
    public EntityManagerFactory emf;
    public EntityManager em;

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

    public void closeRessources() {
        try {
            em.close();
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }

    public void StoreObjectIntoDataBase(Object objet) {
        persist(objet);
    }

    public void Remove(Object object) {
        try {
            em.remove(object);
            em.getTransaction().commit();
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
            em.getTransaction().rollback();
        }
    }

    public List<T> findAll(String nomclasse) {
        List<T> liste = null;
        try {
            Query q = em.createNamedQuery(nomclasse + ".findAll");
            q.setHint(QueryHints.REFRESH, HintValues.TRUE);
            liste = (List<T>) q.getResultList();
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return liste;
    }

    public T findById(String nomclasse, String id) {
        T resultat = null;
        try {
            Query q = em.createNamedQuery(nomclasse + ".findByNom").setParameter("nom", id);
            q.setHint(QueryHints.REFRESH, HintValues.TRUE);
            resultat = (T) q.getSingleResult();
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return resultat;
    }

    public void update(String id, String champ, String valeur, String table) {
        try {
            Query q = em.createNativeQuery("UPDATE " + table.toLowerCase() + " SET " + champ + "='" + valeur + "' WHERE nom='" + id + "';");
            int s = q.executeUpdate();
            em.getTransaction().commit();
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }

    public int find(String table, String champ, String val) {
        int resultat = 0;
        try {
            Query q = em.createNativeQuery("SELECT * FROM " + table.toLowerCase() + " WHERE " + champ + "='" + val + "';");
            resultat = q.getResultList().size();
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return resultat;
    }

    public void updateUsers(int id, String champ, String valeur, String table) {
        try {
            Query q = em.createNativeQuery("UPDATE " + table.toLowerCase() + " SET " + champ + "='" + valeur + "' WHERE ID=" + id + ";");
            int s = q.executeUpdate();
            em.getTransaction().commit();
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }

    public void updateParametre(String id, String champ, String valeur, String table) {
        try {
            Query q = em.createNativeQuery("UPDATE " + table.toLowerCase() + " SET " + champ + "='" + valeur + "' WHERE cle='" + id + "';");
            int s = q.executeUpdate();
            em.getTransaction().commit();
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }

    public T findUser(String nomclasse, int id) {
        T resultat = null;
        try {
            Query q = em.createNamedQuery(nomclasse + ".findById").setParameter("id", id);
            q.setHint(QueryHints.REFRESH, HintValues.TRUE);
            resultat = (T) q.getSingleResult();
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return resultat;
    }

    public T findParametre(String id) {
        T resultat = null;
        try {
            Query q = em.createNamedQuery("Parametres.findByCle").setParameter("cle", id);
            q.setHint(QueryHints.REFRESH, HintValues.TRUE);
            resultat = (T) q.getSingleResult();
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return resultat;
    }

    public T findNivProjet(String id) {
        T resultat = null;
        try {
            Query q = em.createNamedQuery("Niveauprojet.findByNom").setParameter("nom", id);
            q.setHint(QueryHints.REFRESH, HintValues.TRUE);
            resultat = (T) q.getSingleResult();
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return resultat;
    }

    public List<String> getAllRoles() {
        List<String> liste = null;
        try {
            Query q = em.createNativeQuery("SELECT DISTINCT role FROM users");
            liste = q.getResultList();
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return liste;
    }
}
