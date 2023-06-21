package com.reece.platform.eclipse.model.XML.ProductResponse.MassProductInquiryResponse;

import com.reece.platform.eclipse.model.XML.ProductResponse.MassProductInquiryResponse.Product.Product;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "products" })
public class ProductList {
    @XmlElement(name = "Product")
    private List<Product> products;
}
