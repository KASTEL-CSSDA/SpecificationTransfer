package edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.astparser.merge;

import com.github.javaparser.ast.CompilationUnit;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides some Parsing Functions / Operations that are called via
 * the FunctionsController.
 *
 * @author Jonas
 */
public class FunctionsParse {

    /**
     * Parse the given single File.<br>
     * This uses the ASTParser (which itself is using the JavaParser)
     *
     * @param f A File.
     * @return The result of the parsing of the JavaParser.
     */
    public static CompilationUnit parseSingleFile(File f) {
        return ASTParser.parse(f);
    }

    /**
     * Parse the given List of Files. <br>
     * Using the JavaParser.
     *
     * @param files A List of Files to parse.
     * @return A List of the compiled Files.
     */
    public static List<CompilationUnit> parseFiles(List<File> files) {
        List<CompilationUnit> parsedFiles = new ArrayList<>();
        for (File file : files) {
            parsedFiles.add(
                    ASTParser.parse(file)
            );
        }
        return parsedFiles;
    }

}
