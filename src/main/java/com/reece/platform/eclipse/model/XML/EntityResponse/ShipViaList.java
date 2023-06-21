package com.reece.platform.eclipse.model.XML.EntityResponse;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "ShipViaID" })
public class ShipViaList {

    @XmlElement(name = "ShipViaID")
    private List<String> ShipViaID;

    @Override
    public String toString()
    {
        return "ClassPojo [ShipViaID = "+ShipViaID+"]";
    }
}
