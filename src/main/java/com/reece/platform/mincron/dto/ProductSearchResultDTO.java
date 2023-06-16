package com.reece.platform.mincron.dto;

import com.reece.platform.mincron.dto.kerridge.ProductSearchResponseDTO;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductSearchResultDTO {

    private String lastItem;
    private List<ProductSearchResultItemDTO> items;

    public ProductSearchResultDTO(ProductSearchResponseDTO responseDTO) {
        this.lastItem = responseDTO.getLastItem();
        this.items = new ArrayList<>();
        for (var item : responseDTO.getResults()) {
            this.items.add(new ProductSearchResultItemDTO(item));
        }
    }
}
