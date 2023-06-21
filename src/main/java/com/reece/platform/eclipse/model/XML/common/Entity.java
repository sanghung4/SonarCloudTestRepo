
package com.reece.platform.eclipse.model.XML.common;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GenericEntity", propOrder = {
    "entityID",
    "entityName",
    "address",
    "alternateAddress"
})
public class Entity {

    @XmlElement(name = "EntityID")
    private String entityID;
    
    @XmlElement(name = "EntityName")
    private String entityName;
    
    @XmlElement(name = "Address")
    private Address address;
    
    @XmlElement(name = "AlternateAddress")
    private Address alternateAddress;
}
