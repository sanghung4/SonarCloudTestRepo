
package com.reece.platform.eclipse.model.XML.common;

import com.reece.platform.eclipse.model.XML.common.Branch;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {
    "branch"
})
public class ShippingBranch {

    @XmlElement(name = "Branch")
    private Branch branch;
}
