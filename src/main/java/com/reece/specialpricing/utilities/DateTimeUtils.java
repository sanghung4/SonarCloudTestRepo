package com.reece.specialpricing.utilities;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {
    public static String getCentralTimeZoneDateStringFromInstant(Instant instant){
        return DateTimeFormatter
                .ofPattern("yyyy-MM-dd")
                .withZone(ZoneId.of("US/Central"))
                .format(instant);
    }
}
