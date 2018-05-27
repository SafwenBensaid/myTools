/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package threads;

import dto.EnvironnementDTO;
import tools.FtpTools;

/**
 *
 * @author 04486
 */
public class UploadPacksParalleleThread extends Thread {

    public EnvironnementDTO envDestination;
    public String serverDirDestination;
    public String packName;
    public boolean logParallele;
    public String[] listePacksCompressed;
    public String connectedUser;
    public String erreurTransfertPack;

    public UploadPacksParalleleThread(EnvironnementDTO envDestination, String serverDirDestination, String packName, boolean logParallele, String[] listePacksCompressed, String connectedUser) {
        this.envDestination = envDestination;
        this.serverDirDestination = serverDirDestination;
        this.packName = packName;
        this.logParallele = logParallele;
        this.listePacksCompressed = listePacksCompressed;
        this.connectedUser = connectedUser;
    }

    @Override
    public void run() {
        try {
            FtpTools ftpTools = new FtpTools(connectedUser);
            erreurTransfertPack = ftpTools.uploaddCompressedFolderThenUntarIt(connectedUser, envDestination, serverDirDestination, packName, logParallele, listePacksCompressed);
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }
}
