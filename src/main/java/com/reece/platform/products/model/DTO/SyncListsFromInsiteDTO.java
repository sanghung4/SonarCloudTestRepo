package com.reece.platform.products.model.DTO;

import com.reece.platform.products.model.ErpEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SyncListsFromInsiteDTO {

    private String erpAccountId;
    private ErpEnum erpEnum;
}
