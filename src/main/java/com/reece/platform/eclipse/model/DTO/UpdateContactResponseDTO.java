package com.reece.platform.eclipse.model.DTO;

import lombok.Data;

import java.util.List;

@Data
public class UpdateContactResponseDTO {
    private String firstName;
    private String lastName;
    private List<String> phoneNumber;
    private List<String> email;
    private String phoneTypeDisplayName;
}
