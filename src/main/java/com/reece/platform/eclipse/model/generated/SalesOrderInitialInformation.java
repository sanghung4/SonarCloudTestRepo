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
import com.reece.platform.eclipse.model.generated.ConsigmentType;
import com.reece.platform.eclipse.model.generated.SalesOrderLineInitialInformation;
import com.reece.platform.eclipse.model.generated.SalesOrderStatusType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.*;
import javax.validation.Valid;
/**
 * Object representing an Eclipse SalesOrderInitialInformation.
 */
@Schema(description = "Object representing an Eclipse SalesOrderInitialInformation.")

public class SalesOrderInitialInformation {
  @JsonProperty("priceBranch")
  private String priceBranch = null;

  @JsonProperty("shipBranch")
  private String shipBranch = null;

  @JsonProperty("billToCustomer")
  private String billToCustomer = null;

  @JsonProperty("billToCustomerECommerceId")
  private String billToCustomerECommerceId = null;

  @JsonProperty("shipToCustomer")
  private String shipToCustomer = null;

  @JsonProperty("shipToCustomerECommerceId")
  private String shipToCustomerECommerceId = null;

  @JsonProperty("orderStatus")
  private SalesOrderStatusType orderStatus = null;

  @JsonProperty("salesSource")
  private String salesSource = null;

  @JsonProperty("shipDate")
  private LocalDateTime shipDate = null;

  @JsonProperty("requiredDate")
  private LocalDateTime requiredDate = null;

  @JsonProperty("shipVia")
  private String shipVia = null;

  @JsonProperty("customerPONumber")
  private String customerPONumber = null;

  @JsonProperty("customerReleaseNumber")
  private String customerReleaseNumber = null;

  @JsonProperty("insideSalesPerson")
  private String insideSalesPerson = null;

  @JsonProperty("outsideSalesPerson")
  private String outsideSalesPerson = null;

  @JsonProperty("orderBy")
  private String orderBy = null;

  @JsonProperty("writer")
  private String writer = null;

  @JsonProperty("telephone")
  private String telephone = null;

  @JsonProperty("email")
  private String email = null;

  @JsonProperty("street1")
  private String street1 = null;

  @JsonProperty("street2")
  private String street2 = null;

  @JsonProperty("city")
  private String city = null;

  @JsonProperty("state")
  private String state = null;

  @JsonProperty("postalCode")
  private String postalCode = null;

  @JsonProperty("country")
  private String country = null;

  @JsonProperty("internalNotes")
  private String internalNotes = null;

  @JsonProperty("shippingInstructions")
  private String shippingInstructions = null;

  @JsonProperty("termsCode")
  private String termsCode = null;

  @JsonProperty("freightCharges")
  private Double freightCharges = null;

  @JsonProperty("handlingCharges")
  private Double handlingCharges = null;

  @JsonProperty("taxExemptCode")
  private String taxExemptCode = null;

  @JsonProperty("taxExemptId")
  private String taxExemptId = null;

  @JsonProperty("taxJurisdiction")
  private String taxJurisdiction = null;

  @JsonProperty("quoteStatus")
  private String quoteStatus = null;

  @JsonProperty("shipFromVendor")
  private String shipFromVendor = null;

  @JsonProperty("shipIds")
  private List<String> shipIds = null;

  @JsonProperty("shipCouriers")
  private List<String> shipCouriers = null;

  @JsonProperty("additionalData")
  private String additionalData = null;

  @JsonProperty("consigmentType")
  private ConsigmentType consigmentType = null;

  @JsonProperty("lines")
  private List<SalesOrderLineInitialInformation> lines = null;

  public SalesOrderInitialInformation priceBranch(String priceBranch) {
    this.priceBranch = priceBranch;
    return this;
  }

   /**
   * Desc: The SalesOrderInitialInformation PriceBranch
   * @return priceBranch
  **/
  @NotNull
  @Schema(required = true, description = "Desc: The SalesOrderInitialInformation PriceBranch")
  public String getPriceBranch() {
    return priceBranch;
  }

