package com.reece.platform.eclipse.model.XML.SalesOrderResponse;

import lombok.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType( propOrder = {
    "lineItems",
    "lineItemCount"
})
public class LineItemList {

    @XmlElement(name = "LineItem")
    private List<LineItem> lineItems;

    @XmlElement(name = "LineItemCount")
    private String lineItemCount;

}
