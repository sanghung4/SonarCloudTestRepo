package com.reece.platform.products.model.DTO;

import java.util.List;
import lombok.Data;

@Data
public class ProductDetailWrapperDTO {

    private List<LineItemResponse> items;
}