  public void setPriceBranch(String priceBranch) {
    this.priceBranch = priceBranch;
  }

  public SalesOrderInitialInformation shipBranch(String shipBranch) {
    this.shipBranch = shipBranch;
    return this;
  }

   /**
   * Desc: The SalesOrderInitialInformation ShipBranch
   * @return shipBranch
  **/
  @NotNull
  @Schema(required = true, description = "Desc: The SalesOrderInitialInformation ShipBranch")
  public String getShipBranch() {
    return shipBranch;
  }

  public void setShipBranch(String shipBranch) {
    this.shipBranch = shipBranch;
  }

  public SalesOrderInitialInformation billToCustomer(String billToCustomer) {
    this.billToCustomer = billToCustomer;
    return this;
  }

   /**
   * Desc: The SalesOrderInitialInformation BillToCustomer
   * @return billToCustomer
  **/
  @Schema(description = "Desc: The SalesOrderInitialInformation BillToCustomer")
  public String getBillToCustomer() {
    return billToCustomer;
  }

  public void setBillToCustomer(String billToCustomer) {
    this.billToCustomer = billToCustomer;
  }

  public SalesOrderInitialInformation billToCustomerECommerceId(String billToCustomerECommerceId) {
    this.billToCustomerECommerceId = billToCustomerECommerceId;
    return this;
  }

   /**
   * Desc: The SalesOrderInitialInformation BillToCustomerECommerceId
   * @return billToCustomerECommerceId
  **/
  @Schema(description = "Desc: The SalesOrderInitialInformation BillToCustomerECommerceId")
  public String getBillToCustomerECommerceId() {
    return billToCustomerECommerceId;
  }

  public void setBillToCustomerECommerceId(String billToCustomerECommerceId) {
    this.billToCustomerECommerceId = billToCustomerECommerceId;
  }

  public SalesOrderInitialInformation shipToCustomer(String shipToCustomer) {
    this.shipToCustomer = shipToCustomer;
    return this;
  }

   /**
   * Desc: The SalesOrderInitialInformation ShipToCustomer
   * @return shipToCustomer
  **/
  @Schema(description = "Desc: The SalesOrderInitialInformation ShipToCustomer")
  public String getShipToCustomer() {
    return shipToCustomer;
  }

  public void setShipToCustomer(String shipToCustomer) {
    this.shipToCustomer = shipToCustomer;
  }

  public SalesOrderInitialInformation shipToCustomerECommerceId(String shipToCustomerECommerceId) {
    this.shipToCustomerECommerceId = shipToCustomerECommerceId;
    return this;
  }

   /**
   * Desc: The SalesOrderInitialInformation ShipToCustomerECommerceId
   * @return shipToCustomerECommerceId
  **/
  @Schema(description = "Desc: The SalesOrderInitialInformation ShipToCustomerECommerceId")
  public String getShipToCustomerECommerceId() {
    return shipToCustomerECommerceId;
  }

  public void setShipToCustomerECommerceId(String shipToCustomerECommerceId) {
    this.shipToCustomerECommerceId = shipToCustomerECommerceId;
  }

  public SalesOrderInitialInformation orderStatus(SalesOrderStatusType orderStatus) {
    this.orderStatus = orderStatus;
    return this;
  }

   /**
   * Get orderStatus
   * @return orderStatus
  **/
  @Valid
  @Schema(description = "")
  public SalesOrderStatusType getOrderStatus() {
    return orderStatus;
  }

  public void setOrderStatus(SalesOrderStatusType orderStatus) {
    this.orderStatus = orderStatus;
  }

  public SalesOrderInitialInformation salesSource(String salesSource) {
    this.salesSource = salesSource;
    return this;
  }

   /**
   * Desc: The SalesOrderInitialInformation SalesSource ID
   * @return salesSource
  **/
  @Schema(description = "Desc: The SalesOrderInitialInformation SalesSource ID")
  public String getSalesSource() {
    return salesSource;
  }

