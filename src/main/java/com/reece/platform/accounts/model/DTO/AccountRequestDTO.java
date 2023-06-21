package com.reece.platform.accounts.model.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.UUID;

@Data
public class AccountRequestDTO {
    private UUID id;

    @JsonProperty("isEmployee")
    private boolean isEmployee;

    @JsonProperty("isVerified")
    private boolean isVerified;
}