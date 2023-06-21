package com.reece.platform.accounts.model.entity;

import com.reece.platform.accounts.model.DTO.InviteAccountOwnerDTO;
import com.reece.platform.accounts.model.DTO.InviteUserDTO;
import com.reece.platform.accounts.model.enums.ErpEnum;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Audited
@Table(name = "invited_users")
public class InvitedUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "pg-uuid")
    @Column(name = "id")
    private UUID id;

    @Column(name = "email")
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "user_role_id")
    private UUID userRoleId;

    @Column(name = "bill_to_account_id")
    private UUID billToAccountId;

    @Column(name = "approver_id")
    private UUID approverId;

    @Column(name = "erp_account_id")
    private String erpAccountId;

    @Column(name = "erp")
    @Enumerated(EnumType.STRING)
    private ErpEnum erp;

    @Column(name = "is_completed")
    private boolean isCompleted;

    @Column(name = "is_email_sent")
    private boolean isEmailSent;

    public InvitedUser(InviteUserDTO inviteUserDTO) {
        this.email = inviteUserDTO.getEmail();
        this.firstName = inviteUserDTO.getFirstName();
        this.lastName = inviteUserDTO.getLastName();
        this.userRoleId = inviteUserDTO.getUserRoleId();
        this.billToAccountId = inviteUserDTO.getBillToAccountId();
        this.approverId = inviteUserDTO.getApproverId();
        this.erpAccountId = inviteUserDTO.getErpAccountId();
        this.erp = inviteUserDTO.getErp();
        this.isCompleted = false;
        this.isEmailSent = false;
    }

    public InvitedUser(InviteAccountOwnerDTO invitedLegacyAdminDTO) {
        this.email = invitedLegacyAdminDTO.getEmail();
        this.firstName = invitedLegacyAdminDTO.getFirstName();
        this.lastName = invitedLegacyAdminDTO.getLastName();
        this.userRoleId = invitedLegacyAdminDTO.getUserRoleId();
        this.erpAccountId = invitedLegacyAdminDTO.getErpAccountId();
        this.erp = invitedLegacyAdminDTO.getErpId();
        this.isCompleted = false;
    }
}
