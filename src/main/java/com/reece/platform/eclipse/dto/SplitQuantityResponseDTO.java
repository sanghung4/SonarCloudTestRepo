package com.reece.platform.eclipse.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SplitQuantityResponseDTO {

    private String productId;
    private Boolean isSplit;
    private String orderId;
    private SerialListDTO invalidSerialNums;
    private Boolean successStatus;
    private String errorMessage;
    private String errorCode;
}
