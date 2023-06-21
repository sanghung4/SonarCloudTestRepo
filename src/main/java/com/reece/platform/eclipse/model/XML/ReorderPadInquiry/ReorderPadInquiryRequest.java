package com.reece.platform.eclipse.model.XML.ReorderPadInquiry;

import lombok.Data;

import javax.xml.bind.annotation.*;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "IDMS-XML")
@XmlType(propOrder = {"reorderPadInquiry"})
public class ReorderPadInquiryRequest {

    @XmlElement(name = "ReorderPadInquiry")
    private ReorderPadInquiry reorderPadInquiry;
}
