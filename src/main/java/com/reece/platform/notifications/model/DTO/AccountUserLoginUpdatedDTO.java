package com.reece.platform.notifications.model.DTO;

import lombok.Data;

import java.util.*;

@Data
public class AccountUserLoginUpdatedDTO {
    private List<String> emails;
    private String firstName;
    private String lastName;
    private String supportEmail;
    private String brand;
    private String domain;
}
