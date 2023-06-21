package com.reece.platform.eclipse.model.XML.common;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@Data
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "entityID", "entity" })
public class BillTo {

    public BillTo(String EntityID) {
        this.entityID = EntityID;
    }

    @XmlElement(name = "EntityID")
    private String entityID;

    @XmlElement(name = "Entity")
    private Entity entity;
}
