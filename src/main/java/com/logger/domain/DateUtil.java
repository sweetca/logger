package com.logger.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateUtil {

    private static final SimpleDateFormat[] SYSTEM_PATTERNS = {
            new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS"), new SimpleDateFormat("dd.MM.yyyy HH:mm:ss,SSS"),
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS")
    };

    public static long detectDate(String date) throws ParseException{
        long timestamp;

        for (SimpleDateFormat format: SYSTEM_PATTERNS) {
            try {
                timestamp = format.parse(date.trim()).getTime();
            } catch (ParseException e) {
                continue;
            }
            return timestamp;
        }

        throw new ParseException("Pattern does not match", 0);
    }
}
