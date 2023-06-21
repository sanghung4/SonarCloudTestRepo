package com.reece.platform.eclipse.model.XML.SalesOrder.SalesOrderSubmit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "IDMS-XML")
@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "salesOrderSubmitPreview" })
public class SalesOrderSubmitPreviewRequest {
    @XmlElement(name = "SalesOrderSubmitPreview")
    private SalesOrderSubmit salesOrderSubmitPreview;
}
