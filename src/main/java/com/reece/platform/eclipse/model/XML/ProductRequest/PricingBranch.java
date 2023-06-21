package com.reece.platform.eclipse.model.XML.ProductRequest;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "branchId" })
public class PricingBranch {

    @XmlElement(name = "BranchID")
    private String branchId;

}
