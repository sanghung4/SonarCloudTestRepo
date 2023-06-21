package com.reece.platform.accounts.model.DTO;

import lombok.Data;

import java.util.UUID;

@Data
public class ApproveUser {
    private String userId;
    private String userRoleId;
    private UUID approverId;
}
