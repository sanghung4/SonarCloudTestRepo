package com.reece.platform.eclipse.model.XML.OpenOrderResponse;

import lombok.*;

import javax.xml.bind.annotation.*;

@Data
@XmlRootElement(name = "IDMS-XML")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "OpenOrderInquiryResponse"})
public class OpenOrderResponse {

    public OpenOrderResponse() {}

    @XmlElement(name = "OpenOrderInquiryResponse")
    private OpenOrderInquiryResponse OpenOrderInquiryResponse;

    @Override
    public String toString()
    {
        return "ClassPojo [OpenOrderInquiryResponse = " + OpenOrderInquiryResponse + "]";
    }
}
