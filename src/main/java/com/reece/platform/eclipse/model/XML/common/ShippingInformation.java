package com.reece.platform.eclipse.model.XML.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"Address", "ShipVia", "Instructions"})
public class ShippingInformation {

    @XmlElement(name = "Address")
    private Address Address;

    @XmlElement(name = "ShipVia")
    private ShipVia ShipVia;

    @XmlElement(name = "Instructions")
    private String Instructions;
}
