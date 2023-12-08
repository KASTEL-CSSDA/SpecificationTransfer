package edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.astparser.merge.node.declaration.field;

import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.astparser.merge.node.declaration.DeclarationHelper;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import java.util.List;

/**
 * This is a abstract Class that defines some methods to handle Declarations
 * (for example a getter) and check if they match, ...
 *
 * @author Jonas
 * @param <Declaration> The Declaration that is Used (Methods, Constructor, ...)
 */
public class FieldDeclarationHelper extends DeclarationHelper<FieldDeclaration> {

    @Override
    public List<FieldDeclaration> getNodes(CompilationUnit cu) {
        if (cu == null) {
            return null;
        }

        List<FieldDeclaration> fields = cu.findAll(FieldDeclaration.class);
        return fields;
    }

    /**
     * Check if the given Declaration match each other. <br>
     * This is done by checking their Variable's Types and Names to match.
     *
     * @see isMatchingDeclarationVariables & isMatchingVariable.
     *
     * @param leftDeclaration The first Declaration to check the matching of.
     * @param rightDeclaration The second Declaration to check the matching of.
     * @return true if the Declarations match (have the same Variables - so same
     * Type and Name) and false if not.
     */
    @Override
    public boolean isMatching(FieldDeclaration leftDeclaration, FieldDeclaration rightDeclaration) {
        return isMatchingDeclarationVariables(leftDeclaration, rightDeclaration);
    }

    private boolean isMatchingDeclarationVariables(FieldDeclaration leftDeclaration, FieldDeclaration rightDeclaration) {
        if (leftDeclaration == null || rightDeclaration == null) {
            return false;
        }

        NodeList<VariableDeclarator> leftVariables = leftDeclaration.getVariables();
        NodeList<VariableDeclarator> rightVariables = rightDeclaration.getVariables();

        if (leftVariables.size() == rightVariables.size()) {
            for (int i = 0; i < leftVariables.size(); i++) {
                if (!isMatchingVariable(leftVariables.get(i), rightVariables.get(i))) {
                    //Found un-matching Variables
                    return false;
                }
            }

            //Checked all Variables ~> Found all matched
            return true;
        }
        return false;
    }

    public boolean isMatchingVariable(VariableDeclarator leftVariable, VariableDeclarator rightVaraible) {
        if (leftVariable == null || rightVaraible == null) {
            return false;
        }

        if (leftVariable.equals(rightVaraible)) {
            return true;
        }
        //Variables can have Annotations, those should not be checked.
        if (leftVariable.getType().equals(rightVaraible.getType())
                && leftVariable.getNameAsString().equals(rightVaraible.getNameAsString())) {
            return true;
        }

        return false;
    }

}
