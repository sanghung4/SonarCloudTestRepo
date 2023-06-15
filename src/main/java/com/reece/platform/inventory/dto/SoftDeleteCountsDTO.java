package com.reece.platform.inventory.dto;

import com.reece.platform.inventory.model.ERPSystemName;
import lombok.Value;

@Value
public class SoftDeleteCountsDTO {
    String startDate;
    String endDate;
}
