package com.reece.platform.eclipse.model.XML.SalesOrder;

import com.reece.platform.eclipse.model.XML.common.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Dictionary;
import java.util.List;

@Data
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "orderId", "invoiceNumber", "orderStatus", "printStatus", "orderType", "pricingBranch",
        "shippingBranch", "orderDate", "requiredDate", "shipDate", "bidExpireDate",
        "invoiceDueDate", "lastUpdate", "outsideSalesPerson", "writtenBy", "emailAddress", "creditCard", "currency",
        "paymentTerms", "progressBilling", "orderSource", "salesSource", "billTo", "shipTo", "customerPO",
        "internalNotes", "orderedBy", "shippingInformation", "taxExemptCode",
        "taxExemptId", "taxJurisdiction", "telephone", "userDefinedData", "quoteStatus", "shipToId"})
public class OrderHeader {

    public OrderHeader(BillTo billTo, ShipTo shipTo, DateWrapper requiredDate, ShippingInformation shippingInformation,
                       Telephone telephone, OrderStatus orderStatus, String quoteStatus, Description orderedBy, String customerPO, ShippingBranch shippingBranch, CreditCard creditCard) {
        this.billTo = billTo;
        this.shipTo = shipTo;
        this.requiredDate = requiredDate;
        this.shippingInformation = shippingInformation;
        this.telephone = telephone;
        this.orderStatus = orderStatus;
        this.quoteStatus = quoteStatus;
        this.orderedBy = orderedBy;
        this.customerPO = customerPO;
        this.shippingBranch = shippingBranch;
        this.creditCard = creditCard;
    }

    @XmlElement(name = "OrderID")
    private String orderId;

    @XmlElement(name = "InvoiceNumber")
    private String invoiceNumber;

    @XmlElement(name = "OrderStatus")
    private OrderStatus orderStatus;

    @XmlElement(name = "PrintStatus")
    private String printStatus;

    @XmlElement(name = "OrderType")
    private String orderType;

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

    @XmlElement(name = "BidExpireDate")
    private DateWrapper bidExpireDate;

    @XmlElement(name = "InvoiceDueDate")
    private DateWrapper invoiceDueDate;

    @XmlElement(name = "LastUpdate")
    private DateTimeWrapper lastUpdate;

    @XmlElement(name = "OutsideSalesPerson")
    private EclipseIDWrapper outsideSalesPerson;

    @XmlElement(name = "WrittenBy")
    private EclipseIDWrapper writtenBy;

    @XmlElement(name = "EmailAddress")
    private String emailAddress;

    @XmlElement(name = "CreditCard")
    private CreditCard creditCard;

    @XmlElement(name = "Currency")
    private String currency;

    @XmlElement(name = "PaymentTerms")
    private PaymentWrapper paymentTerms;

    @XmlElement(name = "ProgressBilling")
    private String progressBilling;

    @XmlElement(name = "OrderSource")
    private String orderSource;

    @XmlElement(name = "SalesSource")
    private String salesSource;

    @XmlElement(name = "BillTo")
    private BillTo billTo;

    @XmlElement(name = "ShipTo")
    private ShipTo shipTo;

    @XmlElement(name = "CustomerPO")
    private String customerPO;

    @XmlElement(name = "InternalNotes")
    private String internalNotes;

    @XmlElement(name = "OrderedBy")
    private Description orderedBy;

    @XmlElement(name = "ShippingInformation")
    private ShippingInformation shippingInformation;

    @XmlElement(name = "TaxExemptCode")
    private String taxExemptCode;

    @XmlElement(name = "TaxExemptID")
    private String taxExemptId;

    @XmlElement(name = "TaxExemptID")
    private String taxJurisdiction;

    @XmlElement(name = "Telephone")
    private Telephone telephone;

    @XmlElement(name = "UserDefinedData")
    private List<Dictionary<String, String>> userDefinedData;

    @XmlElement(name = "QuoteStatus")
    private String quoteStatus;

    @XmlElement(name = "ShipToId")
    private String shipToId;
}
