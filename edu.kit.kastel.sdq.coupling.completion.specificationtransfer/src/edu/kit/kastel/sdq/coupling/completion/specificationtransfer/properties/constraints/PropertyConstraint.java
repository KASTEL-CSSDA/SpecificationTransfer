package edu.kit.kastel.sdq.coupling.completion.specificationtransfer.properties.constraints;

import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.properties.propertyelements.PropertyValue;
import java.io.Serializable;

/**
 * The abstract super Class of Constraints for the Properties and their values.
 *
 * @author Jonas
 */
public abstract class PropertyConstraint implements Serializable {

    public abstract boolean checkPropertyForConstraint(PropertyValue<?> propVal);

}
