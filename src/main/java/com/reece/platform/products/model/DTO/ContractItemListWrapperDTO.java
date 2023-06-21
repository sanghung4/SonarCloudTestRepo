package com.reece.platform.products.model.DTO;

import java.util.List;
import lombok.Data;

@Data
public class ContractItemListWrapperDTO {

    private String preStartRow;
    private String totalRows;
    private String rowsReturned;
    private List<ProductLineItemDTO> items;
}
