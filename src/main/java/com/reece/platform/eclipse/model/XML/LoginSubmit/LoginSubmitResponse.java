package com.reece.platform.eclipse.model.XML.LoginSubmit;

import lombok.Data;

import javax.xml.bind.annotation.*;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "sessionId", "contactId", "entityId" })
public class LoginSubmitResponse {

    @XmlElement(name = "SessionID")
    private String sessionId;

    @XmlElement(name = "ContactId")
    private String contactId;

    @XmlElement(name = "EntityID")
    private String entityId;
}
