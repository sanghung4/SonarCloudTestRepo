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
import com.reece.platform.eclipse.model.generated.PriceLineBranchData;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.*;
import javax.validation.Valid;
/**
 * Object representing Price Line branch record
 */
@Schema(description = "Object representing Price Line branch record")

public class PriceLineBranch {
  @JsonProperty("updateKey")
  private String updateKey = null;

  @JsonProperty("id")
  private String id = null;

  @JsonProperty("branches")
  private List<PriceLineBranchData> branches = null;

  public PriceLineBranch updateKey(String updateKey) {
    this.updateKey = updateKey;
    return this;
  }

   /**
   * Update key to handle concurrency during updates within Eclipse
   * @return updateKey
  **/
  @Schema(description = "Update key to handle concurrency during updates within Eclipse")
  public String getUpdateKey() {
    return updateKey;
  }

  public void setUpdateKey(String updateKey) {
    this.updateKey = updateKey;
  }

  public PriceLineBranch id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Eclipse ID for the record
   * @return id
  **/
  @Schema(description = "Eclipse ID for the record")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public PriceLineBranch branches(List<PriceLineBranchData> branches) {
    this.branches = branches;
    return this;
  }

  public PriceLineBranch addBranchesItem(PriceLineBranchData branchesItem) {
    if (this.branches == null) {
      this.branches = new ArrayList<>();
    }
    this.branches.add(branchesItem);
    return this;
  }

   /**
   * A list of Price Line Branch specific data
   * @return branches
  **/
  @Valid
  @Schema(description = "A list of Price Line Branch specific data")
  public List<PriceLineBranchData> getBranches() {
    return branches;
  }

  public void setBranches(List<PriceLineBranchData> branches) {
    this.branches = branches;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PriceLineBranch priceLineBranch = (PriceLineBranch) o;
    return Objects.equals(this.updateKey, priceLineBranch.updateKey) &&
        Objects.equals(this.id, priceLineBranch.id) &&
        Objects.equals(this.branches, priceLineBranch.branches);
  }

  @Override
  public int hashCode() {
    return Objects.hash(updateKey, id, branches);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PriceLineBranch {\n");
    
    sb.append("    updateKey: ").append(toIndentedString(updateKey)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    branches: ").append(toIndentedString(branches)).append("\n");
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
