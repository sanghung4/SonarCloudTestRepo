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
 * Object representing an Eclipse ContactAccessList.
 */
@Schema(description = "Object representing an Eclipse ContactAccessList.")

public class ContactAccessList {
  @JsonProperty("userId")
  private String userId = null;

  @JsonProperty("accessLevel")
  private String accessLevel = null;

  public ContactAccessList userId(String userId) {
    this.userId = userId;
    return this;
  }

   /**
   * Desc: The ContactAccessList UserId  File: CONTACT  Attr: 20,x
   * @return userId
  **/
  @Schema(description = "Desc: The ContactAccessList UserId  File: CONTACT  Attr: 20,x")
  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public ContactAccessList accessLevel(String accessLevel) {
    this.accessLevel = accessLevel;
    return this;
  }

   /**
   * Desc: The ContactAccessList AccessLevelId  File: CONTACT  Attr: 21,x
   * @return accessLevel
  **/
  @Schema(description = "Desc: The ContactAccessList AccessLevelId  File: CONTACT  Attr: 21,x")
  public String getAccessLevel() {
    return accessLevel;
  }

  public void setAccessLevel(String accessLevel) {
    this.accessLevel = accessLevel;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ContactAccessList contactAccessList = (ContactAccessList) o;
    return Objects.equals(this.userId, contactAccessList.userId) &&
        Objects.equals(this.accessLevel, contactAccessList.accessLevel);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, accessLevel);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ContactAccessList {\n");
    
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    accessLevel: ").append(toIndentedString(accessLevel)).append("\n");
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
