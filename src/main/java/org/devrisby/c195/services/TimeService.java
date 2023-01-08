package org.devrisby.c195.services;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.ResourceBundle;

public class TimeService {

    // Times are in EST timezone
    private static ZonedDateTime businessStartHours = ZonedDateTime.of(LocalDateTime.of(LocalDate.now(), LocalTime.of(8,0)), ZoneId.of("US/Eastern"));
    private static ZonedDateTime businessEndHours = ZonedDateTime.of(LocalDateTime.of(LocalDate.now(), LocalTime.of(22,0)), ZoneId.of("US/Eastern"));

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

    public static ZonedDateTime convertToEasternTime(ZonedDateTime time) {
        if(time.getZone().equals(ZoneId.of("US/Eastern"))) {
            System.out.println("Is eastern already");
            return time;
        }

        return time.withZoneSameInstant(ZoneId.of("US/Eastern"));
    }

    public boolean inbetweenStartAndEnd(Instant targetStart, Instant targetEnd, Instant startTime, Instant endTime) {
        return targetStart.isAfter(startTime) && targetEnd.isBefore(endTime);
    }

    public boolean equalsStartOrEnd(Instant time, Instant startTime, Instant endTime) {
        return timeIsEqualTo(time, startTime) || timeIsEqualTo(time, endTime);
    }

    public boolean timeIsEqualTo(Instant time, Instant targetTime) {
        return time.equals(targetTime);
    }

    public static boolean isWithinBusinessHours(LocalTime localTime) {
        return localTime.equals(businessStartHours.toLocalTime()) || localTime.equals(businessEndHours.toLocalTime()) ||
                (localTime.isAfter(businessStartHours.toLocalTime()) && localTime.isBefore(businessEndHours.toLocalTime()));
    }
}
