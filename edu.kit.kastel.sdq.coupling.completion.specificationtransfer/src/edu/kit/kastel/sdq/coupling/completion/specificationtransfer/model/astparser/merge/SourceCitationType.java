package edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.astparser.merge;

import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.helper.Labeled;

/**
 * The SourceCitation-Type of the Program defines the way the files/classes to
 * progress are read in. For example, if the user wants to use ISA-JAM for files
 * or a whole directory, ... <br>
 * This is needed to use the correct import method for the file(s).
 *
 * @author Jonas
 */
public enum SourceCitationType implements Labeled<String> {
    /**
     * Only one File
     */
    SingleFile("S"),
    /**
     * Multiple Files by Paths
     */
    Files("F"),
    /**
     * Files by Directory (to add in all Files from that Directory)
     */
    FilesFromDirectory("FFD"),
    /**
     * Directories (Not Files), to also retain the structure of the directories
     */
    Directory("Dir"),
    /**
     * For Testing Purposes, Load in Predefined Files.
     */
    Test("T");

    public final String label;

    private SourceCitationType(String label) {
        this.label = label;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public Labeled<String> getInstanceByLabel(String lbl) {
        if (lbl.equalsIgnoreCase((SingleFile.getLabel()))
                || lbl.equalsIgnoreCase(SingleFile.toString())) {
            return SingleFile;
        } else if (lbl.equalsIgnoreCase((Files.getLabel()))
                || lbl.equalsIgnoreCase(Files.toString())) {
            return Files;
        } else if (lbl.equalsIgnoreCase((FilesFromDirectory.getLabel()))
                || lbl.equalsIgnoreCase(FilesFromDirectory.toString())) {
            return FilesFromDirectory;
        } else if (lbl.equalsIgnoreCase((Directory.getLabel()))
                || lbl.equalsIgnoreCase(Directory.toString())) {
            return Directory;
        } else if (lbl.equalsIgnoreCase((Test.getLabel()))
                || lbl.equalsIgnoreCase(Test.toString())) {
            return Directory;
        } else {
            return null;
        }
    }
}
