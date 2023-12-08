package edu.kit.kastel.sdq.coupling.completion.specificationtransfer.properties.propertyelements;

import java.io.Serializable;
import java.util.Objects;

/**
 * The Property-Value.
 *
 * @author Jonas
 * @param <T> The Generic Type of this Property-Value.
 */
public class PropertyValue<T extends Serializable> implements Serializable {

    /**
     * The Class of the Value. It is from Type T. <br>
     * This has to be passed at the Constructor, because of the Generic-Type it
     * cannot be get on runtime.
     */
    private Class<T> type;

    /**
     * The Value of the Property. This is of the Generic Type T.
     */
    private T value;

    /**
     * This determines if this Property should be shown at the GUI or not.
     */
    private boolean show;

    /**
     * The Constructor of this PropertyValue. <br>
     * Sets the given values and does set the shwo attribute to the default
     * true.
     *
     * @param type The Class this Value is from.
     * @param value The Value of this Property.
     */
    public PropertyValue(Class<T> type, T value) {
        this.type = type;
        this.value = value;

        //Set the Show to true as Default.
        this.show = true;
    }

    public PropertyValue(Class<T> type, T value, boolean show) {
        this.type = type;
        this.value = value;
        this.show = show;
    }

    //Getter & Setter
    public Class<T> getType() {
        return type;
    }

    public void setType(Class<T> type) {
        this.type = type;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    @Override
    public boolean equals(Object obj) {
        //Null-Checker
        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final PropertyValue<?> other = (PropertyValue<?>) obj;

        if (!this.value.equals(other.getValue())) {
            return false;
        }
        if (!this.type.equals(other.getType())) {
            return false;
        }
        if (this.show != other.isShow()) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.type);
        hash = 23 * hash + Objects.hashCode(this.value);
        hash = 23 * hash + (this.show ? 1 : 0);
        return hash;
    }

    @Override
    public String toString() {
        String strPropertyValue = "";

        if (this.value != null) {
            strPropertyValue += this.value.toString();
        } else {
            strPropertyValue += "null";
        }

        strPropertyValue += "(" + this.type.getName() + ")";
        strPropertyValue += "-" + this.show;

        return strPropertyValue;
    }

}
