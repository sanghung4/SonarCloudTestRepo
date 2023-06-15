package com.reece.platform.inventory.dto.kourier;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSearchResponse {

    public String errorCode;
    public String errorMessage;
    public int productIdCount;
    public List<Product> products;
}
