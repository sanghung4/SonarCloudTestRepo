package com.reece.platform.eclipse.model.XML.AccountInquiryResponse;

import com.reece.platform.eclipse.model.XML.common.Payment;
import com.reece.platform.eclipse.model.XML.common.ShippingInformation;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"accountRegisterID", "branchId", "transaction", "payment", "balance", "age", "customerPo", "currency", "shippingInformation"})
public class AccountInquiryItem {
    @XmlElement(name = "AccountRegisterID")
    private String accountRegisterID;

    @XmlElement(name = "BranchID")
    private String branchId;

    @XmlElement(name = "Transaction")
    private Payment transaction;

    @XmlElement(name = "Payment")
    private Payment payment;

    @XmlElement(name = "Balance")
    private Double balance;

    @XmlElement(name = "Age")
    private String age;

    @XmlElement(name = "CustomerPO")
    private String customerPo;

    @XmlElement(name = "Currency")
    private String currency;

    @XmlElement(name = "ShippingInformation")
    private ShippingInformation shippingInformation;
}
