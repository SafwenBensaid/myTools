/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entitiesTrac;

import java.io.Serializable;
import java.math.BigInteger;
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
@Table(name = "revision")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Revision.findAll", query = "SELECT r FROM Revision r"),
    @NamedQuery(name = "Revision.findByRepos", query = "SELECT r FROM Revision r WHERE r.revisionPK.repos = :repos"),
    @NamedQuery(name = "Revision.findByTime", query = "SELECT r FROM Revision r WHERE r.time = :time")})
public class Revision implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RevisionPK revisionPK;
    @Column(name = "time")
    private BigInteger time;
    @Lob
    @Column(name = "author")
    private String author;
    @Lob
    @Column(name = "message")
    private String message;

    public Revision() {
    }

    public Revision(RevisionPK revisionPK) {
        this.revisionPK = revisionPK;
    }

    public Revision(int repos, String rev) {
        this.revisionPK = new RevisionPK(repos, rev);
    }

    public RevisionPK getRevisionPK() {
        return revisionPK;
    }

    public void setRevisionPK(RevisionPK revisionPK) {
        this.revisionPK = revisionPK;
    }

    public BigInteger getTime() {
        return time;
    }

    public void setTime(BigInteger time) {
        this.time = time;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (revisionPK != null ? revisionPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Revision)) {
            return false;
        }
        Revision other = (Revision) object;
        if ((this.revisionPK == null && other.revisionPK != null) || (this.revisionPK != null && !this.revisionPK.equals(other.revisionPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Revision[ revisionPK=" + revisionPK + " ]";
    }
}
