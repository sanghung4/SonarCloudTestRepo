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

package com.reece.platform.eclipse.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Object representing an Eclipse WarehouseTask.
 */
@Schema(description = "Object representing an Eclipse WarehouseTask.")
public class WarehousePickTask {

    @JsonProperty("orderId")
    private String orderId = null;

    @JsonProperty("generationId")
    private Integer generationId = null;

    @JsonProperty("invoiceId")
    private String invoiceId = null;

    @JsonProperty("branchId")
    private String branchId = null;

    @JsonProperty("pickGroup")
    private String pickGroup = null;

    @JsonProperty("assignedUserId")
    private String assignedUserId = null;

    @JsonProperty("billTo")
    private Integer billTo = null;

    @JsonProperty("shipTo")
    private Integer shipTo = null;

    @JsonProperty("shipToName")
    private String shipToName = null;

    @JsonProperty("pickCount")
    private String pickCount = null;

    @JsonProperty("shipVia")
    private String shipVia = null;

    @JsonProperty("isFromMultipleZones")
    private Boolean isFromMultipleZones = null;

    @JsonProperty("taskState")
    private PickTaskState taskState = null;

    @JsonProperty("taskWeight")
    private Double taskWeight = null;

    public WarehousePickTask orderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    /**
     * Desc: The Warehouse Task Order Id
     * @return orderId
     **/
    @NotNull
    @Schema(required = true, description = "Desc: The Warehouse Task Order Id")
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public WarehousePickTask generationId(Integer generationId) {
        this.generationId = generationId;
        return this;
    }

    /**
     * Desc: The Warehouse Task Order Generation
     * @return generationId
     **/
    @NotNull
    @Schema(required = true, description = "Desc: The Warehouse Task Order Generation")
    public Integer getGenerationId() {
        return generationId;
    }

    public void setGenerationId(Integer generationId) {
        this.generationId = generationId;
    }

