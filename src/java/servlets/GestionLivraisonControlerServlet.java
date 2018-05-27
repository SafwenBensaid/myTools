/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import dto.EnvironnementDTO;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import t24Scripts.PM;
import t24Scripts.T24Scripts;
import threads.ManageLogThread;
import tools.AnalysePack;
import tools.Configuration;
import tools.DeploiementParalleleTools;
import tools.FtpTools;
import tools.Tools;

/**
 *
 * @author 04486
 */
public class GestionLivraisonControlerServlet extends HttpServlet {

    private static final Object lock = new Object();

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
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        ManageLogThread manageLogThread = new ManageLogThread();
        try {
            manageLogThread.afficherLogThreads("****************** 1: START SERVLET LIVRAISON ********************");
            String cheminObjetDepot = null;
            String devServerName = null;
            String envirTestDepName = null;
            String analyseEnvironnement = null;
            String integrateur = "OVTOOLS";
            String connectedUser = Tools.getConnectedLogin();
            T24Scripts t24Scripts = new T24Scripts();
            EnvironnementDTO envirVersionning = Configuration.environnementDTOMapUserHasEnvironnement.get(connectedUser).get("VERSIONNING");
            String action = request.getParameter("action");
            servlets.AfficherMessageEtatAvancement.executionEnCours.put(connectedUser, true);
            String circuit = request.getParameter("circuit");
            if (circuit.equals("RELEASE")) {
                devServerName = "DEVR";
                envirTestDepName = "ASS";
                analyseEnvironnement = "RELEASE";
                cheminObjetDepot = Configuration.parametresList.get("cheminDepotRelese");
            } else if (circuit.equals("PROJET")) {
                devServerName = "DEV2";
                envirTestDepName = "ASS2";
                analyseEnvironnement = "PROJET";
                cheminObjetDepot = Configuration.parametresList.get("cheminDepotProjet");
            } else if (circuit.equals("HOTFIX")) {
                devServerName = "DEVH";
                envirTestDepName = "ASS";
                analyseEnvironnement = "RELEASE";
                cheminObjetDepot = Configuration.parametresList.get("cheminDepotTrunk");
            } else if (circuit.equals("UPGRADE")) {
                devServerName = "DEVU";
                envirTestDepName = "ASSU";
                analyseEnvironnement = "UPGRADE";
                cheminObjetDepot = Configuration.parametresList.get("environnementsCircuitUpgrade");
            }
            manageLogThread.afficherLogThreads("\n connectedUser: " + connectedUser + "\n action: " + action + "\n circuit: " + circuit);
            switch (action) {
                case "testBiatStBatch": {
                    String resultat = "";
                    cheminObjetDepot += "/BIAT.ST.BATCH/";
                    String chRes = request.getParameter("chRes").trim();
                    String[] objetsT24Array = chRes.split("\n");
                    for (String objT24 : objetsT24Array) {
                        String[] splitted = objT24.split(">");
                        String typeName = splitted[0];
                        String objName = splitted[1];
                        if (typeName.equals("BATCH") || typeName.equals("TSA.SERVICE")) {
                            boolean fileExists = t24Scripts.testExistanceFichier(envirVersionning, Tools.remplacerCaracteresSpeciaux("BIAT.ST.BATCH-" + objName), cheminObjetDepot);
                            if (fileExists) {
                                resultat += "<li>Attention c'est fortement deconseille de livrer l'objet <b>" + typeName + ">" + objName + "</b> car l'objet <b>BIAT.ST.BATCH>" + objName + "</b> existe dans le depot</li>";
                            }
                        }
                    }
                    //servlets.AfficherMessageEtatAvancement.executionEnCours.put(connectedUser, false);
                    out.println(resultat);
                    break;
                }
                case "testConnexion": {
                    String resultatTestAuthentificationGlobal = "";
                    boolean pb1 = false;
                    EnvironnementDTO envirASS = null;
                    try {
                        envirASS = Configuration.environnementDTOMapUserHasEnvironnement.get(integrateur).get(envirTestDepName);
                        if (envirASS == null) {
                            throw new NullPointerException();
                        }
                    } catch (Exception ex) {
                        pb1 = true;
                        resultatTestAuthentificationGlobal += "Problème d'authentification : Vous n'avez pas un utilisateur sur l'environnement <b>" + envirTestDepName + "</b>. Veuillez le créer à traver le menu <b>T24 -> Administration -> Profils utilisateurs</b>";
                    }
                    boolean pb2 = false;
                    EnvironnementDTO environnementDev = null;
                    try {
                        environnementDev = Configuration.environnementDTOMapUserHasEnvironnement.get(connectedUser).get(devServerName);
                        if (environnementDev == null) {
                            throw new NullPointerException();
                        }
                    } catch (Exception ex) {
                        pb2 = true;
                        resultatTestAuthentificationGlobal += "Problème d'authentification : Vous n'avez pas un utilisateur sur l'environnement <b>" + devServerName + "</b>.<br>Veuillez le créer à traver le menu <b>T24 -> Administration -> Profils utilisateurs</b>";
                    }
                    if (!pb1 && !pb2) {
                        String[] resTab = testAuthentification(envirASS, environnementDev, connectedUser);
                        if (resTab != null) {
                            resultatTestAuthentificationGlobal = resTab[1];
                        }

                        //resultatTestAuthentificationGlobal = "TEST CONNEXION OK";
                    }
                    servlets.AfficherMessageEtatAvancement.executionEnCours.put(connectedUser, false);
                    out.println(resultatTestAuthentificationGlobal);
                    break;
                }
                case "pmTransfertAnalyse": {
                    Map<String, String> resultMap = new HashMap<>();
                    resultMap.put("TN1", "");
                    resultMap.put("BNK", "");
                    resultMap.put("PACK_NAME_TN1", "");
                    resultMap.put("PACK_NAME_BNK", "");
                    resultMap.put("PROBLEME_OBJETS", "");
                    resultMap.put("STRUCTURE_PURE", "");
                    resultMap.put("NEW_POST_PACK", "");
                    resultMap.put("POST_PACK", "");
                    String mnemonicCompany = request.getParameter("selectedCompany");
                    String packName = request.getParameter("packName");
                    String objectList = request.getParameter("objectList");
                    String fileName = packName + ".txt";
                    String[] objetsT24Array = objectList.split("\n");
                    List<String> userObjectList = new ArrayList<>(Arrays.asList(objetsT24Array));
                    EnvironnementDTO environnementDev = Configuration.environnementDTOMapUserHasEnvironnement.get(connectedUser).get(devServerName);
                    EnvironnementDTO envirTestDep = Configuration.environnementDTOMapUserHasEnvironnement.get(integrateur).get(envirTestDepName);
                    int objectNumber = 0;
                    String resultatVerifObjets = "";
                    synchronized (lock) {
                        PM pm = new PM(environnementDev, fileName, mnemonicCompany, "PACK.TAF", connectedUser);
                        objectNumber = pm.preparerPackMan(objectList);
                        resultatVerifObjets = pm.PmFormerPack();
                    }
                    System.err.println("**********************************\n" + resultatVerifObjets + "\n**********************************");
                    //Vérification inéxistance Objets
                    if (resultatVerifObjets.length() > 0) {
                        String nomErreur = resultatVerifObjets;
                        if (resultatVerifObjets.indexOf("Sorry, but your session is no longer active") > 0 || resultatVerifObjets.indexOf("SECURITY.VIOLATION") > 0) {
                            nomErreur = "Erreur d'authentification";
                        }
                        resultMap.put("PROBLEME_OBJETS", nomErreur);
                    } else {
                        //Deplacer le pack vers ASS/PACK.DEP
                        FtpTools ftptools = new FtpTools();
                        ftptools.transferFolder(connectedUser, environnementDev, envirTestDep, "PACK.TAF", "PACK.TAF", "TAF-" + packName, true);
                        //boolean cusExists = false;
                        String resultatAnalysePack = null;
                        AnalysePack analysepack = new AnalysePack();
                        resultatAnalysePack = analysepack.analysePack(analyseEnvironnement, packName, objectNumber, false);
                        String[] tab = resultatAnalysePack.split("\n");
                        if (resultatAnalysePack.contains("FILE.CONTROL de type CUS") && analysepack.packTn1Vide == false) {
                            //cusExists = true;
                            //afficher le contenu des 2 packs dans le script verif cus puis les envoyer à la couche présentation
                            for (Map.Entry<String, String> entry : resultMap.entrySet()) {
                                String key = entry.getKey();
                                if (key.equals("TN1") || key.equals("BNK")) {
                                    String objT24List = traitementObjets(tab, "__" + key + "__", "", "", null);
                                    resultMap.put(key, objT24List.trim());
                                    resultMap.put("PACK_NAME_" + key, packName + "." + key);
                                }
                            }
                            ftptools.transferFolder(connectedUser, envirTestDep, environnementDev, "PACK.TAF", "PACK.TAF", "TAF-" + packName + ".BNK", true);
                            ftptools.transferFolder(connectedUser, envirTestDep, environnementDev, "PACK.TAF", "PACK.TAF", "TAF-" + packName + ".TN1", true);
                        } else if (resultatAnalysePack.contains("FILE.CONTROL de type CUS") && analysepack.packTn1Vide == true) {
                            //mnemonicCompany = "BNK";
                            //packName += ".BNK";
                            //purement structure
                            resultMap.put("STRUCTURE_PURE", "STRUCTURE_PURE");
                            resultMap.put("PACK_NAME_BNK", packName + ".BNK");
                            ftptools.transferFolder(connectedUser, envirTestDep, environnementDev, "PACK.TAF", "PACK.TAF", "TAF-" + packName + ".BNK", true);
                        }
                        //NEW_POST_PACK
                        if (resultatAnalysePack.contains("Dependance de programmes")) {
                            String objT24List = traitementObjets(tab, "__NPP__", "<li>", "</li>", userObjectList);
                            resultMap.put("NEW_POST_PACK", objT24List.trim());
                        }
                        //POST_PACK
                        if (resultatAnalysePack.contains("Modification de structure de table")) {
                            String objT24List = traitementObjets(tab, "__PP__", "<li>", "</li>", userObjectList);
                            resultMap.put("POST_PACK", objT24List.trim());
                        }
                    }
                    String jsonResult = Tools.objectToJsonString(resultMap);
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    out = response.getWriter();
                    out.println(jsonResult);
                    break;
                }
                case "endLog":
                    servlets.AfficherMessageEtatAvancement.setLogmessage("", connectedUser);
                    servlets.AfficherMessageEtatAvancement.executionEnCours.put(connectedUser, false);
                    break;
                case "testExistFolder": {
                    String folder = request.getParameter("folder");
                    EnvironnementDTO environnementDev = Configuration.environnementDTOMapUserHasEnvironnement.get(connectedUser).get(devServerName);
                    boolean folderExists = t24Scripts.testExistanceDossier(environnementDev, folder, "");
                    if (folderExists == false) {
                        out.println("Le dossier spécifié est introuvable");
                    } else {
                        out.println("");
                    }
                    break;
                }
            }
        } finally {
            out.close();
        }
    }

    private String traitementObjets(String[] tab, String regex, String prefixe, String suffixe, List<String> userObjectList) {
        String objT24List = "";
        for (String aux : tab) {
            if (aux.trim().contains(regex)) {
                String objT24 = null;
                objT24 = aux.trim().replace(regex, "");
                objT24 = objT24.replaceFirst("-", ">");
                objT24 = Tools.remplacerCaracteresSpeciauxInverse(objT24);
                if (userObjectList == null) {
                    objT24List += prefixe + objT24 + suffixe + "\n";
                } else {
                    if (!userObjectList.contains(objT24)) {
                        objT24List += prefixe + objT24 + suffixe + "\n";
                    }
                }
            }
        }
        return objT24List.trim();
    }

    public static synchronized String[] testAuthentification(EnvironnementDTO envirTestDep, EnvironnementDTO environnementDev, String connectedUser) {
        List<EnvironnementDTO> enironnementsCiblesList = new ArrayList<>();
        enironnementsCiblesList.add(environnementDev);
        enironnementsCiblesList.add(envirTestDep);
        DeploiementParalleleTools dpt = new DeploiementParalleleTools();
        String[] resTab = null;
        try {
            resTab = dpt.testAuthentification(enironnementsCiblesList, connectedUser);
        } catch (IOException ex) {
            Logger.getLogger(GestionLivraisonControlerServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resTab;
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
