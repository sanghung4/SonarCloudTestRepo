package com.reece.platform.eclipse.model.XML.ProductResponse.MassProductInquiryResponse.Product.Pricing;

import com.reece.platform.eclipse.model.XML.ProductResponse.MassProductInquiryResponse.Product.AvailabilityList.Quantity;
import com.reece.platform.eclipse.model.XML.common.PricingBranch;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@Data
@XmlType(propOrder = { "eclipsePartNumber", "entityId", "pricingBranch", "currency", "quantity", "listPrice", "customerPrice", "extendedPrice" })
@XmlAccessorType(XmlAccessType.FIELD)
public class Pricing {

    @XmlElement(name = "EclipsePartNumber")
    private String eclipsePartNumber;

    @XmlElement(name = "EntityID")
    private String entityId;

    @XmlElement(name = "PricingBranch")
    private PricingBranch pricingBranch;

    @XmlElement(name = "Currency")
    private String currency;

    @XmlElement(name = "Quantity")
    private Quantity quantity;

    @XmlElement(name = "ListPrice")
    private String listPrice;

    @XmlElement(name = "CustomerPrice")
    private String customerPrice;

    @XmlElement(name = "ExtendedPrice")
    private String extendedPrice;
}
