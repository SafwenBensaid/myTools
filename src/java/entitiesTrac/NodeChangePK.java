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
public class NodeChangePK implements Serializable {

    @Basic(optional = false)
    @Column(name = "repos")
    private int repos;
    @Basic(optional = false)
    @Lob
    @Column(name = "rev")
    private String rev;
    @Basic(optional = false)
    @Lob
    @Column(name = "path")
    private String path;
    @Basic(optional = false)
    @Lob
    @Column(name = "change_type")
    private String changeType;

    public NodeChangePK() {
    }

    public NodeChangePK(int repos, String rev, String path, String changeType) {
        this.repos = repos;
        this.rev = rev;
        this.path = path;
        this.changeType = changeType;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) repos;
        hash += (rev != null ? rev.hashCode() : 0);
        hash += (path != null ? path.hashCode() : 0);
        hash += (changeType != null ? changeType.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NodeChangePK)) {
            return false;
        }
        NodeChangePK other = (NodeChangePK) object;
        if (this.repos != other.repos) {
            return false;
        }
        if ((this.rev == null && other.rev != null) || (this.rev != null && !this.rev.equals(other.rev))) {
            return false;
        }
        if ((this.path == null && other.path != null) || (this.path != null && !this.path.equals(other.path))) {
            return false;
        }
        if ((this.changeType == null && other.changeType != null) || (this.changeType != null && !this.changeType.equals(other.changeType))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.NodeChangePK[ repos=" + repos + ", rev=" + rev + ", path=" + path + ", changeType=" + changeType + " ]";
    }
}
