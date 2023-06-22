package com.reece.platform.mincron.model;

import lombok.Data;

@Data
public class EditContactRequestDTO {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String erpUserPassword;
}
