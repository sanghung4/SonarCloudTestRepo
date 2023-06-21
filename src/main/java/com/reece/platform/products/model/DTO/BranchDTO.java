package com.reece.platform.products.model.DTO;

import com.reece.platform.products.branches.model.DTO.BranchResponseDTO;
import com.reece.platform.products.model.ErpEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class BranchDTO {

    private String branchId;
    private String name;
    private String entityId;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String zip;
    private String phone;
    private ErpEnum erpSystemName;
    private String website;
    private String workdayId;
    private String workdayLocationName;
    private String actingBranchManager;
    private String actingBranchManagerPhone;
    private String actingBranchManagerEmail;
    private String businessHours;
    private Double latitude;
    private Double longitude;
    private Boolean isPlumbing;
    private Boolean isWaterworks;
    private Boolean isHvac;
    private Boolean isBandK;
    private String brand;
    private String domain;

    public BranchDTO(BranchResponseDTO branch) {
        this.branchId = branch.getBranchId();
        this.name = branch.getName();
        this.entityId = branch.getEntityId();
        this.address1 = branch.getAddress1();
        this.address2 = branch.getAddress2();
        this.city = branch.getCity();
        this.state = branch.getState();
        this.zip = branch.getZip();
        this.phone = branch.getPhone();
        this.erpSystemName = branch.getErpSystemName();
        this.website = branch.getWebsite();
        this.workdayId = branch.getWorkdayId();
        this.workdayLocationName = branch.getWorkdayLocationName();
        this.actingBranchManager = branch.getActingBranchManager();
        this.actingBranchManagerPhone = branch.getActingBranchManagerPhone();
        this.actingBranchManagerEmail = branch.getActingBranchManagerEmail();
        this.businessHours = branch.getBusinessHours();
        this.latitude = branch.getLatitude();
        this.longitude = branch.getLongitude();
        this.isPlumbing = branch.getIsPlumbing();
        this.isWaterworks = branch.getIsWaterworks();
        this.isHvac = branch.getIsHvac();
        this.isBandK = branch.getIsBandK();
        this.brand = branch.getBrand();
        this.domain = branch.getDomain();
    }
}
