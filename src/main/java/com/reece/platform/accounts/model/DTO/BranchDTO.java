package com.reece.platform.accounts.model.DTO;

import com.reece.platform.accounts.model.enums.ErpEnum;
import lombok.Data;

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
    private Boolean isPricingOnly;
}
