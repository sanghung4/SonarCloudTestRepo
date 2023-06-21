package com.reece.platform.eclipse.model.XML.MassSalesOrderInquiryResponse;

import com.reece.platform.eclipse.model.XML.SalesOrderResponse.SalesOrder;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"salesOrderList"} )
public class SalesOrderList {
    @XmlElement(name = "SalesOrder")
    private List<SalesOrder> salesOrderList;
}
