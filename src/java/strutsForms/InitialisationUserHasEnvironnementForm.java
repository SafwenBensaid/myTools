/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package strutsForms;

import entitiesMysql.Environnement;
import java.util.*;

/**
 *
 * @author 04486
 */
public class InitialisationUserHasEnvironnementForm extends org.apache.struts.action.ActionForm {

    private HashMap<String, ArrayList<Environnement>> environnementsListByUser;
    private String[] arrayUsersLogin;
    private String[] arrayEnvironnements;

    public InitialisationUserHasEnvironnementForm() {
        super();
        // TODO Auto-generated constructor stub
    }

    public HashMap<String, ArrayList<Environnement>> getEnvironnementsListByUser() {
        return environnementsListByUser;
    }

    public void setEnvironnementsListByUser(HashMap<String, ArrayList<Environnement>> environnementsListByUser) {
        this.environnementsListByUser = environnementsListByUser;
    }

    public String[] getArrayUsersLogin() {
        return arrayUsersLogin;
    }

    public void setArrayUsersLogin(String[] arrayUsersLogin) {
        this.arrayUsersLogin = arrayUsersLogin;
    }

    public String[] getArrayEnvironnements() {
        return arrayEnvironnements;
    }

    public void setArrayEnvironnements(String[] arrayEnvironnements) {
        this.arrayEnvironnements = arrayEnvironnements;
    }
    /**
     * This is the action called from the Struts framework.
     *
     * @param mapping The ActionMapping used to select this instance.
     * @param request The HTTP Request we are processing.
     * @return
     */
}
