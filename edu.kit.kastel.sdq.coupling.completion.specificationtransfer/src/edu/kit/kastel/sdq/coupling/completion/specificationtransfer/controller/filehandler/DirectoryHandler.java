package edu.kit.kastel.sdq.coupling.completion.specificationtransfer.controller.filehandler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides some helpful Methods when working with Files that
 * represent directories.
 *
 * @author Jonas
 */
public class DirectoryHandler {

    /**
     * Get all files of a directory and all Sub-Directories in a List. <br>
     * This is done by recursevly adding in all Files of all Sub-Directories
     * into the List.
     *
     * @param dir A File that points to a directory.
     * @return A list of all recursevly found Files of the given directory and
     * all sub-directories.
     */
    public static List<File> getAllFilesFromDirectory(File dir) {
        List<File> files = new ArrayList<>();

        for (File fileEntry : dir.listFiles()) {
            if (fileEntry.isDirectory()) {
                files.addAll(
                        getAllFilesFromDirectory(fileEntry)
                );
            } else {
                files.add(fileEntry);
            }
        }

        return files;
    }

    /**
     * Get the direct Files from the given directory. That means all the Files
     * in that location, not checking any sub-directories.
     *
     * @param dir The File that is a path to a directory.
     * @return The List of Files that are in that directory. (Not regarding any
     * sub-Folders)
     */
    public static List<File> getDirectFilesFromDirectory(File dir) {
        List<File> files = new ArrayList<>();

        for (File fileEntry : dir.listFiles()) {
            if (!fileEntry.isDirectory()) {
                files.add(fileEntry);
            }
        }

        return files;
    }

    /**
     * Get the direct Sub-Directoies from the given directory. That means all
     * the Directories in that location, not checking any further down
     * sub-directories.
     *
     * @param dir The File that is a path to a directory.
     * @return The List of Files that are directories in that dir.
     */
    public static List<File> getSubDirectoriesFromDirectory(File dir) {
        List<File> subDirs = new ArrayList<>();

        for (File fileEntry : dir.listFiles()) {
            if (fileEntry.isDirectory()) {
                subDirs.add(fileEntry);
            }
        }

        return subDirs;
    }

    //String / Print:
    /**
     * Get the String Representation of a given Directory. <br>
     * This String will be in a structured Representation, by using Tabs to
     * indent Sub-Folders and their contents.
     *
     * @param dir The directory to get the String Representation of.
     * @return A String representation (with indents) of all the Sub-Folders and
     * Files of the given directoy.
     */
    public static String getFileStructureRepresentation(File dir) {
        return getFileStructureRepresentation(dir, 0);
    }

    private static String getFileStructureRepresentation(File dir, int tabCount) {
        String curDir = dir.getPath() + ": " + "\n";

        //Move in the Files according to the depth of the tabCount.
        for (int i = 0; i < tabCount + 1; i++) {
            curDir += "\t";
        }

        for (File fileEntry : dir.listFiles()) {
            if (fileEntry.isDirectory()) {
                curDir += getFileStructureRepresentation(fileEntry, tabCount + 1);
            } else {
                curDir += fileEntry.getName();
            }
        }

        return curDir;
    }
}
