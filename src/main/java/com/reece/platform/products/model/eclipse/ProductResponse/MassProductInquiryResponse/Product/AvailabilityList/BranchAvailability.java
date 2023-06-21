package com.reece.platform.products.model.eclipse.ProductResponse.MassProductInquiryResponse.Product.AvailabilityList;

import com.reece.platform.products.model.eclipse.common.Branch;
import lombok.Data;

@Data
public class BranchAvailability {

    private Branch branch;

    private NowQuantity nowQuantity;

    private EarliestMoreDate earliestMoreDate;

    private EarliestMoreQuantity earliestMoreQuantity;

    private PlentyDate plentyDate;

    private StockQuantityOnOrder stockQuantityOnOrder;

    private TaggedQuantityOnOrder taggedQuantityOnOrder;

    private ProjectedInventoryLevel projectedInventoryLevel;
}
