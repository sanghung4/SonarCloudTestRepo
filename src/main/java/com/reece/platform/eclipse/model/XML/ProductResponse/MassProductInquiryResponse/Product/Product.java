package com.reece.platform.eclipse.model.XML.ProductResponse.MassProductInquiryResponse.Product;

import com.reece.platform.eclipse.model.XML.ProductResponse.MassProductInquiryResponse.Product.AvailabilityList.AvailabilityList;
import com.reece.platform.eclipse.model.XML.ProductResponse.MassProductInquiryResponse.Product.Pricing.Pricing;
import com.reece.platform.eclipse.model.XML.ProductResponse.MassProductInquiryResponse.Product.RichContentList.RichContentList;
import com.reece.platform.eclipse.model.XML.common.PartIdentifiers;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@Data
@XmlType(propOrder = { "description", "alternateDescription", "partIdentifiers", "availabilityList", "uomList" , "pricingUOM", "pricing", "volume", "weight", "buyLine", "priceLine", "status", "indexType", "richContentList" })
@XmlAccessorType(XmlAccessType.FIELD)
public class Product {

    @XmlElement(name = "Description")
    private String description;

    @XmlElement(name = "AlternateDescription")
    private String alternateDescription;

    @XmlElement(name = "PartIdentifiers")
    private PartIdentifiers partIdentifiers;

    @XmlElement(name = "AvailabilityList")
    private AvailabilityList availabilityList;

    @XmlElement(name = "UOMList")
    private List<Uom> uomList;

    @XmlElement(name = "PricingUOM")
    private PricingUOM pricingUOM;

    @XmlElement(name = "Pricing")
    private Pricing pricing;

    // I'm not sure how to get the attribute "unit" to show for weight and volume
    @XmlElement(name = "Volume")
    private String volume;

    @XmlElement(name = "Weight")
    private String weight;

    @XmlElement(name = "BuyLine")
    private String buyLine;

    @XmlElement(name = "PriceLine")
    private String priceLine;

    @XmlElement(name = "Status")
    private String status;

    @XmlElement(name = "IndexType")
    private String indexType;

    @XmlElement(name = "RichContentList")
    private RichContentList richContentList;
}
