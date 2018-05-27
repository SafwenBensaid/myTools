/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package springSecurity;

import dto.UserDTO;
import java.util.HashMap;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import org.springframework.security.core.AuthenticationException;
import tools.Configuration;

/**
 *
 * @author 04486
 */
public class SpringSecurityTools {

    public static UserDTO getUserSearch(String matricule, String nom) {
        String usernameDN = "authreader";
        String passwordDN = "re@d123$";
        String base = "cn=Users,DC=biat,DC=int";
        String path = "";
        //String bs = "ou=Utilisateurs BIAT,DC=biat,DC=int";
        String bs = "DC=biat,DC=int";
        String dn = "cn=" + usernameDN + ", " + base;
        String ldapURL = "ldap://172.28.14.1:3268";
        UserDTO user = null;

        // Setup environment for authenticating
        Hashtable<String, String> environment = new Hashtable<String, String>();
        environment.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        environment.put(Context.PROVIDER_URL, ldapURL);
        environment.put(Context.SECURITY_AUTHENTICATION, "simple");
        environment.put(Context.SECURITY_PRINCIPAL, dn);
        environment.put(Context.SECURITY_CREDENTIALS, passwordDN);
        try {
            user = new UserDTO();
            DirContext authContext = new InitialDirContext(environment);
            SearchControls ContrainteRecherche = new SearchControls();
            ContrainteRecherche.setSearchScope(SearchControls.SUBTREE_SCOPE);
            String critere = "";
            if (nom.equals("")) {
                if (Configuration.usersDtoMap != null && Configuration.usersDtoMap.containsKey(matricule)) {
                    return Configuration.usersDtoMap.get(matricule);
                }
                critere = "(sAMAccountname=" + matricule + ")";
            } else {
                critere = "(cn=*" + nom.toUpperCase() + "*)";
            }
            NamingEnumeration answer = authContext.search(bs, critere, ContrainteRecherche);
            while (answer.hasMore()) {
                SearchResult currentElement = (SearchResult) answer.next();
                path = currentElement.getName();
                Attributes attrs = currentElement.getAttributes();
                if (attrs.get("sAMAccountname") != null) {
                    user.setMatricule((String) attrs.get("sAMAccountname").get());
                }
                if (attrs.get("cn") != null) {
                    user.setNom((String) attrs.get("cn").get());
                }
                if (attrs.get("department") != null) {
                    user.setOrganisation((String) attrs.get("department").get());
                }
                if (attrs.get("mail") != null) {
                    user.setEmail((String) attrs.get("mail").get());
                }
                String tel;
                try {
                    tel = (String) attrs.get("telephoneNumber").get();
                } catch (Exception e) {
                    tel = "";
                }
                user.setTelephone(tel);
            }
            authContext.close();
        } catch (AuthenticationException ex) {
            ex.printStackTrace();
        } catch (NamingException ex) {
            ex.printStackTrace();
        }
        if (Configuration.usersDtoMap == null) {
            Configuration.usersDtoMap = new HashMap<String, UserDTO>();
        }
        Configuration.usersDtoMap.put(matricule, user);
        return user;
    }
}