  public void setSalesSource(String salesSource) {
    this.salesSource = salesSource;
  }

  public SalesOrderInitialInformation shipDate(LocalDateTime shipDate) {
    this.shipDate = shipDate;
    return this;
  }

   /**
   * Desc: The SalesOrderInitialInformation ShipDate
   * @return shipDate
  **/
  @Valid
  @Schema(description = "Desc: The SalesOrderInitialInformation ShipDate")
  public LocalDateTime getShipDate() {
    return shipDate;
  }

  public void setShipDate(LocalDateTime shipDate) {
    this.shipDate = shipDate;
  }

  public SalesOrderInitialInformation requiredDate(LocalDateTime requiredDate) {
    this.requiredDate = requiredDate;
    return this;
  }

   /**
   * Desc: The SalesOrderInitialInformation RequiredDate
   * @return requiredDate
  **/
  @Valid
  @Schema(description = "Desc: The SalesOrderInitialInformation RequiredDate")
  public LocalDateTime getRequiredDate() {
    return requiredDate;
  }

  public void setRequiredDate(LocalDateTime requiredDate) {
    this.requiredDate = requiredDate;
  }

  public SalesOrderInitialInformation shipVia(String shipVia) {
    this.shipVia = shipVia;
    return this;
  }

   /**
   * Desc: The SalesOrderInitialInformation ShipVia
   * @return shipVia
  **/
  @Schema(description = "Desc: The SalesOrderInitialInformation ShipVia")
  public String getShipVia() {
    return shipVia;
  }

  public void setShipVia(String shipVia) {
    this.shipVia = shipVia;
  }

  public SalesOrderInitialInformation customerPONumber(String customerPONumber) {
    this.customerPONumber = customerPONumber;
    return this;
  }

   /**
   * Desc: The SalesOrderInitialInformation CustomerPONumber
   * @return customerPONumber
  **/
  @Schema(description = "Desc: The SalesOrderInitialInformation CustomerPONumber")
  public String getCustomerPONumber() {
    return customerPONumber;
  }

  public void setCustomerPONumber(String customerPONumber) {
    this.customerPONumber = customerPONumber;
  }

  public SalesOrderInitialInformation customerReleaseNumber(String customerReleaseNumber) {
    this.customerReleaseNumber = customerReleaseNumber;
    return this;
  }

   /**
   * Desc: The SalesOrderInitialInformation CustomerReleaseNumber
   * @return customerReleaseNumber
  **/
  @Schema(description = "Desc: The SalesOrderInitialInformation CustomerReleaseNumber")
  public String getCustomerReleaseNumber() {
    return customerReleaseNumber;
  }

  public void setCustomerReleaseNumber(String customerReleaseNumber) {
    this.customerReleaseNumber = customerReleaseNumber;
  }

  public SalesOrderInitialInformation insideSalesPerson(String insideSalesPerson) {
    this.insideSalesPerson = insideSalesPerson;
    return this;
  }

   /**
   * Desc: The SalesOrderInitialInformation InsideSalesPerson
   * @return insideSalesPerson
  **/
  @Schema(description = "Desc: The SalesOrderInitialInformation InsideSalesPerson")
  public String getInsideSalesPerson() {
    return insideSalesPerson;
  }

  public void setInsideSalesPerson(String insideSalesPerson) {
    this.insideSalesPerson = insideSalesPerson;
  }

  public SalesOrderInitialInformation outsideSalesPerson(String outsideSalesPerson) {
    this.outsideSalesPerson = outsideSalesPerson;
    return this;
  }

   /**
   * Desc: The SalesOrderInitialInformation OutsideSalesPerson
   * @return outsideSalesPerson
  **/
  @Schema(description = "Desc: The SalesOrderInitialInformation OutsideSalesPerson")
  public String getOutsideSalesPerson() {
    return outsideSalesPerson;
  }

  public void setOutsideSalesPerson(String outsideSalesPerson) {
    this.outsideSalesPerson = outsideSalesPerson;
  }

