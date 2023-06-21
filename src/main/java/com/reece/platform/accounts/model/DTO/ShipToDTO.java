package com.reece.platform.accounts.model.DTO;

import lombok.Data;

@Data
public class ShipToDTO {
    private String name;
    private String addressLine1;
    private String city;
    private String state;
    private String postalCode;
    private String email;
    private String phone;
}
