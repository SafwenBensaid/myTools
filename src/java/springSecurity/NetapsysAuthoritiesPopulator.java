/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package springSecurity;

import java.util.*;
import org.springframework.ldap.core.DirContextOperations;
//import com.sun.jndi.ldap.LdapAttribute;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import tools.Configuration;

/**
 *
 * @author 04486
 */
public class NetapsysAuthoritiesPopulator implements LdapAuthoritiesPopulator {

    @Override
    public Collection<? extends GrantedAuthority> getGrantedAuthorities(DirContextOperations dco, String userMatricule) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        Map<String, Set<GrantedAuthority>> allFonctionalitesMapOrdredByMatricule = Configuration.allFonctionalitesMapOrdredByMatricule;
        if (Configuration.allFonctionalitesMapOrdredByMatricule != null && Configuration.allFonctionalitesMapOrdredByMatricule.containsKey(userMatricule)) {
            authorities.addAll(Configuration.allFonctionalitesMapOrdredByMatricule.get(userMatricule));
        }
        return authorities;
    }
}
