/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package t24Scripts;

import dto.EnvironnementDTO;
import java.io.*;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.net.telnet.*;
import tools.Configuration;
import tools.FtpTools;
import threads.PipeSystemeThread;
import tools.Tools;

/**
 *
 * @author 04486
 */
public class PM {
    /*
     public static void main(String[] args){
     Configuration.initialisation();
        
     Environnement environnementDev = Configuration.environnementList.get("DEVR");
     String fileName="AAAAA.txt";
     int objNbr = 4;
        
     PM pm = new PM(environnementDev, fileName, objNbr);
     pm.PmFormerPack();
     }
     */

    EnvironnementDTO env;
    String fileName;
    String packName;
    TelnetClient telnet = null;
    T24Tools t24tools;
    InputStream in = null;
    PrintStream out = null;
    FtpTools ftptools = null;
    String mnemonicCompany = null;
    String folder = null;
    String connectedUser = null;

    public PM() {
        ftptools = new FtpTools();
    }
    /*
     public static void main(String[]args){
     Configuration.initialisation();
     Environnement env = Configuration.environnementList.get("ASS");
     PM pm = new PM(env,"LIVR.BFIT.9314.TST.txt",2);
     pm.PmFormerPack();
     }
     */

    public PM(EnvironnementDTO env, String fileName, String folder, String connectedUser) {
        this.env = env;
        this.fileName = fileName;
        this.packName = fileName.replace(".txt", "");
        ftptools = new FtpTools();
        this.folder = folder;
        this.connectedUser = connectedUser;
    }

    public PM(EnvironnementDTO env, String fileName, String mnemonicCompany, String folder, String connectedUser) {
        this.env = env;
        this.fileName = fileName;
        this.packName = fileName.replace(".txt", "");
        ftptools = new FtpTools();
        this.mnemonicCompany = mnemonicCompany;
        this.folder = folder;
        this.connectedUser = connectedUser;
    }

    public synchronized int preparerPackMan(String objectList) throws IOException {
        int objectNumber = 0;
        Tools tools = new Tools();
        FtpTools ftpTools = new FtpTools();
        objectList = tools.traiterString(objectList, connectedUser);
        //Calculer le nombre d'objets à packager
        Set<String> objectSet = new TreeSet<String>();
        String[] tab = objectList.split("\n");
        for (String ch : tab) {
            objectSet.add(ch.trim());
        }
        objectNumber = objectSet.size();
        //create dir if not exists        
        tools.createDirectoryIfNotExists(Configuration.parametresList.get("espaceLocal"));
        //create file and write objects in it
        File file = tools.createFile(Configuration.parametresList.get("espaceLocal") + "/" + fileName);
        objectList = objectList.replace(" ", "");
        objectList = objectList.replace("\t", "");
        tools.writeInFile(file, objectList);
        ftpTools.uploadFileToSavedList(file, "./&SAVEDLISTS&", env);

        return objectNumber;
    }

