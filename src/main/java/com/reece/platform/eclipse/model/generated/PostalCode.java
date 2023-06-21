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
import com.reece.platform.eclipse.model.generated.PostalCodeBranchOverride;
import com.reece.platform.eclipse.model.generated.PostalCodeCity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.*;
import javax.validation.Valid;
/**
 * Object representing an Eclipse PostalCode.
 */
@Schema(description = "Object representing an Eclipse PostalCode.")

public class PostalCode {
  @JsonProperty("updateKey")
  private String updateKey = null;

  @JsonProperty("id")
  private String id = null;

  @JsonProperty("state")
  private String state = null;

  @JsonProperty("county")
  private String county = null;

  @JsonProperty("countryCode")
  private String countryCode = null;

  @JsonProperty("defaultTaxJurisdiction")
  private String defaultTaxJurisdiction = null;

  @JsonProperty("defaultShipVia")
  private String defaultShipVia = null;

  @JsonProperty("defaultDeliveryTime")
  private LocalDateTime defaultDeliveryTime = null;

  @JsonProperty("defaultGeocode")
  private String defaultGeocode = null;

  @JsonProperty("remitToOverride")
  private String remitToOverride = null;

  @JsonProperty("branchOverrides")
  private List<PostalCodeBranchOverride> branchOverrides = null;

  @JsonProperty("cities")
  private List<PostalCodeCity> cities = null;

