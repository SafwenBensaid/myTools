/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package t24Scripts;

import dto.EnvironnementDTO;
import java.io.InputStream;
import java.io.PrintStream;
import org.apache.commons.net.telnet.TelnetClient;
import tools.Configuration;
import tools.FtpTools;
import threads.PipeSystemeThread;
import tools.DataBaseTools;
import dataBaseTracRequests.DataBaseTracRequests;
import dto.livraison.T24;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import tools.Tools;

/**
 *
 * @author 04486
 */
public class T24Scripts {

    String connectedUser = null;
    static List<String> testExistancePack = null;

    public T24Scripts() {
        if (testExistancePack == null) {
            testExistancePack = new ArrayList<>();
        }
        try {
            connectedUser = Tools.getConnectedLogin();
        } catch (Exception ex1) {
            //ex1.printStackTrace();
        }
    }

    public T24Scripts(String loginConnectedUser) {
        if (testExistancePack == null) {
            testExistancePack = new ArrayList<>();
        }
        try {
            connectedUser = loginConnectedUser;
        } catch (Exception ex1) {
            //ex1.printStackTrace();
        }
    }

    public boolean testExistanceDossier(EnvironnementDTO env, String packName, String dossierDeBase) {
        String resultatCommande = "";
        try {
            if (testExistancePack != null && testExistancePack.contains(env.getNom() + ":" + dossierDeBase + packName)) {
                resultatCommande = "FOLDER EXISTS";
            } else {
                servlets.AfficherMessageEtatAvancement.setLogmessage("Test d'existence du dossier " + packName + " sous le dossier " + dossierDeBase + " de l'environnement " + env.getNom(), connectedUser);
                resultatCommande = executerCommandeListEnvironnement(env, dossierDeBase, "if test -d " + packName + "; then echo 'FOLDER EXISTS'; else echo 'FOLDER DOES NOT EXISTS' ;fi");
                System.out.println(resultatCommande);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (resultatCommande.contains("FOLDER EXISTS")) {
            testExistancePack.add(env.getNom() + ":" + dossierDeBase + packName);
            System.out.println("Le dossier existe");
            //servlets.AfficherMessageEtatAvancement.setLogmessage("");
            return true;
        } else {
            System.out.println("Le dossier n'existe pas");
            //servlets.AfficherMessageEtatAvancement.setLogmessage("");
            return false;
        }

    }

    public boolean testExistanceFichier(EnvironnementDTO env, String fileName, String dossierDeBase) {
        String resultatCommande = "";
        try {
            servlets.AfficherMessageEtatAvancement.setLogmessage("Test d'existence du fichier " + fileName + " sous le dossier " + dossierDeBase + " de l'environnement " + env.getNom(), connectedUser);
            resultatCommande = executerCommandeListEnvironnement(env, dossierDeBase, "if test -f " + fileName + "; then echo 'FILE EXISTS'; else echo 'FILE DOES NOT EXISTS' ;fi");
            System.out.println(resultatCommande);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (resultatCommande.contains("FILE EXISTS")) {
            System.out.println("Le fichier existe");
            //servlets.AfficherMessageEtatAvancement.setLogmessage("");
            return true;
        } else {
            System.out.println("Le fichier n'existe pas");
            //servlets.AfficherMessageEtatAvancement.setLogmessage("");
            return false;
        }

    }
    /*
     public static void main(String[] args) {
     DataBaseTools dbTools = new DataBaseTools(Configuration.ovToolsPU);

     Configuration.chargerTousLesParametres(dbTools);
     EnvironnementDTO environnementSource = new EnvironnementDTO("CRT", "biatcrt", "T24", "172.28.70.15", "t24crt", "crt$123", "BIGBANG", "BIAT1234", 21);
     T24Scripts t24Scripts = new T24Scripts();
     t24Scripts.testLoginPasswordSystemeEtBrowser(environnementSource, "sinda rahmouni", "");
     dbTools.closeRessources();
     }
     */

    public String testLoginPasswordSystemeEtBrowser(EnvironnementDTO env, String username, String... logParallele) {
        try {
            if (logParallele.length == 0) {
                servlets.AfficherMessageEtatAvancement.setLogmessage("Test d'authentification sur l'environnement " + env.getNom(), username);
            } else {
                servlets.AfficherMessageEtatAvancement.appendLogmessage("Environnement " + env.getNom() + " : Test d'authentification sur l'environnement " + env.getNom() + " ...<br>", username);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        TelnetClient telnet = null;
        T24Tools t24tools;
        InputStream in = null;
        PrintStream out = null;
        boolean problemeConnexion = false;
        String resultatCommande = "";
        telnet = new TelnetClient();
        PipeSystemeThread pipeThread = null;
        String[] patternArrayConnexionSysteme = new String[]{"-->", "Login incorrect", "Choose a new password"};
        String[] patternArrayConnexionBrowser = new String[]{"AWAITING APPLICATION", "SECURITY.VIOLATION", "LOCK SITUATION", "PASSWORD TERMINATED", "PLEASE REPEAT THE PASSWORD", "WRONG ALPHANUMERIC CHAR", "Gestion Client", "AWAITING PAGE INSTRUCTIONS"};
        String messageErreurConnexion = "TEST CONNEXION OK";
        try {
            System.out.println(env.getNom());
            try {
                telnet.connect(env.getUrl(), 23);
            } catch (Exception e) {
                messageErreurConnexion = "<b>Problème d'authentification: Environnement " + env.getNom() + ": <b>Problème de connexion à l'environnement " + env.getNom();
                e.printStackTrace();
                problemeConnexion = true;
            }
            if (problemeConnexion) {
                return messageErreurConnexion;
            }
            // Get input and output stream references
            in = telnet.getInputStream();
            out = new PrintStream(telnet.getOutputStream());
            pipeThread = new PipeSystemeThread(telnet.getInputStream(), System.out, telnet, connectedUser, env.getNom());
            pipeThread.start();
            t24tools = new T24Tools(in, out, pipeThread);
            pipeThread.streamContentAux = "";
            // Log the user on
            t24tools.readUntil("login: ");
            t24tools.write(env.getEnvUserName());
            t24tools.readUntil("Password: ");
            t24tools.write(env.getEnvPassword() + "\n\n");
            String resultatConnexionSysteme = t24tools.readUntil(patternArrayConnexionSysteme);
            String resultatConnexionBrowser = null;
            if (resultatConnexionSysteme.equals("-->")) {
                t24tools.write("ETS");
                t24tools.readUntil("-->");
                t24tools.write("EX");
                t24tools.readUntil("PLEASE ENTER YOUR SIGN ON NAME");
                t24tools.write(env.getBrowserUser());
                t24tools.readUntil("PLEASE ENTER YOUR PASSWORD");
                t24tools.write(env.getBrowserPassword());
                resultatConnexionBrowser = t24tools.readUntil(patternArrayConnexionBrowser);
                if (resultatConnexionBrowser.equals("AWAITING APPLICATION")) {
                    t24tools.write("PM I\n");
                    resultatConnexionBrowser = t24tools.readUntil("INVALID FUNCTION FOR END.OF.DAY", "AWAITING ID", "Sorry, but your session is no longer active");
                    if (resultatConnexionBrowser.equals("INVALID FUNCTION FOR END.OF.DAY")) {
                        messageErreurConnexion = "<b>Problème d'authentification: Environnement " + env.getNom() + " </b><br/>Un Cob est en cours d'exécution";
                    } else if (resultatConnexionBrowser.equals("Sorry, but your session is no longer active")) {
                        messageErreurConnexion = "<b>Problème d'authentification: Environnement " + env.getNom() + " </b><br/>Sorry, but your session is no longer active";
                    }
                    t24tools.write((char) 21 + "\n");
                    // ????
                    t24tools.write((char) 21 + "\n" + (char) 21 + "\nLO\n");
                    t24tools.readUntil("-->");
                } else if (resultatConnexionBrowser.equals("PLEASE REPEAT THE PASSWORD")) {
                    t24tools.write(env.getBrowserPassword());
                    t24tools.readUntil("AWAITING APPLICATION");
                } else if (resultatConnexionBrowser.equals("SECURITY.VIOLATION")) {
                    messageErreurConnexion = "<b>Problème d'authentification: Environnement " + env.getNom() + " </b><br/>Le non d'utilisateur ou/et le mot de passe <b>BROWSER</b> est/sont incorrect(s)";
                    tools.Tools.traiterException("<b>Problème d'authentification: Environnement " + env.getNom() + " </b><br/>Le non d'utilisateur ou/et le mot de passe <b>BROWSER</b> est/sont incorrect(s):[" + env.getBrowserUser() + ":" + env.getBrowserPassword() + "]    USER:" + username);
                } else if (resultatConnexionBrowser.equals("AWAITING PAGE INSTRUCTIONS") || resultatConnexionBrowser.equals("Gestion Client")) {
                    messageErreurConnexion = "<b>Problème d'authentification: Environnement " + env.getNom() + " </b><br/>L'utilisateur T24 <b>ne doit pas avoir un menu</b>";
                    tools.Tools.traiterException("<b>Problème d'authentification: Environnement " + env.getNom() + " </b><br/>L'utilisateur T24 <b>ne doit pas avoir un menu:[" + env.getBrowserUser() + ":" + env.getBrowserPassword() + "]    USER:" + username);
                } else if (resultatConnexionBrowser.equals("LOCK SITUATION")) {
                    messageErreurConnexion = "<b>Problème d'authentification: Environnement " + env.getNom() + " </b><br/>Votre utilisateur T24 est en <b>LOCK SITUATION</b>";
                } else if (resultatConnexionBrowser.equals("WRONG ALPHANUMERIC CHAR")) {
                    messageErreurConnexion = "<b>Problème d'authentification: Environnement " + env.getNom() + " </b><br/>Votre password T24 <b>contient un caractère spécial</b>";
                } else if (resultatConnexionBrowser.equals("PASSWORD TERMINATED")) {
                    messageErreurConnexion = "<b>Problème d'authentification: Environnement " + env.getNom() + " </b><br/>Votre mot de passe T24 a <b>expiré</b>";
                    t24tools.write(env.getBrowserPassword() + "A");
                    t24tools.readUntil("PLEASE REPEAT THE PASSWORD");
                    t24tools.write(env.getBrowserPassword() + "A");
                    t24tools.write("LO\n");
                    //mise à jour password db
                    DataBaseTools dbTools = new DataBaseTools(Configuration.puOvTools);
                    DataBaseTracRequests.updateUserPassword(env.getNom(), username, env.getBrowserPassword() + "A", dbTools);
                    Configuration.chargerTousLesEnvironnements(dbTools);
                    dbTools.closeRessources();
                    /*
                     t24tools.write("LO\n");
                     t24tools.readUntil("SECURITY.VIOLATION");
                     resetPassword(t24tools, env);
                     */
                    //testLoginPasswordSystemeEtBrowser(EnvironnementDTO env, String username, String... logParallele) {
                    messageErreurConnexion = testLoginPasswordSystemeEtBrowser(env, username, logParallele);
                }
            } else {
                messageErreurConnexion = "<b>Problème d'authentification: Environnement " + env.getNom() + " </b><br/>Le non d'utilisateur ou/et le mot de passe <b>SYSTEME</b> est/sont incorrect(s)";
            }
            if (pipeThread.streamContentAux.contains("Cannot find proc 'loginproc' in file")) {
                messageErreurConnexion = "<b>Problème de connexion: Environnement " + env.getNom() + " </b><br/>jpqn : Cannot find proc 'loginproc' in file '/APP/" + env.getAbreviationNom() + "/bnk/bnk.run/VOC' at line 1 in /APP/" + env.getAbreviationNom() + "/bnk/bnk.run/VOC, loginproc";
            }
            if (pipeThread.streamContentAux.contains("No utmpx entry. You must exec")) {
                messageErreurConnexion = "<b>Problème de connexion: Environnement " + env.getNom() + " </b><br/>No utmpx entry. You must exec \"login\" from the lowest level \"shell\".";
            }
            System.out.println(messageErreurConnexion);
            t24tools.write("exit");
            try {
                in.close();
                out.close();
            } catch (Exception exp) {
                System.err.println(exp.getMessage());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            if (!problemeConnexion) {
                pipeThread.stopThread(2);
                //pipeThread.stop();
                pipeThread.interrupt();
            }
        }
        //servlets.AfficherMessageEtatAvancement.setLogmessage("");
        return messageErreurConnexion;
    }

    private void resetPassword(T24Tools t24tools, EnvironnementDTO env) throws InterruptedException {
        t24tools.write("Y\n");
        t24tools.readUntil("PLEASE ENTER YOUR SIGN ON NAME");
        t24tools.write("LO\n");
        t24tools.readUntil("-->");
        t24tools.pipeThread.streamContentAux = "";
        t24tools.write("LIST F.USER ONLY WITH @ID NE BACKUP.USER AND SIGN.ON.NAME EQ " + env.getBrowserUser());
        t24tools.readUntil("Records Listed");
        Thread.sleep(500);
        String userId = t24tools.pipeThread.streamContentAux;
        userId = traiterString(userId);
        t24tools.write("JED F.USER " + userId);
        t24tools.readUntil("Command");
        t24tools.write((char) 27 + "33\n" + (char) 23 + (char) 27 + "fi\n");
        t24tools.readUntil("-->");

    }

    public String traiterString(String ch) {
        String aux = "";
        try {
            aux = ch.split("@ID.............")[1].split("1 Records Listed")[0].trim();
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(ch + " \n\n\n" + tools.Tools.getStackTrace(exep));
        }
        return aux;
    }

    public String deployerPack(String connectedUser, EnvironnementDTO envirTestDep, String scriptDeploiement, String packName, String mnemonic, int nbrIterationDeploiement, int... iterations) {
        boolean sessionExpiree = false;
        //Reset restore Map:
        Configuration.resetDeploiementMap(connectedUser);
        servlets.AfficherMessageEtatAvancement.appendLogmessage("Environnement " + envirTestDep.getNom() + " : Déploiement du pack " + packName + " avec la company " + mnemonic + " ...<br>", connectedUser);
        //Supprimer l'objet PackMan
        //deletePackManRecord(envirTestDep,packName);
        //end
        String commande = scriptDeploiement + " " + packName + " " + mnemonic + " " + envirTestDep.getBrowserUser() + " " + envirTestDep.getBrowserPassword();
        String resultatDeploiement = "";
        try {
            for (int i = 0; i < nbrIterationDeploiement; i++) {
                if (nbrIterationDeploiement > 1) {
                    if (i == 1) {
                        resultatDeploiement += "<br><br>";
                    }
                    resultatDeploiement += "<center><b>****  Itération: " + (i + 1) + "  ****</b></center><br>";
                }

                if (sessionExpiree == false) {
                    resultatDeploiement += executerCommandeListEnvironnementAvecRegex(connectedUser, envirTestDep, "/DEPT24", commande, "#_#_#_# DEBUT RESULTAT #_#_#_#", null, "#_#_#_# FIN RESULTAT #_#_#_#", "Sorry, but your session is no longer active", "SECURITY.VIOLATION", "Login incorrect", "Choose a new password");
                    if (resultatDeploiement.contains("Sorry, but your session is no longer active")) {
                        try {
                            //sleep 20 sec
                            Thread.sleep(20 * 1000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(T24Scripts.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        if (iterations.length == 0) {
                            return deployerPack(connectedUser, envirTestDep, scriptDeploiement, packName, mnemonic, nbrIterationDeploiement, 0);
                        } else if (iterations[0] < 10) {
                            return deployerPack(connectedUser, envirTestDep, scriptDeploiement, packName, mnemonic, nbrIterationDeploiement, (iterations[0] + 1));
                        } else {
                            resultatDeploiement = "<b>Désolé, votre session T24 a expiré [T24: Sorry, but your session is no longer active]</b>";
                            sessionExpiree = true;
                        }
                    } else if (resultatDeploiement.contains("SECURITY.VIOLATION")) {
                        resultatDeploiement = "<b>SECURITY.VIOLATION</b>";
                        sessionExpiree = true;
                    } else if (resultatDeploiement.contains("Login incorrect")) {
                        resultatDeploiement = "<b>Votre login ou mot de passe système sont incorrectes [Login incorrect]</b>";
                        sessionExpiree = true;
                    }
                } else {
                    resultatDeploiement = "<b>Désolé, votre session T24 a expiré [T24: Sorry, but your session is no longer active]</b>";
                    sessionExpiree = true;
                }

            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return resultatDeploiement;
    }

    public static synchronized String versionnerPackHotFix(String scriptVersionning, T24 livT24, String adminUser, Integer numeroLivraison) {
        servlets.AfficherMessageEtatAvancement.setLogmessage("Versionning du pack " + livT24.getPackName(), adminUser);
        FtpTools ftptools = new FtpTools();
        TelnetClient telnet = new TelnetClient();
        T24Tools t24tools;

        EnvironnementDTO envirVersionning = Configuration.environnementDTOMapUserHasEnvironnement.get(adminUser).get("VERSIONNING");
        PipeSystemeThread pipeThread = null;
        String resultatVersionning = "";
        String resultatVersionningAux;
        InputStream in = null;
        PrintStream out = null;

        try {
            try {
                telnet.connect(envirVersionning.getUrl(), 23);
            } catch (Exception e) {
                Thread.sleep(500);
                telnet.connect(envirVersionning.getUrl(), 23);
            }
            // Get input and output stream references
            in = telnet.getInputStream();
            out = new PrintStream(telnet.getOutputStream());
            pipeThread = new PipeSystemeThread(telnet.getInputStream(), System.out, telnet, adminUser, "VERSIONNING");
            pipeThread.start();
            t24tools = new T24Tools(in, out, pipeThread);

            // Log the user on
            t24tools.readUntil("login: ");
            t24tools.write(envirVersionning.getEnvUserName());
            t24tools.readUntil("Password: ");
            t24tools.write(envirVersionning.getEnvPassword());
            t24tools.readUntil("#");
            t24tools.write("bash");
            t24tools.readUntil("#");

            pipeThread.streamContentAux = "";
            System.out.println(scriptVersionning + " TAF-" + livT24.getPackName() + " " + numeroLivraison);
            t24tools.write(scriptVersionning + " TAF-" + livT24.getPackName() + " " + numeroLivraison);
            t24tools.readUntil("Pour confirmer ce versionnig taper Y entree");
            t24tools.write("Y");
            t24tools.readUntil("END.VERSIONNING.PROCESS");

            resultatVersionning = pipeThread.streamContentAux;

            System.out.println(resultatVersionning);
            t24tools.write("exit");
            try {
                in.close();
                out.close();
            } catch (Exception exp) {
                System.err.println(exp.getMessage());
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        } finally {
            pipeThread.stopThread(3);
            //pipeThread.stop();
            pipeThread.interrupt();
        }
        return resultatVersionning;
    }

    public static synchronized String[] versionnerPack(String scriptVersionning, String numLivraison, String niveauprojetAbreviation, String packName, String connectedUser) {
        servlets.AfficherMessageEtatAvancement.setLogmessage("Versionning du pack " + packName, connectedUser);
        FtpTools ftptools = new FtpTools();
        TelnetClient telnet = new TelnetClient();
        T24Tools t24tools;

        EnvironnementDTO envirVersionning = Configuration.environnementDTOMapUserHasEnvironnement.get(connectedUser).get("VERSIONNING");
        PipeSystemeThread pipeThread = null;
        String resultatVersionning = "";
        String resultatVersionningAux;
        String revision = null;
        InputStream in = null;
        PrintStream out = null;
        String[] resultatVersionningAuxRevision = new String[2];

        try {
            try {
                telnet.connect(envirVersionning.getUrl(), 23);
            } catch (Exception e) {
                Thread.sleep(500);
                telnet.connect(envirVersionning.getUrl(), 23);
            }
            // Get input and output stream references
            in = telnet.getInputStream();
            out = new PrintStream(telnet.getOutputStream());
            pipeThread = new PipeSystemeThread(telnet.getInputStream(), System.out, telnet, connectedUser, "VERSIONNING");
            pipeThread.start();
            t24tools = new T24Tools(in, out, pipeThread);

            // Log the user on
            t24tools.readUntil("login: ");
            t24tools.write(envirVersionning.getEnvUserName());
            t24tools.readUntil("Password: ");
            t24tools.write(envirVersionning.getEnvPassword());
            t24tools.readUntil("#");
            t24tools.write("bash");
            t24tools.readUntil("#");

            pipeThread.streamContentAux = "";
            System.out.println(scriptVersionning + " TAF-" + packName + " " + numLivraison + " " + niveauprojetAbreviation);
            t24tools.write(scriptVersionning + " TAF-" + packName + " " + numLivraison + " " + niveauprojetAbreviation);
            t24tools.readUntil("Pour confirmer ce versionnig taper Y entree");
            t24tools.write("Y");
            t24tools.readUntil("END.VERSIONNING.PROCESS");
            //bash-3.0 -> bash-3.2
            /*
             if (detailsLivraison.getPhase().equals("CERTIFICATION")) {
             t24tools.write("SVNVERS_CRT TAF-" + detailsLivraison.getPackName() + " " + detailsLivraison.getNumTicket() + " " + niveauprojet.getAbreviation());
             t24tools.readUntil("Pour confirmer ce versionnig taper Y entree");
             t24tools.write("Y");
             t24tools.readUntil("bash-3.00#");
             }
             */
            resultatVersionning = pipeThread.streamContentAux;
            resultatVersionningAux = new String(resultatVersionning);
            String[] resultatVersionningTab;
            String resultatVersionningSauvegarde = new String(resultatVersionning);
            if (resultatVersionning.contains("Committed revision")) {
                int positionCommitedRevision = resultatVersionning.lastIndexOf("Committed revision");
                resultatVersionning = resultatVersionning.substring(positionCommitedRevision, resultatVersionning.length() - 1);
                revision = resultatVersionning.replace(".", "");
                revision = revision.split("\n")[0];
                revision = revision.split(" ")[2];

                resultatVersionningTab = resultatVersionningAux.split("\n");
                System.out.println("******resultatVersionningAux*****");
                System.out.println(resultatVersionningAux);
                System.out.println("**********************************");
                resultatVersionning = "";
                for (int i = 0; i < resultatVersionningTab.length; i++) {
                    if (resultatVersionningTab[i].trim().startsWith("At revision") || resultatVersionningTab[i].trim().startsWith("Committed revision") || resultatVersionningTab[i].trim().startsWith("VERSIONNING REUSSI")) {
                        resultatVersionning += resultatVersionningTab[i].trim() + "\n";
                    }
                }
                //if (detailsLivraison.getCircuit().equals("PROJET")) {
                //même traitement pour C.Release et C.Projet
                if (resultatVersionningSauvegarde.indexOf("Committed revision") == resultatVersionningSauvegarde.lastIndexOf("Committed revision")) {
                    resultatVersionning += "\n\n<b>Attention: Le pack a été versionné sur une seule branche!!!</b>";
                }

                revision = revision.replace("\n", "");
                revision = revision.trim();
                System.out.println("$$$" + revision + "$$$");
                try {
                    int rev = Integer.parseInt(revision) - 1;
                    revision = String.valueOf(rev);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }

                //}
            } else {
                resultatVersionningTab = resultatVersionningAux.split("\n");
                System.out.println("******resultatVersionningAux*****");
                System.out.println(resultatVersionningAux);
                System.out.println("**********************************");
                resultatVersionning = "";
                for (int i = 0; i < resultatVersionningTab.length; i++) {
                    if (resultatVersionningTab[i].trim().startsWith("At revision") || resultatVersionningTab[i].trim().startsWith("Committed revision") || resultatVersionningTab[i].trim().startsWith("VERSIONNING REUSSI")) {
                        resultatVersionning += resultatVersionningTab[i].trim() + "\n";
                    }
                }
                resultatVersionning += "\n\n<b>Attention: Le pack n'a généré aucune révision!!!</b>";
                revision = "AUCUNE";
            }
            System.out.println(resultatVersionning);
            t24tools.write("exit");

            resultatVersionningAuxRevision[0] = resultatVersionning;
            resultatVersionningAuxRevision[1] = revision;
            try {
                in.close();
                out.close();
            } catch (Exception exp) {
                System.err.println(exp.getMessage());
            }

        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        } finally {
            pipeThread.stopThread(4);
            //pipeThread.stop();
            pipeThread.interrupt();
        }

        //servlets.AfficherMessageEtatAvancement.setLogmessage("");
        return resultatVersionningAuxRevision;
    }

    public String executerCommandeListEnvironnementAvecRegex(String connectedUser, EnvironnementDTO env, String dossier, String commande, String startRegex, String environnementCibleDeploiement, String... endRegex) {

        //pour le déploiement // en masse, le script est exécuté sur l'env versionning, donc la variable environnementCibleDeploiement va contenir le nom de l'environnement sur le quel on va déployer

        TelnetClient telnet = null;
        T24Tools t24tools;
        InputStream in = null;
        PrintStream out = null;
        String resultatCommande = "";
        telnet = new TelnetClient();
        PipeSystemeThread pipeThread = null;

        String[] patternArrayConnexionSysteme = new String[]{"#", "-->", "Login incorrect", "Choose a new password"};
        boolean problemeConnexion = false;
        String messageErreurConnexion = "TEST CONNEXION OK";
        try {
            System.out.println(env.getNom());
            try {
                telnet.connect(env.getUrl(), 23);
            } catch (Exception e) {
                messageErreurConnexion = "<b>Problème de connexion à l'environnement " + env.getNom();
                e.printStackTrace();
                problemeConnexion = true;
            }
            if (problemeConnexion) {
                return messageErreurConnexion;
            }
            // Get input and output stream references
            in = telnet.getInputStream();
            out = new PrintStream(telnet.getOutputStream());
            if (environnementCibleDeploiement != null) {
                pipeThread = new PipeSystemeThread(telnet.getInputStream(), System.out, telnet, connectedUser, env.getNom(), environnementCibleDeploiement);
            } else {
                pipeThread = new PipeSystemeThread(telnet.getInputStream(), System.out, telnet, connectedUser, env.getNom());
            }
            pipeThread.start();
            t24tools = new T24Tools(in, out, pipeThread);

            // Log the user on
            t24tools.readUntil("login: ");
            t24tools.write(env.getEnvUserName());
            t24tools.readUntil("Password: ");
            t24tools.write(env.getEnvPassword() + "\n\n");
            String resultatConnexionSysteme = t24tools.readUntil(patternArrayConnexionSysteme);
            if (resultatConnexionSysteme.equals("-->") || resultatConnexionSysteme.equals("#")) {
                t24tools.write("bash\n");
                t24tools.readUntil("#");
                if (!dossier.equals("")) {
                    t24tools.write("cd " + dossier);
                }
                t24tools.readUntil("#");
                pipeThread.streamContentAux = "";


                t24tools.write(commande);
                t24tools.readUntil(endRegex);


                resultatCommande = pipeThread.streamContentAux;
                if (resultatCommande.contains("Sorry, but your session is no longer active")) {
                    return "Désolé, votre session T24 a expiré (Sorry, but your session is no longer active)";
                }
                if (resultatCommande.contains("Login incorrect") || resultatCommande.contains("Choose a new password")) {
                    return "Login incorrect";
                }
                if (resultatCommande.contains("SECURITY.VIOLATION")) {
                    return "SECURITY.VIOLATION";
                }
                int firstRegexindex = resultatCommande.indexOf(startRegex);
                resultatCommande = resultatCommande.substring(firstRegexindex + startRegex.length());


                int lastRegexindex = resultatCommande.indexOf(endRegex[0]);
                resultatCommande = resultatCommande.substring(0, lastRegexindex);
                /*
                 resultatCommande = resultatCommande.replace("END.PROCESS", "");
                 resultatCommande = resultatCommande.replace("END.PROCESS.END", "");
                 resultatCommande = resultatCommande.replace("bash-3.00#", "");
                 */
                resultatCommande = resultatCommande.trim();
                t24tools.write("exit");
            } else {
                messageErreurConnexion = "<b>Problème d'authentification: Login incorrect: Environnement " + env.getNom() + " </b><br/>Le non d'utilisateur ou/et le mot de passe <b>SYSTEME</b> est/sont incorrect(s)";
            }



            try {
                in.close();
                out.close();
            } catch (Exception exp) {
                System.err.println(exp.getMessage());
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        } finally {
            pipeThread.stopThread(5);
            //pipeThread.stop();
            pipeThread.interrupt();
        }

        return resultatCommande;
    }

    public void deletePackManRecord(EnvironnementDTO env, String packName) {
        TelnetClient telnet = new TelnetClient();
        T24Tools t24tools;
        InputStream in = null;
        PrintStream out = null;

        PipeSystemeThread pipeThread = null;
        try {
            try {
                telnet.connect(env.getUrl(), 23);
            } catch (Exception e) {
                Thread.sleep(500);
                telnet.connect(env.getUrl(), 23);
            }
            // Get input and output stream references
            in = telnet.getInputStream();
            out = new PrintStream(telnet.getOutputStream());
            pipeThread = new PipeSystemeThread(telnet.getInputStream(), System.out, telnet, connectedUser, env.getNom());
            pipeThread.start();
            t24tools = new T24Tools(in, out, pipeThread);

            // Log the user on
            t24tools.readUntil("login: ");
            t24tools.write(env.getEnvUserName());
            t24tools.readUntil("Password: ");
            t24tools.write(env.getEnvPassword() + "\n\n");
            t24tools.readUntil("-->");
            //Suppression du record PM
            pipeThread.streamContentAux = "";
            t24tools.write("SELECT FBNK.T24.W.PACK.MAN WITH @ID EQ TAF-" + packName);
            t24tools.readUntil(">");
            String resultatSuppression = pipeThread.streamContentAux;
            if (resultatSuppression.contains("No Records selected")) {
                t24tools.write("CLEARSELECT");
            } else {
                t24tools.write("DELETE FBNK.T24.W.PACK.MAN");
            }
            t24tools.readUntil("-->");
            t24tools.write("exit");
            //fin Suppression du record PM
            try {
                in.close();
                out.close();
            } catch (Exception exp) {
                System.err.println(exp.getMessage());
            }

        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        } finally {
            pipeThread.stopThread(6);
            //pipeThread.stop();
            pipeThread.interrupt();
        }
    }

    public String executerCommandeListEnvironnement(EnvironnementDTO env, String dossier, String... commandeList) {
        TelnetClient telnet = null;
        T24Tools t24tools;
        InputStream in = null;
        PrintStream out = null;
        String resultatCommande = "";
        telnet = new TelnetClient();
        PipeSystemeThread pipeThread = null;

        try {
            try {
                System.out.println("connection telnet serveur : " + env.getUrl() + ": port 23");
                telnet.connect(env.getUrl(), 23);
            } catch (Exception e) {
                Thread.sleep(500);
                telnet.connect(env.getUrl(), 23);
            }
            // Get input and output stream references
            in = telnet.getInputStream();
            out = new PrintStream(telnet.getOutputStream());
            pipeThread = new PipeSystemeThread(telnet.getInputStream(), System.out, telnet, connectedUser, env.getNom());
            pipeThread.start();
            t24tools = new T24Tools(in, out, pipeThread);

            // Log the user on
            t24tools.readUntil("login: ");
            t24tools.write(env.getEnvUserName());
            t24tools.readUntil("Password: ");
            t24tools.write(env.getEnvPassword() + "\n\n");
            if (env.getNom().equals("VERSIONNING")) {
                t24tools.readUntil("#");
            } else {
                t24tools.readUntil("-->");
            }
            t24tools.write("bash\n");
            t24tools.readUntil("#");
            if (!dossier.equals("")) {
                t24tools.write("cd " + dossier);
            }
            t24tools.readUntil("#");
            if (!pipeThread.streamContentAux.contains("No such file or directory")) {
                pipeThread.streamContentAux = "";


                for (String commande : commandeList) {
                    t24tools.write(commande + ";echo END.PROCESS");
                    t24tools.readUntil("END.PROCESS");
                }
                for (int i = 0; i < 2; i++) {
                    t24tools.write("echo END.PROCESS.END");
                    t24tools.readUntil("END.PROCESS.END");
                }
                resultatCommande = pipeThread.streamContentAux;

                int firstIndexEndProcess = resultatCommande.indexOf("END.PROCESS.END");
                resultatCommande = resultatCommande.substring(0, firstIndexEndProcess);
                resultatCommande = resultatCommande.replace("END.PROCESS", "");
                resultatCommande = resultatCommande.replace("END.PROCESS.END", "");
                resultatCommande = resultatCommande.replace("bash-3.00#", "");
                resultatCommande = resultatCommande.replace("bash-3.2#", "");
                resultatCommande = resultatCommande.trim();
            }
            t24tools.write("exit");
            try {
                in.close();
                out.close();
            } catch (Exception e) {
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        } finally {
            pipeThread.stopThread(7);
            //pipeThread.stop();
            pipeThread.interrupt();
        }
        //resultatCommande = resultatCommande.replace("END.PROCESS", "");
        //resultatCommande = resultatCommande.replace("bash-3.00#", "");

        return resultatCommande;
    }

    public String executerCommandeEnvironnementJSH(EnvironnementDTO env, String commande, String expectedPattern) {
        TelnetClient telnet = null;
        T24Tools t24tools;
        InputStream in = null;
        PrintStream out = null;
        String resultatCommande = "";
        telnet = new TelnetClient();
        PipeSystemeThread pipeThread = null;

        try {
            try {
                System.out.println("connection telnet serveur : " + env.getUrl() + ": port 23");
                telnet.connect(env.getUrl(), 23);
            } catch (Exception e) {
                Thread.sleep(500);
                telnet.connect(env.getUrl(), 23);
            }
            // Get input and output stream references
            in = telnet.getInputStream();
            out = new PrintStream(telnet.getOutputStream());
            pipeThread = new PipeSystemeThread(telnet.getInputStream(), System.out, telnet, connectedUser, env.getNom());
            pipeThread.start();
            t24tools = new T24Tools(in, out, pipeThread);
            pipeThread.streamContentAux = "";
            String msgErreur = "jpqn : Cannot find proc 'loginproc' in file";
            // Log the user on
            t24tools.readUntil("login: ");
            t24tools.write(env.getEnvUserName());
            t24tools.readUntil("Password: ");
            t24tools.write(env.getEnvPassword() + "\n\n");
            t24tools.readUntil("-->", msgErreur);
            resultatCommande = pipeThread.streamContentAux;
            if (resultatCommande.contains(msgErreur)) {
                resultatCommande = msgErreur;
                return resultatCommande;
            }
            pipeThread.streamContentAux = "";
            pipeThread.streamContent = "";
            t24tools.write(commande);
            if (!expectedPattern.equals("")) {
                t24tools.readUntil(expectedPattern);
            } else {
                t24tools.readUntil("-->");
            }
            //t24tools.readUntil("-->");
            resultatCommande = pipeThread.streamContentAux;
            resultatCommande = resultatCommande.replace("END.PROCESS", "");

            t24tools.write("exit");

            try {
                in.close();
                out.close();
            } catch (Exception exp) {
                System.err.println(exp.getMessage());
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        } finally {
            pipeThread.stopThread(8);
            //pipeThread.stop();
            pipeThread.interrupt();
        }

        return resultatCommande;
    }

    public String executerCommandeEnvironnementT24(EnvironnementDTO env, String commande, String expectedPattern) {
        TelnetClient telnet = null;
        T24Tools t24tools;
        InputStream in = null;
        PrintStream out = null;
        boolean problemeConnexion = false;
        telnet = new TelnetClient();
        PipeSystemeThread pipeThread = null;
        String[] patternArrayConnexionSysteme = new String[]{"-->", "Login incorrect", "Choose a new password"};
        String[] patternArrayConnexionBrowser = new String[]{"AWAITING APPLICATION", "SECURITY.VIOLATION", "LOCK SITUATION", "PASSWORD TERMINATED", "PLEASE REPEAT THE PASSWORD", "WRONG ALPHANUMERIC CHAR"};
        String resultatCommande = "";
        try {
            System.out.println(env.getNom());
            try {
                telnet.connect(env.getUrl(), 23);
            } catch (Exception e) {
                resultatCommande = "<b>Problème d'authentification: Environnement " + env.getNom() + ": <b>Problème de connexion à l'environnement " + env.getNom();
                e.printStackTrace();
                problemeConnexion = true;
            }
            if (problemeConnexion) {
                return resultatCommande;
            }
            // Get input and output stream references
            in = telnet.getInputStream();
            out = new PrintStream(telnet.getOutputStream());
            pipeThread = new PipeSystemeThread(telnet.getInputStream(), System.out, telnet, connectedUser, env.getNom());
            pipeThread.start();
            t24tools = new T24Tools(in, out, pipeThread);
            pipeThread.streamContentAux = "";
            // Log the user on
            t24tools.readUntil("login: ");
            t24tools.write(env.getEnvUserName());
            t24tools.readUntil("Password: ");
            t24tools.write(env.getEnvPassword() + "\n\n");
            String resultatConnexionSysteme = t24tools.readUntil(patternArrayConnexionSysteme);
            String resultatConnexionBrowser = null;
            if (resultatConnexionSysteme.equals("-->")) {
                t24tools.write("ETS");
                t24tools.readUntil("-->");
                t24tools.write("EX");
                t24tools.readUntil("PLEASE ENTER YOUR SIGN ON NAME");
                t24tools.write(env.getBrowserUser());
                t24tools.readUntil("PLEASE ENTER YOUR PASSWORD");
                t24tools.write(env.getBrowserPassword());
                resultatConnexionBrowser = t24tools.readUntil(patternArrayConnexionBrowser);
                if (resultatConnexionBrowser.equals("AWAITING APPLICATION")) {
                    t24tools.write(commande + "\n");
                    resultatConnexionBrowser = t24tools.readUntil("APPLICATION MISSING", "AWAITING APPLICATION");
                    if (resultatConnexionBrowser.equals("APPLICATION MISSING")) {
                        resultatCommande = "<b>Environnement " + env.getNom() + "</b>: Problème APPLICATION MISSING: Commande invalide<br/>";
                    } else if (resultatConnexionBrowser.equals("AWAITING APPLICATION")) {
                        resultatCommande = "<b>Environnement " + env.getNom() + "</b>: La commande a ete executee avec succes<br/>";
                    }
                    t24tools.write((char) 21 + "\n");
                    t24tools.write((char) 21 + "\n" + (char) 21 + "\nLO\n");
                    t24tools.readUntil("-->");
                } else if (resultatConnexionBrowser.equals("PLEASE REPEAT THE PASSWORD")) {
                    t24tools.write(env.getBrowserPassword());
                    t24tools.readUntil("AWAITING APPLICATION");
                } else if (resultatConnexionBrowser.equals("SECURITY.VIOLATION")) {
                    resultatCommande = "<b>Problème d'authentification: Environnement " + env.getNom() + " </b><br/>Le non d'utilisateur ou/et le mot de passe <b>BROWSER</b> est/sont incorrect(s)";
                    tools.Tools.traiterException("<b>Problème d'authentification: Environnement " + env.getNom() + " </b><br/>Le non d'utilisateur ou/et le mot de passe <b>BROWSER</b> est/sont incorrect(s):[" + env.getBrowserUser() + ":" + env.getBrowserPassword() + "]");
                } else if (resultatConnexionBrowser.equals("LOCK SITUATION")) {
                    resultatCommande = "<b>Problème d'authentification: Environnement " + env.getNom() + " </b><br/>Votre utilisateur T24 est en <b>LOCK SITUATION</b>";
                } else if (resultatConnexionBrowser.equals("WRONG ALPHANUMERIC CHAR")) {
                    resultatCommande = "<b>Problème d'authentification: Environnement " + env.getNom() + " </b><br/>Votre password T24 <b>contient un caractère spécial</b>";
                } else if (resultatConnexionBrowser.equals("PASSWORD TERMINATED")) {
                    resultatCommande = "<b>Problème d'authentification: Environnement " + env.getNom() + " </b><br/>Votre mot de passe T24 a <b>expiré</b>";
                }
            } else {
                resultatCommande = "<b>Problème d'authentification: Environnement " + env.getNom() + " </b><br/>Le non d'utilisateur ou/et le mot de passe <b>SYSTEME</b> est/sont incorrect(s)";
            }
            if (pipeThread.streamContentAux.contains("Cannot find proc 'loginproc' in file")) {
                resultatCommande = "<b>Problème de connexion: Environnement " + env.getNom() + " </b><br/>jpqn : Cannot find proc 'loginproc' in file '/APP/" + env.getAbreviationNom() + "/bnk/bnk.run/VOC' at line 1 in /APP/" + env.getAbreviationNom() + "/bnk/bnk.run/VOC, loginproc";
            }
            if (pipeThread.streamContentAux.contains("No utmpx entry. You must exec")) {
                resultatCommande = "<b>Problème de connexion: Environnement " + env.getNom() + " </b><br/>No utmpx entry. You must exec \"login\" from the lowest level \"shell\".";
            }
            System.out.println(resultatCommande);
            t24tools.write("exit");
            try {
                in.close();
                out.close();
            } catch (Exception exp) {
                System.err.println(exp.getMessage());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            if (!problemeConnexion) {
                pipeThread.stopThread(2);
                //pipeThread.stop();
                pipeThread.interrupt();
            }
        }
        //servlets.AfficherMessageEtatAvancement.setLogmessage("");
        return resultatCommande;
    }

    public String executerCommanderEBUILDsYSTEMT24(EnvironnementDTO env, String commande) {
        TelnetClient telnet = null;
        T24Tools t24tools;
        InputStream in = null;
        PrintStream out = null;
        boolean problemeConnexion = false;
        telnet = new TelnetClient();
        PipeSystemeThread pipeThread = null;
        String[] patternArrayConnexionSysteme = new String[]{"-->", "Login incorrect", "Choose a new password"};
        String[] patternArrayConnexionBrowser = new String[]{"AWAITING APPLICATION", "SECURITY.VIOLATION", "LOCK SITUATION", "PASSWORD TERMINATED", "PLEASE REPEAT THE PASSWORD", "WRONG ALPHANUMERIC CHAR"};
        String resultatCommande = "";
        try {
            System.out.println(env.getNom());
            try {
                telnet.connect(env.getUrl(), 23);
            } catch (Exception e) {
                resultatCommande = "<b>Problème d'authentification: Environnement " + env.getNom() + ": <b>Problème de connexion à l'environnement " + env.getNom();
                e.printStackTrace();
                problemeConnexion = true;
            }
            if (problemeConnexion) {
                return resultatCommande;
            }
            // Get input and output stream references
            in = telnet.getInputStream();
            out = new PrintStream(telnet.getOutputStream());
            pipeThread = new PipeSystemeThread(telnet.getInputStream(), System.out, telnet, connectedUser, env.getNom());
            pipeThread.start();
            t24tools = new T24Tools(in, out, pipeThread);
            pipeThread.streamContentAux = "";
            // Log the user on
            t24tools.readUntil("login: ");
            t24tools.write(env.getEnvUserName());
            t24tools.readUntil("Password: ");
            t24tools.write(env.getEnvPassword() + "\n\n");
            String resultatConnexionSysteme = t24tools.readUntil(patternArrayConnexionSysteme);
            String resultatConnexionBrowser = null;
            if (resultatConnexionSysteme.equals("-->")) {
                t24tools.write("ETS");
                t24tools.readUntil("-->");
                t24tools.write("EX");
                t24tools.readUntil("PLEASE ENTER YOUR SIGN ON NAME");
                t24tools.write(env.getBrowserUser());
                t24tools.readUntil("PLEASE ENTER YOUR PASSWORD");
                t24tools.write(env.getBrowserPassword());
                resultatConnexionBrowser = t24tools.readUntil(patternArrayConnexionBrowser);
                if (resultatConnexionBrowser.equals("AWAITING APPLICATION")) {
                    t24tools.write("SS, I " + commande + "\n28\nY\n" + (char) 22 + (char) 22 + "\n");
                    resultatConnexionBrowser = t24tools.readUntil("APPLICATION MISSING", "WRONG ALPHANUMERIC CHAR", "MISSING FILE.CONTROL", "AWAITING ID");
                    if (resultatConnexionBrowser.equals("APPLICATION MISSING") || resultatConnexionBrowser.equals("WRONG ALPHANUMERIC CHAR") || resultatConnexionBrowser.equals("MISSING FILE.CONTROL") || resultatConnexionBrowser.equals("INVALID FUNCTION CODE")) {
                        resultatCommande = "<b>Environnement " + env.getNom() + "</b>: " + resultatConnexionBrowser + "<br/>";
                    } else if (resultatConnexionBrowser.equals("AWAITING ID")) {
                        resultatCommande = "<b>Environnement " + env.getNom() + "</b>: L'action REBUILD SYSTEM a ete executee avec succes<br/>";
                    }
                    t24tools.write((char) 21 + "\n");
                    t24tools.write((char) 21 + "\n" + (char) 21 + "\nLO\n");
                    t24tools.readUntil("-->");
                } else if (resultatConnexionBrowser.equals("PLEASE REPEAT THE PASSWORD")) {
                    t24tools.write(env.getBrowserPassword());
                    t24tools.readUntil("AWAITING APPLICATION");
                } else if (resultatConnexionBrowser.equals("SECURITY.VIOLATION")) {
                    resultatCommande = "<b>Problème d'authentification: Environnement " + env.getNom() + " </b><br/>Le non d'utilisateur ou/et le mot de passe <b>BROWSER</b> est/sont incorrect(s)";
                    tools.Tools.traiterException("<b>Problème d'authentification: Environnement " + env.getNom() + " </b><br/>Le non d'utilisateur ou/et le mot de passe <b>BROWSER</b> est/sont incorrect(s):[" + env.getBrowserUser() + ":" + env.getBrowserPassword() + "]");
                } else if (resultatConnexionBrowser.equals("LOCK SITUATION")) {
                    resultatCommande = "<b>Problème d'authentification: Environnement " + env.getNom() + " </b><br/>Votre utilisateur T24 est en <b>LOCK SITUATION</b>";
                } else if (resultatConnexionBrowser.equals("WRONG ALPHANUMERIC CHAR")) {
                    resultatCommande = "<b>Problème d'authentification: Environnement " + env.getNom() + " </b><br/>Votre password T24 <b>contient un caractère spécial</b>";
                } else if (resultatConnexionBrowser.equals("PASSWORD TERMINATED")) {
                    resultatCommande = "<b>Problème d'authentification: Environnement " + env.getNom() + " </b><br/>Votre mot de passe T24 a <b>expiré</b>";
                }
            } else {
                resultatCommande = "<b>Problème d'authentification: Environnement " + env.getNom() + " </b><br/>Le non d'utilisateur ou/et le mot de passe <b>SYSTEME</b> est/sont incorrect(s)";
            }
            if (pipeThread.streamContentAux.contains("Cannot find proc 'loginproc' in file")) {
                resultatCommande = "<b>Problème de connexion: Environnement " + env.getNom() + " </b><br/>jpqn : Cannot find proc 'loginproc' in file '/APP/" + env.getAbreviationNom() + "/bnk/bnk.run/VOC' at line 1 in /APP/" + env.getAbreviationNom() + "/bnk/bnk.run/VOC, loginproc";
            }
            if (pipeThread.streamContentAux.contains("No utmpx entry. You must exec")) {
                resultatCommande = "<b>Problème de connexion: Environnement " + env.getNom() + " </b><br/>No utmpx entry. You must exec \"login\" from the lowest level \"shell\".";
            }
            System.out.println(resultatCommande);
            t24tools.write("exit");
            try {
                in.close();
                out.close();
            } catch (Exception exp) {
                System.err.println(exp.getMessage());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            if (!problemeConnexion) {
                pipeThread.stopThread(2);
                //pipeThread.stop();
                pipeThread.interrupt();
            }
        }
        //servlets.AfficherMessageEtatAvancement.setLogmessage("");
        return resultatCommande;
    }
}