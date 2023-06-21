package com.reece.platform.notifications.model.DTO;

import lombok.Data;

import java.util.List;

@Data
public class CustomerAccountDeletedDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String accountNumber;
    private String companyName;
    private List<String> adminEmails;
    private String branchManagerEmail;
    private String brand;
    private String domain;
}