  public PostalCode updateKey(String updateKey) {
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

  public PostalCode id(String id) {
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

  public PostalCode state(String state) {
    this.state = state;
    return this;
  }

   /**
   * Desc: The PostalCode State  File: ZIP  Attr: 1
   * @return state
  **/
  @Schema(description = "Desc: The PostalCode State  File: ZIP  Attr: 1")
  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public PostalCode county(String county) {
    this.county = county;
    return this;
  }

   /**
   * Desc: The PostalCode County  File: ZIP  Attr: 4,2
   * @return county
  **/
  @Schema(description = "Desc: The PostalCode County  File: ZIP  Attr: 4,2")
  public String getCounty() {
    return county;
  }

  public void setCounty(String county) {
    this.county = county;
  }

  public PostalCode countryCode(String countryCode) {
    this.countryCode = countryCode;
    return this;
  }

   /**
   * Desc: The PostalCode CountryCode  File: ZIP  Attr: 5
   * @return countryCode
  **/
  @Schema(description = "Desc: The PostalCode CountryCode  File: ZIP  Attr: 5")
  public String getCountryCode() {
    return countryCode;
  }

  public void setCountryCode(String countryCode) {
    this.countryCode = countryCode;
  }

  public PostalCode defaultTaxJurisdiction(String defaultTaxJurisdiction) {
    this.defaultTaxJurisdiction = defaultTaxJurisdiction;
    return this;
  }

   /**
   * Desc: The PostalCode DefaultTaxJurisdiction  File: ZIP  Attr: 2
   * @return defaultTaxJurisdiction
  **/
  @Schema(description = "Desc: The PostalCode DefaultTaxJurisdiction  File: ZIP  Attr: 2")
  public String getDefaultTaxJurisdiction() {
    return defaultTaxJurisdiction;
  }

  public void setDefaultTaxJurisdiction(String defaultTaxJurisdiction) {
    this.defaultTaxJurisdiction = defaultTaxJurisdiction;
  }

  public PostalCode defaultShipVia(String defaultShipVia) {
    this.defaultShipVia = defaultShipVia;
    return this;
  }

   /**
   * Desc: The PostalCode DefaultShipVia  File: ZIP  Attr: 7
   * @return defaultShipVia
  **/
  @Schema(description = "Desc: The PostalCode DefaultShipVia  File: ZIP  Attr: 7")
  public String getDefaultShipVia() {
    return defaultShipVia;
  }

  public void setDefaultShipVia(String defaultShipVia) {
    this.defaultShipVia = defaultShipVia;
  }

  public PostalCode defaultDeliveryTime(LocalDateTime defaultDeliveryTime) {
    this.defaultDeliveryTime = defaultDeliveryTime;
    return this;
  }

   /**
   * Desc: The PostalCode DefaultDeliveryTime  File: ZIP  Attr: 8
   * @return defaultDeliveryTime
  **/
  @Valid
  @Schema(description = "Desc: The PostalCode DefaultDeliveryTime  File: ZIP  Attr: 8")
  public LocalDateTime getDefaultDeliveryTime() {
    return defaultDeliveryTime;
  }

  public void setDefaultDeliveryTime(LocalDateTime defaultDeliveryTime) {
    this.defaultDeliveryTime = defaultDeliveryTime;
  }

  public PostalCode defaultGeocode(String defaultGeocode) {
    this.defaultGeocode = defaultGeocode;
    return this;
  }

   /**
   * Desc: The PostalCode DefaultGeocode  File: ZIP  Attr: 10
   * @return defaultGeocode
  **/
  @Schema(description = "Desc: The PostalCode DefaultGeocode  File: ZIP  Attr: 10")
  public String getDefaultGeocode() {
    return defaultGeocode;
  }

  public void setDefaultGeocode(String defaultGeocode) {
    this.defaultGeocode = defaultGeocode;
  }

  public PostalCode remitToOverride(String remitToOverride) {
    this.remitToOverride = remitToOverride;
    return this;
  }

   /**
   * Desc: The PostalCode RemitToOverride  File: ZIP  Attr: 15
   * @return remitToOverride
  **/
  @Schema(description = "Desc: The PostalCode RemitToOverride  File: ZIP  Attr: 15")
  public String getRemitToOverride() {
    return remitToOverride;
  }

  public void setRemitToOverride(String remitToOverride) {
    this.remitToOverride = remitToOverride;
  }

  public PostalCode branchOverrides(List<PostalCodeBranchOverride> branchOverrides) {
    this.branchOverrides = branchOverrides;
    return this;
  }

  public PostalCode addBranchOverridesItem(PostalCodeBranchOverride branchOverridesItem) {
    if (this.branchOverrides == null) {
      this.branchOverrides = new ArrayList<>();
    }
    this.branchOverrides.add(branchOverridesItem);
    return this;
  }

   /**
   * A list of PostalCode BranchOverrides
   * @return branchOverrides
  **/
  @Valid
  @Schema(description = "A list of PostalCode BranchOverrides")
  public List<PostalCodeBranchOverride> getBranchOverrides() {
    return branchOverrides;
  }

  public void setBranchOverrides(List<PostalCodeBranchOverride> branchOverrides) {
    this.branchOverrides = branchOverrides;
  }

  public PostalCode cities(List<PostalCodeCity> cities) {
    this.cities = cities;
    return this;
  }

  public PostalCode addCitiesItem(PostalCodeCity citiesItem) {
    if (this.cities == null) {
      this.cities = new ArrayList<>();
    }
    this.cities.add(citiesItem);
    return this;
  }

   /**
   * A list of PostalCode Cities
   * @return cities
  **/
  @Valid
  @Schema(description = "A list of PostalCode Cities")
  public List<PostalCodeCity> getCities() {
    return cities;
  }

  public void setCities(List<PostalCodeCity> cities) {
    this.cities = cities;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PostalCode postalCode = (PostalCode) o;
    return Objects.equals(this.updateKey, postalCode.updateKey) &&
        Objects.equals(this.id, postalCode.id) &&
        Objects.equals(this.state, postalCode.state) &&
        Objects.equals(this.county, postalCode.county) &&
        Objects.equals(this.countryCode, postalCode.countryCode) &&
        Objects.equals(this.defaultTaxJurisdiction, postalCode.defaultTaxJurisdiction) &&
        Objects.equals(this.defaultShipVia, postalCode.defaultShipVia) &&
        Objects.equals(this.defaultDeliveryTime, postalCode.defaultDeliveryTime) &&
        Objects.equals(this.defaultGeocode, postalCode.defaultGeocode) &&
        Objects.equals(this.remitToOverride, postalCode.remitToOverride) &&
        Objects.equals(this.branchOverrides, postalCode.branchOverrides) &&
        Objects.equals(this.cities, postalCode.cities);
  }

  @Override
  public int hashCode() {
    return Objects.hash(updateKey, id, state, county, countryCode, defaultTaxJurisdiction, defaultShipVia, defaultDeliveryTime, defaultGeocode, remitToOverride, branchOverrides, cities);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PostalCode {\n");
    
    sb.append("    updateKey: ").append(toIndentedString(updateKey)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
    sb.append("    county: ").append(toIndentedString(county)).append("\n");
    sb.append("    countryCode: ").append(toIndentedString(countryCode)).append("\n");
    sb.append("    defaultTaxJurisdiction: ").append(toIndentedString(defaultTaxJurisdiction)).append("\n");
    sb.append("    defaultShipVia: ").append(toIndentedString(defaultShipVia)).append("\n");
    sb.append("    defaultDeliveryTime: ").append(toIndentedString(defaultDeliveryTime)).append("\n");
    sb.append("    defaultGeocode: ").append(toIndentedString(defaultGeocode)).append("\n");
    sb.append("    remitToOverride: ").append(toIndentedString(remitToOverride)).append("\n");
    sb.append("    branchOverrides: ").append(toIndentedString(branchOverrides)).append("\n");
    sb.append("    cities: ").append(toIndentedString(cities)).append("\n");
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
