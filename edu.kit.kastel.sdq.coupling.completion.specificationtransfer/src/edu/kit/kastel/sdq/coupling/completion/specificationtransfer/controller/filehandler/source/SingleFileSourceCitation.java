package edu.kit.kastel.sdq.coupling.completion.specificationtransfer.controller.filehandler.source;

import java.io.File;

/**
 * This SourceCitation holds a single File as the source.
 *
 * @author Jonas
 */
public class SingleFileSourceCitation extends SourceCitation {

    private File file;

    /**
     * The Constructor.
     *
     * @param file The File of this SourceCitation.
     * @throws IllegalArgumentException when the given file cannot be set.
     * (Because it is null, or not a file)
     */
    public SingleFileSourceCitation(File file) throws IllegalArgumentException {
        boolean setFile = this.setFile(file);

        if (!setFile) {
            throw new IllegalArgumentException(
                    "The given Argument \"" + file + "\" is not a File."
                    + " It might be a directory."
            );
        }
    }

    //Getter & Setter:
    public File getFile() {
        return file;
    }

    public final boolean setFile(File file) {
        if (file == null) {
            this.file = null;
        } else if (file.isFile()) {
            this.file = file;
            return true;
        }

        return false;
    }

}
