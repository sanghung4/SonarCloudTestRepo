package com.reece.platform.eclipse.model.XML.AccountHistoryResponse;

import lombok.Data;

import javax.xml.bind.annotation.*;

@Data
@XmlRootElement(name = "IDMS-XML")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "AccountHistoryInquiryResponse"})
public class AccountHistoryResponse {

    public AccountHistoryResponse() {}

    @XmlElement(name = "AccountHistoryInquiryResponse")
    private AccountHistoryInquiryResponse AccountHistoryInquiryResponse;

    @Override
    public String toString()
    {
        return "ClassPojo [AccountHistoryInquiryResponse = " + AccountHistoryInquiryResponse + "]";
    }
}