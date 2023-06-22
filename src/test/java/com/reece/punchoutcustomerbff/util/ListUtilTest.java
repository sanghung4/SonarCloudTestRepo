package com.reece.punchoutcustomerbff.util;

import java.util.List;
import org.junit.jupiter.api.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ListUtilTest {

  @Test
  public void testSplitInto() {
    // given
    new ListUtilTest();
    List<String> inputs = List.of("a", "b", "c", "d", "e");

    // when
    List<List<String>> outputs = ListUtil.splitInto(inputs, 2, String.class);

    // then
    assertThat(outputs.size(), equalTo(3));

    // and
    assertThat(outputs.get(0).size(), equalTo(2));
    assertThat(outputs.get(0).get(0), equalTo("a"));
    assertThat(outputs.get(0).get(1), equalTo("b"));

    // and
    assertThat(outputs.get(1).size(), equalTo(2));
    assertThat(outputs.get(1).get(0), equalTo("c"));
    assertThat(outputs.get(1).get(1), equalTo("d"));

    // and
    assertThat(outputs.get(2).size(), equalTo(1));
    assertThat(outputs.get(2).get(0), equalTo("e"));

  }

}
