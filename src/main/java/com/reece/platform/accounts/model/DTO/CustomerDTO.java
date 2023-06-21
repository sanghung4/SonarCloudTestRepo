package com.reece.platform.accounts.model.DTO;

import lombok.Data;

@Data
public class CustomerDTO {

    private String id;
    private String name;
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String addressLine4;
    private String city;
    private String state;
    private String postalCode;
    private String homeTerritory;
    private String homeBranch;
    private Boolean isBillTo;
    private Boolean isShipTo;
    private Boolean isBranch;
    private Boolean totalCreditHoldFlag;
    private String billToId;
    private Boolean alwaysCod;
    // NOTE: There are more fields in the response for an eclipse customer
    // For now, just using a subset
}
