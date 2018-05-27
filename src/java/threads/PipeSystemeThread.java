/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package threads;

import tools.*;
import java.io.*;
import java.util.*;
import org.apache.commons.net.telnet.TelnetClient;

/**
 *
 * @author 04486
 */
public class PipeSystemeThread extends Thread {

    /**
     * Ce boolean sera utilisé pour signaler au processus s'il doit continuer ou
     * s'arrêter.
     */
    private boolean stopThread = false;
    BufferedReader is;
    PrintStream os;
    boolean log = true;
    public String streamContent = "";
    public String streamContentAux = "";
    TelnetClient telnet;
    private String connectedUser;
    private String[] tabRestores = new String[]{"RESTORE1", "RESTORE2", "RESTORE3"};
    String environnementName;
    private String environnementCibleDeploiement;
    public static Map<String, StringBuilder> logMap;

    /**
     * Construct a PipeSystemeThread to read from is and write to os
     */
    public PipeSystemeThread() {
    }

    public PipeSystemeThread(InputStream is, OutputStream os, TelnetClient telnet, boolean log, String connectedUser, String environnementName) {
        this.is = new BufferedReader(new InputStreamReader(is));
        this.os = new PrintStream(os);
        this.telnet = telnet;
        this.log = log;
        this.connectedUser = connectedUser;
        this.environnementName = environnementName;
    }

    public PipeSystemeThread(InputStream is, OutputStream os, TelnetClient telnet, String connectedUser, String environnementName) {
        this.is = new BufferedReader(new InputStreamReader(is));
        this.os = new PrintStream(os);
        this.telnet = telnet;
        this.connectedUser = connectedUser;
        this.environnementName = environnementName;
    }

    public PipeSystemeThread(InputStream is, OutputStream os, TelnetClient telnet, String connectedUser, String environnementName, String environnementCibleDeploiement) {
        this.is = new BufferedReader(new InputStreamReader(is));
        this.os = new PrintStream(os);
        this.telnet = telnet;
        this.connectedUser = connectedUser;
        this.environnementName = environnementName;
        this.environnementCibleDeploiement = environnementCibleDeploiement;
    }

    /**
     * Do the reading and writing.
     */
    public void run() {
        String streamContentAux_verif_exception = null;
        String restore_verif_exception = null;
        String connectedUser_verif_exception = null;
        /*
         long threadId = Thread.currentThread().getId();
         System.err.println("Thread id : "+threadId);
         */
        try {
            stopThread = false;
            while (!stopThread) {
                try {
                    //traitement
                    char[] buffer = new char[1];
                    String contenu = null;
                    while (is != null && is.read(buffer) > 0) {
                        contenu = new String(buffer);
                        streamContent += contenu;
                        //streamContent est utilisé pour ReadUntil et streamContentAux pour les autres besoins                        
                        streamContentAux += contenu;



                        // J'ai désactivé l'ecriture du log dans un fichier à cause de la lenteur d'exécution 

                        if (environnementCibleDeploiement != null) {
                            storLogIntoLogMap(environnementCibleDeploiement, contenu.toString());
                        } else {
                            storLogIntoLogMap(environnementName, contenu.toString());
                        }


                        for (String restore : tabRestores) {

                            streamContentAux_verif_exception = streamContentAux;
                            restore_verif_exception = restore;
                            connectedUser_verif_exception = connectedUser;

                            if (streamContentAux.contains(restore) && connectedUser != null && !connectedUser.equals("anonymousUser")) {
                                if (environnementCibleDeploiement != null) {
                                    Configuration.restoreMap.get(connectedUser).get(environnementCibleDeploiement).put(restore, streamContentAux.lastIndexOf(restore));

                                    /*
                                     System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");

                                     System.out.println(connectedUser + ":" + restore + ":" + environnementName + ":" + streamContentAux.lastIndexOf(restore));
                                     System.out.println("Restore1: PipeSystemThread Insert" + Configuration.restoreMap.get(connectedUser).get(environnementName).get("RESTORE1"));
                                     System.out.println("Restore2: PipeSystemThread Insert" + Configuration.restoreMap.get(connectedUser).get(environnementName).get("RESTORE2"));
                                     System.out.println("Restore3: PipeSystemThread Insert" + Configuration.restoreMap.get(connectedUser).get(environnementName).get("RESTORE3"));

                                     System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$\n");
                                     */

                                } else {
                                    Configuration.restoreMap.get(connectedUser).get(environnementName).put(restore, streamContentAux.lastIndexOf(restore));
                                }


                            }
                        }
                        if (log == true) {
                            if (os != null) {
                                os.print(contenu.toString());
                                os.flush();
                            }
                        }
                        buffer = new char[1];
                    }
                    //fin traitement

                    //attention, il boucle indéfiniment ici
                    Tools.showConsolLog("END THREAD PROCESSING");
                    if (stopThread == false) {
                        stopThread(9);
                    }
                } catch (Exception exep) {
                    /*
                     System.out.println("________________________");
                     System.out.println("streamContentAux_verif_exception : "+streamContentAux_verif_exception);                    
                     System.out.println("************************");
                     System.out.println("restore_verif_exception : "+restore_verif_exception);
                     System.out.println("connectedUser_verif_exception: "+connectedUser_verif_exception);
                     System.out.println("________________________");
                     exep.printStackTrace();
                     tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
                     */
                    //System.err.println("exception dans le thread -> PipeSystemeThread Fatal thread interruption during read (la var is == null entre le 2ème et 3ème test)");
                    stopThread(10);
                }

            }
            telnet.disconnect();
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }

    public synchronized void stopThread(int trace) {
        try {
            this.stopThread = true;
            streamContent = "";
            streamContentAux = "";
            System.gc();
            Tools.showConsolLog("-------------------------");
            Tools.showConsolLog("STOP THREAD: TRACE " + trace);
            Tools.showConsolLog("-------------------------");
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }

    private void storLogIntoLogMap(String envName, String texte) {
        try {
            if (logMap.containsKey(envName)) {
                logMap.get(envName).append(texte);
            } else {
                logMap.put(envName, new StringBuilder(texte));
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }
    /*
     private void transferLogFromMapTofile(String envName) {
     try {
     String contenu = logMap.get(envName).toString();
     ecrireLogFichier(envName, contenu);
     StringBuilderTools.relaceFirstOccurenceIntoStringBuilder(logMap.get(envName), contenu, "");
     } catch (Exception exep) {
     exep.printStackTrace();
     tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
     }
     }
     */
}