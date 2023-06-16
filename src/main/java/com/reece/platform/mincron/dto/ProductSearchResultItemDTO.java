package com.reece.platform.mincron.dto;

import com.reece.platform.mincron.dto.kerridge.ProductSearchItemDTO;
import lombok.Data;

@Data
public class ProductSearchResultItemDTO {

    private String prodNum;
    private String prodDesc;
    private String uom;

    public ProductSearchResultItemDTO(ProductSearchItemDTO itemDTO) {
        this.prodNum = itemDTO.getProdNum();
        this.prodDesc = itemDTO.getProddesc();
        this.uom = itemDTO.getUom();
    }
}
