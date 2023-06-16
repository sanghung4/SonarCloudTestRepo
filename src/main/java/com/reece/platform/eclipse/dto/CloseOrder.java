package com.reece.platform.eclipse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CloseOrder {

    private String orderId;
    private String pickerId;
}
