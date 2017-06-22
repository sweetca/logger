package com.logger.domain;

import java.text.ParseException;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateUtil {

    private static final DateTimeFormatter[] SYSTEM_PATTERNS = {
            DateTimeFormat.forPattern("dd.MM.yyyy HH:mm:ss.SSS"), DateTimeFormat.forPattern("dd.MM.yyyy HH:mm:ss,SSS"),
            DateTimeFormat.forPattern("yyyy.MM.dd HH:mm:ss.SSS"), DateTimeFormat.forPattern("yyyy.MM.dd HH:mm:ss,SSS"),

            DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss.SSS"), DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss,SSS"),
            DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS"), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss,SSS"),

            DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss.SSS"), DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss,SSS"),
            DateTimeFormat.forPattern("yyyy/MM/dd HH:mm:ss.SSS"), DateTimeFormat.forPattern("yyyy/MM/dd HH:mm:ss,SSS")
    };

    public static synchronized long detectDate(final String date) throws ParseException{
        long timestamp;

        for (DateTimeFormatter format: SYSTEM_PATTERNS) {

            try {
                timestamp = format.parseDateTime(date.trim()).getMillis();
            } catch (IllegalArgumentException e) {
                continue;
            }

            return timestamp;
        }

        throw new ParseException("Pattern does not match", 0);
    }
}
