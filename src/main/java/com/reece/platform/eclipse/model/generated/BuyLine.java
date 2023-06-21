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
import com.reece.platform.eclipse.model.generated.BuyLineCentralWarehouseTypes;
import com.reece.platform.eclipse.model.generated.LinkedBuyLine;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.*;
import javax.validation.Valid;
/**
 * Object representing an Eclipse BuyLine.
 */
@Schema(description = "Object representing an Eclipse BuyLine.")

public class BuyLine {
  @JsonProperty("updateKey")
  private String updateKey = null;

  @JsonProperty("id")
  private String id = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("centralWarehouseType")
  private BuyLineCentralWarehouseTypes centralWarehouseType = null;

  @JsonProperty("superBuyLineList")
  private List<LinkedBuyLine> superBuyLineList = null;

  @JsonProperty("combineOnCentralPurchaseOrder")
  private Boolean combineOnCentralPurchaseOrder = null;

  @JsonProperty("procureGroupId")
  private String procureGroupId = null;

  @JsonProperty("defaultNonstockTemplateId")
  private Integer defaultNonstockTemplateId = null;

  @JsonProperty("disableNonstockCreditCheck")
  private Boolean disableNonstockCreditCheck = null;

  @JsonProperty("createCentralPOTransfers")
  private Boolean createCentralPOTransfers = null;

  @JsonProperty("usePOCostOnCentralPOTransfer")
  private Boolean usePOCostOnCentralPOTransfer = null;

