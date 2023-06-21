package com.reece.platform.accounts.model.DTO;

import com.reece.platform.accounts.model.entity.InvitedUser;
import com.reece.platform.accounts.model.enums.ErpEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@Data
public class InvitedUserResponseDTO {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private UUID userRoleId;
    private UUID billToAccountId;
    private UUID approverId;
    private String erpAccountId;
    private Boolean completed;
    private ErpEnum erpSystemName;

    public InvitedUserResponseDTO(InvitedUser invitedUser) {
        this.id = invitedUser.getId();
        this.email = invitedUser.getEmail();
        this.firstName = invitedUser.getFirstName();
        this.lastName = invitedUser.getLastName();
        this.userRoleId = invitedUser.getUserRoleId();
        this.billToAccountId = invitedUser.getBillToAccountId();
        this.approverId = invitedUser.getApproverId();
        this.erpAccountId = invitedUser.getErpAccountId();
        this.completed = invitedUser.isCompleted();
        this.erpSystemName = invitedUser.getErp();
    }
}
