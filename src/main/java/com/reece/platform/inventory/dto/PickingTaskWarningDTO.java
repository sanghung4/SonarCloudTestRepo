package com.reece.platform.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PickingTaskWarningDTO {
    private String taskOrderId;
    private Integer taskOrderGid;
    private String warningInfo;
}
