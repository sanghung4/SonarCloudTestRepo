package com.reece.platform.mincron.dto.kerridge;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MincronErrorDTO {

    public Boolean isSuccess;
    public ErrorDTO error;
}
