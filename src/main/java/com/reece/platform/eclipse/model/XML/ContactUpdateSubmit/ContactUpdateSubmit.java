package com.reece.platform.eclipse.model.XML.ContactUpdateSubmit;

import com.reece.platform.eclipse.model.XML.common.Contact;
import com.reece.platform.eclipse.model.XML.common.Security;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "Security", "Contact" })
public class ContactUpdateSubmit {
    private Security Security;
    private Contact Contact;
}
