package com.reece.platform.eclipse.model.XML.ProductResponse.MassProductInquiryResponse.Product.PartIdentifiers;

import lombok.Data;

import javax.xml.bind.annotation.*;

@Data
@XmlType(propOrder = { "PrimaryUPC" })
@XmlAccessorType(XmlAccessType.FIELD)
public class Upc {

    @XmlAttribute(name = "PrimaryUPC")
    private String PrimaryUPC;

    @XmlValue
    private String upc;
}
