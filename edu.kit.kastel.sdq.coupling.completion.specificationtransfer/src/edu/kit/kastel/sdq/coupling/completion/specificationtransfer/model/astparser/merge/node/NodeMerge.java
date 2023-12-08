package edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.astparser.merge.node;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.comments.Comment;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.util.Pair;

/**
 * This is an abstract Class for Node-Merge. It provides some Methods that are
 * being used for Merging Nodes. <br>
 * The Sub-Classes will then define some additional Methods for Merging. (For
 * Example Declarations or Fields)
 *
 * @author Jonas
 * @param <N> The Node-Class that is used.
 */
public abstract class NodeMerge<N extends Node> {

    /**
     * This Helper provides Methods for getting the Nodes/Declarations/... and
     * checking if two Declarations/... match. <br>
     * It is set via the Constructor to the corresponding Sub-Class needed.
     */
    protected NodeHelper<N> helper;

    //Constructor:
    public NodeMerge(NodeHelper<N> nodeHelper) {
        this.helper = nodeHelper;
    }

    public NodeList<N> merge(MergeType type, CompilationUnit astLeft, CompilationUnit astRight) {
        if (type == null || astLeft == null || astRight == null) {
            return null;
        }

        NodeList<N> mergedNodes = new NodeList<>();

        List<N> left = helper.getNodes(astLeft);
        List<N> right = helper.getNodes(astRight);

        List<Pair<N, N>> declarationMap = getMatchingPairs(left, right);

        for (Pair<N, N> declarationPair : declarationMap) {
            left.remove(declarationPair.getKey());
            right.remove(declarationPair.getValue());

            N mergedDeclaration = merge(declarationPair.getKey(), declarationPair.getValue());
            mergedNodes.add(mergedDeclaration);
        }

        //Add the other un-merged Declarations corresponding to the Merge-Type.
        switch (type) {
            case None:
                return null;
            case Left:
                mergedNodes.addAll(left);
                break;
            case Right:
                mergedNodes.addAll(right);
                break;
            case Inner:
                //All Declarations present in both left and right are already merged.
                break;
            case Outer:
                mergedNodes.addAll(left);
                mergedNodes.addAll(right);
                break;
        }

        return mergedNodes;
    }

    private List<Pair<N, N>> getMatchingPairs(List<N> leftNodes, List<N> rightNodes) {
        assert (leftNodes != null && rightNodes != null);
        //Note: equals is not Overwritten by the MethodDeclaration class

        List<Pair<N, N>> nodesMapping = new ArrayList<>();

        //ToDo: Performance
        for (N leftNode : leftNodes) {
            for (N rightNode : rightNodes) {
                if (helper.isMatching(leftNode, rightNode)) {
                    nodesMapping.add(
                            new Pair<>(leftNode, rightNode)
                    );
                    break;
                }
            }
        }

        return nodesMapping;
    }

    protected abstract N merge(N leftNode, N rightNode);

    protected Comment mergeComment(N leftNode, N rightNode) {
        Optional<Comment> leftComment = leftNode.getComment();
        Optional<Comment> rightComment = rightNode.getComment();

        if (leftComment.isPresent()) {
            if (rightComment.isPresent()) {
                //Both are present ~> Merge comments.
                //ToDo...
//                Comment mergedComment = leftComment.get();
//                mergedComment.addOrphanComment(rightComment.get());
//                return mergedComment;
                return null;
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

}
