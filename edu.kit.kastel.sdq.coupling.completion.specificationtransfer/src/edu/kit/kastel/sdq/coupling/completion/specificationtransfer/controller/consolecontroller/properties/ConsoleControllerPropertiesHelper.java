package edu.kit.kastel.sdq.coupling.completion.specificationtransfer.controller.consolecontroller.properties;

import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.properties.propertyelements.EmptyClass;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.properties.propertyelements.Properties;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.properties.propertyelements.PropertyValue;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;


/**
 * This Class contains some Helper, static Methods, ... that will be called at
 * the actual Controllers (ConsoleControllerProperties).
 *
 * @author Jonas
 */
public class ConsoleControllerPropertiesHelper {

    /**
     * Get a Map of Property-Key to the Console-Output question for the given
     * Properties.<br>
     * This will access the ResourceBundle for the given property.
     *
     * @param properties The Properties to show.
     * @param locale The current Locale (for the used language at the GUI).
     * @return Map of all Property-Keys (Names) to their Console-Text, stored at
     * the corresponding ResourceBundle, that will be asked to the User to fill
     * in the Value.
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> getConsoleElementFromProperties(Properties properties, Locale locale) {
        //The Map of all Property-Keys (Names) to their Console-Text that will be asked to the User to fill in the Value.
        Map<String, String> mapConsoleElements = new HashMap<>();

        //Check for Null.
        if (properties != null) {
            //Get the ResourceBundle of this Property by the name.
            ResourceBundle bundle = ResourceBundle.getBundle(
                    "resourcebundles." + locale.getLanguage() + ".properties." + properties.getPropName()
            );

            //Add all the Labels and corresponding Textboxes from the Properties.
            for (String propertyKey : properties.getPropertyKeys()) {
                PropertyValue<?> propVal = properties.getPropertyValue(propertyKey);

                if (propVal == null) {
                    continue;
                }

                //Only show the PropVals that really should be shown to the User.
                if (propVal.isShow()) {
                    String strPropText = bundle.getString(propertyKey) + ": "
                            + "[" + propVal.getType().getSimpleName() + "]";

                    strPropText += " (";
                    if (!propVal.getType().isEnum()) {
                        strPropText += propVal.getValue().toString();
                    } else { //Is an Enum
                        for (int i = 0; i < propVal.getType().getEnumConstants().length; i++) {
                            if (i != 0) {
                                strPropText += ", ";
                            }

                            Object curEnum = propVal.getType().getEnumConstants()[i];

                            if (propVal.getValue().equals(curEnum)) {
                                strPropText += ">";
                            }

                            strPropText += curEnum.toString();
                        }
                    }
                    strPropText += ")";

                    mapConsoleElements.put(
                            propertyKey,
                            strPropText
                    );
                }
            }
        }

        return mapConsoleElements;
    }

    /**
     * Get a Map of Property-Key to the Console-Output ToolTipp for the given
     * Properties.<br>
     * This will access the ResourceBundle for the given property. <br>
     * Remember the Formatting: tt + PropertyKey (for example "ttTest").
     *
     * @param properties The Properties to get the tooltipp from.
     * @param locale The current Locale (for the used language at the GUI).
     * @return Map of all Property-Keys (Names) to their ToolTipp-Text, stored
     * at the corresponding ResourceBundle.
     */
    public static Map<String, String> getConsoleToolTippFromProperties(Properties properties, Locale locale) {
        Map<String, String> mapConsoleToolTipps = new HashMap<>();

        if (properties != null) {
            //Get the ResourceBundle of this Property by the name.
            ResourceBundle bundle = ResourceBundle.getBundle(
                    "resourcebundles." + locale.getLanguage() + ".properties." + properties.getPropName()
            );

            //Add all the Labels and corresponding ToolTipps from the Properties.
            for (String propertyKey : properties.getPropertyKeys()) {
                PropertyValue<?> propVal = properties.getPropertyValue(propertyKey);

                if (propVal == null) {
                    continue;
                }

                String ttProp = "tt" + propertyKey;
                if (bundle.containsKey(ttProp)) {
                    mapConsoleToolTipps.put(
                            propertyKey,
                            bundle.getString(ttProp)
                    );
                }
            }
        }

        return mapConsoleToolTipps;
    }

