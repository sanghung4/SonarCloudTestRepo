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
import com.reece.platform.eclipse.model.generated.Money;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.*;
import javax.validation.Valid;
/**
 * Object representing an Eclipse PayableGlPosting.
 */
@Schema(description = "Object representing an Eclipse PayableGlPosting.")

public class PayableDeduction {
  @JsonProperty("updateKey")
  private String updateKey = null;

  @JsonProperty("id")
  private String id = null;

  @JsonProperty("deductionAmount")
  private Money deductionAmount = null;

  @JsonProperty("deductionReason")
  private String deductionReason = null;

  public PayableDeduction updateKey(String updateKey) {
    this.updateKey = updateKey;
    return this;
  }

   /**
   * Update key to handle concurrency during updates within Eclipse
   * @return updateKey
  **/
  @Schema(description = "Update key to handle concurrency during updates within Eclipse")
  public String getUpdateKey() {
    return updateKey;
  }

  public void setUpdateKey(String updateKey) {
    this.updateKey = updateKey;
  }

  public PayableDeduction id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Eclipse ID for the record
   * @return id
  **/
  @Schema(description = "Eclipse ID for the record")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public PayableDeduction deductionAmount(Money deductionAmount) {
    this.deductionAmount = deductionAmount;
    return this;
  }

   /**
   * Get deductionAmount
   * @return deductionAmount
  **/
  @Valid
  @Schema(description = "")
  public Money getDeductionAmount() {
    return deductionAmount;
  }

  public void setDeductionAmount(Money deductionAmount) {
    this.deductionAmount = deductionAmount;
  }

  public PayableDeduction deductionReason(String deductionReason) {
    this.deductionReason = deductionReason;
    return this;
  }

   /**
   * Desc: The PayableDeduction DeductionReason  File: LEDGER  Attr: 17,x
   * @return deductionReason
  **/
  @Schema(description = "Desc: The PayableDeduction DeductionReason  File: LEDGER  Attr: 17,x")
  public String getDeductionReason() {
    return deductionReason;
  }

  public void setDeductionReason(String deductionReason) {
    this.deductionReason = deductionReason;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PayableDeduction payableDeduction = (PayableDeduction) o;
    return Objects.equals(this.updateKey, payableDeduction.updateKey) &&
        Objects.equals(this.id, payableDeduction.id) &&
        Objects.equals(this.deductionAmount, payableDeduction.deductionAmount) &&
        Objects.equals(this.deductionReason, payableDeduction.deductionReason);
  }

  @Override
  public int hashCode() {
    return Objects.hash(updateKey, id, deductionAmount, deductionReason);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PayableDeduction {\n");
    
    sb.append("    updateKey: ").append(toIndentedString(updateKey)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    deductionAmount: ").append(toIndentedString(deductionAmount)).append("\n");
    sb.append("    deductionReason: ").append(toIndentedString(deductionReason)).append("\n");
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
