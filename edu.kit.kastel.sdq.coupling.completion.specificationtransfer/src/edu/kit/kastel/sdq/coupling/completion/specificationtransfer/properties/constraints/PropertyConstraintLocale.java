package edu.kit.kastel.sdq.coupling.completion.specificationtransfer.properties.constraints;

import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.properties.propertyelements.PropertyValue;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.PatternSyntaxException;

/**
 * A Constraint for String Property-Values. <br>
 * It checks the String lengths and Regular-Expression-Patterns.
 *
 * @author Jonas
 */
public class PropertyConstraintLocale extends PropertyConstraint {

    private List<Locale> supportedLocales;

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
    public PropertyConstraintLocale(List<Locale> supportedLocales) {
        this.supportedLocales = new ArrayList<>(supportedLocales);
    }

    //Check Constraints:
    @Override
    public boolean checkPropertyForConstraint(PropertyValue<?> propVal) {
        if (propVal == null) {
            return false;
        }

        if (propVal.getType().equals(Locale.class)) {
            Locale locale = (Locale) propVal.getValue();
            return locale != null && supportedLocales.contains(locale);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "Supported Languages: " + this.supportedLocales.toString();
    }

}
