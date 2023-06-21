package com.reece.platform.eclipse.model.XML.ContactInquiryResponse;

import lombok.Data;

import javax.xml.bind.annotation.*;

@Data
@XmlRootElement(name = "IDMS-XML")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "ContactInquiryResponse" })
public class ContactInquiryResponseWrapper {
    @XmlElement(name = "ContactInquiryResponse")
    ContactInquiryResponse ContactInquiryResponse;
}
