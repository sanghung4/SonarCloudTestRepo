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
import com.reece.platform.eclipse.model.generated.ProductSecondaryUpcList;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.*;
import javax.validation.Valid;
/**
 * Object representing an Eclipse WarehouseProductUPCs.
 */
@Schema(description = "Object representing an Eclipse WarehouseProductUPCs.")

public class WarehouseProductUPCs {
  @JsonProperty("productId")
  private String productId = null;

  @JsonProperty("productDescription")
  private String productDescription = null;

  @JsonProperty("upc")
  private String upc = null;

  @JsonProperty("productSecondaryUPCs")
  private List<ProductSecondaryUpcList> productSecondaryUPCs = null;

  public WarehouseProductUPCs productId(String productId) {
    this.productId = productId;
    return this;
  }

   /**
   * Desc: the Product Id
   * @return productId
  **/
  @NotNull
  @Schema(required = true, description = "Desc: the Product Id")
  public String getProductId() {
    return productId;
  }

  public void setProductId(String productId) {
    this.productId = productId;
  }

  public WarehouseProductUPCs productDescription(String productDescription) {
    this.productDescription = productDescription;
    return this;
  }

   /**
   * Desc: the product description
   * @return productDescription
  **/
  @Schema(description = "Desc: the product description")
  public String getProductDescription() {
    return productDescription;
  }

  public void setProductDescription(String productDescription) {
    this.productDescription = productDescription;
  }

  public WarehouseProductUPCs upc(String upc) {
    this.upc = upc;
    return this;
  }

   /**
   * Desc: PrimaryUpc
   * @return upc
  **/
  @Schema(description = "Desc: PrimaryUpc")
  public String getUpc() {
    return upc;
  }

  public void setUpc(String upc) {
    this.upc = upc;
  }

  public WarehouseProductUPCs productSecondaryUPCs(List<ProductSecondaryUpcList> productSecondaryUPCs) {
    this.productSecondaryUPCs = productSecondaryUPCs;
    return this;
  }

  public WarehouseProductUPCs addProductSecondaryUPCsItem(ProductSecondaryUpcList productSecondaryUPCsItem) {
    if (this.productSecondaryUPCs == null) {
      this.productSecondaryUPCs = new ArrayList<>();
    }
    this.productSecondaryUPCs.add(productSecondaryUPCsItem);
    return this;
  }

   /**
   * Desc: secondaryUpcs List
   * @return productSecondaryUPCs
  **/
  @Valid
  @Schema(description = "Desc: secondaryUpcs List")
  public List<ProductSecondaryUpcList> getProductSecondaryUPCs() {
    return productSecondaryUPCs;
  }

  public void setProductSecondaryUPCs(List<ProductSecondaryUpcList> productSecondaryUPCs) {
    this.productSecondaryUPCs = productSecondaryUPCs;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WarehouseProductUPCs warehouseProductUPCs = (WarehouseProductUPCs) o;
    return Objects.equals(this.productId, warehouseProductUPCs.productId) &&
        Objects.equals(this.productDescription, warehouseProductUPCs.productDescription) &&
        Objects.equals(this.upc, warehouseProductUPCs.upc) &&
        Objects.equals(this.productSecondaryUPCs, warehouseProductUPCs.productSecondaryUPCs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(productId, productDescription, upc, productSecondaryUPCs);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WarehouseProductUPCs {\n");
    
    sb.append("    productId: ").append(toIndentedString(productId)).append("\n");
    sb.append("    productDescription: ").append(toIndentedString(productDescription)).append("\n");
    sb.append("    upc: ").append(toIndentedString(upc)).append("\n");
    sb.append("    productSecondaryUPCs: ").append(toIndentedString(productSecondaryUPCs)).append("\n");
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
