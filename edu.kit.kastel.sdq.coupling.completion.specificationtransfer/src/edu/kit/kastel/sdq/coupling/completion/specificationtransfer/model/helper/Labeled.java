package edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.helper;

import java.io.Serializable;

/**
 * This is an Interface that provides abstract methods for a labeled class or
 * enum.
 *
 * @author Jonas
 * @param <Lbl> The Comparable-Class that is used for a label.
 */
public interface Labeled<Lbl extends Comparable<Lbl>> extends Serializable {

    public String getLabel();

    /**
     * This Method returns you the corresponding Instance / Enum-Value by the
     * given String-Label or Enum-Value-Name.
     *
     * @param lbl The Label of which you want the Enum of.
     * @return The corresponding Instance / Enum-Value or null if there wasn't a
     * matching one.
     */
    public Labeled<Lbl> getInstanceByLabel(Lbl lbl);

}
