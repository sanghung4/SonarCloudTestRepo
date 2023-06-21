package com.reece.platform.products.model.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContractBranchDTO {

    private String branchNumber;
    private String branchName;
    private AddressDTO addressDTO;
}
