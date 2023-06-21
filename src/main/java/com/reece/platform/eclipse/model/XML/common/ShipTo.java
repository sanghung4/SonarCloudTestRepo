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
@XmlType(propOrder = { "entityId", "entity" })
public class ShipTo {

    public ShipTo(String entityId) {
        this.entityId = entityId;
    }

    @XmlElement(name = "EntityID")
    private String entityId;

    @XmlElement(name = "Entity")
    private Entity entity;
}
