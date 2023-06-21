package com.reece.platform.eclipse.model.XML.AccountHistoryInquiry;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@Data
@XmlType(propOrder = { "Date" })
@XmlAccessorType(XmlAccessType.FIELD)
public class StartDate {
    @XmlElement(name = "Date")
    private String Date;
}
