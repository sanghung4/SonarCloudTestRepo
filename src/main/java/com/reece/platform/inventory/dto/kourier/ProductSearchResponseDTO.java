package com.reece.platform.inventory.dto.kourier;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSearchResponseDTO {

    public List<ProductSearchResponse> prodSearch;
}
