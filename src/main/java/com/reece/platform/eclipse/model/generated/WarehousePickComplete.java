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
import com.reece.platform.eclipse.model.generated.ProductLocationType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import javax.validation.constraints.*;
import javax.validation.Valid;
/**
 * Object representing an Eclipse WarehouseUserPick.
 */
@Schema(description = "Object representing an Eclipse WarehouseUserPick.")

public class WarehousePickComplete {
  @JsonProperty("productId")
  private Integer productId = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("quantity")
  private Integer quantity = null;

  @JsonProperty("uom")
  private String uom = null;

  @JsonProperty("locationType")
  private ProductLocationType locationType = null;

  @JsonProperty("location")
  private String location = null;

  @JsonProperty("lot")
  private String lot = null;

  @JsonProperty("splitId")
  private String splitId = null;

  @JsonProperty("orderId")
  private String orderId = null;

  @JsonProperty("generationId")
  private Integer generationId = null;

  @JsonProperty("lineId")
  private Integer lineId = null;

  @JsonProperty("shipVia")
  private String shipVia = null;

  @JsonProperty("tote")
  private String tote = null;

  @JsonProperty("userId")
  private String userId = null;

  @JsonProperty("branchId")
  private String branchId = null;

  @JsonProperty("cutDetail")
  private String cutDetail = null;

  @JsonProperty("cutGroup")
  private String cutGroup = null;

  @JsonProperty("isParallelCut")
  private Boolean isParallelCut = null;

  @JsonProperty("warehouseID")
  private String warehouseID = null;

  @JsonProperty("isLot")
  private String isLot = null;

  @JsonProperty("isSerial")
  private Boolean isSerial = null;

  @JsonProperty("pickGroup")
  private String pickGroup = null;

  @JsonProperty("isOverrideProduct")
  private Boolean isOverrideProduct = null;

  @JsonProperty("startPickTime")
  private LocalDateTime startPickTime = null;

  @JsonProperty("ignoreLockToteCheck")
  private Boolean ignoreLockToteCheck = null;

  public WarehousePickComplete productId(Integer productId) {
    this.productId = productId;
    return this;
  }

   /**
   * Eclipse Product Number
   * @return productId
  **/
  @Schema(description = "Eclipse Product Number")
  public Integer getProductId() {
    return productId;
  }

  public void setProductId(Integer productId) {
    this.productId = productId;
  }

  public WarehousePickComplete description(String description) {
    this.description = description;
    return this;
  }

   /**
   * Eclipse Product Description
   * @return description
  **/
  @Schema(description = "Eclipse Product Description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public WarehousePickComplete quantity(Integer quantity) {
    this.quantity = quantity;
    return this;
  }

   /**
   * Pick Quantity
   * @return quantity
  **/
  @Schema(description = "Pick Quantity")
  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public WarehousePickComplete uom(String uom) {
    this.uom = uom;
    return this;
  }

   /**
   * Desc: Product UoM
   * @return uom
  **/
  @Schema(description = "Desc: Product UoM")
  public String getUom() {
    return uom;
  }

  public void setUom(String uom) {
    this.uom = uom;
  }

  public WarehousePickComplete locationType(ProductLocationType locationType) {
    this.locationType = locationType;
    return this;
  }

   /**
   * Get locationType
   * @return locationType
  **/
  @Valid
  @Schema(description = "")
  public ProductLocationType getLocationType() {
    return locationType;
  }

  public void setLocationType(ProductLocationType locationType) {
    this.locationType = locationType;
  }

  public WarehousePickComplete location(String location) {
    this.location = location;
    return this;
  }

   /**
   * Location
   * @return location
  **/
  @Schema(description = "Location")
  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public WarehousePickComplete lot(String lot) {
    this.lot = lot;
    return this;
  }

   /**
   * Lot
   * @return lot
  **/
  @Schema(description = "Lot")
  public String getLot() {
    return lot;
  }