  public SalesOrderInitialInformation orderBy(String orderBy) {
    this.orderBy = orderBy;
    return this;
  }

   /**
   * Desc: The SalesOrderInitialInformation OrderBy
   * @return orderBy
  **/
  @Schema(description = "Desc: The SalesOrderInitialInformation OrderBy")
  public String getOrderBy() {
    return orderBy;
  }

  public void setOrderBy(String orderBy) {
    this.orderBy = orderBy;
  }

  public SalesOrderInitialInformation writer(String writer) {
    this.writer = writer;
    return this;
  }

   /**
   * Desc: The SalesOrderInitialInformation Writer
   * @return writer
  **/
  @Schema(description = "Desc: The SalesOrderInitialInformation Writer")
  public String getWriter() {
    return writer;
  }

  public void setWriter(String writer) {
    this.writer = writer;
  }

  public SalesOrderInitialInformation telephone(String telephone) {
    this.telephone = telephone;
    return this;
  }

   /**
   * Desc: The SalesOrderInitialInformation Telephone
   * @return telephone
  **/
  @Schema(description = "Desc: The SalesOrderInitialInformation Telephone")
  public String getTelephone() {
    return telephone;
  }

  public void setTelephone(String telephone) {
    this.telephone = telephone;
  }

  public SalesOrderInitialInformation email(String email) {
    this.email = email;
    return this;
  }

   /**
   * Desc: The SalesOrderInitialInformation Email
   * @return email
  **/
  @Schema(description = "Desc: The SalesOrderInitialInformation Email")
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public SalesOrderInitialInformation street1(String street1) {
    this.street1 = street1;
    return this;
  }

   /**
   * Desc: The SalesOrderInitialInformation Street1
   * @return street1
  **/
  @Schema(description = "Desc: The SalesOrderInitialInformation Street1")
  public String getStreet1() {
    return street1;
  }

  public void setStreet1(String street1) {
    this.street1 = street1;
  }

  public SalesOrderInitialInformation street2(String street2) {
    this.street2 = street2;
    return this;
  }

   /**
   * Desc: The SalesOrderInitialInformation Street2
   * @return street2
  **/
  @Schema(description = "Desc: The SalesOrderInitialInformation Street2")
  public String getStreet2() {
    return street2;
  }

  public void setStreet2(String street2) {
    this.street2 = street2;
  }

  public SalesOrderInitialInformation city(String city) {
    this.city = city;
    return this;
  }

   /**
   * Desc: The SalesOrderInitialInformation City
   * @return city
  **/
  @Schema(description = "Desc: The SalesOrderInitialInformation City")
  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public SalesOrderInitialInformation state(String state) {
    this.state = state;
    return this;
  }

   /**
   * Desc: The SalesOrderInitialInformation State
   * @return state
  **/
  @Schema(description = "Desc: The SalesOrderInitialInformation State")
  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public SalesOrderInitialInformation postalCode(String postalCode) {
    this.postalCode = postalCode;
    return this;
  }

   /**
   * Desc: The SalesOrderInitialInformation PostalCode
   * @return postalCode
  **/
  @Schema(description = "Desc: The SalesOrderInitialInformation PostalCode")
  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public SalesOrderInitialInformation country(String country) {
    this.country = country;
    return this;
  }

   /**
   * Desc: The SalesOrderInitialInformation Country
   * @return country
  **/
  @Schema(description = "Desc: The SalesOrderInitialInformation Country")
  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public SalesOrderInitialInformation internalNotes(String internalNotes) {
    this.internalNotes = internalNotes;
    return this;
  }

   /**
   * Desc: The SalesOrderInitialInformation InternalNotes
   * @return internalNotes
  **/
  @Schema(description = "Desc: The SalesOrderInitialInformation InternalNotes")
  public String getInternalNotes() {
    return internalNotes;
  }

  public void setInternalNotes(String internalNotes) {
    this.internalNotes = internalNotes;
  }

  public SalesOrderInitialInformation shippingInstructions(String shippingInstructions) {
    this.shippingInstructions = shippingInstructions;
    return this;
  }

