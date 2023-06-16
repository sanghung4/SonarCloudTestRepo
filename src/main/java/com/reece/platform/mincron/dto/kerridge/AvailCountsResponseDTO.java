package com.reece.platform.mincron.dto.kerridge;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AvailCountsResponseDTO extends MincronErrorDTO {

    private int NumCounts;
    private List<CountInfoDTO> countinfo;
}