  public void setLot(String lot) {
    this.lot = lot;
  }

  public WarehousePickComplete splitId(String splitId) {
    this.splitId = splitId;
    return this;
  }

   /**
   * Split ID
   * @return splitId
  **/
  @Schema(description = "Split ID")
  public String getSplitId() {
    return splitId;
  }

  public void setSplitId(String splitId) {
    this.splitId = splitId;
  }

  public WarehousePickComplete orderId(String orderId) {
    this.orderId = orderId;
    return this;
  }

   /**
   * Eclipse Order Number
   * @return orderId
  **/
  @Schema(description = "Eclipse Order Number")
  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  public WarehousePickComplete generationId(Integer generationId) {
    this.generationId = generationId;
    return this;
  }

   /**
   * Eclipse Generation Number
   * @return generationId
  **/
  @Schema(description = "Eclipse Generation Number")
  public Integer getGenerationId() {
    return generationId;
  }

  public void setGenerationId(Integer generationId) {
    this.generationId = generationId;
  }

  public WarehousePickComplete lineId(Integer lineId) {
    this.lineId = lineId;
    return this;
  }

   /**
   * Line Item ID
   * @return lineId
  **/
  @Schema(description = "Line Item ID")
  public Integer getLineId() {
    return lineId;
  }

  public void setLineId(Integer lineId) {
    this.lineId = lineId;
  }

  public WarehousePickComplete shipVia(String shipVia) {
    this.shipVia = shipVia;
    return this;
  }

   /**
   * Eclipse Ship Via
   * @return shipVia
  **/
  @Schema(description = "Eclipse Ship Via")
  public String getShipVia() {
    return shipVia;
  }

  public void setShipVia(String shipVia) {
    this.shipVia = shipVia;
  }

  public WarehousePickComplete tote(String tote) {
    this.tote = tote;
    return this;
  }

   /**
   * Tote
   * @return tote
  **/
  @Schema(description = "Tote")
  public String getTote() {
    return tote;
  }

  public void setTote(String tote) {
    this.tote = tote;
  }

  public WarehousePickComplete userId(String userId) {
    this.userId = userId;
    return this;
  }

   /**
   * Eclipse Picker ID
   * @return userId
  **/
  @Schema(description = "Eclipse Picker ID")
  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public WarehousePickComplete branchId(String branchId) {
    this.branchId = branchId;
    return this;
  }

   /**
   * Eclipse Branch ID
   * @return branchId
  **/
  @Schema(description = "Eclipse Branch ID")
  public String getBranchId() {
    return branchId;
  }

  public void setBranchId(String branchId) {
    this.branchId = branchId;
  }

  public WarehousePickComplete cutDetail(String cutDetail) {
    this.cutDetail = cutDetail;
    return this;
  }

   /**
   * Cut Product Detail
   * @return cutDetail
  **/
  @Schema(description = "Cut Product Detail")
  public String getCutDetail() {
    return cutDetail;
  }

  public void setCutDetail(String cutDetail) {
    this.cutDetail = cutDetail;
  }

  public WarehousePickComplete cutGroup(String cutGroup) {
    this.cutGroup = cutGroup;
    return this;
  }

   /**
   * Cut Group
   * @return cutGroup
  **/
  @Schema(description = "Cut Group")
  public String getCutGroup() {
    return cutGroup;
  }

  public void setCutGroup(String cutGroup) {
    this.cutGroup = cutGroup;
  }

  public WarehousePickComplete isParallelCut(Boolean isParallelCut) {
    this.isParallelCut = isParallelCut;
    return this;
  }

   /**
   * if true this is a parallel cut
   * @return isParallelCut
  **/
  @Schema(description = "if true this is a parallel cut")
  public Boolean isIsParallelCut() {
    return isParallelCut;
  }

  public void setIsParallelCut(Boolean isParallelCut) {
    this.isParallelCut = isParallelCut;
  }

