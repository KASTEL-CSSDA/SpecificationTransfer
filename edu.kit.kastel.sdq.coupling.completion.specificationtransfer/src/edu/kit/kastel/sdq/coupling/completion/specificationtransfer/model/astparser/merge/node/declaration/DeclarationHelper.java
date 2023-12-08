package edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.astparser.merge.node.declaration;

import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.astparser.merge.node.NodeHelper;
import com.github.javaparser.ast.body.BodyDeclaration;

/**
 * This is an abstract Class, that defines some methods (for example a getter)
 * to handle Declarations and check if they match, ... <br>
 * The JavaParser has multiple different Declaration so this Class has possibly
 * multiple Sub-Classes for all those Declarations (Callable/Method/Constructor,
 * Field, ...)
 *
 * @author Jonas
 * @param <Declaration> The Declaration that is Used (Methods, Constructor, ...)
 */
public abstract class DeclarationHelper<Declaration extends BodyDeclaration<Declaration>> extends NodeHelper<Declaration> {

    /*
        No own Methods here, because provided by the Super-Class and implemented by the Sub-Classes.
        This is only used as a separation beween Declarations and Fields/... Nodes.
     */
}
