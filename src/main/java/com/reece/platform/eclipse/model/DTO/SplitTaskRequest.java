package com.reece.platform.eclipse.model.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SplitTaskRequest {
    String orderId;
    String toteId;
    String pickerId;
    Integer qty;
    String productId;
    String location;
    String serialNums;
}
