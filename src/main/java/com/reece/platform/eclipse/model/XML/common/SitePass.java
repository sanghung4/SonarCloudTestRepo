package com.reece.platform.eclipse.model.XML.common;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@Data
@XmlType(propOrder = { "activeCustomer", "loginId", "password" })
@XmlAccessorType(XmlAccessType.FIELD)
public class SitePass {

    @XmlElement(name = "ActiveCustomer")
    private String activeCustomer;

    @XmlElement(name = "LoginID")
    private String loginId;

    @XmlElement(name = "Password")
    private String password;

}