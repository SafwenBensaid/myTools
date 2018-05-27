/**
 *
 * @author 04486
 */
package tools;

import java.io.*;
import java.util.zip.*;
import org.apache.tools.bzip2.*;
import org.apache.tools.tar.*;

/**
 * Utility class to untar files, files can be zipped in multi format (extension
 * tar, tar.gzip,tar.gz, tar.bz2, tar.bzip2 are supported).
 *
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 *
 */
public class Untar {

    private String tarFileName;
    private File dest;

    /**
     * The logger
     */
    /**
     * (note : constructor that takes two files as parameter could probably be a
     * better design)
     *
     * @param tarFileName the path to the file we want to untar
     * @param dest the directory where the file should be untar
     */
    public Untar(String tarFileName, File dest) {
        this.tarFileName = tarFileName;
        this.dest = dest;
    }

    private InputStream getDecompressedInputStream(final String name, final InputStream istream) throws IOException {
        Tools.showConsolLog("untar: decompress " + name + " to " + dest);
        if (name == null) {
            throw new RuntimeException("fileName to decompress can not be null");
        }
        if (name.toLowerCase().endsWith("gzip") || name.toLowerCase().endsWith("gz")) {
            return new BufferedInputStream(new GZIPInputStream(istream));
        } else if (name.toLowerCase().endsWith("bz2") || name.toLowerCase().endsWith("bzip2")) {
            final char[] magic = new char[]{'B', 'Z'};
            for (int i = 0; i < magic.length; i++) {
                if (istream.read() != magic[i]) {
                    throw new RuntimeException("Invalid bz2 file." + name);
                }
            }
            return new BufferedInputStream(new CBZip2InputStream(istream));
        } else if (name.toLowerCase().endsWith("tar")) {
            return istream;
        }
        throw new RuntimeException("can only detect compression for extension tar, gzip, gz, bz2, or bzip2");
    }

    /**
     * process the untar operation
     *
     * @throws IOException
     */
    public void untar() throws IOException {
        Tools.showConsolLog("untar: untar " + tarFileName + " to " + dest);
        TarInputStream tin = null;
        try {
            if (!dest.exists()) {
                dest.mkdir();
            }

            tin = new TarInputStream(getDecompressedInputStream(tarFileName, new FileInputStream(new File(tarFileName))));

            TarEntry tarEntry = tin.getNextEntry();

            while (tarEntry != null) {
                File destPath = new File(dest.toString() + File.separatorChar + tarEntry.getName());

                if (tarEntry.isDirectory()) {
                    destPath.mkdir();
                } else {
                    if (!destPath.getParentFile().exists()) {
                        destPath.getParentFile().mkdirs();
                    }
                    Tools.showConsolLog("untar: untar " + tarEntry.getName() + " to " + destPath);
                    FileOutputStream fout = new FileOutputStream(destPath);
                    try {
                        tin.copyEntryContents(fout);
                    } finally {
                        try {
                            fout.flush();
                            fout.close();
                        } catch (Exception exp) {
                            System.err.println(exp.getMessage());
                        }
                    }
                }
                tarEntry = tin.getNextEntry();
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        } finally {
            if (tin != null) {
                try {
                    tin.close();
                } catch (Exception exp) {
                    System.err.println(exp.getMessage());
                }
            }
        }

    }
}