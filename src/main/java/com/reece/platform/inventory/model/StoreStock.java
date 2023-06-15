package com.reece.platform.inventory.model;

import com.reece.platform.inventory.dto.GetBranchesByProximityResponseDTO;
import lombok.Data;

@Data
public class StoreStock {

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

    public StoreStock(
            BranchAvailability branchAvailability,
            GetBranchesByProximityResponseDTO.BranchWithDistanceResponseDTO branch
    ) {
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