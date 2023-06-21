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
 * Object representing an Eclipse CustomerRemoteValidShipVia.
 */
@Schema(description = "Object representing an Eclipse CustomerRemoteValidShipVia.")

public class CustomerRemoteValidShipVia {
  @JsonProperty("shipViaId")
  private String shipViaId = null;

  public CustomerRemoteValidShipVia shipViaId(String shipViaId) {
    this.shipViaId = shipViaId;
    return this;
  }

   /**
   * Desc: The CustomerRemoteValidShipVia ShipViaId  File: ENTITY  Attr: 68,18,x
   * @return shipViaId
  **/
  @Schema(description = "Desc: The CustomerRemoteValidShipVia ShipViaId  File: ENTITY  Attr: 68,18,x")
  public String getShipViaId() {
    return shipViaId;
  }

  public void setShipViaId(String shipViaId) {
    this.shipViaId = shipViaId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CustomerRemoteValidShipVia customerRemoteValidShipVia = (CustomerRemoteValidShipVia) o;
    return Objects.equals(this.shipViaId, customerRemoteValidShipVia.shipViaId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(shipViaId);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CustomerRemoteValidShipVia {\n");
    
    sb.append("    shipViaId: ").append(toIndentedString(shipViaId)).append("\n");
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
