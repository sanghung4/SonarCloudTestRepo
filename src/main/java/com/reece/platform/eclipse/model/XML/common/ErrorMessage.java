package com.reece.platform.eclipse.model.XML.common;


import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"Code", "Description"})
public class ErrorMessage {

    @XmlElement(name = "Code")
    private String Code;

    @XmlElement(name = "Description")
    private String Description;
}
