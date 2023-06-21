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
import com.reece.platform.eclipse.model.generated.LabelFormatType;
import com.reece.platform.eclipse.model.generated.WarehouseRFLabelsPrintOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.*;
import javax.validation.Valid;
/**
 * Warehouse RF Labels Print Task
 */
@Schema(description = "Warehouse RF Labels Print Task")

public class WarehouseRFLabelsPrintTask {
  @JsonProperty("labelFormatName")
  private String labelFormatName = null;

  @JsonProperty("formatType")
  private LabelFormatType formatType = null;

  @JsonProperty("userId")
  private String userId = null;

  @JsonProperty("printOrderList")
  private List<WarehouseRFLabelsPrintOrder> printOrderList = new ArrayList<>();

  @JsonProperty("pickGroup")
  private String pickGroup = null;

  @JsonProperty("printerID")
  private String printerID = null;

  public WarehouseRFLabelsPrintTask labelFormatName(String labelFormatName) {
    this.labelFormatName = labelFormatName;
    return this;
  }

   /**
   * Label format name
   * @return labelFormatName
  **/
  @NotNull
  @Schema(required = true, description = "Label format name")
  public String getLabelFormatName() {
    return labelFormatName;
  }

  public void setLabelFormatName(String labelFormatName) {
    this.labelFormatName = labelFormatName;
  }

  public WarehouseRFLabelsPrintTask formatType(LabelFormatType formatType) {
    this.formatType = formatType;
    return this;
  }

   /**
   * Get formatType
   * @return formatType
  **/
  @Valid
  @Schema(description = "")
  public LabelFormatType getFormatType() {
    return formatType;
  }

  public void setFormatType(LabelFormatType formatType) {
    this.formatType = formatType;
  }

  public WarehouseRFLabelsPrintTask userId(String userId) {
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

  public WarehouseRFLabelsPrintTask printOrderList(List<WarehouseRFLabelsPrintOrder> printOrderList) {
    this.printOrderList = printOrderList;
    return this;
  }

  public WarehouseRFLabelsPrintTask addPrintOrderListItem(WarehouseRFLabelsPrintOrder printOrderListItem) {
    this.printOrderList.add(printOrderListItem);
    return this;
  }

   /**
   * List of orders to print
   * @return printOrderList
  **/
  @NotNull
  @Valid
  @Schema(required = true, description = "List of orders to print")
  public List<WarehouseRFLabelsPrintOrder> getPrintOrderList() {
    return printOrderList;
  }

  public void setPrintOrderList(List<WarehouseRFLabelsPrintOrder> printOrderList) {
    this.printOrderList = printOrderList;
  }

  public WarehouseRFLabelsPrintTask pickGroup(String pickGroup) {
    this.pickGroup = pickGroup;
    return this;
  }

   /**
   * Filters orders by their items&#x27; Pick Group  NOTE: The suggested default value for this filter is \&quot;ALL\&quot;. An empty string (\&quot;\&quot;) is an applicable Pick Group value.
   * @return pickGroup
  **/
  @Schema(description = "Filters orders by their items' Pick Group  NOTE: The suggested default value for this filter is \"ALL\". An empty string (\"\") is an applicable Pick Group value.")
  public String getPickGroup() {
    return pickGroup;
  }

  public void setPickGroup(String pickGroup) {
    this.pickGroup = pickGroup;
  }

  public WarehouseRFLabelsPrintTask printerID(String printerID) {
    this.printerID = printerID;
    return this;
  }

   /**
   * Printer ID
   * @return printerID
  **/
  @Schema(description = "Printer ID")
  public String getPrinterID() {
    return printerID;
  }

  public void setPrinterID(String printerID) {
    this.printerID = printerID;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WarehouseRFLabelsPrintTask warehouseRFLabelsPrintTask = (WarehouseRFLabelsPrintTask) o;
    return Objects.equals(this.labelFormatName, warehouseRFLabelsPrintTask.labelFormatName) &&
        Objects.equals(this.formatType, warehouseRFLabelsPrintTask.formatType) &&
        Objects.equals(this.userId, warehouseRFLabelsPrintTask.userId) &&
        Objects.equals(this.printOrderList, warehouseRFLabelsPrintTask.printOrderList) &&
        Objects.equals(this.pickGroup, warehouseRFLabelsPrintTask.pickGroup) &&
        Objects.equals(this.printerID, warehouseRFLabelsPrintTask.printerID);
  }

  @Override
  public int hashCode() {
    return Objects.hash(labelFormatName, formatType, userId, printOrderList, pickGroup, printerID);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WarehouseRFLabelsPrintTask {\n");
    
    sb.append("    labelFormatName: ").append(toIndentedString(labelFormatName)).append("\n");
    sb.append("    formatType: ").append(toIndentedString(formatType)).append("\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    printOrderList: ").append(toIndentedString(printOrderList)).append("\n");
    sb.append("    pickGroup: ").append(toIndentedString(pickGroup)).append("\n");
    sb.append("    printerID: ").append(toIndentedString(printerID)).append("\n");
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
