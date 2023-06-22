package com.reece.platform.mincron.model.contracts;

import lombok.Data;

import java.util.List;

@Data
public class ItemWrapperDTO {
    private List<LineItemRequest> items;
}
