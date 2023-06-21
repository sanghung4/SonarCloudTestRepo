package com.reece.platform.eclipse.model.DTO;

import lombok.Data;

import java.util.List;

@Data
public class ErpAccountInfoDTO {
    public ErpAccountInfoDTO() {}

    public ErpAccountInfoDTO(GetAccountResponseDTO getAccountResponseDTO) {
        this.erpAccountId = getAccountResponseDTO.getErpAccountId();
        this.companyName = getAccountResponseDTO.getCompanyName();
        this.street1 = getAccountResponseDTO.getStreet1();
        this.street2 = getAccountResponseDTO.getStreet2();
        this.city = getAccountResponseDTO.getCity();
        this.state = getAccountResponseDTO.getState();
        this.zip = getAccountResponseDTO.getZip();
        this.phoneNumber = getAccountResponseDTO.getPhoneNumber();
        if (getAccountResponseDTO.getEmail() != null && !getAccountResponseDTO.getEmail().isEmpty()) {
            this.email = getAccountResponseDTO.getEmail();
        }

    }

    private String erpAccountId;
    private String companyName;
    private String street1;
    private String street2;
    private String city;
    private String state;
    private String zip;
    private String phoneNumber;
    private List<String> email;
}
