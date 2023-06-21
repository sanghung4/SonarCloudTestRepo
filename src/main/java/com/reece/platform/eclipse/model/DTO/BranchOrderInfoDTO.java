package com.reece.platform.eclipse.model.DTO;

import com.reece.platform.eclipse.model.XML.common.Branch;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BranchOrderInfoDTO {

    public BranchOrderInfoDTO(Branch branch) {
        this.branchName = branch.getBranchName();
        this.streetLineOne = branch.getAddress().getStreetLineOne();
        this.streetLineTwo = branch.getAddress().getStreetLineTwo();
        this.streetLineThree = branch.getAddress().getStreetLineThree();
        this.city = branch.getAddress().getCity();
        this.state = branch.getAddress().getState();
        this.postalCode = branch.getAddress().getPostalCode();
        this.country = branch.getAddress().getCountry();
    }

    private String branchName;
    private String streetLineOne;
    private String streetLineTwo;
    private String streetLineThree;
    private String city;
    private String state;
    private String postalCode;
    private String country;
}
