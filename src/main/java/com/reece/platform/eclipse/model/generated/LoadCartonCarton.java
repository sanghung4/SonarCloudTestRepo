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
 * Object representing a Carton of a Load Carton Task
 */
@Schema(description = "Object representing a Carton of a Load Carton Task")

public class LoadCartonCarton {
  @JsonProperty("manifestID")
  private String manifestID = null;

  @JsonProperty("stopPosition")
  private Integer stopPosition = null;

  @JsonProperty("stopId")
  private String stopId = null;

  @JsonProperty("shipVia")
  private String shipVia = null;

  @JsonProperty("stagingLocation")
  private String stagingLocation = null;

  @JsonProperty("invoiceNumber")
  private String invoiceNumber = null;

  @JsonProperty("cartonID")
  private String cartonID = null;

  @JsonProperty("cartonNumber")
  private String cartonNumber = null;

  @JsonProperty("cartonType")
  private String cartonType = null;

  @JsonProperty("branchID")
  private String branchID = null;

  @JsonProperty("truckID")
  private String truckID = null;

  @JsonProperty("isSkippedCarton")
  private Boolean isSkippedCarton = null;

  @JsonProperty("actionFlag")
  private Integer actionFlag = null;

  @JsonProperty("confirmOutOfSequence")
  private Boolean confirmOutOfSequence = null;

  @JsonProperty("updateKey")
  private String updateKey = null;

  public LoadCartonCarton manifestID(String manifestID) {
    this.manifestID = manifestID;
    return this;
  }

   /**
   * Manifest ID
   * @return manifestID
  **/
  @Schema(description = "Manifest ID")
  public String getManifestID() {
    return manifestID;
  }

  public void setManifestID(String manifestID) {
    this.manifestID = manifestID;
  }

  public LoadCartonCarton stopPosition(Integer stopPosition) {
    this.stopPosition = stopPosition;
    return this;
  }

   /**
   * Stop Position
   * @return stopPosition
  **/
  @Schema(description = "Stop Position")
  public Integer getStopPosition() {
    return stopPosition;
  }

  public void setStopPosition(Integer stopPosition) {
    this.stopPosition = stopPosition;
  }

  public LoadCartonCarton stopId(String stopId) {
    this.stopId = stopId;
    return this;
  }

   /**
   * Stop ID
   * @return stopId
  **/
  @Schema(description = "Stop ID")
  public String getStopId() {
    return stopId;
  }

  public void setStopId(String stopId) {
    this.stopId = stopId;
  }

  public LoadCartonCarton shipVia(String shipVia) {
    this.shipVia = shipVia;
    return this;
  }

   /**
   * Ship Via
   * @return shipVia
  **/
  @Schema(description = "Ship Via")
  public String getShipVia() {
    return shipVia;
  }

  public void setShipVia(String shipVia) {
    this.shipVia = shipVia;
  }

  public LoadCartonCarton stagingLocation(String stagingLocation) {
    this.stagingLocation = stagingLocation;
    return this;
  }

   /**
   * Staging Location
   * @return stagingLocation
  **/
  @Schema(description = "Staging Location")
  public String getStagingLocation() {
    return stagingLocation;
  }

  public void setStagingLocation(String stagingLocation) {
    this.stagingLocation = stagingLocation;
  }

  public LoadCartonCarton invoiceNumber(String invoiceNumber) {
    this.invoiceNumber = invoiceNumber;
    return this;
  }

   /**
   * Invoice Number
   * @return invoiceNumber
  **/
  @Schema(description = "Invoice Number")
  public String getInvoiceNumber() {
    return invoiceNumber;
  }

  public void setInvoiceNumber(String invoiceNumber) {
    this.invoiceNumber = invoiceNumber;
  }

  public LoadCartonCarton cartonID(String cartonID) {
    this.cartonID = cartonID;
    return this;
  }

   /**
   * Carton ID
   * @return cartonID
  **/
  @NotNull
  @Schema(required = true, description = "Carton ID")
  public String getCartonID() {
    return cartonID;
  }

  public void setCartonID(String cartonID) {
    this.cartonID = cartonID;
  }

  public LoadCartonCarton cartonNumber(String cartonNumber) {
    this.cartonNumber = cartonNumber;
    return this;
  }

   /**
   * Carton Number
   * @return cartonNumber
  **/
  @Schema(description = "Carton Number")
  public String getCartonNumber() {
    return cartonNumber;
  }

  public void setCartonNumber(String cartonNumber) {
    this.cartonNumber = cartonNumber;
  }

  public LoadCartonCarton cartonType(String cartonType) {
    this.cartonType = cartonType;
    return this;
  }

   /**
   * CartonType
   * @return cartonType
  **/
  @Schema(description = "CartonType")
  public String getCartonType() {
    return cartonType;
  }

  public void setCartonType(String cartonType) {
    this.cartonType = cartonType;
  }

  public LoadCartonCarton branchID(String branchID) {
    this.branchID = branchID;
    return this;
  }

   /**
   * Branch ID
   * @return branchID
  **/
  @NotNull
  @Schema(required = true, description = "Branch ID")
  public String getBranchID() {
    return branchID;
  }

  public void setBranchID(String branchID) {
    this.branchID = branchID;
  }

  public LoadCartonCarton truckID(String truckID) {
    this.truckID = truckID;
    return this;
  }

   /**
   * Truck ID
   * @return truckID
  **/
  @NotNull
  @Schema(required = true, description = "Truck ID")
  public String getTruckID() {
    return truckID;
  }

