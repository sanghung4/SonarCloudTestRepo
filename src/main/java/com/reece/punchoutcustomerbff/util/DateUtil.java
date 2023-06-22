package com.reece.punchoutcustomerbff.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility for dealing with dates
 * @author john.valentino
 */
@Slf4j
public class DateUtil {

  public static final String ISO = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
  public static final String GMT = "GMT";


  public static Timestamp toTimestamp(String iso) {
    return new Timestamp(DateUtil.toDate(iso).getTime());
  }

  public static Date toDate(String iso) {
    return DateUtil.toDate(iso, null);
  }
  public static Date toDate(String iso, String timeZone) {
    if (timeZone == null) {
      timeZone = GMT;
    }
    if (iso == null) {
      return null;
    }
    DateFormat df1 = new SimpleDateFormat(ISO, Locale.ENGLISH);
    df1.setTimeZone(TimeZone.getTimeZone(timeZone));

    Date result = null;
    try {
      result = df1.parse(iso);
    } catch (ParseException e) {
      log.error(iso, e);
    }
    return result;
  }

  public static String fromDate(Timestamp input) {
    if (input == null) {
      return null;
    }
    return DateUtil.fromDate(new Date(input.getTime()), null);
  }

  public static String fromDate(Date date) {
    return DateUtil.fromDate(date, null);
  }

  public static String fromDate(Date date, String timeZone) {
    if (date == null) {
      return null;
    }
    if (timeZone == null) {
      timeZone = GMT;
    }
    DateFormat dateFormat = new SimpleDateFormat(ISO, Locale.ENGLISH);
    dateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
    return dateFormat.format(date);
  }

}
