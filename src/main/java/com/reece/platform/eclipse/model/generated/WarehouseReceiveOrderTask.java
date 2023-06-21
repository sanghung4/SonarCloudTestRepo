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
 * Object representing an Eclipse WarehouseReceiveOrderTask.
 */
@Schema(description = "Object representing an Eclipse WarehouseReceiveOrderTask.")

public class WarehouseReceiveOrderTask {
  @JsonProperty("orderId")
  private String orderId = null;

  @JsonProperty("generationId")
  private Integer generationId = null;

  @JsonProperty("lineItem")
  private Integer lineItem = null;

  @JsonProperty("productId")
  private Integer productId = null;

  @JsonProperty("locationType")
  private ProductLocationType locationType = null;

  @JsonProperty("fullLocation")
  private String fullLocation = null;

  @JsonProperty("requestedDate")
  private LocalDateTime requestedDate = null;

  @JsonProperty("isNewGen")
  private Boolean isNewGen = null;

  @JsonProperty("dtsCustomer")
  private Integer dtsCustomer = null;

  @JsonProperty("warehouseID")
  private String warehouseID = null;

  public WarehouseReceiveOrderTask orderId(String orderId) {
    this.orderId = orderId;
    return this;
  }

   /**
   * Desc: Order Id
   * @return orderId
  **/
  @NotNull
  @Schema(required = true, description = "Desc: Order Id")
  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  public WarehouseReceiveOrderTask generationId(Integer generationId) {
    this.generationId = generationId;
    return this;
  }

   /**
   * Desc: Generation Id
   * @return generationId
  **/
  @NotNull
  @Schema(required = true, description = "Desc: Generation Id")
  public Integer getGenerationId() {
    return generationId;
  }

  public void setGenerationId(Integer generationId) {
    this.generationId = generationId;
  }

  public WarehouseReceiveOrderTask lineItem(Integer lineItem) {
    this.lineItem = lineItem;
    return this;
  }

   /**
   * Desc: Line Item Id
   * @return lineItem
  **/
  @Schema(description = "Desc: Line Item Id")
  public Integer getLineItem() {
    return lineItem;
  }

  public void setLineItem(Integer lineItem) {
    this.lineItem = lineItem;
  }

  public WarehouseReceiveOrderTask productId(Integer productId) {
    this.productId = productId;
    return this;
  }

   /**
   * Desc: Product Number
   * @return productId
  **/
  @Schema(description = "Desc: Product Number")
  public Integer getProductId() {
    return productId;
  }

  public void setProductId(Integer productId) {
    this.productId = productId;
  }

  public WarehouseReceiveOrderTask locationType(ProductLocationType locationType) {
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

  public WarehouseReceiveOrderTask fullLocation(String fullLocation) {
    this.fullLocation = fullLocation;
    return this;
  }

   /**
   * Desc: Full Location
   * @return fullLocation
  **/
  @Schema(description = "Desc: Full Location")
  public String getFullLocation() {
    return fullLocation;
  }

  public void setFullLocation(String fullLocation) {
    this.fullLocation = fullLocation;
  }

  public WarehouseReceiveOrderTask requestedDate(LocalDateTime requestedDate) {
    this.requestedDate = requestedDate;
    return this;
  }

   /**
   * Desc: Requested Date
   * @return requestedDate
  **/
  @Valid
  @Schema(description = "Desc: Requested Date")
  public LocalDateTime getRequestedDate() {
    return requestedDate;
  }

  public void setRequestedDate(LocalDateTime requestedDate) {
    this.requestedDate = requestedDate;
  }

  public WarehouseReceiveOrderTask isNewGen(Boolean isNewGen) {
    this.isNewGen = isNewGen;
    return this;
  }

   /**
   * Desc: Flag to indicate you want to receive an individual item into a new Gen or an Existing Gen
   * @return isNewGen
  **/
  @Schema(description = "Desc: Flag to indicate you want to receive an individual item into a new Gen or an Existing Gen")
  public Boolean isIsNewGen() {
    return isNewGen;
  }

  public void setIsNewGen(Boolean isNewGen) {
    this.isNewGen = isNewGen;
  }

  public WarehouseReceiveOrderTask dtsCustomer(Integer dtsCustomer) {
    this.dtsCustomer = dtsCustomer;
    return this;
  }

   /**
   * DTS Customer
   * @return dtsCustomer
  **/
  @Schema(description = "DTS Customer")
  public Integer getDtsCustomer() {
    return dtsCustomer;
  }

  public void setDtsCustomer(Integer dtsCustomer) {
    this.dtsCustomer = dtsCustomer;
  }

  public WarehouseReceiveOrderTask warehouseID(String warehouseID) {
    this.warehouseID = warehouseID;
    return this;
  }

   /**
   * Desc: The Warehouse Queue ID
   * @return warehouseID
  **/
  @Schema(description = "Desc: The Warehouse Queue ID")
  public String getWarehouseID() {
    return warehouseID;
  }

  public void setWarehouseID(String warehouseID) {
    this.warehouseID = warehouseID;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WarehouseReceiveOrderTask warehouseReceiveOrderTask = (WarehouseReceiveOrderTask) o;
    return Objects.equals(this.orderId, warehouseReceiveOrderTask.orderId) &&
        Objects.equals(this.generationId, warehouseReceiveOrderTask.generationId) &&
        Objects.equals(this.lineItem, warehouseReceiveOrderTask.lineItem) &&
        Objects.equals(this.productId, warehouseReceiveOrderTask.productId) &&
        Objects.equals(this.locationType, warehouseReceiveOrderTask.locationType) &&
        Objects.equals(this.fullLocation, warehouseReceiveOrderTask.fullLocation) &&
        Objects.equals(this.requestedDate, warehouseReceiveOrderTask.requestedDate) &&
        Objects.equals(this.isNewGen, warehouseReceiveOrderTask.isNewGen) &&
        Objects.equals(this.dtsCustomer, warehouseReceiveOrderTask.dtsCustomer) &&
        Objects.equals(this.warehouseID, warehouseReceiveOrderTask.warehouseID);
  }

  @Override
  public int hashCode() {
    return Objects.hash(orderId, generationId, lineItem, productId, locationType, fullLocation, requestedDate, isNewGen, dtsCustomer, warehouseID);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WarehouseReceiveOrderTask {\n");
    
    sb.append("    orderId: ").append(toIndentedString(orderId)).append("\n");
    sb.append("    generationId: ").append(toIndentedString(generationId)).append("\n");
    sb.append("    lineItem: ").append(toIndentedString(lineItem)).append("\n");
    sb.append("    productId: ").append(toIndentedString(productId)).append("\n");
    sb.append("    locationType: ").append(toIndentedString(locationType)).append("\n");
    sb.append("    fullLocation: ").append(toIndentedString(fullLocation)).append("\n");
    sb.append("    requestedDate: ").append(toIndentedString(requestedDate)).append("\n");
    sb.append("    isNewGen: ").append(toIndentedString(isNewGen)).append("\n");
    sb.append("    dtsCustomer: ").append(toIndentedString(dtsCustomer)).append("\n");
    sb.append("    warehouseID: ").append(toIndentedString(warehouseID)).append("\n");
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
