package com.reece.platform.eclipse.dto.inventory;

import lombok.Data;

@Data
public class KourierCountInfoDTO {

    private String brid;
    private String cycle;
    private Integer numItems;
    private String readyDateTimeStamp;
}
