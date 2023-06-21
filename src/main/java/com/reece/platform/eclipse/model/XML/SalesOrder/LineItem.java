package com.reece.platform.eclipse.model.XML.SalesOrder;

import com.reece.platform.eclipse.model.DTO.LineItemDTO;
import com.reece.platform.eclipse.model.XML.SalesOrderResponse.Quantity;
import com.reece.platform.eclipse.model.XML.common.LineItemPrice;
import com.reece.platform.eclipse.model.XML.common.QuantityWrapper;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.*;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@NoArgsConstructor
@XmlType(propOrder = { "lineItemId", "partIdentifiers", "lineItemPrice", "qtyOrdered", "lineItemComment", "userDefinedData" })
public class LineItem {

    public LineItem(LineItemDTO lineItemDTO) {
        this.lineItemPrice = lineItemDTO.getLineItemPrice();

        PartIdentifiers partIdentifiers = new PartIdentifiers();
        if (lineItemDTO.getCustomerPartNumber() != null) {
            CustomerPartNumberList customerPartNumberList = new CustomerPartNumberList();
            customerPartNumberList.setCustomerPartNumber(Collections.singletonList(lineItemDTO.getCustomerPartNumber()));
            partIdentifiers.setCustomerPartNumberList(customerPartNumberList);
        }
        partIdentifiers.setEclipsePartNumber(Integer.parseInt(lineItemDTO.getErpPartNumber()));
        this.partIdentifiers = partIdentifiers;

        QuantityWrapper quantityOrdered = new QuantityWrapper();
        Quantity quantity = new Quantity();
        quantity.setValue(lineItemDTO.getQuantity());
        quantity.setUmqt(lineItemDTO.getUmqty());
        quantity.setUom(lineItemDTO.getUom());
        quantityOrdered.setQuantity(quantity);
        this.qtyOrdered = quantityOrdered;
    }

    @XmlElement(name = "LineItemID")
    private int lineItemId;

    @XmlElement(name = "PartIdentifiers")
    private PartIdentifiers partIdentifiers;

    @XmlElement(name = "LineItemPrice")
    private LineItemPrice lineItemPrice;

    @XmlElement(name = "QtyOrdered")
    private QuantityWrapper qtyOrdered;

    @XmlElement(name = "LineItemCommentList")
    private List<LineItemComment> lineItemComment;

    @XmlElement(name = "UserDefinedData")
    private List<Dictionary<String, String>> userDefinedData;
}
