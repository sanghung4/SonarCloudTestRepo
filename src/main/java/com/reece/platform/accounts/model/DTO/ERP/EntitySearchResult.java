package com.reece.platform.accounts.model.DTO.ERP;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntitySearchResult {

    private Boolean isBillTo;
    private String name;
    private String id;
    private String homeBranch;
    private String erpName;
}
