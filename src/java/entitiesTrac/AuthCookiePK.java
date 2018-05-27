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
public class AuthCookiePK implements Serializable {

    @Basic(optional = false)
    @Lob
    @Column(name = "cookie")
    private String cookie;
    @Basic(optional = false)
    @Lob
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @Lob
    @Column(name = "ipnr")
    private String ipnr;

    public AuthCookiePK() {
    }

    public AuthCookiePK(String cookie, String name, String ipnr) {
        this.cookie = cookie;
        this.name = name;
        this.ipnr = ipnr;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        hash += (cookie != null ? cookie.hashCode() : 0);
        hash += (name != null ? name.hashCode() : 0);
        hash += (ipnr != null ? ipnr.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AuthCookiePK)) {
            return false;
        }
        AuthCookiePK other = (AuthCookiePK) object;
        if ((this.cookie == null && other.cookie != null) || (this.cookie != null && !this.cookie.equals(other.cookie))) {
            return false;
        }
        if ((this.name == null && other.name != null) || (this.name != null && !this.name.equals(other.name))) {
            return false;
        }
        if ((this.ipnr == null && other.ipnr != null) || (this.ipnr != null && !this.ipnr.equals(other.ipnr))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.AuthCookiePK[ cookie=" + cookie + ", name=" + name + ", ipnr=" + ipnr + " ]";
    }
}
