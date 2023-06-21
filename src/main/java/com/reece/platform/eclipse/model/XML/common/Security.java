package com.reece.platform.eclipse.model.XML.common;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "sessionId", "login", "sitePass" })
public class Security implements Serializable {

    @XmlElement(name = "Login")
    private Login login;

    @XmlElement(name = "SessionID")
    private String sessionId;

    @XmlElement(name = "SitePass")
    private SitePass sitePass;
}