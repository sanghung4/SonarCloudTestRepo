package com.reece.platform.inventory.external.mincron;

import lombok.Value;

@Value
public class MincronAddToCountDTO {
    String branchNum;
    String countId;
    String locationId;
    String prodNum;
    int countedQty;
}
