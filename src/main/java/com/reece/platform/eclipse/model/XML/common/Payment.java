
package com.reece.platform.eclipse.model.XML.common;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {
    "amount",
    "date"
})
public class Payment {

    @XmlElement(name = "Amount")
    private Double amount;
    
    @XmlElement(name = "Date")
    private String date;
}
