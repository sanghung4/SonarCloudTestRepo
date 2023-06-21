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
 * Object representing an Eclipse ProductKitComponent.
 */
@Schema(description = "Object representing an Eclipse ProductKitComponent.")

public class ProductKitComponent {
  @JsonProperty("componentProductId")
  private Integer componentProductId = null;

  @JsonProperty("componentQty")
  private Integer componentQty = null;

  @JsonProperty("componentComment")
  private String componentComment = null;

  @JsonProperty("componentSpoilage")
  private Double componentSpoilage = null;

  @JsonProperty("isKeyComponent")
  private Boolean isKeyComponent = null;

  public ProductKitComponent componentProductId(Integer componentProductId) {
    this.componentProductId = componentProductId;
    return this;
  }

   /**
   * Desc: The ProductKitComponent ComponentProductId  File: PRODUCT  Attr: 53,x
   * @return componentProductId
  **/
  @Schema(description = "Desc: The ProductKitComponent ComponentProductId  File: PRODUCT  Attr: 53,x")
  public Integer getComponentProductId() {
    return componentProductId;
  }

  public void setComponentProductId(Integer componentProductId) {
    this.componentProductId = componentProductId;
  }

  public ProductKitComponent componentQty(Integer componentQty) {
    this.componentQty = componentQty;
    return this;
  }

   /**
   * Desc: The ProductKitComponent ComponentQty  File: PRODUCT  Attr: 52,x
   * @return componentQty
  **/
  @Schema(description = "Desc: The ProductKitComponent ComponentQty  File: PRODUCT  Attr: 52,x")
  public Integer getComponentQty() {
    return componentQty;
  }

  public void setComponentQty(Integer componentQty) {
    this.componentQty = componentQty;
  }

  public ProductKitComponent componentComment(String componentComment) {
    this.componentComment = componentComment;
    return this;
  }

   /**
   * Desc: The ProductKitComponent ComponentComment  File: PRODUCT  Attr: 85,x
   * @return componentComment
  **/
  @Schema(description = "Desc: The ProductKitComponent ComponentComment  File: PRODUCT  Attr: 85,x")
  public String getComponentComment() {
    return componentComment;
  }

  public void setComponentComment(String componentComment) {
    this.componentComment = componentComment;
  }

  public ProductKitComponent componentSpoilage(Double componentSpoilage) {
    this.componentSpoilage = componentSpoilage;
    return this;
  }

   /**
   * Desc: The ProductKitComponent ComponentSpoilage  File: PRODUCT  Attr: 87,x
   * @return componentSpoilage
  **/
  @Schema(description = "Desc: The ProductKitComponent ComponentSpoilage  File: PRODUCT  Attr: 87,x")
  public Double getComponentSpoilage() {
    return componentSpoilage;
  }

  public void setComponentSpoilage(Double componentSpoilage) {
    this.componentSpoilage = componentSpoilage;
  }

  public ProductKitComponent isKeyComponent(Boolean isKeyComponent) {
    this.isKeyComponent = isKeyComponent;
    return this;
  }

   /**
   * Desc: The ProductKitComponent IsKeyComponent flag  File: PRODUCT  Attr: 145,x
   * @return isKeyComponent
  **/
  @Schema(description = "Desc: The ProductKitComponent IsKeyComponent flag  File: PRODUCT  Attr: 145,x")
  public Boolean isIsKeyComponent() {
    return isKeyComponent;
  }

  public void setIsKeyComponent(Boolean isKeyComponent) {
    this.isKeyComponent = isKeyComponent;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProductKitComponent productKitComponent = (ProductKitComponent) o;
    return Objects.equals(this.componentProductId, productKitComponent.componentProductId) &&
        Objects.equals(this.componentQty, productKitComponent.componentQty) &&
        Objects.equals(this.componentComment, productKitComponent.componentComment) &&
        Objects.equals(this.componentSpoilage, productKitComponent.componentSpoilage) &&
        Objects.equals(this.isKeyComponent, productKitComponent.isKeyComponent);
  }

  @Override
  public int hashCode() {
    return Objects.hash(componentProductId, componentQty, componentComment, componentSpoilage, isKeyComponent);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProductKitComponent {\n");
    
    sb.append("    componentProductId: ").append(toIndentedString(componentProductId)).append("\n");
    sb.append("    componentQty: ").append(toIndentedString(componentQty)).append("\n");
    sb.append("    componentComment: ").append(toIndentedString(componentComment)).append("\n");
    sb.append("    componentSpoilage: ").append(toIndentedString(componentSpoilage)).append("\n");
    sb.append("    isKeyComponent: ").append(toIndentedString(isKeyComponent)).append("\n");
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
