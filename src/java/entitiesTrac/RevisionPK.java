/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entitiesTrac;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;

/**
 *
 * @author 04486
 */
@Embeddable
public class RevisionPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "repos")
    private int repos;
    @Basic(optional = false)
    @Lob
    @Column(name = "rev")
    private String rev;

    public RevisionPK() {
    }

    public RevisionPK(int repos, String rev) {
        this.repos = repos;
        this.rev = rev;
    }

    public int getRepos() {
        return repos;
    }

    public void setRepos(int repos) {
        this.repos = repos;
    }

    public String getRev() {
        return rev;
    }

    public void setRev(String rev) {
        this.rev = rev;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) repos;
        hash += (rev != null ? rev.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RevisionPK)) {
            return false;
        }
        RevisionPK other = (RevisionPK) object;
        if (this.repos != other.repos) {
            return false;
        }
        if ((this.rev == null && other.rev != null) || (this.rev != null && !this.rev.equals(other.rev))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.RevisionPK[ repos=" + repos + ", rev=" + rev + " ]";
    }
}
