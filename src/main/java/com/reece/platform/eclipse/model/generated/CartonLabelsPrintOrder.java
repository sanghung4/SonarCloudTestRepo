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
import com.reece.platform.eclipse.model.generated.LabelPrintSelect;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.*;
import javax.validation.Valid;
/**
 * Carton Labels Print Order
 */
@Schema(description = "Carton Labels Print Order")

public class CartonLabelsPrintOrder {
  @JsonProperty("cartonId")
  private String cartonId = null;

  @JsonProperty("orderId")
  private String orderId = null;

  @JsonProperty("generationId")
  private Integer generationId = null;

  @JsonProperty("printSelection")
  private LabelPrintSelect printSelection = null;

  @JsonProperty("printQuantity")
  private Integer printQuantity = null;

  @JsonProperty("boxedQuantity")
  private Integer boxedQuantity = null;

  public CartonLabelsPrintOrder cartonId(String cartonId) {
    this.cartonId = cartonId;
    return this;
  }

   /**
   * Carton format type
   * @return cartonId
  **/
  @NotNull
  @Schema(required = true, description = "Carton format type")
  public String getCartonId() {
    return cartonId;
  }

  public void setCartonId(String cartonId) {
    this.cartonId = cartonId;
  }

  public CartonLabelsPrintOrder orderId(String orderId) {
    this.orderId = orderId;
    return this;
  }

   /**
   * Order ID
   * @return orderId
  **/
  @NotNull
  @Schema(required = true, description = "Order ID")
  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  public CartonLabelsPrintOrder generationId(Integer generationId) {
    this.generationId = generationId;
    return this;
  }

   /**
   * Generation ID
   * @return generationId
  **/
  @NotNull
  @Schema(required = true, description = "Generation ID")
  public Integer getGenerationId() {
    return generationId;
  }

  public void setGenerationId(Integer generationId) {
    this.generationId = generationId;
  }

  public CartonLabelsPrintOrder printSelection(LabelPrintSelect printSelection) {
    this.printSelection = printSelection;
    return this;
  }

   /**
   * Get printSelection
   * @return printSelection
  **/
  @NotNull
  @Valid
  @Schema(required = true, description = "")
  public LabelPrintSelect getPrintSelection() {
    return printSelection;
  }

  public void setPrintSelection(LabelPrintSelect printSelection) {
    this.printSelection = printSelection;
  }

  public CartonLabelsPrintOrder printQuantity(Integer printQuantity) {
    this.printQuantity = printQuantity;
    return this;
  }

   /**
   * Quantity of labels to print
   * minimum: 1
   * maximum: 2147483647
   * @return printQuantity
  **/
  @NotNull
 @Min(1) @Max(2147483647)  @Schema(required = true, description = "Quantity of labels to print")
  public Integer getPrintQuantity() {
    return printQuantity;
  }

  public void setPrintQuantity(Integer printQuantity) {
    this.printQuantity = printQuantity;
  }

  public CartonLabelsPrintOrder boxedQuantity(Integer boxedQuantity) {
    this.boxedQuantity = boxedQuantity;
    return this;
  }

   /**
   * Items per box. Used only for the \&quot;Single Item\&quot; PrintSelection option.
   * minimum: 0
   * maximum: 2147483647
   * @return boxedQuantity
  **/
 @Min(0) @Max(2147483647)  @Schema(description = "Items per box. Used only for the \"Single Item\" PrintSelection option.")
  public Integer getBoxedQuantity() {
    return boxedQuantity;
  }

  public void setBoxedQuantity(Integer boxedQuantity) {
    this.boxedQuantity = boxedQuantity;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CartonLabelsPrintOrder cartonLabelsPrintOrder = (CartonLabelsPrintOrder) o;
    return Objects.equals(this.cartonId, cartonLabelsPrintOrder.cartonId) &&
        Objects.equals(this.orderId, cartonLabelsPrintOrder.orderId) &&
        Objects.equals(this.generationId, cartonLabelsPrintOrder.generationId) &&
        Objects.equals(this.printSelection, cartonLabelsPrintOrder.printSelection) &&
        Objects.equals(this.printQuantity, cartonLabelsPrintOrder.printQuantity) &&
        Objects.equals(this.boxedQuantity, cartonLabelsPrintOrder.boxedQuantity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cartonId, orderId, generationId, printSelection, printQuantity, boxedQuantity);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CartonLabelsPrintOrder {\n");
    
    sb.append("    cartonId: ").append(toIndentedString(cartonId)).append("\n");
    sb.append("    orderId: ").append(toIndentedString(orderId)).append("\n");
    sb.append("    generationId: ").append(toIndentedString(generationId)).append("\n");
    sb.append("    printSelection: ").append(toIndentedString(printSelection)).append("\n");
    sb.append("    printQuantity: ").append(toIndentedString(printQuantity)).append("\n");
    sb.append("    boxedQuantity: ").append(toIndentedString(boxedQuantity)).append("\n");
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
