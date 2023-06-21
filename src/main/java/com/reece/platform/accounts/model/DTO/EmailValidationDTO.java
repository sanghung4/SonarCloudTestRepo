package com.reece.platform.accounts.model.DTO;

import lombok.Data;

@Data
public class EmailValidationDTO {
    private Boolean isValid;
    private Boolean isEmployee;

    public EmailValidationDTO() {
        isValid = true;
        isEmployee = false;
    }
}
