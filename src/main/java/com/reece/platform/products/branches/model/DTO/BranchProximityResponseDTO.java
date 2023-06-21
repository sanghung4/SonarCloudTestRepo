package com.reece.platform.products.branches.model.DTO;

import java.util.List;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BranchProximityResponseDTO {

    private List<BranchWithDistanceResponseDTO> branchList;
    private BranchesRequestDTO inputLocation;
}
