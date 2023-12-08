package edu.kit.kastel.sdq.coupling.completion.specificationtransfer.properties;

import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.astparser.merge.node.MergeType;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.model.astparser.merge.mapping.MergeMapping;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.properties.constraints.PropertyConstraint;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.properties.propertyelements.Properties;
import edu.kit.kastel.sdq.coupling.completion.specificationtransfer.properties.propertyelements.PropertyValue;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Local
 */
public class MergeProperties extends Properties {

    @SuppressWarnings("unchecked")
    public MergeProperties() {
        super("MergeProperties");

        //Add all Properties of this ISAJAM-Properties.
        //Remember: You have to add the Key-string into an Separat properties-File so the GUI can show the Name.
        LinkedHashMap<String, PropertyValue> mergeProperties = new LinkedHashMap<>();

        mergeProperties.put("MergeType", new PropertyValue<>(MergeType.class, MergeType.Right));

        mergeProperties.put("MergeMapping", new PropertyValue<>(MergeMapping.class, MergeMapping.Name));

        this.getProperties().putAll(mergeProperties);

        this.initConstraints();
    }

    public MergeProperties(MergeType mergeType, MergeMapping mergeMapping, Boolean showMergedFiles, Boolean saveMergedFiles) {
        this();

        this.setProperty("MergeType", new PropertyValue<>(MergeType.class, mergeType));
        this.setProperty("MergeMapping", new PropertyValue<>(MergeMapping.class, mergeMapping));
    }

    @Override
    protected List<String> initConstraints() {
        //Initialize the List for possible Errors:
        List<String> propNamesFailedConstr = new ArrayList<>();

        //Clear the Constraints.
        this.getConstraints().clear();

        Map<String, PropertyConstraint> isajamPropertiesConstraints = new LinkedHashMap<>();

        this.getConstraints().putAll(isajamPropertiesConstraints);

        return propNamesFailedConstr;
    }

}