    /**
     * Fill in the given Properties with the values from the given Map of
     * Property-Key and User-Input from the Console.
     *
     * @param <P> The Properties-Class.
     * @param properties the Properties to fill.
     * @param mapPropertyValue The Map between the Property-Key (String) and the
     * given user Input/Value (String).
     * @return A Pair of the filled Properties with the Values from the
     * Console-Input, and a List of Strings representing the errors that
     * occured.
     */
    public static <P extends Properties> Pair<P, List<String>> fillPropertiesWithValues(P properties, Map<String, String> mapPropertyValue) {
        //TODO: Add Error Messages everywhere.

        //Make a new Property from the Same Class as the given and fill this with the new Values.
        P filledProperties = properties;

        //Remember:This value is overwritten each time a Property-Value is set to just get the Errors of the last state.
        List<String> setPropErrors = new ArrayList<>();
        List<String> propErrors = new ArrayList<>();

        //Fill the Properties with the set Values of the User.
        for (String propertyKey : filledProperties.getPropertyKeys()) {
            String controlInput = mapPropertyValue.get(propertyKey);

            if (controlInput != null && !controlInput.isEmpty()) {
                //Check which Type the current Variable is to add the
                Class<?> propType = filledProperties.getPropertyValue(propertyKey).getType();

                if (propType.isAssignableFrom(EmptyClass.class)) {
                    //It should only be Used as an Headline. => Noting to do here.
                } else if (propType.isAssignableFrom(Boolean.class)) {
                    //ToDo: Check for 0/1, t/f, ... Writing (?)
                    setPropErrors = filledProperties.setProperty(
                            propertyKey,
                            new PropertyValue<>(
                                    Boolean.class,
                                    Boolean.parseBoolean(controlInput)
                            )
                    );
                } else if (propType.isEnum()) {
                    //Make an additional check for Labeled-Enums, to utilize that too
//                    if (!Labeled.class.isAssignableFrom(propType)) {
                    try {
                        //It is a regular Enum ~> Use String
                        //ToDo: Code-Smell!
                        //Remember that the String has to be parsed correctly by the input method above...
                        Enum enumVal = Enum.valueOf((Class<Enum>) propType, controlInput);

                        setPropErrors = filledProperties.setProperty(
                                propertyKey,
                                new PropertyValue(
                                        Enum.class,
                                        enumVal
                                )
                        );
                    } catch (IllegalArgumentException ex) {
                        //The given value was not part of the enums Values...
                        System.err.println("Error: "
                                + "The given Input: \"" + controlInput + "\""
                                + " is not part of the Enum \"" + (Class<Enum>) propType + "\"."
                                + " The Value coudn't be set.");
                        //Set this Error to the List as well:
                        propErrors.add(propertyKey);
                    }
                    //ToDo: Add for Locales.
//                    } else {
//                        //It is Labeled ~> Use Also Labels not only the Name
//                        setPropErrors = filledProperties.setProperty(
//                                propertyKey,
//                                new PropertyValue(
//                                        Enum.class,
//                                        //ToDo: Code-Smell!
//                                        //Remember that the String has to be parsed correctly by the input method above...
//                                        Labeled.class.cast(propType).getInstanceByLabel(controlInput)
//                                //((Labeled) propType).getInstanceByLabel(controlInput)
//                                )
//                        );
//                    }
                } else {
                    //Only for Presentation. TODO: @QS
                    if (propType.isAssignableFrom(Integer.class)) {
                        setPropErrors = filledProperties.setProperty(
                                propertyKey,
                                new PropertyValue<>(
                                        Integer.class,
                                        Integer.parseInt(controlInput)
                                )
                        );
                    } else if (propType.isAssignableFrom(Double.class)) {
                        setPropErrors = filledProperties.setProperty(
                                propertyKey,
                                new PropertyValue<>(
                                        Double.class,
                                        Double.parseDouble(controlInput)
                                )
                        );
                    } else {
                        //Use it as a simple String-Text.
                        setPropErrors = filledProperties.setProperty(
                                propertyKey,
                                new PropertyValue<>(
                                        String.class,
                                        controlInput
                                )
                        );
                    }
                }
            }
        }

        setPropErrors.addAll(propErrors);

        return new Pair<>(filledProperties, setPropErrors);
    }

    /**
     * This Method checks if there were any Errors by filling the Properties
     * from the User-Console-Input.<br>
     * This was outsourced from the Classes & Methods that uses the Properties
     * to maintain some overview.
     *
     * @param fillPropertiesWithValues The Result of the
     * fillPropertiesWithValues-Method.
     * @return False If there occured an Error at the Properties. (= if the
     * Error-String was empty). True if there was no Error.
     */
    public static Boolean checkForPropertiesErrors(Pair<Properties, List<String>> fillPropertiesWithValues) {
        //Only the Errors are important here.
        List<String> strArrPropErrors = fillPropertiesWithValues.getValue();

        //Check if the Filling returned any Errors.
        if (strArrPropErrors.size() > 0) {
            //Generate the String to show.
            String strPropErrors = "";
            for (String curPropError : strArrPropErrors) {
                strPropErrors += "- " + curPropError + "\n";
            }

            System.err.println(
                    "Error at Processing the specified Properties.\n"
                    + "There was an Error while Processing the given Properties.\n"
                    + "Some Constraints were not fullfilled.\n"
                    + "Please check the Following:\n"
                    + strPropErrors
            );

            //There was at least one Error => False.
            return false;
        } else {
            System.out.println("Properties were filled with the specified Values ...");

            //No Error occured => ture.
            return true;
        }
    }

}
