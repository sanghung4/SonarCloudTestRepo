package com.reece.platform.eclipse.model.XML.ReorderPadInquiryResponse;

import lombok.Data;

import javax.xml.bind.annotation.*;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"lastDate", "lastQuantity"})
public class LastOrder {

    @XmlElement(name = "LastDate")
    private String lastDate;

    @XmlElement(name = "LastQuantity")
    private String lastQuantity;
}
