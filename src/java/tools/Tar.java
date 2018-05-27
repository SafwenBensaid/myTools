/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


import org.xeustechnologies.jtar.TarEntry;
import org.xeustechnologies.jtar.TarInputStream;
import org.xeustechnologies.jtar.TarOutputStream;

/**
 *
 * @author 04486
 */
public class Tar {

    static final int BUFFER = 2048;

    /**
     * Tar the given folder
     *
     * @throws IOException
     */
    /*
     public static void main(String[] args) {
     try {
     Tar t = new Tar();
            
     String[] packsArray = new String[2];
     packsArray[0]="C:\\FILES\\TEST\\TAF-LIVHF.MAIN.8826.TST";
     packsArray[1]="C:\\FILES\\TEST\\TAF-LIVHF.MAIN.8827.TST";
            
     t.tar("C:\\FILES\\TEST\\TAF-LIV.CORR.8817.tar",packsArray);
            
     } catch (IOException ex) {
     Logger.getLogger(Tar.class.getName()).log(Level.SEVERE, null, ex);
     }
     }
     */
    public void tar(String destinationFileNameWithAbsolutePath, String... packNamesWithAbsolutePath) throws IOException {
        TarOutputStream out = null;
        FileOutputStream dest = null;
        BufferedOutputStream bops = null;
        try {
            dest = new FileOutputStream(destinationFileNameWithAbsolutePath);
            bops = new BufferedOutputStream(dest);
            out = new TarOutputStream(bops);

            for (String packNameWithAbsolutePath : packNamesWithAbsolutePath) {
                tarFolder(null, packNameWithAbsolutePath, out);
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        } finally {
            try {
                out.close();
                dest.close();
                bops.close();
            } catch (Exception exp) {
                System.err.println(exp.getMessage());
            }
        }
    }

    /**
     * Untar the tar file
     *
     * @throws IOException
     */
    public void untar() throws IOException {
        try {
            File zf = new File("C:\\FILES\\TEST\\TAF-1379596036529.tar");
            String destFolder = "C:\\FILES\\TEST";

            BufferedOutputStream dest = null;
            FileInputStream fis = new FileInputStream(zf);
            TarInputStream tis = new TarInputStream(new BufferedInputStream(fis));
            TarEntry entry;
            while ((entry = tis.getNextEntry()) != null) {
                Tools.showConsolLog("Extracting: " + entry.getName());
                int count;
                byte data[] = new byte[BUFFER];

                if (entry.isDirectory()) {
                    new File(destFolder + "/" + entry.getName()).mkdirs();
                    continue;
                } else {
                    new File(destFolder + "/" + entry.getName().substring(0, entry.getName().lastIndexOf('/')))
                            .mkdirs();
                }

                FileOutputStream fos = new FileOutputStream(destFolder + "/" + entry.getName());
                dest = new BufferedOutputStream(fos, BUFFER);

                while ((count = tis.read(data, 0, BUFFER)) != -1) {
                    dest.write(data, 0, count);
                }
                try {
                    dest.flush();
                    dest.close();
                } catch (Exception exp) {
                    System.err.println(exp.getMessage());
                }

            }
            try {
                tis.close();
                fis.close();
            } catch (Exception exp) {
                System.err.println(exp.getMessage());
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }

    public void tarFolder(String parent, String path, TarOutputStream out) {
        try {
            BufferedInputStream origin = null;
            File f = new File(path);
            String files[] = f.list();

            // is file
            if (files == null) {
                files = new String[1];
                files[0] = f.getName();
            }

            parent = ((parent == null) ? (f.isFile()) ? "" : f.getName() + "/" : parent + f.getName() + "/");

            for (int i = 0; i < files.length; i++) {
                Tools.showConsolLog("Adding: " + files[i]);
                File fe = f;
                byte data[] = new byte[BUFFER];

                if (f.isDirectory()) {
                    fe = new File(f, files[i]);
                }

                if (fe.isDirectory()) {
                    String[] fl = fe.list();
                    if (fl != null && fl.length != 0) {
                        tarFolder(parent, fe.getPath(), out);
                    } else {
                        TarEntry entry = new TarEntry(fe, parent + files[i] + "/");
                        out.putNextEntry(entry);
                    }
                    continue;
                }

                FileInputStream fi = new FileInputStream(fe);
                origin = new BufferedInputStream(fi, BUFFER);

                TarEntry entry = new TarEntry(fe, parent + files[i]);
                out.putNextEntry(entry);

                int count;
                int bc = 0;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                    bc += count;
                }
                try {
                    out.flush();
                    fi.close();
                    origin.close();
                } catch (Exception exp) {
                    System.err.println(exp.getMessage());
                }
            }

        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }
}
