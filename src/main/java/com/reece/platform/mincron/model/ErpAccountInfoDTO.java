package com.reece.platform.mincron.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ErpAccountInfoDTO {
    public ErpAccountInfoDTO() {}

    public ErpAccountInfoDTO(GetAccountResponseDTO getAccountResponseDTO) {
        this.erpAccountId = getAccountResponseDTO.getErpAccountId().trim();
        this.companyName = getAccountResponseDTO.getCompanyName().trim();
        this.street1 = getAccountResponseDTO.getStreet1();
        this.street2 = getAccountResponseDTO.getStreet2();
        this.city = getAccountResponseDTO.getCity();
        this.state = getAccountResponseDTO.getState();
        this.zip = getAccountResponseDTO.getZip();

        // These are not supplied by Mincron but are needed upstream in the Account Mgmt service
        this.phoneNumber = "";
        this.email = new ArrayList<>();
    }

    public ErpAccountInfoDTO(MincronJobListJobDTO mincronJobListJobDTO) {
        this.erpAccountId = mincronJobListJobDTO.getCustomerJobNumber().trim();
        this.companyName = mincronJobListJobDTO.getCustomerJobNumber().trim();
        this.street1 = mincronJobListJobDTO.getAddress().getAddress1();
        this.street2 = mincronJobListJobDTO.getAddress().getAddress2();
        this.city = mincronJobListJobDTO.getAddress().getCity();
        this.state = mincronJobListJobDTO.getAddress().getState();
        this.zip = mincronJobListJobDTO.getAddress().getZip();
        // Branch IDs in data systems outside of Mincron prefix Mincron branches with a "6"
        this.branchId = String.format("6%s", mincronJobListJobDTO.getBranch().getBranchNumber());

        // These are not supplied by Mincron but are needed upstream in the Account Mgmt service
        this.phoneNumber = "";
        this.email = new ArrayList<>();
    }

    private String erpAccountId;
    private String companyName;
    private String street1;
    private String street2;
    private String city;
    private String state;
    private String zip;
    private String branchId;
    private String phoneNumber;
    private List<String> email;
}
