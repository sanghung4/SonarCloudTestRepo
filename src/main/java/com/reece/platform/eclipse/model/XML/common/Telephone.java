package com.reece.platform.eclipse.model.XML.common;

import lombok.Data;

import javax.xml.bind.annotation.*;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "number", "description" })
public class Telephone {

    @XmlElement(name = "Number")
    private String number;

    @XmlElement(name = "Description")
    private String description;
}
