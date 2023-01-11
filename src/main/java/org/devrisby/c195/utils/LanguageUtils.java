package org.devrisby.c195.utils;

import java.util.Locale;
import java.util.ResourceBundle;

/** Helper class for code related to internationalization */
public class LanguageUtils {

    /** Returns resource bundle matching the current system locale */
    public static ResourceBundle getResourceBundle() {
        return ResourceBundle.getBundle("fx", Locale.getDefault());
    }
}
