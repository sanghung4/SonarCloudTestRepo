package com.reece.platform.eclipse.model.XML.ReorderPadInquiryResponse;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "reorderPadItems" })
public class ReorderPadList {

    @XmlElement(name = "ReorderPadItem")
    private List<ReorderPadItem> reorderPadItems;
}
