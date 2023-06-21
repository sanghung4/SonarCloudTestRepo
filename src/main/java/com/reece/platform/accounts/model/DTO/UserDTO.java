package com.reece.platform.accounts.model.DTO;

import com.reece.platform.accounts.model.entity.User;
import com.reece.platform.accounts.model.enums.PhoneTypeEnum;
import lombok.Data;

import java.util.UUID;

@Data
public class UserDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private UUID roleId;
    private UUID approverId;
    private PhoneTypeEnum phoneTypeId;
    private String phoneTypeDisplayName;
    private String erpUserPassword;

    public UserDTO() {}

    public UserDTO(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.phoneNumber = user.getPhoneNumber();
        this.email = user.getEmail();
        this.roleId = user.getRole().getId();

        if (user.getApprover() != null) {
            this.approverId = user.getApprover().getId();
        }

        if (user.getPhoneType() != null) {
            this.phoneTypeId = user.getPhoneType();
            this.phoneTypeDisplayName = user.getPhoneType().getDisplayName();
        }
    }
}
