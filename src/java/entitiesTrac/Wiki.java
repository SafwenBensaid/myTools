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
@Table(name = "wiki")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Wiki.findAll", query = "SELECT w FROM Wiki w"),
    @NamedQuery(name = "Wiki.findByVersion", query = "SELECT w FROM Wiki w WHERE w.wikiPK.version = :version"),
    @NamedQuery(name = "Wiki.findByTime", query = "SELECT w FROM Wiki w WHERE w.time = :time"),
    @NamedQuery(name = "Wiki.findByReadonly", query = "SELECT w FROM Wiki w WHERE w.readonly = :readonly")})
public class Wiki implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected WikiPK wikiPK;
    @Column(name = "time")
    private BigInteger time;
    @Lob
    @Column(name = "author")
    private String author;
    @Lob
    @Column(name = "ipnr")
    private String ipnr;
    @Lob
    @Column(name = "text")
    private String text;
    @Lob
    @Column(name = "comment")
    private String comment;
    @Column(name = "readonly")
    private Integer readonly;

    public Wiki() {
    }

    public Wiki(WikiPK wikiPK) {
        this.wikiPK = wikiPK;
    }

    public Wiki(String name, int version) {
        this.wikiPK = new WikiPK(name, version);
    }

    public WikiPK getWikiPK() {
        return wikiPK;
    }

    public void setWikiPK(WikiPK wikiPK) {
        this.wikiPK = wikiPK;
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

    public String getIpnr() {
        return ipnr;
    }

    public void setIpnr(String ipnr) {
        this.ipnr = ipnr;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getReadonly() {
        return readonly;
    }

    public void setReadonly(Integer readonly) {
        this.readonly = readonly;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (wikiPK != null ? wikiPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Wiki)) {
            return false;
        }
        Wiki other = (Wiki) object;
        if ((this.wikiPK == null && other.wikiPK != null) || (this.wikiPK != null && !this.wikiPK.equals(other.wikiPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Wiki[ wikiPK=" + wikiPK + " ]";
    }
}
