package com.reece.platform.eclipse.model.XML.SalesOrder;

import com.reece.platform.eclipse.model.XML.common.OrderTotals;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "customerPartNumber" })
public class CustomerPartNumberList {

    @XmlElement(name = "CustomerPartNumber")
    private List<String> customerPartNumber;

}
