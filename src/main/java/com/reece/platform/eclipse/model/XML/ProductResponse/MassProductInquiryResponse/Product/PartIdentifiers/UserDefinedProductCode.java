package com.reece.platform.eclipse.model.XML.ProductResponse.MassProductInquiryResponse.Product.PartIdentifiers;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@Data
@XmlType(propOrder = { "userDefinedProductCode1", "userDefinedProductCode2", "userDefinedProductCode3", "userDefinedProductCode4", "userDefinedProductCode5", "userDefinedProductCode6", "userDefinedProductCode7", "userDefinedProductCode8", "userDefinedProductCode9" })
@XmlAccessorType(XmlAccessType.FIELD)
public class UserDefinedProductCode {

    @XmlElement(name = "UserDefinedProductCode1")
    private String userDefinedProductCode1;

    @XmlElement(name = "UserDefinedProductCode2")
    private String userDefinedProductCode2;

    @XmlElement(name = "UserDefinedProductCode3")
    private String userDefinedProductCode3;

    @XmlElement(name = "UserDefinedProductCode4")
    private String userDefinedProductCode4;

    @XmlElement(name = "UserDefinedProductCode5")
    private String userDefinedProductCode5;

    @XmlElement(name = "UserDefinedProductCode6")
    private String userDefinedProductCode6;

    @XmlElement(name = "UserDefinedProductCode7")
    private String userDefinedProductCode7;

    @XmlElement(name = "UserDefinedProductCode8")
    private String userDefinedProductCode8;

    @XmlElement(name = "UserDefinedProductCode9")
    private String userDefinedProductCode9;
}
