package edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.astparser.merge.node;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import java.util.List;

/**
 * This is an abstract Class, that defines some methods to handle Nodes. <br>
 * Some of the Methods are: A getter for the corresponding
 * Nodes/Declarations/... ; and Also a method that checks if the two
 * Nodes/Declarations/... are matching. <br>
 * This is overwritten by it's subclasses to handle Declarations, ...
 *
 * @author Jonas
 * @param <N> The Node that is Used (Body/...Declarations)
 */
public abstract class NodeHelper<N extends Node> {

    public abstract List<N> getNodes(CompilationUnit cu);

    /**
     * Check if the given Nodes match each other.
     *
     * @param leftNode The first Node to check the matching of.
     * @param rightNode The second Node to check the matching of.
     * @return true if the Node match and false if not.
     */
    public abstract boolean isMatching(N leftNode, N rightNode);

}
