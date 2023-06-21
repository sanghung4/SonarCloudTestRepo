package com.reece.platform.eclipse.model.DTO;

import lombok.Data;

import java.util.List;

@Data
public class GetContactResponseDTO {
    private String firstName;
    private String lastName;
    private List<String> telephone;
    private List<String> email;
}
