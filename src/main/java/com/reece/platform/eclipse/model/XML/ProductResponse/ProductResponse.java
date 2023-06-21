package com.reece.platform.eclipse.model.XML.ProductResponse;

import com.reece.platform.eclipse.model.XML.ProductResponse.MassProductInquiryResponse.MassProductInquiryResponse;
import lombok.Data;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "IDMS-XML")
@Data
@XmlType(propOrder = { "massProductInquiryResponse" })
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductResponse {

    public ProductResponse() {}

    @XmlElement(name = "MassProductInquiryResponse")
    private MassProductInquiryResponse massProductInquiryResponse;

}