  public void setTruckID(String truckID) {
    this.truckID = truckID;
  }

  public LoadCartonCarton isSkippedCarton(Boolean isSkippedCarton) {
    this.isSkippedCarton = isSkippedCarton;
    return this;
  }

   /**
   * Is Skipped Carton?
   * @return isSkippedCarton
  **/
  @Schema(description = "Is Skipped Carton?")
  public Boolean isIsSkippedCarton() {
    return isSkippedCarton;
  }

  public void setIsSkippedCarton(Boolean isSkippedCarton) {
    this.isSkippedCarton = isSkippedCarton;
  }

  public LoadCartonCarton actionFlag(Integer actionFlag) {
    this.actionFlag = actionFlag;
    return this;
  }

   /**
   * Action Flag  0 &#x3D; Load Carton (Default)  1 &#x3D; Skip Carton
   * @return actionFlag
  **/
  @Schema(description = "Action Flag  0 = Load Carton (Default)  1 = Skip Carton")
  public Integer getActionFlag() {
    return actionFlag;
  }

  public void setActionFlag(Integer actionFlag) {
    this.actionFlag = actionFlag;
  }

  public LoadCartonCarton confirmOutOfSequence(Boolean confirmOutOfSequence) {
    this.confirmOutOfSequence = confirmOutOfSequence;
    return this;
  }

   /**
   * Confirm load carton out of sequence  0 &#x3D; No (Default)  1 &#x3D; Yes
   * @return confirmOutOfSequence
  **/
  @Schema(description = "Confirm load carton out of sequence  0 = No (Default)  1 = Yes")
  public Boolean isConfirmOutOfSequence() {
    return confirmOutOfSequence;
  }

  public void setConfirmOutOfSequence(Boolean confirmOutOfSequence) {
    this.confirmOutOfSequence = confirmOutOfSequence;
  }

  public LoadCartonCarton updateKey(String updateKey) {
    this.updateKey = updateKey;
    return this;
  }

   /**
   * Update Key (Input Only)
   * @return updateKey
  **/
  @NotNull
  @Schema(required = true, description = "Update Key (Input Only)")
  public String getUpdateKey() {
    return updateKey;
  }

  public void setUpdateKey(String updateKey) {
    this.updateKey = updateKey;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LoadCartonCarton loadCartonCarton = (LoadCartonCarton) o;
    return Objects.equals(this.manifestID, loadCartonCarton.manifestID) &&
        Objects.equals(this.stopPosition, loadCartonCarton.stopPosition) &&
        Objects.equals(this.stopId, loadCartonCarton.stopId) &&
        Objects.equals(this.shipVia, loadCartonCarton.shipVia) &&
        Objects.equals(this.stagingLocation, loadCartonCarton.stagingLocation) &&
        Objects.equals(this.invoiceNumber, loadCartonCarton.invoiceNumber) &&
        Objects.equals(this.cartonID, loadCartonCarton.cartonID) &&
        Objects.equals(this.cartonNumber, loadCartonCarton.cartonNumber) &&
        Objects.equals(this.cartonType, loadCartonCarton.cartonType) &&
        Objects.equals(this.branchID, loadCartonCarton.branchID) &&
        Objects.equals(this.truckID, loadCartonCarton.truckID) &&
        Objects.equals(this.isSkippedCarton, loadCartonCarton.isSkippedCarton) &&
        Objects.equals(this.actionFlag, loadCartonCarton.actionFlag) &&
        Objects.equals(this.confirmOutOfSequence, loadCartonCarton.confirmOutOfSequence) &&
        Objects.equals(this.updateKey, loadCartonCarton.updateKey);
  }

  @Override
  public int hashCode() {
    return Objects.hash(manifestID, stopPosition, stopId, shipVia, stagingLocation, invoiceNumber, cartonID, cartonNumber, cartonType, branchID, truckID, isSkippedCarton, actionFlag, confirmOutOfSequence, updateKey);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LoadCartonCarton {\n");
    
    sb.append("    manifestID: ").append(toIndentedString(manifestID)).append("\n");
    sb.append("    stopPosition: ").append(toIndentedString(stopPosition)).append("\n");
    sb.append("    stopId: ").append(toIndentedString(stopId)).append("\n");
    sb.append("    shipVia: ").append(toIndentedString(shipVia)).append("\n");
    sb.append("    stagingLocation: ").append(toIndentedString(stagingLocation)).append("\n");
    sb.append("    invoiceNumber: ").append(toIndentedString(invoiceNumber)).append("\n");
    sb.append("    cartonID: ").append(toIndentedString(cartonID)).append("\n");
    sb.append("    cartonNumber: ").append(toIndentedString(cartonNumber)).append("\n");
    sb.append("    cartonType: ").append(toIndentedString(cartonType)).append("\n");
    sb.append("    branchID: ").append(toIndentedString(branchID)).append("\n");
    sb.append("    truckID: ").append(toIndentedString(truckID)).append("\n");
    sb.append("    isSkippedCarton: ").append(toIndentedString(isSkippedCarton)).append("\n");
    sb.append("    actionFlag: ").append(toIndentedString(actionFlag)).append("\n");
    sb.append("    confirmOutOfSequence: ").append(toIndentedString(confirmOutOfSequence)).append("\n");
    sb.append("    updateKey: ").append(toIndentedString(updateKey)).append("\n");
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
