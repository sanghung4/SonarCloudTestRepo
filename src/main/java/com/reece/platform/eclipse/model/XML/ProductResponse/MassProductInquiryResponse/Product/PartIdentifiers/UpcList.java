package com.reece.platform.eclipse.model.XML.ProductResponse.MassProductInquiryResponse.Product.PartIdentifiers;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@Data
@XmlType(propOrder = { "" })
@XmlAccessorType(XmlAccessType.FIELD)
public class UpcList {
    @XmlElement(name = "UPC")
    private List<Upc> upcList;
}
