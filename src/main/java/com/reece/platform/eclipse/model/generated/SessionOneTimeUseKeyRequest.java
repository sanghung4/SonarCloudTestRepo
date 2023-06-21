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
import com.reece.platform.eclipse.model.generated.SessionUserType;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.*;
import javax.validation.Valid;
/**
 * Session One Time Use Key Request
 */
@Schema(description = "Session One Time Use Key Request")

public class SessionOneTimeUseKeyRequest {
  @JsonProperty("oneTimeUseKey")
  private String oneTimeUseKey = null;

  @JsonProperty("loginType")
  private SessionUserType loginType = null;

  @JsonProperty("workstationId")
  private String workstationId = null;

  @JsonProperty("printerLocationId")
  private String printerLocationId = null;

  @JsonProperty("applicationKey")
  private String applicationKey = null;

  @JsonProperty("developerKey")
  private String developerKey = null;

  @JsonProperty("deviceId")
  private String deviceId = null;

  @JsonProperty("clientDescription")
  private String clientDescription = null;

  public SessionOneTimeUseKeyRequest oneTimeUseKey(String oneTimeUseKey) {
    this.oneTimeUseKey = oneTimeUseKey;
    return this;
  }

   /**
   * One Time Use key for the session to be created.
   * @return oneTimeUseKey
  **/
  @NotNull
  @Schema(required = true, description = "One Time Use key for the session to be created.")
  public String getOneTimeUseKey() {
    return oneTimeUseKey;
  }

  public void setOneTimeUseKey(String oneTimeUseKey) {
    this.oneTimeUseKey = oneTimeUseKey;
  }

  public SessionOneTimeUseKeyRequest loginType(SessionUserType loginType) {
    this.loginType = loginType;
    return this;
  }

   /**
   * Get loginType
   * @return loginType
  **/
  @Valid
  @Schema(description = "")
  public SessionUserType getLoginType() {
    return loginType;
  }

  public void setLoginType(SessionUserType loginType) {
    this.loginType = loginType;
  }

  public SessionOneTimeUseKeyRequest workstationId(String workstationId) {
    this.workstationId = workstationId;
    return this;
  }

   /**
   * Workstation ID for the client.  Used to map device to printers and other physical location information  (This replaces the Terminal ID from eterm/solar)
   * @return workstationId
  **/
  @Schema(description = "Workstation ID for the client.  Used to map device to printers and other physical location information  (This replaces the Terminal ID from eterm/solar)")
  public String getWorkstationId() {
    return workstationId;
  }

  public void setWorkstationId(String workstationId) {
    this.workstationId = workstationId;
  }

  public SessionOneTimeUseKeyRequest printerLocationId(String printerLocationId) {
    this.printerLocationId = printerLocationId;
    return this;
  }

   /**
   * Optional Printer location ID for the client.  Used to map user to printer location.  NOTE: This will be overridden by the printer location specified in location/terminal maintenance if set.  NOTE2: This must match a valid printer location for the user, or an error will be returned.  (This replaces the \&quot;location\&quot; field during eterm/solar login)
   * @return printerLocationId
  **/
  @Schema(description = "Optional Printer location ID for the client.  Used to map user to printer location.  NOTE: This will be overridden by the printer location specified in location/terminal maintenance if set.  NOTE2: This must match a valid printer location for the user, or an error will be returned.  (This replaces the \"location\" field during eterm/solar login)")
  public String getPrinterLocationId() {
    return printerLocationId;
  }

  public void setPrinterLocationId(String printerLocationId) {
    this.printerLocationId = printerLocationId;
  }

  public SessionOneTimeUseKeyRequest applicationKey(String applicationKey) {
    this.applicationKey = applicationKey;
    return this;
  }

   /**
   * Optional Application Key for the client
   * @return applicationKey
  **/
  @Schema(description = "Optional Application Key for the client")
  public String getApplicationKey() {
    return applicationKey;
  }

  public void setApplicationKey(String applicationKey) {
    this.applicationKey = applicationKey;
  }

  public SessionOneTimeUseKeyRequest developerKey(String developerKey) {
    this.developerKey = developerKey;
    return this;
  }

   /**
   * Optional developer Key for the developer of the client
   * @return developerKey
  **/
  @Schema(description = "Optional developer Key for the developer of the client")
  public String getDeveloperKey() {
    return developerKey;
  }

  public void setDeveloperKey(String developerKey) {
    this.developerKey = developerKey;
  }

  public SessionOneTimeUseKeyRequest deviceId(String deviceId) {
    this.deviceId = deviceId;
    return this;
  }

   /**
   * Optional device ID which should be randomly created at install time
   * @return deviceId
  **/
  @Schema(description = "Optional device ID which should be randomly created at install time")
  public String getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(String deviceId) {
    this.deviceId = deviceId;
  }

  public SessionOneTimeUseKeyRequest clientDescription(String clientDescription) {
    this.clientDescription = clientDescription;
    return this;
  }

   /**
   * Optional error_description of the client connecting to the API
   * @return clientDescription
  **/
  @Schema(description = "Optional error_description of the client connecting to the API")
  public String getClientDescription() {
    return clientDescription;
  }

  public void setClientDescription(String clientDescription) {
    this.clientDescription = clientDescription;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SessionOneTimeUseKeyRequest sessionOneTimeUseKeyRequest = (SessionOneTimeUseKeyRequest) o;
    return Objects.equals(this.oneTimeUseKey, sessionOneTimeUseKeyRequest.oneTimeUseKey) &&
        Objects.equals(this.loginType, sessionOneTimeUseKeyRequest.loginType) &&
        Objects.equals(this.workstationId, sessionOneTimeUseKeyRequest.workstationId) &&
        Objects.equals(this.printerLocationId, sessionOneTimeUseKeyRequest.printerLocationId) &&
        Objects.equals(this.applicationKey, sessionOneTimeUseKeyRequest.applicationKey) &&
        Objects.equals(this.developerKey, sessionOneTimeUseKeyRequest.developerKey) &&
        Objects.equals(this.deviceId, sessionOneTimeUseKeyRequest.deviceId) &&
        Objects.equals(this.clientDescription, sessionOneTimeUseKeyRequest.clientDescription);
  }

  @Override
  public int hashCode() {
    return Objects.hash(oneTimeUseKey, loginType, workstationId, printerLocationId, applicationKey, developerKey, deviceId, clientDescription);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SessionOneTimeUseKeyRequest {\n");
    
    sb.append("    oneTimeUseKey: ").append(toIndentedString(oneTimeUseKey)).append("\n");
    sb.append("    loginType: ").append(toIndentedString(loginType)).append("\n");
    sb.append("    workstationId: ").append(toIndentedString(workstationId)).append("\n");
    sb.append("    printerLocationId: ").append(toIndentedString(printerLocationId)).append("\n");
    sb.append("    applicationKey: ").append(toIndentedString(applicationKey)).append("\n");
    sb.append("    developerKey: ").append(toIndentedString(developerKey)).append("\n");
    sb.append("    deviceId: ").append(toIndentedString(deviceId)).append("\n");
    sb.append("    clientDescription: ").append(toIndentedString(clientDescription)).append("\n");
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
