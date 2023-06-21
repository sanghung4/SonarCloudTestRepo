package com.reece.platform.eclipse.model.XML.common;

import com.reece.platform.eclipse.model.enums.OrderStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.*;

@Data
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "code", "content" })
public class OrderStatus {

    public OrderStatus(OrderStatusEnum orderStatusEnum) {
        this.code = orderStatusEnum.getCode();
        this.content = orderStatusEnum.getContent();
    }

    @XmlAttribute(name = "Code")
    private String code;

    @XmlValue
    private String content;

    @Override
    public String toString()
    {
        return "ClassPojo [Code = "+code+", content = "+content+"]";
    }
}
