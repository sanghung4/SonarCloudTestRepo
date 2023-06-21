package com.reece.platform.eclipse.model.XML.SalesOrder;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@Data
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "lineItemComment" })
public class LineItemComment {

    @XmlElement(name = "LineItemComment")
    private String lineItemComment;
}
