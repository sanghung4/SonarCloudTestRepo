package com.reece.platform.eclipse.model.XML.ProductResponse.MassProductInquiryResponse.Product.RichContentList;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@Data
@XmlType(propOrder = { "" })
@XmlAccessorType(XmlAccessType.FIELD)
public class RichContentList {

    @XmlElement(name = "RichContentItem")
    private List<RichContentItem> richContentItemList;
}
