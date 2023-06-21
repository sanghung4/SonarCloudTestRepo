package com.reece.platform.eclipse.model.XML.ReorderPadInquiryResponse;


import com.reece.platform.eclipse.model.XML.ProductResponse.MassProductInquiryResponse.Product.AvailabilityList.Quantity;
import com.reece.platform.eclipse.model.XML.ProductResponse.MassProductInquiryResponse.Product.Product;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"product", "quantity", "lastOrder"})
public class ReorderPadItem {

    @XmlElement(name = "Product")
    private Product product;

    @XmlElement(name = "Quantity")
    private Quantity quantity;

    @XmlElement(name = "LastOrder")
    private LastOrder lastOrder;
}
