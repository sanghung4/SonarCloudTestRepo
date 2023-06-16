package com.reece.platform.mincron.dto;

import lombok.Data;

@Data
public class UpdateCountDTO {

    private String branchNum;
    private String countId;
    private String locationId;
    private String tagNum;
    private Integer countedQty;
}
