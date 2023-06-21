package com.reece.platform.eclipse.model.DTO;

import lombok.Data;

@Data
public class ContactDTO {
    private String streetLineOne;
    private String streetLineTwo;
    private String streetLineThree;
    private String city;
    private String state;
    private String postalCode;
    private String phoneNumber;
}
