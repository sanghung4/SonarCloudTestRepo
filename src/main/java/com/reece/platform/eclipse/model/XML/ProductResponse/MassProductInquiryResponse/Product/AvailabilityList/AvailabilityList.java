package com.reece.platform.eclipse.model.XML.ProductResponse.MassProductInquiryResponse.Product.AvailabilityList;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@Data
@XmlType(propOrder = { "branchAvailabilityList" })
@XmlAccessorType(XmlAccessType.FIELD)
public class AvailabilityList {
    @XmlElement(name = "BranchAvailability")
    private List<BranchAvailability> branchAvailabilityList;
}
