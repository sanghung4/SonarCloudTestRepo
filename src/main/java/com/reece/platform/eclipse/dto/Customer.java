package com.reece.platform.eclipse.dto;

import lombok.Data;

@Data
public class Customer {

    private String id;
    private String homeBranch;
    private String name;
    private Boolean isBillTo;
    private Boolean isShipTo;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String postalCode;
    private String countryCode;
}
