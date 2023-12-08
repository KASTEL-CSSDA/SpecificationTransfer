package edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.astparser.helper;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import java.util.List;

/**
 * This Class is a Visitor that is used at the JavaParser to get the Name of a
 * class or interface.
 *
 * @author Peeradon
 * @see
 * https://stackoverflow.com/questions/65377062/javaparser-how-to-get-classname-in-compilationunit
 */
public class ClassNameCollector extends VoidVisitorAdapter<List<String>> {

    @Override
    public void visit(ClassOrInterfaceDeclaration n, List<String> collector) {
        if (n == null || collector == null) {
            return;
        }

        super.visit(n, collector);
        collector.add(n.getNameAsString());
    }
}
