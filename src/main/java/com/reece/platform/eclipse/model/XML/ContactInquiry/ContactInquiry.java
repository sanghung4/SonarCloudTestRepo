package com.reece.platform.eclipse.model.XML.ContactInquiry;

import com.reece.platform.eclipse.model.XML.common.Contact;
import com.reece.platform.eclipse.model.XML.common.Security;
import lombok.Data;

import javax.xml.bind.annotation.*;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "Security", "ContactID" })
public class ContactInquiry {
    @XmlElement(name = "Security")
    private Security Security;

    @XmlElement(name = "ContactID")
    private String ContactID;
}
