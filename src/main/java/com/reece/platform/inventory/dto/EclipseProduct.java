package com.reece.platform.inventory.dto;

import com.reece.platform.inventory.model.CountItem;
import com.reece.platform.inventory.model.CountLocationItemStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.val;

import java.util.Objects;

@Data
@NoArgsConstructor
public class EclipseProduct {
    private String batchNumber;
    private String batchDescription;
    private String batchDate;
    private String batchLockDate;
    private String createdById;
    private String createdByName;
    private String recordId;
    private String countId;
    private String branch;
    private String location;
    private String productId;
    private String productDescription1;
    private String productDescription2;
    private String unitOfMeasure;
    private String currentCount;
    private String countDate;
    private String recount1;
    private String recount2;
    private String recount3;
    private String onHand;
    private String averageCost;

}
