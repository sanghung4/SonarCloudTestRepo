package com.reece.platform.accounts.model.DTO.API;

import com.reece.platform.accounts.model.DTO.ApproverDTO;
import com.reece.platform.accounts.model.entity.User;
import com.reece.platform.accounts.model.enums.PhoneTypeEnum;
import com.reece.platform.accounts.model.enums.RoleEnum;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class ApiUserResponseDTO {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private PhoneTypeEnum phoneType;
    private ApiRoleResponseDTO role;
    private ApproverDTO approver;
    private UUID approverId;
    private Boolean isEmployee;
    private List<UUID> ecommShipToIds;
    private Date contactUpdatedAt;
    private String contactUpdatedBy;

    public ApiUserResponseDTO() {}

    public ApiUserResponseDTO(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.phoneNumber = user.getPhoneNumber();
        this.isEmployee = false;

        if (user.getRole() != null) {
            var roleName = user.getRole().getName();
            this.isEmployee = roleName.equals(RoleEnum.MORSCO_ADMIN.label)
                    || roleName.equals(RoleEnum.MORSCO_ENGINEER.label);
        }

        if (user.getPhoneType() != null) {
            this.phoneType = user.getPhoneType();
        }

        if (user.getRole() != null) {
            this.role = new ApiRoleResponseDTO(user.getRole());
        }

        if (user.getApprover() != null) {
            this.approverId = user.getApprover().getId();
        }

        var maybeErpsUser = user.getErpsUsers().stream().findFirst();

        maybeErpsUser.ifPresent( erpsUser -> {
            contactUpdatedAt = erpsUser.getLastModifiedDate();
            contactUpdatedBy = erpsUser.getLastModifiedBy();
        });
    }
}
