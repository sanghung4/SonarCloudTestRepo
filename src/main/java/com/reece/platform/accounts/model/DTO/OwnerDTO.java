package com.reece.platform.accounts.model.DTO;
import lombok.Data;

@Data
public class OwnerDTO {
    private String ownerName;
    private String streetLineOne;
    private String streetLineTwo;
    private String streetLineThree;
    private String city;
    private String state;
    private String postalCode;
    private String phoneNumber;
}
