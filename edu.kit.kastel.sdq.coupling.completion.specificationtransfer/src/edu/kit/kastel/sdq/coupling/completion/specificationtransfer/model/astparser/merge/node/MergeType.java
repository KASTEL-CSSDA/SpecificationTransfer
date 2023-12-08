package edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.astparser.merge.node;

import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.helper.Labeled;

/**
 * An Enum of the Merge-Type. <br>
 * The naming is derived from the merging of databases, ...
 *
 * @author Jonas
 */
public enum MergeType implements Labeled<String> {
    //Invalid
    None("N"),
    //Merge:
    Left("L"),
    Right("R"),
    Inner("I"),
    Outer("O");

    public final String label;

    private MergeType(String label) {
        this.label = label;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public MergeType getInstanceByLabel(String lbl) {
        if (lbl.equalsIgnoreCase((Left.getLabel()))
                || lbl.equalsIgnoreCase(Left.toString())) {
            return Left;
        } else if (lbl.equalsIgnoreCase((Right.getLabel()))
                || lbl.equalsIgnoreCase(Right.toString())) {
            return Right;
        } else if (lbl.equalsIgnoreCase((Inner.getLabel()))
                || lbl.equalsIgnoreCase(Inner.toString())) {
            return Inner;
        } else if (lbl.equalsIgnoreCase((Outer.getLabel()))
                || lbl.equalsIgnoreCase(Outer.toString())) {
            return Outer;
        } else {
            return None;
        }
    }
}
