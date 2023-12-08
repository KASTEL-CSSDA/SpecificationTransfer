package edu.kit.kastel.sdq.coupling.completion.specificationtransfer.controller.consolecontroller.source;

import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.controller.filehandler.DirectoryHandler;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.controller.filehandler.FileHandler;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.controller.filehandler.source.DirectoriesSourceCitation;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.controller.filehandler.source.FilesSourceCitation;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.controller.filehandler.source.SingleFileSourceCitation;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.controller.filehandler.source.SourceCitation;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.controller.resourcebundle.ResourceBundleController;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.astparser.merge.SourceCitationType;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.properties.TransferProperties;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class gets the user to specify the file, files or directories that shall
 * be used in this programm for the later processing.
 *
 * @author Jonas
 */
public class ConsoleControllerSourceCitation {

    //ToDo: Check that the files are only Java-Files not all kinds of Files...
    /**
     * Get the Source-Citation regarding of the property "SourceCitationType" of
     * the given ISAJAM-Property.<br>
     * This returns different Sub-Classes of the SourceCitation regarding the
     * setting.
     *
     * @param transferProperties The ISAJAM-Properties to get the settings for
     * which Source-Citation Type to use.
     * @param in The Scanner used in this Program, to read in the Console-Input
     * of the User.
     * @return The sourceCitation defined through user Input. The exact
     * Sub-Class is defined by the property.
     */
    public static SourceCitation getSourceCitation(TransferProperties transferProperties, Scanner in) throws IllegalArgumentException {
        if (transferProperties == null) {
            throw new IllegalArgumentException("The provided Properties shoudn't be null");
        }
        if (in == null) {
            throw new IllegalArgumentException("The provided Scanner for the User Input shoudn't be null");
        }

        SourceCitationType exec = (SourceCitationType) transferProperties.getPropertyValue("SourceCitationType").getValue();
        Locale language = (Locale) transferProperties.getPropertyValue("Language").getValue();
        return getSourceCitation(exec, in, language);
    }

