package com.reece.platform.eclipse.model.XML.ProductResponse.MassProductInquiryResponse.Product.AvailabilityList;

import com.reece.platform.eclipse.model.XML.common.Branch;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@Data
@XmlType(propOrder = { "branch", "nowQuantity", "earliestMoreDate", "earliestMoreQuantity", "plentyDate", "stockQuantityOnOrder", "taggedQuantityOnOrder", "projectedInventoryLevel" })
@XmlAccessorType(XmlAccessType.FIELD)
public class BranchAvailability {
    @XmlElement(name = "Branch")
    private Branch branch;

    @XmlElement(name = "NowQuantity")
    private NowQuantity nowQuantity;

    @XmlElement(name = "EarliestMoreDate")
    private EarliestMoreDate earliestMoreDate;

    @XmlElement(name = "EarliestMoreQuantity")
    private EarliestMoreQuantity earliestMoreQuantity;

    @XmlElement(name = "PlentyDate")
    private PlentyDate plentyDate;

    @XmlElement(name = "StockQuantityOnOrder")
    private StockQuantityOnOrder stockQuantityOnOrder;

    @XmlElement(name = "TaggedQuantityOnOrder")
    private TaggedQuantityOnOrder taggedQuantityOnOrder;

    @XmlElement(name = "ProjectedInventoryLevel")
    private ProjectedInventoryLevel projectedInventoryLevel;
}
