package com.reece.platform.eclipse.dto.inventory;

import lombok.Data;

@Data
public class KourierErrorDTO {

    private Boolean isSuccess;
    private String errorCode;
    private String errorMessage;
}
