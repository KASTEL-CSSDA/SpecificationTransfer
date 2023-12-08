package edu.kit.kastel.sdq.coupling.completion.specificationtransfer.controller.filehandler.source;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * The SourceCitation with a List of Directoies.
 *
 * @author Jonas
 */
public class DirectoriesSourceCitation extends SourceCitation {

    private List<File> directories;

    //Constructors:
    public DirectoriesSourceCitation() {
        this.directories = new ArrayList<>();
    }

    /**
     * The Constructor.
     *
     * @param dirs List of Files that all are directories. <br>
     * Remember: If a File-Object in the provided List points to an actual File
     * instead of a directory it is not added to the internal data-structure.
     * @throws IllegalArgumentException when one of the given files cannot be
     * added. (Because it is null, or not pointing to a directory)
     */
    public DirectoriesSourceCitation(List<File> dirs) throws IllegalArgumentException {
        this();
        for (File dir : dirs) {
            boolean addedDir = this.addDirectory(dir);

            if (!addedDir) {
                throw new IllegalArgumentException(
                        "The given Argument \"" + dir + "\" is not a Directory."
                        + " It might be a File."
                );
            }
        }
    }

    //Helper / Worker:
    /**
     * Add a given File to the List of directories if it is a directory.
     * Otehrwise nothing is added.
     *
     * @param dir A File (should be a directory), that is added to the List of
     * directories if it is one.
     * @return true if the given file was a directoey and therefore added to the
     * List, or false otherwise.
     */
    public final boolean addDirectory(File dir) {
        if (dir != null && dir.isDirectory()) {
            this.directories.add(dir);
            return true;
        }
        return false;
    }

    //Getter & Setter:
    public List<File> getDirectories() {
        return this.directories;
    }

    /**
     *
     * @param dirs List of Files that all are directories. <br>
     * Remember: If a File-Object in the provided List points to an actual File
     * instead of a directory it is not added to the internal data-structure.
     */
    public void setDirectories(List<File> dirs) {
        this.directories.clear();
        for (File dir : dirs) {
            this.addDirectory(dir);
        }
    }
}
