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
 * Object representing an Eclipse EntityBranchAuthActive.
 */
@Schema(description = "Object representing an Eclipse EntityBranchAuthActive.")

public class EntityBranchAuthActive {
  @JsonProperty("activeBranch")
  private String activeBranch = null;

  public EntityBranchAuthActive activeBranch(String activeBranch) {
    this.activeBranch = activeBranch;
    return this;
  }

   /**
   * Desc: The Customer/Vendor BranchAuthActive ActiveBranch  File: ENTITY.BR.AUTH  Attr: 1,x
   * @return activeBranch
  **/
  @NotNull
  @Schema(required = true, description = "Desc: The Customer/Vendor BranchAuthActive ActiveBranch  File: ENTITY.BR.AUTH  Attr: 1,x")
  public String getActiveBranch() {
    return activeBranch;
  }

  public void setActiveBranch(String activeBranch) {
    this.activeBranch = activeBranch;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EntityBranchAuthActive entityBranchAuthActive = (EntityBranchAuthActive) o;
    return Objects.equals(this.activeBranch, entityBranchAuthActive.activeBranch);
  }

  @Override
  public int hashCode() {
    return Objects.hash(activeBranch);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EntityBranchAuthActive {\n");
    
    sb.append("    activeBranch: ").append(toIndentedString(activeBranch)).append("\n");
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
