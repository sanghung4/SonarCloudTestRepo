package com.reece.platform.accounts.model.DTO.ERP;

import lombok.Data;

@Data
public class ErpUserResponseDTO {
    private String erpId;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String phoneType;
}
