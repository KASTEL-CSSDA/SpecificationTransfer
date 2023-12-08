package edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.astparser.merge.node.declaration.callable;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import java.util.List;

/**
 * This DeclarationHelper works with ConstructorDeclarations.
 *
 * @author Jonas
 */
public class ConstructorDeclarationHelper extends CallableDeclarationHelper<ConstructorDeclaration> {

    @Override
    public List<ConstructorDeclaration> getNodes(CompilationUnit cu) {
        if (cu == null) {
            return null;
        }

        List<ConstructorDeclaration> constructors = cu.findAll(ConstructorDeclaration.class);
        return constructors;
    }

}
