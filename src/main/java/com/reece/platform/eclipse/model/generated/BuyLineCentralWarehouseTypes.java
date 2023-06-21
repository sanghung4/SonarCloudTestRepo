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
 * The warehouse type determines how your hits are controlled.If       you use a multi-branch network, select one of the warehouse types   T - TopDown  B - BottomUp  if This field is left blank Bottom-Up setting is used.
 */
public enum BuyLineCentralWarehouseTypes {
  TOPDOWN("TopDown"),
  BOTTOMUP("BottomUp"),
  NONE("None");

  private String value;

  BuyLineCentralWarehouseTypes(String value) {
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
  public static BuyLineCentralWarehouseTypes fromValue(String text) {
    for (BuyLineCentralWarehouseTypes b : BuyLineCentralWarehouseTypes.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
