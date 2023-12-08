package edu.kit.kastel.sdq.coupling.completion.specificationtransfer.controller.resourcebundle;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * This Class handles the ResourceBundle Accesses, ...
 *
 * @author Jonas
 */
public class ResourceBundleController {

    /**
     * The list of from the Programm Supported Locales / Languages. <br>
     * Remember: This List has to be adapted whenever new languages are
     * supported.
     */
    private final static List<Locale> supportedLanguages = Arrays.asList(Locale.ENGLISH, Locale.GERMAN);

    /**
     * Check if the given Locale is valid. <br>
     * This is done by checking for null, and if the Locale is contained in the
     * supportedLanguages-List.
     *
     * @param locale The given locale to check.
     * @return true if the given Locale is not null and supported by the
     * program; false otherwise.
     */
    public static boolean checkLocale(Locale locale) {
        return locale != null && supportedLanguages.contains(locale);
    }

    /**
     * Get the Resource-Bundle for all the texts, corresponding to the given
     * Language-Locale. <br>
     * The Locales of this Project are stored at:
     * src/main/resources/resourcebundles/<LanguageLocale>/properties/<PropertyName>
     * with <LanguageLocale> = The Locale defining the Language. (For example
     * en, de, ...)
     *
     * @param locale The locale that defines the language to use and therefore
     * which resourcebundle to load. It has to be supported by this Program,
     * which is checked by containment of the provided supportedLanguages-List.
     * @param propertyName The String of the Name of the Property, which also
     * the Name of the resourcebundle at the location.
     * @return The resourceBundle with the given language, defined in the
     * locale. Or null, if the locale is not supported or the propertyName was
     * not existant.
     */
    public static ResourceBundle getPropertyResourceBundle(Locale locale, String propertyName) {
        if (!checkLocale(locale)) {
            System.err.println("The given Locale is invalid!");
            return null;
        }

        ResourceBundle resourceBundle = null;
        try {
            resourceBundle = ResourceBundle.getBundle(
                    "resourcebundles." + locale.getLanguage() + ".properties." + propertyName
            );
        } catch (MissingResourceException ex) {
            System.err.println("The ResourceBundle coudn't be found!");
        }

        return resourceBundle;
    }

    //Getter:
    public static List<Locale> getSupportedLanguages() {
        return supportedLanguages;
    }
}
