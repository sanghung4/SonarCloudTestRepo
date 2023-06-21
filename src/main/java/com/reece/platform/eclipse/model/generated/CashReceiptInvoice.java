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
import com.reece.platform.eclipse.model.generated.AccrualRegisterAppliedPaymentGlPosting;
import com.reece.platform.eclipse.model.generated.Money;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.*;
import javax.validation.Valid;
/**
 * Object representing an Eclipse CashReceiptInvoice.
 */
@Schema(description = "Object representing an Eclipse CashReceiptInvoice.")

public class CashReceiptInvoice {
  @JsonProperty("transactionId")
  private String transactionId = null;

  @JsonProperty("cashApplied")
  private Money cashApplied = null;

  @JsonProperty("discApplied")
  private Money discApplied = null;

  @JsonProperty("balance")
  private Money balance = null;

  @JsonProperty("availableDisc")
  private Money availableDisc = null;

  @JsonProperty("openAmount")
  private Money openAmount = null;

  @JsonProperty("appliedDate")
  private LocalDateTime appliedDate = null;

  @JsonProperty("shipToId")
  private Integer shipToId = null;

  @JsonProperty("updateKey")
  private String updateKey = null;

  @JsonProperty("glPostings")
  private List<AccrualRegisterAppliedPaymentGlPosting> glPostings = null;

  public CashReceiptInvoice transactionId(String transactionId) {
    this.transactionId = transactionId;
    return this;
  }

