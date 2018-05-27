/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package t24Scripts;

import java.io.*;
import threads.PipeSystemeThread;

/**
 *
 * @author 04486
 */
public class T24Tools {

    InputStream in;
    PrintStream out;
    PipeSystemeThread pipeThread;

    public T24Tools() {
    }

    public T24Tools(InputStream in, PrintStream out, PipeSystemeThread pipeThread) {
        this.in = in;
        this.out = out;
        this.pipeThread = pipeThread;
    }

    public void write(String value) {
        try {
            out.println(value);
            out.flush();
            System.out.println(value);
            System.gc();
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }

    public String readUntil(String... expectedPatternArray) {
        boolean trouve = false;
        String patternTrouve = "";
        while (trouve == false) {
            try {
                Thread.sleep(500);

                for (String expectedPattern : expectedPatternArray) {
                    //System.out.println("__"+pipeThread.streamContent+"__");
                    if (pipeThread.streamContent.contains(expectedPattern)) {
                        trouve = true;
                        patternTrouve = expectedPattern;
                        Thread.sleep(50);
                        pipeThread.streamContent = "";
                        break;
                    }
                }
            } catch (Exception exep) {
                exep.printStackTrace();
                tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
            }
        }
        pipeThread.streamContent = "";
        //System.out.println("pattern trouvé "+patternTrouve);
        return patternTrouve;
    }

    public void readUntil(String expectedPattern) {
        while (!pipeThread.streamContent.contains(expectedPattern)) {
            try {
                //ecrire(expectedPattern);
                Thread.sleep(250);
            } catch (Exception exep) {
                exep.printStackTrace();
                tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
            }
        }
        pipeThread.streamContent = pipeThread.streamContent.replaceFirst(expectedPattern, "");
    }

    public String readUntilPatterOrContinueOriginal(String expectedPattern) {

        boolean repeat = true;
        String output = "";
        while (repeat == true) {
            try {
                Thread.sleep(250);

                output = pipeThread.streamContent;

                if (output.contains("(Y)")) {
                    write("Y\n");
                    pipeThread.streamContent = pipeThread.streamContent.replaceFirst("(Y)", "");
                }
                if (output.contains("AWAITING PAGE INSTRUCTIONS")) {
                    pipeThread.streamContent = pipeThread.streamContent.replaceFirst("AWAITING PAGE INSTRUCTIONS", "");
                    repeat = false;
                }
                if (output.contains(expectedPattern)) {
                    pipeThread.streamContent = pipeThread.streamContent.replaceFirst(expectedPattern, "");
                    repeat = false;
                }
                if (output.contains("Sorry, but your session is no longer active")) {
                    output = "Sorry, but your session is no longer active";
                    repeat = false;
                }
                if (output.contains("SECURITY.VIOLATION")) {
                    output = "SECURITY.VIOLATION";
                    repeat = false;
                }
            } catch (Exception exep) {
                exep.printStackTrace();
                tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
            }
        }
        return output;
    }
    /*
     public String verifierExistanceObjets(String nomFichier, String envName) {
     servlets.AfficherMessageEtatAvancement.setLogmessage("Vérification de l'existance des objets sur l'environnement "+envName,Tools.getConnectedLogin());
     String resultatVerifObjets = "";
     Tools tools = new Tools();
     T24Scripts t24Scripts = new T24Scripts();     
     String connectedUser=Tools.getConnectedLogin();
     EnvironnementDTO envVers = Configuration.environnementDTOMapUserHasEnvironnement.get(connectedUser).get("VERSIONNING");
     EnvironnementDTO env = Configuration.environnementDTOMapUserHasEnvironnement.get(connectedUser).get(envName);

     resultatVerifObjets = t24Scripts.executerCommandeEnvironnementJSH(env, "run BIAT.BP BIAT.VERIF.EXIST.PM.RTN " + nomFichier,"ENVIRONNEMENT: ");
     if (!resultatVerifObjets.contains("#*#*#*#*#*#*#*#*#*#*#*#*#*#*#*#*#*#*#*#*#")) {
     //transfert du pack de la routine
     FtpTools ftpTools = new FtpTools();
     ftpTools.transferFolder(envVers, env, "/work", "PACK.TAF", "TAF-PRE.PM",true);
     //Deploy de la routine
     String commandeDeploy = "PM I PRE.PM\n\nPACK.TAF\n\nRESTORE\nALL\n";
     t24Scripts.executerCommandeEnvironnementT24(env, commandeDeploy, "pendant la restauration");
     resultatVerifObjets = t24Scripts.executerCommandeEnvironnementJSH(env, "run BIAT.BP BIAT.VERIF.EXIST.PM.RTN " + nomFichier,"ENVIRONNEMENT: ");
     }
     resultatVerifObjets = resultatVerifObjets.replace("#*#*#*#*#*#*#*#*#*#*#*#*#*#*#*#*#*#*#*#*#", "____________________________________________");
     String[] resultatVerifObjetsTab = resultatVerifObjets.split("____________________________________________");
     //servlets.AfficherMessageEtatAvancement.setLogmessage("");
     return resultatVerifObjetsTab[1].trim();
     }
     */
    /*
     public  void ecrire(String text) 
     {
     PrintWriter ecri ;
     try
     {
     ecri = new PrintWriter(new FileWriter("C:\\Users\\04486\\Documents\\ANIS\\expectedPattern.txt", true));
     ecri.print(text+"\n");
     ecri.flush();
     ecri.close();
     }//try
     catch (NullPointerException a)
     {
     System.out.println("Erreur : pointeur null");
     }
     catch (IOException a)
     {
     System.out.println("Problème d'IO: "+a.getMessage());
     }
     }
     */
}
