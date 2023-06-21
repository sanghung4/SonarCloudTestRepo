package com.reece.platform.accounts.model.DTO;

import com.reece.platform.accounts.model.entity.User;
import lombok.Data;

import java.util.UUID;

@Data
public class ApproverDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;

    public ApproverDTO() {}

    public ApproverDTO(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
    }
}
