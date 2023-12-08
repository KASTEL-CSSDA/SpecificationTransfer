package edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.astparser.merge;

import com.github.javaparser.ParseProblemException;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This Class encapsulates / interfaces the JavaParser and provides the Methods
 * used for this Program.
 *
 * @author Jonas
 */
public class ASTParser {

    /**
     * Parse the given String using the StaticJavaParser.
     *
     * @param str The String-Representation of a Java-File to parse.
     * @return The Parsing-Result from the StaticJavaParser.
     */
    public static CompilationUnit parse(String str) {
        try {
            return StaticJavaParser.parse(str);
        } catch (ParseProblemException ex) {
            Logger.getLogger(ASTParser.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    /**
     * Parse the given File using the StaticJavaParser.
     *
     * @param f The file to parse
     * @return The Parsing-Result from the StaticJavaParser.
     */
    public static CompilationUnit parse(File f) {
        try {
            return StaticJavaParser.parse(f);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ASTParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseProblemException ex) {
            Logger.getLogger(ASTParser.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

}
