package org.devrisby.c195.utils;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/** Helper class for code related to internationalization */
public class LanguageUtils {

    /** Returns resource bundle matching the current system locale */
    public static ResourceBundle getResourceBundle() {
        try {
            return ResourceBundle.getBundle("fx", Locale.getDefault());
        } catch (MissingResourceException e) {
            System.out.println("Cannot find resourcebundle!\n" + e.getClassName());
            System.exit(1);
        }

        return null;
    }
}
