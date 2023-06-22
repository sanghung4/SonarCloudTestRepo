package com.reece.platform.mincron.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BranchDTO {
    private String branchNumber;
    private String branchName;
    private AddressDTO addressDTO;
}
