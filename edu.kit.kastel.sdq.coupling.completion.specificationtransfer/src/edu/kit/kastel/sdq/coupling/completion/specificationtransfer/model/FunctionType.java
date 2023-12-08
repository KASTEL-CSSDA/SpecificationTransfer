package edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model;

import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.helper.Labeled;

/**
 * This enum lists all the fundamental functions (possible tasks, executions,
 * operations, ...) that this program is able to offer and perform. <br>
 *
 * @author Jonas
 */
public enum FunctionType implements Labeled<String> {

    Merge("Merge");

    public final String label;

    private FunctionType(String label) {
        this.label = label;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public FunctionType getInstanceByLabel(String lbl) {
     if (lbl.equalsIgnoreCase((Merge.getLabel()))
                || lbl.equalsIgnoreCase(Merge.toString())) {
            return Merge;
        } else {
            return null;
        }
    }
}
