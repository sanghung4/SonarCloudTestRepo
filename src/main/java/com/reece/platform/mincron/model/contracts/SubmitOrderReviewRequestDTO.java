package com.reece.platform.mincron.model.contracts;

import lombok.Data;

@Data
public class SubmitOrderReviewRequestDTO {
    private ContractAddItemToCartRequestDTO addItemsToCart;
    private CreateCartRequestDTO createCartRequest;
}
