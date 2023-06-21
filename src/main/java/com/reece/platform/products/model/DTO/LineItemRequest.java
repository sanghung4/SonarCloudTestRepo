package com.reece.platform.products.model.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.reece.platform.products.external.mincron.model.Address;
import lombok.Data;

@Data
public class LineItemRequest {

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Branch {

        private String branchNumber;
        private String branchName;
        private Address addressDTO;
    }

    private String productNumber;
    private String jobNumber;
    private String contractNumber;
    private Branch branch;
}
