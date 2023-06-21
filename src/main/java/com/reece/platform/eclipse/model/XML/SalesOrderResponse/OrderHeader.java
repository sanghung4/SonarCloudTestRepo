
package com.reece.platform.eclipse.model.XML.SalesOrderResponse;

import com.reece.platform.eclipse.model.XML.common.*;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {
    "orderID",
    "invoiceNumber",
    "orderStatus",
    "printStatus",
    "orderType",
    "customerPO",
    "customerReleaseNumber",
    "pricingBranch",
    "shippingBranch",
    "orderDate",
    "requiredDate",
    "shipDate",
    "invoiceDueDate",
    "lastUpdate",
    "billTo",
    "shipTo",
    "shippingInformation",
    "outsideSalesPerson",
    "insideSalesPerson",
    "writtenBy",
    "orderedBy",
    "telephone",
    "emailAddress",
    "currency",
    "paymentTerms",
    "progressBilling",
    "internalNotes",
    "salesSource",
    "orderSource"
})
public class OrderHeader {

    @XmlElement(name = "OrderID")
    private String orderID;

    @XmlElement(name = "InvoiceNumber")
    private String invoiceNumber;

    @XmlElement(name = "OrderStatus")
    private OrderStatus orderStatus;

    @XmlElement(name = "PrintStatus")
    private String printStatus;

    @XmlElement(name = "OrderType")
    private String orderType;

    @XmlElement(name = "CustomerPO")
    private String customerPO;

    @XmlElement(name = "CustomerReleaseNumber")
    private String customerReleaseNumber;

    @XmlElement(name = "PricingBranch")
    private PricingBranch pricingBranch;

    @XmlElement(name = "ShippingBranch")
    private ShippingBranch shippingBranch;

    @XmlElement(name = "OrderDate")
    private DateWrapper orderDate;

    @XmlElement(name = "RequiredDate")
    private DateWrapper requiredDate;

    @XmlElement(name = "ShipDate")
    private DateWrapper shipDate;

    @XmlElement(name = "InvoiceDueDate")
    private DateWrapper invoiceDueDate;

    @XmlElement(name = "LastUpdate")
    private DateTimeWrapper lastUpdate;

    @XmlElement(name = "BillTo")
    private EntityWrapper billTo;

    @XmlElement(name = "ShipTo")
    private EntityWrapper shipTo;

    @XmlElement(name = "ShippingInformation")
    private ShippingInformation shippingInformation;

    @XmlElement(name = "OutsideSalesPerson")
    private EclipseIDWrapper outsideSalesPerson;

    @XmlElement(name = "InsideSalesPerson")
    private EclipseIDWrapper insideSalesPerson;

    @XmlElement(name = "WrittenBy")
    private EclipseIDWrapper writtenBy;

    @XmlElement(name = "OrderedBy")
    private Description orderedBy;

    @XmlElement(name = "Telephone")
    private Telephone telephone;

    @XmlElement(name = "EmailAddress")
    private String emailAddress;

    @XmlElement(name = "Currency")
    private String currency;

    @XmlElement(name = "PaymentTerms")
    private PaymentWrapper paymentTerms;

    @XmlElement(name = "ProgressBilling")
    private String progressBilling;

    @XmlElement(name = "InternalNotes")
    private String internalNotes;

    @XmlElement(name = "SalesSource")
    private String salesSource;

    @XmlElement(name = "OrderSource")
    private String orderSource;
}