    /**
     * Get the Source-Citation regarding of the property "SourceCitationType" of
     * the given ISAJAM-Property.<br>
     * This returns different Sub-Classes of the SourceCitation regarding the
     * setting.
     *
     * @param srcCiteType The Type of SourceCitation. This defines the
     * SourceCitation that should be returned.
     * @param in The Scanner used in this Program, to read in the Console-Input
     * of the User.
     * @param locale The current Locale (for the used language at the
     * GUI/Console).
     * @return The sourceCitation defined through user Input. The exact
     * Sub-Class is defined by the property.
     */
    public static SourceCitation getSourceCitation(SourceCitationType srcCiteType, Scanner in, Locale locale) throws IllegalArgumentException {
        if (srcCiteType == null) {
            throw new IllegalArgumentException("The provided SourceCitationType shoudn't be null");
        }
        if (in == null) {
            throw new IllegalArgumentException("The provided Scanner for the User Input shoudn't be null");
        }
        if (locale == null) {
            System.err.println("The provided Locale to define the used Language was null or invalid.\n"
                    + "The Program defined it to be the default one.");
            locale = (Locale) new TransferProperties().getPropertyValue("Language").getValue();
        }

        switch (srcCiteType) {
            case SingleFile:
                return getGuidedSingleFileSourceCitation(in, locale);
            case Files:
                return getGuidedFilesSourceCitation(in, locale);
            case FilesFromDirectory:
                return getGuidedFilesSourceCitationFromDirectories(in, locale);
            case Directory:
                return getGuidedDirectoriesSourceCitation(in, locale);
            case Test: {
                try {
                    return new FilesSourceCitation(
                            Arrays.asList(
                                    FileHandler.getFileFromPath("./src/test/resources/File1.java"),
                                    FileHandler.getFileFromPath("./src/test/resources/FileA.java")
                            )
                    //                            Arrays.asList(
                    //                                    FileHandler.getFileFromPath("./src/test/resources/dir/body/File_SameName.java"),
                    //                                    FileHandler.getFileFromPath("./src/test/resources/dir/anno/File_SameName.java")
                    //                            )
                    //                            Arrays.asList(
                    //                                    FileHandler.getFileFromPath("./src/test/resources/dir/body/File_ParamAnno.java"),
                    //                                    FileHandler.getFileFromPath("./src/test/resources/dir/anno/File_ParamAnno.java")
                    //                            )
                    //                            Arrays.asList(
                    //                                    FileHandler.getFileFromPath("./src/test/resources/TravelPlanner/body/datatypes/CreditCardDetails.java"),
                    //                                    FileHandler.getFileFromPath("./src/test/resources/TravelPlanner/anno/datatypes/CreditCardDetails.java")
                    //                            )
                    //                            Arrays.asList(
                    //                                    FileHandler.getFileFromPath("./src/test/resources/TravelPlanner/body/components/CreditCardCenter.java"),
                    //                                    FileHandler.getFileFromPath("./src/test/resources/TravelPlanner/anno/components/CreditCardCenter.java")
                    //                            )
                    );
                    //./src/test/resources/TravelPlanner/body
                    //./src/test/resources/TravelPlanner/anno
                } catch (IOException ex) {
                    //Because the Test-Files are given Manually before compile-time, this shoudn't happen.
                    Logger.getLogger(ConsoleControllerSourceCitation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }

        return null;
    }

    /**
     * Get the Resource-Bundle for all the texts, corresponding to the given
     * Language-Locale.
     *
     * @param locale The locale that defines the language to use and therefore
     * which resourcebundle to load.
     * @return The resourceBundle for the
     * "ConsoleControllerSourceCitation"-Properties, with the given language,
     * defined in the locale.
     */
    private static ResourceBundle getResourceBundle(Locale locale) {
        return ResourceBundleController.getPropertyResourceBundle(locale, "ConsoleControllerSourceCitation");
    }

    private static File getGuidedFileFromInput(Scanner in, Locale locale) {
        System.out.println(
                getResourceBundle(locale).getString("srcPathToFile")
        );
        String strDir = in.nextLine();

        if (strDir != null) {
            try {
                return FileHandler.getFileFromPath(strDir);
            } catch (IOException ex) { //No need to catch NullPointer
                System.err.println("Error: " + ex.getMessage());
                //Logger.getLogger(ConsoleControllerSourceCitation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return null;
    }

    private static File getGuidedDirectoryFromInput(Scanner in, Locale locale) {
        System.out.println(
                getResourceBundle(locale).getString("srcPathToDir")
        );
        String strDir = in.nextLine();

        if (strDir != null) {
            try {
                return FileHandler.getDirectoryFromPath(strDir);
            } catch (IOException ex) {
                System.err.println("Error: " + ex.getMessage());
                //Logger.getLogger(ConsoleControllerSourceCitation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return null;
    }

    private static SingleFileSourceCitation getGuidedSingleFileSourceCitation(Scanner in, Locale locale) {
        return new SingleFileSourceCitation(getGuidedFileFromInput(in, locale));
    }

    private static FilesSourceCitation getGuidedFilesSourceCitation(Scanner in, Locale locale) {
        System.out.println(
                getResourceBundle(locale).getString("srcFiles")
        );

        FilesSourceCitation filesSrcCite = new FilesSourceCitation();

        String nextLine;
        do {
            boolean wasAdded = filesSrcCite.addFile(getGuidedFileFromInput(in, locale));

            if (wasAdded) {
                System.out.println(
                        getResourceBundle(locale).getString("addedFile")
                );
            } else {
                System.out.println(
                        getResourceBundle(locale).getString("addedNoFile")
                );
            }

            System.out.println(
                    getResourceBundle(locale).getString("srcMoreFiles")
            );
            nextLine = in.nextLine();
        } while (nextLine.compareToIgnoreCase("END") != 0);

        return filesSrcCite;
    }

    private static FilesSourceCitation getGuidedFilesSourceCitationFromDirectories(Scanner in, Locale locale) {
        DirectoriesSourceCitation dirSourceCite = getGuidedDirectoriesSourceCitation(in, locale);

        List<File> files = new ArrayList<>();

        for (File dir : dirSourceCite.getDirectories()) {
            List<File> filesFromDir = DirectoryHandler.getAllFilesFromDirectory(dir);
            files.addAll(filesFromDir);
        }

        return new FilesSourceCitation(files);
    }

    private static DirectoriesSourceCitation getGuidedDirectoriesSourceCitation(Scanner in, Locale locale) {
        System.out.println(
                getResourceBundle(locale).getString("srcDirs")
        );

        DirectoriesSourceCitation dirsSrcCite = new DirectoriesSourceCitation();

        String nextLine;
        do {
            boolean wasAdded = dirsSrcCite.addDirectory(getGuidedDirectoryFromInput(in, locale));

            if (wasAdded) {
                System.out.println(
                        getResourceBundle(locale).getString("addedDir")
                );
            } else {
                System.out.println(
                        getResourceBundle(locale).getString("addedNoDir")
                );
            }

            System.out.println(
                    getResourceBundle(locale).getString("srcMoreDirs")
            );
            nextLine = in.nextLine();
        } while (nextLine.compareToIgnoreCase("END") != 0);

        return dirsSrcCite;
    }

}
