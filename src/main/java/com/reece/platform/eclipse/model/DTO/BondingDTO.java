package com.reece.platform.eclipse.model.DTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BondingDTO extends ContactDTO {
    private String bondNumber;
    private String suretyName;
}
