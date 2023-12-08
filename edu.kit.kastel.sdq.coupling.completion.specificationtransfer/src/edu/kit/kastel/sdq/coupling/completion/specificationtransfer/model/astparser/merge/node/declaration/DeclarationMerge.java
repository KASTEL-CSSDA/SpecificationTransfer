package edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.astparser.merge.node.declaration;

import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.astparser.merge.node.NodeMerge;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import java.util.HashSet;
import java.util.Set;

/**
 * This is an abstract Class for Declaration-Merge. It provides some Methods
 * that are being used for Merging Declarations. <br>
 * The Sub-Classes will then define some additional Methods for Merging. For
 * merging of different Declaration-Sub-Classes (Body/Field,
 * Callable/Method/Constructor).
 *
 * @author Jonas
 * @param <Declaration> The Declaration-Class that is used.
 */
public abstract class DeclarationMerge<Declaration extends BodyDeclaration<Declaration>> extends NodeMerge<Declaration> {

    //Constructor:
    public DeclarationMerge(DeclarationHelper<Declaration> declarationHelper) {
        super(declarationHelper);
    }

    protected NodeList<AnnotationExpr> mergeDeclarationAnnotation(Declaration leftDeclaration, Declaration rightDeclaration) {
        assert (leftDeclaration != null && rightDeclaration != null);

        NodeList<AnnotationExpr> leftAnnotations = leftDeclaration.getAnnotations();
        NodeList<AnnotationExpr> rightAnnotations = rightDeclaration.getAnnotations();

        //ToDo: Merge Annotations
        //For now just expect that one has annotations and the other doesn't.
        if (leftAnnotations.isEmpty()) {
            if (rightAnnotations.isNonEmpty()) {
                return rightAnnotations;
            } else {
                //Both are Empty.
                return new NodeList<>();
            }
        } else {
            if (rightAnnotations.isEmpty()) {
                return leftAnnotations;
            } else {
                //Both are not empty...
                //ToDo: Merge
                //Use a set to remove duplicates (for example Override)
                Set<AnnotationExpr> mergedAnnotations = new HashSet<>();
                mergedAnnotations.addAll(leftAnnotations);
                mergedAnnotations.addAll(rightAnnotations);

                return new NodeList<>(mergedAnnotations);
            }
        }
    }

}
