package com.reece.specialpricing.utilities;

import org.junit.Test;

import java.time.Instant;
import java.util.Objects;
import java.util.regex.Pattern;

import static com.reece.specialpricing.utilities.StringUtils.removePrefix;

public class StringUtilsTests {
    @Test
    public void removePrefix_succeeedsIfExists(){
        var value = "MSC-123456";
        var result = removePrefix(value, "MSC-");

        assert Objects.equals(result, "123456");
    }

    @Test
    public void removePrefix_succeeedsIfPrefixNotFound(){
        var value = "123456";
        var result = removePrefix(value, "MSC-");

        assert Objects.equals(result, "123456");
    }
}
