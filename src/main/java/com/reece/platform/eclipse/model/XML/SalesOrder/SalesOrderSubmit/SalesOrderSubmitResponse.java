package com.reece.platform.eclipse.model.XML.SalesOrder.SalesOrderSubmit;

import com.reece.platform.eclipse.model.XML.SalesOrderResponse.SalesOrder;
import com.reece.platform.eclipse.model.XML.common.StatusResult;
import lombok.Data;

import javax.xml.bind.annotation.*;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "salesOrder", "statusResult" })
public class SalesOrderSubmitResponse {

    @XmlElement(name = "SalesOrder")
    private SalesOrder salesOrder;

    @XmlElement(name = "StatusResult")
    private StatusResult statusResult;
}
