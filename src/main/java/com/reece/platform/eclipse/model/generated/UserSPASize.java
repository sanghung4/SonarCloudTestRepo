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
 * Object representing an Eclipse UserSPASize.
 */
@Schema(description = "Object representing an Eclipse UserSPASize.")

public class UserSPASize {
  @JsonProperty("spaSize")
  private String spaSize = null;

  public UserSPASize spaSize(String spaSize) {
    this.spaSize = spaSize;
    return this;
  }

   /**
   * Desc: SPA Auth Size  File: INITIALS  Attr: 118,2,xx
   * @return spaSize
  **/
  @Schema(description = "Desc: SPA Auth Size  File: INITIALS  Attr: 118,2,xx")
  public String getSpaSize() {
    return spaSize;
  }

  public void setSpaSize(String spaSize) {
    this.spaSize = spaSize;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserSPASize userSPASize = (UserSPASize) o;
    return Objects.equals(this.spaSize, userSPASize.spaSize);
  }

  @Override
  public int hashCode() {
    return Objects.hash(spaSize);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserSPASize {\n");
    
    sb.append("    spaSize: ").append(toIndentedString(spaSize)).append("\n");
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
