package com.reece.platform.eclipse.model.XML.MassSalesOrderInquiryResponse;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.*;

@Data
@XmlRootElement(name = "IDMS-XML")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"massSalesOrderInquiryResponse"})
@NoArgsConstructor
public class MassSalesOrderResponse {

    @XmlElement(name = "MassSalesOrderInquiryResponse")
    private MassSalesOrderInquiryResponse massSalesOrderInquiryResponse;

    @Override
    public String toString()
    {
        return "ClassPojo [MassSalesOrderInquiryResponse = " + massSalesOrderInquiryResponse + "]";
    }
}
