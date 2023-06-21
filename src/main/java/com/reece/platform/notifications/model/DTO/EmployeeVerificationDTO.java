package com.reece.platform.notifications.model.DTO;

import lombok.Data;

import java.util.UUID;

@Data
public class EmployeeVerificationDTO {
    private String email;
    private String firstName;
    private String LastName;
    private UUID verificationToken;
    private String brand;
    private String domain;
    private String inviteLink;

    public void generateInviteLink(String url, String domain) {
        this.inviteLink = String.format("https://%s.%s/verify?token=%s", domain, url, this.verificationToken);
    }

    public String getBaseUrl(String url) {
        return String.format("https://%s.%s", this.domain, url);
    }
}
