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
import com.reece.platform.eclipse.model.generated.VendorPartNumberCommodityCode;
import com.reece.platform.eclipse.model.generated.VendorPartNumberSubstituteProduct;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.*;
import javax.validation.Valid;
/**
 * Object representing an Eclipse VendorPartNumber.
 */
@Schema(description = "Object representing an Eclipse VendorPartNumber.")

public class VendorPartNumber {
  @JsonProperty("updateKey")
  private String updateKey = null;

  @JsonProperty("id")
  private String id = null;

  @JsonProperty("partNumber")
  private String partNumber = null;

  @JsonProperty("location")
  private String location = null;

  @JsonProperty("productId")
  private Integer productId = null;

  @JsonProperty("pricingBasis")
  private String pricingBasis = null;

  @JsonProperty("pricingFormula")
  private String pricingFormula = null;

  @JsonProperty("minQty")
  private Integer minQty = null;

  @JsonProperty("maxQty")
  private Integer maxQty = null;

  @JsonProperty("rdcLevelCount")
  private Integer rdcLevelCount = null;

  @JsonProperty("rdcCountDate")
  private LocalDateTime rdcCountDate = null;

  @JsonProperty("rdcCountTime")
  private LocalDateTime rdcCountTime = null;

  @JsonProperty("rank1")
  private String rank1 = null;

  @JsonProperty("rank2")
  private String rank2 = null;

  @JsonProperty("rank3")
  private String rank3 = null;

  @JsonProperty("comment")
  private String comment = null;

  @JsonProperty("replenishmentPoNumber")
  private String replenishmentPoNumber = null;

  @JsonProperty("replenishmentBreakPoint")
  private String replenishmentBreakPoint = null;

  @JsonProperty("replenishmentReleaseNumber")
  private String replenishmentReleaseNumber = null;

  @JsonProperty("replenishmentPartNumberPrintOverride")
  private String replenishmentPartNumberPrintOverride = null;

  @JsonProperty("lineItemTaxFlag")
  private Boolean lineItemTaxFlag = null;

  @JsonProperty("lineItemTaxCode")
  private String lineItemTaxCode = null;

  @JsonProperty("costCode")
  private String costCode = null;

  @JsonProperty("vendorId")
  private Integer vendorId = null;

  @JsonProperty("commodityCodes")
  private List<VendorPartNumberCommodityCode> commodityCodes = null;

  @JsonProperty("substituteProducts")
  private List<VendorPartNumberSubstituteProduct> substituteProducts = null;