    public synchronized String PmFormerPack(int... iterations) {
        System.out.println("================================1===============================");
        System.out.println("PACKMAN: Formation du pack TAF-" + packName + " dans le dossier " + folder + " de l'environnement " + env.getNom());
        System.out.println("================================2===============================");
        String resultatCommande = "";
        String resultatAux = "";
        servlets.AfficherMessageEtatAvancement.setLogmessage("PACKMAN: Formation du pack TAF-" + packName + " dans le dossier " + folder + " de l'environnement " + env.getNom(), connectedUser);
        new Tools().viderLeDossierSiRempliSinonLeCreer(env, "TAF-" + packName, folder);

        System.out.println("PACK-MAN START");
        telnet = new TelnetClient();
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
            t24tools.readUntil("selected");
            String resultatSuppression = pipeThread.streamContentAux;
            if (resultatSuppression.contains("No Records selected")) {
                t24tools.write("CLEARSELECT");
            } else {
                t24tools.write("DELETE FBNK.T24.W.PACK.MAN");
            }
            t24tools.readUntil("-->");
            //fin Suppression du record PM
            t24tools.write("ETS");
            t24tools.readUntil("-->");
            t24tools.write("EX");
            t24tools.readUntil("PLEASE ENTER YOUR SIGN ON NAME");
            t24tools.write(env.getBrowserUser());
            t24tools.readUntil("PLEASE ENTER YOUR PASSWORD");
            t24tools.write(env.getBrowserPassword());
            resultatAux += t24tools.readUntil("AWAITING APPLICATION", "Sorry, but your session is no longer active", "SECURITY.VIOLATION");
            if (mnemonicCompany != null) {
                pipeThread.streamContent.replace("AWAITING APPLICATION", "");
                t24tools.write(mnemonicCompany);
                resultatAux += t24tools.readUntil("AWAITING APPLICATION", "Sorry, but your session is no longer active", "SECURITY.VIOLATION");
            }
            pipeThread.streamContentAux = "";
            t24tools.write("PM I " + packName + "\n\n" + folder + "\n" + fileName + "\n" + (char) 22 + (char) 22 + "\n");
            resultatAux += t24tools.readUntilPatterOrContinueOriginal("END PROCESSING UNIT");
            Thread.sleep(500);
            t24tools.write((char) 21 + "\n" + (char) 21 + "\nLO\n");
            resultatCommande = pipeThread.streamContentAux;
            System.out.println(resultatCommande);
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
            pipeThread.stopThread(1);
            //pipeThread.stop();
            pipeThread.interrupt();
        }
        System.out.println("PACK-MAN END");
        if (resultatAux.contains("Sorry, but your session is no longer active")) {
            try {
                //sleep 20 sec
                Thread.sleep(20 * 1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(PM.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (iterations.length == 0) {
                return PmFormerPack(0);
            } else if (iterations[0] < 10) {
                return PmFormerPack((iterations[0] + 1));
            } else {
                return "<u>Environnement " + env.getNom() + ":</u> <b>Désolé, votre session T24 a expiré [T24: Sorry, but your session is no longer active]</b>";
            }
        } else if (resultatAux.contains("SECURITY.VIOLATION")) {
            return "<u>Environnement " + env.getNom() + ":</u> <b>SECURITY.VIOLATION</b>";
        } else {
            return getNotProcessedObjects(env, packName);
        }
    }

    public synchronized String getNotProcessedObjects(EnvironnementDTO env, String packName) {
        FtpTools ftpTools = new FtpTools();
        ftpTools.downloadFile(env, "&SAVEDLISTS&", "TAF-" + packName, env.getNom(), ".log");
        String[] tab = null;
        Set<String> notFoundObjectsSet = new TreeSet<String>();
        Set<String> routinesContenantDebugSet = new TreeSet<String>();
        StringBuilder sb = new StringBuilder();
        try {
            InputStream ips = new FileInputStream(Configuration.parametresList.get("espaceLocal") + "/" + env.getNom() + "/TAF-" + packName + ".log");
            InputStreamReader ipsr = new InputStreamReader(ips);
            BufferedReader br = new BufferedReader(ipsr);
            String ligne;
            while ((ligne = br.readLine()) != null) {
                if (ligne.contains("ELEMENT NOT PROCESSED") && !ligne.contains("contient un DEBUG")) {
                    tab = ligne.split(" - ");
                    notFoundObjectsSet.add(tab[1].split(" ")[0]);
                }
                if (ligne.contains("contient un DEBUG")) {
                    tab = ligne.split(" - ");
                    routinesContenantDebugSet.add(tab[1].split(" ")[0]);
                }
            }
            //&& !ligne.contains("contient un DEBUG")

            try {
                br.close();
                ips.close();
                ipsr.close();
            } catch (Exception exp) {
                System.err.println(exp.getMessage());
            }
            if (notFoundObjectsSet.size() > 0) {
                sb.append("\n<center><b>Liste des objets inexistants sur l'environnement: ").append(env.getNom()).append("</b></center>\n");
                for (String ch : notFoundObjectsSet) {
                    sb.append("<li>").append(ch.replaceFirst("-", ">")).append("</li>");
                }
            }
            sb.append("\n");
            if (routinesContenantDebugSet.size() > 0) {
                sb.append("\n<center><b>**** Liste des routines contenats <b>\"DEBUG\"</b> sur l'environnement: ");
                sb.append(env.getNom());
                sb.append(" ****</b></center>\n");
                for (String ch : routinesContenantDebugSet) {
                    sb.append(ch.replaceFirst("-", ">"));
                    sb.append("\n");
                }
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return sb.toString().trim();
    }
}
