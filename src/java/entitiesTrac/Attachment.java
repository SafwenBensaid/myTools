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
@Table(name = "attachment")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Attachment.findAll", query = "SELECT a FROM Attachment a"),
    @NamedQuery(name = "Attachment.findBySize", query = "SELECT a FROM Attachment a WHERE a.size = :size"),
    @NamedQuery(name = "Attachment.findByTime", query = "SELECT a FROM Attachment a WHERE a.time = :time"),
    @NamedQuery(name = "Attachment.findAllTicketId", query = "SELECT DISTINCT(a.attachmentPK.id) FROM Attachment a WHERE a.attachmentPK.type = 'ticket'")})
public class Attachment implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AttachmentPK attachmentPK;
    @Column(name = "size")
    private Integer size;
    @Column(name = "time")
    private Long time;
    @Lob
    @Column(name = "description")
    private String description;
    @Lob
    @Column(name = "author")
    private String author;
    @Lob
    @Column(name = "ipnr")
    private String ipnr;

    public Attachment() {
    }

    public Attachment(AttachmentPK attachmentPK) {
        this.attachmentPK = attachmentPK;
    }

    public Attachment(String type, String id, String filename) {
        this.attachmentPK = new AttachmentPK(type, id, filename);
    }

    public AttachmentPK getAttachmentPK() {
        return attachmentPK;
    }

    public void setAttachmentPK(AttachmentPK attachmentPK) {
        this.attachmentPK = attachmentPK;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIpnr() {
        return ipnr;
    }

    public void setIpnr(String ipnr) {
        this.ipnr = ipnr;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (attachmentPK != null ? attachmentPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Attachment)) {
            return false;
        }
        Attachment other = (Attachment) object;
        if ((this.attachmentPK == null && other.attachmentPK != null) || (this.attachmentPK != null && !this.attachmentPK.equals(other.attachmentPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Attachment[ attachmentPK=" + attachmentPK + " ]";
    }
}
