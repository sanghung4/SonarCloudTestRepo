
package com.reece.platform.eclipse.model.XML.SalesOrderResponse;

import com.reece.platform.eclipse.model.XML.common.LineItemPrice;
import com.reece.platform.eclipse.model.XML.common.PartIdentifiers;
import com.reece.platform.eclipse.model.XML.common.QuantityWrapper;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {
    "lineItemID",
    "partIdentifiers",
    "lineItemPrice",
    "priceUOM",
    "pricePerQty",
    "qtyOrdered",
    "qtyShipped"
})
public class LineItem {

    @XmlElement(name = "LineItemID")
    private String lineItemID;
    
    @XmlElement(name = "PartIdentifiers")
    private PartIdentifiers partIdentifiers;
    
    @XmlElement(name = "LineItemPrice")
    private LineItemPrice lineItemPrice;
    
    @XmlElement(name = "PriceUOM")
    private String priceUOM;
    
    @XmlElement(name = "PricePerQty")
    private String pricePerQty;
    
    @XmlElement(name = "QtyOrdered")
    private QuantityWrapper qtyOrdered;
    
    @XmlElement(name = "QtyShipped")
    private QuantityWrapper qtyShipped;
     
}
