package com.reece.platform.eclipse.model.XML.SalesOrderResponse;

import com.reece.platform.eclipse.model.XML.SalesOrder.CreditCard;
import com.reece.platform.eclipse.model.XML.SalesOrder.OrderHeader;
import com.reece.platform.eclipse.model.XML.common.OrderTotals;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "orderHeader", "lineItemList", "orderTotals" })
public class SalesOrder {

    @XmlElement(name = "OrderHeader")
    private OrderHeader orderHeader;

    @XmlElement(name = "LineItemList")
    private LineItemList lineItemList;

    @XmlElement(name = "OrderTotals")
    private OrderTotals orderTotals;
}
