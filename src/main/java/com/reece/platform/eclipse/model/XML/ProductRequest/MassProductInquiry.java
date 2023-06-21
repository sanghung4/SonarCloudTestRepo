package com.reece.platform.eclipse.model.XML.ProductRequest;

import com.reece.platform.eclipse.model.XML.common.Security;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "security", "partIdentifiersList", "pricingBranch", "calculatePriceData", "calculateAvailabilityData" })
public class MassProductInquiry {

    @XmlElement(name = "Security")
    private Security security;

    @XmlElement(name = "PartIdentifiersList")
    private PartIdentifiersList partIdentifiersList;

    @XmlElement(name = "PricingBranch")
    private PricingBranch pricingBranch;

    @XmlElement(name = "CalculatePriceData")
    private String calculatePriceData;

    @XmlElement(name = "CalculateAvailabilityData")
    private String calculateAvailabilityData;

}
