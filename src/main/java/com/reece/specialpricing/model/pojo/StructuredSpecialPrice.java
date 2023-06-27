package com.reece.specialpricing.model.pojo;

import com.reece.specialpricing.postgres.SpecialPrice;
import lombok.Data;

import java.util.List;

@Data
public class StructuredSpecialPrice {
    private String productId;
    private String customerId;
    private String branch;
    private String customerDisplayName;
    private String imageUrl;
    private String manufacturer;
    private String displayName;
    private String manufacturerReferenceNumber;
    private String salesperson;
    private String priceLine;
    private int customerSalesQuantity;
    private List<StructuredPrice> prices;
    private String territory;

    public StructuredSpecialPrice(SpecialPrice specialPrice){
        this.productId = specialPrice.getProductId();
        this.customerId = specialPrice.getCustomerId();
        this.branch = specialPrice.getBranch();
        this.customerDisplayName = specialPrice.getCustomerDisplayName();
        this.imageUrl = specialPrice.getImageUrl();
        this.manufacturer = specialPrice.getManufacturer();
        this.displayName = specialPrice.getDisplayName();
        this.manufacturerReferenceNumber = specialPrice.getManufacturerReferenceNumber();
        this.salesperson = specialPrice.getSalesperson();
        this.priceLine = specialPrice.getPriceLine();
        this.customerSalesQuantity = specialPrice.getCustomerSalesQuantity();
        this.prices = List.of(
                new StructuredPrice("current", specialPrice.getCurrentPrice(), "USD", "Current Price"),
                new StructuredPrice("typical", specialPrice.getTypicalPrice(), "USD", "Typical Price"),
                new StructuredPrice("rateCard", specialPrice.getRateCardPrice(), "USD", "Rate Card"),
                new StructuredPrice("recommended", specialPrice.getRecommendedPrice(), "USD", "Recommended"),
                new StructuredPrice("competitiveMarket", specialPrice.getCompetitiveMarketPrice(), "USD", "Competitive Market"),
                new StructuredPrice("customerSales", specialPrice.getCustomerSalesPrice(), "USD", "Customer Sales"),
                new StructuredPrice("standardCost", specialPrice.getStandardCost(), "USD", "Standard Cost")
        );
        this.territory = specialPrice.getTerritory();
    }
}
