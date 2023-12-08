package edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.astparser.merge.node.declaration.callable;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.type.Type;
import java.util.Optional;

/**
 * This is a DeclarationMerge for Method-Declarations. <br>
 * It provides additional Methods for merging the Return-Type and some changed
 * Behaviours for Method-Body (that is optional here).
 *
 * @author Jonas
 */
public class MethodDeclarationMerge extends CallableDeclarationMerge<MethodDeclaration> {

    public MethodDeclarationMerge(MethodDeclarationHelper methodDeclarationHelper) {
        super(methodDeclarationHelper);
    }

    @Override
    protected MethodDeclaration merge(MethodDeclaration leftMethod, MethodDeclaration rightMethod) {
        if (leftMethod == null || rightMethod == null) {
            return null;
        }

        MethodDeclaration mergedMethod = new MethodDeclaration();

        //Assumption: Same Name and Parameters, but disjunct Comment, Body ?
        if (!this.helper.isMatching(leftMethod, rightMethod)) {
            throw new IllegalArgumentException("The given Method-Declarations do not match, and therefore cannot be merged!");
        }
        mergedMethod.setName(leftMethod.getName());
        mergedMethod.setParameters(
                mergeDeclarationParameter(leftMethod, rightMethod)
        );

        mergedMethod.setModifiers(
                mergeDeclarationModifier(leftMethod, rightMethod)
        );

        mergedMethod.setType(
                mergeMethodReturnType(leftMethod, rightMethod)
        );

        mergedMethod.setThrownExceptions(
                mergeMethodExceptionsThrown(leftMethod, rightMethod)
        );

        mergedMethod.setAnnotations(
                mergeDeclarationAnnotation(leftMethod, rightMethod)
        );

        mergedMethod.setComment(
                mergeComment(leftMethod, rightMethod)
        );
        //Check about JavaDoc and JavaDocComment

        mergedMethod.setBody(
                mergeMethodBody(leftMethod, rightMethod)
        );

        return mergedMethod;
    }

    private static Type mergeMethodReturnType(MethodDeclaration leftMethod, MethodDeclaration rightMethod) {
        assert leftMethod != null && rightMethod != null;

        Type leftType = leftMethod.getType();
        Type rightType = rightMethod.getType();

        //ToDo...
        return leftType;
    }

    private NodeList<ReferenceType> mergeMethodExceptionsThrown(MethodDeclaration leftMethod, MethodDeclaration rightMethod) {
        assert leftMethod != null && rightMethod != null;

        NodeList<ReferenceType> leftExceptions = leftMethod.getThrownExceptions();
        NodeList<ReferenceType> rightExceptions = rightMethod.getThrownExceptions();

        //ToDo...
        return leftExceptions;
    }

    private static BlockStmt mergeMethodBody(MethodDeclaration leftMethod, MethodDeclaration rightMethod) {
        assert leftMethod != null && rightMethod != null;

        Optional<BlockStmt> leftBody = leftMethod.getBody();
        Optional<BlockStmt> rightBody = rightMethod.getBody();

        if (leftBody.isPresent() && !leftBody.get().isEmpty()) {
            if (rightBody.isPresent() && !rightBody.get().isEmpty()) {
                //Both are present ~> Merge body.
                //ToDo...
                if (leftBody.get().equals(rightBody.get())) {
                    return leftBody.get();
                }

                return null;
            } else {
                return leftBody.get();
            }
        } else {
            if (rightBody.isPresent() && !rightBody.get().isEmpty()) {
                return rightBody.get();
            } else {
                return null;
            }
        }
    }

}
