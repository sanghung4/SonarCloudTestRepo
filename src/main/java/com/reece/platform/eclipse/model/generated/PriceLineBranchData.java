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
import com.reece.platform.eclipse.model.generated.PriceLineBranchControlTypes;
import com.reece.platform.eclipse.model.generated.PriceLineBranchIncQtyRfPickingTypes;
import com.reece.platform.eclipse.model.generated.PriceLinePointsProgram;
import com.reece.platform.eclipse.model.generated.PriceLineRanking;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.*;
import javax.validation.Valid;
/**
 * Object representing Price Line branch Specific Data
 */
@Schema(description = "Object representing Price Line branch Specific Data")

public class PriceLineBranchData {
  @JsonProperty("branchId")
  private String branchId = null;

  @JsonProperty("checkAvailability")
  private Boolean checkAvailability = null;

  @JsonProperty("passAlongDiscount")
  private Double passAlongDiscount = null;

  @JsonProperty("controlType")
  private PriceLineBranchControlTypes controlType = null;

  @JsonProperty("excludeFromCycleCount")
  private Boolean excludeFromCycleCount = null;

  @JsonProperty("allowIncrQtyInRfPicking")
  private PriceLineBranchIncQtyRfPickingTypes allowIncrQtyInRfPicking = null;

  @JsonProperty("notifyOnCogsChange")
  private Boolean notifyOnCogsChange = null;

  @JsonProperty("economicReturnAmount")
  private Money economicReturnAmount = null;

  @JsonProperty("orderStockGpPercentage")
  private Double orderStockGpPercentage = null;

  @JsonProperty("orderDirectGpPercentage")
  private Double orderDirectGpPercentage = null;

  @JsonProperty("reportStockGpPercentage")
  private Double reportStockGpPercentage = null;

  @JsonProperty("reportDirectGpPercentage")
  private Double reportDirectGpPercentage = null;

  @JsonProperty("priceLinePointsPrograms")
  private List<PriceLinePointsProgram> priceLinePointsPrograms = null;

  @JsonProperty("priceLineRankOne")
  private PriceLineRanking priceLineRankOne = null;

  @JsonProperty("priceLineRankTwo")
  private PriceLineRanking priceLineRankTwo = null;

  @JsonProperty("priceLineRankThree")
  private PriceLineRanking priceLineRankThree = null;

  @JsonProperty("priceLineRankFour")
  private PriceLineRanking priceLineRankFour = null;

  @JsonProperty("priceLineRankFive")
  private PriceLineRanking priceLineRankFive = null;

  public PriceLineBranchData branchId(String branchId) {
    this.branchId = branchId;
    return this;
  }

   /**
   * Desc: The PriceLineBranch Branch Id  File: PRICE.LINE.BR  Attr: 10,x
   * @return branchId
  **/
  @Schema(description = "Desc: The PriceLineBranch Branch Id  File: PRICE.LINE.BR  Attr: 10,x")
  public String getBranchId() {
    return branchId;
  }

  public void setBranchId(String branchId) {
    this.branchId = branchId;
  }

  public PriceLineBranchData checkAvailability(Boolean checkAvailability) {
    this.checkAvailability = checkAvailability;
    return this;
  }

   /**
   * Desc: The PriceLineBranch CheckAvailability flag  File: PRICE.LINE.BR  Attr: 1,x
   * @return checkAvailability
  **/
  @Schema(description = "Desc: The PriceLineBranch CheckAvailability flag  File: PRICE.LINE.BR  Attr: 1,x")
  public Boolean isCheckAvailability() {
    return checkAvailability;
  }

  public void setCheckAvailability(Boolean checkAvailability) {
    this.checkAvailability = checkAvailability;
  }

  public PriceLineBranchData passAlongDiscount(Double passAlongDiscount) {
    this.passAlongDiscount = passAlongDiscount;
    return this;
  }

   /**
   * Desc: The PriceLineBranch PassAlongDiscount %age  File: PRICE.LINE.BR  Attr: 3,x
   * @return passAlongDiscount
  **/
  @Schema(description = "Desc: The PriceLineBranch PassAlongDiscount %age  File: PRICE.LINE.BR  Attr: 3,x")
  public Double getPassAlongDiscount() {
    return passAlongDiscount;
  }

  public void setPassAlongDiscount(Double passAlongDiscount) {
    this.passAlongDiscount = passAlongDiscount;
  }

  public PriceLineBranchData controlType(PriceLineBranchControlTypes controlType) {
    this.controlType = controlType;
    return this;
  }

   /**
   * Get controlType
   * @return controlType
  **/
  @Valid
  @Schema(description = "")
  public PriceLineBranchControlTypes getControlType() {
    return controlType;
  }

  public void setControlType(PriceLineBranchControlTypes controlType) {
    this.controlType = controlType;
  }

