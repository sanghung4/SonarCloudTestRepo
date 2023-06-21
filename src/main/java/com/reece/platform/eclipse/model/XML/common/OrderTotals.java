package com.reece.platform.eclipse.model.XML.common;


import com.reece.platform.eclipse.model.XML.common.DiscountWrapper;
import com.reece.platform.eclipse.model.XML.common.PaymentWrapper;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "subtotal", "tax", "federalExciseTax", "freight", "handling", "payment", "discount", "total"})
public class OrderTotals {

    @XmlElement(name = "Subtotal")
    private float subtotal;

    @XmlElement(name = "Tax")
    private float tax;

    @XmlElement(name = "FederalExciseTax")
    private float federalExciseTax;

    @XmlElement(name = "Freight")
    private float freight;

    @XmlElement(name = "Handling")
    private float handling;

    @XmlElement(name = "Payment")
    private PaymentWrapper payment;

    @XmlElement(name = "Discount")
    private DiscountWrapper discount;

    @XmlElement(name = "Total")
    private float total;
}
