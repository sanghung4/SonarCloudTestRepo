package com.reece.platform.eclipse.model.XML.common;

import lombok.Data;

import javax.xml.bind.annotation.*;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "contactID", "entityID", "contactName", "telephoneList", "emailAddressList", "isSuperuser", "login"})
public class Contact {

    @XmlElement(name = "ContactID")
    private ContactID contactID;

    @XmlElement(name =  "EntityID")
    private String entityID;

    @XmlElement(name = "ContactName")
    private ContactName contactName;

    @XmlElement(name = "TelephoneList")
    private TelephoneList telephoneList;

    @XmlElement(name = "EmailAddressList")
    private EmailAddressList emailAddressList;

    @XmlElement(name = "IsSuperuser")
    private String isSuperuser;

    @XmlElement(name = "Login")
    private Login login;
}
