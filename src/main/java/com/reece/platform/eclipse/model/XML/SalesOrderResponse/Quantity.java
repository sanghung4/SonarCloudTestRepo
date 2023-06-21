
package com.reece.platform.eclipse.model.XML.SalesOrderResponse;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {
    "value"
})
public class Quantity {

    @XmlValue
    protected int value;

    @XmlAttribute(name = "UOM")
    protected String uom;

    @XmlAttribute(name = "UMQT")
    protected String umqt;
}
