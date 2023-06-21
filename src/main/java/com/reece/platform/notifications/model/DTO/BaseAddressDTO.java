package com.reece.platform.notifications.model.DTO;

import lombok.Data;

@Data
public class BaseAddressDTO {
    private String streetLineOne;
    private String streetLineTwo;
    private String streetLineThree;
    private String city;
    private String state;
    private String postalCode;
    private String country;
}
