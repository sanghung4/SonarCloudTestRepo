package com.reece.platform.mincron.dto;

import com.reece.platform.mincron.dto.kerridge.AvailCountsResponseDTO;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class MincronCountsDTO {

    private boolean moreThan100Counts;
    private List<MincronCountSummaryDTO> counts;

    public MincronCountsDTO(AvailCountsResponseDTO responseDTO) {
        moreThan100Counts = responseDTO.getNumCounts() > 100;
        counts = new ArrayList<>();

        for (var count : responseDTO.getCountinfo()) {
            var countInfo = new MincronCountSummaryDTO(
                count.getBrid(),
                count.getCycle(),
                count.getNumitems(),
                count.getCntdate()
            );
            counts.add(countInfo);
        }
    }
}
