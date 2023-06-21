package com.reece.platform.eclipse.model.XML.LoginSubmit;

import com.reece.platform.eclipse.model.XML.common.Security;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"security"})
@NoArgsConstructor
public class LoginSubmit {

    @XmlElement(name = "Security")
    private Security security;
}
