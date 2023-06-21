package com.reece.platform.eclipse.model.XML.common;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"Amount", "Percentage", "Date"})
public class Discount {

    @XmlElement(name = "Amount")
    private String Amount;

    @XmlElement(name = "Percentage")
    private String Percentage;

    @XmlElement(name = "Date")
    private String Date;

}
