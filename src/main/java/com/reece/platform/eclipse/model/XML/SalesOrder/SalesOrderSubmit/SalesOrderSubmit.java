package com.reece.platform.eclipse.model.XML.SalesOrder.SalesOrderSubmit;

import com.reece.platform.eclipse.model.XML.SalesOrder.SalesOrder;
import com.reece.platform.eclipse.model.XML.common.Security;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "security", "salesOrder" })
public class SalesOrderSubmit {

    @XmlElement(name = "Security")
    private Security security;

    @XmlElement(name = "SalesOrder")
    private SalesOrder salesOrder;
}