   /**
   * Desc: The SalesOrderInitialInformation ShippingInstructions
   * @return shippingInstructions
  **/
  @Schema(description = "Desc: The SalesOrderInitialInformation ShippingInstructions")
  public String getShippingInstructions() {
    return shippingInstructions;
  }

  public void setShippingInstructions(String shippingInstructions) {
    this.shippingInstructions = shippingInstructions;
  }

  public SalesOrderInitialInformation termsCode(String termsCode) {
    this.termsCode = termsCode;
    return this;
  }

   /**
   * Desc: The SalesOrderInitialInformation TermsCode
   * @return termsCode
  **/
  @Schema(description = "Desc: The SalesOrderInitialInformation TermsCode")
  public String getTermsCode() {
    return termsCode;
  }

  public void setTermsCode(String termsCode) {
    this.termsCode = termsCode;
  }

  public SalesOrderInitialInformation freightCharges(Double freightCharges) {
    this.freightCharges = freightCharges;
    return this;
  }

   /**
   * Desc: The SalesOrderInitialInformation FreightCharges
   * @return freightCharges
  **/
  @Schema(description = "Desc: The SalesOrderInitialInformation FreightCharges")
  public Double getFreightCharges() {
    return freightCharges;
  }

  public void setFreightCharges(Double freightCharges) {
    this.freightCharges = freightCharges;
  }

  public SalesOrderInitialInformation handlingCharges(Double handlingCharges) {
    this.handlingCharges = handlingCharges;
    return this;
  }

   /**
   * Desc: The SalesOrderInitialInformation HandlingCharges
   * @return handlingCharges
  **/
  @Schema(description = "Desc: The SalesOrderInitialInformation HandlingCharges")
  public Double getHandlingCharges() {
    return handlingCharges;
  }

  public void setHandlingCharges(Double handlingCharges) {
    this.handlingCharges = handlingCharges;
  }

  public SalesOrderInitialInformation taxExemptCode(String taxExemptCode) {
    this.taxExemptCode = taxExemptCode;
    return this;
  }

   /**
   * Desc: The SalesOrderInitialInformation TaxExemptCode
   * @return taxExemptCode
  **/
  @Schema(description = "Desc: The SalesOrderInitialInformation TaxExemptCode")
  public String getTaxExemptCode() {
    return taxExemptCode;
  }

  public void setTaxExemptCode(String taxExemptCode) {
    this.taxExemptCode = taxExemptCode;
  }

  public SalesOrderInitialInformation taxExemptId(String taxExemptId) {
    this.taxExemptId = taxExemptId;
    return this;
  }

   /**
   * Desc: The SalesOrderInitialInformation TaxExemptId
   * @return taxExemptId
  **/
  @Schema(description = "Desc: The SalesOrderInitialInformation TaxExemptId")
  public String getTaxExemptId() {
    return taxExemptId;
  }

  public void setTaxExemptId(String taxExemptId) {
    this.taxExemptId = taxExemptId;
  }

  public SalesOrderInitialInformation taxJurisdiction(String taxJurisdiction) {
    this.taxJurisdiction = taxJurisdiction;
    return this;
  }

   /**
   * Desc: The SalesOrderInitialInformation TaxJuridiction
   * @return taxJurisdiction
  **/
  @Schema(description = "Desc: The SalesOrderInitialInformation TaxJuridiction")
  public String getTaxJurisdiction() {
    return taxJurisdiction;
  }

  public void setTaxJurisdiction(String taxJurisdiction) {
    this.taxJurisdiction = taxJurisdiction;
  }

  public SalesOrderInitialInformation quoteStatus(String quoteStatus) {
    this.quoteStatus = quoteStatus;
    return this;
  }

   /**
   * Desc: The SalesOrderInitialInformation QuoteStatys
   * @return quoteStatus
  **/
  @Schema(description = "Desc: The SalesOrderInitialInformation QuoteStatys")
  public String getQuoteStatus() {
    return quoteStatus;
  }

