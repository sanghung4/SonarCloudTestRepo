package com.reece.platform.eclipse.model.XML.AccountInquiryResponse;

import com.reece.platform.eclipse.model.XML.common.DateWrapper;
import com.reece.platform.eclipse.model.XML.common.Payment;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"entityStartDate", "asOfDate", "future", "current", "thirty",
        "sixty", "ninety", "oneTwenty", "arTotal", "arDeposits",
        "arOrders", "mtdSales", "ytdSales", "sixMonthAverage", "sixMonthHigh", "paymentDays",
        "arTerms", "arCreditLimit", "arCreditAvail", "lastSale", "lastPayment", "currency"})
public class AccountInquirySummary {

    @XmlElement(name = "EntityStartDate")
    private DateWrapper entityStartDate;

    @XmlElement(name = "AsOfDate")
    private DateWrapper asOfDate;

    @XmlElement(name = "Future")
    private Double future;

    @XmlElement(name = "Current")
    private Double current;

    @XmlElement(name = "Thirty")
    private Double thirty;

    @XmlElement(name = "Sixty")
    private Double sixty;

    @XmlElement(name = "Ninety")
    private Double ninety;

    @XmlElement(name = "OneTwenty")
    private Double oneTwenty;

    @XmlElement(name = "ARTotal")
    private Double arTotal;

    @XmlElement(name = "ARDeposits")
    private Double arDeposits;

    @XmlElement(name = "AROrders")
    private Double arOrders;

    @XmlElement(name = "MTDSales")
    private Double mtdSales;

    @XmlElement(name = "YTDSales")
    private Double ytdSales;

    @XmlElement(name = "SixMonthAverage")
    private Double sixMonthAverage;

    @XmlElement(name = "SixMonthHigh")
    private Double sixMonthHigh;

    @XmlElement(name = "PaymentDays")
    private Integer paymentDays;

    @XmlElement(name = "ARTerms")
    private String arTerms;

    @XmlElement(name = "ARCreditLimit")
    private Double arCreditLimit;

    @XmlElement(name = "ARCreditAvail")
    private Double arCreditAvail;

    @XmlElement(name = "LastSale")
    private Payment lastSale;

    @XmlElement(name = "LastPayment")
    private Payment lastPayment;

    @XmlElement(name = "Currency")
    private String currency;
}
