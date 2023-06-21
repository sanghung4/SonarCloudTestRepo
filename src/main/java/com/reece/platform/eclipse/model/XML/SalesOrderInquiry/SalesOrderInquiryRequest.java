package com.reece.platform.eclipse.model.XML.SalesOrderInquiry;

import com.reece.platform.eclipse.model.XML.common.Security;
import com.reece.platform.eclipse.model.XML.common.SitePass;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.*;

@Data
@XmlRootElement(name = "IDMS-XML")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"SalesOrderInquiry"})
@NoArgsConstructor
public class SalesOrderInquiryRequest {

    public SalesOrderInquiryRequest(Security security, String orderID, String invoiceNumber) {

        SalesOrderInquiry salesOrderInquiry = new SalesOrderInquiry();
        salesOrderInquiry.setSecurity(security);
        salesOrderInquiry.setOrderID(orderID);
        salesOrderInquiry.setInvoiceNumber(invoiceNumber);

        this.SalesOrderInquiry = salesOrderInquiry;
    }

    @XmlElement(name = "SalesOrderInquiry")
    private SalesOrderInquiry SalesOrderInquiry;
}
