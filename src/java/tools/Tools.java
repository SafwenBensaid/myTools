/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import dataBaseTracRequests.AppelRequetes;
import dto.EnvironnementDTO;
import dto.LinkedProperties;
import dto.UserDTO;
import dto.livraison.T24;
import entitiesMysql.Environnement;
import entitiesMysql.Groupe;
import entitiesMysql.Users;
import entitiesMysql.UsersHasEnvironnement;
import entitiesMysql.UsersHasEnvironnementPK;
import entitiesMysql.UsersHasGroupe;
import entitiesMysql.UsersHasGroupePK;
import entitiesTrac.Ticket;
import entitiesTrac.TicketCustom;
import org.apache.commons.io.FileUtils;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.*;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.simple.JSONValue;
import t24Scripts.T24Scripts;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import javax.mail.*;
import javax.mail.internet.*;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;
import springSecurity.SpringSecurityTools;
import threads.AutomatisationDeploiementIeThread;
import threads.GestionHotfixProdThread;

/**
 *
 * @author Moalla Med Anis
 */
public class Tools {

    public static void showConsolLog(String logmessage) {
        try {
            if (Configuration.parametresList.get("LOG").equals("1")) {
                System.out.println(logmessage);
            }
        } catch (Exception ex) {
        }
    }

    public static String traiterChamp(Map<String, Object> tickMap, String cle) {
        String resultat = null;
        try {
            resultat = tickMap.get(cle).toString().trim();
        } catch (Exception ex) {
            resultat = "";
        }
        return resultat;
    }

    public static String[] separerEnvironnementsNoms(String envConcat) {
        envConcat = envConcat.replace("et", ",");
        String[] tabEnvNameCircuit = envConcat.split(",");
        for (int i = 0; i < tabEnvNameCircuit.length; i++) {
            tabEnvNameCircuit[i] = tabEnvNameCircuit[i].trim();
        }
        return tabEnvNameCircuit;
    }

    public static void clearMapTicketsEnCours(String connectedUser) {
        //delete all tickets of connected user
        for (Iterator<Map.Entry<String, String>> it = Configuration.livraisonsEnCoursMap.entrySet().iterator(); it.hasNext();) {
            Map.Entry<String, String> entry = it.next();
            if (entry.getValue().equals(connectedUser)) {
                it.remove();
            }
        }
    }

