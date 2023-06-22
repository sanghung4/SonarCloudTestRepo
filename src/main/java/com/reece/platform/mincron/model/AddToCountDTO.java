package com.reece.platform.mincron.model;

import lombok.Value;

@Value
public class AddToCountDTO {
    String branchNum;
    String countId;
    String locationId;
    String prodNum;
    int countedQty;
}
