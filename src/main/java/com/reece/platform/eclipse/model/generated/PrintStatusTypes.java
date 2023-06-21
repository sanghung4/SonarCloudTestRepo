/*
 * Eclipse API Developer Documentation
 * This documentation provides a list of API endpoints provided in this release as well as examples for using the various API endpoints
 *
 * OpenAPI spec version: 9.1.1
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package com.reece.platform.eclipse.model.generated;

import java.util.Objects;
import java.util.Arrays;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.*;
import javax.validation.Valid;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * These are Valid List of Print Status Types used in Ship Via  Print Status Field.  N - No Print  P - Detailed Invoice Preview Report  Q - Invoice Preview Queue  B - Batch Print  M - Flag to be Placed on Manifest
 */
public enum PrintStatusTypes {
  NOPRINT("NoPrint"),
  DETAILEDINVOICEPREVIEWREPORT("DetailedInvoicePreviewReport"),
  INVOICEPREVIEWQUEUE("InvoicePreviewQueue"),
  BATCHPRINT("BatchPrint"),
  FLAGTOBEPLACEDONMANIFEST("FlagToBePlacedOnManifest"),
  NONE("None");

  private String value;

  PrintStatusTypes(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static PrintStatusTypes fromValue(String text) {
    for (PrintStatusTypes b : PrintStatusTypes.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
