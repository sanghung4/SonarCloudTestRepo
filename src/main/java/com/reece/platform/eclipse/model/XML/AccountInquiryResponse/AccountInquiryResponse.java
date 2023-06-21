package com.reece.platform.eclipse.model.XML.AccountInquiryResponse;

import com.reece.platform.eclipse.model.XML.common.StatusResult;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"sessionId", "accountInquirySummary", "accountInquiryItemList", "statusResult"})
public class AccountInquiryResponse {

    @XmlElement(name = "SessionID")
    private String sessionId;

    @XmlElement(name = "AccountInquirySummary")
    private AccountInquirySummary accountInquirySummary;

    @XmlElement(name = "AccountInquiryItemList")
    private AccountInquiryItemList accountInquiryItemList;

    @XmlElement(name = "StatusResult")
    private StatusResult statusResult;
}
