package com.reece.platform.eclipse.model.XML.SalesOrder;

import com.reece.platform.eclipse.model.XML.common.OrderTotals;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"orderHeader", "lineItemList", "creditCard", "orderTotals"})
public class SalesOrder {

    @XmlElement(name = "OrderHeader")
    private OrderHeader orderHeader;

    @XmlElement(name = "LineItemList")
    private LineItemsListWrapper lineItemList;

    @XmlElement(name = "CreditCard")
    private CreditCard creditCard;

    @XmlElement(name = "OrderTotals")
    private OrderTotals orderTotals;
}
