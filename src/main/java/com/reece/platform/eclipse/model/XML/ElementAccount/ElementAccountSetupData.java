package com.reece.platform.eclipse.model.XML.ElementAccount;

import com.reece.platform.eclipse.model.XML.common.Security;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "cardHolder", "streetAddress", "postalCode", "returnUrl" })
public class ElementAccountSetupData {

    @XmlElement(name = "CardHolder")
    private String cardHolder;

    @XmlElement(name = "StreetAddress")
    private String streetAddress;

    @XmlElement(name = "PostalCode")
    private String postalCode;

    @XmlElement(name = "ReturnUrl")
    private String returnUrl;


}
