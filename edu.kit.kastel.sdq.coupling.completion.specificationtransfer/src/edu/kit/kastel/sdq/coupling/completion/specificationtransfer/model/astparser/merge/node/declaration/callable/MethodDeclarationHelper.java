package edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.astparser.merge.node.declaration.callable;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import java.util.List;

/**
 * This DeclarationHelper works with MethodDeclarations.
 *
 * @author Jonas
 */
public class MethodDeclarationHelper extends CallableDeclarationHelper<MethodDeclaration> {

    @Override
    public List<MethodDeclaration> getNodes(CompilationUnit cu) {
        if (cu == null) {
            return null;
        }

        List<MethodDeclaration> methods = cu.findAll(MethodDeclaration.class);
        return methods;
    }

}
