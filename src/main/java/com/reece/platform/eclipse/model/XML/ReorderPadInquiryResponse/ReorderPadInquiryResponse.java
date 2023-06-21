package com.reece.platform.eclipse.model.XML.ReorderPadInquiryResponse;

import com.reece.platform.eclipse.model.XML.common.StatusResult;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"sessionId", "reorderPadList", "statusResult"})
public class ReorderPadInquiryResponse {

    @XmlElement(name = "SessionID")
    private String sessionId;

    @XmlElement(name = "ReorderPadList")
    private ReorderPadList reorderPadList;

    @XmlElement(name = "StatusResult")
    private StatusResult statusResult;
}
