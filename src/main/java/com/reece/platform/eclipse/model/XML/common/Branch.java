package com.reece.platform.eclipse.model.XML.common;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@Data
@XmlType(propOrder = { "branchId", "branchName", "address"})
@XmlAccessorType(XmlAccessType.FIELD)
public class Branch {
    @XmlElement(name = "BranchID")
    private String branchId;

    @XmlElement(name = "BranchName")
    private String branchName;

    @XmlElement(name = "Address")
    private Address address;
}