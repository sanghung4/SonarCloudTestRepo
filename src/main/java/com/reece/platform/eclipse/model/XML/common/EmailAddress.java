package com.reece.platform.eclipse.model.XML.common;

import lombok.Data;

import javax.xml.bind.annotation.*;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "Type", "HTMLPref", "content" })
public class EmailAddress {

    public EmailAddress() {}

    public EmailAddress(String email) {
        this.content = email;
    }

    @XmlAttribute(name = "Type")
    private String Type;

    @XmlAttribute(name = "HTMLPref")
    private String HTMLPref;

    @XmlValue
    private String content;

    @Override
    public String toString()
    {
        return "ClassPojo [Type = "+Type+", HTMLPref = "+HTMLPref+", content = "+content+"]";
    }
}
