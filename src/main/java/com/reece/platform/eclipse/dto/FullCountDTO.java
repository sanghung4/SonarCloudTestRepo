package com.reece.platform.eclipse.dto;

import com.reece.platform.eclipse.dto.inventory.KourierFullCountDTO;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class FullCountDTO {

    private Integer countId;
    private String countDescription;
    private String branchId;
    private String createdAt;
    private List<CountProductDTO> products;

    public FullCountDTO(KourierFullCountDTO countDTO) {
        countId = countDTO.getErpcountID();
        countDescription = countDTO.getCountDesc();
        branchId = countDTO.getErpBranchNum();
        createdAt = countDTO.getCreateDate();

        products = new ArrayList<>();
        for (var product : countDTO.getProducts()) {
            products.add(new CountProductDTO(product));
        }
    }
}
