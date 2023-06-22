package com.reece.platform.mincron.model.contracts;

import com.reece.platform.mincron.model.BranchDTO;
import lombok.Data;

@Data
public class LineItemResponse {
    private String quantityAvailableHomeBranch;
    private String productNumber;
    private BranchDTO branch;
}
