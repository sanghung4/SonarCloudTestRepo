package com.reece.platform.inventory.util;

import com.reece.platform.inventory.exception.InvalidDateException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    public static final String datePattern = "yyyy-MM-dd HH:mm:ss";

    public static Date extractDate(LocalDate inputDate, boolean isStart) {
        return Date.from(inputDate.plusDays(isStart ? 0 : 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static Date extractDateFromString(String dateInString) {
        LocalDate date = LocalDate.parse(dateInString);
        return Date.from(date.atTime(12, 0).atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date extractDateTimeFromString(String dateInString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern, Locale.ENGLISH);
        LocalDateTime dateTime = LocalDateTime.parse(dateInString, formatter);
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static String formatAsDateString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        return formatter.format(date);
    }

    public static Date simpleDateParsing(String dateInString) {
        var dateFormat = new SimpleDateFormat(datePattern);
        try {
            return dateFormat.parse(dateInString);
        } catch (ParseException ex) {
            throw new InvalidDateException();
        }
    }
}
