package com.reece.platform.products.model;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stock implements Serializable {

    private StoreStock homeBranch;
    private List<StoreStock> otherBranches;
}
