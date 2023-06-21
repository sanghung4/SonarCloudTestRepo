package com.reece.platform.eclipse.model.XML.AccountInquiry;

import lombok.Data;

import javax.xml.bind.annotation.*;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "IDMS-XML")
@XmlType(propOrder = {"accountInquiry"})
public class AccountInquiryRequest {

    @XmlElement(name = "AccountInquiry")
    private AccountInquiry accountInquiry;
}
