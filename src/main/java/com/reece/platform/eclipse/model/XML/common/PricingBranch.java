package com.reece.platform.eclipse.model.XML.common;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@Data
@XmlType(propOrder = { "branch" })
@XmlAccessorType(XmlAccessType.FIELD)
public class PricingBranch {

    @XmlElement(name = "Branch")
    private Branch branch;
}
