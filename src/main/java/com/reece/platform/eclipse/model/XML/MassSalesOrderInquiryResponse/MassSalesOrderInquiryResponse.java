
package com.reece.platform.eclipse.model.XML.MassSalesOrderInquiryResponse;

import com.reece.platform.eclipse.model.XML.common.StatusResult;
import lombok.Data;

import javax.xml.bind.annotation.*;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {
    "sessionID",
    "salesOrderList",
    "statusResult"
})
public class MassSalesOrderInquiryResponse {

    @XmlElement(name = "SessionID")
    private String sessionID;
    
    @XmlElement(name = "SalesOrderList")
    private SalesOrderList salesOrderList;
    
    @XmlElement(name = "StatusResult")
    private StatusResult statusResult;
}
