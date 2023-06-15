package com.reece.platform.inventory.external.mincron;

import lombok.Value;

@Value
public class MincronUpdateCountDTO {
    String branchNum;
    String countId;
    String locationId;
    String tagNum;
    int countedQty;
}
