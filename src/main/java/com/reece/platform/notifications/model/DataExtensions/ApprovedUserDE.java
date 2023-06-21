package com.reece.platform.notifications.model.DataExtensions;

import com.reece.platform.notifications.model.DTO.UserDTO;

import java.util.HashMap;

public class ApprovedUserDE extends BaseDataExtension {
    public ApprovedUserDE(UserDTO approvedUser) {
        super.setTo(approvedUser.getEmail());
        HashMap<String, String> attr = new HashMap<>();
        attr.put("FirstName", approvedUser.getFirstName());
        attr.put("BranchPhoneNumber", approvedUser.getBranchInfo().getPhoneNumber());
        attr.put("BranchSupportEmail", approvedUser.getBranchInfo().getSupportEmail());
        super.setAttr(attr);
    }
}
