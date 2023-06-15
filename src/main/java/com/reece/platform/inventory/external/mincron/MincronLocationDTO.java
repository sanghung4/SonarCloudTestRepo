package com.reece.platform.inventory.external.mincron;

import java.util.List;
import lombok.Data;

@Data
public class MincronLocationDTO {

    private String locationId;
    private int totalQuantity;
    private int itemCount;
    private List<MincronItemDTO> items;
}
