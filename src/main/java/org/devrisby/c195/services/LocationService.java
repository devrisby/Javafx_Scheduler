package org.devrisby.c195.services;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.ResourceBundle;

public class LocationService {
    public static String getCurrentCountry() {
        return Locale.getDefault().getDisplayCountry();
    }

    public static ResourceBundle getResourceBundle() {
        return ResourceBundle.getBundle("fx", Locale.getDefault());
    }

    public static String getLocalTime() {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime time = ZonedDateTime.ofInstant(Instant.now(), zoneId);
        return time.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM));
    }

    public static String getLocalTime(ZoneId zoneId) {
        ZonedDateTime time = ZonedDateTime.ofInstant(Instant.now(), zoneId);
        return time.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM));
    }


}
