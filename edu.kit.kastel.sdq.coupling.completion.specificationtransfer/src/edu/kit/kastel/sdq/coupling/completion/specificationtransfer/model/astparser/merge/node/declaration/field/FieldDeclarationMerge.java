package edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.astparser.merge.node.declaration.field;

import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.astparser.merge.node.declaration.DeclarationMerge;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.type.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This is a DeclarationMerge for Method-Declarations. <br>
 * It provides additional Methods for merging the Return-Type and some changed
 * Behaviours for Method-Body (that is optional here).
 *
 * @author Jonas
 */
public class FieldDeclarationMerge extends DeclarationMerge<FieldDeclaration> {

    public FieldDeclarationMerge(FieldDeclarationHelper fieldDeclarationHelper) {
        super(fieldDeclarationHelper);
    }

    @Override
    protected FieldDeclaration merge(FieldDeclaration leftField, FieldDeclaration rightField) {
        if (leftField == null || rightField == null) {
            return null;
        }

        FieldDeclaration mergedField = new FieldDeclaration();
        mergedField.setModifiers(
                mergeFieldModifiers(leftField, rightField)
        );
        mergedField.setVariables(
                mergeFieldVariables(leftField, rightField)
        );

        return mergedField;
    }

    private NodeList<Modifier> mergeFieldModifiers(FieldDeclaration leftDeclaration, FieldDeclaration rightDeclaration) {
        assert (leftDeclaration != null && rightDeclaration != null);

        NodeList<Modifier> leftModifiers = leftDeclaration.getModifiers();
        NodeList<Modifier> rightModifiers = rightDeclaration.getModifiers();

        //ToDo...
        return leftModifiers;
    }

    private NodeList<VariableDeclarator> mergeFieldVariables(FieldDeclaration leftField, FieldDeclaration rightField) {
        assert leftField != null && rightField != null;

        NodeList<VariableDeclarator> leftVariables = leftField.getVariables();
        NodeList<VariableDeclarator> rightVariables = rightField.getVariables();

        List<VariableDeclarator> leftVariablesCache = new ArrayList<>(leftVariables);
        List<VariableDeclarator> rightVariablesCache = new ArrayList<>(rightVariables);

        //ToDo: At this point the MergeType is not accessable
        //Assumtion: ~> Do an Outer-Merge as default!
        if (leftVariables.isEmpty()) {
            if (rightVariables.isNonEmpty()) {
                return rightVariables;
            } else {
                //Both are Empty.
                return new NodeList<>();
            }
        } else {
            if (rightVariables.isEmpty()) {
                return leftVariables;
            } else {
                //Both are not empty... ~> Merge
                //Assumption: No wrong Variables (doubles, same Name different Types, ...)!
                NodeList<VariableDeclarator> mergedVariables = new NodeList<>();

                for (VariableDeclarator leftVariable : leftVariables) {
                    for (VariableDeclarator rightVariable : rightVariables) {
                        if (((FieldDeclarationHelper) this.helper).isMatchingVariable(
                                leftVariable, rightVariable
                        )) {
                            VariableDeclarator mergedVariable = new VariableDeclarator();

                            mergedVariable.setComment(
                                    mergeVariableComment(leftVariable, rightVariable)
                            );
                            //ToDo: Annotations aren't included for VariableDeclarators...
                            //ToDo: Modifiers are non-existent in this Class ?
                            mergedVariable.setType(
                                    mergeVariableType(leftVariable, rightVariable)
                            );
                            mergedVariable.setName(
                                    mergeVariableName(leftVariable, rightVariable)
                            );

                            mergedVariables.add(mergedVariable);

                            //Update Caches:
                            leftVariablesCache.remove(leftVariable);
                            rightVariablesCache.remove(rightVariable);
                        }
                    }
                }

                //Add in the left out Variables from both sides (See Assumtion Outer-Merge)
                mergedVariables.addAll(leftVariablesCache);
                mergedVariables.addAll(rightVariablesCache);

                return mergedVariables;
            }
        }
    }

    private Comment mergeVariableComment(VariableDeclarator leftVariable, VariableDeclarator rightVariable) {
        assert leftVariable != null && rightVariable != null;
        //ToDo: Double Code from "mergeComment"...

        //Assumption: Only one has a comment!
        Optional<Comment> leftComment = leftVariable.getComment();
        Optional<Comment> rightComment = rightVariable.getComment();
        //ToDo: The Comments of Variables aren't showing up here! ~> No Merging...
        List<Comment> leftComments = leftVariable.getAllContainedComments();
        List<Comment> rightComments = rightVariable.getAllContainedComments();

        if (leftComment.isPresent()) {
            if (rightComment.isPresent()) {
                //Both are present ~> Merge comments.
                //ToDo...
//                Comment mergedComment = leftComment.get();
//                mergedComment.addOrphanComment(rightComment.get());
//                return mergedComment;
                return leftComment.get();
            } else {
                return leftComment.get();
            }
        } else {
            if (rightComment.isPresent()) {
                return rightComment.get();
            } else {
                //None Present ~> ToDo: Empty Comment
                return null;
            }
        }
    }

    private Type mergeVariableType(VariableDeclarator leftVariable, VariableDeclarator rightVariable) {
        assert leftVariable != null && rightVariable != null;

        Type leftType = leftVariable.getType();
        Type rightType = rightVariable.getType();
        //ToDo: ...

        return leftType;
    }

    private SimpleName mergeVariableName(VariableDeclarator leftVariable, VariableDeclarator rightVariable) {
        assert leftVariable != null && rightVariable != null;

        SimpleName leftName = leftVariable.getName();
        SimpleName rightName = rightVariable.getName();
        //ToDo: ...

        return leftName;
    }

}
