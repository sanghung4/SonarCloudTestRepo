package com.reece.platform.inventory.model;

import lombok.Data;

import java.util.List;

@Data
public class Stock {
    private StoreStock homeBranch;
    private List<StoreStock> otherBranches;
}
