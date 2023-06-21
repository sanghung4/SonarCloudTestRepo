package com.reece.platform.eclipse.model.XML.EntityResponse;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "CustomerType" })
public class CustomerTypeList {

    @XmlElement(name = "CustomerType")
    private String CustomerType;

    @Override
    public String toString()
    {
        return "ClassPojo [CustomerType = "+CustomerType+"]";
    }
}
