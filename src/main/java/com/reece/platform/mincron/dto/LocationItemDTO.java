package com.reece.platform.mincron.dto;

import com.reece.platform.mincron.dto.kerridge.CountLocationItemDTO;
import lombok.Data;

@Data
public class LocationItemDTO {

    private String itemNum;
    private String prodNum;
    private String prodDesc;
    private String catalogNum;
    private String tagNum;
    private String uom;

    public LocationItemDTO(CountLocationItemDTO locationDTO) {
        this.itemNum = locationDTO.getItemNumber();
        this.prodNum = locationDTO.getProductNumber();
        this.prodDesc = locationDTO.getProductDescription();
        this.catalogNum = locationDTO.getCategoryNumber();
        this.tagNum = locationDTO.getTagNumber();
        this.uom = locationDTO.getUom();
    }
}
