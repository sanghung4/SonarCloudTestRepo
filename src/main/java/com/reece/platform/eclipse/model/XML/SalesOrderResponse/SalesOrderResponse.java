package com.reece.platform.eclipse.model.XML.SalesOrderResponse;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.*;

@Data
@XmlRootElement(name = "IDMS-XML")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"salesOrderInquiryResponse"})
@NoArgsConstructor
public class SalesOrderResponse {

    @XmlElement(name = "SalesOrderInquiryResponse")
    private SalesOrderInquiryResponse salesOrderInquiryResponse;

    @Override
    public String toString()
    {
        return "ClassPojo [SalesOrderInquiryResponse = " + salesOrderInquiryResponse + "]";
    }
}
