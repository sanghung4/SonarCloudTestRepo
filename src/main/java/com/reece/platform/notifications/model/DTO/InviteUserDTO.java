package com.reece.platform.notifications.model.DTO;

import lombok.Data;

import java.util.UUID;

@Data
public class InviteUserDTO {
    private String email;
    private String firstName;
    private UUID inviteId;
    private String inviteLink;
    private String brand;
    private String domain;

    public void generateInviteLink(String url) {
        this.inviteLink = String.format("%s/register?inviteId=%s", url, this.inviteId);
    }
}
