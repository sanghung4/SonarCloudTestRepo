package com.reece.platform.eclipse.model.XML.ContactNewSubmitResponse;

import com.reece.platform.eclipse.model.XML.common.Contact;
import com.reece.platform.eclipse.model.XML.common.StatusResult;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "contact", "statusResult" })
public class ContactNewSubmitResponse {
    @XmlElement(name = "Contact")
    private Contact contact;

    @XmlElement(name = "StatusResult")
    private StatusResult statusResult;
}
