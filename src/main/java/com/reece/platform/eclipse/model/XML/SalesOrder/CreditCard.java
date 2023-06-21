package com.reece.platform.eclipse.model.XML.SalesOrder;

import com.reece.platform.eclipse.model.XML.common.DateWrapper;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.UUID;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "creditCardType", "creditCardNumber", "expirationDate", "cardHolder", "streetAddress", "postalCode", "elementPaymentAccountId" })
public class CreditCard {

    @XmlElement(name = "CreditCardType")
    private String creditCardType;

    @XmlElement(name = "CreditCardNumber")
    private String creditCardNumber;

    @XmlElement(name = "ExpirationDate")
    private DateWrapper expirationDate;

    @XmlElement(name = "CardHolder")
    private String cardHolder;

    @XmlElement(name = "StreetAddress")
    private String streetAddress;

    @XmlElement(name = "PostalCode")
    private String postalCode;

    @XmlElement(name = "ElementPaymentAccountId")
    private UUID elementPaymentAccountId;
}