  public WarehousePickComplete warehouseID(String warehouseID) {
    this.warehouseID = warehouseID;
    return this;
  }

   /**
   * Eclipse Warehouse ID
   * @return warehouseID
  **/
  @Schema(description = "Eclipse Warehouse ID")
  public String getWarehouseID() {
    return warehouseID;
  }

  public void setWarehouseID(String warehouseID) {
    this.warehouseID = warehouseID;
  }

  public WarehousePickComplete isLot(String isLot) {
    this.isLot = isLot;
    return this;
  }

   /**
   * Identify if the item is \&quot;lot/detail\&quot; lot inventory
   * @return isLot
  **/
  @Schema(description = "Identify if the item is \"lot/detail\" lot inventory")
  public String getIsLot() {
    return isLot;
  }

  public void setIsLot(String isLot) {
    this.isLot = isLot;
  }

  public WarehousePickComplete isSerial(Boolean isSerial) {
    this.isSerial = isSerial;
    return this;
  }

   /**
   * Identify if the item is serialized
   * @return isSerial
  **/
  @Schema(description = "Identify if the item is serialized")
  public Boolean isIsSerial() {
    return isSerial;
  }

  public void setIsSerial(Boolean isSerial) {
    this.isSerial = isSerial;
  }

  public WarehousePickComplete pickGroup(String pickGroup) {
    this.pickGroup = pickGroup;
    return this;
  }

   /**
   * Desc: The Warehouse PickGroup  File: WHSE.QUEUE  Attr: 13
   * @return pickGroup
  **/
  @Schema(description = "Desc: The Warehouse PickGroup  File: WHSE.QUEUE  Attr: 13")
  public String getPickGroup() {
    return pickGroup;
  }

  public void setPickGroup(String pickGroup) {
    this.pickGroup = pickGroup;
  }

  public WarehousePickComplete isOverrideProduct(Boolean isOverrideProduct) {
    this.isOverrideProduct = isOverrideProduct;
    return this;
  }

   /**
   * Flag indicating if the Product was scaned as OVERRIDE.
   * @return isOverrideProduct
  **/
  @Schema(description = "Flag indicating if the Product was scaned as OVERRIDE.")
  public Boolean isIsOverrideProduct() {
    return isOverrideProduct;
  }

  public void setIsOverrideProduct(Boolean isOverrideProduct) {
    this.isOverrideProduct = isOverrideProduct;
  }

  public WarehousePickComplete startPickTime(LocalDateTime startPickTime) {
    this.startPickTime = startPickTime;
    return this;
  }

   /**
   * Time the picking started. Used for Report/log purposes.
   * @return startPickTime
  **/
  @Valid
  @Schema(description = "Time the picking started. Used for Report/log purposes.")
  public LocalDateTime getStartPickTime() {
    return startPickTime;
  }

  public void setStartPickTime(LocalDateTime startPickTime) {
    this.startPickTime = startPickTime;
  }

  public WarehousePickComplete ignoreLockToteCheck(Boolean ignoreLockToteCheck) {
    this.ignoreLockToteCheck = ignoreLockToteCheck;
    return this;
  }

   /**
   * Check Tote Prompt answer
   * @return ignoreLockToteCheck
  **/
  @Schema(description = "Check Tote Prompt answer")
  public Boolean isIgnoreLockToteCheck() {
    return ignoreLockToteCheck;
  }

