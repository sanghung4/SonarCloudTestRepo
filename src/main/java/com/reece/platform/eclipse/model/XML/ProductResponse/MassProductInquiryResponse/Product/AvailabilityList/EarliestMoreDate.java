package com.reece.platform.eclipse.model.XML.ProductResponse.MassProductInquiryResponse.Product.AvailabilityList;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@Data
@XmlType(propOrder = { "date" })
@XmlAccessorType(XmlAccessType.FIELD)
public class EarliestMoreDate {
    @XmlElement(name = "Date")
    private String date;
}
