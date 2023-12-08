package edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.astparser.merge.mapping;

import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.helper.Labeled;

/**
 * An Enum of the Merge-Type. <br>
 * The naming is derived from the merging of databases, ...
 *
 * @author Jonas
 */
public enum MergeMapping implements Labeled<String> {
    Successive("Succ"),
    Name("N");

    public final String label;

    private MergeMapping(String label) {
        this.label = label;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public MergeMapping getInstanceByLabel(String lbl) {
        if (lbl.equalsIgnoreCase((Successive.getLabel()))
                || lbl.equalsIgnoreCase(Successive.toString())) {
            return Successive;
        } else if (lbl.equalsIgnoreCase((Name.getLabel()))
                || lbl.equalsIgnoreCase(Name.toString())) {
            return Name;
        } else {
            return null;
        }
    }
}
