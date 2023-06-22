package com.reece.platform.mincron.model.contracts;

import com.reece.platform.mincron.model.common.CartLineItemDTO;
import lombok.Data;

import java.util.List;

@Data
public class ContractAddItemToCartRequestDTO {
    private List<CartLineItemDTO> items;
}
