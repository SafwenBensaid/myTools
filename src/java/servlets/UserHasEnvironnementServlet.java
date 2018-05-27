package servlets;

import dto.EnvironnementDTO;
import entitiesMysql.Environnement;
import entitiesMysql.Users;
import entitiesMysql.UsersHasEnvironnement;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tools.Configuration;
import dataBaseTracRequests.DataBaseTracRequests;
import tools.DataBaseTools;
import tools.Tools;

public class UserHasEnvironnementServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            String connectedUser = Tools.getConnectedLogin();
            String typeReq = request.getParameter("typeModf");
            String userLogin = null;
            String selectedUser = null;
            DataBaseTools dbTools = new DataBaseTools(Configuration.puOvTools);
            DataBaseTracRequests dbRequests = new DataBaseTracRequests();
            if (typeReq.equals("supp")) {
                Tools.showConsolLog("---------------------------------servlet inside!--------Suppression");
                userLogin = request.getParameter("id").trim();
                String environnement = request.getParameter("envName").trim();

                UsersHasEnvironnement usr = dbRequests.findUserHasEnvByIdAndEnv(dbTools, userLogin, environnement);
                dbTools.remove(usr);
                try {
                    out.println(majStructure(dbTools, connectedUser, userLogin)); //resultat 
                } finally {
                    try {
                        out.close();
                    } catch (Exception exp) {
                    }
                }
            } else if (typeReq.equals("add")) {
                Tools.showConsolLog("---------------------------servlet inside!--------Ajout");
                userLogin = request.getParameter("user");
                String envName = request.getParameter("envName");
                String browserLogin = request.getParameter("browserLogin");
                String browserPassword = request.getParameter("browserPass");

                browserPassword = browserPassword.replace("ET_COMMERCIAL", "&");
                browserPassword = browserPassword.replace("DIESE", "#");
                browserPassword = browserPassword.replace("PLUS", "+");
                browserPassword = browserPassword.replace("DOLLAR", "$");

                Users usr = Configuration.usersMap.get(userLogin);
                Environnement env = Configuration.allEnvironnementMap.get(envName);
                UsersHasEnvironnement usrHasEnv = new UsersHasEnvironnement(userLogin, envName);
                usrHasEnv.setUsers(usr);
                usrHasEnv.setEnvironnement(env);
                usrHasEnv.setBrowserLogin(browserLogin);
                usrHasEnv.setBrowsrPassword(browserPassword);
                String errorMessage = "OK";
                try {
                    dbTools.StoreObjectIntoDataBase(usrHasEnv);
                } catch (Exception ex) {
                    errorMessage = "PROBLEME_INSERTION";
                }
                try {
                    out.println(majStructure(dbTools, connectedUser, userLogin)); //resultat 
                } finally {
                    try {
                        out.close();
                    } catch (Exception exp) {
                    }
                }
            } else if (typeReq.equals("initAdd")) {
                try {
                    boolean isAdmin = Configuration.usersGroupMap.get(connectedUser).contains("SUPER_ADMIN");
                    if (isAdmin) {
                        out.println(generateAdminToAddTr());
                    } else {
                        out.println(generateToAddTr(connectedUser));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else if (typeReq.equals("affListEnvByUser")) {
                selectedUser = request.getParameter("selectedUser");
                out.println(generateSelectDropDownByUser(selectedUser));
            } else if (typeReq.equals("edit")) {
                Tools.showConsolLog("---------------------------servlet inside!--------edition");
                userLogin = request.getParameter("user").trim();
                String envName = request.getParameter("envName").trim();
                String browserLogin = request.getParameter("browserLogin").trim();
                String browserPassword = request.getParameter("browserPass").trim();

                browserPassword = browserPassword.replace("ET_COMMERCIAL", "&");
                browserPassword = browserPassword.replace("DIESE", "#");
                browserPassword = browserPassword.replace("PLUS", "+");
                browserPassword = browserPassword.replace("DOLLAR", "$");

                dbRequests.updateUserHasEnvironnement(dbTools, userLogin, "browser_login", "browsr_password", browserLogin, browserPassword, "users_has_environnement", envName);

                Tools.showConsolLog("---------------------------fin--------edition");
            }
            /*
             if (!typeReq.equals("initAdd")) {
             //mise à jour de la structure
             Configuration.chargerTousLesEnvironnements(dbTools);
             dbTools.closeRessources();
             try {
             String connectedUser = (String) request.getSession().getAttribute("connectedUser");
             out.println(convertHashMapToJsonObj(connectedUser, userLogin)); //resultat 
             } finally {
             out.close();
             }
             }
             */
            dbTools.closeRessources();
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }

    private String majStructure(DataBaseTools dbTools, String connectedUser, String userLogin) {
        Configuration.chargerTousLesEnvironnements(dbTools);
        return convertHashMapToJsonObj(connectedUser, userLogin);
    }

    public String generateTr(String login) {
        int i = 0;
        Map<String, EnvironnementDTO> auxDTOMap = null;
        StringBuilder resultat = new StringBuilder();
        try {
            Map<String, Map<String, EnvironnementDTO>> aaa = Configuration.environnementDTOMapUserHasEnvironnement;
            auxDTOMap = Configuration.environnementDTOMapUserHasEnvironnement.get(login);
            if (auxDTOMap != null) {
                for (Map.Entry<String, EnvironnementDTO> en : auxDTOMap.entrySet()) {
                    {
                        resultat.append("<tr id='" + login + "' class='containId aff " + login.replaceAll("\\.", "_") + "'>");
                        resultat.append("<td class='imageContenu'></td>");
                        if (en.getKey().equals("VERSIONNING")) {
                            resultat.append("<td class='imageContenu'><img src='images/remove-disabled.png' class='imageRemove'/></td>");
                        } else {
                            resultat.append("<td class='imageContenu'><img src='images/remove.png' class='imageRemove' onclick='removeUserHasEnv(this);'/></td>");
                        }
                        //resultat.append("<td class='imageContenu'><img src='images/remove.png' class='imageRemove' onclick='removeUserHasEnv(this);'/></td>");
                        if (i == 0) {
                            resultat.append("<td class='tdContenu' id='user' style='vertical-align: middle' rowspan='" + auxDTOMap.size() + "'><span class='valueOf'> " + login + "</span></td>");
                        }
                        resultat.append("<td class='tdContenu' id='env'><span class='valueOf'>" + en.getKey() + " </span></td>");
                        resultat.append("<td class='tdContenu' id='login'><span class='valueOf userT24Login'>" + en.getValue().getBrowserUser() + " </span></td>");
                        resultat.append("<td class='tdContenu' id='pass'><span class='valueOf userT24Password'>" + en.getValue().getBrowserPassword() + " </span></td>");
                        resultat.append("</tr>");
                        i++;
                    }
                }
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return resultat.toString();
    }

    public String generateToAddTr(String login) {
        List<String> envToDisplay = new ArrayList<String>();
        Set<String> displayedSet = null;
        Set<String> toAddEnvSet = null;
        StringBuilder trAjout = new StringBuilder();
        try {
            Map<String, EnvironnementDTO> map = Configuration.environnementDTOMapUserHasEnvironnement.get(login);
            toAddEnvSet = Configuration.environnementMapDutyGroup.get(login).keySet();
            if (map != null) {
                displayedSet = map.keySet();
            } else {
                envToDisplay.addAll(toAddEnvSet);
            }
            for (String envName : toAddEnvSet) {
                if (displayedSet != null && !displayedSet.contains(envName)) {
                    envToDisplay.add(envName);
                }
            }
            if (envToDisplay.isEmpty()) {
                return "";
            } else {
                trAjout.append("<tr class='add'>");
                trAjout.append("<td class='imageContenu'></td>");
                trAjout.append("<td class='imageContenu'><img src='images/add.png' class='imageAdd' onclick='addUserHasEnv(this);'></td>");
                trAjout.append("<td class=\"ToAdd tdContenu user\" id=\"user\">");
                trAjout.append("<input class=\"inputContenu\" id=\"user\" name=\"user\" value='" + login + "' type=\"text\">");
                trAjout.append("</td>");
                trAjout.append("<td class=\"ToAdd tdContenu env\" id=\"env\">");
                trAjout.append("<select id='env' name='nomEnvironnement'>");
                trAjout.append("<option></option>");
                for (int i = 0; i < envToDisplay.size(); i++) {
                    trAjout.append("<option>" + envToDisplay.get(i) + "</option>");
                }
                trAjout.append("</select>");
                trAjout.append("</td>");
                trAjout.append("<td class=\"ToAdd tdContenu login\"><input class=\"inputContenu\" id=\"login\" name=\"login\" type=\"text\"></td>");
                trAjout.append("<td class=\"ToAdd tdContenu pass\"><input class=\"inputContenu\" id=\"pass\" name=\"pass\" type=\"text\"></td>");
                trAjout.append("</tr>");
                /*
                 trAjout.append("<tr class='separationTr'>");
                 trAjout.append("<td style=\"padding: 0px;\"></td>");
                 trAjout.append("<td style=\"padding: 0px;\"></td>");
                 trAjout.append(" <td class=\"separationTd\" colspan=\"4\" style=\"background-color:#FFDA8C;padding-top: 1px\"></td>");
                 trAjout.append("</tr>");
                 */
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return trAjout.toString();
    }

    public String generateAdminToAddTr() {
        StringBuilder trAjoutAdmin = new StringBuilder();
        try {
            Map<String, EnvironnementDTO> userHasEnvAux = null;
            Set<String> usersLoginQuiOntEncoreDroitADesUsersHasEnv = new TreeSet<String>();
            for (Map.Entry<String, Map<String, Environnement>> entry : Configuration.environnementMapDutyGroup.entrySet()) {
                //userHasEnvAux = la map de userHasEnv du user en cours
                userHasEnvAux = Configuration.environnementDTOMapUserHasEnvironnement.get(entry.getKey());
                if (entry.getValue().size() > userHasEnvAux.size()) {
                    usersLoginQuiOntEncoreDroitADesUsersHasEnv.add(entry.getKey());
                }
            }
            //usersLoginQuiOntEncoreDroitADesUsersHasEnv contient la liste des logins qui ont encore le droit d'ajout
            if (usersLoginQuiOntEncoreDroitADesUsersHasEnv.isEmpty()) {
                return "";
            } else {//si l'utilisateur a encore un granted environnement qu'il n'a pas encore ajouté.
                trAjoutAdmin.append("<tr class=\"add\">");
                trAjoutAdmin.append("<td class=\"imageContenu\"><img src=\"images/add.png\" class=\"imageAdd\" onclick=\"addUserHasEnv(this);\"></td>");
                trAjoutAdmin.append("<td class=\"imageContenu\"><img src=\"images/remove.png\" class=\"imageRemove\" onclick=\"annulerUserHasEnv(this);\"></td>");
                trAjoutAdmin.append("<td class=\"ToAdd tdContenu user\" id=\"user\">");
                trAjoutAdmin.append("<select id='user' name='user'>");
                for (String loginName : usersLoginQuiOntEncoreDroitADesUsersHasEnv) {
                    trAjoutAdmin.append("<option>" + loginName + "</option>");
                }
                trAjoutAdmin.append("</select>");
                trAjoutAdmin.append("</td>");
                trAjoutAdmin.append("<td class=\"ToAdd tdContenu env\" id=\"env\">");
                trAjoutAdmin.append("</td>");
                trAjoutAdmin.append("<td class=\"ToAdd tdContenu login\"><input class=\"inputContenu\" id=\"login\" name=\"login\" type=\"text\"></td>");
                trAjoutAdmin.append("<td class=\"ToAdd tdContenu pass\"><input class=\"inputContenu\" id=\"pass\" name=\"pass\" type=\"text\"></td>");
                trAjoutAdmin.append("</tr>");
                /*
                 trAjoutAdmin.append("<tr class=\"separationTr\">");
                 trAjoutAdmin.append("<td style=\"padding: 0px;\"></td>");
                 trAjoutAdmin.append("<td style=\"padding: 0px;\"></td>");
                 trAjoutAdmin.append(" <td class=\"separationTd\" colspan=\"4\" style=\"background-color:#FFDA8C;padding-top: 1px\"></td>");
                 trAjoutAdmin.append("</tr>");
                 */
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return trAjoutAdmin.toString();
    }

    public String generateSelectDropDownByUser(String login) {
        List<String> envToDisplay = new ArrayList<String>();
        Set<String> displayedSet = null;
        Set<String> toAddEnvSet = null;
        StringBuilder envByUser = new StringBuilder();
        try {
            Map<String, EnvironnementDTO> map = Configuration.environnementDTOMapUserHasEnvironnement.get(login);
            toAddEnvSet = Configuration.environnementMapDutyGroup.get(login).keySet();
            if (map != null) {
                displayedSet = map.keySet();
            } else {
                envToDisplay.addAll(toAddEnvSet);
            }
            for (String envName : toAddEnvSet) {
                if (displayedSet != null && !displayedSet.contains(envName)) {
                    envToDisplay.add(envName);
                }
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        if (envToDisplay.isEmpty()) {
            return "";
        } else {
            envByUser.append("<select id='env' name='nomEnvironnement'>");
            envByUser.append("<option></option>");
            for (String ddlEnv : envToDisplay) {
                envByUser.append("<option>" + ddlEnv + "</option>");
            }
            envByUser.append("</select>");
            return envByUser.toString();
        }
    }

    private String convertHashMapToJsonObj(String connectedUser, String userLogin) {
        boolean isAdmin = Configuration.usersGroupMap.get(connectedUser).contains("SUPER_ADMIN");
        Map<String, String> mapTrContent = new HashMap<String, String>();
        mapTrContent.put("TR_Affichee", generateTr(userLogin));
        if (isAdmin) {
            mapTrContent.put("TR_Ajout", generateAdminToAddTr());
        } else {
            mapTrContent.put("TR_Ajout", generateToAddTr(connectedUser));
        }
        return Tools.objectToJsonString(mapTrContent);
    }
    /* public static void main(String[] args) {
     DataBaseTools dbTools = new DataBaseTools(Configuration.ovToolsPU);
     Configuration.chargerTousLesEnvironnements(dbTools);
     // String s = new UserHasEnvironnementServlet().generateToAddTr("test");
     String s2 = new UserHasEnvironnementServlet().generateSelectDropDownByUser("anis.moalla");
     dbTools.closeRessources();
     Tools.showConsolLog(s2);
     }*/

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
