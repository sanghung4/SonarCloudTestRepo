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
 * Color
 */


public class Color {
  @JsonProperty("r")
  private Integer r = null;

  @JsonProperty("g")
  private Integer g = null;

  @JsonProperty("b")
  private Integer b = null;

  @JsonProperty("a")
  private Integer a = null;

  @JsonProperty("isKnownColor")
  private Boolean isKnownColor = null;

  @JsonProperty("isEmpty")
  private Boolean isEmpty = null;

  @JsonProperty("isNamedColor")
  private Boolean isNamedColor = null;

  @JsonProperty("isSystemColor")
  private Boolean isSystemColor = null;

  @JsonProperty("name")
  private String name = null;

   /**
   * Get r
   * @return r
  **/
  @Schema(description = "")
  public Integer getR() {
    return r;
  }

   /**
   * Get g
   * @return g
  **/
  @Schema(description = "")
  public Integer getG() {
    return g;
  }

   /**
   * Get b
   * @return b
  **/
  @Schema(description = "")
  public Integer getB() {
    return b;
  }

   /**
   * Get a
   * @return a
  **/
  @Schema(description = "")
  public Integer getA() {
    return a;
  }

   /**
   * Get isKnownColor
   * @return isKnownColor
  **/
  @Schema(description = "")
  public Boolean isIsKnownColor() {
    return isKnownColor;
  }

   /**
   * Get isEmpty
   * @return isEmpty
  **/
  @Schema(description = "")
  public Boolean isIsEmpty() {
    return isEmpty;
  }

   /**
   * Get isNamedColor
   * @return isNamedColor
  **/
  @Schema(description = "")
  public Boolean isIsNamedColor() {
    return isNamedColor;
  }

   /**
   * Get isSystemColor
   * @return isSystemColor
  **/
  @Schema(description = "")
  public Boolean isIsSystemColor() {
    return isSystemColor;
  }

   /**
   * Get name
   * @return name
  **/
  @Schema(description = "")
  public String getName() {
    return name;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Color color = (Color) o;
    return Objects.equals(this.r, color.r) &&
        Objects.equals(this.g, color.g) &&
        Objects.equals(this.b, color.b) &&
        Objects.equals(this.a, color.a) &&
        Objects.equals(this.isKnownColor, color.isKnownColor) &&
        Objects.equals(this.isEmpty, color.isEmpty) &&
        Objects.equals(this.isNamedColor, color.isNamedColor) &&
        Objects.equals(this.isSystemColor, color.isSystemColor) &&
        Objects.equals(this.name, color.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(r, g, b, a, isKnownColor, isEmpty, isNamedColor, isSystemColor, name);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Color {\n");
    
    sb.append("    r: ").append(toIndentedString(r)).append("\n");
    sb.append("    g: ").append(toIndentedString(g)).append("\n");
    sb.append("    b: ").append(toIndentedString(b)).append("\n");
    sb.append("    a: ").append(toIndentedString(a)).append("\n");
    sb.append("    isKnownColor: ").append(toIndentedString(isKnownColor)).append("\n");
    sb.append("    isEmpty: ").append(toIndentedString(isEmpty)).append("\n");
    sb.append("    isNamedColor: ").append(toIndentedString(isNamedColor)).append("\n");
    sb.append("    isSystemColor: ").append(toIndentedString(isSystemColor)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
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
