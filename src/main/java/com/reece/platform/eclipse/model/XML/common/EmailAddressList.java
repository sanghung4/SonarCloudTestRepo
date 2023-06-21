package com.reece.platform.eclipse.model.XML.common;

import com.reece.platform.eclipse.model.XML.common.EmailAddress;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "EmailAddresses" })
public class EmailAddressList {

    @XmlElement(name = "EmailAddress")
    private List<EmailAddress> EmailAddresses;

    @Override
    public String toString()
    {
        return "ClassPojo [EmailAddress = "+EmailAddresses+"]";
    }
}
