package edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.astparser.merge.node.declaration.callable;

import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;

/**
 * This is a DeclarationMerge for Constructor-Declarations. <br>
 * It provides additional Methods for merging it.
 *
 * @author Jonas
 */
public class ConstructorDeclarationMerge extends CallableDeclarationMerge<ConstructorDeclaration> {

    //Constructor:
    public ConstructorDeclarationMerge(ConstructorDeclarationHelper constructorDeclarationHelper) {
        super(constructorDeclarationHelper);
    }

    @Override
    protected ConstructorDeclaration merge(ConstructorDeclaration leftConstructor, ConstructorDeclaration rightConstructor) {
        if (leftConstructor == null || rightConstructor == null) {
            return null;
        }

        ConstructorDeclaration mergedConstructor = new ConstructorDeclaration();

        //Assumption: Same Name and Parameters, but disjunct Comment, Body ?
        if (!this.helper.isMatching(leftConstructor, rightConstructor)) {
            throw new IllegalArgumentException("The given Constructor-Declarations do not match, and therefore cannot be merged!");
        }
        mergedConstructor.setName(leftConstructor.getName());
        mergedConstructor.setParameters(
                mergeDeclarationParameter(leftConstructor, rightConstructor)
        );

        mergedConstructor.setModifiers(
                mergeDeclarationModifier(leftConstructor, rightConstructor)
        );

        mergedConstructor.setAnnotations(
                mergeDeclarationAnnotation(leftConstructor, rightConstructor)
        );

        mergedConstructor.setComment(
                mergeComment(leftConstructor, rightConstructor)
        );
        //Check about JavaDoc and JavaDocComment

        mergedConstructor.setBody(
                mergeConstructorBody(leftConstructor, rightConstructor)
        );

        return mergedConstructor;
    }

    private static BlockStmt mergeConstructorBody(ConstructorDeclaration leftConstructor, ConstructorDeclaration rightConstructor) {
        assert leftConstructor != null && rightConstructor != null;

        BlockStmt leftBody = leftConstructor.getBody();
        BlockStmt rightBody = rightConstructor.getBody();

        //ToDo...?
        return leftBody;
    }

}
