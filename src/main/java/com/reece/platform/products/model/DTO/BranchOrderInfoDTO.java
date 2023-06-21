package com.reece.platform.products.model.DTO;

import com.reece.platform.products.branches.model.DTO.BranchResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BranchOrderInfoDTO {

    private String branchName;
    private String streetLineOne;
    private String streetLineTwo;
    private String streetLineThree;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private String branchPhone;
    private String branchHours;
}
