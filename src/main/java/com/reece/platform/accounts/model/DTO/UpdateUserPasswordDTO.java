package com.reece.platform.accounts.model.DTO;

import lombok.Data;

@Data
public class UpdateUserPasswordDTO {
    private String oldUserPassword;
    private String newUserPassword;
}
