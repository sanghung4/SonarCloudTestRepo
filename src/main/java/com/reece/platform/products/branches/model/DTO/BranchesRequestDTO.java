package com.reece.platform.products.branches.model.DTO;

import com.reece.platform.products.branches.model.enums.MileRadiusEnum;
import lombok.*;

@NoArgsConstructor
@Data
public class BranchesRequestDTO {

    private float latitude;
    private float longitude;
    private int count;
    private String territory;
    private MileRadiusEnum branchSearchRadius;
    private Boolean isStoreFinder;
    private Boolean isShoppable;
}
