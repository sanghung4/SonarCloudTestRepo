package com.reece.platform.eclipse.model.XML.ProductResponse.MassProductInquiryResponse.Product;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@Data
@XmlType(propOrder = { "uom" })
@XmlAccessorType(XmlAccessType.FIELD)
public class PricingUOM {
    // attrs
    @XmlElement(name = "UOM")
    private String uom;
}
