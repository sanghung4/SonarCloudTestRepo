package com.reece.platform.inventory.external.mincron;

import com.reece.platform.inventory.dto.internal.PostCountDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MincronPostCountDTO {

    private String location;
    private String tag;
    private Integer qty;

    public MincronPostCountDTO(PostCountDTO countDTO) {
        location = countDTO.getLocationNum();
        tag = countDTO.getTag();
        qty = countDTO.getQuantity();
    }
}