  public PriceLineBranchData excludeFromCycleCount(Boolean excludeFromCycleCount) {
    this.excludeFromCycleCount = excludeFromCycleCount;
    return this;
  }

   /**
   * Desc: The PriceLineBranch ExcludeFromCycleCount  File: PRICE.LINE.BR  Attr: 8,x
   * @return excludeFromCycleCount
  **/
  @Schema(description = "Desc: The PriceLineBranch ExcludeFromCycleCount  File: PRICE.LINE.BR  Attr: 8,x")
  public Boolean isExcludeFromCycleCount() {
    return excludeFromCycleCount;
  }

  public void setExcludeFromCycleCount(Boolean excludeFromCycleCount) {
    this.excludeFromCycleCount = excludeFromCycleCount;
  }

  public PriceLineBranchData allowIncrQtyInRfPicking(PriceLineBranchIncQtyRfPickingTypes allowIncrQtyInRfPicking) {
    this.allowIncrQtyInRfPicking = allowIncrQtyInRfPicking;
    return this;
  }

   /**
   * Get allowIncrQtyInRfPicking
   * @return allowIncrQtyInRfPicking
  **/
  @Valid
  @Schema(description = "")
  public PriceLineBranchIncQtyRfPickingTypes getAllowIncrQtyInRfPicking() {
    return allowIncrQtyInRfPicking;
  }

  public void setAllowIncrQtyInRfPicking(PriceLineBranchIncQtyRfPickingTypes allowIncrQtyInRfPicking) {
    this.allowIncrQtyInRfPicking = allowIncrQtyInRfPicking;
  }

  public PriceLineBranchData notifyOnCogsChange(Boolean notifyOnCogsChange) {
    this.notifyOnCogsChange = notifyOnCogsChange;
    return this;
  }

   /**
   * Desc: The PriceLineBranch NotifyOnCogsChange flag  File: PRICE.LINE.BR  Attr: 13,x
   * @return notifyOnCogsChange
  **/
  @Schema(description = "Desc: The PriceLineBranch NotifyOnCogsChange flag  File: PRICE.LINE.BR  Attr: 13,x")
  public Boolean isNotifyOnCogsChange() {
    return notifyOnCogsChange;
  }

  public void setNotifyOnCogsChange(Boolean notifyOnCogsChange) {
    this.notifyOnCogsChange = notifyOnCogsChange;
  }

  public PriceLineBranchData economicReturnAmount(Money economicReturnAmount) {
    this.economicReturnAmount = economicReturnAmount;
    return this;
  }

   /**
   * Get economicReturnAmount
   * @return economicReturnAmount
  **/
  @Valid
  @Schema(description = "")
  public Money getEconomicReturnAmount() {
    return economicReturnAmount;
  }

  public void setEconomicReturnAmount(Money economicReturnAmount) {
    this.economicReturnAmount = economicReturnAmount;
  }

  public PriceLineBranchData orderStockGpPercentage(Double orderStockGpPercentage) {
    this.orderStockGpPercentage = orderStockGpPercentage;
    return this;
  }

   /**
   * Desc: The PriceLineBranch OrderStockGpPercentage  File: PRICE.LINE.BR  Attr: 5,x,1
   * @return orderStockGpPercentage
  **/
  @Schema(description = "Desc: The PriceLineBranch OrderStockGpPercentage  File: PRICE.LINE.BR  Attr: 5,x,1")
  public Double getOrderStockGpPercentage() {
    return orderStockGpPercentage;
  }

  public void setOrderStockGpPercentage(Double orderStockGpPercentage) {
    this.orderStockGpPercentage = orderStockGpPercentage;
  }

  public PriceLineBranchData orderDirectGpPercentage(Double orderDirectGpPercentage) {
    this.orderDirectGpPercentage = orderDirectGpPercentage;
    return this;
  }

   /**
   * Desc: The PriceLineBranch OrderDirectGpPercentage  File: PRICE.LINE.BR  Attr: 5,x,2
   * @return orderDirectGpPercentage
  **/
  @Schema(description = "Desc: The PriceLineBranch OrderDirectGpPercentage  File: PRICE.LINE.BR  Attr: 5,x,2")
  public Double getOrderDirectGpPercentage() {
    return orderDirectGpPercentage;
  }

  public void setOrderDirectGpPercentage(Double orderDirectGpPercentage) {
    this.orderDirectGpPercentage = orderDirectGpPercentage;
  }

  public PriceLineBranchData reportStockGpPercentage(Double reportStockGpPercentage) {
    this.reportStockGpPercentage = reportStockGpPercentage;
    return this;
  }

