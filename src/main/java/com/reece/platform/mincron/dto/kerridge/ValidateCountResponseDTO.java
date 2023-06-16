package com.reece.platform.mincron.dto.kerridge;

import lombok.Data;

@Data
public class ValidateCountResponseDTO extends MincronErrorDTO {

    private String branch;
    private String brname;
}
