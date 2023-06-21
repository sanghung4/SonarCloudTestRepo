package com.reece.platform.eclipse.model.XML.ElementAccount;

import com.reece.platform.eclipse.model.XML.SalesOrder.CreditCard;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"sessionId", "creditCard", "statusResult"})
public class ElementSetupQueryResponse {

    @XmlElement(name = "SessionID")
    private String sessionId;

    @XmlElement(name = "CreditCard")
    private CreditCard creditCard;

    @XmlElement(name = "StatusResult")
    private StatusResult statusResult;
}
