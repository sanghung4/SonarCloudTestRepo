package com.reece.platform.products.model.DTO;

import lombok.Data;

@Data
public class SubmitOrderReviewRequestDTO {

    private ContractAddItemToCartRequestDTO addItemsToCart;
    private CreateCartRequestDTO createCartRequest;
}