  public void setIgnoreLockToteCheck(Boolean ignoreLockToteCheck) {
    this.ignoreLockToteCheck = ignoreLockToteCheck;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WarehousePickComplete warehousePickComplete = (WarehousePickComplete) o;
    return Objects.equals(this.productId, warehousePickComplete.productId) &&
        Objects.equals(this.description, warehousePickComplete.description) &&
        Objects.equals(this.quantity, warehousePickComplete.quantity) &&
        Objects.equals(this.uom, warehousePickComplete.uom) &&
        Objects.equals(this.locationType, warehousePickComplete.locationType) &&
        Objects.equals(this.location, warehousePickComplete.location) &&
        Objects.equals(this.lot, warehousePickComplete.lot) &&
        Objects.equals(this.splitId, warehousePickComplete.splitId) &&
        Objects.equals(this.orderId, warehousePickComplete.orderId) &&
        Objects.equals(this.generationId, warehousePickComplete.generationId) &&
        Objects.equals(this.lineId, warehousePickComplete.lineId) &&
        Objects.equals(this.shipVia, warehousePickComplete.shipVia) &&
        Objects.equals(this.tote, warehousePickComplete.tote) &&
        Objects.equals(this.userId, warehousePickComplete.userId) &&
        Objects.equals(this.branchId, warehousePickComplete.branchId) &&
        Objects.equals(this.cutDetail, warehousePickComplete.cutDetail) &&
        Objects.equals(this.cutGroup, warehousePickComplete.cutGroup) &&
        Objects.equals(this.isParallelCut, warehousePickComplete.isParallelCut) &&
        Objects.equals(this.warehouseID, warehousePickComplete.warehouseID) &&
        Objects.equals(this.isLot, warehousePickComplete.isLot) &&
        Objects.equals(this.isSerial, warehousePickComplete.isSerial) &&
        Objects.equals(this.pickGroup, warehousePickComplete.pickGroup) &&
        Objects.equals(this.isOverrideProduct, warehousePickComplete.isOverrideProduct) &&
        Objects.equals(this.startPickTime, warehousePickComplete.startPickTime) &&
        Objects.equals(this.ignoreLockToteCheck, warehousePickComplete.ignoreLockToteCheck);
  }

  @Override
  public int hashCode() {
    return Objects.hash(productId, description, quantity, uom, locationType, location, lot, splitId, orderId, generationId, lineId, shipVia, tote, userId, branchId, cutDetail, cutGroup, isParallelCut, warehouseID, isLot, isSerial, pickGroup, isOverrideProduct, startPickTime, ignoreLockToteCheck);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WarehousePickComplete {\n");
    
    sb.append("    productId: ").append(toIndentedString(productId)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    uom: ").append(toIndentedString(uom)).append("\n");
    sb.append("    locationType: ").append(toIndentedString(locationType)).append("\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
    sb.append("    lot: ").append(toIndentedString(lot)).append("\n");
    sb.append("    splitId: ").append(toIndentedString(splitId)).append("\n");
    sb.append("    orderId: ").append(toIndentedString(orderId)).append("\n");
    sb.append("    generationId: ").append(toIndentedString(generationId)).append("\n");
    sb.append("    lineId: ").append(toIndentedString(lineId)).append("\n");
    sb.append("    shipVia: ").append(toIndentedString(shipVia)).append("\n");
    sb.append("    tote: ").append(toIndentedString(tote)).append("\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    branchId: ").append(toIndentedString(branchId)).append("\n");
    sb.append("    cutDetail: ").append(toIndentedString(cutDetail)).append("\n");
    sb.append("    cutGroup: ").append(toIndentedString(cutGroup)).append("\n");
    sb.append("    isParallelCut: ").append(toIndentedString(isParallelCut)).append("\n");
    sb.append("    warehouseID: ").append(toIndentedString(warehouseID)).append("\n");
    sb.append("    isLot: ").append(toIndentedString(isLot)).append("\n");
    sb.append("    isSerial: ").append(toIndentedString(isSerial)).append("\n");
    sb.append("    pickGroup: ").append(toIndentedString(pickGroup)).append("\n");
    sb.append("    isOverrideProduct: ").append(toIndentedString(isOverrideProduct)).append("\n");
    sb.append("    startPickTime: ").append(toIndentedString(startPickTime)).append("\n");
    sb.append("    ignoreLockToteCheck: ").append(toIndentedString(ignoreLockToteCheck)).append("\n");
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
