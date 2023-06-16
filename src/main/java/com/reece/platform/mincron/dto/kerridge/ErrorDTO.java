package com.reece.platform.mincron.dto.kerridge;

import lombok.Data;

@Data
public class ErrorDTO {

    private String code;
    private String message;
    private String details;
}
