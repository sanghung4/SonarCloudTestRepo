package com.reece.platform.accounts.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountValidationResponseDTO {

    private String accountName;
    private Boolean isTradeAccount;
}
