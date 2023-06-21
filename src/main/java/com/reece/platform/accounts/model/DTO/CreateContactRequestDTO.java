package com.reece.platform.accounts.model.DTO;

import lombok.Data;

@Data
public class CreateContactRequestDTO {
    private String firstName;
    private String lastName;
    private String telephone;
    private String email;
    private String phoneType;
}
