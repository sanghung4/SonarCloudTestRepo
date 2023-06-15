package com.reece.platform.inventory.util;

import static org.junit.jupiter.api.Assertions.*;

import com.reece.platform.inventory.exception.InvalidDateException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import lombok.val;
import org.junit.jupiter.api.Test;

class DateUtilTest {

    @Test
    void extractDate_endDate_success() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, Calendar.JANUARY, 0, 5, 30, 56);
        Date date = calendar.getTime();
        val endDate = LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault());

        Date result = DateUtil.extractDate(endDate, false);

        calendar.set(2022, Calendar.JANUARY, 1, 0, 0, 0);
        Date expectedDate = calendar.getTime();

        assertEquals(expectedDate.toString(), result.toString());
    }

    @Test
    void extractDate_startDate_success() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, Calendar.JANUARY, 1, 5, 0, 0);
        Date date = calendar.getTime();
        val startDate = LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault());

        Date result = DateUtil.extractDate(startDate, true);

        calendar.set(2022, Calendar.JANUARY, 1, 0, 0, 0);
        Date expectedDate = calendar.getTime();

        assertEquals(expectedDate.toString(), result.toString());
    }

    @Test
    void simpleDateParsing_Test() {
        String incorrectDate = "2023-03-22 14:33:30";

        Date formattedDate = DateUtil.simpleDateParsing(incorrectDate);

        assertNotNull(formattedDate);
    }
}
