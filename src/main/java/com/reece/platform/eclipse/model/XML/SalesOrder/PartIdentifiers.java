package com.reece.platform.eclipse.model.XML.SalesOrder;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "eclipsePartNumber", "customerPartNumberList", "productKeywords", "description" })
public class PartIdentifiers {

    @XmlElement(name = "EclipsePartNumber")
    private int eclipsePartNumber;

    @XmlElement(name = "CustomerPartNumberList")
    private CustomerPartNumberList customerPartNumberList;

    @XmlElement(name = "ProductKeywords")
    private String productKeywords;

    @XmlElement(name = "Description")
    private String description;
}