   /**
   * Desc: The PriceLineBranch ReportStockGpPercenatage  File: PRICE.LINE.BR  Attr: 5,x,3
   * @return reportStockGpPercentage
  **/
  @Schema(description = "Desc: The PriceLineBranch ReportStockGpPercenatage  File: PRICE.LINE.BR  Attr: 5,x,3")
  public Double getReportStockGpPercentage() {
    return reportStockGpPercentage;
  }

  public void setReportStockGpPercentage(Double reportStockGpPercentage) {
    this.reportStockGpPercentage = reportStockGpPercentage;
  }

  public PriceLineBranchData reportDirectGpPercentage(Double reportDirectGpPercentage) {
    this.reportDirectGpPercentage = reportDirectGpPercentage;
    return this;
  }

   /**
   * Desc: The PriceLineBranch ReportDirectGpPercentage  File: PRICE.LINE.BR  Attr: 5,x,4
   * @return reportDirectGpPercentage
  **/
  @Schema(description = "Desc: The PriceLineBranch ReportDirectGpPercentage  File: PRICE.LINE.BR  Attr: 5,x,4")
  public Double getReportDirectGpPercentage() {
    return reportDirectGpPercentage;
  }

  public void setReportDirectGpPercentage(Double reportDirectGpPercentage) {
    this.reportDirectGpPercentage = reportDirectGpPercentage;
  }

  public PriceLineBranchData priceLinePointsPrograms(List<PriceLinePointsProgram> priceLinePointsPrograms) {
    this.priceLinePointsPrograms = priceLinePointsPrograms;
    return this;
  }

  public PriceLineBranchData addPriceLinePointsProgramsItem(PriceLinePointsProgram priceLinePointsProgramsItem) {
    if (this.priceLinePointsPrograms == null) {
      this.priceLinePointsPrograms = new ArrayList<>();
    }
    this.priceLinePointsPrograms.add(priceLinePointsProgramsItem);
    return this;
  }

   /**
   * List of Price Line Points Program
   * @return priceLinePointsPrograms
  **/
  @Valid
  @Schema(description = "List of Price Line Points Program")
  public List<PriceLinePointsProgram> getPriceLinePointsPrograms() {
    return priceLinePointsPrograms;
  }

  public void setPriceLinePointsPrograms(List<PriceLinePointsProgram> priceLinePointsPrograms) {
    this.priceLinePointsPrograms = priceLinePointsPrograms;
  }

  public PriceLineBranchData priceLineRankOne(PriceLineRanking priceLineRankOne) {
    this.priceLineRankOne = priceLineRankOne;
    return this;
  }

   /**
   * Get priceLineRankOne
   * @return priceLineRankOne
  **/
  @Valid
  @Schema(description = "")
  public PriceLineRanking getPriceLineRankOne() {
    return priceLineRankOne;
  }

  public void setPriceLineRankOne(PriceLineRanking priceLineRankOne) {
    this.priceLineRankOne = priceLineRankOne;
  }

  public PriceLineBranchData priceLineRankTwo(PriceLineRanking priceLineRankTwo) {
    this.priceLineRankTwo = priceLineRankTwo;
    return this;
  }

   /**
   * Get priceLineRankTwo
   * @return priceLineRankTwo
  **/
  @Valid
  @Schema(description = "")
  public PriceLineRanking getPriceLineRankTwo() {
    return priceLineRankTwo;
  }

  public void setPriceLineRankTwo(PriceLineRanking priceLineRankTwo) {
    this.priceLineRankTwo = priceLineRankTwo;
  }

  public PriceLineBranchData priceLineRankThree(PriceLineRanking priceLineRankThree) {
    this.priceLineRankThree = priceLineRankThree;
    return this;
  }

   /**
   * Get priceLineRankThree
   * @return priceLineRankThree
  **/
  @Valid
  @Schema(description = "")
  public PriceLineRanking getPriceLineRankThree() {
    return priceLineRankThree;
  }

  public void setPriceLineRankThree(PriceLineRanking priceLineRankThree) {
    this.priceLineRankThree = priceLineRankThree;
  }

  public PriceLineBranchData priceLineRankFour(PriceLineRanking priceLineRankFour) {
    this.priceLineRankFour = priceLineRankFour;
    return this;
  }

   /**
   * Get priceLineRankFour
   * @return priceLineRankFour
  **/
  @Valid
  @Schema(description = "")
  public PriceLineRanking getPriceLineRankFour() {
    return priceLineRankFour;
  }

  public void setPriceLineRankFour(PriceLineRanking priceLineRankFour) {
    this.priceLineRankFour = priceLineRankFour;
  }

  public PriceLineBranchData priceLineRankFive(PriceLineRanking priceLineRankFive) {
    this.priceLineRankFive = priceLineRankFive;
    return this;
  }

