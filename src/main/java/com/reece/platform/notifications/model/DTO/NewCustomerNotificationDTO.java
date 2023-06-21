package com.reece.platform.notifications.model.DTO;

import lombok.Data;

import java.util.List;

@Data
public class NewCustomerNotificationDTO {
    private String email;
    private String brand;
    private String domain;
    private String managerFirstName;
    private String requestFirstName;
    private String requestLastName;
    private String billToName;
    private String requestEmail;
    private List<UserDTO> accountAdmins;
    private boolean isExistingEcommAccount;
    private String phoneNumber;
    private String accountNumber;
}
