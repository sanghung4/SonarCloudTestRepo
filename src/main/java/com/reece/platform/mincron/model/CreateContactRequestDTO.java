package com.reece.platform.mincron.model;

import lombok.Data;

import java.util.List;

@Data
public class CreateContactRequestDTO {
    private String firstName;
    private String lastName;
    private String telephone;
    private String email;
}
