package com.reece.platform.accounts.model.DTO;

import lombok.*;

import java.util.*;

@Data
public class AccountUserLoginUpdatedNotificationDTO {
    private List<String> emails;
    private String firstName;
    private String lastName;
    private String supportEmail;
    private String brand;
    private String domain;
}
