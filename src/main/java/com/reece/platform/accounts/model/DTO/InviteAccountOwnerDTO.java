package com.reece.platform.accounts.model.DTO;

import com.reece.platform.accounts.model.enums.ErpEnum;
import lombok.Data;

import java.util.UUID;

@Data
public class InviteAccountOwnerDTO {
    private String email;
    private String firstName;
    private String lastName;
    private UUID userRoleId;
    private ErpEnum erpId;
    private String erpAccountId;
}
