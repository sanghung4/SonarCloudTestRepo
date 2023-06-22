package com.reece.platform.mincron.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data model for account return from Mincron
**/
@Data
@NoArgsConstructor
public class GetAccountResponseDTO {
    public GetAccountResponseDTO(ErpAccountInfoDTO erpAccountInfoDTO, String billToPhoneNumber) {
        this.erpAccountId = erpAccountInfoDTO.getErpAccountId();
        this.companyName = erpAccountInfoDTO.getCompanyName();
        this.city = erpAccountInfoDTO.getCity();
        this.state = erpAccountInfoDTO.getState();
        this.street1 = erpAccountInfoDTO.getStreet1();
        this.street2 = erpAccountInfoDTO.getStreet2();
        this.zip = erpAccountInfoDTO.getZip();
        this.branchId = erpAccountInfoDTO.getBranchId();
        this.phoneNumber = billToPhoneNumber; // Use bill-to phone number since job accounts don't have a unique phone number
    }

    private String erpAccountId;
    private String companyName;
    private String city;
    private String state;
    private String street1;
    private String street2;
    private String zip;
    private String branchId;
    private String phoneNumber;
    private List<GetAccountResponseDTO> shipToAccounts;
}
