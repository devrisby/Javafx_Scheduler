package org.devrisby.c195.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/** Help class for code related to Time */
public class TimeUtils {

    // Times are in EST timezone
    private static final ZonedDateTime businessStartHours = ZonedDateTime.of(LocalDateTime.of(LocalDate.now(), LocalTime.of(8,0)), ZoneId.of("US/Eastern"));
    private static final ZonedDateTime businessEndHours = ZonedDateTime.of(LocalDateTime.of(LocalDate.now(), LocalTime.of(22,0)), ZoneId.of("US/Eastern"));


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

    public boolean betweenStartAndEnd(Instant targetStart, Instant targetEnd, Instant startTime, Instant endTime) {
        return targetStart.isAfter(startTime) && targetEnd.isBefore(endTime);
    }

    public boolean equalsStartOrEnd(Instant time, Instant startTime, Instant endTime) {
        return timeIsEqualTo(time, startTime) || timeIsEqualTo(time, endTime);
    }

    public boolean timeIsEqualTo(Instant time, Instant targetTime) {
        return time.equals(targetTime);
    }

    public void validateForBusinessHours(Instant start, Instant end) {
        LocalTime startTime = LocalTime.ofInstant(start, ZoneId.systemDefault());
        LocalTime endTime = LocalTime.ofInstant(end, ZoneId.systemDefault());

        boolean isEqualOrGreaterThanBusinessStart = startTime.equals(businessStartHours.toLocalTime()) ||
                startTime.isAfter(businessStartHours.toLocalTime());

        boolean isEqualOrLessThanBusinessEnd = endTime.equals(businessEndHours.toLocalTime()) ||
                endTime.isBefore(businessEndHours.toLocalTime());

        if(!(isEqualOrGreaterThanBusinessStart && isEqualOrLessThanBusinessEnd)) {
            throw new IllegalArgumentException("Times can't be outside of the business hours (8:00 AM - 10:00 PM EST");
        }
    }

    public void validateStartEndTimes(Instant start, Instant end) {
        LocalDateTime startDateTime = LocalDateTime.ofInstant(start, ZoneId.systemDefault());
        LocalDateTime endDateTime = LocalDateTime.ofInstant(end, ZoneId.systemDefault());

        if(startDateTime.isAfter(endDateTime)) {
            throw new IllegalArgumentException("Start time cannot occur after end time!");
        } else if (startDateTime.isEqual(endDateTime)) {
            throw new IllegalArgumentException("Start and end times cannot be the same!");
        }
    }
}
