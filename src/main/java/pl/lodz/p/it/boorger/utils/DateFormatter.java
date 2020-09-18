package pl.lodz.p.it.boorger.utils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateFormatter {

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String TIME_FORMAT = "HH:mm";

    public static String dateToString(LocalDateTime localDateTime) {
        if(localDateTime != null)
            return localDateTime.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
        else return "";
    }

    public static LocalDateTime stringToDate(String date) {
        if( date != null)
            return LocalDateTime.parse(date, DateTimeFormatter.ofPattern(DATE_FORMAT));
        else return null;
    }

    public static String timeToString(LocalTime localTime) {
        if(localTime != null)
            return localTime.format(DateTimeFormatter.ofPattern(TIME_FORMAT));
        else return "";
    }

    public static LocalTime stringToTime(String date) {
        if(date != null)
            return LocalTime.parse(date, DateTimeFormatter.ofPattern(TIME_FORMAT));
        else return LocalTime.MIN;
    }
}
