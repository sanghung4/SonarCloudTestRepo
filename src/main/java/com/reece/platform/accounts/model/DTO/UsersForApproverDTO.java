package com.reece.platform.accounts.model.DTO;

import com.reece.platform.accounts.model.entity.User;
import com.reece.platform.accounts.model.enums.PhoneTypeEnum;
import lombok.Data;

import java.util.UUID;

@Data
public class UsersForApproverDTO {
    private String firstName;
    private String lastName;
    private String email;

    public UsersForApproverDTO() {}

    public UsersForApproverDTO(User user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
    }
}
