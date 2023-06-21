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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.*;
import javax.validation.Valid;
/**
 * Object representing an Eclipse PriceLinePriceSheet.
 */
@Schema(description = "Object representing an Eclipse PriceLinePriceSheet.")

public class PriceLinePriceSheet {
  @JsonProperty("priceSheetId")
  private String priceSheetId = null;

  @JsonProperty("discountClass")
  private String discountClass = null;

  public PriceLinePriceSheet priceSheetId(String priceSheetId) {
    this.priceSheetId = priceSheetId;
    return this;
  }

   /**
   * Desc: The PriceLinePriceSheet PriceSheetId  File: PRICE.LINE  Attr: 21,x
   * @return priceSheetId
  **/
  @Schema(description = "Desc: The PriceLinePriceSheet PriceSheetId  File: PRICE.LINE  Attr: 21,x")
  public String getPriceSheetId() {
    return priceSheetId;
  }

  public void setPriceSheetId(String priceSheetId) {
    this.priceSheetId = priceSheetId;
  }

  public PriceLinePriceSheet discountClass(String discountClass) {
    this.discountClass = discountClass;
    return this;
  }

   /**
   * Desc: The PriceLinePriceSheet DiscountClass  File: PRICE.LINE  Attr: 21,x
   * @return discountClass
  **/
  @Schema(description = "Desc: The PriceLinePriceSheet DiscountClass  File: PRICE.LINE  Attr: 21,x")
  public String getDiscountClass() {
    return discountClass;
  }

  public void setDiscountClass(String discountClass) {
    this.discountClass = discountClass;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PriceLinePriceSheet priceLinePriceSheet = (PriceLinePriceSheet) o;
    return Objects.equals(this.priceSheetId, priceLinePriceSheet.priceSheetId) &&
        Objects.equals(this.discountClass, priceLinePriceSheet.discountClass);
  }

  @Override
  public int hashCode() {
    return Objects.hash(priceSheetId, discountClass);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PriceLinePriceSheet {\n");
    
    sb.append("    priceSheetId: ").append(toIndentedString(priceSheetId)).append("\n");
    sb.append("    discountClass: ").append(toIndentedString(discountClass)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}
