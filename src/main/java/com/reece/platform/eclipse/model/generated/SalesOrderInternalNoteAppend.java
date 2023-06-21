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
 * Object representing an Eclipse SalesOrderInternalNoteAppend.
 */
@Schema(description = "Object representing an Eclipse SalesOrderInternalNoteAppend.")

public class SalesOrderInternalNoteAppend {
  @JsonProperty("internalNotes")
  private String internalNotes = null;

  public SalesOrderInternalNoteAppend internalNotes(String internalNotes) {
    this.internalNotes = internalNotes;
    return this;
  }

   /**
   * Desc: The SalesOrderInternalNote Internal Note  File: LEDGER  Attr: 80
   * @return internalNotes
  **/
  @Schema(description = "Desc: The SalesOrderInternalNote Internal Note  File: LEDGER  Attr: 80")
  public String getInternalNotes() {
    return internalNotes;
  }

  public void setInternalNotes(String internalNotes) {
    this.internalNotes = internalNotes;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SalesOrderInternalNoteAppend salesOrderInternalNoteAppend = (SalesOrderInternalNoteAppend) o;
    return Objects.equals(this.internalNotes, salesOrderInternalNoteAppend.internalNotes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(internalNotes);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SalesOrderInternalNoteAppend {\n");
    
    sb.append("    internalNotes: ").append(toIndentedString(internalNotes)).append("\n");
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
