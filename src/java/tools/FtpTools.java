/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import dataBaseTracRequests.DataBaseTracRequests;
import com.ftpClient.ClientFTP;
import dto.EnvironnementDTO;
import java.io.*;
import com.amoebacode.ftp.*;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import t24Scripts.T24Scripts;

/**
 *
 * @author 04486
 */
public class FtpTools {

    private String userName;

    public FtpTools() {
    }

    public FtpTools(String userName) {
        this.userName = userName;
    }

    public int compterNbrObjetsPack(EnvironnementDTO environnementDto, String pathPackName) {
        T24Scripts t24Scripts = new T24Scripts();
        int nbrObj = -1;
        try {
            String res = t24Scripts.executerCommandeListEnvironnement(environnementDto, pathPackName, "echo 'DEBUT_RESULTAT';echo 'find . -type f | wc -l' | bash;echo 'FIN_RESULTAT'");
            nbrObj = Integer.parseInt(res.split("DEBUT_RESULTAT")[1].split("FIN_RESULTAT")[0].trim());
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return nbrObj;
    }

    public void transferFolder(String connectedUser, EnvironnementDTO envSource, EnvironnementDTO envDestination, String serverDirSource, String serverDirDestination, String packName, boolean decompressTarFolderInLocal, String... listePacksCompressed) {
        try {
            downloadFolder(envSource, serverDirSource, packName, packName, false, decompressTarFolderInLocal, listePacksCompressed);
            uploaddCompressedFolderThenUntarIt(connectedUser, envDestination, serverDirDestination, packName, false, listePacksCompressed);
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }

    public void countAndStoreNbrOfFilesByPackName(EnvironnementDTO envSource, String packPath, String... packNameList) {
        try {
            // Enregistrer le nombre d'objet du pack
            DataBaseTracRequests dbReq = new DataBaseTracRequests();
            DataBaseTools dbTools = new DataBaseTools(Configuration.puOvTools);
            for (String packName : packNameList) {
                int nbrObjetsPack = compterNbrObjetsPack(envSource, packPath + packName);
                if (nbrObjetsPack != -1) {
                    dbReq.storeNbrOfFilesByPackName(packName, nbrObjetsPack, dbTools);
                }
            }
            dbTools.closeRessources();
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }

    public String checkNbrOfFilesInUploadedFolder(Map<String, Integer> nbrOfFilesByPack) {
        StringBuilder resultat = new StringBuilder();
        try {
            DataBaseTracRequests dbReq = new DataBaseTracRequests();
            DataBaseTools dbTools = new DataBaseTools(Configuration.puOvTools);
            int nbrStored = 0;
            for (Entry<String, Integer> entry : nbrOfFilesByPack.entrySet()) {
                nbrStored = dbReq.getNbrOfFilesByPackName(entry.getKey(), dbTools);
                if (nbrStored != entry.getValue().intValue()) {
                    resultat.append("Le transfert du pack <b>");
                    resultat.append(entry.getKey());
                    resultat.append(" a échoué: (Nombre d'objets transférés = ");
                    resultat.append(nbrStored);
                    resultat.append("  !=  Nombre d'objets reçus = ");
                    resultat.append(entry.getValue());
                    resultat.append(")<br>");
                }
            }
            dbTools.closeRessources();
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return resultat.toString();
    }

    public void downloadFolder(EnvironnementDTO envSource, String serverDirSource, String packName, String packsToBeCompressedConcatenes, boolean deleteCompressedFileFromLocal, boolean decompress, String... listePacksCompressed) {
        try {
            if (listePacksCompressed.length > 0) {
                countAndStoreNbrOfFilesByPackName(envSource, serverDirSource + "/", listePacksCompressed);
            } else {
                countAndStoreNbrOfFilesByPackName(envSource, serverDirSource + "/", packName);
            }

            // on va télécharger le .tar, le décomresser et puis recompresser le/les pack(s), comme ça les fichiers vont perdre leurs détails, date, owner...
            System.err.println("\n\nDownload Folder\n\n");
            decompress = true;
            deleteCompressedFileFromLocal = false;
            setLogmessage("Download du pack " + packName + " depuis l'environnement " + envSource.getNom());

            String espaceLocal = Configuration.parametresList.get("espaceLocal");

            FTPClient ftpClient = new FTPClient(true);
            ftpClient.openConnection(envSource.getUrl(), 21);
            ftpClient.login(envSource.getEnvUserName(), envSource.getEnvPassword());
            Tools.showConsolLog("Je suis sous:" + ftpClient.getCurrentDirectory());
            Tools.showConsolLog("Je vais accèder à:" + serverDirSource);
            ftpClient.changeDirectory(serverDirSource);
            Tools.showConsolLog(ftpClient.getCurrentDirectory());
            deleteAll(new File(espaceLocal + System.getProperty("file.separator") + packName));
            if (decompress == true && listePacksCompressed.length == 0) {
                //Si le dossier n'existe pas en local, le creer
                File dossierPackLocal = new File(espaceLocal + System.getProperty("file.separator") + packName);
                if (!dossierPackLocal.exists()) {
                    Tools.showConsolLog("je vais creer le dossier en local car il n'existe pas");
                    dossierPackLocal.mkdir();
                }
            }
            System.err.println("\n\nCompress en .tar\n\n");

            //compresser le pack en .tar
            Tools tools = new Tools();
            T24Scripts t24Scripts = new T24Scripts();
            t24Scripts.executerCommandeListEnvironnement(envSource, serverDirSource, "tar -cf " + packName + ".tar " + packsToBeCompressedConcatenes);


            System.err.println("\n\nDownload Cmpressed Pack .tar\n\n");

            //Supprimer le pack.tar s'il existe
            File comressedFile = new File(espaceLocal + System.getProperty("file.separator") + packName + ".tar");
            if (comressedFile.exists()) {
                comressedFile.delete();
            }


            //télécharger le pack .tar
            ftpClient.downloadFile(packName + ".tar", espaceLocal + System.getProperty("file.separator") + packName + ".tar", false);
            try {
                System.err.println("\n\nDelete Cmpressed Pack .tar from Server\n\n");
                //supprimer le fichier du serveur
                ftpClient.deleteFile(packName + ".tar");
            } catch (Exception ex) {
            }
            if (decompress == true) {
                // si le .tar contient plusieurs packs, les effacer de l'espace local avant de décompresser le .tar
                if (listePacksCompressed.length > 0) {
                    for (String pack : listePacksCompressed) {
                        deleteAll(new File(espaceLocal + System.getProperty("file.separator") + pack));
                    }
                }

                System.err.println("\n\ndécompresser le fichier .tar en local\n\n");

                //décompresser le fichier .tar en local
                //////tools.unTarPack(espaceLocal, packName + ".tar");
                Process proc = null;
                try {
                    String untarCommand = "cmd /c  cmd.exe /K \"cd " + espaceLocal + " && tar -xvf " + packName + ".tar\"";
                    proc = Runtime.getRuntime().exec(untarCommand);
                } catch (Exception ex) {
                    proc = Runtime.getRuntime().exec(new String[]{"bash", "-c", "cd " + espaceLocal + " && tar -xvf " + packName + ".tar"});
                }
                InputStream inputStream = proc.getInputStream();
                tools.waitReadInputStream(inputStream);
                //Tar t = new Tar();
                if (deleteCompressedFileFromLocal == true) {
                    System.err.println("\n\n//supprimer fichier .tar du local\n\n");
                    //supprimer fichier .tar du local
                    tools.deleteFile(espaceLocal + System.getProperty("file.separator") + packName + ".tar");
                    System.err.println("\n\n//recompresser les packs en un fichier .tar du local\n\n");
                    // on va recompresser le pack, comme ça les fichiers vont perdre leurs détails, date, owner...    


                    String tarCommand;
                    if (listePacksCompressed.length > 0) {
                        //////t.tar(espaceLocal + "/" + packName + ".tar", listePacksCompressed);
                        tarCommand = "cmd /c  cmd.exe /K \"cd " + espaceLocal + " && tar -cf " + packName + ".tar " + packsToBeCompressedConcatenes.trim() + "\"";
                        Tools.showConsolLog(tarCommand);
                    } else {
                        //////t.tar(espaceLocal + "/" + packName + ".tar", espaceLocal + "/" + packName);
                        tarCommand = "cmd /c  cmd.exe /K \"cd " + espaceLocal + " && tar -cf " + packName + ".tar " + packName + "\"";
                    }
                    try {
                        proc = Runtime.getRuntime().exec(tarCommand);
                    } catch (Exception ex) {
                        proc = Runtime.getRuntime().exec(new String[]{"bash", "-c", "cd " + espaceLocal + " && tar -cf " + packName + ".tar " + packName});
                    }
                    inputStream = proc.getInputStream();
                    tools.waitReadInputStream(inputStream);

                }
                try {
                    inputStream.close();
                } catch (Exception exp) {
                    System.err.println(exp.getMessage());
                }
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
            downloadFolder(envSource, serverDirSource, packName, packsToBeCompressedConcatenes, deleteCompressedFileFromLocal, decompress);
        }
        setLogmessage("");
    }

    public synchronized String uploaddCompressedFolderThenUntarIt(String connectedUser, EnvironnementDTO envDestination, String serverDirDestination, String packName, boolean logParallele, String... listePacksCompressed) {
        Map<String, Integer> nbrOfFilesByPack = new LinkedHashMap<String, Integer>();
        try {
            System.err.println("\n\nuploaddCompressedFolderThenUntarIt Folder\n\n");
            if (logParallele == true) {
                appendLogmessage(connectedUser, "Environnement " + envDestination.getNom() + " : Upload du pack " + packName + " vers l'environnement " + envDestination.getNom() + " sous le dossier " + serverDirDestination + " ...<br>");
            } else {
                setLogmessage("Upload du pack " + packName + " vers l'environnement " + envDestination.getNom() + " sous le dossier " + serverDirDestination);
            }
            String espaceLocal = Configuration.parametresList.get("espaceLocal");
            FTPClient ftpClient = new FTPClient(true);
            ftpClient.openConnection(envDestination.getUrl(), 21);
            ftpClient.login(envDestination.getEnvUserName(), envDestination.getEnvPassword());
            ftpClient.changeDirectory(serverDirDestination);
            Tools.showConsolLog(ftpClient.getCurrentDirectory());
            //if the directory already exists on the server delete it
            Tools tools = new Tools();
            T24Scripts t24Scripts = null;
            if (userName != null) {
                t24Scripts = new T24Scripts(userName);
            } else {
                t24Scripts = new T24Scripts();
            }

            try {
                //supprimer le fichier compressé du serveur s'il existe
                ftpClient.deleteFile(packName + ".tar");
            } catch (Exception ex) {
            }
            ftpClient.uploadFile("./" + packName + ".tar", espaceLocal + System.getProperty("file.separator") + packName + ".tar", false);
            //si le pack existe déjà, le supprimer, le créer et enfin décompresser le .tar dedans
            long time = System.currentTimeMillis();
            if (listePacksCompressed.length == 0) {
                // premier cas : un simple pack à uploader
                t24Scripts.executerCommandeListEnvironnement(envDestination, serverDirDestination, "rm -rf " + packName, "mkdir " + packName, "tar -xvf " + packName + ".tar");
            } else {
                // deuxième cas: le fichier .tar contient plusieurs packs: packName.tar est le fichier qui represente les 
                //packs compressés et  listePacksCompresses représente la liste de dossiers qui seront générés après la décompression
                List<String> commandeList = new ArrayList<String>();
                for (String packNameThatWillBeCompressed : listePacksCompressed) {
                    commandeList.add("rm -rf " + packNameThatWillBeCompressed);
                    commandeList.add("mkdir " + packNameThatWillBeCompressed);
                    commandeList.add("ls " + packNameThatWillBeCompressed);
                }
                commandeList.add("tar -xvf " + packName + ".tar");

                File file = null;
                //faire un sleep aléatoire
                Random rand = new Random();
                int n = (rand.nextInt(10) * rand.nextInt(11)) + rand.nextInt(173);
                try {
                    Thread.sleep(n);
                } catch (InterruptedException ex) {
                    Logger.getLogger(FtpTools.class.getName()).log(Level.SEVERE, null, ex);
                }

                try {
                    file = tools.createFile(Configuration.parametresList.get("espaceLocal") + System.getProperty("file.separator") + "commandsFileOvTools" + time);
                    tools.writeInFile(file, commandeList);
                } catch (IOException exep) {
                    exep.printStackTrace();
                    Tools.traiterException(Tools.getStackTrace(exep));
                }
                uploadFileToServerDirectory(connectedUser, file, serverDirDestination, envDestination, logParallele, false);
                String[] commandArray = new String[3];
                commandArray[0] = "chmod 777 commandsFileOvTools" + time;
                commandArray[1] = "./commandsFileOvTools" + time;
                commandArray[2] = "rm -f ./commandsFileOvTools" + time;
                t24Scripts.executerCommandeListEnvironnement(envDestination, serverDirDestination, commandArray);
            }
            try {
                //supprimer le fichier du serveur
                ftpClient.deleteFile("commandsFileOvTools" + time);
                ftpClient.deleteFile(packName + ".tar");
                Tools.showConsolLog("###################################");
            } catch (Exception ex) {
                //si le fichier est introuvable, ne rien faire
            }
            //servlets.AfficherMessageEtatAvancement.setLogmessage("");
            // compter le nombre d'objet du pack uploadé
            int nbrObjetsPack;
            if (listePacksCompressed.length > 0) {
                for (String nom : listePacksCompressed) {
                    nbrObjetsPack = compterNbrObjetsPack(envDestination, serverDirDestination + "/" + nom);
                    nbrOfFilesByPack.put(nom, nbrObjetsPack);
                }
            } else {
                nbrObjetsPack = compterNbrObjetsPack(envDestination, serverDirDestination + "/" + packName);
                nbrOfFilesByPack.put(packName, nbrObjetsPack);
            }
        } catch (UnknownHostException exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        } catch (IllegalFTPResponseException exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        } catch (IOException exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return checkNbrOfFilesInUploadedFolder(nbrOfFilesByPack);
    }

    public void setLogmessage(String message) {
        try {
            servlets.AfficherMessageEtatAvancement.setLogmessage(message, Tools.getConnectedLogin());
        } catch (Exception ex) {
            try {
                servlets.AfficherMessageEtatAvancement.setLogmessage(message, userName);
            } catch (Exception exep) {
                exep.printStackTrace();
                tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
            }
        }
    }

    public void appendLogmessage(String connectedUser, String message) {
        try {
            servlets.AfficherMessageEtatAvancement.appendLogmessage(message, connectedUser);
        } catch (Exception ex) {
            try {
                servlets.AfficherMessageEtatAvancement.appendLogmessage(message, userName);
            } catch (Exception exep) {
                exep.printStackTrace();
                tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
            }
        }
    }

    private boolean deleteAll(File dir) {
        try {
            if (dir.isDirectory()) {
                File[] children = dir.listFiles();
                for (int i = 0; i < children.length; i++) {
                    boolean success = deleteAll(children[i]);
                    if (!success) {
                        return false;
                    }
                }
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return dir.delete();
    }

    public void uploadFileToServerDirectory(String connectedUser, File file, String url, EnvironnementDTO env, boolean logParallele, boolean binaire) {
        try {
            if (logParallele == true) {
                appendLogmessage(connectedUser, "Environnement " + env.getNom() + " : Upload du fichier " + file.getName() + " vers le dossier " + url + " de l'environnement " + env.getNom() + " ...<br>");
            } else {
                setLogmessage("Upload du fichier " + file.getName() + " vers le dossier " + url + " de l'environnement " + env.getNom());
            }
            ClientFTP ftp = new ClientFTP(env.getUrl(), env.getPort(), env.getEnvUserName(), env.getEnvPassword());
            if (ftp.Connect()) {
                ftp.cd(url);
                //Supprimer le fichier
                ftp.deleteFile(file.getName());
                ftp.Uploadfile(file, binaire);
            }
            ftp.Disconnect();
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        //servlets.AfficherMessageEtatAvancement.setLogmessage("");
    }

    public void uploadFileToSavedList(File file, String url, EnvironnementDTO env) {
        try {
            setLogmessage("Upload du fichier " + file.getName() + " vers le dossier &SAVEDLISTS& de l'environnement " + env.getNom());
            ClientFTP ftp = new ClientFTP(env.getUrl(), env.getPort(), env.getEnvUserName(), env.getEnvPassword());
            if (ftp.Connect()) {
                ftp.cd(url);
                //Supprimer les fichiers TAF-PACK et PACK.txt
                ftp.deleteFile(file.getName());
                String packname = file.getName().replace(".txt", "");
                packname = "TAF-" + packname;
                ftp.deleteFile(packname);
                ftp.Uploadfile(file, false);

            }
            ftp.Disconnect();
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        //servlets.AfficherMessageEtatAvancement.setLogmessage("");
    }

    public void createFolder(String folderName, String url, EnvironnementDTO env) {
        try {
            ClientFTP ftp = new ClientFTP(env.getUrl(), env.getPort(), env.getEnvUserName(), env.getEnvPassword());
            if (ftp.Connect()) {
                ftp.cd(url);
                ftp.mkDir(folderName);
            }
            ftp.Disconnect();
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }

    public void downloadFile(EnvironnementDTO envSource, String serverDirSource, String fileName, String... details) {
        setLogmessage("Download du fichier " + fileName + " depuis l'environnement " + envSource.getNom());

        try {
            FTPClient ftpClient = new FTPClient(true);
            try {
                ftpClient.openConnection(envSource.getUrl(), 21);
            } catch (Exception e) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(FtpTools.class.getName()).log(Level.SEVERE, null, ex);
                }
                ftpClient.openConnection(envSource.getUrl(), 21);
            }
            ftpClient.login(envSource.getEnvUserName(), envSource.getEnvPassword());
            ftpClient.changeDirectory(serverDirSource);



            Tools.showConsolLog("Je suis sous: " + ftpClient.getCurrentDirectory());
            Tools.showConsolLog("Je vais telecharger:" + fileName);
            File file = null;
            if (details.length > 0) {
                File f1 = new File(Configuration.parametresList.get("espaceLocal") + System.getProperty("file.separator") + details[0]);
                if (!f1.exists()) {
                    f1.mkdir();
                }
                file = new File(Configuration.parametresList.get("espaceLocal") + System.getProperty("file.separator") + details[0] + System.getProperty("file.separator") + fileName);
                if (file.exists()) {
                    file.delete();
                }
                ftpClient.downloadFile(fileName, Configuration.parametresList.get("espaceLocal") + System.getProperty("file.separator") + details[0] + System.getProperty("file.separator") + fileName + details[1], true);
                ftpClient.deleteFile(fileName);
            } else {
                file = new File(Configuration.parametresList.get("espaceLocal") + System.getProperty("file.separator") + fileName);
                if (file.exists()) {
                    file.delete();
                }
                ftpClient.downloadFile(fileName, Configuration.parametresList.get("espaceLocal") + System.getProperty("file.separator") + fileName, true);
                ftpClient.deleteFile(fileName);
            }




        } catch (UnknownHostException exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        } catch (IllegalFTPResponseException exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        } catch (IOException exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        //servlets.AfficherMessageEtatAvancement.setLogmessage("");
    }
}
