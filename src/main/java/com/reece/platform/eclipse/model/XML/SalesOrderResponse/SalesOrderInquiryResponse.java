
package com.reece.platform.eclipse.model.XML.SalesOrderResponse;

import com.reece.platform.eclipse.model.XML.common.StatusResult;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {
    "sessionID",
    "salesOrder",
    "statusResult"
})
public class SalesOrderInquiryResponse {

    @XmlElement(name = "SessionID")
    private String sessionID;
    
    @XmlElement(name = "SalesOrder")
    private SalesOrder salesOrder;
    
    @XmlElement(name = "StatusResult")
    private StatusResult statusResult;
}
