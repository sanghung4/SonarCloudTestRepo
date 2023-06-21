package com.reece.platform.products.model;

import com.reece.platform.products.branches.model.DTO.BranchWithDistanceResponseDTO;
import com.reece.platform.products.model.DTO.*;
import com.reece.platform.products.model.eclipse.ProductResponse.MassProductInquiryResponse.Product.AvailabilityList.*;
import java.io.Serializable;
import lombok.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data model for product stock data
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StoreStock implements Serializable {

    private String branchName;
    private int availability;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String zip;
    private String phone;
    private String branchId;
    private Float distanceToBranch;

    public StoreStock(BranchAvailability branchAvailability, BranchWithDistanceResponseDTO branch) {
        this.branchName = branchAvailability.getBranch().getBranchName();
        this.branchId = branchAvailability.getBranch().getBranchId();
        this.availability =
            branchAvailability.getNowQuantity().getQuantity().getQuantity() != null
                ? branchAvailability.getNowQuantity().getQuantity().getQuantity()
                : 0;
        if (branch != null) {
            this.address1 = branch.getAddress1();
            this.address2 = branch.getAddress2();
            this.city = branch.getCity();
            this.state = branch.getState();
            this.zip = branch.getZip();
            this.phone = branch.getPhone();
            this.distanceToBranch = branch.getDistanceToBranch();
        }
    }
}
