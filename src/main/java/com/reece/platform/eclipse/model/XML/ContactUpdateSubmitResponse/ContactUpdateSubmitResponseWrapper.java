package com.reece.platform.eclipse.model.XML.ContactUpdateSubmitResponse;

import lombok.Data;

import javax.xml.bind.annotation.*;

@Data
@XmlRootElement(name = "IDMS-XML")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "ContactUpdateSubmitResponse" })
public class ContactUpdateSubmitResponseWrapper {
    @XmlElement(name = "ContactUpdateSubmitResponse")
    private ContactUpdateSubmitResponse ContactUpdateSubmitResponse;

    @Override
    public String toString()
    {
        return "ClassPojo [ContactUpdateSubmitResponse = "+ContactUpdateSubmitResponse+"]";
    }
}
