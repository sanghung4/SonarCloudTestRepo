package com.reece.platform.accounts.model.DTO;

import lombok.Data;

import java.util.UUID;

@Data
public class InviteUserNotificationDTO {
    private String email;
    private String firstName;
    private UUID inviteId;
    private String brand;
    private String domain;
}
