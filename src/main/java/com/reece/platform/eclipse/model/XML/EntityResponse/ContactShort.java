package com.reece.platform.eclipse.model.XML.EntityResponse;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "Description", "Telephone" })
public class ContactShort {

    @XmlElement(name = "Description")
        private String Description;

    @XmlElement(name = "Telephone")
        private Telephone Telephone;

    @Override
    public String toString()
    {
        return "ClassPojo [Description = "+Description+", Telephone = "+Telephone+"]";
    }
}
