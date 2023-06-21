package com.reece.platform.eclipse.model.XML.ProductRequest;

import com.reece.platform.eclipse.model.DTO.ErpUserInformationDTO;
import com.reece.platform.eclipse.model.XML.common.Login;
import com.reece.platform.eclipse.model.XML.common.Security;

import com.reece.platform.eclipse.model.XML.common.SitePass;
import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "IDMS-XML")
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "massProductInquiry" })
public class ProductRequest {

    public ProductRequest() {}

    public ProductRequest(List<String> productIds, Security security) {
        MassProductInquiry massProductInquiry = new MassProductInquiry();

        List<PartIdentifiers> partIdentifiers = new ArrayList<>();
        for (String productId : productIds) {
            PartIdentifiers partIdentifier = new PartIdentifiers();
            partIdentifier.setEclipsePartNumber(productId);
            partIdentifiers.add(partIdentifier);
        }
        PricingBranch pricingBranch = new PricingBranch();
        PartIdentifiersList partIdentifiersList = new PartIdentifiersList();
        partIdentifiersList.setPartIdentifiers(partIdentifiers);

        massProductInquiry.setCalculatePriceData("Yes");
        massProductInquiry.setCalculateAvailabilityData("Yes");

        massProductInquiry.setSecurity(security);
        massProductInquiry.setPartIdentifiersList(partIdentifiersList);
        massProductInquiry.setPricingBranch(pricingBranch);

        this.massProductInquiry = massProductInquiry;
    }

    @XmlElement(name = "MassProductInquiry")
    private MassProductInquiry massProductInquiry;

}

