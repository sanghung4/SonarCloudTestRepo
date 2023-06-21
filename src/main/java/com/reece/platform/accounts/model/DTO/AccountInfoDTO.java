package com.reece.platform.accounts.model.DTO;

import com.reece.platform.accounts.model.enums.ErpEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Representation of account information for user creation
 */

@Data
@NoArgsConstructor
public class AccountInfoDTO {
    private ErpEnum erpId;
    private String accountNumber;
    private String companyName;
    private String companyAddress1;
    private String companyAddress2;
    private String companyAddressCity;
    private String companyAddressState;
    private String companyAddressZip;
    private String companyEmail;
    private String companyPhoneNumber;

    public AccountInfoDTO(ErpEnum erp, String accountNumber, String companyName) {
        this.erpId = erp;
        this.accountNumber = accountNumber;
        this.companyName = companyName;
    }
}
