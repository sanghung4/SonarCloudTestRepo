package com.reece.platform.mincron.model.contracts;

import com.reece.platform.mincron.model.common.ProductLineItemDTO;
import lombok.Data;

import java.util.List;

@Data
public class ContractItemListWrapperDTO {
    private String preStartRow;
    private String totalRows;
    private String rowsReturned;
    private List<ProductLineItemDTO> items;
}
