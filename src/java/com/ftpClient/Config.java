// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 14/11/2012 15:22:20
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Config.java
package com.ftpClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import com.ftpClient.ClientFTP;

// Referenced classes of package com.strs:
//            ftpClient
public class Config {

    public Config() {
    }

    public static String getPathConfigFile() {
        return pathConfigFile;
    }

    public static void setPathConfigFile(String pathConfigFile) {
        pathConfigFile = pathConfigFile;
    }

    public static String getPathOfScript() {
        return pathOfScript;
    }

    public static void setPathOfScript(String pathOfScript) {
        pathOfScript = pathOfScript;
    }

    public static String getPathconfigfile() {
        return pathConfigFile;
    }

    public static String parseDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }

    public static String parseDate2(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
        return sdf.format(date);
    }

    public static String parseDate3(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy");
        return sdf.format(date);
    }

    public static String parseDate4(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd  HHmmss");
        return sdf.format(date);
    }
    public static String pathOfScript = "/export/strsuser/COMMON_DVLP/SCRIPTS/java/SendFilesToGed";
    private static String pathConfigFile;
    public static String path_pp = "";
    public static String prefixe = "";
    public static String suffixe = "";
    public static String ftp_server = "127.0.0.1";
    public static int ftp_port = 21;
    public static String ftp_logIn = "anonymous";
    public static String ftp_passwd = "anonymous";
    public static String ftp_cwd = "./";

    static {
        pathConfigFile = "/export/strsuser/COMMON_DVLP/SCRIPTS/java/SendFilesToGed/SendFilesToGed.properties";

        Properties prop = new Properties();
        try {
            FileInputStream in = new FileInputStream(pathConfigFile);
            prop.load(in);
            try {
                in.close();
            } catch (Exception exp) {
            }
            path_pp = prop.getProperty("com.strs.pp_dir");
            prefixe = prop.getProperty("com.strs.prefixe");
            suffixe = prop.getProperty("com.strs.suffixe");
            ftp_server = prop.getProperty("com.strs.ftp.server");
            ftp_port = Integer.parseInt(prop.getProperty("com.strs.ftp.port"));
            ftp_logIn = prop.getProperty("com.strs.ftp.login");
            ftp_passwd = prop.getProperty("com.strs.ftp.passwd");
            ftp_cwd = prop.getProperty("com.strs.ftp.cwd");
        } catch (IOException e) {
            System.err.print(e);
        }
    }
}