    public WarehousePickTask invoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
        return this;
    }

    /**
     * �The�Warehouse�Order�Invoice�number  �
     * @return invoiceId
     **/
    @Schema(description = "�The�Warehouse�Order�Invoice�number  �")
    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public WarehousePickTask branchId(String branchId) {
        this.branchId = branchId;
        return this;
    }

    /**
     * Desc: The Warehouse Task Branch Id
     * @return branchId
     **/
    @NotNull
    @Schema(required = true, description = "Desc: The Warehouse Task Branch Id")
    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public WarehousePickTask pickGroup(String pickGroup) {
        this.pickGroup = pickGroup;
        return this;
    }

    /**
     * Desc: The Warehouse Task Pick Group
     * @return pickGroup
     **/
    @Schema(description = "Desc: The Warehouse Task Pick Group")
    public String getPickGroup() {
        return pickGroup;
    }

    public void setPickGroup(String pickGroup) {
        this.pickGroup = pickGroup;
    }

    public WarehousePickTask assignedUserId(String assignedUserId) {
        this.assignedUserId = assignedUserId;
        return this;
    }

    /**
     * Desc: The Warehouse Task User Id Assigned
     * @return assignedUserId
     **/
    @Schema(description = "Desc: The Warehouse Task User Id Assigned")
    public String getAssignedUserId() {
        return assignedUserId;
    }

    public void setAssignedUserId(String assignedUserId) {
        this.assignedUserId = assignedUserId;
    }

    public WarehousePickTask billTo(Integer billTo) {
        this.billTo = billTo;
        return this;
    }

    /**
     * Desc: The Warehouse Task BillTo Entitity
     * @return billTo
     **/
    @Schema(description = "Desc: The Warehouse Task BillTo Entitity")
    public Integer getBillTo() {
        return billTo;
    }

    public void setBillTo(Integer billTo) {
        this.billTo = billTo;
    }

    public WarehousePickTask shipTo(Integer shipTo) {
        this.shipTo = shipTo;
        return this;
    }

    /**
     * Desc: The Warehouse Task ShipTo Entitity
     * @return shipTo
     **/
    @Schema(description = "Desc: The Warehouse Task ShipTo Entitity")
    public Integer getShipTo() {
        return shipTo;
    }

    public void setShipTo(Integer shipTo) {
        this.shipTo = shipTo;
    }

    public WarehousePickTask shipToName(String shipToName) {
        this.shipToName = shipToName;
        return this;
    }

    /**
     * Desc: The Warehouse Task ShipTo Name
     * @return shipToName
     **/
    @Schema(description = "Desc: The Warehouse Task ShipTo Name")
    public String getShipToName() {
        return shipToName;
    }

    public void setShipToName(String shipToName) {
        this.shipToName = shipToName;
    }

    public WarehousePickTask pickCount(String pickCount) {
        this.pickCount = pickCount;
        return this;
    }

    /**
     * Desc: Then Number of Picks this WarehouseTask has.
     * @return pickCount
     **/
    @Schema(description = "Desc: Then Number of Picks this WarehouseTask has.")
    public String getPickCount() {
        return pickCount;
    }

    public void setPickCount(String pickCount) {
        this.pickCount = pickCount;
    }

    public WarehousePickTask shipVia(String shipVia) {
        this.shipVia = shipVia;
        return this;
    }

    /**
     * Desc: The Warehouse Task Ship Via
     * @return shipVia
     **/
    @Schema(description = "Desc: The Warehouse Task Ship Via")
    public String getShipVia() {
        return shipVia;
    }

    public void setShipVia(String shipVia) {
        this.shipVia = shipVia;
    }

    public WarehousePickTask isFromMultipleZones(Boolean isFromMultipleZones) {
        this.isFromMultipleZones = isFromMultipleZones;
        return this;
    }

    /**
     * Desc: TaskIsFromMultipleZones flag indicates the pick is a complet order combined from multiple zones
     * @return isFromMultipleZones
     **/
    @Schema(
        description = "Desc: TaskIsFromMultipleZones flag indicates the pick is a complet order combined from multiple zones"
    )
    public Boolean isIsFromMultipleZones() {
        return isFromMultipleZones;
    }

    public void setIsFromMultipleZones(Boolean isFromMultipleZones) {
        this.isFromMultipleZones = isFromMultipleZones;
    }

    public WarehousePickTask taskState(PickTaskState taskState) {
        this.taskState = taskState;
        return this;
    }

    /**
     * Get taskState
     * @return taskState
     **/
    @NotNull
    @Valid
    @Schema(required = true, description = "")
    public PickTaskState getTaskState() {
        return taskState;
    }

    public void setTaskState(PickTaskState taskState) {
        this.taskState = taskState;
    }

    public WarehousePickTask taskWeight(Double taskWeight) {
        this.taskWeight = taskWeight;
        return this;
    }

    /**
     * Desc: The Warehouse Task Weight
     * @return taskWeight
     **/
    @Schema(description = "Desc: The Warehouse Task Weight")
    public Double getTaskWeight() {
        return taskWeight;
    }

    public void setTaskWeight(Double taskWeight) {
        this.taskWeight = taskWeight;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WarehousePickTask warehousePickTask = (WarehousePickTask) o;
        return (
            Objects.equals(this.orderId, warehousePickTask.orderId) &&
            Objects.equals(this.generationId, warehousePickTask.generationId) &&
            Objects.equals(this.invoiceId, warehousePickTask.invoiceId) &&
            Objects.equals(this.branchId, warehousePickTask.branchId) &&
            Objects.equals(this.pickGroup, warehousePickTask.pickGroup) &&
            Objects.equals(this.assignedUserId, warehousePickTask.assignedUserId) &&
            Objects.equals(this.billTo, warehousePickTask.billTo) &&
            Objects.equals(this.shipTo, warehousePickTask.shipTo) &&
            Objects.equals(this.shipToName, warehousePickTask.shipToName) &&
            Objects.equals(this.pickCount, warehousePickTask.pickCount) &&
            Objects.equals(this.shipVia, warehousePickTask.shipVia) &&
            Objects.equals(this.isFromMultipleZones, warehousePickTask.isFromMultipleZones) &&
            Objects.equals(this.taskState, warehousePickTask.taskState) &&
            Objects.equals(this.taskWeight, warehousePickTask.taskWeight)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            orderId,
            generationId,
            invoiceId,
            branchId,
            pickGroup,
            assignedUserId,
            billTo,
            shipTo,
            shipToName,
            pickCount,
            shipVia,
            isFromMultipleZones,
            taskState,
            taskWeight
        );
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class WarehousePickTask {\n");

        sb.append("    orderId: ").append(toIndentedString(orderId)).append("\n");
        sb.append("    generationId: ").append(toIndentedString(generationId)).append("\n");
        sb.append("    invoiceId: ").append(toIndentedString(invoiceId)).append("\n");
        sb.append("    branchId: ").append(toIndentedString(branchId)).append("\n");
        sb.append("    pickGroup: ").append(toIndentedString(pickGroup)).append("\n");
        sb.append("    assignedUserId: ").append(toIndentedString(assignedUserId)).append("\n");
        sb.append("    billTo: ").append(toIndentedString(billTo)).append("\n");
        sb.append("    shipTo: ").append(toIndentedString(shipTo)).append("\n");
        sb.append("    shipToName: ").append(toIndentedString(shipToName)).append("\n");
        sb.append("    pickCount: ").append(toIndentedString(pickCount)).append("\n");
        sb.append("    shipVia: ").append(toIndentedString(shipVia)).append("\n");
        sb.append("    isFromMultipleZones: ").append(toIndentedString(isFromMultipleZones)).append("\n");
        sb.append("    taskState: ").append(toIndentedString(taskState)).append("\n");
        sb.append("    taskWeight: ").append(toIndentedString(taskWeight)).append("\n");
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
