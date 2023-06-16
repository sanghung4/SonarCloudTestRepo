package com.reece.platform.mincron.dto.kerridge;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CountLocationResponseDTO extends MincronErrorDTO {

    private Integer totalqty;
    private Integer itemcount;
    private List<CountLocationItemDTO> results;
}