  public void setQuoteStatus(String quoteStatus) {
    this.quoteStatus = quoteStatus;
  }

  public SalesOrderInitialInformation shipFromVendor(String shipFromVendor) {
    this.shipFromVendor = shipFromVendor;
    return this;
  }

   /**
   * Desc: The SalesOrderInitialInformation ShipFromVendor
   * @return shipFromVendor
  **/
  @Schema(description = "Desc: The SalesOrderInitialInformation ShipFromVendor")
  public String getShipFromVendor() {
    return shipFromVendor;
  }

  public void setShipFromVendor(String shipFromVendor) {
    this.shipFromVendor = shipFromVendor;
  }

  public SalesOrderInitialInformation shipIds(List<String> shipIds) {
    this.shipIds = shipIds;
    return this;
  }

  public SalesOrderInitialInformation addShipIdsItem(String shipIdsItem) {
    if (this.shipIds == null) {
      this.shipIds = new ArrayList<>();
    }
    this.shipIds.add(shipIdsItem);
    return this;
  }

   /**
   * Desc: The SalesOrderInitialInformation ShipIds
   * @return shipIds
  **/
  @Schema(description = "Desc: The SalesOrderInitialInformation ShipIds")
  public List<String> getShipIds() {
    return shipIds;
  }

  public void setShipIds(List<String> shipIds) {
    this.shipIds = shipIds;
  }

  public SalesOrderInitialInformation shipCouriers(List<String> shipCouriers) {
    this.shipCouriers = shipCouriers;
    return this;
  }

  public SalesOrderInitialInformation addShipCouriersItem(String shipCouriersItem) {
    if (this.shipCouriers == null) {
      this.shipCouriers = new ArrayList<>();
    }
    this.shipCouriers.add(shipCouriersItem);
    return this;
  }

   /**
   * Desc: The SalesOrderInitialInformation ShipCouriers
   * @return shipCouriers
  **/
  @Schema(description = "Desc: The SalesOrderInitialInformation ShipCouriers")
  public List<String> getShipCouriers() {
    return shipCouriers;
  }

  public void setShipCouriers(List<String> shipCouriers) {
    this.shipCouriers = shipCouriers;
  }

  public SalesOrderInitialInformation additionalData(String additionalData) {
    this.additionalData = additionalData;
    return this;
  }

   /**
   * Desc: The SalesOrderInitialInformation AdditionalData
   * @return additionalData
  **/
  @Schema(description = "Desc: The SalesOrderInitialInformation AdditionalData")
  public String getAdditionalData() {
    return additionalData;
  }

  public void setAdditionalData(String additionalData) {
    this.additionalData = additionalData;
  }

  public SalesOrderInitialInformation consigmentType(ConsigmentType consigmentType) {
    this.consigmentType = consigmentType;
    return this;
  }

   /**
   * Get consigmentType
   * @return consigmentType
  **/
  @Valid
  @Schema(description = "")
  public ConsigmentType getConsigmentType() {
    return consigmentType;
  }

  public void setConsigmentType(ConsigmentType consigmentType) {
    this.consigmentType = consigmentType;
  }

  public SalesOrderInitialInformation lines(List<SalesOrderLineInitialInformation> lines) {
    this.lines = lines;
    return this;
  }

  public SalesOrderInitialInformation addLinesItem(SalesOrderLineInitialInformation linesItem) {
    if (this.lines == null) {
      this.lines = new ArrayList<>();
    }
    this.lines.add(linesItem);
    return this;
  }

   /**
   * A list of SalesOrder Lines
   * @return lines
  **/
  @Valid
  @Schema(description = "A list of SalesOrder Lines")
  public List<SalesOrderLineInitialInformation> getLines() {
    return lines;
  }

