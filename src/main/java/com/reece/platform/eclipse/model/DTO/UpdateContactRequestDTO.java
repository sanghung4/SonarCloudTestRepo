package com.reece.platform.eclipse.model.DTO;

import lombok.Data;

import java.util.List;

@Data
public class UpdateContactRequestDTO {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String phoneTypeDisplayName;
}