   /**
   * Desc: The CashReceiptInvoice TransactionId  File: AR  Attr: 2,x
   * @return transactionId
  **/
  @Schema(description = "Desc: The CashReceiptInvoice TransactionId  File: AR  Attr: 2,x")
  public String getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }

  public CashReceiptInvoice cashApplied(Money cashApplied) {
    this.cashApplied = cashApplied;
    return this;
  }

   /**
   * Get cashApplied
   * @return cashApplied
  **/
  @Valid
  @Schema(description = "")
  public Money getCashApplied() {
    return cashApplied;
  }

  public void setCashApplied(Money cashApplied) {
    this.cashApplied = cashApplied;
  }

  public CashReceiptInvoice discApplied(Money discApplied) {
    this.discApplied = discApplied;
    return this;
  }

   /**
   * Get discApplied
   * @return discApplied
  **/
  @Valid
  @Schema(description = "")
  public Money getDiscApplied() {
    return discApplied;
  }

  public void setDiscApplied(Money discApplied) {
    this.discApplied = discApplied;
  }

  public CashReceiptInvoice balance(Money balance) {
    this.balance = balance;
    return this;
  }

   /**
   * Get balance
   * @return balance
  **/
  @Valid
  @Schema(description = "")
  public Money getBalance() {
    return balance;
  }

  public void setBalance(Money balance) {
    this.balance = balance;
  }

  public CashReceiptInvoice availableDisc(Money availableDisc) {
    this.availableDisc = availableDisc;
    return this;
  }

   /**
   * Get availableDisc
   * @return availableDisc
  **/
  @Valid
  @Schema(description = "")
  public Money getAvailableDisc() {
    return availableDisc;
  }

  public void setAvailableDisc(Money availableDisc) {
    this.availableDisc = availableDisc;
  }

  public CashReceiptInvoice openAmount(Money openAmount) {
    this.openAmount = openAmount;
    return this;
  }

   /**
   * Get openAmount
   * @return openAmount
  **/
  @Valid
  @Schema(description = "")
  public Money getOpenAmount() {
    return openAmount;
  }

  public void setOpenAmount(Money openAmount) {
    this.openAmount = openAmount;
  }

  public CashReceiptInvoice appliedDate(LocalDateTime appliedDate) {
    this.appliedDate = appliedDate;
    return this;
  }

   /**
   * Desc: The CashReceiptInvoice AppliedDate  File: AR  Attr: 3,x
   * @return appliedDate
  **/
  @Valid
  @Schema(description = "Desc: The CashReceiptInvoice AppliedDate  File: AR  Attr: 3,x")
  public LocalDateTime getAppliedDate() {
    return appliedDate;
  }

  public void setAppliedDate(LocalDateTime appliedDate) {
    this.appliedDate = appliedDate;
  }

  public CashReceiptInvoice shipToId(Integer shipToId) {
    this.shipToId = shipToId;
    return this;
  }

   /**
   * Desc: The CashReceiptInvoice ShipToId  File: AR  Attr: 15,x
   * @return shipToId
  **/
  @Schema(description = "Desc: The CashReceiptInvoice ShipToId  File: AR  Attr: 15,x")
  public Integer getShipToId() {
    return shipToId;
  }

  public void setShipToId(Integer shipToId) {
    this.shipToId = shipToId;
  }

  public CashReceiptInvoice updateKey(String updateKey) {
    this.updateKey = updateKey;
    return this;
  }

   /**
   * Update key of the records related to cash receipt
   * @return updateKey
  **/
  @Schema(description = "Update key of the records related to cash receipt")
  public String getUpdateKey() {
    return updateKey;
  }

  public void setUpdateKey(String updateKey) {
    this.updateKey = updateKey;
  }

  public CashReceiptInvoice glPostings(List<AccrualRegisterAppliedPaymentGlPosting> glPostings) {
    this.glPostings = glPostings;
    return this;
  }

  public CashReceiptInvoice addGlPostingsItem(AccrualRegisterAppliedPaymentGlPosting glPostingsItem) {
    if (this.glPostings == null) {
      this.glPostings = new ArrayList<>();
    }
    this.glPostings.add(glPostingsItem);
    return this;
  }

   /**
   * A list of CashReceiptInvoice GlPostings
   * @return glPostings
  **/
  @Valid
  @Schema(description = "A list of CashReceiptInvoice GlPostings")
  public List<AccrualRegisterAppliedPaymentGlPosting> getGlPostings() {
    return glPostings;
  }

  public void setGlPostings(List<AccrualRegisterAppliedPaymentGlPosting> glPostings) {
    this.glPostings = glPostings;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CashReceiptInvoice cashReceiptInvoice = (CashReceiptInvoice) o;
    return Objects.equals(this.transactionId, cashReceiptInvoice.transactionId) &&
        Objects.equals(this.cashApplied, cashReceiptInvoice.cashApplied) &&
        Objects.equals(this.discApplied, cashReceiptInvoice.discApplied) &&
        Objects.equals(this.balance, cashReceiptInvoice.balance) &&
        Objects.equals(this.availableDisc, cashReceiptInvoice.availableDisc) &&
        Objects.equals(this.openAmount, cashReceiptInvoice.openAmount) &&
        Objects.equals(this.appliedDate, cashReceiptInvoice.appliedDate) &&
        Objects.equals(this.shipToId, cashReceiptInvoice.shipToId) &&
        Objects.equals(this.updateKey, cashReceiptInvoice.updateKey) &&
        Objects.equals(this.glPostings, cashReceiptInvoice.glPostings);
  }

  @Override
  public int hashCode() {
    return Objects.hash(transactionId, cashApplied, discApplied, balance, availableDisc, openAmount, appliedDate, shipToId, updateKey, glPostings);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CashReceiptInvoice {\n");
    
    sb.append("    transactionId: ").append(toIndentedString(transactionId)).append("\n");
    sb.append("    cashApplied: ").append(toIndentedString(cashApplied)).append("\n");
    sb.append("    discApplied: ").append(toIndentedString(discApplied)).append("\n");
    sb.append("    balance: ").append(toIndentedString(balance)).append("\n");
    sb.append("    availableDisc: ").append(toIndentedString(availableDisc)).append("\n");
    sb.append("    openAmount: ").append(toIndentedString(openAmount)).append("\n");
    sb.append("    appliedDate: ").append(toIndentedString(appliedDate)).append("\n");
    sb.append("    shipToId: ").append(toIndentedString(shipToId)).append("\n");
    sb.append("    updateKey: ").append(toIndentedString(updateKey)).append("\n");
    sb.append("    glPostings: ").append(toIndentedString(glPostings)).append("\n");
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
