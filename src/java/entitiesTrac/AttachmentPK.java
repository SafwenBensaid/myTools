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
public class AttachmentPK implements Serializable {

    @Basic(optional = false)
    @Lob
    @Column(name = "type")
    private String type;
    @Basic(optional = false)
    @Lob
    @Column(name = "id")
    private String id;
    @Basic(optional = false)
    @Lob
    @Column(name = "filename")
    private String filename;

    public AttachmentPK() {
    }

    public AttachmentPK(String type, String id, String filename) {
        this.type = type;
        this.id = id;
        this.filename = filename;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (type != null ? type.hashCode() : 0);
        hash += (id != null ? id.hashCode() : 0);
        hash += (filename != null ? filename.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AttachmentPK)) {
            return false;
        }
        AttachmentPK other = (AttachmentPK) object;
        if ((this.type == null && other.type != null) || (this.type != null && !this.type.equals(other.type))) {
            return false;
        }
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        if ((this.filename == null && other.filename != null) || (this.filename != null && !this.filename.equals(other.filename))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.AttachmentPK[ type=" + type + ", id=" + id + ", filename=" + filename + " ]";
    }
}
