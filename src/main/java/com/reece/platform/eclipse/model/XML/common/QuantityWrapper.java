package com.reece.platform.eclipse.model.XML.common;

import com.reece.platform.eclipse.model.XML.SalesOrderResponse.Quantity;
import lombok.Data;

import javax.xml.bind.annotation.*;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "quantity" })
public class QuantityWrapper {
    @XmlElement(name = "Quantity")
    private Quantity quantity;
}
