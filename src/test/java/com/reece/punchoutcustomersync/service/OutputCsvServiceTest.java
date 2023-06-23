package com.reece.punchoutcustomersync.service;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.reece.punchoutcustomersync.dto.OutputCsvRecord;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OutputCsvServiceTest {

  @InjectMocks
  private OutputCsvService subject;

  @Test
  public void testParse() throws Exception {
    // given
    String content = Files.readString(Paths.get("./src/test/resources/test-output-csv.csv"));
    String encoded = Base64.getEncoder().encodeToString(content.getBytes());

    // when
    List<OutputCsvRecord> results = subject.parse(encoded);

    // then
    assertThat(results.size(), equalTo(1));

    OutputCsvRecord one = results.get(0);
    assertThat(one.getProductName(), equalTo("Test Product 05"));
    assertThat(one.getProductDescription(), equalTo("Test Product 05 Description"));
    assertThat(one.getImageFullsize(), equalTo("https://www.reece.com/static/media/logo.18178d36.svg"));
    assertThat(one.getImageThumb(), equalTo("https://www.reece.com/static/media/logo.18178d36.svg"));
    assertThat(one.getProductPrice().doubleValue(), equalTo(0.01D));
    assertThat(one.getListPrice().doubleValue(), equalTo(0.02D));
    assertThat(one.getPartNumber(), equalTo("TEST-PART-05"));
    assertThat(one.getUnitOfMeasure(), equalTo("FT"));
    assertThat(one.getManufacturer(), equalTo("Test Manufacturer"));
    assertThat(one.getManufacturerPartNumber(), equalTo("TEST-MFN-05"));
    assertThat(one.getCategoryLevel1Name(), equalTo("Test Category 1"));
    assertThat(one.getCategoryLevel2Name(), equalTo("Test Category 2"));
    assertThat(one.getCategoryLevel3Name(), equalTo("Test Category 3"));
    assertThat(one.getUnspsc(), equalTo("TEST-unspsc-05"));
    assertThat(one.getDeliveryInDays(), equalTo(7));
    assertThat(one.getBuyerId(), equalTo("TEST-01"));

  }

  @Test
  public void testToBigDecimal() {
    assertThat(subject.toBigDecimal(""), equalTo(null));
    assertThat(subject.toBigDecimal(" "), equalTo(null));
    assertThat(subject.toBigDecimal(null), equalTo(null));
    assertThat(subject.toBigDecimal("alpha"), equalTo(null));
    assertThat(subject.toBigDecimal("1.23").doubleValue(), equalTo(1.23D));
  }

  @Test
  public void testToInteger() {
    assertThat(subject.toInteger(""), equalTo(null));
    assertThat(subject.toInteger(" "), equalTo(null));
    assertThat(subject.toInteger(null), equalTo(null));
    assertThat(subject.toInteger("alpha"), equalTo(null));
    assertThat(subject.toInteger("23"), equalTo(23));
  }

}
