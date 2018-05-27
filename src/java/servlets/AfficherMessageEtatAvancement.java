/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import entitiesMysql.Environnement;
import entitiesMysql.Users;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tools.Configuration;
import static tools.Configuration.allEnvironnementMap;
import tools.StringBuilderTools;
import tools.Tools;

/**
 *
 * @author 04486
 */
public class AfficherMessageEtatAvancement extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    public static Map<String, Boolean> executionEnCours = new LinkedHashMap<String, Boolean>();
    //<userLogin,<EnvName,boolean>>
    public static Map<String, Map<String, Boolean>> deploiementEnCours = new LinkedHashMap<String, Map<String, Boolean>>();
    //Map<userlaogin,Map<EnvName,Map<RestoreName, Index>>>
    public static Map<String, StringBuilder> logMessageMap = null;
    public static boolean logMessageMapInitialised = false;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        //String connectedUser = request.getParameter("connectedUser").trim();
        //Tools.showConsolLog(connectedUser);

        //// test RESTORE X ////
        try {
            initialisationLogMessagesForAllUsers();
            //Tools.showConsolLog("SERVLET LOG INVOQUEE");
            StringBuilder logMessage = null;
            String connectedUser = Tools.getConnectedLogin();
            if (connectedUser.equals("anonymousUser")) {
                logMessage = new StringBuilder();
            } else {
                logMessage = logMessageMap.get(connectedUser);
                if (logMessage != null) {
                    try {
                        if (executionEnCours.get(connectedUser) == false) {
                            out.println("");
                        } else if (logMessage.length() > 0) {
                            //out.println(logMessage + getRestoreDeploiement() + " ...");
                            if (logMessage.toString().trim().equals("...")) {
                                out.println("");
                            } else {
                                //Tools.showConsolLog("______XXX:logMessage______\n" + logMessage + "\n______XXX______");
                                //out.println(logMessage.toString().replace("...", " ") + getRestoreDeploiement(connectedUser) + "...");
                                //out.println(logMessage);
                                if (!logMessage.toString().trim().startsWith("Environnement ")) {
                                    //fonctionnement simple
                                    out.println(logMessage.toString().replace("...", getRestoreDeploiement(connectedUser) + "..."));
                                } else {
                                    //fonctionnement parallele
                                    out.println(analyseMessageParallele(connectedUser, logMessage.toString()));
                                }
                            }
                        }
                    } catch (Exception exep) {
                    } finally {
                        try {
                            out.close();
                        } catch (Exception exp) {
                        }
                    }
                }
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }

    private static String analyseMessageParallele(String connectedUser, String messageTotal) {
        StringBuilder resultat = new StringBuilder();
        try {
            messageTotal = messageTotal.replaceAll("<br>", "<br>%%%");
            String[] tabLog = messageTotal.split("%%%");
            int indexDeuxPoints = -1;
            String envName = null;
            for (String logMessage : tabLog) {
                if (logMessage.trim().length() > 0) {
                    indexDeuxPoints = logMessage.toString().indexOf(":");
                    if (indexDeuxPoints > 0 && logMessage.startsWith("Environnement")) {
                        String environnementDetails = logMessage.subSequence(0, indexDeuxPoints).toString().trim();
                        envName = environnementDetails.replace("Environnement", "").trim();
                        if (deploiementEnCours.get(connectedUser).get(envName) == true) {
                            resultat.append(logMessage.replace("...", traiterRestoreDeploiement(connectedUser, envName) + "..."));
                        } else {
                            resultat.append(logMessage);
                        }
                    }
                }
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return resultat.toString();
    }

    private static String getRestoreDeploiement(String connectedUser) {
        String restore = "";
        String resultat = "";
        try {
            for (Map.Entry<String, Boolean> envEntry : deploiementEnCours.get(connectedUser).entrySet()) {
                if (envEntry.getValue().equals(true)) {
                    resultat = traiterRestoreDeploiement(connectedUser, envEntry.getKey());
                }
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return resultat;
    }

    private static String traiterRestoreDeploiement(String connectedUser, String envName) {
        String restore = "";
        String resultat = "";
        int indexMaxValue = -1;
        StringBuilder problem = new StringBuilder();
        try {
            problem.append("**********************************\nconnectedUser: " + connectedUser);
            problem.append("\n**********************************\nConfiguration.restoreMap: " + Configuration.restoreMap);
            problem.append("\n**********************************\nConfiguration.restoreMap.get(connectedUser): " + Configuration.restoreMap.get(connectedUser));
            problem.append("\n**********************************\nConfiguration.restoreMap.get(connectedUser).get(envName): " + Configuration.restoreMap.get(connectedUser).get(envName));
            problem.append("\n**********************************\nConfiguration.restoreMap.get(connectedUser).get(envName).entrySet(): " + Configuration.restoreMap.get(connectedUser).get(envName).entrySet());
            for (Map.Entry<String, Integer> entry : Configuration.restoreMap.get(connectedUser).get(envName).entrySet()) {
                if (entry.getValue() > indexMaxValue && entry.getValue() != -1) {
                    restore = entry.getKey();
                    indexMaxValue = entry.getValue();
                }
            }
            if (restore.contains("RESTORE")) {
                resultat = " (" + restore + ")";
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(problem + "\n\n" + tools.Tools.getStackTrace(exep));
        }
        /*
         Tools.showConsolLog("_________________________________________________________");
         Tools.showConsolLog("_________________________________________________________");
         Tools.showConsolLog("_________________________________________________________");
         Tools.showConsolLog(connectedUser + ":" + envName);
         Tools.showConsolLog("Restore1: AfficherMessageEtatAvancement" + Configuration.restoreMap.get(connectedUser).get(envName).get("RESTORE1"));
         Tools.showConsolLog("Restore2: AfficherMessageEtatAvancement" + Configuration.restoreMap.get(connectedUser).get(envName).get("RESTORE2"));
         Tools.showConsolLog("Restore3: AfficherMessageEtatAvancement" + Configuration.restoreMap.get(connectedUser).get(envName).get("RESTORE3"));
         Tools.showConsolLog(resultat);
         Tools.showConsolLog("_________________________________________________________");
         Tools.showConsolLog("_________________________________________________________");
         Tools.showConsolLog("_________________________________________________________");
         */
        return resultat;
    }

    public static void initialisationLogMessagesForAllUsers() {
        try {
            if (!logMessageMapInitialised) {
                logMessageMap = new LinkedHashMap<String, StringBuilder>();

                Configuration.getAllUsers();

                HashMap<String, Boolean> envDeploys = new HashMap<String, Boolean>();
                for (Map.Entry<String, Environnement> entry : allEnvironnementMap.entrySet()) {
                    envDeploys.put(entry.getKey(), false);
                }

                for (Map.Entry<String, Users> usrEntry : Configuration.usersMap.entrySet()) {
                    logMessageMap.put(usrEntry.getValue().getLogin(), new StringBuilder());
                    deploiementEnCours.put(usrEntry.getValue().getLogin(), (Map<String, Boolean>) envDeploys.clone());
                    executionEnCours.put(usrEntry.getValue().getLogin(), false);
                }
                logMessageMapInitialised = true;
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }

    public static void setLogmessage(String ch, String userLogin) {
        StringBuilder logMessage = null;
        try {
            if (userLogin != null && !userLogin.equals("anonymousUser")) {
                //si la map n'est pas encore initialisée, le faire
                initialisationLogMessagesForAllUsers();
                //Tools.showConsolLog("______111______\n" + ch + "\n______222______");
                //Tools.showConsolLog("______333______\n" + logMessage.toString() + "\n______444______");
                logMessage = logMessageMap.get(userLogin);
                if (logMessage != null) {
                    if (ch.trim().length() > 0) {
                        logMessage = logMessage.replace(0, logMessage.length(), ch + " ...");
                    } else {
                        logMessage = logMessage.replace(0, logMessage.length(), "");
                    }
                    logMessageMap.put(userLogin, logMessage);
                }
                //Tools.showConsolLog("______555______\n" + logMessage.toString() + "\n______666______");
            }
        } catch (Exception exep) {
            String log = "logMessage:|" + logMessage.toString() + "|\n";
            log += "ch:|" + ch + "|\n\n";
            log += tools.Tools.getStackTrace(exep);
            exep.printStackTrace();
            tools.Tools.traiterException(log);
        }
    }

    public static synchronized void appendLogmessage(String ch, String connectedUser) {
        StringBuilder logMessage = logMessageMap.get(connectedUser);
        //test
        if (logMessage == null) {
            String log = "variable logMessage == null\n\n";
            log += "le user qui a causé le problème: " + connectedUser + "\n\n";
            log += "--------\n" + ch + "\n--------\n\n";
            for (String userName : logMessageMap.keySet()) {
                log += userName + "\n";
            }
            tools.Tools.traiterException(log);
        }
        //
        try {
            ch = ch.trim();
            int indexDeuxPoints = ch.toString().indexOf(":");
            String envName = null;
            if (indexDeuxPoints > 0) {
                String environnementDetails = null;
                try {
                    environnementDetails = ch.subSequence(0, indexDeuxPoints).toString().trim();
                } catch (Exception ex) {
                    String errorLog = "logMessage:#" + logMessage + "#<br><br>\n\n";
                    errorLog += "ch:#" + ch + "#<br><br>\n\n";
                    errorLog += "indexDeuxPoints:#" + indexDeuxPoints + "#<br><br>\n\n";
                    errorLog += "le user qui a causé le problème: " + connectedUser + "<br><br>\n\n";
                    errorLog += tools.Tools.getStackTrace(ex);
                    tools.Tools.traiterException(errorLog);
                    return;
                }
                if (logMessage.toString().contains(environnementDetails)) {
                    String[] tabLog = logMessage.toString().split("<br>");
                    for (int i = 0; i < tabLog.length; i++) {
                        if (tabLog[i].trim().length() > 0) {
                            if (tabLog[i].trim().startsWith(environnementDetails)) {
                                envName = environnementDetails.replace("Environnement", "").trim();
                                try {
                                    if (deploiementEnCours.get(connectedUser) != null && envName != null) {
                                        if (deploiementEnCours.get(connectedUser).get(envName) != null) {
                                            if (deploiementEnCours.get(connectedUser).get(envName) == false) {
                                                StringBuilderTools.replaceFirstOccurenceIntoStringBuilder(logMessage, tabLog[i], ch.replace("<br>", ""));
                                            } else {
                                                StringBuilderTools.replaceFirstOccurenceIntoStringBuilder(logMessage, tabLog[i], ch.replace("<br>", "").replace("...", " ") + traiterRestoreDeploiement(connectedUser, envName) + "...");
                                                logMessage.append(traiterRestoreDeploiement(connectedUser, envName));
                                            }
                                        }
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    tools.Tools.traiterException(tools.Tools.getStackTrace(ex));
                                }
                            }
                        }
                    }
                } else {
                    logMessage.append(ch);
                }
            } else {
                if (logMessage.toString().trim().equals("...")) {
                    logMessage.replace(0, logMessage.length(), ch);
                } else {
                    logMessage.append(ch);
                }
            }
            if (logMessage.toString().contains("...Environnement")) {
                StringBuilderTools.replaceFirstOccurenceIntoStringBuilder(logMessage, "...Environnement", "...<br>Environnement");
            }
            //Tools.showConsolLog("______5______\n" + logMessage.toString() + "\n______6______\n\n\n\n\n\n");
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        logMessageMap.put(connectedUser, logMessage);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