   /**
   * Get priceLineRankFive
   * @return priceLineRankFive
  **/
  @Valid
  @Schema(description = "")
  public PriceLineRanking getPriceLineRankFive() {
    return priceLineRankFive;
  }

  public void setPriceLineRankFive(PriceLineRanking priceLineRankFive) {
    this.priceLineRankFive = priceLineRankFive;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PriceLineBranchData priceLineBranchData = (PriceLineBranchData) o;
    return Objects.equals(this.branchId, priceLineBranchData.branchId) &&
        Objects.equals(this.checkAvailability, priceLineBranchData.checkAvailability) &&
        Objects.equals(this.passAlongDiscount, priceLineBranchData.passAlongDiscount) &&
        Objects.equals(this.controlType, priceLineBranchData.controlType) &&
        Objects.equals(this.excludeFromCycleCount, priceLineBranchData.excludeFromCycleCount) &&
        Objects.equals(this.allowIncrQtyInRfPicking, priceLineBranchData.allowIncrQtyInRfPicking) &&
        Objects.equals(this.notifyOnCogsChange, priceLineBranchData.notifyOnCogsChange) &&
        Objects.equals(this.economicReturnAmount, priceLineBranchData.economicReturnAmount) &&
        Objects.equals(this.orderStockGpPercentage, priceLineBranchData.orderStockGpPercentage) &&
        Objects.equals(this.orderDirectGpPercentage, priceLineBranchData.orderDirectGpPercentage) &&
        Objects.equals(this.reportStockGpPercentage, priceLineBranchData.reportStockGpPercentage) &&
        Objects.equals(this.reportDirectGpPercentage, priceLineBranchData.reportDirectGpPercentage) &&
        Objects.equals(this.priceLinePointsPrograms, priceLineBranchData.priceLinePointsPrograms) &&
        Objects.equals(this.priceLineRankOne, priceLineBranchData.priceLineRankOne) &&
        Objects.equals(this.priceLineRankTwo, priceLineBranchData.priceLineRankTwo) &&
        Objects.equals(this.priceLineRankThree, priceLineBranchData.priceLineRankThree) &&
        Objects.equals(this.priceLineRankFour, priceLineBranchData.priceLineRankFour) &&
        Objects.equals(this.priceLineRankFive, priceLineBranchData.priceLineRankFive);
  }

  @Override
  public int hashCode() {
    return Objects.hash(branchId, checkAvailability, passAlongDiscount, controlType, excludeFromCycleCount, allowIncrQtyInRfPicking, notifyOnCogsChange, economicReturnAmount, orderStockGpPercentage, orderDirectGpPercentage, reportStockGpPercentage, reportDirectGpPercentage, priceLinePointsPrograms, priceLineRankOne, priceLineRankTwo, priceLineRankThree, priceLineRankFour, priceLineRankFive);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PriceLineBranchData {\n");
    
    sb.append("    branchId: ").append(toIndentedString(branchId)).append("\n");
    sb.append("    checkAvailability: ").append(toIndentedString(checkAvailability)).append("\n");
    sb.append("    passAlongDiscount: ").append(toIndentedString(passAlongDiscount)).append("\n");
    sb.append("    controlType: ").append(toIndentedString(controlType)).append("\n");
    sb.append("    excludeFromCycleCount: ").append(toIndentedString(excludeFromCycleCount)).append("\n");
    sb.append("    allowIncrQtyInRfPicking: ").append(toIndentedString(allowIncrQtyInRfPicking)).append("\n");
    sb.append("    notifyOnCogsChange: ").append(toIndentedString(notifyOnCogsChange)).append("\n");
    sb.append("    economicReturnAmount: ").append(toIndentedString(economicReturnAmount)).append("\n");
    sb.append("    orderStockGpPercentage: ").append(toIndentedString(orderStockGpPercentage)).append("\n");
    sb.append("    orderDirectGpPercentage: ").append(toIndentedString(orderDirectGpPercentage)).append("\n");
    sb.append("    reportStockGpPercentage: ").append(toIndentedString(reportStockGpPercentage)).append("\n");
    sb.append("    reportDirectGpPercentage: ").append(toIndentedString(reportDirectGpPercentage)).append("\n");
    sb.append("    priceLinePointsPrograms: ").append(toIndentedString(priceLinePointsPrograms)).append("\n");
    sb.append("    priceLineRankOne: ").append(toIndentedString(priceLineRankOne)).append("\n");
    sb.append("    priceLineRankTwo: ").append(toIndentedString(priceLineRankTwo)).append("\n");
    sb.append("    priceLineRankThree: ").append(toIndentedString(priceLineRankThree)).append("\n");
    sb.append("    priceLineRankFour: ").append(toIndentedString(priceLineRankFour)).append("\n");
    sb.append("    priceLineRankFive: ").append(toIndentedString(priceLineRankFive)).append("\n");
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
