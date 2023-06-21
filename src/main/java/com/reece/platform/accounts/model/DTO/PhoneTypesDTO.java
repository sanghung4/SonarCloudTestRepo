package com.reece.platform.accounts.model.DTO;

import com.reece.platform.accounts.model.enums.PhoneTypeEnum;
import lombok.Data;

import java.util.UUID;

@Data
public class PhoneTypesDTO {
    private String id;
    private String name;
    private String displayName;

    public PhoneTypesDTO(PhoneTypeEnum phoneType) {
        id = phoneType.getId();
        name = phoneType.name();
        displayName = phoneType.getDisplayName();
    }
}
