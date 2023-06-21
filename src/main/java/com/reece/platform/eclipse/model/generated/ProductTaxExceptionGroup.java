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
import com.reece.platform.eclipse.model.generated.TaxExemptionCode;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.*;
import javax.validation.Valid;
/**
 * Object representing an Eclipse ProductTaxExceptionGroup.
 */
@Schema(description = "Object representing an Eclipse ProductTaxExceptionGroup.")

public class ProductTaxExceptionGroup {
  @JsonProperty("taxExceptionGroupId")
  private String taxExceptionGroupId = null;

  @JsonProperty("taxExceptionCode")
  private TaxExemptionCode taxExceptionCode = null;

  @JsonProperty("reducedRate")
  private Boolean reducedRate = null;

  public ProductTaxExceptionGroup taxExceptionGroupId(String taxExceptionGroupId) {
    this.taxExceptionGroupId = taxExceptionGroupId;
    return this;
  }

   /**
   * Desc: The ProductTaxExceptionGroup TaxExceptionGroupId  File: PRODUCT  Attr: 22,x
   * @return taxExceptionGroupId
  **/
  @Schema(description = "Desc: The ProductTaxExceptionGroup TaxExceptionGroupId  File: PRODUCT  Attr: 22,x")
  public String getTaxExceptionGroupId() {
    return taxExceptionGroupId;
  }

  public void setTaxExceptionGroupId(String taxExceptionGroupId) {
    this.taxExceptionGroupId = taxExceptionGroupId;
  }

  public ProductTaxExceptionGroup taxExceptionCode(TaxExemptionCode taxExceptionCode) {
    this.taxExceptionCode = taxExceptionCode;
    return this;
  }

   /**
   * Get taxExceptionCode
   * @return taxExceptionCode
  **/
  @Valid
  @Schema(description = "")
  public TaxExemptionCode getTaxExceptionCode() {
    return taxExceptionCode;
  }

  public void setTaxExceptionCode(TaxExemptionCode taxExceptionCode) {
    this.taxExceptionCode = taxExceptionCode;
  }

  public ProductTaxExceptionGroup reducedRate(Boolean reducedRate) {
    this.reducedRate = reducedRate;
    return this;
  }

   /**
   * Desc: The ProductTaxExceptionGroup TaxExceptionCode  File: PRODUCT  Attr: 23,x,2
   * @return reducedRate
  **/
  @Schema(description = "Desc: The ProductTaxExceptionGroup TaxExceptionCode  File: PRODUCT  Attr: 23,x,2")
  public Boolean isReducedRate() {
    return reducedRate;
  }

  public void setReducedRate(Boolean reducedRate) {
    this.reducedRate = reducedRate;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProductTaxExceptionGroup productTaxExceptionGroup = (ProductTaxExceptionGroup) o;
    return Objects.equals(this.taxExceptionGroupId, productTaxExceptionGroup.taxExceptionGroupId) &&
        Objects.equals(this.taxExceptionCode, productTaxExceptionGroup.taxExceptionCode) &&
        Objects.equals(this.reducedRate, productTaxExceptionGroup.reducedRate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(taxExceptionGroupId, taxExceptionCode, reducedRate);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProductTaxExceptionGroup {\n");
    
    sb.append("    taxExceptionGroupId: ").append(toIndentedString(taxExceptionGroupId)).append("\n");
    sb.append("    taxExceptionCode: ").append(toIndentedString(taxExceptionCode)).append("\n");
    sb.append("    reducedRate: ").append(toIndentedString(reducedRate)).append("\n");
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
