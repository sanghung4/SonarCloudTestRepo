package com.reece.platform.products.model.DTO;

import com.reece.platform.products.model.eclipse.common.*;
import java.util.*;
import lombok.*;

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
