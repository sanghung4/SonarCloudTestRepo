package com.reece.platform.eclipse.model.DTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class LenderDTO extends ContactDTO {
    private String loanNumber;
    private String lenderName;
}
