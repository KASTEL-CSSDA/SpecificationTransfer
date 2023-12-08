package edu.kit.kastel.sdq.coupling.completion.specificationtransfer.properties.propertyelements;

import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.properties.constraints.PropertyConstraint;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The Properies-Class combines all Properties (by their ID and their Values)
 * and the optional Constraints to one Class.
 *
 * @author Jonas
 */
public class Properties implements Serializable {

    /**
     * This String is the Name of the corresponding ResourceBundle-File. These
     * connects the different Key-Strings to the actual Names on the GUI.
     */
    final String propName;

    /**
     * The Map that contains all the PropertyValues and an String ID as a key.
     * <br>
     * The PropertyValues can contain different Types of the Generic Type T so
     * do not set any.
     */
    private LinkedHashMap<String, PropertyValue> properties;

    /**
     * This are the Constraints for the given Properties. <br>
     * This is only optional. <br>
     * It is a Map of Property-Key (The String-Name of the Property) and a
     * Constraint for that Property-Value.
     */
    private Map<String, PropertyConstraint> constraints;

    //Constructor:
    public Properties(String name) {
        this.propName = name;
        this.properties = new LinkedHashMap<>();
        this.constraints = new HashMap<>();
    }

    public Properties(String name, Map<String, PropertyValue> properties) {
        this(name);
        this.properties.putAll(properties);
    }

    public Properties(String name, Map<String, PropertyValue> properties, Map<String, PropertyConstraint> constraints) {
        this(name, properties);
        this.constraints.putAll(constraints);
    }

    /**
     * Overwrite in SubClasses.
     *
     * @return Return the List of Strings (Similar to Check-Constraints if some
     * given Parameters are wrong and the Constraints Cannot be set in the First
     * Place. Pass these on like at the Constraints-Check.
     */
    protected List<String> initConstraints() {
        //Nothing to do here. => Overwrite in SubClasses.
        return new ArrayList<>();
    }

    /**
     * Check if all Property-Constraints are fullfilled.
     *
     * @return The List of all Property-Names that did not satisfy the
     * constraint. This list can be empty if all constraints were fullfilled.
     */
    public List<String> initAndCheckAllConstraints() {
        List<String> propNamesFailedConstr = new ArrayList<>(initConstraints());

        for (Map.Entry<String, PropertyConstraint> entry : this.constraints.entrySet()) {
            String curPropName = entry.getKey();
            PropertyConstraint curPropConstr = entry.getValue();

            boolean curConstrCheck = curPropConstr.checkPropertyForConstraint(
                    this.properties.get(curPropName)
            );

            if (!curConstrCheck) {
                propNamesFailedConstr.add(curPropName);
            }
        }

        return propNamesFailedConstr;
    }

    //Getter & Setter:
    public String getPropName() {
        return this.propName;
    }

    public List<String> getPropertyKeys() {
        return new ArrayList<>(this.properties.keySet());
    }

    public PropertyValue getPropertyValue(String key) {
        return this.properties.get(key);
    }

    /**
     * Set the Value of the Property.
     *
     * @param key The String of the Property to set.
     * @param value The Value to set the Property to.
     * @return The List of all Property-Names that did not satisfy the
     * constraint. This list can be empty if all constraints were fullfilled.
     */
    @SuppressWarnings("unchecked")
    public List<String> setProperty(String key, PropertyValue value) {
        PropertyValue propVal = this.properties.get(key);
        if (propVal != null) {
            propVal.setValue(value.getValue());
        } else {
            this.properties.put(key, value);
        }

        //Update the Constraints & Check all Constraints because of interconectivity.
        return this.initAndCheckAllConstraints();
    }

    public LinkedHashMap<String, PropertyValue> getProperties() {
        return properties;
    }

    /**
     * Set the properties.
     *
     * @param properties The Properties to set.
     * @return The List of Key's (Names) that do not fullfill the Costraints.
     * Eather by being unable to set because of an Error at
     * Constraint-Construction or wrong Values.
     */
    public List<String> setProperties(LinkedHashMap<String, PropertyValue> properties) {
        if (properties == null) {
            this.properties = new LinkedHashMap<>();
        }

        this.properties.clear();
        this.properties.putAll(properties);

        //Update the Constraints.
        return this.initAndCheckAllConstraints();
    }

    public Map<String, PropertyConstraint> getConstraints() {
        return constraints;
    }

    public PropertyConstraint getConstraintValue(String key) {
        return this.constraints.get(key);
    }

    public void setConstraints(Map<String, PropertyConstraint> constraints) {
        this.constraints = constraints;
    }

    @Override
    public String toString() {
        String strProperties = "";

        strProperties += this.propName + "\n";
        strProperties += this.properties.toString() + "\n";
        strProperties += this.constraints.toString() + "\n";

        return strProperties;
    }

}
