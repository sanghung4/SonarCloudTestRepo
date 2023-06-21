package com.reece.platform.eclipse.model.XML.ProductResponse.MassProductInquiryResponse.Product.AvailabilityList;

import lombok.Data;

import javax.xml.bind.annotation.*;

@Data
@XmlType(propOrder = { "uom", "umqt" })
@XmlAccessorType(XmlAccessType.FIELD)
public class Quantity {
    @XmlAttribute(name = "UOM")
    private String uom;

    @XmlAttribute(name = "UMQT")
    private String umqt;

    @XmlValue
    private String quantity;
}
