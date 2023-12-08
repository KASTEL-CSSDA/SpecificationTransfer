package edu.kit.kastel.sdq.coupling.completion.specificationtransfer.controller.filehandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

/**
 * This Class provides some helpful Methods when working with Files. <br>
 * Remember: Files can be pointing to real Files or Directories.
 *
 * @author Jonas
 */
public class FileHandler {

    public static File getFileFromPath(String strDirToFile) throws NullPointerException, FileNotFoundException, IOException {
        if (strDirToFile == null) {
            throw new NullPointerException("The File-Path is not allowed to be null.");
        }

        File file = new File(strDirToFile);

        //Error-Handling
        if (!file.exists()) {
            throw new FileNotFoundException("The given File does not exist.");
        }
        if (!file.isFile()) {
            throw new IOException("The given Path is not pointing to a File.");
        }

        return file;
    }

    public static File getDirectoryFromPath(String directoryPath) throws FileNotFoundException, IOException {
        File file = Path.of(directoryPath).toAbsolutePath().toFile();

        //Error-Handling
        if (!file.exists()) {
            throw new FileNotFoundException("The given Directory does not exist.");
        }
        if (!file.isDirectory()) {
            throw new IOException("The given Path is not pointing to a Directory.");
        }

        return file;
    }

    public static String getFileExtension(File file) {
        if (file == null) {
            return null;
        }

        String fileName = file.getName();

        if (fileName == null) {
            return null;
        }

        int dotIndex = fileName.lastIndexOf(".");

        if (dotIndex < 0) {
            return null;
        }

        return fileName.substring(dotIndex + 1);
    }

    public static boolean hasExtension(File file, String ext) {
        if (file == null || ext == null) {
            return false;
        }

        String extension = new String(ext);
        //Check that the "." is not part of the extension.
        if (extension.startsWith(".")) {
            extension = extension.substring(1);
        }
        if (extension.matches("[\\w\\n]+")) {
            throw new IllegalArgumentException("The given Extension does not match the allowed Characters.");
        }

        String fileExtension = getFileExtension(file);
        if (fileExtension == null) {
            return false;
        }

        return fileExtension.equals(extension);
    }

    public static boolean isJavaFile(File file) {
        return hasExtension(file, "java");
    }

    public static String getStringFromFile(File f) throws FileNotFoundException, IOException {
        if (f == null) {
            return null;
        }

        BufferedReader br = new BufferedReader(new FileReader(f));

        String strFile = "";
        String curStr;
        while ((curStr = br.readLine()) != null) {
            strFile += curStr + "\n";
        }

        return strFile;
    }

}
