package com.reece.platform.accounts.model.DTO;

import lombok.Data;

import java.util.UUID;

/**
 * Representation of all user creation data
 */
@Data
public class CreateUser {
    private String id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String phoneType;
    private AccountInfoDTO accountInfo;
    private String userContactTitle;
    private String preferredLocationId;
    private String customerCategory;
    private UUID approverId;
}
