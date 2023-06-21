package com.reece.platform.notifications.model.DTO;

import lombok.Data;

@Data
public class RejectedUserDTO extends UserDTO {
    private String rejectionReason;
}
