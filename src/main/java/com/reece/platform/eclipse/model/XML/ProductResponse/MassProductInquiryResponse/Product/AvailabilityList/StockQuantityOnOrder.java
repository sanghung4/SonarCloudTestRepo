package com.reece.platform.eclipse.model.XML.ProductResponse.MassProductInquiryResponse.Product.AvailabilityList;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@Data
@XmlType(propOrder = { "quantity" })
@XmlAccessorType(XmlAccessType.FIELD)
public class StockQuantityOnOrder {
    // attributes
    private Quantity quantity;
}
