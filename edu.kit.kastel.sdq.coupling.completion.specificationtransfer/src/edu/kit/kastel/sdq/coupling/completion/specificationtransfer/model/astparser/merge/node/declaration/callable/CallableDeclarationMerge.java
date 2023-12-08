package edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.astparser.merge.node.declaration.callable;

import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.astparser.merge.node.declaration.DeclarationMerge;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.body.Parameter;

/**
 * This is an abstract Class for Callable-Declaration-Merge. It provides some
 * Methods that are being used for Merging Declarations. The Sub-Classes will
 * then define some Additional Methods for Merging. (For example for Merging the
 * Return Type, which is only present for Merging Methods and not Constructor
 * Declarations).
 *
 * @author Jonas
 * @param <Declaration> The Declaration-Class that is used.
 */
public abstract class CallableDeclarationMerge<Declaration extends CallableDeclaration<Declaration>> extends DeclarationMerge<Declaration> {

    //Constructor:
    public CallableDeclarationMerge(CallableDeclarationHelper<Declaration> declarationHelper) {
        super(declarationHelper);
    }

    //Methods that can be used for each:
    protected NodeList<Parameter> mergeDeclarationParameter(Declaration leftDeclaration, Declaration rightDeclaration) {
        assert (leftDeclaration != null && rightDeclaration != null);

        //Parameters can have Annotations too ~> Do not overwrite those.
        NodeList<Parameter> leftParameter = leftDeclaration.getParameters();
        NodeList<Parameter> rightParameter = rightDeclaration.getParameters();

        NodeList<Parameter> parameter = new NodeList<>();

        //Already checked for equal parameters.
        for (int i = 0; i < leftParameter.size(); i++) {
            //Assumption: Only one Parameter of left/right has Annotations.
            if (leftParameter.get(i).getAnnotations().isNonEmpty()) {
                parameter.add(leftParameter.get(i));
            } else { //if (rightParameter.get(i).getAnnotations().isNonEmpty()) {
                //Either right has Annotations, or none of them has ~> Just use right
                parameter.add(rightParameter.get(i));
            }
        }

        return parameter;
    }

    protected NodeList<Modifier> mergeDeclarationModifier(Declaration leftDeclaration, Declaration rightDeclaration) {
        assert (leftDeclaration != null && rightDeclaration != null);

        NodeList<Modifier> leftModifiers = leftDeclaration.getModifiers();
        NodeList<Modifier> rightModifiers = rightDeclaration.getModifiers();

        //ToDo...
        return leftModifiers;
    }

}
