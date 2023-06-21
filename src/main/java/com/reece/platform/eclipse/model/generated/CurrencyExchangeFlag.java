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
 *  List of valid Currency exchange flags which may be entered in the \&quot;CurrencyExchangeFlag\&quot; field.  &#x27;1 - RecalculatedExchange&#x27;  &#x27;2 - HistoricalExchange&#x27;  &#x27;3 - AverageExchange&#x27;
 */
public enum CurrencyExchangeFlag {
  RECALCULATEDEXCHANGE("RecalculatedExchange"),
  HISTORICALEXCHANGE("HistoricalExchange"),
  AVERAGEEXCHANGE("AverageExchange");

  private String value;

  CurrencyExchangeFlag(String value) {
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
  public static CurrencyExchangeFlag fromValue(String text) {
    for (CurrencyExchangeFlag b : CurrencyExchangeFlag.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
