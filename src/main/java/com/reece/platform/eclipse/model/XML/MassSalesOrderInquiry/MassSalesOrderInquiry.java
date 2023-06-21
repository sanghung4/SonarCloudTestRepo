package com.reece.platform.eclipse.model.XML.MassSalesOrderInquiry;


import com.reece.platform.eclipse.model.XML.AccountHistoryInquiry.PostDateRange;
import com.reece.platform.eclipse.model.XML.common.Security;
import com.reece.platform.eclipse.model.XML.common.SitePass;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"Security", "SitePass", "InvoiceNumber", "StartIndex", "MaxResults", "OrderDateRange", "IncludeUserDefined"})
public class MassSalesOrderInquiry {

    @XmlElement(name = "Security")
    private Security Security;

    @XmlElement(name = "SitePass")
    private SitePass SitePass;

    @XmlElement(name = "InvoiceNumber")
    private String InvoiceNumber;

    @XmlElement(name = "StartIndex")
    private String StartIndex;

    @XmlElement(name = "MaxResults")
    private String MaxResults;

    @XmlElement(name = "OrderDateRange")
    private PostDateRange OrderDateRange;

    @XmlElement(name = "IncludeUserDefined")
    private String IncludeUserDefined;
}
