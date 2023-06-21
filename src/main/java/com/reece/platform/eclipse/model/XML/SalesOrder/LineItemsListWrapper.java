package com.reece.platform.eclipse.model.XML.SalesOrder;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@AllArgsConstructor
@XmlType(propOrder = { "lineItemList" })
public class LineItemsListWrapper {
    @XmlElement(name = "LineItem")
    private List<LineItem> lineItemList;
}
