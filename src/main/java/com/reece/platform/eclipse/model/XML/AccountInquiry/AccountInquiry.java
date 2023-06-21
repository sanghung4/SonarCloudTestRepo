package com.reece.platform.eclipse.model.XML.AccountInquiry;

import com.reece.platform.eclipse.model.XML.common.Security;
import lombok.Data;

import javax.xml.bind.annotation.*;

@Data
@XmlRootElement(name = "IDMS-XML")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "security", "entityId"})
public class AccountInquiry {

    @XmlElement(name = "Security")
    private Security security;

    @XmlElement(name = "EntityID")
    private String entityId;
}
