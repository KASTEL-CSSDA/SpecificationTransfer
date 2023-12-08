package edu.kit.kastel.sdq.coupling.completion.specificationtransfer.controller.filehandler.source;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * The SourceCitation that holds a List of Files.
 *
 * @author Jonas
 */
public class FilesSourceCitation extends SourceCitation {

    private List<File> files;

    public FilesSourceCitation() {
        this.files = new ArrayList<>();
    }

    /**
     * The Constructor.
     *
     * @param files A list of Files (that are actually files).
     * @throws IllegalArgumentException when one of the given files cannot be
     * added to the List. (Because it is null, or not a file)
     */
    public FilesSourceCitation(List<File> files) throws IllegalArgumentException {
        this();
        for (File file : files) {
            boolean addedFile = this.addFile(file);

            if (!addedFile) {
                throw new IllegalArgumentException(
                        "The given Argument \"" + file + "\" is not a File."
                        + " It might be a directory."
                );
            }
        }
    }

    //Helper / Worker:
    /**
     * Add a given File to the List of files. If it really is a file. Otehrwise
     * nothing is added.
     *
     * @param file A File, that is added to the List of files if it is one.
     * @return true if the given file was a file and therefore added to the
     * List, or false otherwise.
     */
    public final boolean addFile(File file) {
        if (file != null && file.isFile()) {
            this.files.add(file);
            return true;
        }
        return false;
    }

    //Getter & Setter:
    public List<File> getFiles() {
        return files;
    }

    /**
     *
     * @param files List of Files that all are true/normal files. <br>
     * Remember: If a File-Object in the provided List points to an Directory
     * instead of a File, it is not added to the internal data-structure.
     */
    public void setFiles(List<File> files) {
        this.files.clear();
        for (File file : files) {
            this.addFile(file);
        }
    }

}
