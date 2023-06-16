package com.reece.platform.eclipse.dto.inventory;

import lombok.Data;

@Data
public class ValidateCountDTO extends KourierErrorDTO {

    private String branch;
    private String countId;
    private String branchName;
}
