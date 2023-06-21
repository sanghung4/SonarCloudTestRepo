package com.reece.platform.eclipse.model.XML.common;

import lombok.Data;

import javax.xml.bind.annotation.*;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ContactID")
public class ContactID {

    @XmlAttribute(name = "New")
    private String isNew;

    @XmlValue
    private String contactId;
}
