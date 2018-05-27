// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 14/11/2012 15:22:35
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ftpClient.java
package com.ftpClient;

import java.io.*;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import tools.Tools;

public class ClientFTP {

    public ClientFTP(String fTP_hostname, int pORT, String FTP_LOGIN, String FTP_PASSWORD) {
        this.FTP_LOGIN = "";
        this.FTP_PASSWORD = "";
        ftp = new FTP();
        ftpClient = null;
        listfilesEchec = null;
        FTP_hostname = fTP_hostname;
        PORT = pORT;
        this.FTP_LOGIN = FTP_LOGIN;
        this.FTP_PASSWORD = FTP_PASSWORD;
        listfilesEchec = new ArrayList();
        ftpClient = new FTPClient();
    }

    public ClientFTP(String fTP_hostname, String FTP_LOGIN, String FTP_PASSWORD) {
        this.FTP_LOGIN = "";
        this.FTP_PASSWORD = "";
        ftp = new FTP();
        ftpClient = null;
        listfilesEchec = null;
        FTP_hostname = fTP_hostname;
        this.FTP_LOGIN = FTP_LOGIN;
        this.FTP_PASSWORD = FTP_PASSWORD;
        listfilesEchec = new ArrayList();
        ftpClient = new FTPClient();
    }

    public boolean Connect() {
        boolean result = false;
        try {
            ftpClient.connect(FTP_hostname, PORT);
            //FtpClient.setConnectTimeout(600000);

            if (ftpClient.isConnected()) {
                if (result = ftpClient.login(FTP_LOGIN, FTP_PASSWORD)) {
                    Tools.showConsolLog((new StringBuilder("Connection vers le serveur FTP ")).append(FTP_hostname).append(" sur le port ").append(PORT).append(" : Ok").toString());
                } else {
                    System.err.println((new StringBuilder("Authentification vers le serveur FTP ")).append(FTP_hostname).append(":").append(result).toString());
                }
            }
        } catch (SocketException e) {
            System.err.println((new StringBuilder("ftpClient.Connect-SocketException : ")).append(e.getMessage()).toString());
        } catch (IOException e) {
            System.err.println((new StringBuilder("ftpClient.Connect-IOException : ")).append(e.getMessage()).toString());
        }
        return result;
    }

    public void Disconnect() {
        if (ftpClient.isConnected()) {
            try {
                if (ftpClient.logout()) {
                    ftpClient.disconnect();
                    Tools.showConsolLog("D\351connexion du serveur FTP:\t OK");
                }
            } catch (IOException e) {
                System.err.println("ftpClient.Disconnect-IOException : " + e);
                e.printStackTrace();
            }
        }
    }

    public void deleteFile(String fileName) {
        try {
            ftpClient.deleteFile(fileName);
        } catch (IOException ex) {
            Logger.getLogger(ClientFTP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean downloadFile(File file, String folderAbsoluteName) {
        boolean result_FTPSendFile = false;
        FileInputStream fis = null;
        OutputStream output = null;
        try {
            fis = new FileInputStream(file);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.ASCII_FILE_TYPE);
            ftpClient.pasv();


            output = new FileOutputStream(folderAbsoluteName + "/" + file.getName());

            result_FTPSendFile = ftpClient.retrieveFile(file.getName(), output);
            if (result_FTPSendFile) {
                listfilesEchec.add(file);
            }
        } catch (FileNotFoundException e) {
            System.err.println("ftpClient.Uploadfile-FileNotFoundException :" + e);
        } catch (IOException e) {
            System.err.println("ftpClient.Uploadfile-IOException :" + e);
        }
        try {
            fis.close();
            output.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return result_FTPSendFile;
    }

    public boolean Uploadfile(File file, boolean binaire) {
        boolean result_FTPSendFile = false;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            ftpClient.enterLocalPassiveMode();
            if (binaire) {
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            } else {
                ftpClient.setFileType(FTP.ASCII_FILE_TYPE);
            }

            ftpClient.pasv();
            result_FTPSendFile = ftpClient.storeFile(file.getName(), fis);
            if (result_FTPSendFile) {
                listfilesEchec.add(file);
            }
        } catch (FileNotFoundException e) {
            System.err.println("ftpClient.Uploadfile-FileNotFoundException :" + e);
        } catch (IOException e) {
            System.err.println("ftpClient.Uploadfile-IOException :" + e);
        }
        try {
            fis.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return result_FTPSendFile;
    }

    public int mkDir(String filename) {
        int a = 0;
        try {
            a = ftpClient.mkd(filename);
        } catch (IOException e) {
            System.err.println("ftpClient.mkDir-IOException :" + e);
        }
        return a;
    }

    public int cd(String filename) {
        int a = 0;
        try {
            a = ftpClient.cwd(filename);
        } catch (IOException e) {
            System.err.println("ftpClient.cd-IOException :" + e);
        }
        return a;
    }

    public String getFTP_hostname() {
        return FTP_hostname;
    }

    public void setFTP_hostname(String fTPHostname) {
        FTP_hostname = fTPHostname;
    }

    public static int getPORT() {
        return PORT;
    }

    public static void setPORT(int pORT) {
        PORT = pORT;
    }

    public String getFTP_LOGIN() {
        return FTP_LOGIN;
    }

    public void setFTP_LOGIN(String fTPLOGIN) {
        FTP_LOGIN = fTPLOGIN;
    }

    public String getFTP_PASSWORD() {
        return FTP_PASSWORD;
    }

    public void setFTP_PASSWORD(String fTPPASSWORD) {
        FTP_PASSWORD = fTPPASSWORD;
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        ftpClient = ftpClient;
    }

    public List getListfilesEchec() {
        return listfilesEchec;
    }

    public void setListfilesEchec(List listfilesEchec) {
        this.listfilesEchec = listfilesEchec;
    }
    private static String FTP_hostname = "";
    private static int PORT = 21;
    private String FTP_LOGIN;
    private String FTP_PASSWORD;
    private FTP ftp;
    private FTPClient ftpClient;
    private List listfilesEchec;
}
