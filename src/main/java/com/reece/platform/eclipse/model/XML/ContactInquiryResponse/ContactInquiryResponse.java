package com.reece.platform.eclipse.model.XML.ContactInquiryResponse;

import com.reece.platform.eclipse.model.XML.common.Contact;
import com.reece.platform.eclipse.model.XML.common.StatusResult;
import lombok.Data;

import javax.xml.bind.annotation.*;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "StatusResult", "Contact" })
public class ContactInquiryResponse {
    @XmlElement(name = "StatusResult")
    StatusResult StatusResult;

    @XmlElement(name = "Contact")
    Contact Contact;
}
