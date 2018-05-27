/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import java.util.*;
import java.io.*;

/**
 *
 * @author 04486
 */
public class FileListing {

    public List<File> listAllFiles(File startingDirectory) throws FileNotFoundException {
        List<File> fileList = new ArrayList<File>();
        try {
            List<File> filesAndDirectory = FileListing.getFileListing(startingDirectory);
            //print out all file names, in the the order of File.compareTo()
            for (File file : filesAndDirectory) {
                if (file.isFile()) {
                    fileList.add(file);
                }
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return fileList;
    }

    /**
     * Recursively walk a directory tree and return a List of all Files found;
     * the List is sorted using File.compareTo().
     *
     * @param aStartingDir is a valid directory, which can be read.
     */
    static public List<File> getFileListing(File aStartingDir) {
        List<File> result = null;
        try {
            validateDirectory(aStartingDir);
            result = getFileListingNoSort(aStartingDir);
            Collections.sort(result);
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return result;
    }

    // PRIVATE //
    static private List<File> getFileListingNoSort(File aStartingDir) {
        List<File> result = new ArrayList<File>();
        try {
            File[] filesAndDirs = aStartingDir.listFiles();
            List<File> filesDirs = Arrays.asList(filesAndDirs);
            for (File file : filesDirs) {
                result.add(file); //always add, even if directory
                if (!file.isFile()) {
                    //must be a directory
                    //recursive call!
                    List<File> deeperList = getFileListingNoSort(file);
                    result.addAll(deeperList);
                }
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return result;
    }

    /**
     * Directory is valid if it exists, does not represent a file, and can be
     * read.
     */
    static private void validateDirectory(
            File aDirectory) throws FileNotFoundException {
        if (aDirectory == null) {
            throw new IllegalArgumentException("Directory should not be null.");
        }
        if (!aDirectory.exists()) {
            throw new FileNotFoundException("Directory does not exist: " + aDirectory);
        }
        if (!aDirectory.isDirectory()) {
            throw new IllegalArgumentException("Is not a directory: " + aDirectory);
        }
        if (!aDirectory.canRead()) {
            throw new IllegalArgumentException("Directory cannot be read: " + aDirectory);
        }
    }
}
