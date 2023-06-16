package com.reece.platform.mincron.dto;

import com.reece.platform.mincron.dto.kerridge.ValidateCountResponseDTO;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CountDTO {

    private String branchNumber;
    private String countId;
    private String branchName;

    public CountDTO(ValidateCountResponseDTO responseDTO, String countId) {
        this.branchNumber = responseDTO.getBranch();
        this.branchName = responseDTO.getBrname();
        this.countId = countId;
    }
}
