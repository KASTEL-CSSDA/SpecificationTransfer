package edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model;

import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.controller.filehandler.FileHandler;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class provides some Show Functions / Operations that are called via the
 * FunctionsController.
 *
 * @author Jonas
 */
public class FunctionsShow {

    //Show:
    /**
     * Get the String from the given File, by trying to read it in.<br> This
     * could result in a IOException, that will be catched.
     *
     * @param f The file to read.
     * @return The String of the read in File, or null if there was an error.
     */
    public static String getStringOfSingleFile(File f) {
        String strFile = null;
        try {
            strFile = FileHandler.getStringFromFile(f);
        } catch (IOException ex) {
            Logger.getLogger(FunctionsShow.class.getName()).log(Level.SEVERE, null, ex);
        }
        return strFile;
    }

    /**
     * Get a List of each String-Representations of the given Files.
     *
     * @param files List of Files.
     * @return A List of Strings that represent the given Files. (Each
     * File-Entry of the given List is used to get a String entry for the
     * restult-List)
     */
    public static List<String> getStringOfFiles(List<File> files) {
        List<String> fileStrings = new ArrayList<>();
        for (File file : files) {
            fileStrings.add(
                    getStringOfSingleFile(file)
            );
        }
        return fileStrings;
    }

}
