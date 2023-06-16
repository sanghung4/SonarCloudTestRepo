package com.reece.platform.eclipse.dto.productsearch.kourier;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductKourierSearchResponseDTO {

    private List<ProductKourierSearchResponse> prodSearch;
}
