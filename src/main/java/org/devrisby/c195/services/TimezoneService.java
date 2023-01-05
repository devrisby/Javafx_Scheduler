package org.devrisby.c195.services;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.ResourceBundle;

public class LocationService {

    // Times are in EST timezone
    private static ZonedDateTime businessStartHours = ZonedDateTime.of(LocalDateTime.of(LocalDate.now(), LocalTime.of(8,0)), ZoneId.of("US/Eastern"));
    private static ZonedDateTime businessEndHours = ZonedDateTime.of(LocalDateTime.of(LocalDate.now(), LocalTime.of(22,0)), ZoneId.of("US/Eastern"));

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

    public static boolean isWithinBusinessHours(ZonedDateTime time) {
        System.out.println("Business start: " + businessStartHours.toLocalTime().toString());
        System.out.println("Business end: " + businessEndHours.toLocalTime().toString());
        return false;
    }

}
