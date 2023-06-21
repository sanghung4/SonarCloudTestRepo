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
 * Object representing an Eclipse UserSPAVisibility.
 */
@Schema(description = "Object representing an Eclipse UserSPAVisibility.")

public class UserSPAVisibility {
  @JsonProperty("spaVisibility")
  private String spaVisibility = null;

  public UserSPAVisibility spaVisibility(String spaVisibility) {
    this.spaVisibility = spaVisibility;
    return this;
  }

   /**
   * Desc: SPA Auth Visibility  File: INITIALS  Attr: 118,4,xx
   * @return spaVisibility
  **/
  @Schema(description = "Desc: SPA Auth Visibility  File: INITIALS  Attr: 118,4,xx")
  public String getSpaVisibility() {
    return spaVisibility;
  }

  public void setSpaVisibility(String spaVisibility) {
    this.spaVisibility = spaVisibility;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserSPAVisibility userSPAVisibility = (UserSPAVisibility) o;
    return Objects.equals(this.spaVisibility, userSPAVisibility.spaVisibility);
  }

  @Override
  public int hashCode() {
    return Objects.hash(spaVisibility);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserSPAVisibility {\n");
    
    sb.append("    spaVisibility: ").append(toIndentedString(spaVisibility)).append("\n");
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