    public static String traiterClignotantLivraisonEnCoursIE(String livraison) {
        try {
            if (AutomatisationDeploiementIeThread.livraisonsActionManuelleSet.contains(Integer.parseInt(livraison))) {
                return " class='clignotantRouge' title='" + Configuration.livraisonsEnCoursMapIE.get(livraison) + "' ";
            } else {
                if (Configuration.livraisonsEnCoursMapIE.containsKey(livraison)) {
                    return " class='clignotant' title='" + Configuration.livraisonsEnCoursMapIE.get(livraison) + "' ";
                } else {
                    return "";
                }
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
            return "";
        }
    }

    public static String traiterClignotantLivraisonEnCours(String livraison) {
        if (Configuration.livraisonsEnCoursMap.containsKey(livraison)) {
            return " class='clignotant' title='" + Configuration.livraisonsEnCoursMap.get(livraison) + "' ";
        } else {
            return "";
        }
    }

    public static String getEmailByLogin(String login) {
        String matricule = Configuration.loginMatriculeMap.get(login);
        UserDTO userDto = SpringSecurityTools.getUserSearch(matricule, "");
        return userDto.getEmail();
    }

    public static String getConnectedMatricule() {
        LdapUserDetailsImpl connectedUser = (LdapUserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userMatricule = connectedUser.getUsername();
        return userMatricule;
    }

    public static String getConnectedLogin() {
        String login = null;
        String userMatricule = null;
        LdapUserDetailsImpl connectedUser = null;
        try {
            //
            SecurityContext context = SecurityContextHolder.getContext();
            Authentication authentication = context.getAuthentication();
            Object principal = authentication.getPrincipal();
            if (principal instanceof LdapUserDetailsImpl) {
                connectedUser = (LdapUserDetailsImpl) principal;
            } else if (principal instanceof String) {
                return principal.toString();
            }
            //LdapUserDetailsImpl connectedUser = (LdapUserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            //Tools.showConsolLog("connectedUser " + connectedUser);
            userMatricule = connectedUser.getUsername();
            if (Configuration.matriculeLoginMap != null && Configuration.matriculeLoginMap.containsKey(userMatricule)) {
                login = Configuration.matriculeLoginMap.get(userMatricule);
            } else {
                login = "anonymousUser";
                //test
                //tools.Tools.traiterException("xx MÃ©thode Tools.getConnectedLogin()   |||    matricule:" + userMatricule + "|login:" + login);
                //
            }
        } catch (Exception ex) {
            // la méthode a été exécutée depuis un thread
            login = "mohamed anis moalla";
            //test
            //tools.Tools.traiterException("yy MÃ©thode Tools.getConnectedLogin()   |||    matricule:" + userMatricule + "|login:" + login);
            //
        }
        return login;
    }

    public static String getConnectedUserName() {
        String userName = null;
        try {
            LdapUserDetailsImpl connectedUser = (LdapUserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            //Tools.showConsolLog("connectedUser " + connectedUser);
            String userMatricule = connectedUser.getUsername();
            //Tools.showConsolLog("userMatricule " + userMatricule);
            userName = SpringSecurityTools.getUserSearch(userMatricule, "").getNom();
            //Tools.showConsolLog("userName " + userName);
        } catch (Exception ex) {
            ex.printStackTrace();
            userName = "anonymousUser";
        }
        return userName;
    }

    public static boolean testTempsEcoule(Date lastExecutionDate, int intervaleSeconds) {
        SimpleDateFormat parseFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String dateExectionRequeteString = parseFormat.format(lastExecutionDate);
        Date dateExpirationrequete = null;
        String dateExpirationrequeteString = null;
        try {
            dateExpirationrequete = parseFormat.parse(dateExectionRequeteString);
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateExpirationrequete);
            cal.add(Calendar.SECOND, intervaleSeconds);
            dateExpirationrequeteString = parseFormat.format(cal.getTime());
        } catch (java.text.ParseException exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        //s'il y a moins de 5 min que l'ancienne requete a été executée, retourner l'ancien résultat, sinon reexecuter la requete 
        if (Tools.isTimeToDo(dateExectionRequeteString, dateExpirationrequeteString, "dd-MM-yyyy HH:mm:ss", false)) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String reformTlivTicketOrigine(String cle, String valeur) {
        if (isInteger(valeur)) {
            return valeur;
        }
        String resultat = "";
        if (cle.equals("t_liv")) {
            if (valeur.contains(" ") && valeur.contains("]")) {
                String[] tabAux = valeur.split("-");
                try {
                    for (String ch : tabAux) {
                        resultat = resultat + ch.split(" ")[1].replace("]", "") + "-";
                    }
                    if (resultat.endsWith("-")) {
                        resultat = resultat.substring(0, resultat.length() - 1);
                    }
                } catch (Exception exep) {
                    exep.printStackTrace();
                    tools.Tools.traiterException(":::" + valeur + ":::\n!!!" + resultat + "!!!\n" + tools.Tools.getStackTrace(exep));
                }
            } else {
                resultat = valeur;
            }
            String[] tabAux = resultat.split("-");
            resultat = tabAux[tabAux.length - 1];
        } else if (cle.equals("ticket_origine") || cle.equals("ticket_appl_prod")) {
            if (valeur.contains(" ") && valeur.contains("]")) {
                try {
                    valeur = valeur.split(" ")[1].replace("]", "");
                } catch (Exception exep) {
                    exep.printStackTrace();
                    tools.Tools.traiterException("***" + valeur + ":::\n!!!" + "$$$\n" + tools.Tools.getStackTrace(exep));
                }
                resultat = valeur;
            }
        }
        return resultat;
    }

    public static Object reformTlivTicketOrigine(Object tickCus, String siteTrac) {
        if (siteTrac.equals(Configuration.tracAnomalies)) {
            TicketCustom tickCusAux = (TicketCustom) tickCus;
            if (tickCusAux.getName().equals("t_liv")) {
                String valAux = tickCusAux.getValue();
                if (valAux.contains(" ") && valAux.contains("]")) {
                    String[] tabAux = valAux.split("-");
                    String resultat = "";
                    try {
                        for (String ch : tabAux) {
                            resultat = resultat + ch.split(" ")[1].replace("]", "") + "-";
                        }
                        if (resultat.endsWith("-")) {
                            resultat = resultat.substring(0, resultat.length() - 1);
                        }
                    } catch (Exception exep) {
                        exep.printStackTrace();
                        tools.Tools.traiterException(":::" + valAux + ":::\n!!!" + resultat + "!!!\n" + tools.Tools.getStackTrace(exep));
                    }
                    tickCusAux.setValue(resultat);
                }
                /*
                 Tools.showConsolLog("_______________________*3*___________________________");
                 Tools.showConsolLog(tickCusAux.getValue());
                 Tools.showConsolLog("_______________________*4*___________________________");
                 */
                return tickCusAux;
            }
        } else if (siteTrac.equals(Configuration.tracLivraisons)) {
            TicketCustom tickCusAux = (TicketCustom) tickCus;
            if (tickCusAux.getName().equals("ticket_origine") || tickCusAux.getName().equals("ticket_appl_prod")) {
                String valAux = tickCusAux.getValue();
                if (valAux.contains(" ") && valAux.contains("]")) {
                    try {
                        valAux = valAux.split(" ")[1].replace("]", "");
                    } catch (Exception exep) {
                        exep.printStackTrace();
                        tools.Tools.traiterException("***" + valAux + ":::\n!!!" + "$$$\n" + tools.Tools.getStackTrace(exep));
                    }
                    tickCusAux.setValue(valAux);
                }
                /*
                 Tools.showConsolLog("_______________________*1*___________________________");
                 Tools.showConsolLog(tickCusAux.getValue());
                 Tools.showConsolLog("_______________________*2*___________________________");
                 */
                return tickCusAux;
            }
        }
        return tickCus;
    }

    public static ActionForward redirectionPageErreurs(String titreErreur, String messageErreur, ActionMapping mapping,
            HttpServletRequest request, HttpServletResponse response, String connectedUser) throws IOException {

        Map<String, String> errorMap = new HashMap<String, String>();
        errorMap.put("errorName", titreErreur);
        errorMap.put("errorValue", messageErreur);
        request.getSession().setAttribute("erreurNameErreurValue", errorMap);
        servlets.AfficherMessageEtatAvancement.executionEnCours.put(connectedUser, false);
        servlets.AfficherMessageEtatAvancement.setLogmessage("", connectedUser);
        response.setContentType("text/text;charset=utf-8");
        response.setHeader("cache-control", "no-cache");
        PrintWriter out = response.getWriter();
        out.println("PROBLEME_EXISTE");
        out.flush();
        servlets.AfficherMessageEtatAvancement.setLogmessage("", connectedUser);
        return mapping.findForward("AfficherMessageErreurs");
    }

    public boolean hasRole(String role) {
        // get security context from thread local
        Tools.showConsolLog("***************************");
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) {
            Tools.showConsolLog("------CONTEXT NULL------");
            Tools.showConsolLog("***************************");
            return false;
        }
        Authentication authentication = context.getAuthentication();
        if (authentication == null) {
            Tools.showConsolLog("------AUTHENTICATION NULL------");
            Tools.showConsolLog("***************************");
            return false;
        }
        String username = authentication.getName();
        for (GrantedAuthority auth : authentication.getAuthorities()) {
            if (role.equals(auth.getAuthority())) {
                Tools.showConsolLog("+++++ROLE:" + role + "|USER:" + username + "|TRUE+++++");
                Tools.showConsolLog("***************************");
                return true;
            }
        }
        Tools.showConsolLog("-----ROLE:" + role + "|USER:" + username + "|FALSE-----");
        Tools.showConsolLog("***************************");
        return false;
    }

    public String getInexistantsObjetsIntoDepot(String resultatCommande, String depot) {
        if (!resultatCommande.contains("cannot access")) {
            return "";
        } else {
            StringBuilder objetsInexistants = new StringBuilder();
            try {
                objetsInexistants.append("\n<center><b>**** Liste des objets inexistants sur le dépôt: ");
                objetsInexistants.append(depot);
                objetsInexistants.append(" ****</b></center>\n");

                String[] tab1 = resultatCommande.split("\n");
                String[] tab2;
                for (String ch : tab1) {
                    if (ch.contains("cannot access")) {
                        tab2 = ch.split("/");
                        objetsInexistants.append(tab2[tab2.length - 1].replaceFirst("-", ">"));
                        objetsInexistants.append("<br>");
                    }
                }
            } catch (Exception exep) {
                exep.printStackTrace();
                tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
            }
            return objetsInexistants.toString();
        }
    }

    public LinkedHashMap<String, LinkedProperties> transformAllPipeTickets(String resultatRequete) {
        LinkedHashMap<String, LinkedProperties> mapPipeTickets = new LinkedHashMap<String, LinkedProperties>();
        try {
            mapPipeTickets.put(Configuration.parametresList.get("phaseRelease"), new LinkedProperties());
            mapPipeTickets.put("QUALIFICATION_PROJET", new LinkedProperties());
            mapPipeTickets.put("HOT FIXE TEST", new LinkedProperties());
            mapPipeTickets.put("HARMONISATION_C.RELEASE", new LinkedProperties());
            mapPipeTickets.put("HARMONISATION_C.PROJET", new LinkedProperties());
            mapPipeTickets.put("HF.PROD", new LinkedProperties());
            mapPipeTickets.put("ACTION A CHAUD TEST", new LinkedProperties());
            mapPipeTickets.put("ACTION A CHAUD PROD", new LinkedProperties());
            mapPipeTickets.put("HARMONISATION_HF_OV", new LinkedProperties());

            String[] tableauTicketsDetails = resultatRequete.split("___");
            String[] tableauTickDetails;
            LinkedProperties prop;
            String livraison = "";
            String projet = "";
            String type = "";
            String anomalie = "";

            for (String det : tableauTicketsDetails) {
                if (det.contains(":::")) {
                    tableauTickDetails = det.split(":::");
                    try {
                        livraison = tableauTickDetails[0];
                        if (livraison == null || livraison.length() == 0) {
                            livraison = "-";
                        }
                    } catch (Exception ex) {
                        livraison = "-";
                    }
                    try {
                        projet = tableauTickDetails[1];
                        if (projet == null || projet.length() == 0) {
                            projet = "-";
                        }
                    } catch (Exception ex) {
                        projet = "-";
                    }
                    try {
                        type = tableauTickDetails[2];
                        if (type == null || type.length() == 0) {
                            type = "-";
                        }
                    } catch (Exception ex) {
                        type = "-";
                    }
                    try {
                        anomalie = tableauTickDetails[3];
                        if (anomalie == null || anomalie.length() == 0) {
                            anomalie = "-";
                        }
                    } catch (Exception ex) {
                        anomalie = "-";
                    }
                    prop = mapPipeTickets.get(type);
                    if (prop != null) {
                        prop.setProperty(livraison, anomalie + "___" + projet);
                        mapPipeTickets.put(type, prop);
                    } else {
                        String messageErreur = "Veuillez verifier le type de la livraison: #" + livraison + " Certification / Qualification";
                        T24Scripts t24Scripts = new T24Scripts();
                        String connectedUser = Tools.getConnectedLogin();
                        EnvironnementDTO env = Configuration.environnementDTOMapUserHasEnvironnement.get(connectedUser).get("VERSIONNING");
                        String commande = "./envoi_mail_ticket_mal_parametre \"" + messageErreur + "\"";
                        try {
                            t24Scripts.executerCommandeListEnvironnement(env, "/DEPT24/mailing_ov", commande);
                        } catch (Exception exep) {
                            exep.printStackTrace();
                            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
                        }
                    }
                }
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return mapPipeTickets;
    }

    public void deleteFile(String fileName) {
        try {
            File f = new File(fileName);
            if (f.exists()) {
                f.delete();
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }

    public void unTarPack(String dossierLocalConteneur, String fileNameTar) {
        try {
            File outputDir = new File(dossierLocalConteneur);
            Untar untar = new Untar(dossierLocalConteneur + "/" + fileNameTar, outputDir);
            untar.untar();
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }

    public static String remplacerCaracteresSpeciauxInverse(String ch) {
        try {
            ch = ch.replace("^M", "/");
            ch = ch.replace("^A", "*");
            ch = ch.replace("7^7^R", "%");
            ch = ch.replace("^Q", "$");
            ch = ch.replace(".7^7.", "..");
            ch = ch.replace("^H", "&");
            ch = ch.replace("^N", "'");
            ch = ch.replace("-7^7-", "--");
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return ch;
    }

    public static String remplacerCaracteresSpeciaux(String ch) {
        try {
            ch = ch.replace("/", "^M");
            ch = ch.replace("*", "^A");
            ch = ch.replace("%", "7^7^R");
            ch = ch.replace("$", "^Q");
            ch = ch.replace("..", ".7^7.");
            ch = ch.replace("&", "^H");
            ch = ch.replace("'", "^N");
            ch = ch.replace("--", "-7^7-");
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return ch;
    }

    public File createDirectoryIfNotExists(String url) {
        File dir = new File(url);
        try {
            String absol = dir.getAbsolutePath();
            boolean b = false;
            if (!dir.exists()) {
                b = dir.mkdir();
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return dir;
    }

    public void deplacerDossier(String urlFile, String urlFolderDestination) {
        try {
            Tools.showConsolLog(urlFile);
            Tools.showConsolLog(urlFolderDestination);
            File repOrig = new File(urlFile);
            File repNew = new File(urlFolderDestination);
            if (repNew.exists()) {
                try {
                    FileUtils.deleteDirectory(repNew);
                } catch (IOException ex) {
                    Logger.getLogger(Tools.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            boolean resultat = repOrig.renameTo(new File(urlFolderDestination));
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }

    public void deplacer(File fich, String urlFolderDestination) {
        try {
            File rep = new File(urlFolderDestination);
            if (!rep.exists()) {
                rep.mkdir();
            }
            // Déplacer le fichier vers le disque D:
            File f = new File(urlFolderDestination + "/" + fich.getName());
            if (f.exists()) {
                f.delete();
            }
            boolean resultat = fich.renameTo(new File(urlFolderDestination + "/" + fich.getName()));
            /*
             if (resultat) {
             Tools.showConsolLog("Le fichier a été déplacé vers==> " + rep);
             } else {
             Tools.showConsolLog("Impossible de déplacer ce fichier");
             }
             */
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }

    public void mergeObjetsParType(String exportName, String dossierDownload) {
        servlets.AfficherMessageEtatAvancement.setLogmessage("Regroupement des objets par type", Tools.getConnectedLogin());
        List<File> ListOfFiles;
        File aux;
        try {
            Tools.showConsolLog(exportName);
            File exportDirectory = new File(dossierDownload + "/" + exportName);
            ListOfFiles = new FileListing().listAllFiles(exportDirectory);

            for (File fich : ListOfFiles) {
                if (fich.isFile()) {
                    aux = new File(dossierDownload + "/" + exportName + "/" + fich.getName());
                    if (!aux.getAbsolutePath().equals(fich.getAbsolutePath())) {
                        deplacer(fich, dossierDownload + "/" + exportName);
                    }
                }
            }
            //supprimer les dossiers vides
            File[] tableauFiles = exportDirectory.listFiles();
            for (File fich : tableauFiles) {
                if (fich.isDirectory()) {
                    fich.delete();
                }
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        } finally {
            //servlets.AfficherMessageEtatAvancement.setLogmessage("");
        }
    }

    public void triObjetsParType(String exportName, String dossierDownload) {
        servlets.AfficherMessageEtatAvancement.setLogmessage("Regroupement des objets par type", Tools.getConnectedLogin());
        List<File> ListOfFiles;
        File aux;
        try {
            Tools.showConsolLog(exportName);
            File exportDirectory = new File(dossierDownload + "/" + exportName);
            ListOfFiles = new FileListing().listAllFiles(exportDirectory);
            String typeObjet = "";
            String typeObjetDossierUrl = "";
            for (File fich : ListOfFiles) {
                if (fich.isFile()) {
                    typeObjet = fich.getName().split("-")[0];
                    typeObjetDossierUrl = dossierDownload + "/" + exportName + "/" + typeObjet;
                    createDirectoryIfNotExists(typeObjetDossierUrl);
                    aux = new File(typeObjetDossierUrl + "/" + fich.getName());
                    if (!aux.getAbsolutePath().equals(fich.getAbsolutePath())) {
                        deplacer(fich, typeObjetDossierUrl);
                    }
                }
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        } finally {
            //servlets.AfficherMessageEtatAvancement.setLogmessage("");
        }
    }

    public static File createFile(String url) throws IOException {
        File file = new File(url);
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
        } catch (Exception exep) {
            exep.printStackTrace();
        }
        return file;
    }

    public String traiterListString(List<String> listeString) {
        servlets.AfficherMessageEtatAvancement.setLogmessage("Traitement des objets", Tools.getConnectedLogin());
        Tools.showConsolLog("nbr objets: " + listeString.size());
        String res = "";
        try {
            for (int i = 0; i < listeString.size() - 1; i++) {
                if (listeString.get(i).length() > 0) {
                    res += listeString.get(i) + "\n";
                }
            }
            if (listeString.get(listeString.size() - 1).length() > 0) {
                res += listeString.get(listeString.size() - 1);
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        //servlets.AfficherMessageEtatAvancement.setLogmessage("");
        return res;
    }

    public String traiterListString(List<String> listeString, String separateur) {
        servlets.AfficherMessageEtatAvancement.setLogmessage("Traitement des objets", Tools.getConnectedLogin());
        Tools.showConsolLog("nbr objets: " + listeString.size());
        String res = "";
        try {
            for (int i = 0; i < listeString.size() - 1; i++) {
                if (listeString.get(i).length() > 0) {
                    res += listeString.get(i) + separateur;
                }
            }
            if (listeString.get(listeString.size() - 1).length() > 0) {
                res += listeString.get(listeString.size() - 1);
            }
            //servlets.AfficherMessageEtatAvancement.setLogmessage("");
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return res;
    }

    public List<String> convertObjectListToObjetctPathList(String objets) {
        servlets.AfficherMessageEtatAvancement.setLogmessage("Traitement des objets", Tools.getConnectedLogin());
        String[] tableauObjets = objets.split("\n");
        List<String> retour = new ArrayList<String>();
        try {
            String type = "";
            String objet = "";
            String[] tab = null;
            for (String ch : tableauObjets) {
                if (ch.contains(">")) {
                    ch = remplacerCaracteresSpeciaux(ch);
                    tab = ch.split(">");
                    type = tab[0];
                    objet = tab[1];
                    retour.add(type + "/" + type + "-" + objet);
                }
            }
            //servlets.AfficherMessageEtatAvancement.setLogmessage("");
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return retour;
    }

    public List<String> listDirectoryFiles(EnvironnementDTO environnementSource, String pathDuPackEnvSource, String packName) {
        String pathDuPackEnvSource1 = pathDuPackEnvSource + "/";
        List<String> listOfFiles = new ArrayList<String>();
        Tools tools = new Tools();
        T24Scripts t24Scripts = new T24Scripts();
        FtpTools ftpTools = new FtpTools();
        long millisecondsTime = System.currentTimeMillis();
        t24Scripts.executerCommandeListEnvironnement(environnementSource, pathDuPackEnvSource1 + packName, "find ./ -type f |awk '{ print \"ls \",$1 }'|bash > ../VERIFICATIONDELTAOV_" + millisecondsTime + ".txt");
        ftpTools.downloadFile(environnementSource, pathDuPackEnvSource, "VERIFICATIONDELTAOV_" + millisecondsTime + ".txt");
        String[] fileTab = tools.convertFileContentToTab(Configuration.parametresList.get("espaceLocal") + "/VERIFICATIONDELTAOV_" + millisecondsTime + ".txt");
        Tools.showConsolLog("__________________________");
        for (int i = 0; i < fileTab.length; i++) {
            if ((fileTab[i].trim().length() > 0)) {
                String auxString;
                String auxTab[];
                auxString = fileTab[i].trim();
                auxTab = auxString.split("./");
                auxString = auxTab[auxTab.length - 1];
                auxString = auxString.replaceFirst("-", ">");
                auxString = auxString.replace("^M", "/");
                auxString = auxString.replace("^A", "*");
                auxString = auxString.replace("7^7^R", "%");
                auxString = auxString.replace("^Q", "$");
                auxString = auxString.replace(".7^7.", "..");
                auxString = auxString.replace("^H", "&");
                auxString = auxString.replace("^N", "'");
                auxString = auxString.replace("-7^7-", "--");

                if (auxString.contains(">")) {
                    //Tools.showConsolLog("###" + auxString);
                    listOfFiles.add(auxString);
                }
            }
        }
        Tools.showConsolLog("_____________list Size_____________" + listOfFiles.size());
        return listOfFiles;
    }

    public String traiterString(String ch, String... connectedUserArray) {
        String res = "";
        try {
            try {
                String connectedUser = null;
                if (connectedUserArray.length > 0) {
                    connectedUser = connectedUserArray[0];
                } else {
                    connectedUser = Tools.getConnectedLogin();
                }
                servlets.AfficherMessageEtatAvancement.setLogmessage("Traitement d'objets", connectedUser);
            } catch (Exception exep) {
                exep.printStackTrace();
                tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
            }
            ch = ch.replace(" ", "");
            ch = ch.replace("\r", "");
            String[] tab = ch.split("\n");
            for (int i = 0; i < tab.length - 1; i++) {
                if (tab[i].length() > 0) {
                    res += tab[i] + "\n";
                }
            }
            if (tab[tab.length - 1].length() > 0) {
                res += tab[tab.length - 1];
            }
            //servlets.AfficherMessageEtatAvancement.setLogmessage("");
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return res;
    }

    public String[] convertFileContentToTab(String FilePathName, String... connectedUserParam) {
        String connectedUser = null;
        if (connectedUserParam.length > 0) {
            connectedUser = connectedUserParam[0];
        } else {
            connectedUser = Tools.getConnectedLogin();
        }
        servlets.AfficherMessageEtatAvancement.setLogmessage("Traitement des objets", connectedUser);
        List<String> liste = new ArrayList<String>();
        String[] array = null;
        try {
            InputStream ips = new FileInputStream(FilePathName);
            InputStreamReader ipsr = new InputStreamReader(ips);
            BufferedReader br = new BufferedReader(ipsr);
            String ligne;
            while ((ligne = br.readLine()) != null) {
                if (ligne.trim().length() > 0) {
                    liste.add(ligne.trim());
                }
            }
            try {
                br.close();
                ipsr.close();
                ips.close();
            } catch (Exception exp) {
                System.err.println(exp.getMessage());
            }
            array = liste.toArray(new String[liste.size()]);
        } catch (Exception e) {
            Tools.showConsolLog(e.toString());
        }
        //servlets.AfficherMessageEtatAvancement.setLogmessage("");
        return array;
    }

    public String viderLeDossierSiRempliSinonLeCreer(EnvironnementDTO env, String packName, String dossierBase) {
        String resultat = null;
        try {
            T24Scripts t24scripts = new T24Scripts();
            resultat = t24scripts.executerCommandeListEnvironnement(env, dossierBase, "rm -rf " + packName, "mkdir " + packName);
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return resultat;
    }

    public static String objectToJsonString(Object objet) {
        StringWriter out = new StringWriter();
        try {
            JSONValue.writeJSONString(objet, out);
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return out.toString();
    }

    public boolean testIfResultatDeploiementContainsProblems(String msg) {
        boolean problemeDeploiementExiste = false;
        try {
            if (msg.contains("RESTORE3")) {
                String auxTab[] = msg.split("RESTORE3");
                String aux = auxTab[auxTab.length - 1];
                if (aux.contains("erreurs")) {
                    problemeDeploiementExiste = true;
                }
            }
            if (!msg.contains("RESTORE2") || !msg.contains("RESTORE3")) {
                problemeDeploiementExiste = true;
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return problemeDeploiementExiste;
    }

    public static void writeInFile(File file, String text, boolean... nePasTraiterException) throws IOException {
        PrintWriter ecrivain = null;
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            fw = new FileWriter(file);
            bw = new BufferedWriter(fw);
            ecrivain = new PrintWriter(bw);
            ecrivain.println(text);
        } catch (Exception exep) {
            exep.printStackTrace();
            if (nePasTraiterException.length == 0) {
                tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
            }
        } finally {
            try {
                if (ecrivain != null) {
                    ecrivain.close();
                }
                if (bw != null) {
                    bw.close();
                }
                if (fw != null) {
                    fw.close();
                }
            } catch (Exception exp) {
                exp.printStackTrace();
            }
        }
    }

    public synchronized void writeInFile(File file, List<String> lignesListe) throws IOException {
        PrintWriter ecrivain = null;
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            fw = new FileWriter(file);
            bw = new BufferedWriter(fw);
            ecrivain = new PrintWriter(bw);
            for (int i = 0; i < lignesListe.size(); i++) {
                String ch = lignesListe.get(i);
                if (i < lignesListe.size() - 1) {
                    ecrivain.println(ch);
                } else {
                    ecrivain.print(ch);
                }
            }
            /*
             for (String ch : lignesListe) {
             ecrivain.println(ch);
             }
             */
        } catch (Exception exep) {
            exep.printStackTrace();
            traiterException(tools.Tools.getStackTrace(exep));
        } finally {
            try {
                if (ecrivain != null) {
                    ecrivain.close();
                }
                if (bw != null) {
                    bw.close();
                }
                if (fw != null) {
                    fw.close();
                }
            } catch (Exception exp) {
                exp.printStackTrace();
            }
        }
    }

    public static void traiterException(String exceptionMessage) {
        Tools.showConsolLog("_______________________________________");
        Tools.showConsolLog("_______________________________________");
        Tools.showConsolLog("_______________________________________");
        Tools.showConsolLog("_______________________________________");
        Tools.showConsolLog(exceptionMessage);
        Tools.showConsolLog("_______________________________________");
        Tools.showConsolLog("_______________________________________");
        Tools.showConsolLog("_______________________________________");
        Tools.showConsolLog("_______________________________________");
        sendEMail("Exception Plateforme Ov Management Solutions", "OvManagementSolutions@biat.com.tn", new String[]{"safwen.bensaid@biat.com.tn", /*"faten.slim@biat.com.tn"/*, "walid.benmahmoud@biat.com.tn", "ibtihel.makhloufi@biat.com.tn", "ahmed.daadaa@biat.com.tn", "akram.lassoued@biat.com.tn", "zied.ghanmi@biat.com.tn"*/}, new String[]{}, new String[]{}, exceptionMessage, true);
    }

    public static void sendEMail(String subject, String from, String[] to, String[] cc, String[] bcc, String emailContent, boolean addAttachment, int... iterations) {
        System.out.println("--------------------------STHARM-----------------------------");
        System.out.println(subject);
        System.out.println("-------------------------------------------------------");
        System.out.println(emailContent);
        System.out.println("--------------------------ENDHARM---------------------------");
        GestionHotfixProdThread.afficherLog("--------------------------STHARM-----------------------------");
        GestionHotfixProdThread.afficherLog(subject);
        GestionHotfixProdThread.afficherLog(emailContent);
        GestionHotfixProdThread.afficherLog("--------------------------ENDHARM---------------------------");
        /*
         Tools.showConsolLog("______EMAIL______");
         Tools.showConsolLog("SUJET:" + subject);
         Tools.showConsolLog("FROM:" + from);
         Tools.showConsolLog("TO:");
         for (String a : to) {
         System.out.print(a + ", ");
         }
         Tools.showConsolLog("");
         Tools.showConsolLog("CC:");
         for (String a : cc) {
         System.out.print(a + ", ");
         }
         Tools.showConsolLog("");
         Tools.showConsolLog("BCC:");
         for (String a : bcc) {
         System.out.print(a + ", ");
         }
         Tools.showConsolLog("");
         Tools.showConsolLog("EMAIL_CONTENT:");
         Tools.showConsolLog(emailContent);
         Tools.showConsolLog("______FIN DESCRIPTION EMAIL______");
         File file = null;
         try {
         MimeMultipart mimeMultipart = new MimeMultipart();
         //corps email
         MimeBodyPart mimeBodyPartMail = new MimeBodyPart();
         mimeBodyPartMail.setContent(emailContent, "text/html; charset=utf-8");
         mimeMultipart.addBodyPart(mimeBodyPartMail);

         if (addAttachment) {
         //preparer le fichier de la piece jointe                
         String connectedUser = null;
         long dateActuelle = System.currentTimeMillis();

         String espaceLocal;
         if (Configuration.parametresList != null && Configuration.parametresList.containsKey("espaceLocal")) {
         espaceLocal = Configuration.parametresList.get("espaceLocal");
         } else {
         espaceLocal = "C:\\FILES";
         }

         try {
         file = createFile(espaceLocal + System.getProperty("file.separator") + dateActuelle);
         writeInFile(file, emailContent, true);
         } catch (IOException ex) {
         ex.printStackTrace();
         }

         //joindre le fichier
         FileDataSource datasource1 = new FileDataSource(file);
         DataHandler handler1 = new DataHandler(datasource1);
         MimeBodyPart mimeBodyPartPJ = new MimeBodyPart();
         mimeBodyPartPJ.setDataHandler(handler1);
         mimeBodyPartPJ.setFileName("MessageException.txt");
         mimeMultipart.addBodyPart(mimeBodyPartPJ);
         }

         String host = "172.28.73.4";
         //Get the session object  
         Properties props = new Properties();
         props.put("mail.smtp.host", host);
         props.put("mail.smtp.auth", "false");


         Session session = Session.getDefaultInstance(props, null);
         session.setDebug(true);

         //Compose the message  

         InternetAddress[] addressTo = new InternetAddress[to.length];
         InternetAddress[] addressCc = new InternetAddress[cc.length];
         InternetAddress[] addressBcc = new InternetAddress[bcc.length];

         MimeMessage message = new MimeMessage(session);
         message.setFrom(new InternetAddress(from));
         message.saveChanges();

         for (int i = 0; i < to.length; i++) {
         if (to[i].length() > 0) {
         addressTo[i] = new InternetAddress(to[i]);
         }
         }

         for (int i = 0; i < cc.length; i++) {
         if (cc[i].length() > 0) {
         addressCc[i] = new InternetAddress(cc[i]);
         }
         }

         for (int i = 0; i < bcc.length; i++) {
         if (bcc[i].length() > 0) {
         addressBcc[i] = new InternetAddress(bcc[i]);
         }
         }

         message.setRecipients(Message.RecipientType.TO, addressTo);
         message.setRecipients(Message.RecipientType.CC, addressCc);
         message.setRecipients(Message.RecipientType.BCC, addressBcc);
         message.setSubject(subject);
         message.setContent(mimeMultipart);

         //send the message

         Transport.send(message);

         Tools.showConsolLog("message sent successfully...");

         } catch (Exception exep) {
         //exep.printStackTrace();
         Tools.showConsolLog("Probleme d'envoi de message!!!");
         } finally {
         if (file != null) {
         file.delete();
         }
         } 
         */
    }

    public static String getStackTrace(final Throwable throwable) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }

    public static void waitReadInputStream(InputStream inputStream) {
        try {
            int readedByte = 0;
            int data = 1;
            int nbrBitRestants = inputStream.available();
            String osType = Configuration.parametresList.get("OS");
            int conditionArret = 0;
            if (osType.equals("WINDOWS")) {
                conditionArret = 62;
            } else if (osType.equals("LINUX")) {
                conditionArret = -1;
            }
            readedByte = inputStream.read();
            while (readedByte != conditionArret) {
                nbrBitRestants = inputStream.available();
                readedByte = inputStream.read();
                //Tools.showConsolLog(" ___ " + nbrBitRestants); 
            }
            Thread.sleep(2000);
            try {
                inputStream.close();
            } catch (Exception exp) {
                System.err.println(exp.getMessage());
            }

        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }

    public static TicketCustom createTicketCustom(Ticket ticket, String name, String value) {
        TicketCustom ticketCustom = new TicketCustom(ticket.getId());
        ticketCustom.setTicketPointer(ticket);
        ticketCustom.setName(name);
        ticketCustom.setValue(value);
        return ticketCustom;
    }

    public static boolean isTimeToDo(String startDateString, String endDateString, String pattern, boolean log) {
        boolean sendHfProd = false;
        boolean nowAfterStart = false;
        boolean nowBeforeEnd = false;
        Boolean resultat = false;
        Date dateActuelle = null;
        Date endDate = null;
        Date startDate = null;
        if (startDateString != null && endDateString != null && startDateString.equals(endDateString)) {
            return true;
        }
        try {
            //Convertir le résultat en Date
            SimpleDateFormat parseFormat = new SimpleDateFormat(pattern);
            Date tmp = new Date();
            parseFormat.setTimeZone(TimeZone.getTimeZone("GMT+1"));
            String dateStringAux = parseFormat.format(tmp);

            if (log) {
                System.out.println("temps actuel:" + dateStringAux);
            }
            dateActuelle = parseFormat.parse(dateStringAux);
            startDate = parseFormat.parse(startDateString);
            if (log) {
                System.out.println("dateDebut: " + parseFormat.format(startDate));
            }
            if (endDateString != null) {
                endDate = parseFormat.parse(endDateString);
                if (log) {
                    System.out.println("dateArret: " + parseFormat.format(endDate));
                }
                nowAfterStart = dateActuelle.after(startDate) || dateActuelle.equals(startDate);
                nowBeforeEnd = dateActuelle.before(endDate);
                resultat = nowAfterStart && nowBeforeEnd;
            } else {
                resultat = dateActuelle.equals(startDate);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            resultat = false;
        }
        if (log) {
            System.out.println(resultat.toString());
        }
        return resultat;
    }

    public static Map<Integer, Map<String, Object>> analyseTicketsAnomalies(List<Integer> globalList) {
        Map<Integer, Map<String, Object>> globalResultMap = new HashMap<Integer, Map<String, Object>>();
        String[] cles = new String[]{"t_liv", "mode_traitement", "motivation_hf", "projet"};
        if (globalList.size() > 0) {
            globalResultMap = AppelRequetes.getTicketCustomByTicketIdAndNames(globalList, Configuration.puAnomalies, Configuration.tracAnomalies, cles);
        } else {
            Tools.showConsolLog("Aucun ticket");
        }
        return globalResultMap;
    }

    public static String sha1(String input) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    public static byte[] convertFileToByteArray(File file) {
        byte[] b = new byte[(int) file.length()];
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(b);
            for (int i = 0; i < b.length; i++) {
                System.out.print((char) b[i]);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found.");
            e.printStackTrace();
        } catch (IOException e1) {
            System.out.println("Error Reading The File.");
            e1.printStackTrace();
        }
        return b;
    }

    public static byte[] createLivraisonList(String selectedMnemonic, String packName, int nbrIter, String objetsT24) {
        List<Object> livraisonList = new ArrayList<>();
        T24 t24 = new T24();
        t24.setSelectedMnemonic(selectedMnemonic);
        t24.setPackName(packName);
        t24.setNbrIter("" + nbrIter);
        t24.setObjetsT24(objetsT24);
        t24.setWarningsT24("");
        livraisonList.add(t24);
        byte[] serializedLivraisonList = ManipulationObjectsTool.serialisation(livraisonList);
        return serializedLivraisonList;
    }

    public static void alertParEmail(String titre, String emailContent, String... dest) {
        System.out.println("*********************************");
        String[] a = null;
        String[] cc = null;
        String[] cci = null;
        if (dest.length > 0) {
            a = dest;
            cc = new String[]{};
            cci = new String[]{};
        } else {
            a = new String[]{"C24OV@biat.com.tn"};
            cc = new String[]{};
            cci = new String[]{};
        }

        tools.Tools.sendEMail(titre, "OV.Management.Solutions@biat.com.tn", a, cc, cci, emailContent, false);
        System.out.println("*********************************");
    }

    public static Map<Integer, Long> sortByComparator(Map<Integer, Long> unsortMap) {

        // Convert Map to List
        List<Map.Entry<Integer, Long>> list = new LinkedList<Map.Entry<Integer, Long>>(unsortMap.entrySet());

        // Sort list with comparator, to compare the Map values
        Collections.sort(list, new Comparator<Map.Entry<Integer, Long>>() {
            public int compare(Map.Entry<Integer, Long> o1,
                    Map.Entry<Integer, Long> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        // Convert sorted map back to a Map
        Map<Integer, Long> sortedMap = new LinkedHashMap<Integer, Long>();
        for (Iterator<Map.Entry<Integer, Long>> it = list.iterator(); it.hasNext();) {
            Map.Entry<Integer, Long> entry = it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    public static void insertNewUserIntoDB(String matricule) {

        try {
            DataBaseTools dbTools = new DataBaseTools(Configuration.puOvTools);
            UserDTO userDTO = SpringSecurityTools.getUserSearch(matricule, "");
            String userName = userDTO.getNom().toLowerCase();
            Users newUser = new Users();
            newUser.setLogin(userName);
            newUser.setMatricule(matricule);
            newUser.setNomPrenom(userDTO.getNom());
            newUser.setDateCreation(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()));
            newUser.setEmail(userDTO.getEmail());

            Environnement envVers = dbTools.em.find(Environnement.class, "VERSIONNING");
            UsersHasEnvironnement userHasEnv = new UsersHasEnvironnement();
            UsersHasEnvironnementPK userHasEnvPk = new UsersHasEnvironnementPK(userName, "VERSIONNING");
            userHasEnv.setUsersHasEnvironnementPK(userHasEnvPk);
            userHasEnv.setUsers(newUser);
            userHasEnv.setEnvironnement(envVers);
            userHasEnv.setBrowsrPassword("");
            userHasEnv.setBrowserLogin("");

            Groupe grp = dbTools.em.find(Groupe.class, "SIMPLE_USER");
            UsersHasGroupe userHasGrp = new UsersHasGroupe();
            UsersHasGroupePK userhasgrpPk = new UsersHasGroupePK(userName, "SIMPLE_USER");
            userHasGrp.setUsersHasGroupePK(userhasgrpPk);
            userHasGrp.setUsers(newUser);
            userHasGrp.setGroupe(grp);
            userHasGrp.setDescription("");

            dbTools.StoreObjectIntoDataBase(newUser);
            dbTools.StoreObjectIntoDataBase(userHasEnv);
            dbTools.StoreObjectIntoDataBase(userHasGrp);
            dbTools.closeRessources();
        } catch (Exception ex) {
            ex.printStackTrace();
            Tools.traiterException(Tools.getStackTrace(ex));
        } finally {
            Configuration.initialisation();
        }
    }

    public static String encrypt(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(input.getBytes());
        byte byteData[] = md.digest();
        //convert the byte to hex format
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}