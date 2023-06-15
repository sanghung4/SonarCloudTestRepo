package com.reece.platform.inventory.external.eclipse;

import java.util.List;
import lombok.Data;

@Data
public class EclipseCountDTO {

    private Integer countId;
    private String countDescription;
    private String branchId;
    private String createdAt;
    private List<EclipseProductDTO> products;
}
