package com.reece.platform.inventory.exception;

import lombok.Value;

@Value
public class VarianceNotFoundDTO {

    Boolean isSuccess;
    String errorCode;
    String errorMessage;
}
