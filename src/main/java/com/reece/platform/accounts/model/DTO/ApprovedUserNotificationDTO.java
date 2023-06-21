package com.reece.platform.accounts.model.DTO;

import lombok.Data;

@Data
public class ApprovedUserNotificationDTO {
    private String email;
    private String firstName;
    private String brand;
    private String domain;
    private BranchContactInfoNotificationDTO branchInfo;
}
