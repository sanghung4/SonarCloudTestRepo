package com.reece.platform.eclipse.model.XML.ProductResponse.MassProductInquiryResponse.Product.RichContentList;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@Data
@XmlType(propOrder = { "name", "id", "value" })
@XmlAccessorType(XmlAccessType.FIELD)
public class RichContentItem {

    @XmlElement(name = "Name")
    private String name;

    @XmlElement(name = "ID")
    private String id;

    @XmlElement(name = "Value")
    private String value;
}
