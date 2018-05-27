/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entitiesTrac;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author 04486
 */
@Entity
@Table(name = "repository")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Repository.findAll", query = "SELECT r FROM Repository r"),
    @NamedQuery(name = "Repository.findById", query = "SELECT r FROM Repository r WHERE r.repositoryPK.id = :id")})
public class Repository implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RepositoryPK repositoryPK;
    @Lob
    @Column(name = "value")
    private String value;

    public Repository() {
    }

    public Repository(RepositoryPK repositoryPK) {
        this.repositoryPK = repositoryPK;
    }

    public Repository(int id, String name) {
        this.repositoryPK = new RepositoryPK(id, name);
    }

    public RepositoryPK getRepositoryPK() {
        return repositoryPK;
    }

    public void setRepositoryPK(RepositoryPK repositoryPK) {
        this.repositoryPK = repositoryPK;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (repositoryPK != null ? repositoryPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Repository)) {
            return false;
        }
        Repository other = (Repository) object;
        if ((this.repositoryPK == null && other.repositoryPK != null) || (this.repositoryPK != null && !this.repositoryPK.equals(other.repositoryPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Repository[ repositoryPK=" + repositoryPK + " ]";
    }
}
