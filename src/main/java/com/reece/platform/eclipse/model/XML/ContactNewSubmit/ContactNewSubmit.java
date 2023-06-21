package com.reece.platform.eclipse.model.XML.ContactNewSubmit;

import com.reece.platform.eclipse.model.XML.common.Contact;
import com.reece.platform.eclipse.model.XML.common.Security;
import lombok.Data;

import javax.xml.bind.annotation.*;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "security", "contact" })
public class ContactNewSubmit {
    @XmlElement(name = "Security")
    private Security security;

    @XmlElement(name = "Contact")
    private Contact contact;
}