  public void setLines(List<SalesOrderLineInitialInformation> lines) {
    this.lines = lines;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SalesOrderInitialInformation salesOrderInitialInformation = (SalesOrderInitialInformation) o;
    return Objects.equals(this.priceBranch, salesOrderInitialInformation.priceBranch) &&
        Objects.equals(this.shipBranch, salesOrderInitialInformation.shipBranch) &&
        Objects.equals(this.billToCustomer, salesOrderInitialInformation.billToCustomer) &&
        Objects.equals(this.billToCustomerECommerceId, salesOrderInitialInformation.billToCustomerECommerceId) &&
        Objects.equals(this.shipToCustomer, salesOrderInitialInformation.shipToCustomer) &&
        Objects.equals(this.shipToCustomerECommerceId, salesOrderInitialInformation.shipToCustomerECommerceId) &&
        Objects.equals(this.orderStatus, salesOrderInitialInformation.orderStatus) &&
        Objects.equals(this.salesSource, salesOrderInitialInformation.salesSource) &&
        Objects.equals(this.shipDate, salesOrderInitialInformation.shipDate) &&
        Objects.equals(this.requiredDate, salesOrderInitialInformation.requiredDate) &&
        Objects.equals(this.shipVia, salesOrderInitialInformation.shipVia) &&
        Objects.equals(this.customerPONumber, salesOrderInitialInformation.customerPONumber) &&
        Objects.equals(this.customerReleaseNumber, salesOrderInitialInformation.customerReleaseNumber) &&
        Objects.equals(this.insideSalesPerson, salesOrderInitialInformation.insideSalesPerson) &&
        Objects.equals(this.outsideSalesPerson, salesOrderInitialInformation.outsideSalesPerson) &&
        Objects.equals(this.orderBy, salesOrderInitialInformation.orderBy) &&
        Objects.equals(this.writer, salesOrderInitialInformation.writer) &&
        Objects.equals(this.telephone, salesOrderInitialInformation.telephone) &&
        Objects.equals(this.email, salesOrderInitialInformation.email) &&
        Objects.equals(this.street1, salesOrderInitialInformation.street1) &&
        Objects.equals(this.street2, salesOrderInitialInformation.street2) &&
        Objects.equals(this.city, salesOrderInitialInformation.city) &&
        Objects.equals(this.state, salesOrderInitialInformation.state) &&
        Objects.equals(this.postalCode, salesOrderInitialInformation.postalCode) &&
        Objects.equals(this.country, salesOrderInitialInformation.country) &&
        Objects.equals(this.internalNotes, salesOrderInitialInformation.internalNotes) &&
        Objects.equals(this.shippingInstructions, salesOrderInitialInformation.shippingInstructions) &&
        Objects.equals(this.termsCode, salesOrderInitialInformation.termsCode) &&
        Objects.equals(this.freightCharges, salesOrderInitialInformation.freightCharges) &&
        Objects.equals(this.handlingCharges, salesOrderInitialInformation.handlingCharges) &&
        Objects.equals(this.taxExemptCode, salesOrderInitialInformation.taxExemptCode) &&
        Objects.equals(this.taxExemptId, salesOrderInitialInformation.taxExemptId) &&
        Objects.equals(this.taxJurisdiction, salesOrderInitialInformation.taxJurisdiction) &&
        Objects.equals(this.quoteStatus, salesOrderInitialInformation.quoteStatus) &&
        Objects.equals(this.shipFromVendor, salesOrderInitialInformation.shipFromVendor) &&
        Objects.equals(this.shipIds, salesOrderInitialInformation.shipIds) &&
        Objects.equals(this.shipCouriers, salesOrderInitialInformation.shipCouriers) &&
        Objects.equals(this.additionalData, salesOrderInitialInformation.additionalData) &&
        Objects.equals(this.consigmentType, salesOrderInitialInformation.consigmentType) &&
        Objects.equals(this.lines, salesOrderInitialInformation.lines);
  }

