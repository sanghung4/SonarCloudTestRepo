package com.reece.platform.products.model.eclipse.PreviouslyPurchasedProduct;

import com.reece.platform.products.model.DTO.PaginationResponseDTO;
import java.util.List;
import lombok.Data;

@Data
public class PreviouslyPurchasedEclipseProductResponse {

    private List<PreviouslyPurchasedProduct> products;
    private PaginationResponseDTO pagination;
}
