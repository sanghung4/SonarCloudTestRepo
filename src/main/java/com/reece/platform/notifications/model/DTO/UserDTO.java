package com.reece.platform.notifications.model.DTO;

import lombok.Data;

@Data
public class UserDTO {
    private String email;
    private String firstName;
    private String brand;
    private String domain;
    private BranchContactInformationDTO branchInfo;
}
