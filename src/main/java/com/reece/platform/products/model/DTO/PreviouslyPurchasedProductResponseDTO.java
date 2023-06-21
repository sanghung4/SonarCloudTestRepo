package com.reece.platform.products.model.DTO;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PreviouslyPurchasedProductResponseDTO implements Serializable {

    private List<PreviouslyPurchasedProductDTO> products;
    private PaginationResponseDTO pagination;
}
