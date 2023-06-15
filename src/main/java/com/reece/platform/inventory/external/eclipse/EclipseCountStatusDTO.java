package com.reece.platform.inventory.external.eclipse;

import lombok.Data;

@Data
public class EclipseCountStatusDTO {

    private String branchNum;
    private String countId;
    private Integer numItems;
    private String countDate;
}
