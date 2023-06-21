package com.reece.platform.accounts.model.DTO;

import lombok.Data;

@Data
public class RejectedUserNotificationDTO extends ApprovedUserNotificationDTO {
    private String rejectionReason;
}
