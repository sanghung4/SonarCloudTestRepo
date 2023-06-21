package com.reece.platform.eclipse.model.XML.common;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "loginID", "password", "sessionId", "activeCustomer" })
public class Login {
    @XmlElement(name = "SessionId")
    private String sessionId;

    @XmlElement(name = "LoginID")
    private String loginID;

    @XmlElement(name = "Password")
    private String password;

    @XmlElement(name = "ActiveCustomer")
    private String activeCustomer;
}
