package com.reece.platform.eclipse.model.XML.EntityResponse;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "ShipViaList", "OrderStatusList", "DisplayProductAvailability" })
public class EntityRemoteData {

    @XmlElement(name = "ShipViaList")
    private ShipViaList ShipViaList;

    @XmlElement(name = "OrderStatusList")
    private OrderStatusList OrderStatusList;

    @XmlElement(name = "DisplayProductAvailability")
    private String DisplayProductAvailability;

    @Override
    public String toString()
    {
        return "ClassPojo [ShipViaList = "+ShipViaList+", OrderStatusList = "+OrderStatusList+", DisplayProductAvailability = "+DisplayProductAvailability+"]";
    }
}
