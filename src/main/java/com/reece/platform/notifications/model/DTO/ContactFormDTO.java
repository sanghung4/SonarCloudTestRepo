package com.reece.platform.notifications.model.DTO;

import lombok.Data;

@Data
public class ContactFormDTO {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String zip;
    private String topic;
    private String message;
}
