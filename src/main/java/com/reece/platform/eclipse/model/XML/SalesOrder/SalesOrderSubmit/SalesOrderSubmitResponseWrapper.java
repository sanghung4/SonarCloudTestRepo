package com.reece.platform.eclipse.model.XML.SalesOrder.SalesOrderSubmit;

import lombok.Data;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "IDMS-XML")
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "salesOrderSubmitResponse" })
public class SalesOrderSubmitResponseWrapper {

    @XmlElement(name = "SalesOrderSubmitResponse")
    private SalesOrderSubmitResponse salesOrderSubmitResponse;
}
