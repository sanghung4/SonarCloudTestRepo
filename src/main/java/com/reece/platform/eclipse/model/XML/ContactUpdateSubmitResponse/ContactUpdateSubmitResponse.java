package com.reece.platform.eclipse.model.XML.ContactUpdateSubmitResponse;

import com.reece.platform.eclipse.model.XML.common.Contact;
import com.reece.platform.eclipse.model.XML.common.StatusResult;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "StatusResult", "SessionID", "Contact" })
public class ContactUpdateSubmitResponse {
    private StatusResult StatusResult;

    private String SessionID;

    private Contact Contact;

    @Override
    public String toString()
    {
        return "ClassPojo [StatusResult = "+StatusResult+", SessionID = "+SessionID+", Contact = "+Contact+"]";
    }
}
