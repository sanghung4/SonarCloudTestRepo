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
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.*;
import javax.validation.Valid;
/**
 * Object representing an Eclipse WarehouseTask.
 */
@Schema(description = "Object representing an Eclipse WarehouseTask.")

public class WarehouseToteTask {
  @JsonProperty("invoiceNumber")
  private String invoiceNumber = null;

  @JsonProperty("branchId")
  private String branchId = null;

  @JsonProperty("location")
  private String location = null;

  @JsonProperty("tote")
  private String tote = null;

  @JsonProperty("updateLocationOnly")
  private Boolean updateLocationOnly = null;

  @JsonProperty("taskStatus")
  private String taskStatus = null;

  public WarehouseToteTask invoiceNumber(String invoiceNumber) {
    this.invoiceNumber = invoiceNumber;
    return this;
  }

   /**
   * Order Id
   * @return invoiceNumber
  **/
  @NotNull
  @Schema(required = true, description = "Order Id")
  public String getInvoiceNumber() {
    return invoiceNumber;
  }

  public void setInvoiceNumber(String invoiceNumber) {
    this.invoiceNumber = invoiceNumber;
  }

  public WarehouseToteTask branchId(String branchId) {
    this.branchId = branchId;
    return this;
  }

   /**
   * Branch Id
   * @return branchId
  **/
  @NotNull
  @Schema(required = true, description = "Branch Id")
  public String getBranchId() {
    return branchId;
  }

  public void setBranchId(String branchId) {
    this.branchId = branchId;
  }

  public WarehouseToteTask location(String location) {
    this.location = location;
    return this;
  }

   /**
   * Staging Location. If left blank the Staging Action won&#x27;t happen but Order Status will still be updated.
   * @return location
  **/
  @Schema(description = "Staging Location. If left blank the Staging Action won't happen but Order Status will still be updated.")
  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public WarehouseToteTask tote(String tote) {
    this.tote = tote;
    return this;
  }

   /**
   * Tote name
   * @return tote
  **/
  @NotNull
  @Schema(required = true, description = "Tote name")
  public String getTote() {
    return tote;
  }

  public void setTote(String tote) {
    this.tote = tote;
  }

  public WarehouseToteTask updateLocationOnly(Boolean updateLocationOnly) {
    this.updateLocationOnly = updateLocationOnly;
    return this;
  }

   /**
   * If true, this will just update the Staging Location. Tote must be already staged first.
   * @return updateLocationOnly
  **/
  @Schema(description = "If true, this will just update the Staging Location. Tote must be already staged first.")
  public Boolean isUpdateLocationOnly() {
    return updateLocationOnly;
  }

  public void setUpdateLocationOnly(Boolean updateLocationOnly) {
    this.updateLocationOnly = updateLocationOnly;
  }

  public WarehouseToteTask taskStatus(String taskStatus) {
    this.taskStatus = taskStatus;
    return this;
  }

   /**
   * Staging TaskStatus.
   * @return taskStatus
  **/
  @Schema(description = "Staging TaskStatus.")
  public String getTaskStatus() {
    return taskStatus;
  }

  public void setTaskStatus(String taskStatus) {
    this.taskStatus = taskStatus;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WarehouseToteTask warehouseToteTask = (WarehouseToteTask) o;
    return Objects.equals(this.invoiceNumber, warehouseToteTask.invoiceNumber) &&
        Objects.equals(this.branchId, warehouseToteTask.branchId) &&
        Objects.equals(this.location, warehouseToteTask.location) &&
        Objects.equals(this.tote, warehouseToteTask.tote) &&
        Objects.equals(this.updateLocationOnly, warehouseToteTask.updateLocationOnly) &&
        Objects.equals(this.taskStatus, warehouseToteTask.taskStatus);
  }

  @Override
  public int hashCode() {
    return Objects.hash(invoiceNumber, branchId, location, tote, updateLocationOnly, taskStatus);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WarehouseToteTask {\n");
    
    sb.append("    invoiceNumber: ").append(toIndentedString(invoiceNumber)).append("\n");
    sb.append("    branchId: ").append(toIndentedString(branchId)).append("\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
    sb.append("    tote: ").append(toIndentedString(tote)).append("\n");
    sb.append("    updateLocationOnly: ").append(toIndentedString(updateLocationOnly)).append("\n");
    sb.append("    taskStatus: ").append(toIndentedString(taskStatus)).append("\n");
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
