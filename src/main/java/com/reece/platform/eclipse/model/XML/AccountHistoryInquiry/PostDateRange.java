package com.reece.platform.eclipse.model.XML.AccountHistoryInquiry;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@Data
@XmlType(propOrder = {"StartDate", "EndDate"})
@XmlAccessorType(XmlAccessType.FIELD)
public class PostDateRange {
    @XmlElement(name = "StartDate")
    private StartDate StartDate;

    @XmlElement(name = "EndDate")
    private com.reece.platform.eclipse.model.XML.AccountHistoryInquiry.EndDate EndDate;
}
