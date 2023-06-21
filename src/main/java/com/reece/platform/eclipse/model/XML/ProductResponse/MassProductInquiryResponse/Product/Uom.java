package com.reece.platform.eclipse.model.XML.ProductResponse.MassProductInquiryResponse.Product;

import lombok.Data;

import javax.xml.bind.annotation.*;

@Data
@XmlType(propOrder = { "uom" })
@XmlAccessorType(XmlAccessType.FIELD)
public class Uom {

    @XmlAttribute(name = "UMQT")
    private String umqt;

    @XmlElement(name = "UOM")
    private String uom;

}
