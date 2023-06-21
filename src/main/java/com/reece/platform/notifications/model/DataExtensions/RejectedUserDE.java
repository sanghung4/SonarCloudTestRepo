package com.reece.platform.notifications.model.DataExtensions;

import com.reece.platform.notifications.model.DTO.RejectedUserDTO;

import java.util.HashMap;

public class RejectedUserDE extends BaseDataExtension {
    public RejectedUserDE(RejectedUserDTO rejectedUser) {
        super.setTo(rejectedUser.getEmail());
        HashMap<String, String> attr = new HashMap<>();
        attr.put("FirstName", rejectedUser.getFirstName());
        attr.put("BranchPhoneNumber", rejectedUser.getBranchInfo().getPhoneNumber());
        attr.put("BranchSupportEmail", rejectedUser.getBranchInfo().getSupportEmail());
        attr.put("RejectionReason", rejectedUser.getRejectionReason());
        super.setAttr(attr);
    }
}
