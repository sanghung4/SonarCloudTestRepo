package com.reece.platform.accounts.model.DTO;

import lombok.Data;

@Data
public class SFMCTokenRequestDTO {

    private String grant_type;
    private String client_id;
    private String client_secret;
    private String account_id;
}
