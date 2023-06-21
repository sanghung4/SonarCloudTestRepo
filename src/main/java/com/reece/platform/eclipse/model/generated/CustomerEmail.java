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
 * Object representing an Eclipse CustomerEmail.
 */
@Schema(description = "Object representing an Eclipse CustomerEmail.")

public class CustomerEmail {
  @JsonProperty("address")
  private String address = null;

  @JsonProperty("type")
  private String type = null;

  @JsonProperty("preference")
  private String preference = null;

  @JsonProperty("emailIndex")
  private String emailIndex = null;

  public CustomerEmail address(String address) {
    this.address = address;
    return this;
  }

   /**
   * Desc: The CustomerEmail Address  File: ENTITY  Attr: 115,x
   * @return address
  **/
  @Schema(description = "Desc: The CustomerEmail Address  File: ENTITY  Attr: 115,x")
  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public CustomerEmail type(String type) {
    this.type = type;
    return this;
  }

   /**
   * Desc: The CustomerEmail Type  File: ENTITY  Attr: 116,x
   * @return type
  **/
  @Schema(description = "Desc: The CustomerEmail Type  File: ENTITY  Attr: 116,x")
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public CustomerEmail preference(String preference) {
    this.preference = preference;
    return this;
  }

   /**
   * Desc: The CustomerEmail Preference  File: ENTITY  Attr: 98,x
   * @return preference
  **/
  @Schema(description = "Desc: The CustomerEmail Preference  File: ENTITY  Attr: 98,x")
  public String getPreference() {
    return preference;
  }

  public void setPreference(String preference) {
    this.preference = preference;
  }

  public CustomerEmail emailIndex(String emailIndex) {
    this.emailIndex = emailIndex;
    return this;
  }

   /**
   * Desc : The CustomerEmail Index  File : ENTITY  Attr: 131,x
   * @return emailIndex
  **/
  @Schema(description = "Desc : The CustomerEmail Index  File : ENTITY  Attr: 131,x")
  public String getEmailIndex() {
    return emailIndex;
  }

  public void setEmailIndex(String emailIndex) {
    this.emailIndex = emailIndex;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CustomerEmail customerEmail = (CustomerEmail) o;
    return Objects.equals(this.address, customerEmail.address) &&
        Objects.equals(this.type, customerEmail.type) &&
        Objects.equals(this.preference, customerEmail.preference) &&
        Objects.equals(this.emailIndex, customerEmail.emailIndex);
  }

  @Override
  public int hashCode() {
    return Objects.hash(address, type, preference, emailIndex);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CustomerEmail {\n");
    
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    preference: ").append(toIndentedString(preference)).append("\n");
    sb.append("    emailIndex: ").append(toIndentedString(emailIndex)).append("\n");
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
