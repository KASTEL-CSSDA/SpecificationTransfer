package edu.kit.kastel.sdq.coupling.completion.specificationtransfer.properties.constraints;

import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.properties.propertyelements.PropertyValue;

/**
 * The Constraint for Numbers that define a min and max Value.<br>
 * This uses Double-Values to check against the Numbers.
 *
 * @author Jonas
 */
public class PropertyConstraintNumber extends PropertyConstraint {

    //Use Double because of the higher Range of Numberical Values.
    private final Double minVal;
    private final Double maxVal;

    //Constructor:
    /**
     * The Constructor of the PropertyConstraint for Numbers. <br>
     * This Class uses Doubles to cover all Number-Values.
     *
     * @param minVal The minimal-Value for the Constraint.
     * @param maxVal The maximal-Value for the Constraint.
     *
     * @throws IllegalArgumentException If the minVal > maxVal
     */
    public PropertyConstraintNumber(Double minVal, Double maxVal)
            throws IllegalArgumentException {
        //Null is allowed for not using a Constraint at all.
        //Check min Max:
        if (minVal != null && maxVal != null && minVal > maxVal) {
            throw new IllegalArgumentException(
                    "The given Min-Value needs to be smaller, than the given Max-Value!"
            );
        }

        this.minVal = minVal;
        this.maxVal = maxVal;
    }

    //Check Constraints:
    private boolean checkConstraintMinVal(Number num) {
        if (this.minVal != null) {
            return num.doubleValue() >= this.minVal;
        } else {
            //The Constraint is not set. => It is fulfilled by default.
            return true;
        }
    }

    private boolean checkConstraintMaxVal(Number num) {
        if (this.maxVal != null) {
            return num.doubleValue() <= this.maxVal;
        } else {
            //The Constraint is not set. => It is fulfilled by default.
            return true;
        }
    }

    @Override
    public boolean checkPropertyForConstraint(PropertyValue<?> propVal) {
        if (propVal == null) {
            return false;
        }

        if (Number.class.isAssignableFrom(propVal.getType())) {
            //Check the Constraint for the String.
            return this.checkConstraintMinVal((Number) propVal.getValue())
                    && this.checkConstraintMaxVal((Number) propVal.getValue());
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "(" + minVal + ", " + maxVal + ")";
    }

}
