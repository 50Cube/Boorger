package pl.lodz.p.it.boorger.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateFormatter {

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String dateToString(LocalDateTime localDateTime) {
        if(localDateTime != null)
            return localDateTime.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
        else return "";
    }
}
