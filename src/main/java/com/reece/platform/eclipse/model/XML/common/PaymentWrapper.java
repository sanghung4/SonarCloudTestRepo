
package com.reece.platform.eclipse.model.XML.common;

import com.reece.platform.eclipse.model.XML.common.DiscountWrapper;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {
    "code",
    "discount",
    "description"
})
public class PaymentWrapper {

    @XmlElement(name = "Code")
    private String code;
    
    @XmlElement(name = "Discount")
    private DiscountWrapper discount;
    
    @XmlElement(name = "Description")
    private String description;
}
