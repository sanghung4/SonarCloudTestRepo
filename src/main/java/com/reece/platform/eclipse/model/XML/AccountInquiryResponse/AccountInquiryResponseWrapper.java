package com.reece.platform.eclipse.model.XML.AccountInquiryResponse;

import lombok.Data;

import javax.xml.bind.annotation.*;

@Data
@XmlRootElement(name="IDMS-XML")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"accountInquiryResponse"})
public class AccountInquiryResponseWrapper {

    @XmlElement(name = "AccountInquiryResponse")
    private AccountInquiryResponse accountInquiryResponse;
}
