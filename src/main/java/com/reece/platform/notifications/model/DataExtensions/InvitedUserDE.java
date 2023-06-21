package com.reece.platform.notifications.model.DataExtensions;

import com.reece.platform.notifications.model.DTO.InviteUserDTO;

import java.util.HashMap;

public class InvitedUserDE extends BaseDataExtension {
    public InvitedUserDE(InviteUserDTO invitedUser, String url) {
        invitedUser.generateInviteLink(url);
        super.setTo(invitedUser.getEmail());
        HashMap<String, String> attr = new HashMap<>();
        attr.put("FirstName", invitedUser.getFirstName());
        attr.put("InviteLink", invitedUser.getInviteLink());
        super.setAttr(attr);
    }
}
