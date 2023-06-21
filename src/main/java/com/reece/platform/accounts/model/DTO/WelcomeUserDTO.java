package com.reece.platform.accounts.model.DTO;

import com.reece.platform.accounts.model.entity.User;
import lombok.Data;

@Data
public class WelcomeUserDTO {
    private String email;
    private String firstName;
    private String brand;
    private String domain;
    private BranchContactInfoNotificationDTO branchInfo;

    public WelcomeUserDTO(BranchDTO branch, User user, String supportEmail) {
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.brand = branch.getBrand();
        this.domain = branch.getDomain();
        var branchInfo = new BranchContactInfoNotificationDTO();
        branchInfo.setSupportEmail(supportEmail);
        branchInfo.setPhoneNumber(branch.getPhone());
        this.branchInfo = branchInfo;
    }

    public WelcomeUserDTO() {}
}
