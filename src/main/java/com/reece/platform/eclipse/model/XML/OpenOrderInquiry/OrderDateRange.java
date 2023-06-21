package com.reece.platform.eclipse.model.XML.OpenOrderInquiry;

import lombok.*;

import javax.xml.bind.annotation.*;

@Data
@XmlType(propOrder = {"StartDate", "EndDate"})
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderDateRange {
    @XmlElement(name = "StartDate")
    private com.reece.platform.eclipse.model.XML.AccountHistoryInquiry.StartDate StartDate;

    @XmlElement(name = "EndDate")
    private com.reece.platform.eclipse.model.XML.AccountHistoryInquiry.EndDate EndDate;
}
