package com.reece.platform.mincron.model.contracts;

import com.reece.platform.mincron.model.BranchDTO;
import lombok.Data;

@Data
public class LineItemRequest {
    private String productNumber;
    private String jobNumber;
    private String contractNumber;
    private BranchDTO branch;
}
