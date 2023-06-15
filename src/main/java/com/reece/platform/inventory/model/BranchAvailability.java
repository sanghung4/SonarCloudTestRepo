package com.reece.platform.inventory.model;

import com.reece.platform.inventory.model.EclipseBranch;
import lombok.Data;

@Data
public class BranchAvailability {
    private EclipseBranch branch;
    private NowQuantity nowQuantity;
    private EarliestMoreDate earliestMoreDate;
    private EarliestMoreQuantity earliestMoreQuantity;
    private PlentyDate plentyDate;
    private StockQuantityOnOrder stockQuantityOnOrder;
    private TaggedQuantityOnOrder taggedQuantityOnOrder;
    private ProjectedInventoryLevel projectedInventoryLevel;
}