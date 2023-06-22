package com.reece.platform.mincron.model;

import lombok.Value;

@Value
public class UpdateCountDTO {
    String branchNum;
    String countId;
    String locationId;
    String tagNum;
    int countedQty;
}
