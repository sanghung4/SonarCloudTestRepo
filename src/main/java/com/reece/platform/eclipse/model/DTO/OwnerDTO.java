package com.reece.platform.eclipse.model.DTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class OwnerDTO extends ContactDTO {
    private String ownerName;
}
