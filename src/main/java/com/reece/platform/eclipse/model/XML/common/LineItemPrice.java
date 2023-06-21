
package com.reece.platform.eclipse.model.XML.common;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {
    "unitPrice",
    "listPrice",
    "extendedPrice"
})
public class LineItemPrice {

    @XmlElement(name = "UnitPrice")
    private float unitPrice;
    
    @XmlElement(name = "ListPrice")
    private float listPrice;
    
    @XmlElement(name = "ExtendedPrice")
    private float extendedPrice;

}
