package com.shirwa.simplistic_rss;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.DateTimeParser;

import java.util.Locale;

public class Utils {

    static final DateTimeFormatter FORMATTER_DEFAULT;
    static final DateTimeFormatter FORMATTER_US;

    static {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .append(null, new DateTimeParser[]{
                        DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").getParser(),
                        DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").getParser(),
                        DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ").getParser(),
                        DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ").getParser(),
                        DateTimeFormat.forPattern("EEE, d MMM yy HH:mm:ss z").getParser(),
                        DateTimeFormat.forPattern("EEE, d MMM yy HH:mm z").getParser(),
                        DateTimeFormat.forPattern("EEE, d MMM yyyy HH:mm:ss Z").getParser(),
                        DateTimeFormat.forPattern("EEE, d MMM yyyy HH:mm:ss z").getParser(),
                        DateTimeFormat.forPattern("EEE, d MMM yyyy HH:mm z").getParser(),
                        DateTimeFormat.forPattern("EEE d MMM yy HH:mm:ss z").getParser(),
                        DateTimeFormat.forPattern("EEE d MMM yy HH:mm z").getParser(),
                        DateTimeFormat.forPattern("EEE d MMM yyyy HH:mm:ss Z").getParser(),
                        DateTimeFormat.forPattern("EEE d MMM yyyy HH:mm:ss z").getParser(),
                        DateTimeFormat.forPattern("EEE d MMM yyyy HH:mm z").getParser(),
                        DateTimeFormat.forPattern("d MMM yy HH:mm z").getParser(),
                        DateTimeFormat.forPattern("d MMM yy HH:mm:ss z").getParser(),
                        DateTimeFormat.forPattern("d MMM yyyy HH:mm z").getParser(),
                        DateTimeFormat.forPattern("d MMM yyyy HH:mm:ss z").getParser(),
                }).toFormatter();
        FORMATTER_US = formatter.withLocale(Locale.US);
        FORMATTER_DEFAULT = formatter;
    }

    static DateTime parseDate(String date) {
        try {
            return FORMATTER_US.parseDateTime(date);
        } catch (IllegalArgumentException e) {
            return FORMATTER_DEFAULT.parseDateTime(date);
        }
    }
}