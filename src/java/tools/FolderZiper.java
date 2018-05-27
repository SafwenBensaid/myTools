/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

/**
 *
 * @author 04486
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FolderZiper {

    public void zipFolder(String srcFolder, String destZipFile) throws Exception {
        try {
            servlets.AfficherMessageEtatAvancement.setLogmessage("Comression du dossier " + srcFolder, Tools.getConnectedLogin());
            ZipOutputStream zip = null;
            FileOutputStream fileWriter = null;

            fileWriter = new FileOutputStream(destZipFile);
            zip = new ZipOutputStream(fileWriter);

            addFolderToZip("", srcFolder, zip);
            zip.flush();
            try {
                zip.close();
                fileWriter.close();
            } catch (Exception exp) {
                System.err.println(exp.getMessage());
            }

        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        //servlets.AfficherMessageEtatAvancement.setLogmessage("");
    }

    static private void addFileToZip(String path, String srcFile, ZipOutputStream zip) {
        try {
            File folder = new File(srcFile);
            if (folder.isDirectory()) {
                addFolderToZip(path, srcFile, zip);
            } else {
                byte[] buf = new byte[1024];
                int len;
                FileInputStream in = new FileInputStream(srcFile);
                zip.putNextEntry(new ZipEntry(path + "/" + folder.getName()));
                while ((len = in.read(buf)) > 0) {
                    zip.write(buf, 0, len);
                }
                try {
                    in.close();
                } catch (Exception exp) {
                    System.err.println(exp.getMessage());
                }
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }

    static private void addFolderToZip(String path, String srcFolder, ZipOutputStream zip) {
        try {
            File folder = new File(srcFolder);
            for (String fileName : folder.list()) {
                if (path.equals("")) {
                    addFileToZip(folder.getName(), srcFolder + "/" + fileName, zip);
                } else {
                    addFileToZip(path + "/" + folder.getName(), srcFolder + "/" + fileName, zip);
                }
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }
}