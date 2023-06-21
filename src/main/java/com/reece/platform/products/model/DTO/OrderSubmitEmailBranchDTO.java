package com.reece.platform.products.model.DTO;

import lombok.Data;

@Data
public class OrderSubmitEmailBranchDTO {

    public OrderSubmitEmailBranchDTO(BranchOrderInfoDTO branchInfo) {
        this.branchName = branchInfo.getBranchName();
        this.streetLineOne = branchInfo.getStreetLineOne();
        this.streetLineTwo = branchInfo.getStreetLineTwo();
        this.streetLineThree = branchInfo.getStreetLineThree();
        this.city = branchInfo.getCity();
        this.state = branchInfo.getState();
        this.postalCode = branchInfo.getPostalCode();
        this.country = branchInfo.getCountry();
        this.branchHours = branchInfo.getBranchHours();
        this.branchPhone = branchInfo.getBranchPhone();
    }

    private String branchName;
    private String streetLineOne;
    private String streetLineTwo;
    private String streetLineThree;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private String branchPhone;
    private String branchHours;
}
