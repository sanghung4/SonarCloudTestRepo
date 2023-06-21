package com.reece.platform.eclipse.model.XML.SalesOrderInquiry;


import com.reece.platform.eclipse.model.XML.common.Security;
import com.reece.platform.eclipse.model.XML.common.SitePass;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"Security", "SitePass", "OrderID", "InvoiceNumber", "IncludeUserDefined"})
public class SalesOrderInquiry {

    @XmlElement(name = "Security")
    private Security Security;

    @XmlElement(name = "SitePass")
    private SitePass SitePass;

    @XmlElement(name = "OrderID")
    private String OrderID;

    @XmlElement(name = "InvoiceNumber")
    private String InvoiceNumber;

    @XmlElement(name = "IncludeUserDefined")
    private String IncludeUserDefined;
}
