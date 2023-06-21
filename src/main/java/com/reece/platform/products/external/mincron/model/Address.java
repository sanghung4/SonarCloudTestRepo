package com.reece.platform.products.external.mincron.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Address {

    private String address1;
    private String address2;
    private String address3;
    private String city;
    private String state;
    private String zip;
    private String county;
    private String country;
}
