package com.reece.platform.eclipse.model.XML.ElementAccount;

import lombok.Data;

import javax.xml.bind.annotation.*;


@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"sessionId", "elementSetupUrl", "elementSetupId", "statusResult"})
public class ElementAccountSetupResponse {

    @XmlElement(name = "SessionID")
    private String sessionId;

    @XmlElement(name = "ElementSetupUrl")
    private String elementSetupUrl;

    @XmlElement(name = "ElementSetupId")
    private String elementSetupId;

    @XmlElement(name = "StatusResult")
    private StatusResult statusResult;
}
