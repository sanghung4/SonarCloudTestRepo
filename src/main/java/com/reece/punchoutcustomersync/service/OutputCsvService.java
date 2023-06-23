package com.reece.punchoutcustomersync.service;

import com.jcraft.jsch.ChannelSftp;
import com.opencsv.CSVWriter;
import com.reece.punchoutcustomersync.dto.OutputCsvRecord;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

/**
 * Service used for dealing with the Output CSV file format.
 * @author john.valentino
 */
@Slf4j
@Service
public class OutputCsvService {

  public static final String[] HEADERS = {
      "Product Name",
      "Product Description",
      "Image Fullsize",
      "Image Thumb",
      "Product Price",
      "List Price",
      "Part Number",
      "Unit of Measure",
      "Manufacturer",
      "Manufacturer Part Number",
      "Category Level 1 Name",
      "Category Level 2 Name",
      "Category Level 3 Name",
      "UNSPSC",
      "Delivery In Days",
      "Buyer ID",
  };

  public List<OutputCsvRecord> parse(String encoded) {
    List<OutputCsvRecord> results = new ArrayList<>();

    byte[] decodedBytes = Base64.getDecoder().decode(encoded);
    String decodedString = new String(decodedBytes);

    CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
        .setHeader(HEADERS)
        .setSkipHeaderRecord(true)
        .build();

    Reader in = new StringReader(decodedString);

    Iterable<CSVRecord> records = new ArrayList<>();

    try {
      records = csvFormat.parse(in);
    } catch (IOException e) {
      log.error("Unable to process the following CSV:\n" + decodedString, e);
    }

    for (CSVRecord record : records) {
      if (record.get(0).isEmpty()) {
        continue;
      }
      int i = 0;
      OutputCsvRecord output = OutputCsvRecord.builder()
          .productName(record.get(i++))
          .productDescription(record.get(i++))
          .imageFullsize(record.get(i++))
          .imageThumb(record.get(i++))
          .productPrice(toBigDecimal(record.get(i++))) //
          .listPrice(toBigDecimal(record.get(i++))) //
          .partNumber(record.get(i++))
          .unitOfMeasure(record.get(i++))
          .manufacturer(record.get(i++))
          .manufacturerPartNumber(record.get(i++))
          .categoryLevel1Name(record.get(i++))
          .categoryLevel2Name(record.get(i++))
          .categoryLevel3Name(record.get(i++))
          .unspsc(record.get(i++))
          .deliveryInDays(toInteger(record.get(i++))) //
          .buyerId(record.get(i++))
          .build();
      results.add(output);
    }

    return results;
  }

  public BigDecimal toBigDecimal(String input) {
    if (input == null || input.trim().length() == 0) {
      return null;
    }
    try {
      return BigDecimal.valueOf(Double.parseDouble(input));
    } catch (NumberFormatException e) {
      log.error("Not a valid Big Decimal " + input, e);
    }
    return null;
  }

  public Integer toInteger(String input) {
    if (input == null || input.trim().length() == 0) {
      return null;
    }
    try {
      return Integer.valueOf(input);
    } catch (NumberFormatException e) {
      log.error("Not a valid Integer " + input, e);
    }
    return null;
  }

  public InputStream createCsv(List<String[]> data) throws IOException {
    StringWriter stringWriter = new StringWriter();
    CSVWriter csvWriter = new CSVWriter(stringWriter);
    data.stream().forEach(i -> csvWriter.writeNext(i));
    csvWriter.close();
    String csvData = stringWriter.toString();

    return new ByteArrayInputStream(csvData.getBytes());
  }

}
