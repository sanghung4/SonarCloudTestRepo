package com.reece.platform.eclipse.model.XML.EntityResponse;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "Number" })
public class Telephone {

    @XmlElement(name = "Number")
    private String Number;

    @Override
    public String toString()
    {
        return "ClassPojo [Number = "+Number+"]";
    }
}
