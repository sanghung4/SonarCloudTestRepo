package com.reece.platform.products.model;

import com.reece.platform.products.model.eclipse.ProductResponse.MassProductInquiryResponse.Product.Product;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.val;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceAndAvailability {

    private BigDecimal price;
    private Stock stock;
    private String partNumber;
    private String description;
    private String status;
    private String uom;
    List<String> listIds;

    public PriceAndAvailability(Product eclipseProduct, String selectedBranch, List<String> listIds) {
        price = new BigDecimal(eclipseProduct.getPricing().getCustomerPrice());
        stock = new Stock();
        partNumber = eclipseProduct.getPartIdentifiers().getEclipsePartNumber();
        description = eclipseProduct.getDescription();
        status = eclipseProduct.getStatus();
        uom = eclipseProduct.getPricing().getQuantity().getUom();
        this.listIds = listIds;
        val otherStock = new ArrayList<StoreStock>();

        val branchId = Optional
            .ofNullable(selectedBranch)
            .orElseGet(() -> {
                if (eclipseProduct.getPricing().getPricingBranch() != null) {
                    return eclipseProduct.getPricing().getPricingBranch().getBranch().getBranchId();
                }

                return "";
            });

        for (val branchAvailability : eclipseProduct.getAvailabilityList().getBranchAvailabilityList()) {
            val storeStock = new StoreStock(branchAvailability, null);
            if (branchId.equals(branchAvailability.getBranch().getBranchId())) {
                stock.setHomeBranch(storeStock);
            } else {
                otherStock.add(storeStock);
            }
        }

        stock.setOtherBranches(otherStock);
    }
}
