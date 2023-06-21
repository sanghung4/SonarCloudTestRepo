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
 * Object representing an Eclipse UserSalesSource.
 */
@Schema(description = "Object representing an Eclipse UserSalesSource.")

public class UserSalesSource {
  @JsonProperty("salesSourceId")
  private String salesSourceId = null;

  public UserSalesSource salesSourceId(String salesSourceId) {
    this.salesSourceId = salesSourceId;
    return this;
  }

   /**
   * Desc: The UserSalesSource SalesSourceId  File: INITIALS  Attr: 18,x
   * @return salesSourceId
  **/
  @Schema(description = "Desc: The UserSalesSource SalesSourceId  File: INITIALS  Attr: 18,x")
  public String getSalesSourceId() {
    return salesSourceId;
  }

  public void setSalesSourceId(String salesSourceId) {
    this.salesSourceId = salesSourceId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserSalesSource userSalesSource = (UserSalesSource) o;
    return Objects.equals(this.salesSourceId, userSalesSource.salesSourceId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(salesSourceId);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserSalesSource {\n");
    
    sb.append("    salesSourceId: ").append(toIndentedString(salesSourceId)).append("\n");
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
