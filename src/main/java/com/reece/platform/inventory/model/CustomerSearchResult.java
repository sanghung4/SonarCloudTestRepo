package com.reece.platform.inventory.model;

import lombok.Data;

import java.util.List;

@Data
public class CustomerSearchResult {
    private String name;
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String addressLine4;
    private String city;
    private String state;
    private String postalCode;
    private String countryCode;
    private Boolean isBillTo;
    private Boolean isShipTo;
    private Boolean isBranchCash;
    private Boolean isProspect;
    private String sortBy;
    private String nameIndex;
    private String billToId;
    private String defaultPriceClass;
    private String ediId;
    private String orderEntryMessage;
    private String updateKey;
    private String id;
    private List<ShipToId> shipToLists;
}