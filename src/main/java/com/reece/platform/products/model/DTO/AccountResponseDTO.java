package com.reece.platform.products.model.DTO;

import java.util.List;
import java.util.UUID;
import lombok.Data;

/**
 * Account information model for shipTo and billTo account
 */
@Data
public class AccountResponseDTO {

    private UUID id;
    private UUID erpId;
    private String erpAccountId;
    private String companyName;
    private String street1;
    private String street2;
    private String city;
    private String state;
    private String zip;
    private String phoneNumber;
    private String branchId;
    private List<String> email;
    private List<AccountResponseDTO> shipToAccounts;
    private String erpName;
    private String poReleaseRequired;
}
