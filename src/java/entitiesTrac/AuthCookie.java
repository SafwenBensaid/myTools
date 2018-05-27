/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entitiesTrac;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author 04486
 */
@Entity
@Table(name = "auth_cookie")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AuthCookie.findAll", query = "SELECT a FROM AuthCookie a"),
    @NamedQuery(name = "AuthCookie.findByTime", query = "SELECT a FROM AuthCookie a WHERE a.time = :time")})
public class AuthCookie implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AuthCookiePK authCookiePK;
    @Column(name = "time")
    private Integer time;

    public AuthCookie() {
    }

    public AuthCookie(AuthCookiePK authCookiePK) {
        this.authCookiePK = authCookiePK;
    }

    public AuthCookie(String cookie, String name, String ipnr) {
        this.authCookiePK = new AuthCookiePK(cookie, name, ipnr);
    }

    public AuthCookiePK getAuthCookiePK() {
        return authCookiePK;
    }

    public void setAuthCookiePK(AuthCookiePK authCookiePK) {
        this.authCookiePK = authCookiePK;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (authCookiePK != null ? authCookiePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AuthCookie)) {
            return false;
        }
        AuthCookie other = (AuthCookie) object;
        if ((this.authCookiePK == null && other.authCookiePK != null) || (this.authCookiePK != null && !this.authCookiePK.equals(other.authCookiePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.AuthCookie[ authCookiePK=" + authCookiePK + " ]";
    }
}
