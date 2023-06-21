package com.reece.platform.accounts.model.DTO.ERP;

import lombok.Data;

import java.util.UUID;

@Data
public class ErpUserInformationDTO {
    private String userId;
    private String password;
    private String erpAccountId;
    private String name;
    private String erpSystemName;
}
