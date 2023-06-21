package com.reece.platform.eclipse.model.XML.EntityResponse;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "Type", "Description" })
public class EntityNote {

    @XmlElement(name = "Type")
    private String Type;

    @XmlElement(name = "Description")
    private String Description;

    @Override
    public String toString()
    {
        return "ClassPojo [Type = "+Type+", Description = "+Description+"]";
    }
}
