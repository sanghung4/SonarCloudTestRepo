package com.reece.platform.eclipse.model.XML.ContactNewSubmitResponse;

import lombok.Data;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "IDMS-XML")
@Data
@XmlType(propOrder = { "contactNewSubmitResponse" })
@XmlAccessorType(XmlAccessType.FIELD)
public class ContactNewSubmitResponseWrapper {
    @XmlElement(name = "ContactNewSubmitResponse")
    private ContactNewSubmitResponse contactNewSubmitResponse;
}
