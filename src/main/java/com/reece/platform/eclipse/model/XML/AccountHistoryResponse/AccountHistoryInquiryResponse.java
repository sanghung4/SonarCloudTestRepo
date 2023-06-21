package com.reece.platform.eclipse.model.XML.AccountHistoryResponse;

import com.reece.platform.eclipse.model.XML.common.StatusResult;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"SessionID", "AccountHistoryItemList", "StatusResult"})
public class AccountHistoryInquiryResponse {

    @XmlElement(name = "SessionID")
    private String SessionID;

    @XmlElement(name = "AccountHistoryItemList")
    private AccountHistoryItemList AccountHistoryItemList;

    @XmlElement(name = "StatusResult")
    private StatusResult StatusResult;

    @Override
    public String toString()
    {
        return "ClassPojo [StatusResult = " + StatusResult + "AccountHistoryItemList = " + AccountHistoryItemList +
                "SessionID = " + SessionID + "]";
    }
}