package com.reece.platform.eclipse.model.XML.ReorderPadInquiryResponse;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"reorderPadInquiryResponse"})
public class ReorderPadInquiryResponseWrapper {

    @XmlElement(name = "ReorderPadInquiryResponse")
    private ReorderPadInquiryResponse reorderPadInquiryResponse;
}
