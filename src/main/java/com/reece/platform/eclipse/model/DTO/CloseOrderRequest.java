package com.reece.platform.eclipse.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CloseOrderRequest {
    private CloseOrder closeOrder;
}