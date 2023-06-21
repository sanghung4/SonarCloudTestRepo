package com.reece.platform.eclipse.model.XML.common;

import com.reece.platform.eclipse.model.XML.ProductResponse.MassProductInquiryResponse.Product.PartIdentifiers.UpcList;
import com.reece.platform.eclipse.model.XML.ProductResponse.MassProductInquiryResponse.Product.PartIdentifiers.UserDefinedProductCode;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@Data
@XmlType(propOrder = { "eclipsePartNumber", "upcList", "catalogNumber", "productKeywords", "description", "userDefinedProductCodeList" })
@XmlAccessorType(XmlAccessType.FIELD)
public class PartIdentifiers {

    @XmlElement(name = "EclipsePartNumber")
    private String eclipsePartNumber;

    @XmlElement(name = "UPCList")
    private UpcList upcList;

    @XmlElement(name = "CatalogNumber")
    private String catalogNumber;

    @XmlElement(name = "ProductKeywords")
    private String productKeywords;

    @XmlElement(name = "Description")
    private String description;

    @XmlElement(name = "UserDefinedProductCodes")
    private UserDefinedProductCode userDefinedProductCodeList;
}
