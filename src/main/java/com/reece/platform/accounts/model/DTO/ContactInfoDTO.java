package com.reece.platform.accounts.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representation of all user creation data
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactInfoDTO {
    private String phoneNumber;
    private String emailAddress;
    private Boolean isBranchInfo;
}
