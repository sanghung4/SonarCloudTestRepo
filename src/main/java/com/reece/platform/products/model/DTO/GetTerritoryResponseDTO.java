package com.reece.platform.products.model.DTO;

import java.util.List;
import lombok.Data;

@Data
public class GetTerritoryResponseDTO {

    private String updateKey;
    private String id;
    private String description;
    private String entityPriority;
    private String productPriority;
    private List<String> branchList;
}
