package com.reece.platform.mincron.utilities;

import com.reece.platform.mincron.exceptions.MincronException;
import org.springframework.http.HttpStatus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Class for formatting Mincron data.
 */
public class MincronDataFormatting {

    /**
     * Converts a date to a format that can readily be used by the front-end or Mincron server.
     */
    public static String convertDateFormat(
            String actualDate, String currentDateFormat, String desiredDateFormat) {

        boolean isMonthMissingLeadingZero = currentDateFormat.indexOf('M') == 0 && actualDate.indexOf('0') == 0;

        if (isMonthMissingLeadingZero) {
            actualDate = "0" + actualDate;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(currentDateFormat);

        Date date;
        try {
            date = sdf.parse(actualDate);
        } catch (ParseException e) {
            throw new MincronException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        sdf.applyPattern(desiredDateFormat);
        String formattedDate = sdf.format(date);

        return formattedDate;
    }

    public static String formatDate(String day, String month, String year){
        if (year.trim().length() == 2) {
            year = "20" + year.trim();
        } else if (year.trim().length() == 1) {
            year = "200" + year.trim();
        }
        if (month.trim().length() == 1) {
            month = "0" + month.trim();
        }
        if (day.trim().length() == 1) {
            day = "0" + day.trim();
        }
        LocalDate dt = LocalDate.parse(year + "-" + month + "-" + day);

        return DateTimeFormatter.ofPattern("MM/dd/YYYY").format(dt);
    }
}
