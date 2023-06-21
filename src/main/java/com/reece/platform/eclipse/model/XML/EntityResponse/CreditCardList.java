package com.reece.platform.eclipse.model.XML.EntityResponse;

import com.reece.platform.eclipse.model.XML.SalesOrder.CreditCard;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "creditCard" })
public class CreditCardList {

    @XmlElement(name = "CreditCard" )
    private List<CreditCard> creditCard;
}