  @Override
  public int hashCode() {
    return Objects.hash(priceBranch, shipBranch, billToCustomer, billToCustomerECommerceId, shipToCustomer, shipToCustomerECommerceId, orderStatus, salesSource, shipDate, requiredDate, shipVia, customerPONumber, customerReleaseNumber, insideSalesPerson, outsideSalesPerson, orderBy, writer, telephone, email, street1, street2, city, state, postalCode, country, internalNotes, shippingInstructions, termsCode, freightCharges, handlingCharges, taxExemptCode, taxExemptId, taxJurisdiction, quoteStatus, shipFromVendor, shipIds, shipCouriers, additionalData, consigmentType, lines);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SalesOrderInitialInformation {\n");
    
    sb.append("    priceBranch: ").append(toIndentedString(priceBranch)).append("\n");
    sb.append("    shipBranch: ").append(toIndentedString(shipBranch)).append("\n");
    sb.append("    billToCustomer: ").append(toIndentedString(billToCustomer)).append("\n");
    sb.append("    billToCustomerECommerceId: ").append(toIndentedString(billToCustomerECommerceId)).append("\n");
    sb.append("    shipToCustomer: ").append(toIndentedString(shipToCustomer)).append("\n");
    sb.append("    shipToCustomerECommerceId: ").append(toIndentedString(shipToCustomerECommerceId)).append("\n");
    sb.append("    orderStatus: ").append(toIndentedString(orderStatus)).append("\n");
    sb.append("    salesSource: ").append(toIndentedString(salesSource)).append("\n");
    sb.append("    shipDate: ").append(toIndentedString(shipDate)).append("\n");
    sb.append("    requiredDate: ").append(toIndentedString(requiredDate)).append("\n");
    sb.append("    shipVia: ").append(toIndentedString(shipVia)).append("\n");
    sb.append("    customerPONumber: ").append(toIndentedString(customerPONumber)).append("\n");
    sb.append("    customerReleaseNumber: ").append(toIndentedString(customerReleaseNumber)).append("\n");
    sb.append("    insideSalesPerson: ").append(toIndentedString(insideSalesPerson)).append("\n");
    sb.append("    outsideSalesPerson: ").append(toIndentedString(outsideSalesPerson)).append("\n");
    sb.append("    orderBy: ").append(toIndentedString(orderBy)).append("\n");
    sb.append("    writer: ").append(toIndentedString(writer)).append("\n");
    sb.append("    telephone: ").append(toIndentedString(telephone)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    street1: ").append(toIndentedString(street1)).append("\n");
    sb.append("    street2: ").append(toIndentedString(street2)).append("\n");
    sb.append("    city: ").append(toIndentedString(city)).append("\n");
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
    sb.append("    postalCode: ").append(toIndentedString(postalCode)).append("\n");
    sb.append("    country: ").append(toIndentedString(country)).append("\n");
    sb.append("    internalNotes: ").append(toIndentedString(internalNotes)).append("\n");
    sb.append("    shippingInstructions: ").append(toIndentedString(shippingInstructions)).append("\n");
    sb.append("    termsCode: ").append(toIndentedString(termsCode)).append("\n");
    sb.append("    freightCharges: ").append(toIndentedString(freightCharges)).append("\n");
    sb.append("    handlingCharges: ").append(toIndentedString(handlingCharges)).append("\n");
    sb.append("    taxExemptCode: ").append(toIndentedString(taxExemptCode)).append("\n");
    sb.append("    taxExemptId: ").append(toIndentedString(taxExemptId)).append("\n");
    sb.append("    taxJurisdiction: ").append(toIndentedString(taxJurisdiction)).append("\n");
    sb.append("    quoteStatus: ").append(toIndentedString(quoteStatus)).append("\n");
    sb.append("    shipFromVendor: ").append(toIndentedString(shipFromVendor)).append("\n");
    sb.append("    shipIds: ").append(toIndentedString(shipIds)).append("\n");
    sb.append("    shipCouriers: ").append(toIndentedString(shipCouriers)).append("\n");
    sb.append("    additionalData: ").append(toIndentedString(additionalData)).append("\n");
    sb.append("    consigmentType: ").append(toIndentedString(consigmentType)).append("\n");
    sb.append("    lines: ").append(toIndentedString(lines)).append("\n");
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
