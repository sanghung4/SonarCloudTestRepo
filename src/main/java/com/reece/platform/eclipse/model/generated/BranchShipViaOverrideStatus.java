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
 * Object representing an Eclipse BranchShipViaOverrideStatus.
 */
@Schema(description = "Object representing an Eclipse BranchShipViaOverrideStatus.")

public class BranchShipViaOverrideStatus {
  @JsonProperty("orderInprocessStatus")
  private String orderInprocessStatus = null;

  public BranchShipViaOverrideStatus orderInprocessStatus(String orderInprocessStatus) {
    this.orderInprocessStatus = orderInprocessStatus;
    return this;
  }

   /**
   * Desc: The BranchShipViaOverrideStatus OrderInprocessStatus  File: TERRITORY  Attr: 23,x,y
   * @return orderInprocessStatus
  **/
  @Schema(description = "Desc: The BranchShipViaOverrideStatus OrderInprocessStatus  File: TERRITORY  Attr: 23,x,y")
  public String getOrderInprocessStatus() {
    return orderInprocessStatus;
  }

  public void setOrderInprocessStatus(String orderInprocessStatus) {
    this.orderInprocessStatus = orderInprocessStatus;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BranchShipViaOverrideStatus branchShipViaOverrideStatus = (BranchShipViaOverrideStatus) o;
    return Objects.equals(this.orderInprocessStatus, branchShipViaOverrideStatus.orderInprocessStatus);
  }

  @Override
  public int hashCode() {
    return Objects.hash(orderInprocessStatus);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BranchShipViaOverrideStatus {\n");
    
    sb.append("    orderInprocessStatus: ").append(toIndentedString(orderInprocessStatus)).append("\n");
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
