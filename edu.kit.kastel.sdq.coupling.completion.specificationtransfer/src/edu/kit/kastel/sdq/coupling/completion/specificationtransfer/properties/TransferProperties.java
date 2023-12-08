package edu.kit.kastel.sdq.coupling.completion.specificationtransfer.properties;

import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.controller.resourcebundle.ResourceBundleController;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.FunctionType;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.astparser.merge.SourceCitationType;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.properties.constraints.PropertyConstraint;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.properties.constraints.PropertyConstraintLocale;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.properties.propertyelements.Properties;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.properties.propertyelements.PropertyValue;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * This is the Properties-Class for the Project. It allows the user to define
 * the general settings (properties) of the execution. <br>
 * For example which Function (Operation) should be executed.
 *
 * @author Jonas
 */
public class TransferProperties extends Properties {

    public static final String LANGUAGE_TRANSFER_PROPERTY = "Language";
    public static final String FUNCTION_TYPE_TRANSFER_PROPERTY = "FunctionType";
    public static final String SOURCE_CITATION_TYPE_TRANSFER_PROPERTY = "SourceCitationType";

    public static final String OUTPUT_BASE_DIR_TRANSFER_PROPERTY = "OutputBaseDirectory";

    @SuppressWarnings("unchecked")
    public TransferProperties() {
        super("ISAJAMProperties");

        //Add all Properties of this ISAJAM-Properties.
        //Remember: You have to add the Key-string into an Separate properties-File so the GUI can show the Name.
        LinkedHashMap<String, PropertyValue> properties = new LinkedHashMap<>();

        properties.put(LANGUAGE_TRANSFER_PROPERTY, new PropertyValue<>(Locale.class, Locale.ENGLISH, false));
        //For now Only "en" and "de". And setting Manually.

        //Function
        properties.put(FUNCTION_TYPE_TRANSFER_PROPERTY, new PropertyValue<>(FunctionType.class, FunctionType.Merge));

        //Import
        properties.put(SOURCE_CITATION_TYPE_TRANSFER_PROPERTY, new PropertyValue<>(SourceCitationType.class, SourceCitationType.Directory));

        properties.put(OUTPUT_BASE_DIR_TRANSFER_PROPERTY, new PropertyValue<>(String.class,  "./src/output/dirs/"));

        this.getProperties().putAll(properties);

        this.initConstraints();
    }

    public TransferProperties(Locale language, FunctionType func, SourceCitationType srcCite) {
        this();

        this.setProperty(LANGUAGE_TRANSFER_PROPERTY, new PropertyValue<>(Locale.class, language, false));
        this.setProperty(FUNCTION_TYPE_TRANSFER_PROPERTY, new PropertyValue<>(FunctionType.class, func));
        this.setProperty(SOURCE_CITATION_TYPE_TRANSFER_PROPERTY, new PropertyValue<>(SourceCitationType.class, srcCite));
    }

    @Override
    protected List<String> initConstraints() {
        //Initialize the List for possible Errors:
        List<String> propNamesFailedConstr = new ArrayList<>();

        //Clear the Constraints.
        this.getConstraints().clear();

        Map<String, PropertyConstraint> constraints = new LinkedHashMap<>();

        try {
            constraints.put(
                    LANGUAGE_TRANSFER_PROPERTY,
                    new PropertyConstraintLocale(
                            ResourceBundleController.getSupportedLanguages()
                    )
            );
        } catch (IllegalArgumentException e) {
            propNamesFailedConstr.add(LANGUAGE_TRANSFER_PROPERTY);
        }

        this.getConstraints().putAll(constraints);

        return propNamesFailedConstr;
    }

}
