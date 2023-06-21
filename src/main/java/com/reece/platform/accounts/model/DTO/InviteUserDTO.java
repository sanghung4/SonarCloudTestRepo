package com.reece.platform.accounts.model.DTO;

import com.reece.platform.accounts.model.enums.ErpEnum;
import lombok.Data;

import java.util.UUID;

@Data
public class InviteUserDTO {
    private String email;
    private String firstName;
    private String lastName;
    private UUID userRoleId;
    private UUID billToAccountId;
    private UUID approverId;
    private String erpAccountId;
    private ErpEnum erp;

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }
}
