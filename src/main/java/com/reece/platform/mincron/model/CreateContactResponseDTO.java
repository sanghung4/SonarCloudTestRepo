package com.reece.platform.mincron.model;

import lombok.Data;

@Data
public class CreateContactResponseDTO {
    private String contactId;
    private String erpUsername;
    private String erpPassword;
}
