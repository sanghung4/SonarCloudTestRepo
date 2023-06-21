package com.reece.platform.eclipse.model.XML.EntityResponse;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "Value", "Name" })
public class ClassificationItem {

    @XmlElement(name = "Value")
    private String Value;

    @XmlElement(name = "Name")
    private String Name;

    @Override
    public String toString()
    {
        return "ClassPojo [Value = "+Value+", Name = "+Name+"]";
    }
}
