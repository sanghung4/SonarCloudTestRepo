package com.reece.platform.inventory.dto;

import lombok.Data;

import java.util.List;

@Data
public class GetBranchesByProximityResponseDTO {

    private List<BranchWithDistanceResponseDTO> branchList;

    @Data
    public static class BranchWithDistanceResponseDTO {

        private String branchId;
        private String name;
        private String entityId;
        private String address1;
        private String address2;
        private String city;
        private String state;
        private String zip;
        private String phone;
        private Float distanceToBranch;
    }
}