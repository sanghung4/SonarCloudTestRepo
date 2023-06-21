package com.reece.platform.eclipse.model.XML.EntityResponse;

import com.reece.platform.eclipse.model.XML.common.OrderStatus;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "OrderStatus" })
public class OrderStatusList {

    @XmlElement(name = "OrderStatus")
    private List<OrderStatus> OrderStatus;

    @Override
    public String toString()
    {
        return "ClassPojo [OrderStatus = "+OrderStatus+"]";
    }
}
