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
@Table(name = "node_change")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NodeChange.findAll", query = "SELECT n FROM NodeChange n"),
    @NamedQuery(name = "NodeChange.findByRepos", query = "SELECT n FROM NodeChange n WHERE n.nodeChangePK.repos = :repos")})
public class NodeChange implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected NodeChangePK nodeChangePK;
    @Lob
    @Column(name = "node_type")
    private String nodeType;
    @Lob
    @Column(name = "base_path")
    private String basePath;
    @Lob
    @Column(name = "base_rev")
    private String baseRev;

    public NodeChange() {
    }

    public NodeChange(NodeChangePK nodeChangePK) {
        this.nodeChangePK = nodeChangePK;
    }

    public NodeChange(int repos, String rev, String path, String changeType) {
        this.nodeChangePK = new NodeChangePK(repos, rev, path, changeType);
    }

    public NodeChangePK getNodeChangePK() {
        return nodeChangePK;
    }

    public void setNodeChangePK(NodeChangePK nodeChangePK) {
        this.nodeChangePK = nodeChangePK;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public String getBaseRev() {
        return baseRev;
    }

    public void setBaseRev(String baseRev) {
        this.baseRev = baseRev;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nodeChangePK != null ? nodeChangePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NodeChange)) {
            return false;
        }
        NodeChange other = (NodeChange) object;
        if ((this.nodeChangePK == null && other.nodeChangePK != null) || (this.nodeChangePK != null && !this.nodeChangePK.equals(other.nodeChangePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.NodeChange[ nodeChangePK=" + nodeChangePK + " ]";
    }
}
