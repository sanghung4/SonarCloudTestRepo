package com.reece.specialpricing.utilities;

import org.junit.Test;

import java.time.Instant;
import java.util.regex.Pattern;

public class DateTimeUtilsTests {
    @Test
    public void getCentralTimeZoneDateStringFromInstant_shouldReturnInTheRightFormat(){
        var result = DateTimeUtils.getCentralTimeZoneDateStringFromInstant(Instant.now());
        Pattern r = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
        assert r.matcher(result).find();
    }
}
