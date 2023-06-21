package com.reece.platform.eclipse.model.XML.EntityResponse;

import lombok.Data;

import javax.xml.bind.annotation.*;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "Description", "Currency", "CreditLimit", "Terms", "OrderEntryOK", "CreditAvailable" })
public class Credit {

    @XmlElement(name = "Description")
    private String Description;

    @XmlAttribute(name = "Currency")
    private String Currency;

    @XmlAttribute(name = "CreditLimit")
    private String CreditLimit;

    @XmlAttribute(name = "Terms")
    private String Terms;

    @XmlAttribute(name = "OrderEntryOK")
    private String OrderEntryOK;

    @XmlAttribute(name = "CreditAvailable")
    private String CreditAvailable;

    @Override
    public String toString()
    {
        return "ClassPojo [Description = "+Description+", Currency = "+Currency+", CreditLimit = "+CreditLimit+", Terms = "+Terms+", OrderEntryOK = "+OrderEntryOK+", CreditAvailable = "+CreditAvailable+"]";
    }
}
