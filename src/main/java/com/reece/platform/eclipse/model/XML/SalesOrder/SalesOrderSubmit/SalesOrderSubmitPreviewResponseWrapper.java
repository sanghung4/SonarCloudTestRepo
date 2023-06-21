package com.reece.platform.eclipse.model.XML.SalesOrder.SalesOrderSubmit;

import lombok.Data;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "IDMS-XML")
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "salesOrderSubmitPreviewResponse" })
public class SalesOrderSubmitPreviewResponseWrapper {

    @XmlElement(name = "SalesOrderSubmitPreviewResponse")
    private SalesOrderSubmitPreviewResponse salesOrderSubmitPreviewResponse;
}
