package edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.astparser.merge.node.declaration.callable;

import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.astparser.merge.node.declaration.DeclarationHelper;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.body.Parameter;

/**
 * This is an abstract Declaration-Helper for Callable-Declarations (Method,
 * Constructors, ...).
 *
 * @author Jonas
 * @param <Declaration> The Callable-Declaration that is Used (Method,
 * Constructor, ...)
 */
public abstract class CallableDeclarationHelper<Declaration extends CallableDeclaration<Declaration>> extends DeclarationHelper<Declaration> {

    /**
     * Check if the given Callable-Declarations match each other. <br>
     * This is done by checking their Names and Parameters to match.
     *
     * @see isMatchingDeclarationName & isMatchingDeclarationParameters.
     *
     * @param leftDeclaration The first Declaration to check the matching of.
     * @param rightDeclaration The second Declaration to check the matching of.
     * @return true if the Declarations match (have the same Name and
     * Parameters) and false if not.
     */
    @Override
    public boolean isMatching(Declaration leftDeclaration, Declaration rightDeclaration) {
        return isMatchingDeclarationName(leftDeclaration, rightDeclaration)
                && isMatchingDeclarationParameters(leftDeclaration, rightDeclaration);
    }

    /**
     * Check if the given Declaration's Names match. <br>
     * This is done by checking the Strings of their Names.
     *
     * @param leftDeclaration The first Declaration to check the matching of.
     * @param rightDeclaration The second Declaration to check the matching of.
     * @return true if the Declaration's Names match and false if not.
     */
    public boolean isMatchingDeclarationName(Declaration leftDeclaration, Declaration rightDeclaration) {
        if (leftDeclaration == null || rightDeclaration == null) {
            return false;
        }

        return leftDeclaration.getNameAsString().equals(rightDeclaration.getNameAsString());
        //getName return SimpleName which doesn't implement equal correctly at the moment.
    }

    /**
     * Check if the given Declaration's Parameters match. <br>
     * This is done by checking each Parameters and compare their Types. (Not
     * their Names)
     *
     * @param leftDeclaration The first Declaration to check the matching of.
     * @param rightDeclaration The second Declaration to check the matching of.
     * @return true if the Declaration's Parameters all match and false if not,
     * and there is at least one missmatch.
     */
    public boolean isMatchingDeclarationParameters(Declaration leftDeclaration, Declaration rightDeclaration) {
        if (leftDeclaration == null || rightDeclaration == null) {
            return false;
        }

        NodeList<Parameter> leftParameters = leftDeclaration.getParameters();
        NodeList<Parameter> rightParameters = rightDeclaration.getParameters();

        if (leftParameters.size() == rightParameters.size()) {
            for (int i = 0; i < leftParameters.size(); i++) {
                if (!isMatchingParameter(leftParameters.get(i), rightParameters.get(i))) {
                    //Found un-matching Parameters
                    return false;
                }
            }

            //Checked all Parameters ~> Found all matched
            return true;
        }
        return false;
    }

    private boolean isMatchingParameter(Parameter leftParameter, Parameter rightParameter) {
        if (leftParameter == null || rightParameter == null) {
            return false;
        }

        if (leftParameter.equals(rightParameter)) {
            return true;
        }
        //Parameters can have Annotations, those should not be checked.
        if (leftParameter.getType().equals(rightParameter.getType())) {
            return true;
        }

        return false;
    }

}
