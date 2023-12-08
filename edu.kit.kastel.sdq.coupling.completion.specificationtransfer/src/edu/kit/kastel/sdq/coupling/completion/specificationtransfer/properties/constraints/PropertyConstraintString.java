package edu.kit.kastel.sdq.coupling.completion.specificationtransfer.properties.constraints;

import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.properties.propertyelements.PropertyValue;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * A Constraint for String Property-Values. <br>
 * It checks the String lengths and Regular-Expression-Patterns.
 *
 * @author Jonas
 */
public class PropertyConstraintString extends PropertyConstraint {

    private final Integer length;
    private final Pattern regExpPattern;

    //Constructor:
    /**
     * The Constructor of the PropertyConstraint for Numbers. <br>
     * This Class uses Doubles to cover all Number-Values.
     *
     * @param length The length of the String for the Constraint.
     * @param strRegExpPattern The String Representation of the
     * Regular-Expression for the Constraint.
     *
     * @throws IllegalArgumentException If the length < 0.
     * @throws PatternSyntaxException If the given RegExp-String was not a valid
     * RegExp.
     */
    public PropertyConstraintString(Integer length, String strRegExpPattern)
            throws IllegalArgumentException, PatternSyntaxException {
        //Null is allowed for not using a Constraint at all.
        //Check min Max:
        if (length != null && length < 0) {
            throw new IllegalArgumentException(
                    "The given Length needs to be grather, than 0!"
            );
        }

        this.length = length;

        if (strRegExpPattern == null) {
            this.regExpPattern = null;
        } else {
            try {
                this.regExpPattern = Pattern.compile(strRegExpPattern);
            } catch (PatternSyntaxException exp) {
                throw exp;
            }
        }
    }

    //Check Constraints:
    private boolean checkConstraintLength(String str) {
        if (this.length != null) {
            return str.length() == this.length;
        } else {
            //The Constraint is not set. => It is fulfilled by default.
            return true;
        }
    }

    private boolean checkConstraintRegExp(String str) {
        if (this.regExpPattern != null) {
            return regExpPattern.matcher(str).matches();
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

        if (propVal.getType().equals(String.class)) {
            //Check the Constraint for the String.
            return this.checkConstraintLength((String) propVal.getValue())
                    && this.checkConstraintRegExp((String) propVal.getValue());
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        String strReturn = "length=" + this.length;
        strReturn += ", RegExp=";

        if (this.regExpPattern == null) {
            strReturn += "null";
        } else {
            strReturn += this.regExpPattern.pattern();
        }

        return strReturn;
    }

}
