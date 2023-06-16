package com.reece.platform.eclipse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CloseOrderResponseDTO {

    private boolean status;
    private String orderId;
    private String pickerId;
    private String errorCode;
    private String errorMessage;
    private boolean orderLocked;
    private boolean moreToPick;
    private boolean stillPicking;
}
