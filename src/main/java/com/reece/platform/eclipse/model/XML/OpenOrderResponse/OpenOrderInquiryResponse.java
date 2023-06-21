package com.reece.platform.eclipse.model.XML.OpenOrderResponse;

import com.reece.platform.eclipse.model.XML.common.*;
import lombok.*;

import javax.xml.bind.annotation.*;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"SessionID", "OpenOrderItemList", "StatusResult"})
public class OpenOrderInquiryResponse {

    @XmlElement(name = "SessionID")
    private String SessionID;

    @XmlElement(name = "OpenOrderItemList")
    private OpenOrderItemList OpenOrderItemList;

    @XmlElement(name = "StatusResult")
    private StatusResult StatusResult;

    @Override
    public String toString()
    {
        return "ClassPojo [StatusResult = " + StatusResult + "OpenOrderItemList = " + OpenOrderItemList +
                "SessionID = " + SessionID + "]";
    }
}
