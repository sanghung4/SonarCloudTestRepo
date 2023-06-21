package com.reece.platform.eclipse.model.XML.OpenOrderResponse;


import com.reece.platform.eclipse.model.XML.common.*;
import lombok.*;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"AccountRegisterID", "OrderID", "InvoiceNumber", "OrderStatus", "PrintStatus",
        "CustomerPO", "CustomerReleaseNumber", "Description", "BranchID", "OrderDate", "PostDate", "ShipDate",
        "ShippingInformation", "Age", "Discount", "Amount", "Subtotal", "Balance", "Currency", "BidExpireDate"})
@Data
public class OpenOrderItem {

    @XmlElement(name = "AccountRegisterID")
    private String AccountRegisterID;

    @XmlElement(name = "OrderID")
    private String OrderID;

    @XmlElement(name = "InvoiceNumber")
    private String InvoiceNumber;

    @XmlElement(name = "OrderStatus")
    private OrderStatus OrderStatus;

    @XmlElement(name = "PrintStatus")
    private String PrintStatus;

    @XmlElement(name = "CustomerPO")
    private String CustomerPO;

    @XmlElement(name = "CustomerReleaseNumber")
    private String CustomerReleaseNumber;

    @XmlElement(name = "Description")
    private String Description;

    @XmlElement(name = "BranchID")
    private String BranchID;

    @XmlElement(name = "OrderDate")
    private OrderDate OrderDate;

    @XmlElement(name = "PostDate")
    private com.reece.platform.eclipse.model.XML.AccountHistoryResponse.PostDate PostDate;

    @XmlElement(name = "ShipDate")
    private ShipDate ShipDate;

    @XmlElement(name = "ShippingInformation")
    private ShippingInformation ShippingInformation;

    @XmlElement(name = "Age")
    private String Age;

    @XmlElement(name = "Discount")
    private DiscountWrapper Discount;

    @XmlElement(name = "Amount")
    private String Amount;

    @XmlElement(name = "Subtotal")
    private String Subtotal;

    @XmlElement(name = "Balance")
    private String Balance;

    @XmlElement(name = "Currency")
    private String Currency;

    @XmlElement(name = "BidExpireDate")
    private BidExpireDate BidExpireDate;
}
