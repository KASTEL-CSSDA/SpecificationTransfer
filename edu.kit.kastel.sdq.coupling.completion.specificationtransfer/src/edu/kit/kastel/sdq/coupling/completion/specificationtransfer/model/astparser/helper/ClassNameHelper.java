package edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.astparser.helper;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitor;
import java.util.ArrayList;
import java.util.List;

/**
 * This Class provides the Functionality to get the Name of the Class (or
 * Interface) from a Compilation-Unit.
 *
 * @author Jonas
 */
public class ClassNameHelper {

    /**
     * Get the Name of the Class (or Interface) from a Compilation-Unit. <br>
     * This uses the ClassNameCollector as a Visitor.
     *
     * @param cu The JavaParser CompilationUnit to get the Name from.
     * @return The Name of the given Class (or Interface).
     */
    public static String getClassName(CompilationUnit cu) {
        if (cu == null) {
            return null;
        }

        //Get Class-Names:
        List<String> classNames = new ArrayList<>();
        VoidVisitor<List<String>> classNameVisitor = new ClassNameCollector();
        classNameVisitor.visit(cu, classNames);

        //Assumption: There is only one class per cu!
        if (classNames.isEmpty()) {
            return null;
        }

        return classNames.get(0);
    }

}
