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
 * Object representing an Eclipse CustomerTaxExceptionGroup.
 */
@Schema(description = "Object representing an Eclipse CustomerTaxExceptionGroup.")

public class CustomerTaxExceptionGroup {
  @JsonProperty("taxExceptionGroup")
  private String taxExceptionGroup = null;

  public CustomerTaxExceptionGroup taxExceptionGroup(String taxExceptionGroup) {
    this.taxExceptionGroup = taxExceptionGroup;
    return this;
  }

   /**
   * Desc: The CustomerTaxExceptionGroup TaxExceptionGroup  File: ENTITY  Attr: 32,x
   * @return taxExceptionGroup
  **/
  @Schema(description = "Desc: The CustomerTaxExceptionGroup TaxExceptionGroup  File: ENTITY  Attr: 32,x")
  public String getTaxExceptionGroup() {
    return taxExceptionGroup;
  }

  public void setTaxExceptionGroup(String taxExceptionGroup) {
    this.taxExceptionGroup = taxExceptionGroup;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CustomerTaxExceptionGroup customerTaxExceptionGroup = (CustomerTaxExceptionGroup) o;
    return Objects.equals(this.taxExceptionGroup, customerTaxExceptionGroup.taxExceptionGroup);
  }

  @Override
  public int hashCode() {
    return Objects.hash(taxExceptionGroup);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CustomerTaxExceptionGroup {\n");
    
    sb.append("    taxExceptionGroup: ").append(toIndentedString(taxExceptionGroup)).append("\n");
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
