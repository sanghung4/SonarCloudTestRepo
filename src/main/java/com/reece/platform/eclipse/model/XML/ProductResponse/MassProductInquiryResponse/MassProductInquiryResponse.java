package com.reece.platform.eclipse.model.XML.ProductResponse.MassProductInquiryResponse;

import com.reece.platform.eclipse.model.XML.common.StatusResult;
import com.reece.platform.eclipse.model.XML.ProductResponse.MassProductInquiryResponse.Product.Product;
import lombok.Data;

import javax.xml.bind.annotation.*;

@Data
@XmlType(propOrder = { "sessionId", "productList", "statusResult" })
@XmlAccessorType(XmlAccessType.FIELD)
public class MassProductInquiryResponse {

    @XmlElement(name = "SessionID")
    private String sessionId;

    @XmlElement(name = "ProductList")
    private ProductList productList;

    @XmlElement(name = "StatusResult")
    private StatusResult statusResult;

}