  public BuyLine updateKey(String updateKey) {
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

  public BuyLine id(String id) {
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

  public BuyLine description(String description) {
    this.description = description;
    return this;
  }

   /**
   * Desc: The BuyLine Description  File: BUY.LINE  Attr: 1
   * @return description
  **/
  @Schema(description = "Desc: The BuyLine Description  File: BUY.LINE  Attr: 1")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public BuyLine centralWarehouseType(BuyLineCentralWarehouseTypes centralWarehouseType) {
    this.centralWarehouseType = centralWarehouseType;
    return this;
  }

   /**
   * Get centralWarehouseType
   * @return centralWarehouseType
  **/
  @Valid
  @Schema(description = "")
  public BuyLineCentralWarehouseTypes getCentralWarehouseType() {
    return centralWarehouseType;
  }

  public void setCentralWarehouseType(BuyLineCentralWarehouseTypes centralWarehouseType) {
    this.centralWarehouseType = centralWarehouseType;
  }

  public BuyLine superBuyLineList(List<LinkedBuyLine> superBuyLineList) {
    this.superBuyLineList = superBuyLineList;
    return this;
  }

  public BuyLine addSuperBuyLineListItem(LinkedBuyLine superBuyLineListItem) {
    if (this.superBuyLineList == null) {
      this.superBuyLineList = new ArrayList<>();
    }
    this.superBuyLineList.add(superBuyLineListItem);
    return this;
  }

   /**
   * Desc: List of Buy Lines grouped together to make it a Super Buy Line.  File: BUY.LINE  Attr: 4
   * @return superBuyLineList
  **/
  @Valid
  @Schema(description = "Desc: List of Buy Lines grouped together to make it a Super Buy Line.  File: BUY.LINE  Attr: 4")
  public List<LinkedBuyLine> getSuperBuyLineList() {
    return superBuyLineList;
  }

  public void setSuperBuyLineList(List<LinkedBuyLine> superBuyLineList) {
    this.superBuyLineList = superBuyLineList;
  }

  public BuyLine combineOnCentralPurchaseOrder(Boolean combineOnCentralPurchaseOrder) {
    this.combineOnCentralPurchaseOrder = combineOnCentralPurchaseOrder;
    return this;
  }

   /**
   * Desc: The BuyLine CombineOnCentralPurchaseOrder flag  File: BUY.LINE  Attr: 5
   * @return combineOnCentralPurchaseOrder
  **/
  @Schema(description = "Desc: The BuyLine CombineOnCentralPurchaseOrder flag  File: BUY.LINE  Attr: 5")
  public Boolean isCombineOnCentralPurchaseOrder() {
    return combineOnCentralPurchaseOrder;
  }

  public void setCombineOnCentralPurchaseOrder(Boolean combineOnCentralPurchaseOrder) {
    this.combineOnCentralPurchaseOrder = combineOnCentralPurchaseOrder;
  }

  public BuyLine procureGroupId(String procureGroupId) {
    this.procureGroupId = procureGroupId;
    return this;
  }

   /**
   * Desc: The BuyLine ProcureGroupId  File: BUY.LINE  Attr: 6
   * @return procureGroupId
  **/
  @Schema(description = "Desc: The BuyLine ProcureGroupId  File: BUY.LINE  Attr: 6")
  public String getProcureGroupId() {
    return procureGroupId;
  }

  public void setProcureGroupId(String procureGroupId) {
    this.procureGroupId = procureGroupId;
  }

  public BuyLine defaultNonstockTemplateId(Integer defaultNonstockTemplateId) {
    this.defaultNonstockTemplateId = defaultNonstockTemplateId;
    return this;
  }

   /**
   * Desc: The BuyLine DefaultNonstockTemplateId  File: BUY.LINE  Attr: 7
   * @return defaultNonstockTemplateId
  **/
  @Schema(description = "Desc: The BuyLine DefaultNonstockTemplateId  File: BUY.LINE  Attr: 7")
  public Integer getDefaultNonstockTemplateId() {
    return defaultNonstockTemplateId;
  }

  public void setDefaultNonstockTemplateId(Integer defaultNonstockTemplateId) {
    this.defaultNonstockTemplateId = defaultNonstockTemplateId;
  }

  public BuyLine disableNonstockCreditCheck(Boolean disableNonstockCreditCheck) {
    this.disableNonstockCreditCheck = disableNonstockCreditCheck;
    return this;
  }

   /**
   * Desc: The BuyLine DisableNonstockCreditCheck flag  File: BUY.LINE  Attr: 33
   * @return disableNonstockCreditCheck
  **/
  @Schema(description = "Desc: The BuyLine DisableNonstockCreditCheck flag  File: BUY.LINE  Attr: 33")
  public Boolean isDisableNonstockCreditCheck() {
    return disableNonstockCreditCheck;
  }

  public void setDisableNonstockCreditCheck(Boolean disableNonstockCreditCheck) {
    this.disableNonstockCreditCheck = disableNonstockCreditCheck;
  }

  public BuyLine createCentralPOTransfers(Boolean createCentralPOTransfers) {
    this.createCentralPOTransfers = createCentralPOTransfers;
    return this;
  }

   /**
   * Desc: The BuyLine CreateCentralPOTransfers flag  File: BUY.LINE  Attr: 35
   * @return createCentralPOTransfers
  **/
  @Schema(description = "Desc: The BuyLine CreateCentralPOTransfers flag  File: BUY.LINE  Attr: 35")
  public Boolean isCreateCentralPOTransfers() {
    return createCentralPOTransfers;
  }

  public void setCreateCentralPOTransfers(Boolean createCentralPOTransfers) {
    this.createCentralPOTransfers = createCentralPOTransfers;
  }

  public BuyLine usePOCostOnCentralPOTransfer(Boolean usePOCostOnCentralPOTransfer) {
    this.usePOCostOnCentralPOTransfer = usePOCostOnCentralPOTransfer;
    return this;
  }

   /**
   * Desc: The BuyLine UsePOCostOnCentralPOTransfer flag  File: BUY.LINE  Attr: 36
   * @return usePOCostOnCentralPOTransfer
  **/
  @Schema(description = "Desc: The BuyLine UsePOCostOnCentralPOTransfer flag  File: BUY.LINE  Attr: 36")
  public Boolean isUsePOCostOnCentralPOTransfer() {
    return usePOCostOnCentralPOTransfer;
  }

  public void setUsePOCostOnCentralPOTransfer(Boolean usePOCostOnCentralPOTransfer) {
    this.usePOCostOnCentralPOTransfer = usePOCostOnCentralPOTransfer;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BuyLine buyLine = (BuyLine) o;
    return Objects.equals(this.updateKey, buyLine.updateKey) &&
        Objects.equals(this.id, buyLine.id) &&
        Objects.equals(this.description, buyLine.description) &&
        Objects.equals(this.centralWarehouseType, buyLine.centralWarehouseType) &&
        Objects.equals(this.superBuyLineList, buyLine.superBuyLineList) &&
        Objects.equals(this.combineOnCentralPurchaseOrder, buyLine.combineOnCentralPurchaseOrder) &&
        Objects.equals(this.procureGroupId, buyLine.procureGroupId) &&
        Objects.equals(this.defaultNonstockTemplateId, buyLine.defaultNonstockTemplateId) &&
        Objects.equals(this.disableNonstockCreditCheck, buyLine.disableNonstockCreditCheck) &&
        Objects.equals(this.createCentralPOTransfers, buyLine.createCentralPOTransfers) &&
        Objects.equals(this.usePOCostOnCentralPOTransfer, buyLine.usePOCostOnCentralPOTransfer);
  }

  @Override
  public int hashCode() {
    return Objects.hash(updateKey, id, description, centralWarehouseType, superBuyLineList, combineOnCentralPurchaseOrder, procureGroupId, defaultNonstockTemplateId, disableNonstockCreditCheck, createCentralPOTransfers, usePOCostOnCentralPOTransfer);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BuyLine {\n");
    
    sb.append("    updateKey: ").append(toIndentedString(updateKey)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    centralWarehouseType: ").append(toIndentedString(centralWarehouseType)).append("\n");
    sb.append("    superBuyLineList: ").append(toIndentedString(superBuyLineList)).append("\n");
    sb.append("    combineOnCentralPurchaseOrder: ").append(toIndentedString(combineOnCentralPurchaseOrder)).append("\n");
    sb.append("    procureGroupId: ").append(toIndentedString(procureGroupId)).append("\n");
    sb.append("    defaultNonstockTemplateId: ").append(toIndentedString(defaultNonstockTemplateId)).append("\n");
    sb.append("    disableNonstockCreditCheck: ").append(toIndentedString(disableNonstockCreditCheck)).append("\n");
    sb.append("    createCentralPOTransfers: ").append(toIndentedString(createCentralPOTransfers)).append("\n");
    sb.append("    usePOCostOnCentralPOTransfer: ").append(toIndentedString(usePOCostOnCentralPOTransfer)).append("\n");
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
