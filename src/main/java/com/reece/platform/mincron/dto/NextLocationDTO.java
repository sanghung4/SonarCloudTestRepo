package com.reece.platform.mincron.dto;

import com.reece.platform.mincron.dto.kerridge.NextLocationResponseDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NextLocationDTO {

    private String locationId;

    public NextLocationDTO(NextLocationResponseDTO responseDTO) {
        this.locationId = responseDTO.getNextloc();
    }
}
