/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entitiesMysql;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author 04486
 */
@Entity
@Table(name = "environnement")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Environnement.findAll", query = "SELECT e FROM Environnement e ORDER BY e.nom"),
    @NamedQuery(name = "Environnement.findByNom", query = "SELECT e FROM Environnement e WHERE e.nom = :nom"),
    @NamedQuery(name = "Environnement.findByAbreviationNom", query = "SELECT e FROM Environnement e WHERE e.abreviationNom = :abreviationNom"),
    @NamedQuery(name = "Environnement.findByType", query = "SELECT e FROM Environnement e WHERE e.type = :type"),
    @NamedQuery(name = "Environnement.findByUrl", query = "SELECT e FROM Environnement e WHERE e.url = :url"),
    @NamedQuery(name = "Environnement.findByEnvUserName", query = "SELECT e FROM Environnement e WHERE e.envUserName = :envUserName"),
    @NamedQuery(name = "Environnement.findByEnvPassword", query = "SELECT e FROM Environnement e WHERE e.envPassword = :envPassword"),
    @NamedQuery(name = "Environnement.findByPort", query = "SELECT e FROM Environnement e WHERE e.port = :port")})
public class Environnement implements Serializable, Comparable<Environnement> {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "nom")
    private String nom;
    @Basic(optional = false)
    @Column(name = "abreviationNom")
    private String abreviationNom;
    @Basic(optional = false)
    @Column(name = "type")
    private String type;
    @Basic(optional = false)
    @Column(name = "url")
    private String url;
    @Basic(optional = false)
    @Column(name = "envUserName")
    private String envUserName;
    @Basic(optional = false)
    @Column(name = "envPassword")
    private String envPassword;
    @Basic(optional = false)
    @Column(name = "port")
    private int port;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "environnement")
    private List<GroupeHasEnvironnement> groupeHasEnvironnementList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "environnement")
    private List<UsersHasEnvironnement> usersHasEnvironnementList;

    public Environnement() {
    }

    public Environnement(String nom) {
        this.nom = nom;
    }

    public Environnement(String nom, String abreviationNom, String type, String url, String envUserName, String envPassword, int port) {
        this.nom = nom;
        this.abreviationNom = abreviationNom;
        this.type = type;
        this.url = url;
        this.envUserName = envUserName;
        this.envPassword = envPassword;
        this.port = port;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAbreviationNom() {
        return abreviationNom;
    }

    public void setAbreviationNom(String abreviationNom) {
        this.abreviationNom = abreviationNom;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEnvUserName() {
        return envUserName;
    }

    public void setEnvUserName(String envUserName) {
        this.envUserName = envUserName;
    }

    public String getEnvPassword() {
        return envPassword;
    }

    public void setEnvPassword(String envPassword) {
        this.envPassword = envPassword;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @XmlTransient
    public List<GroupeHasEnvironnement> getGroupeHasEnvironnementList() {
        return groupeHasEnvironnementList;
    }

    public void setGroupeHasEnvironnementList(List<GroupeHasEnvironnement> groupeHasEnvironnementList) {
        this.groupeHasEnvironnementList = groupeHasEnvironnementList;
    }

    @XmlTransient
    public List<UsersHasEnvironnement> getUsersHasEnvironnementList() {
        return usersHasEnvironnementList;
    }

    public void setUsersHasEnvironnementList(List<UsersHasEnvironnement> usersHasEnvironnementList) {
        this.usersHasEnvironnementList = usersHasEnvironnementList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nom != null ? nom.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Environnement)) {
            return false;
        }
        Environnement other = (Environnement) object;
        if ((this.nom == null && other.nom != null) || (this.nom != null && !this.nom.equals(other.nom))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entitiesMysql.Environnement[ nom=" + nom + " ]";
    }

    @Override
    public int compareTo(Environnement o) {
        String envName1 = ((Environnement) o).getNom();
        String envName2 = this.getNom();
        return envName1.compareTo(envName2);
    }
}
