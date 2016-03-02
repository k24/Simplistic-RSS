package com.shirwa.simplistic_rss;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.DateTimeParser;

public class Utils {

    static final DateTimeFormatter FORMATTER = new DateTimeFormatterBuilder()
            .append(null, new DateTimeParser[]{
                    DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").getParser(),
                    DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").getParser(),
                    DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ").getParser(),
                    DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ").getParser(),
                    DateTimeFormat.forPattern("EEE, d MMM yy HH:mm:ss z").getParser(),
                    DateTimeFormat.forPattern("EEE, d MMM yy HH:mm z").getParser(),
                    DateTimeFormat.forPattern("EEE, d MMM yyyy HH:mm:ss z").getParser(),
                    DateTimeFormat.forPattern("EEE, d MMM yyyy HH:mm:ss Z").getParser(),
                    DateTimeFormat.forPattern("EEE, d MMM yyyy HH:mm z").getParser(),
                    DateTimeFormat.forPattern("EEE d MMM yy HH:mm:ss z").getParser(),
                    DateTimeFormat.forPattern("EEE d MMM yy HH:mm z").getParser(),
                    DateTimeFormat.forPattern("EEE d MMM yyyy HH:mm:ss z").getParser(),
                    DateTimeFormat.forPattern("EEE d MMM yyyy HH:mm:ss Z").getParser(),
                    DateTimeFormat.forPattern("EEE d MMM yyyy HH:mm z").getParser(),
                    DateTimeFormat.forPattern("d MMM yy HH:mm z").getParser(),
                    DateTimeFormat.forPattern("d MMM yy HH:mm:ss z").getParser(),
                    DateTimeFormat.forPattern("d MMM yyyy HH:mm z").getParser(),
                    DateTimeFormat.forPattern("d MMM yyyy HH:mm:ss z").getParser(),
            }).toFormatter();

    static DateTime parseDate(String date) {
        return FORMATTER.parseDateTime(date);
    }
}