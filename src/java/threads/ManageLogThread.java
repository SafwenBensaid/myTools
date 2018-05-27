/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package threads;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import tools.Configuration;
import tools.StringBuilderTools;

/**
 *
 * @author 04486
 */
public class ManageLogThread extends Thread {

    @Override
    public void run() {
        try {
            if (PipeSystemeThread.logMap != null) {
                while (true) {
                    for (Map.Entry<String, StringBuilder> entry : PipeSystemeThread.logMap.entrySet()) {
                        transferLogFromMapTofile(entry.getKey());
                    }
                    try {
                        Thread.sleep(60000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ManageLogThread.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }

    private synchronized void transferLogFromMapTofile(String envName) {
        try {
            String contenu = PipeSystemeThread.logMap.get(envName).toString();
            ecrireLogFichier(envName, contenu);
            StringBuilderTools.replaceFirstOccurenceIntoStringBuilder(PipeSystemeThread.logMap.get(envName), contenu, "");
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }

    public static void afficherLogThreads(String texte) {
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String date = formatter.format(new Date());
        ecrireLogFichier("LOG_THREADS", "\n" + date + "   " + texte);
    }

    public static synchronized void ecrireLogFichier(String folder1, String texte, String... folder2) {
        if (Configuration.intitOk) {
            DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
            String date = formatter.format(new Date());

            PrintWriter ecrivain = null;
            //FileWriter fw = null;
            FileOutputStream fos = null;
            OutputStreamWriter osw = null;
            BufferedWriter bw = null;
            String pathFolder2 = "";
            if (folder2.length > 0) {
                pathFolder2 += System.getProperty("file.separator") + folder2[0];
            }
            String fileName = null;
            try {
                String folderpath = Configuration.parametresList.get("espaceLocal") + System.getProperty("file.separator") + "LOGS" + System.getProperty("file.separator") + date + pathFolder2;
                File logFlder = new File(folderpath);
                logFlder.mkdirs();

                fileName = Configuration.parametresList.get("espaceLocal") + System.getProperty("file.separator") + "LOGS" + System.getProperty("file.separator") + date + pathFolder2 + System.getProperty("file.separator") + folder1 + ".txt";
                File file = new File(fileName);
                if (!file.exists()) {
                    file.createNewFile();
                }
                fos = new FileOutputStream(file, true);
                osw = new OutputStreamWriter(fos, "UTF-8");
                bw = new BufferedWriter(osw);
                ecrivain = new PrintWriter(bw, true);
                ecrivain.print(texte);
            } catch (Exception exep) {
                tools.Tools.traiterException("FileName: " + fileName + "\n" + tools.Tools.getStackTrace(exep));
            } finally {
                try {
                    if (ecrivain != null) {
                        ecrivain.close();
                    }
                    if (bw != null) {
                        bw.close();
                    }
                    if (osw != null) {
                        osw.close();
                    }
                    if (fos != null) {
                        fos.close();
                    }
                } catch (Exception exp) {
                    exp.printStackTrace();
                }
            }
        }
    }
}