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
 * CartonPackList
 */
@Schema(description = "CartonPackList")

public class CartonPackList {
  @JsonProperty("productId")
  private Integer productId = null;

  @JsonProperty("productDescription")
  private String productDescription = null;

  @JsonProperty("toPackQuantity")
  private Integer toPackQuantity = null;

  @JsonProperty("packedQuantity")
  private Integer packedQuantity = null;

  @JsonProperty("packageCount")
  private Integer packageCount = null;

  @JsonProperty("typeOfPackage")
  private String typeOfPackage = null;

  @JsonProperty("um")
  private String um = null;

  @JsonProperty("tote")
  private String tote = null;

  public CartonPackList productId(Integer productId) {
    this.productId = productId;
    return this;
  }

   /**
   * Product Id
   * @return productId
  **/
  @Schema(description = "Product Id")
  public Integer getProductId() {
    return productId;
  }

  public void setProductId(Integer productId) {
    this.productId = productId;
  }

  public CartonPackList productDescription(String productDescription) {
    this.productDescription = productDescription;
    return this;
  }

   /**
   * Product Description
   * @return productDescription
  **/
  @Schema(description = "Product Description")
  public String getProductDescription() {
    return productDescription;
  }

  public void setProductDescription(String productDescription) {
    this.productDescription = productDescription;
  }

  public CartonPackList toPackQuantity(Integer toPackQuantity) {
    this.toPackQuantity = toPackQuantity;
    return this;
  }

   /**
   * To Pack Quantity
   * @return toPackQuantity
  **/
  @Schema(description = "To Pack Quantity")
  public Integer getToPackQuantity() {
    return toPackQuantity;
  }

  public void setToPackQuantity(Integer toPackQuantity) {
    this.toPackQuantity = toPackQuantity;
  }

  public CartonPackList packedQuantity(Integer packedQuantity) {
    this.packedQuantity = packedQuantity;
    return this;
  }

   /**
   * Packed Quantity
   * @return packedQuantity
  **/
  @Schema(description = "Packed Quantity")
  public Integer getPackedQuantity() {
    return packedQuantity;
  }

  public void setPackedQuantity(Integer packedQuantity) {
    this.packedQuantity = packedQuantity;
  }

  public CartonPackList packageCount(Integer packageCount) {
    this.packageCount = packageCount;
    return this;
  }

   /**
   * Package quantity
   * @return packageCount
  **/
  @Schema(description = "Package quantity")
  public Integer getPackageCount() {
    return packageCount;
  }

  public void setPackageCount(Integer packageCount) {
    this.packageCount = packageCount;
  }

  public CartonPackList typeOfPackage(String typeOfPackage) {
    this.typeOfPackage = typeOfPackage;
    return this;
  }

   /**
   * Package Type
   * @return typeOfPackage
  **/
  @Schema(description = "Package Type")
  public String getTypeOfPackage() {
    return typeOfPackage;
  }

  public void setTypeOfPackage(String typeOfPackage) {
    this.typeOfPackage = typeOfPackage;
  }

  public CartonPackList um(String um) {
    this.um = um;
    return this;
  }

   /**
   * Unit of Measure
   * @return um
  **/
  @Schema(description = "Unit of Measure")
  public String getUm() {
    return um;
  }

  public void setUm(String um) {
    this.um = um;
  }

  public CartonPackList tote(String tote) {
    this.tote = tote;
    return this;
  }

   /**
   * Tote Id
   * @return tote
  **/
  @Schema(description = "Tote Id")
  public String getTote() {
    return tote;
  }

  public void setTote(String tote) {
    this.tote = tote;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CartonPackList cartonPackList = (CartonPackList) o;
    return Objects.equals(this.productId, cartonPackList.productId) &&
        Objects.equals(this.productDescription, cartonPackList.productDescription) &&
        Objects.equals(this.toPackQuantity, cartonPackList.toPackQuantity) &&
        Objects.equals(this.packedQuantity, cartonPackList.packedQuantity) &&
        Objects.equals(this.packageCount, cartonPackList.packageCount) &&
        Objects.equals(this.typeOfPackage, cartonPackList.typeOfPackage) &&
        Objects.equals(this.um, cartonPackList.um) &&
        Objects.equals(this.tote, cartonPackList.tote);
  }

  @Override
  public int hashCode() {
    return Objects.hash(productId, productDescription, toPackQuantity, packedQuantity, packageCount, typeOfPackage, um, tote);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CartonPackList {\n");
    
    sb.append("    productId: ").append(toIndentedString(productId)).append("\n");
    sb.append("    productDescription: ").append(toIndentedString(productDescription)).append("\n");
    sb.append("    toPackQuantity: ").append(toIndentedString(toPackQuantity)).append("\n");
    sb.append("    packedQuantity: ").append(toIndentedString(packedQuantity)).append("\n");
    sb.append("    packageCount: ").append(toIndentedString(packageCount)).append("\n");
    sb.append("    typeOfPackage: ").append(toIndentedString(typeOfPackage)).append("\n");
    sb.append("    um: ").append(toIndentedString(um)).append("\n");
    sb.append("    tote: ").append(toIndentedString(tote)).append("\n");
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