  public VendorPartNumber updateKey(String updateKey) {
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

  public VendorPartNumber id(String id) {
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

  public VendorPartNumber partNumber(String partNumber) {
    this.partNumber = partNumber;
    return this;
  }

   /**
   * Desc: The EntityPartNumber PartNumber  File: ENTITY.PN.IDS  Attr: 0
   * @return partNumber
  **/
  @NotNull
 @Size(min=1,max=30)  @Schema(required = true, description = "Desc: The EntityPartNumber PartNumber  File: ENTITY.PN.IDS  Attr: 0")
  public String getPartNumber() {
    return partNumber;
  }

  public void setPartNumber(String partNumber) {
    this.partNumber = partNumber;
  }

  public VendorPartNumber location(String location) {
    this.location = location;
    return this;
  }

   /**
   * Desc: The EntityPartNumber Location  File: ENTITY.PN.IDS  Attr: 0
   * @return location
  **/
 @Size(max=20)  @Schema(description = "Desc: The EntityPartNumber Location  File: ENTITY.PN.IDS  Attr: 0")
  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public VendorPartNumber productId(Integer productId) {
    this.productId = productId;
    return this;
  }

   /**
   * Desc: The EntityPartNumber ProductId  File: ENTITY.PN.IDS  Attr: 0
   * @return productId
  **/
  @NotNull
  @Schema(required = true, description = "Desc: The EntityPartNumber ProductId  File: ENTITY.PN.IDS  Attr: 0")
  public Integer getProductId() {
    return productId;
  }

  public void setProductId(Integer productId) {
    this.productId = productId;
  }

  public VendorPartNumber pricingBasis(String pricingBasis) {
    this.pricingBasis = pricingBasis;
    return this;
  }

   /**
   * Desc: The EntityPartNumber PricingBasis  File: ENTITY.PN.IDS  Attr: 1,1
   * @return pricingBasis
  **/
  @Schema(description = "Desc: The EntityPartNumber PricingBasis  File: ENTITY.PN.IDS  Attr: 1,1")
  public String getPricingBasis() {
    return pricingBasis;
  }

  public void setPricingBasis(String pricingBasis) {
    this.pricingBasis = pricingBasis;
  }

  public VendorPartNumber pricingFormula(String pricingFormula) {
    this.pricingFormula = pricingFormula;
    return this;
  }

   /**
   * Desc: The EntityPartNumber PricingFormula  File: ENTITY.PN.IDS  Attr: 1,2
   * @return pricingFormula
  **/
  @Schema(description = "Desc: The EntityPartNumber PricingFormula  File: ENTITY.PN.IDS  Attr: 1,2")
  public String getPricingFormula() {
    return pricingFormula;
  }

  public void setPricingFormula(String pricingFormula) {
    this.pricingFormula = pricingFormula;
  }

  public VendorPartNumber minQty(Integer minQty) {
    this.minQty = minQty;
    return this;
  }

   /**
   * Desc: The EntityPartNumber MinQty  File: ENTITY.PN.IDS  Attr: 2
   * @return minQty
  **/
  @Schema(description = "Desc: The EntityPartNumber MinQty  File: ENTITY.PN.IDS  Attr: 2")
  public Integer getMinQty() {
    return minQty;
  }

  public void setMinQty(Integer minQty) {
    this.minQty = minQty;
  }

  public VendorPartNumber maxQty(Integer maxQty) {
    this.maxQty = maxQty;
    return this;
  }

   /**
   * Desc: The EntityPartNumber MaxQty  File: ENTITY.PN.IDS  Attr: 3
   * @return maxQty
  **/
  @Schema(description = "Desc: The EntityPartNumber MaxQty  File: ENTITY.PN.IDS  Attr: 3")
  public Integer getMaxQty() {
    return maxQty;
  }

  public void setMaxQty(Integer maxQty) {
    this.maxQty = maxQty;
  }

  public VendorPartNumber rdcLevelCount(Integer rdcLevelCount) {
    this.rdcLevelCount = rdcLevelCount;
    return this;
  }

   /**
   * Desc: The EntityPartNumber RdcLevelCount  File: ENTITY.PN.IDS  Attr: 4
   * @return rdcLevelCount
  **/
  @Schema(description = "Desc: The EntityPartNumber RdcLevelCount  File: ENTITY.PN.IDS  Attr: 4")
  public Integer getRdcLevelCount() {
    return rdcLevelCount;
  }

  public void setRdcLevelCount(Integer rdcLevelCount) {
    this.rdcLevelCount = rdcLevelCount;
  }

  public VendorPartNumber rdcCountDate(LocalDateTime rdcCountDate) {
    this.rdcCountDate = rdcCountDate;
    return this;
  }

   /**
   * Desc: The EntityPartNumber RdcCountDate  File: ENTITY.PN.IDS  Attr: 5
   * @return rdcCountDate
  **/
  @Valid
  @Schema(description = "Desc: The EntityPartNumber RdcCountDate  File: ENTITY.PN.IDS  Attr: 5")
  public LocalDateTime getRdcCountDate() {
    return rdcCountDate;
  }

  public void setRdcCountDate(LocalDateTime rdcCountDate) {
    this.rdcCountDate = rdcCountDate;
  }

  public VendorPartNumber rdcCountTime(LocalDateTime rdcCountTime) {
    this.rdcCountTime = rdcCountTime;
    return this;
  }

   /**
   * Desc: The EntityPartNumber RdcCountTime  File: ENTITY.PN.IDS  Attr: 6
   * @return rdcCountTime
  **/
  @Valid
  @Schema(description = "Desc: The EntityPartNumber RdcCountTime  File: ENTITY.PN.IDS  Attr: 6")
  public LocalDateTime getRdcCountTime() {
    return rdcCountTime;
  }

  public void setRdcCountTime(LocalDateTime rdcCountTime) {
    this.rdcCountTime = rdcCountTime;
  }

  public VendorPartNumber rank1(String rank1) {
    this.rank1 = rank1;
    return this;
  }

   /**
   * Desc: The EntityPartNumber Rank1  File: ENTITY.PN.IDS  Attr: 10,1
   * @return rank1
  **/
 @Size(max=8)  @Schema(description = "Desc: The EntityPartNumber Rank1  File: ENTITY.PN.IDS  Attr: 10,1")
  public String getRank1() {
    return rank1;
  }

  public void setRank1(String rank1) {
    this.rank1 = rank1;
  }

  public VendorPartNumber rank2(String rank2) {
    this.rank2 = rank2;
    return this;
  }

   /**
   * Desc: The EntityPartNumber Rank2  File: ENTITY.PN.IDS  Attr: 10,2
   * @return rank2
  **/
 @Size(max=8)  @Schema(description = "Desc: The EntityPartNumber Rank2  File: ENTITY.PN.IDS  Attr: 10,2")
  public String getRank2() {
    return rank2;
  }

  public void setRank2(String rank2) {
    this.rank2 = rank2;
  }

  public VendorPartNumber rank3(String rank3) {
    this.rank3 = rank3;
    return this;
  }

   /**
   * The EntityPartNumber Ranks3  File: ENTITY.PN.IDS  Attr: 10,3
   * @return rank3
  **/
 @Size(max=8)  @Schema(description = "The EntityPartNumber Ranks3  File: ENTITY.PN.IDS  Attr: 10,3")
  public String getRank3() {
    return rank3;
  }

  public void setRank3(String rank3) {
    this.rank3 = rank3;
  }

  public VendorPartNumber comment(String comment) {
    this.comment = comment;
    return this;
  }

   /**
   * Desc: The EntityPartNumber Comment  File: ENTITY.PN.IDS  Attr: 9
   * @return comment
  **/
  @Schema(description = "Desc: The EntityPartNumber Comment  File: ENTITY.PN.IDS  Attr: 9")
  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public VendorPartNumber replenishmentPoNumber(String replenishmentPoNumber) {
    this.replenishmentPoNumber = replenishmentPoNumber;
    return this;
  }

   /**
   * Desc: The EntityPartNumber ReplenishmentPoNumber  File: ENTITY.PN.IDS  Attr: 11
   * @return replenishmentPoNumber
  **/
 @Size(max=20)  @Schema(description = "Desc: The EntityPartNumber ReplenishmentPoNumber  File: ENTITY.PN.IDS  Attr: 11")
  public String getReplenishmentPoNumber() {
    return replenishmentPoNumber;
  }

  public void setReplenishmentPoNumber(String replenishmentPoNumber) {
    this.replenishmentPoNumber = replenishmentPoNumber;
  }

  public VendorPartNumber replenishmentBreakPoint(String replenishmentBreakPoint) {
    this.replenishmentBreakPoint = replenishmentBreakPoint;
    return this;
  }

   /**
   * Desc: The EntityPartNumber ReplenishmentBreakPoint  File: ENTITY.PN.IDS  Attr: 12
   * @return replenishmentBreakPoint
  **/
 @Size(max=20)  @Schema(description = "Desc: The EntityPartNumber ReplenishmentBreakPoint  File: ENTITY.PN.IDS  Attr: 12")
  public String getReplenishmentBreakPoint() {
    return replenishmentBreakPoint;
  }

  public void setReplenishmentBreakPoint(String replenishmentBreakPoint) {
    this.replenishmentBreakPoint = replenishmentBreakPoint;
  }

  public VendorPartNumber replenishmentReleaseNumber(String replenishmentReleaseNumber) {
    this.replenishmentReleaseNumber = replenishmentReleaseNumber;
    return this;
  }

   /**
   * Desc: The EntityPartNumber ReplenishmentReleaseNumber  File: ENTITY.PN.IDS  Attr: 13
   * @return replenishmentReleaseNumber
  **/
 @Size(max=20)  @Schema(description = "Desc: The EntityPartNumber ReplenishmentReleaseNumber  File: ENTITY.PN.IDS  Attr: 13")
  public String getReplenishmentReleaseNumber() {
    return replenishmentReleaseNumber;
  }

  public void setReplenishmentReleaseNumber(String replenishmentReleaseNumber) {
    this.replenishmentReleaseNumber = replenishmentReleaseNumber;
  }

  public VendorPartNumber replenishmentPartNumberPrintOverride(String replenishmentPartNumberPrintOverride) {
    this.replenishmentPartNumberPrintOverride = replenishmentPartNumberPrintOverride;
    return this;
  }

   /**
   * Desc: The EntityPartNumber ReplenishmentPartNumberPrintOverride  File: ENTITY.PN.IDS  Attr: 14
   * @return replenishmentPartNumberPrintOverride
  **/
 @Size(max=20)  @Schema(description = "Desc: The EntityPartNumber ReplenishmentPartNumberPrintOverride  File: ENTITY.PN.IDS  Attr: 14")
  public String getReplenishmentPartNumberPrintOverride() {
    return replenishmentPartNumberPrintOverride;
  }

  public void setReplenishmentPartNumberPrintOverride(String replenishmentPartNumberPrintOverride) {
    this.replenishmentPartNumberPrintOverride = replenishmentPartNumberPrintOverride;
  }

  public VendorPartNumber lineItemTaxFlag(Boolean lineItemTaxFlag) {
    this.lineItemTaxFlag = lineItemTaxFlag;
    return this;
  }

   /**
   * Desc: The EntityPartNumber LineItemTaxFlag  File: ENTITY.PN.IDS  Attr: 15
   * @return lineItemTaxFlag
  **/
  @Schema(description = "Desc: The EntityPartNumber LineItemTaxFlag  File: ENTITY.PN.IDS  Attr: 15")
  public Boolean isLineItemTaxFlag() {
    return lineItemTaxFlag;
  }

  public void setLineItemTaxFlag(Boolean lineItemTaxFlag) {
    this.lineItemTaxFlag = lineItemTaxFlag;
  }

  public VendorPartNumber lineItemTaxCode(String lineItemTaxCode) {
    this.lineItemTaxCode = lineItemTaxCode;
    return this;
  }

   /**
   * Desc: The EntityPartNumber LineItemTaxCode  File: ENTITY.PN.IDS  Attr: 16
   * @return lineItemTaxCode
  **/
  @Schema(description = "Desc: The EntityPartNumber LineItemTaxCode  File: ENTITY.PN.IDS  Attr: 16")
  public String getLineItemTaxCode() {
    return lineItemTaxCode;
  }

  public void setLineItemTaxCode(String lineItemTaxCode) {
    this.lineItemTaxCode = lineItemTaxCode;
  }

  public VendorPartNumber costCode(String costCode) {
    this.costCode = costCode;
    return this;
  }

   /**
   * Desc: The EntityPartNumber CostCode  File: ENTITY.PN.IDS  Attr: 17
   * @return costCode
  **/
 @Size(max=25)  @Schema(description = "Desc: The EntityPartNumber CostCode  File: ENTITY.PN.IDS  Attr: 17")
  public String getCostCode() {
    return costCode;
  }

  public void setCostCode(String costCode) {
    this.costCode = costCode;
  }

  public VendorPartNumber vendorId(Integer vendorId) {
    this.vendorId = vendorId;
    return this;
  }

   /**
   * Desc: The VendorPartNumber VendorId  File: ENTITY.PN.IDS  Attr: 0
   * @return vendorId
  **/
  @NotNull
  @Schema(required = true, description = "Desc: The VendorPartNumber VendorId  File: ENTITY.PN.IDS  Attr: 0")
  public Integer getVendorId() {
    return vendorId;
  }

  public void setVendorId(Integer vendorId) {
    this.vendorId = vendorId;
  }

  public VendorPartNumber commodityCodes(List<VendorPartNumberCommodityCode> commodityCodes) {
    this.commodityCodes = commodityCodes;
    return this;
  }

  public VendorPartNumber addCommodityCodesItem(VendorPartNumberCommodityCode commodityCodesItem) {
    if (this.commodityCodes == null) {
      this.commodityCodes = new ArrayList<>();
    }
    this.commodityCodes.add(commodityCodesItem);
    return this;
  }

   /**
   * A list of VendorPartNumber CommodityCodes
   * @return commodityCodes
  **/
  @Valid
  @Schema(description = "A list of VendorPartNumber CommodityCodes")
  public List<VendorPartNumberCommodityCode> getCommodityCodes() {
    return commodityCodes;
  }

  public void setCommodityCodes(List<VendorPartNumberCommodityCode> commodityCodes) {
    this.commodityCodes = commodityCodes;
  }

  public VendorPartNumber substituteProducts(List<VendorPartNumberSubstituteProduct> substituteProducts) {
    this.substituteProducts = substituteProducts;
    return this;
  }

  public VendorPartNumber addSubstituteProductsItem(VendorPartNumberSubstituteProduct substituteProductsItem) {
    if (this.substituteProducts == null) {
      this.substituteProducts = new ArrayList<>();
    }
    this.substituteProducts.add(substituteProductsItem);
    return this;
  }

   /**
   * A list of VendorPartNumber SubstituteProducts
   * @return substituteProducts
  **/
  @Valid
  @Schema(description = "A list of VendorPartNumber SubstituteProducts")
  public List<VendorPartNumberSubstituteProduct> getSubstituteProducts() {
    return substituteProducts;
  }

  public void setSubstituteProducts(List<VendorPartNumberSubstituteProduct> substituteProducts) {
    this.substituteProducts = substituteProducts;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VendorPartNumber vendorPartNumber = (VendorPartNumber) o;
    return Objects.equals(this.updateKey, vendorPartNumber.updateKey) &&
        Objects.equals(this.id, vendorPartNumber.id) &&
        Objects.equals(this.partNumber, vendorPartNumber.partNumber) &&
        Objects.equals(this.location, vendorPartNumber.location) &&
        Objects.equals(this.productId, vendorPartNumber.productId) &&
        Objects.equals(this.pricingBasis, vendorPartNumber.pricingBasis) &&
        Objects.equals(this.pricingFormula, vendorPartNumber.pricingFormula) &&
        Objects.equals(this.minQty, vendorPartNumber.minQty) &&
        Objects.equals(this.maxQty, vendorPartNumber.maxQty) &&
        Objects.equals(this.rdcLevelCount, vendorPartNumber.rdcLevelCount) &&
        Objects.equals(this.rdcCountDate, vendorPartNumber.rdcCountDate) &&
        Objects.equals(this.rdcCountTime, vendorPartNumber.rdcCountTime) &&
        Objects.equals(this.rank1, vendorPartNumber.rank1) &&
        Objects.equals(this.rank2, vendorPartNumber.rank2) &&
        Objects.equals(this.rank3, vendorPartNumber.rank3) &&
        Objects.equals(this.comment, vendorPartNumber.comment) &&
        Objects.equals(this.replenishmentPoNumber, vendorPartNumber.replenishmentPoNumber) &&
        Objects.equals(this.replenishmentBreakPoint, vendorPartNumber.replenishmentBreakPoint) &&
        Objects.equals(this.replenishmentReleaseNumber, vendorPartNumber.replenishmentReleaseNumber) &&
        Objects.equals(this.replenishmentPartNumberPrintOverride, vendorPartNumber.replenishmentPartNumberPrintOverride) &&
        Objects.equals(this.lineItemTaxFlag, vendorPartNumber.lineItemTaxFlag) &&
        Objects.equals(this.lineItemTaxCode, vendorPartNumber.lineItemTaxCode) &&
        Objects.equals(this.costCode, vendorPartNumber.costCode) &&
        Objects.equals(this.vendorId, vendorPartNumber.vendorId) &&
        Objects.equals(this.commodityCodes, vendorPartNumber.commodityCodes) &&
        Objects.equals(this.substituteProducts, vendorPartNumber.substituteProducts);
  }

  @Override
  public int hashCode() {
    return Objects.hash(updateKey, id, partNumber, location, productId, pricingBasis, pricingFormula, minQty, maxQty, rdcLevelCount, rdcCountDate, rdcCountTime, rank1, rank2, rank3, comment, replenishmentPoNumber, replenishmentBreakPoint, replenishmentReleaseNumber, replenishmentPartNumberPrintOverride, lineItemTaxFlag, lineItemTaxCode, costCode, vendorId, commodityCodes, substituteProducts);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VendorPartNumber {\n");
    
    sb.append("    updateKey: ").append(toIndentedString(updateKey)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    partNumber: ").append(toIndentedString(partNumber)).append("\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
    sb.append("    productId: ").append(toIndentedString(productId)).append("\n");
    sb.append("    pricingBasis: ").append(toIndentedString(pricingBasis)).append("\n");
    sb.append("    pricingFormula: ").append(toIndentedString(pricingFormula)).append("\n");
    sb.append("    minQty: ").append(toIndentedString(minQty)).append("\n");
    sb.append("    maxQty: ").append(toIndentedString(maxQty)).append("\n");
    sb.append("    rdcLevelCount: ").append(toIndentedString(rdcLevelCount)).append("\n");
    sb.append("    rdcCountDate: ").append(toIndentedString(rdcCountDate)).append("\n");
    sb.append("    rdcCountTime: ").append(toIndentedString(rdcCountTime)).append("\n");
    sb.append("    rank1: ").append(toIndentedString(rank1)).append("\n");
    sb.append("    rank2: ").append(toIndentedString(rank2)).append("\n");
    sb.append("    rank3: ").append(toIndentedString(rank3)).append("\n");
    sb.append("    comment: ").append(toIndentedString(comment)).append("\n");
    sb.append("    replenishmentPoNumber: ").append(toIndentedString(replenishmentPoNumber)).append("\n");
    sb.append("    replenishmentBreakPoint: ").append(toIndentedString(replenishmentBreakPoint)).append("\n");
    sb.append("    replenishmentReleaseNumber: ").append(toIndentedString(replenishmentReleaseNumber)).append("\n");
    sb.append("    replenishmentPartNumberPrintOverride: ").append(toIndentedString(replenishmentPartNumberPrintOverride)).append("\n");
    sb.append("    lineItemTaxFlag: ").append(toIndentedString(lineItemTaxFlag)).append("\n");
    sb.append("    lineItemTaxCode: ").append(toIndentedString(lineItemTaxCode)).append("\n");
    sb.append("    costCode: ").append(toIndentedString(costCode)).append("\n");
    sb.append("    vendorId: ").append(toIndentedString(vendorId)).append("\n");
    sb.append("    commodityCodes: ").append(toIndentedString(commodityCodes)).append("\n");
    sb.append("    substituteProducts: ").append(toIndentedString(substituteProducts)).append("\n");
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
