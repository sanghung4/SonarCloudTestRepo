package com.reece.platform.eclipse.model.XML.AccountHistoryInquiry;

import com.reece.platform.eclipse.model.XML.common.Security;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "Security", "EntityID", "PostDateRange" })
public class AccountHistoryInquiry {
    @XmlElement(name = "Security")
    private Security Security;

    @XmlElement(name = "EntityID")
    private String EntityID;

    @XmlElement(name = "PostDateRange")
    private PostDateRange PostDateRange;
}
