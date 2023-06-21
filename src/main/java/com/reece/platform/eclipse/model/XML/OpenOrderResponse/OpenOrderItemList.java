package com.reece.platform.eclipse.model.XML.OpenOrderResponse;
import lombok.*;

import javax.xml.bind.annotation.*;
import java.util.*;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "OpenOrderItems" })
public class OpenOrderItemList {

    @XmlElement(name = "OpenOrderItem")
    private List<OpenOrderItem> OpenOrderItems;
}
