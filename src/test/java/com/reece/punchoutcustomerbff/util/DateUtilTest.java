package com.reece.punchoutcustomerbff.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.sql.Timestamp;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DateUtilTest {

  @BeforeEach
  public void before() {
    new DateUtil();
  }

  @Test
  public void testAllTheDates() {
    String input = "2023-05-17T05:00:00.000+0000";

    Date output = DateUtil.toDate(input);

    String output2 = DateUtil.fromDate(new Timestamp(output.getTime()));

    assertThat(output2, equalTo(input));

    String output3 = DateUtil.fromDate(output);
    assertThat(output3, equalTo(input));

    Date nullDate = null;
    assertThat(DateUtil.fromDate(nullDate), equalTo(null));

    assertThat(DateUtil.toDate(null), equalTo(null));

    assertThat(DateUtil.formatDate(nullDate, ""), equalTo(null));

    assertThat(DateUtil.formatDate(output, "YYYY"), equalTo("2023"));

  }


}
