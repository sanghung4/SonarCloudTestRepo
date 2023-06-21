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
 * Object representing the CR Aging Data
 */
@Schema(description = "Object representing the CR Aging Data")

public class CRAgingInfo {
  @JsonProperty("future")
  private Money future = null;

  @JsonProperty("current")
  private Money current = null;

  @JsonProperty("balance30to61")
  private Money balance30to61 = null;

  @JsonProperty("balance61to90")
  private Money balance61to90 = null;

  @JsonProperty("balance91to120")
  private Money balance91to120 = null;

  @JsonProperty("balanceOver120")
  private Money balanceOver120 = null;

  @JsonProperty("total")
  private Money total = null;

  public CRAgingInfo future(Money future) {
    this.future = future;
    return this;
  }

   /**
   * Get future
   * @return future
  **/
  @Valid
  @Schema(description = "")
  public Money getFuture() {
    return future;
  }

  public void setFuture(Money future) {
    this.future = future;
  }

  public CRAgingInfo current(Money current) {
    this.current = current;
    return this;
  }

   /**
   * Get current
   * @return current
  **/
  @Valid
  @Schema(description = "")
  public Money getCurrent() {
    return current;
  }

  public void setCurrent(Money current) {
    this.current = current;
  }

  public CRAgingInfo balance30to61(Money balance30to61) {
    this.balance30to61 = balance30to61;
    return this;
  }

   /**
   * Get balance30to61
   * @return balance30to61
  **/
  @Valid
  @Schema(description = "")
  public Money getBalance30to61() {
    return balance30to61;
  }

  public void setBalance30to61(Money balance30to61) {
    this.balance30to61 = balance30to61;
  }

  public CRAgingInfo balance61to90(Money balance61to90) {
    this.balance61to90 = balance61to90;
    return this;
  }

   /**
   * Get balance61to90
   * @return balance61to90
  **/
  @Valid
  @Schema(description = "")
  public Money getBalance61to90() {
    return balance61to90;
  }

  public void setBalance61to90(Money balance61to90) {
    this.balance61to90 = balance61to90;
  }

  public CRAgingInfo balance91to120(Money balance91to120) {
    this.balance91to120 = balance91to120;
    return this;
  }

   /**
   * Get balance91to120
   * @return balance91to120
  **/
  @Valid
  @Schema(description = "")
  public Money getBalance91to120() {
    return balance91to120;
  }

  public void setBalance91to120(Money balance91to120) {
    this.balance91to120 = balance91to120;
  }

  public CRAgingInfo balanceOver120(Money balanceOver120) {
    this.balanceOver120 = balanceOver120;
    return this;
  }

   /**
   * Get balanceOver120
   * @return balanceOver120
  **/
  @Valid
  @Schema(description = "")
  public Money getBalanceOver120() {
    return balanceOver120;
  }

  public void setBalanceOver120(Money balanceOver120) {
    this.balanceOver120 = balanceOver120;
  }

  public CRAgingInfo total(Money total) {
    this.total = total;
    return this;
  }

   /**
   * Get total
   * @return total
  **/
  @Valid
  @Schema(description = "")
  public Money getTotal() {
    return total;
  }

  public void setTotal(Money total) {
    this.total = total;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CRAgingInfo crAgingInfo = (CRAgingInfo) o;
    return Objects.equals(this.future, crAgingInfo.future) &&
        Objects.equals(this.current, crAgingInfo.current) &&
        Objects.equals(this.balance30to61, crAgingInfo.balance30to61) &&
        Objects.equals(this.balance61to90, crAgingInfo.balance61to90) &&
        Objects.equals(this.balance91to120, crAgingInfo.balance91to120) &&
        Objects.equals(this.balanceOver120, crAgingInfo.balanceOver120) &&
        Objects.equals(this.total, crAgingInfo.total);
  }

  @Override
  public int hashCode() {
    return Objects.hash(future, current, balance30to61, balance61to90, balance91to120, balanceOver120, total);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CRAgingInfo {\n");
    
    sb.append("    future: ").append(toIndentedString(future)).append("\n");
    sb.append("    current: ").append(toIndentedString(current)).append("\n");
    sb.append("    balance30to61: ").append(toIndentedString(balance30to61)).append("\n");
    sb.append("    balance61to90: ").append(toIndentedString(balance61to90)).append("\n");
    sb.append("    balance91to120: ").append(toIndentedString(balance91to120)).append("\n");
    sb.append("    balanceOver120: ").append(toIndentedString(balanceOver120)).append("\n");
    sb.append("    total: ").append(toIndentedString(total)).append("\n");
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
