package com.reece.platform.eclipse.dto;

import lombok.Data;

@Data
public class SplitTaskResponse {

    private String productId;
    private Boolean isSplit;
    private String orderId;
    private String location;
    private String invalidSerialNums;
    private Boolean successStatus;
    private String errorMessage;
    private String errorCode;
}
