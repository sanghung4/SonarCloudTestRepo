package com.reece.platform.accounts.model.DTO;

import com.reece.platform.accounts.model.DTO.ERP.EntitySearchResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntitySearchResponseDTO {

    private Boolean isBillTo;
    private String companyName;
    private String branchId;
    private String erpAccountId;
    private String erpName;

    public EntitySearchResponseDTO(EntitySearchResult result) {
        this.branchId = result.getHomeBranch();
        this.isBillTo = result.getIsBillTo();
        this.erpAccountId = result.getId();
        this.erpName = result.getErpName();
        this.companyName = result.getName();
    }
}
